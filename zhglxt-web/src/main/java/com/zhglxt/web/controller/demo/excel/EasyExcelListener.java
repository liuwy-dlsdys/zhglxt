package com.zhglxt.web.controller.demo.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.google.common.collect.Lists;
import com.zhglxt.demo.excel.template.ImportSysUserTemplate;
import com.zhglxt.demo.mapper.ImportDemoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @Description EasyExcel监听
 * @Author liuwy
 * @Date 2021/6/17
 * @Version 1.0
 **/
public class EasyExcelListener extends AnalysisEventListener<ImportSysUserTemplate> {
    private static final Logger logger = LoggerFactory.getLogger(EasyExcelListener.class);

    @Autowired
    private ImportDemoMapper importDemoMapper;

    public EasyExcelListener(ImportDemoMapper importDemoMapper) {
        this.importDemoMapper = importDemoMapper;
    }

    /**
     * 每隔3000(设置3000即可，不要设置过大，否则MySQL语法会报错)条，插入一次数据库，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 3000;
    private static int count = 1;
    private final List<ImportSysUserTemplate> list = Lists.newArrayList();

    long begin;
    long end;

    /**
     * 读取表头内容
     * @param headMap
     * @param context
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        begin = System.currentTimeMillis();
        //logger.info("解析到一条头数据:{}", JSON.toJSONString(headMap));
    }

    /**
     * 读取excel内容
     * @param sysUser
     * @param context
     */
    @Override
    public void invoke(ImportSysUserTemplate sysUser, AnalysisContext context) {
        list.add(sysUser);
        count++;
        //每3000条数据进行保存操作
        if (list.size() >= BATCH_COUNT) {
            saveData(count); //保存到数据库操作
            list.clear();
        }
    }

    /**
     * 读取完成之后
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        logger.info("read {} rows", list.size());

        if(list.size()<BATCH_COUNT){
            importDemoMapper.insertBatch(list);
            logger.info("成功存储{}条数据",count-1);
        }

        end = System.currentTimeMillis();
        long total = end - begin;
        if (total > 1000) {
            logger.info("数据存储完毕，耗时：" + (total / 1000 % 60) + "秒");
        } else {
            logger.info("数据存储完毕，耗时：" + total + "豪秒");
        }
    }

    /**
     * 在转换异常 获取其它异常下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行。
     * @param exception
     * @param context
     * @throws Exception
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) {
        logger.error("解析失败，但是继续解析下一行:{}", exception.getMessage());
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException)exception;
            logger.error("第{}行，第{}列解析异常，数据为:{}", excelDataConvertException.getRowIndex(),
                    excelDataConvertException.getColumnIndex(), excelDataConvertException.getCellData());
        }
    }

    public List<ImportSysUserTemplate> getRows() {
        return list;
    }

    /**
     * 加上存储数据库
     */
    private void saveData(int count) {
        logger.info("开始存储第{}条数据", count);
        importDemoMapper.insertBatch(list);
        logger.info("存储数据成功");
    }
}
