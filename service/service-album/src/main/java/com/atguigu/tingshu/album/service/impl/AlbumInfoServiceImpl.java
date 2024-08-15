package com.atguigu.tingshu.album.service.impl;

import com.atguigu.tingshu.album.mapper.*;
import com.atguigu.tingshu.album.service.AlbumInfoService;
import com.atguigu.tingshu.common.constant.SystemConstant;
import com.atguigu.tingshu.common.execption.GuiguException;
import com.atguigu.tingshu.model.album.*;
import com.atguigu.tingshu.query.album.AlbumInfoQuery;
import com.atguigu.tingshu.vo.album.AlbumAttributeValueVo;
import com.atguigu.tingshu.vo.album.AlbumInfoVo;
import com.atguigu.tingshu.vo.album.AlbumListVo;
import com.atguigu.tingshu.vo.album.TrackInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@SuppressWarnings({"unchecked", "rawtypes"})
public class AlbumInfoServiceImpl extends ServiceImpl<AlbumInfoMapper, AlbumInfo> implements AlbumInfoService {

	@Autowired
	private AlbumInfoMapper albumInfoMapper;

	/**
	 * 新增专辑
	 *
	 * @param albumInfoVo
	 */
	@Override
	public void saveAlbumInfo(AlbumInfoVo albumInfoVo) {
		//专辑的DO初始化
		AlbumInfo albumInfo = new AlbumInfo();
		BeanUtils.copyProperties(albumInfoVo,albumInfo);
		//TODO 补全属性
		albumInfo.setUserId(1L);
		albumInfo.setStatus("0301");
		//新增专辑数据
		if (albumInfoMapper.insert(albumInfo) <= 0) {
			//新增失败
			throw new GuiguException(201,"新增专辑失败");
		}
		//新增成功后，获取专辑的主键
		Long albumId = albumInfo.getId();
		//新增专辑的标签数据
		saveAlbumAttrInfo(albumId,albumInfoVo.getAlbumAttributeValueVoList());
		//初始化专辑的统计数据
		initAlbumStat(albumId);
	}


	@Autowired
	private AlbumStatMapper albumStatMapper;

	/**
	 * 初始化专辑的统计数据
	 * @param albumId
	 */
	private void initAlbumStat(Long albumId) {
		//初始化Do
		AlbumStat albumStat = new AlbumStat();
		albumStat.setAlbumId(albumId);
		albumStat.setStatNum(0);
		//播放量
		albumStat.setStatType(SystemConstant.ALBUM_STAT_PLAY);
		//新增
		albumStatMapper.insert(albumStat);
		//订阅量
		albumStat.setId(null);
		albumStat.setStatType(SystemConstant.ALBUM_STAT_SUBSCRIBE);
		albumStatMapper.insert(albumStat);
		//购买量
		albumStat.setId(null);
		albumStat.setStatType(SystemConstant.ALBUM_STAT_BROWSE);
		albumStatMapper.insert(albumStat);
		//评论数
		albumStat.setId(null);
		albumStat.setStatType(SystemConstant.ALBUM_STAT_COMMENT);
		albumStatMapper.insert(albumStat);

	}

	@Autowired
	private AlbumAttributeValueMapper albumAttributeValueMapper;
	/**
	 * 新增专辑的标签数据
	 * @param albumId
	 * @param albumAttributeValueVoList
	 */
	private void saveAlbumAttrInfo(Long albumId
			, List<AlbumAttributeValueVo> albumAttributeValueVoList) {
		//遍历新增
		albumAttributeValueVoList.stream().forEach(albumAttributeValueVo ->{
			//初始化DO
			AlbumAttributeValue albumAttributeValue = new AlbumAttributeValue();
			BeanUtils.copyProperties(albumAttributeValueVo,albumAttributeValue);
			albumAttributeValue.setAlbumId(albumId);
			//新增
			if (albumAttributeValueMapper.insert(albumAttributeValue) <= 0){
				throw new GuiguException(201,"新增专辑标签失败");
			}
		});
	}

