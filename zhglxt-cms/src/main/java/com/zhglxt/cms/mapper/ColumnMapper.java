package com.zhglxt.cms.mapper;

import com.zhglxt.cms.entity.Column;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 栏目 数据层
 *
 * @author liuwy
 * @date 2019/12/3
 */
@Mapper
@Component
public interface ColumnMapper {
    /**
     * 根据栏目列表
     */
    public List<Column> selectColumnList(Map<String, Object> paramMap);

    /**
     * 新增栏目菜单
     */
    public int insertColumnMenu(Map<String, Object> paramMap);

    /**
     * 删除栏目菜单
     */
    public int deleteColumn(Map<String, Object> paramMap);

    /**
     * 修改栏目菜单
     */
    public int updateColumn(Map<String, Object> paramMap);

    /**
     * 根据站点id删除该站点的所有栏目数据
     */
    void deleteColumnsBySiteIds(String[] ids);
}
