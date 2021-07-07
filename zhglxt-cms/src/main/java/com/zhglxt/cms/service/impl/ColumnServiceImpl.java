package com.zhglxt.cms.service.impl;


import com.google.common.collect.Maps;
import com.zhglxt.cms.entity.Column;
import com.zhglxt.cms.mapper.ColumnMapper;
import com.zhglxt.cms.service.IColumnService;
import com.zhglxt.cms.service.ISiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 栏目 业务层处理
 * @author liuwy
 * @date 2019/12/3
 */
@Service
public class ColumnServiceImpl implements IColumnService {
    @Autowired
    private ColumnMapper columnMapper;

    @Autowired
    private ISiteService siteService;

    /**
     * 查询-官网首页-栏目列表（官网后台管理树形栏目需要的数据结构）
     */
    @Override
    public List<Column> selectColumnList(Map<String, Object> paramMap) {
        List<Column> columns = columnMapper.selectColumnList(paramMap);
        if(!CollectionUtils.isEmpty(columns)){
            //根据父id进行分组
            Map<String, List<Column>> columnIds = columns.stream().collect(Collectors.groupingBy(Column::getpId));

            //遍历设置子栏目
            for (Column column: columns) {
                //map中存在key,则设置到父栏目中
                if(columnIds.containsKey(column.getId())){
                    column.setColumnChilds(columnIds.get(column.getId()));
                }
            }

            /*------------------------------------以下为非外部链接树型数据的处理逻辑-------------------------------------*/
            //子节点为非外部链接，父节点为外部链接时的树型逻辑
            Map<String, List<Column>> outIds = columns.stream().collect(Collectors.groupingBy(Column::getId));

            //所有栏目数据
            Map<String, Object> outLinkMap = Maps.newHashMap();
            outLinkMap.put("siteId", siteService.selectOneSite().getId());
            List<Column> columnAll = columnMapper.selectColumnList(outLinkMap);
            Map<String, List<Column>> columnAllMap = columnAll.stream().collect(Collectors.groupingBy(Column::getId));

            Set<String> keys = columnIds.keySet();
            for (String key: keys) {
                if(!"0".equals(key)){//排除顶级栏目
                    if(!outIds.containsKey(key)){
                        //父节点不是外部链接，但存在子节点为非外部链接的数据时
                        Column outParent = columnAllMap.get(key).get(0);
                        //把非外部链接的子节点数据，设置到该父节点的子类中
                        outParent.setColumnChilds(columnIds.get(key));
                        //重新添加到新的栏目列表
                        columns.add(outParent);
                        //移除没有父节点的非外部链接的子节点数据
                        columns.remove(columnIds.get(key));
                    }
                }
            }
            /*--------------------------------------------------end---------------------------------------------------*/

        }
        return columns;
    }

    /**
     * 查询-官网首页-栏目列表（前端官网首页下拉栏目需要的数据结构）
     */
    @Override
    public List<Column> selectCMSColumnList(Map<String, Object> paramMap) {
        //官网首页栏目数据
        List<Column> columns = columnMapper.selectColumnList(paramMap);
        if(!CollectionUtils.isEmpty(columns)){
            Map<String, Object> columnMap = Maps.newHashMap();
            columnMap.put("siteId", siteService.selectOneSite().getId());
            //查询出该站点的所有栏目数据
            List<Column> allColumns = selectColumnList(columnMap);
            //根据父id进行分组
            Map<String, List<Column>> columnIds = allColumns.stream().collect(Collectors.groupingBy(Column::getpId));
            //遍历设置子栏目
            for (Column column: columns) {
                //map中存在key,则设置到父栏目中
                if(columnIds.containsKey(column.getId())){
                    //过滤掉不显示的数据
                    List<Column> columnChilds = columnIds.get(column.getId()).stream().filter(e -> !"1".equals(e.getDisplay())).collect(Collectors.toList());
                    //设置子栏目
                    column.setColumnChilds(columnChilds);
                }
            }
        }
        return columns;
    }

    @Override
    @Transactional
    public int insertColumnMenu(Map<String, Object> paramMap) {
        return columnMapper.insertColumnMenu(paramMap);
    }

    @Override
    @Transactional
    public int deleteColumn(Map<String, Object> paramMap) {
        return columnMapper.deleteColumn(paramMap);
    }

    @Override
    @Transactional
    public int updateColumn(Map<String, Object> paramMap) {
        return columnMapper.updateColumn(paramMap);
    }
}