	/* *
	 * 分页条件查询专辑的列表
	 * @param page
	 * @param size
	 * @param albumInfoQuery
	 * @return
	 */
	@Override
	public Page<AlbumListVo> findUserAlbumPage(Long page
			, Long size
			, AlbumInfoQuery albumInfoQuery) {
		return albumInfoMapper.selectAlbumListVoByQuery(albumInfoQuery
				,new Page<AlbumListVo>(page,size));
	}

	/**
	 * 删除专辑信息
	 *
	 * @param id
	 */
	@Override
	public void removeAlbumInfo(Long id) {
		// TODO 获取用户的ID
		Long userId = 1L;

		//删除专辑表的数据
		int i = albumInfoMapper.delete(
				new LambdaQueryWrapper<AlbumInfo>()
				.eq(AlbumInfo::getUserId,userId)
						.eq(AlbumInfo::getId,id));
		if (i < 0){
			throw new GuiguException(201,"删除专辑数据失败");
		}
		//统计表的数据
		 i = albumStatMapper.delete(
				new LambdaQueryWrapper<AlbumStat>().eq(AlbumStat::getAlbumId, id));
		if (i < 0){
			throw new GuiguException(201,"统计表数据删除失败");
		}
		//专辑的标签表数据删除
		i = albumAttributeValueMapper.delete(
				new LambdaQueryWrapper<AlbumAttributeValue>().eq(AlbumAttributeValue::getAlbumId, id));
		if (i < 0){
			throw new GuiguException(201,"标签表数据删除失败");
		}

	}

	/**
	 * 专辑修改数据回显
	 *
	 * @param id
	 * @return
	 */
	@Override
	public AlbumInfo getAlbumInfo(Long id) {
		// TODO 获取用户的ID
		Long userId = 1L;
		//查询专辑的数据
		AlbumInfo albumInfo = albumInfoMapper.selectOne(
				new LambdaQueryWrapper<AlbumInfo>()
						.eq(AlbumInfo::getId, id)
						.eq(AlbumInfo::getUserId, userId));
		if (albumInfo == null){
			throw new GuiguException(201,"专辑不存在");
		}
		//查询专辑的标签数据
		List<AlbumAttributeValue> albumAttributeValues = albumAttributeValueMapper.selectList(
				new LambdaQueryWrapper<AlbumAttributeValue>()
						.eq(AlbumAttributeValue::getAlbumId, id));

		//保存专辑标签的数据
		albumInfo.setAlbumAttributeValueVoList(albumAttributeValues);
		return albumInfo;
	}

	/**
	 * 修改专辑信息
	 *
	 * @param albumInfoVo
	 * @param id
	 * @return
	 */
	@Override
	public void updateAlbumInfo(AlbumInfoVo albumInfoVo, Long id) {
		 //TODO 获取用户的id
		Long userId = 1L;
		//创建do
		AlbumInfo albumInfo = new AlbumInfo();
		BeanUtils.copyProperties(albumInfoVo,albumInfo);
		//填充userId
		albumInfo.setUserId(userId);
		//将专辑的数据进行修改album_info
		int update = albumInfoMapper.update(albumInfo, new LambdaQueryWrapper<AlbumInfo>()
				.eq(AlbumInfo::getId, id)
				.eq(AlbumInfo::getUserId, userId));
		if (update < 0){
			throw new GuiguException(201,"修改专辑数据失败");
		}
		//删除旧的标签数据，新增新的标签数据
		int i = albumAttributeValueMapper.delete(
				new LambdaQueryWrapper<AlbumAttributeValue>()
						.eq(AlbumAttributeValue::getAlbumId, id));
		if (i < 0){
			throw new GuiguException(201,"删除标签数据失败");
		}

		//新增标签
		saveAlbumAttrInfo(id,albumInfoVo.getAlbumAttributeValueVoList());
	}

	/**
	 * 声音新增时查询用户专辑列表
	 *
	 * @return
	 */
	@Override
	public List<AlbumInfo> findUserAllAlbumList() {
		//TODO 获取用户的id
		Long userId = 1L;
		//查询这个用户可用的全部的声音：默认分页
		return albumInfoMapper.selectPage(new Page<>(1,10)
				,new LambdaQueryWrapper<AlbumInfo>()
				.eq(AlbumInfo::getUserId,userId)
								.orderByDesc(AlbumInfo::getId)).getRecords();

	}


}


















