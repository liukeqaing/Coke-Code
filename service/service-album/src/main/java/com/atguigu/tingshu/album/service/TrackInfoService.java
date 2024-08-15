package com.atguigu.tingshu.album.service;

import com.atguigu.tingshu.model.album.TrackInfo;
import com.atguigu.tingshu.vo.album.TrackInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

public interface TrackInfoService extends IService<TrackInfo> {

    /**
     * 上传声音
     *
     * @return
     */
    Object uploadTrack(MultipartFile file);

    /**
     * 保存声音
     * @param trackInfoVo
     */
    void saveTrackInfo(TrackInfoVo trackInfoVo);
}
