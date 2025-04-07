package com.iqeq.dto.common;

import com.iqeq.enums.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class SearchResponse extends BasePaginationDto {
    private List<?> data;
    private List<ColumnDto> columns;


    public static SearchResponse build(List<?> data, Page<?> page, boolean isDashboard) {
        SearchResponse searchResponse = new SearchResponse();
        searchResponse.setData(data);
        searchResponse.setColumns(isDashboard ? getDashboardColumns() : getAllColumns());
        searchResponse.setNumber(page.getNumber());
        searchResponse.setSize(page.getSize());
        searchResponse.setNumberOfElements(page.getNumberOfElements());
        searchResponse.setTotalPages(page.getTotalPages());
        searchResponse.setTotalElements(page.getTotalElements());
        return searchResponse;
    }

    private static List<ColumnDto> getDashboardColumns() {
        List<ColumnDto> dashboardColumns = new ArrayList<>();
        dashboardColumns.add(new ColumnDto(Column.DOCUMENT_NAME.getLabel(), Column.DOCUMENT_NAME.getValue()));
        dashboardColumns.add(new ColumnDto(Column.UPLOADED_DATE.getLabel(), Column.UPLOADED_DATE.getValue()));
        dashboardColumns.add(new ColumnDto(Column.PRIORITY_ORDER.getLabel(), Column.PRIORITY_ORDER.getValue()));
        dashboardColumns.add(new ColumnDto(Column.STATUS.getLabel(), Column.STATUS.getValue()));
        return dashboardColumns;
    }

    private static List<ColumnDto> getAllColumns() {
        List<ColumnDto> allColumns = new ArrayList<>();
        allColumns.add(new ColumnDto(Column.DOCUMENT_NAME.getLabel(), Column.DOCUMENT_NAME.getValue()));
        allColumns.add(new ColumnDto(Column.BATCH_NAME.getLabel(), Column.BATCH_NAME.getValue()));
        allColumns.add(new ColumnDto(Column.INVOICING_TYPE.getLabel(), Column.INVOICING_TYPE.getValue()));
        allColumns.add(new ColumnDto(Column.PRIORITY_ORDER.getLabel(), Column.PRIORITY_ORDER.getValue()));
        allColumns.add(new ColumnDto(Column.UPLOADED_DATE.getLabel(), Column.UPLOADED_DATE.getValue()));
        allColumns.add(new ColumnDto(Column.PROCESSING_DATE.getLabel(), Column.PROCESSING_DATE.getValue()));
        allColumns.add(new ColumnDto(Column.PROCESSING_TIME.getLabel(), Column.PROCESSING_TIME.getValue()));
        allColumns.add(new ColumnDto(Column.ACCURACY.getLabel(), Column.ACCURACY.getValue()));
        allColumns.add(new ColumnDto(Column.STATUS.getLabel(), Column.STATUS.getValue()));
        return allColumns;
    }
}