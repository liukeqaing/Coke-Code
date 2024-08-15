package com.atguigu.tingshu.album.service;

import com.tencentcloudapi.vod.v20180717.models.MediaInfo;

public interface VodService {

    /**
     * 查询文件的详细信息
     *
     * @param fileId
     * @return
     */
    public MediaInfo getFileDetailFromTx(String fileId);

}
