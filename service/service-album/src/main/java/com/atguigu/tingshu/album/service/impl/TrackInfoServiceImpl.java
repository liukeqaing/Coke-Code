package com.atguigu.tingshu.album.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.tingshu.album.config.VodConstantProperties;
import com.atguigu.tingshu.album.mapper.TrackInfoMapper;
import com.atguigu.tingshu.album.mapper.TrackStatMapper;
import com.atguigu.tingshu.album.service.TrackInfoService;
import com.atguigu.tingshu.album.service.VodService;
import com.atguigu.tingshu.common.constant.SystemConstant;
import com.atguigu.tingshu.common.execption.GuiguException;
import com.atguigu.tingshu.common.util.UploadFileUtil;
import com.atguigu.tingshu.model.album.TrackInfo;
import com.atguigu.tingshu.model.album.TrackStat;
import com.atguigu.tingshu.vo.album.TrackInfoVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcloud.vod.VodUploadClient;
import com.qcloud.vod.model.VodUploadRequest;
import com.qcloud.vod.model.VodUploadResponse;
import com.tencentcloudapi.vod.v20180717.models.MediaBasicInfo;
import com.tencentcloudapi.vod.v20180717.models.MediaInfo;
import com.tencentcloudapi.vod.v20180717.models.MediaMetaData;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@SuppressWarnings({"unchecked", "rawtypes"})
public class TrackInfoServiceImpl extends ServiceImpl<TrackInfoMapper, TrackInfo> implements TrackInfoService {

	@Autowired
	private TrackInfoMapper trackInfoMapper;

	@Autowired
	private VodConstantProperties vodConstantProperties;

	@Autowired
	private VodUploadClient vodUploadClient;
	/**
	 * 上传声音
	 *
	 * @return
	 */
	@Override
	@SneakyThrows
	public Object uploadTrack(MultipartFile file) {
		//将文件存储到临时目录
		String tempPath = UploadFileUtil.uploadTempPath(vodConstantProperties.getTempPath(), file);
		//构造上传请求对象
		VodUploadRequest request = new VodUploadRequest();
		request.setMediaFilePath(tempPath);
		//调用上传
		VodUploadResponse response = vodUploadClient.upload("ap-guangzhou", request);
		//返回结果初始化
		JSONObject result = new JSONObject();
		//获取文件的id
		String fileId = response.getFileId();
		result.put("mediaFileId",fileId);
		//获取文件的播放地址
		String mediaUrl = response.getMediaUrl();
		result.put("mediaUrl",mediaUrl);
		//返回
		return result;
	}
	@Autowired
	private VodService vodService;
	/**
	 * 保存声音
	 * @param trackInfoVo
	 */
	@Override
	public void saveTrackInfo(TrackInfoVo trackInfoVo) {
		//TODO 获取用户的id
		Long userId = 1L;
		//保存声音表的数据
		TrackInfo trackInfo = new TrackInfo();
		//属性转移
		BeanUtils.copyProperties(trackInfoVo,trackInfo);
		//补全数据
		trackInfo.setUserId(userId);
		//找腾讯云点播获取文件的详细信息
		MediaInfo mediaInfo = vodService.getFileDetailFromTx(trackInfo.getMediaFileId());
		//基础信息
		MediaBasicInfo basicInfo = mediaInfo.getBasicInfo();
		//元信息
		MediaMetaData metaData = mediaInfo.getMetaData();
		//媒体信息
		trackInfo.setMediaDuration(new BigDecimal(metaData.getDuration().toString()));//时长
		trackInfo.setMediaFileId(trackInfoVo.getMediaFileId()); //唯一标识
		trackInfo.setMediaUrl(basicInfo.getMediaUrl());//播放地址
		trackInfo.setMediaType(basicInfo.getType());//类型
		trackInfo.setSource("0601");
		trackInfo.setStatus("0501");
		trackInfo.setIsOpen(null);
		trackInfo.setMediaSize(metaData.getSize());
		int insert = trackInfoMapper.insert(trackInfo);
		if (insert <= 0){
			throw new GuiguException(201,"新增声音失败");
		}
		//初始化声音的统计信息
		initTrackStat(trackInfo.getId());
	}
	private TrackStatMapper trackStatMapper;
	/**
	 * 初始化声音的统计信息
	 * @param id
	 */
	private void initTrackStat(Long id) {
		//初始化
		TrackStat trackStat = new TrackStat();
		trackStat.setStatNum(0);
		trackStat.setTrackId(id);
		//设置类型:播放量
		trackStat.setStatType(SystemConstant.TRACK_STAT_PLAY);
		trackStatMapper.insert(trackStat);
		//设置类型：收藏量
		trackStat.setId(null);
		trackStat.setStatType(SystemConstant.ALBUM_STAT_SUBSCRIBE);
		trackStatMapper.insert(trackStat);
		//设置类型：点赞量
		trackStat.setId(null);
		trackStat.setStatType(SystemConstant.ALBUM_STAT_BROWSE);
		trackStatMapper.insert(trackStat);
		//设置类型：评论数
		trackStat.setId(null);
		trackStat.setStatType(SystemConstant.ALBUM_STAT_BROWSE);
		trackStatMapper.insert(trackStat);

	}

}

















