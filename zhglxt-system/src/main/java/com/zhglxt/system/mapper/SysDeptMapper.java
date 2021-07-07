package com.zhglxt.system.mapper;

import com.zhglxt.common.core.entity.sys.SysDept;
import com.zhglxt.system.entity.SysDeptUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 部门管理 数据层
 *
 * @author ruoyi
 */
@Mapper
@Component
public interface SysDeptMapper {
    /**
     * 查询部门人数
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int selectDeptCount(SysDept dept);

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int checkDeptExistUser(String deptId);

    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    public List<SysDept> selectDeptList(SysDept dept);

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int deleteDeptById(String deptId);

    /**
     * 新增部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int insertDept(SysDept dept);

    /**
     * 修改部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int updateDept(SysDept dept);

    /**
     * 修改子元素关系
     *
     * @param depts 子元素
     * @return 结果
     */
    public int updateDeptChildren(@Param("depts") List<SysDept> depts);

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    public SysDept selectDeptById(String deptId);

    /**
     * 校验部门名称是否唯一
     *
     * @param deptName 部门名称
     * @param parentId 父部门ID
     * @return 结果
     */
    public SysDept checkDeptNameUnique(@Param("deptName") String deptName, @Param("parentId") String parentId);

    /**
     * 根据角色ID查询部门
     *
     * @param roleId 角色ID
     * @return 部门列表
     */
    public List<String> selectRoleDeptTree(String roleId);

    /**
     * 修改所在部门正常状态
     *
     * @param deptIds 部门ID组
     */
    public void updateDeptStatusNormal(String[] deptIds);

    /**
     * 根据ID查询所有子部门
     *
     * @param deptId 部门ID
     * @return 部门列表
     */
    public List<SysDept> selectChildrenDeptById(String deptId);

    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    public int selectNormalChildrenDeptById(String deptId);

    /**
     * @Description 查询所有部门数据
     * @Author liuwy
     * @Date 2021/5/14
     */
    public List<SysDept> selectDeptAll();

    /**
     * @Description 查询部门用户数据
     * @Author liuwy
     * @Date 2021/5/14
     */
    public List<SysDeptUser> selectDeptUser();

    /**
     * @Description 根据部门id查询选中的部门
     * @Author liuwy
     * @Date 2021/6/27
     **/
    public List<SysDept> selectCheckedDeptByDeptId(List<String> list);

    /**
     * @Description 根据部门父id查询选中的部门
     * @Author liuwy
     * @Date 2021/6/27
     **/
    public List<SysDept> selectCheckedDeptByParentId(List<String> list);

}
