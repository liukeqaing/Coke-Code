package com.atguigu.tingshu.album.service;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.tingshu.model.album.BaseAttribute;
import com.atguigu.tingshu.model.album.BaseCategory1;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/***
 * 分类操作的接口类
 */
public interface BaseCategoryService extends IService<BaseCategory1> {

    /**
     * 查询所有的一级二级三级分类，并且关联对应关系
     *
     * @return
     */
    List<JSONObject> getIndexCategory();

    /**
     * 查询标签数据
     * @param category3Id
     * @return
     */
    List<BaseAttribute> findAttribute(Long category3Id);

}
