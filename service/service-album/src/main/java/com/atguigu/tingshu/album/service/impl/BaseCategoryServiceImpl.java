package com.atguigu.tingshu.album.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.tingshu.album.mapper.BaseAttributeMapper;
import com.atguigu.tingshu.album.mapper.BaseCategory1Mapper;
import com.atguigu.tingshu.album.mapper.BaseCategoryViewMapper;
import com.atguigu.tingshu.album.service.BaseCategoryService;
import com.atguigu.tingshu.model.album.BaseAttribute;
import com.atguigu.tingshu.model.album.BaseCategory1;
import com.atguigu.tingshu.model.album.BaseCategoryView;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/***
 * 分类操作的实现类
 */
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class BaseCategoryServiceImpl extends ServiceImpl<BaseCategory1Mapper, BaseCategory1> implements BaseCategoryService {

	@Autowired
	private BaseCategoryViewMapper baseCategoryViewMapper;

	@Autowired
	private BaseAttributeMapper baseAttributeMapper;


	/**
	 * 查询所有的一级二级三级分类，并且关联对应关系
	 *
	 * @return
	 */
	@Override
	public List<JSONObject> getIndexCategory() {

		//查询所有的一级二级三级分类的数据：分类的视图
		List<BaseCategoryView> baseCategory1Views = baseCategoryViewMapper.selectList(null);
		//构建一对多的关系：一级和二级的一对多  二级和三级的一对多---分桶思维
		Map<Long, List<BaseCategoryView>> collect1Map = baseCategory1Views.stream()   //key就是
				.collect(Collectors.groupingBy(BaseCategoryView::getCategory1Id));  //groupingBy:分组  根据getCategory1Id进行分桶
		//遍历将每个一级分类中对应桶里面的二级分类数据进行分桶
		return collect1Map.entrySet().stream().map(category1->{
			JSONObject c1Json = new JSONObject();
			//获取key
			Long category1Id = category1.getKey();
			c1Json.put("categoryId",category1Id);
			//获取value:每个一级分类对应的全部的二级分类和三级分类
			List<BaseCategoryView> baseCategory2Views = category1.getValue();
			//获取一级分类的名字
			 String category1Name = baseCategory2Views.get(0).getCategory1Name();
			 c1Json.put("categoryName",category1Name);
			//再根据二级分类id进行分组
			Map<Long, List<BaseCategoryView>> collect2Map = baseCategory2Views.stream()
					.collect(Collectors.groupingBy(BaseCategoryView::getCategory2Id));
			//遍历获取每个二级分类的信息同时获取每个二级分类对应的三级分类的数据
			List<JSONObject> c2JsonList = collect2Map.entrySet().stream().map(category2 -> {
				JSONObject c2Json = new JSONObject();
				//获取key
				Long category2Id = category2.getKey();
				c2Json.put("categoryId", category2Id);
				//获取value:不重复的全部三级分类
				List<BaseCategoryView> baseCategory3Views = category2.getValue();
				//二级分类的名字
				String category2Name = baseCategory3Views.get(0).getCategory2Name();
				c2Json.put("categoryName", category2Name);

				//获取三级分类的id和名字
				List<JSONObject> c3JsonList = baseCategory3Views.stream().map(category3 -> {    //map和forEach遍历的区别：// forEach是遍历，map是返回值
					JSONObject c3Json = new JSONObject();
					Map<String, Object> m = new HashMap<>();
					//获取三级分类的id
					Long category3Id = category3.getCategory3Id();
					c3Json.put("categoryId", category3Id);
					//获取三级分类的名字
					String category3Name = category3.getCategory3Name();
					c3Json.put("categoryName", category3Name);
					//返回
					return c3Json;
				}).collect(Collectors.toList());
				//这个二级分类对应的三级分类的列表
				c2Json.put("categoryChild", c3JsonList);
				//返回
				return c2Json;
			}).collect(Collectors.toList());
			//保存这个一级分类对应的二级分类的列表
			c1Json.put("categoryChild",c2JsonList);
			//返回
			return c1Json;
		}).collect(Collectors.toList());
	}

	/**
	 * 查询标签数据
	 *
	 * @param category3Id
	 * @return
	 */
	@Override
	public List<BaseAttribute> findAttribute(Long category3Id) {
		return baseAttributeMapper.selectBaseAttributeByCategory1Id(category3Id);
	}
}



















