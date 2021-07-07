package com.zhglxt.activiti.util;

import com.zhglxt.activiti.config.ActivitiConfig;
import com.zhglxt.activiti.entity.Act;
import com.zhglxt.common.core.entity.sys.SysUser;
import com.zhglxt.common.util.Encodes;
import com.zhglxt.common.util.StringUtils;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 流程工具
 * @author liuwy
 * @version 2019/9/29
 */
public class ActUtils {

    /**
     * 定义流程定义KEY，必须以“PD_”开头
     * 组成结构：string[]{"流程标识","业务主表表名"}
     */
    public static final String[] PD_LEAVE = new String[]{"leave", "oa_leave"};

    /**
     * -每次新增一个流程，需要添加一个String[]{"流程定义id（必须与流程图的一致且唯一）","业务关联的数据库表（与数据库的业务表名称一致）"}
     *
     * —— 请假流程
     */
    //public static final String[] PD_TEST_LEAVE_AUDIT = new String[]{"test_leave", "oa_test_leave"};

    /**
     * 获取流程表单URL
     *
     * @param formKey
     * @param act     表单传递参数
     * @return
     */
    public static String getFormUrl(String formKey, Act act) {

        StringBuilder formUrl = new StringBuilder();

        String formServerUrl = new ActivitiConfig().getFormServerUrl();
        if (StringUtils.isBlank(formServerUrl)) {
            formUrl.append("");
        } else {
            formUrl.append(formServerUrl);
        }

        formUrl.append(formKey).append(formUrl.indexOf("?") == -1 ? "?" : "&");
        formUrl.append("act.taskId=").append(act.getTaskId() != null ? act.getTaskId() : "");
        formUrl.append("&act.taskName=").append(act.getTaskName() != null ? Encodes.urlEncode(act.getTaskName()) : "");
        formUrl.append("&act.taskDefKey=").append(act.getTaskDefKey() != null ? act.getTaskDefKey() : "");
        formUrl.append("&act.procInsId=").append(act.getProcInsId() != null ? act.getProcInsId() : "");
        formUrl.append("&act.procDefId=").append(act.getProcDefId() != null ? act.getProcDefId() : "");
        formUrl.append("&act.status=").append(act.getStatus() != null ? act.getStatus() : "");
        formUrl.append("&id=").append(act.getBusinessId() != null ? act.getBusinessId() : "");

        return formUrl.toString();
    }


    public static UserEntity toActivitiUser(SysUser user) {
        if (user == null) {
            return null;
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getLoginName());
        userEntity.setFirstName(user.getUserName());
        userEntity.setLastName(StringUtils.EMPTY);
        userEntity.setPassword(user.getPassword());
        userEntity.setEmail(user.getEmail());
        userEntity.setRevision(1);
        return userEntity;
    }

    public static GroupEntity toActivitiGroup(Object roleKeys) {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setRevision(1);
        groupEntity.setType("assignment");
        groupEntity.setId(roleKeys.toString());
        return groupEntity;
    }

    public static List<Group> toActivitiGroups(Set roleKeys) {
        List<Group> groups = new ArrayList<>();
        for (Object code : roleKeys) {
            GroupEntity groupEntity = toActivitiGroup(code);
            groups.add(groupEntity);
        }
        return groups;
    }

}
