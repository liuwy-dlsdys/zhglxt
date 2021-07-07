package com.zhglxt.demo.excel.Listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.google.common.collect.Lists;
import com.zhglxt.demo.excel.template.ImportSysUserTemplate;

import java.util.List;
import java.util.Map;

public class ExcelListener extends AnalysisEventListener<ImportSysUserTemplate> {

    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 3000;
    List<ImportSysUserTemplate> list = Lists.newArrayList();
    private static int count = 1;

    long begin;
    long end;

    @Override
    public void invoke(ImportSysUserTemplate data, AnalysisContext analysisContext) {
        list.add(data);
        count++;
        //每3000条数据进行保存操作
        if (list.size() >= BATCH_COUNT) {
            saveData(count); //保存到数据库操作
            list.clear();
        }
    }

    //读取表头内容
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        begin = System.currentTimeMillis();
        System.out.println("表头： " + headMap);
    }

    //读取完成之后
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        end = System.currentTimeMillis();
        long total = end - begin;
        if (total > 1000) {
            System.out.println("数据读取完毕，耗时：" + (total / 1000 % 60) + "秒");
        } else {
            System.out.println("数据读取完毕，耗时：" + total + "豪秒");
        }

        System.out.println("{ " + count + " }条数据，开始存储数据库！" + list.size());
    }

    /**
     * 加上存储数据库
     */
    private void saveData(int count) {
        System.out.println("{ " + count + " }条数据，开始存储数据库！" + list.size());
        System.out.println("存储数据库成功！");
    }
}
