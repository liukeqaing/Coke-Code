package com.atguigu.tingshu.album.service.impl;

import com.atguigu.tingshu.album.mapper.AlbumAttributeValueMapper;
import com.atguigu.tingshu.album.mapper.AlbumInfoMapper;
import com.atguigu.tingshu.album.mapper.AlbumStatMapper;
import com.atguigu.tingshu.album.service.AlbumInfoService;
import com.atguigu.tingshu.common.constant.SystemConstant;
import com.atguigu.tingshu.common.execption.GuiguException;
import com.atguigu.tingshu.model.album.AlbumAttributeValue;
import com.atguigu.tingshu.model.album.AlbumInfo;
import com.atguigu.tingshu.model.album.AlbumStat;
import com.atguigu.tingshu.vo.album.AlbumAttributeValueVo;
import com.atguigu.tingshu.vo.album.AlbumInfoVo;
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
}


















