package com.atguigu.tingshu.album.service;

import com.atguigu.tingshu.model.album.AlbumInfo;
import com.atguigu.tingshu.query.album.AlbumInfoQuery;
import com.atguigu.tingshu.vo.album.AlbumInfoVo;
import com.atguigu.tingshu.vo.album.AlbumListVo;
import com.atguigu.tingshu.vo.album.TrackInfoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface AlbumInfoService extends IService<AlbumInfo> {
    /**
     * 新增专辑
     * @param albumInfoVo
     */
    void saveAlbumInfo(AlbumInfoVo albumInfoVo);

    Page<AlbumListVo> findUserAlbumPage(Long page, Long size, AlbumInfoQuery albumInfoQuery);

    /**
     * 删除专辑信息
     * @param id
     */
    void removeAlbumInfo(Long id);

    /**
     * 专辑修改数据回显
     *
     * @param id
     * @return
     */
    AlbumInfo getAlbumInfo(Long id);

    /**
     * 修改专辑信息
     * @param albumInfoVo
     * @param id
     * @return
     */
    void updateAlbumInfo(AlbumInfoVo albumInfoVo, Long id);

    /**
     * 声音新增时查询用户专辑列表
     * @return
     */
    List<AlbumInfo> findUserAllAlbumList();

}




















