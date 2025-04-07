package com.iqeq.helper;

import com.iqeq.exception.CustomException;
import com.iqeq.util.CommonConstants;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class DocumentExcelFileWriter implements DocumentFileWriter{

    @Override
    public InputStreamResource export(List<?> data, List<String> headers, List<String> fields, Class<?> classType, String filename) throws CustomException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(filename);
            int rowIndex = 0;
            CellStyle headerStyle = workbook.createCellStyle();
            headerRowFont(workbook, headerStyle);

            Row row = sheet.createRow(rowIndex++);
            int count = 0;
            for (String header:headers) {
                Cell cell = row.createCell(count++);
                cell.setCellValue(header);
                cell.setCellStyle(headerStyle);
            }
            //Build documents rows
            if(data!=null && !data.isEmpty()) {
                for(Object object:data) {
                    row = sheet.createRow(rowIndex++);
                    count = 0;
                    addCellValue(fields, classType, object, row, count);
                }
            }
            workbook.write(out);
            return new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));
        }catch (IOException | IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            throw new CustomException(500, "Failed to download the Document export "+e.getMessage());
        }
    }

    private void addCellValue(List<String> fields, Class<?> classType, Object object, Row row, int count) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        for (String field : fields) {
            Object value = new PropertyDescriptor(field, classType).getReadMethod().invoke(object);
            if (Objects.isNull(value)) {
                value = "";
            }
            try {
                if (value instanceof Integer val) {
                    row.createCell(count++).setCellValue(val);
                } else if (value instanceof Double val) {
                    row.createCell(count++).setCellValue(val);
                } else if (value instanceof String val) {
                    row.createCell(count++).setCellValue(val);
                } else if (value instanceof Boolean val) {
                    row.createCell(count++).setCellValue(val);
                } else if (value instanceof Date) {
                    row.createCell(count++).setCellValue(convertDateToSimpleDateFormat(value, field));
                }else if (value instanceof LocalDateTime) {
                    row.createCell(count++).setCellValue(convertDateToSimpleDateFormat(value, field));
                }else{
                    row.createCell(count++).setCellValue(value+"");
                }
            }catch (Exception e){
                row.createCell(count++).setCellValue(value+"");
            }
        }
    }

    private String convertDateToSimpleDateFormat(Object value, String field) {
        SimpleDateFormat dateFormat = field.equalsIgnoreCase("createdDate") ?
                CommonConstants.SIMPLE_DATE_FORMAT : CommonConstants.SIMPLE_DATE_TIME_FORMAT;
        String convertedDateFormat = "";
        if (value instanceof Date) {
            convertedDateFormat =  dateFormat.format((Date) value);
        } else if (value instanceof LocalDateTime) {
            convertedDateFormat = ((LocalDateTime) value).format(CommonConstants.DATE_TIME_FORMATTER);
        } else if (value instanceof String) {
            LocalDateTime dateTime = LocalDateTime.parse((String) value, CommonConstants.DATE_TIME_FORMATTER);
            convertedDateFormat = dateTime.format(CommonConstants.DATE_TIME_FORMATTER);
        }
        return convertedDateFormat;
    }

    private void headerRowFont(Workbook workbook, CellStyle headerStyle) {
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }
}
