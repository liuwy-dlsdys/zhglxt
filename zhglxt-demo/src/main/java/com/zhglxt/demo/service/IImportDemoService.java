package com.zhglxt.demo.service;

import com.zhglxt.demo.entity.ImportSysUser;

import java.util.List;

public interface IImportDemoService {
    public List<ImportSysUser> selectUserList(ImportSysUser user);
}
