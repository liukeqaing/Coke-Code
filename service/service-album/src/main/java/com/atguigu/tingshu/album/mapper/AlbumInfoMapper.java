package com.atguigu.tingshu.album.mapper;

import com.atguigu.tingshu.model.album.AlbumInfo;
import com.atguigu.tingshu.query.album.AlbumInfoQuery;
import com.atguigu.tingshu.vo.album.AlbumListVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AlbumInfoMapper extends BaseMapper<AlbumInfo> {

    /**
     * 分页条件查询专辑的列表
     * @param albumInfoQuery
     * @param page
     * @return
     */
    Page<AlbumListVo> selectAlbumListVoByQuery(@Param("vo") AlbumInfoQuery albumInfoQuery,
                                               Page<AlbumListVo> page);
}
