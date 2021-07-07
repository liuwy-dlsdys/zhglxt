package com.zhglxt.oa.mapper;

import com.zhglxt.oa.entity.Notify;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @Description 通知通告 数据访问层
 * @Author liuwy
 * @Date 2020/12/19
 */
@Mapper
@Component
public interface NotifyMapper {
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
     */
    public int insertNotify(Notify notify);

    /**
     * @Description 更新
     * @Author liuwy
     * @Date 2020/12/19
     */
    public int updateNotify(Notify notify);

    /**
     * @Description 删除公告
     * @Author liuwy
     * @Date 2020/12/20
     */
    public int deleteNotifyByIds(String[] ids);
}
