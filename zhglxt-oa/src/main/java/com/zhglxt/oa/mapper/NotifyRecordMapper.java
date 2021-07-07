package com.zhglxt.oa.mapper;

import com.zhglxt.oa.entity.NotifyRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description 通知通告阅读记录数据访问层
 * @Author liuwy
 * @Date 2020/12/19
 */
@Mapper
@Component
public interface NotifyRecordMapper {

    /**
     * 插入通知记录
     *
     * @param notifyRecordList
     * @return
     */
    public int insertAll(List<NotifyRecord> notifyRecordList);

    /**
     * 根据通知ID删除通知记录
     *
     * @param notifyIds 通知IDs
     * @return
     */
    public int deleteByNotifyId(String[] notifyIds);

    /**
     * @Description 根据通告id查询通告记录数据列表
     * @Author liuwy
     * @Date 2020/12/20
     */
    public List<NotifyRecord> selectByNotify(NotifyRecord notifyRecord);

    /**
     * @Description /根据通告id和用户id更新阅读状态和阅读时间
     * @Author liuwy
     * @Date 2020/12/20
     */
    public void updateNotifyRecordByNotifyIdAndUserId(@Param("notifyId") String notifyId, @Param("userId") String userId);

    /**
     * @Description 获取通告通知消息数
     * @Author liuwy
     * @Date 2020/12/21
     */
    public int getNotifyNumber(NotifyRecord notifyRecord);
}
