package com.atguigu.tingshu.album.mapper;

import com.atguigu.tingshu.model.album.BaseAttribute;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BaseAttributeMapper extends BaseMapper<BaseAttribute> {

    /**
     * 根据一级分类id查询标签数据
     * @return
     *
     * @Param 是 MyBatis 框架中的一个注解，用于在 SQL 映射文件中给参数命名，以便在 SQL 语句中引用它们。这个注解特别有用，
     * 当你传递多个参数给一个 SQL 方法时，可以使用 @Param 来明确标识每个参数，从而在 SQL 语句中更容易地引用它们。
     */
     List<BaseAttribute> selectBaseAttributeByCategory1Id(@Param("category1Id") Long category1Id);
}
