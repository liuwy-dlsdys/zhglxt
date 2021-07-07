package com.zhglxt.oa.service;

import com.zhglxt.common.core.entity.Ztree;
import com.zhglxt.oa.entity.Notify;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * @Description 通知通告业务接口层
 * @Author liuwy
 * @Date 2020/12/19
 */
public interface INotifyService {

    /**
     * 查询公告列表
     *
     * @param notify 公告信息
     * @return 公告集合
     */
    public List<Notify> selectNotifyList(Notify notify);

    /**
     * 新增公告
     *
     * @param notify 公告信息
     * @return 结果
     */
    public int insertNotify(Notify notify);

    /**
     * @Description 根据id查询通告
     * @Author liuwy
     * @Date 2020/12/19
     */
    public Notify selectNotifyById(Notify notify);

    /**
     * @Description 更新通告
     * @Author liuwy
     * @Date 2020/12/19
     */
    public int updateNotify(Notify notify);

    /**
     * @Description 删除通告
     * @Author liuwy
     * @Date 2020/12/20
     */
    public int deleteNotifyByIds(String ids);

    /**
     * @Description 根据通告id和用户id更新阅读状态和阅读时间
     * @Author liuwy
     * @Date 2020/12/20
     */
    public void updateNotifyRecordByNotifyIdAndUserId(@Param("notifyId") String notifyId, @Param("userId") String userId);

    /**
     * @Description 查询部门人员列表
     * @Author liuwy
     * @Date 2021/5/13
     */
    List<Ztree> deptUserData(Notify notify);
}
