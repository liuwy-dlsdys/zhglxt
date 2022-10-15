package com.zhglxt.oa.service;

import com.zhglxt.oa.entity.Leave;

import java.util.List;
import java.util.Map;

/**
 * @Description 请假业务接口层
 * @Author liuwy
 * @Date 2019/9/27
 **/
public interface ILeaveService {

    /**
     * 获取请假列表
     *
     * @param paramMap
     * @return int
     **/
    List<Leave> getLeaveList(Map<String, Object> paramMap);

    /**
     * 更新请假信息-ids
     *
     * @param paramsToMap
     * @return int
     **/
    int updateLeaveInFoByIds(Map paramsToMap);

    /**
     * 更新请假信息-list
     *
     * @param paramsToMap
     * @return int
     **/
    int updateLeaveInFoToList(Map paramsToMap);

    /**
     * 更新请假信息-array
     *
     * @param paramsToMap
     * @return int
     **/
    int updateLeaveInFoToArray(Map paramsToMap);
}
