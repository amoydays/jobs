package com.amoydays.jobs.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ExcelUtil {
    /**
     * 用户信息导出类
     *
     * @param response   响应
     * @param fileName   文件名
     * @param columnList 每列的标题名
     * @param dataList   导出的数据
     */
    public static void uploadExcelAboutUser(HttpServletResponse response, String fileName, List<String> columnList, List<List<Object>> dataList) {
        //声明输出流
        OutputStream os = null;
        //设置响应头
        setResponseHeader(response, fileName);
        try {
            //获取输出流
            os = response.getOutputStream();
            //内存中保留1000条数据，以免内存溢出，其余写入硬盘
            SXSSFWorkbook wb = new SXSSFWorkbook(1000);
            //获取该工作区的第一个sheet
            Sheet sheet1 = wb.createSheet("sheet1");
            //这里的A1:H1，表示是从哪里开始，哪里结束这个筛选框
            CellRangeAddress c = CellRangeAddress.valueOf("A1:H1");
            sheet1.setAutoFilter(c);
            int excelRow = 0;
            //创建标题行
            Row titleRow = sheet1.createRow(excelRow++);
            for (int i = 0; i < columnList.size(); i++) {
                //创建该行下的每一列，并写入标题数据
                Cell cell = titleRow.createCell(i);
                cell.setCellValue(columnList.get(i));
            }
            //设置内容行
            if (dataList != null && dataList.size() > 0) {
                //序号是从1开始的
                int count = 1;
                //外层for循环创建行
                for (int i = 0; i < dataList.size(); i++) {
                    Row dataRow = sheet1.createRow(excelRow++);
                    //内层for循环创建每行对应的列，并赋值
                    for (int j = -1; j < dataList.get(0).size(); j++) {//由于多了一列序号列所以内层循环从-1开始
                        Cell cell = dataRow.createCell(j + 1);
                        if (j == -1) {//第一列是序号列，不是在数据库中读取的数据，因此手动递增赋值
                            cell.setCellValue(count++);
                            continue;
                        }
                        String typeName = dataList.get(i).get(j).getClass().getName();
                        if (typeName.contains("Integer")) {//第六列和第七列是数字
                            cell.setCellValue((Integer) dataList.get(i).get(j));
                        } else if (typeName.contains("int")) {
                            cell.setCellValue((Integer) dataList.get(i).get(j));
                        } else if (typeName.contains("Double")) {
                            cell.setCellValue((Double) dataList.get(i).get(j));
                        } else if (typeName.contains("double")) {
                            cell.setCellValue((Double) dataList.get(i).get(j));
                        } else {//其余列是数据列，将数据库中读取到的数据依次赋值
                            cell.setCellValue(dataList.get(i).get(j).toString());
                        }
                    }
                }
            }
            //将整理好的excel数据写入流中
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭输出流
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置浏览器下载响应头
     */
    private static void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 查询结果转换成List<List<String>></String>
     */
    private static List<List<String>> transList(List<T> dataList) {
        List<List<String>> resultList = new ArrayList<>();
        if (dataList != null) {
            Field[] fields = T.class.getDeclaredFields();
            for (T entity : dataList) {
                List<String> entityList = new ArrayList<>();
                for (Field field : fields) {
                    field.setAccessible(true);
                    try {
                        entityList.add(field.get(entity).toString());
                    } catch (IllegalAccessException e) {
                        log.warn("transList数据获取失败，字段名：" + field.getName(), e);
                        throw new RuntimeException(e);
                    }
                }
                resultList.add(entityList);
            }
        }

        return resultList;
    }

}