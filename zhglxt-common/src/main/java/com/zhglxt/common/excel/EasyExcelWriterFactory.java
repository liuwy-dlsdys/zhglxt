package com.zhglxt.common.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import java.io.OutputStream;
import java.io.File;
import java.util.List;

/**
 * @Description EasyExcelFactory
 * @Author liuwy
 * @Date 2021/6/17
 * @Version 1.0
 **/
public class EasyExcelWriterFactory {
    private int sheetNo = 0;
    private ExcelWriter excelWriter = null;

    public EasyExcelWriterFactory(OutputStream outputStream) {
        excelWriter = EasyExcel.write(outputStream).build();
    }

    public EasyExcelWriterFactory(File file) {
        excelWriter = EasyExcel.write(file).build();
    }

    public EasyExcelWriterFactory(String filePath) {
        excelWriter = EasyExcel.write(filePath).build();
    }

    /**
     * 链式模板表头写入
     * @param headClazz 表头格式
     * @param data 数据 List<ExcelModel> 或者List<List<Object>>
     * @return
     */
    public  EasyExcelWriterFactory writeModel(Class headClazz, List data, String sheetName){
        excelWriter.write(data, EasyExcel.writerSheet(this.sheetNo++, sheetName).head(headClazz).build());
        return this;
    }

    /**
     * 链式自定义表头写入
     * @param head
     * @param data 数据 List<ExcelModel> 或者List<List<Object>>
     * @param sheetName
     * @return
     */
    public EasyExcelWriterFactory write(List<List<String>> head, List data, String sheetName){
        excelWriter.write(data, EasyExcel.writerSheet(this.sheetNo++, sheetName).head(head).build());
        return this;
    }

    public void finish() {
        excelWriter.finish();
    }
}
