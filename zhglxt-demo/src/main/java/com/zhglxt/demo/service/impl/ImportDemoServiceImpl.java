package com.zhglxt.demo.service.impl;

import com.zhglxt.demo.entity.ImportSysUser;
import com.zhglxt.demo.mapper.ImportDemoMapper;
import com.zhglxt.demo.service.IImportDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ImportDemoServiceImpl implements IImportDemoService {

    @Autowired
    private ImportDemoMapper importDemoMapper;

    @Override
    public List<ImportSysUser> selectUserList(ImportSysUser user) {
        return importDemoMapper.selectUserList(user);
    }
}
