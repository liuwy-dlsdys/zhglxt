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
    public List<Leave> getLeaveList(Map<String, Object> paramMap);

    public int updateLeaveInFoByIds(Map paramsToMap);

    public int updateLeaveInFoToList(Map paramsToMap);

    public int updateLeaveInFoToArray(Map paramsToMap);
}
