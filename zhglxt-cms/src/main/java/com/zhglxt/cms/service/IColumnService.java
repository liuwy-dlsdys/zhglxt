package com.zhglxt.cms.service;

import com.zhglxt.cms.entity.Column;

import java.util.List;
import java.util.Map;

/**
 * 栏目 业务层
 * @author liuwy
 * @date 2019/12/3
 */
public interface IColumnService {
    /**
     * 查询栏目列表（后台管理树形栏目需要的数据结构）
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
     * 查询栏目列表（前端栏目下拉显示需要的数据结构）
     */
    public List<Column> selectCMSColumnList(Map<String, Object> paramMap);
}
