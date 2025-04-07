package com.iqeq.dto.common;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.iqeq.enums.DocumentStatus;
import com.iqeq.exception.CustomException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
public class SearchRequestDto {

	private Long documentTypeId;
	private LocalDate startDate;
	private LocalDate endDate;
	private String status;
	private Long documentBatchId;
	private int page = 0;
    private int size = 10;
    private String sortField;
    private String sortOrder;
    private String name;
	private Long filterBy;
	private String batchName;
	private Boolean isDashboard = false;

	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate createdDate;

	public void validSearchRequest(SearchRequestDto searchRequestDto) throws CustomException {
		if (searchRequestDto == null) {
			throw new CustomException((HttpStatus.BAD_REQUEST).value(), "missing parameters",
					new Object[]{"Request"});
		}
		if (searchRequestDto.getSize() <= 0) {
			throw new CustomException((HttpStatus.BAD_REQUEST).value(), "Page size should be positive number");
		}
		if (searchRequestDto.getPage() < 0) {
			throw new CustomException((HttpStatus.BAD_REQUEST).value(), "Page number should not be negative number");
		}

		if (Objects.nonNull(status) && !"ALL".equals(status)) {
			try {
				DocumentStatus.valueOf(status); // Validate status against enum
			} catch (IllegalArgumentException e) {
				throw new CustomException(HttpStatus.BAD_REQUEST.value(), "Invalid status value: " + searchRequestDto.getStatus());
			}
		}

		if (searchRequestDto.getStartDate() != null && searchRequestDto.getEndDate() != null) {
			if (searchRequestDto.getStartDate().isAfter(searchRequestDto.getEndDate())) {
				throw new CustomException(HttpStatus.BAD_REQUEST.value(), "Start date cannot be after end date");
			}
		}
		if (searchRequestDto.getDocumentTypeId() != null && searchRequestDto.getDocumentTypeId() <= 0) {
			throw new CustomException(HttpStatus.BAD_REQUEST.value(), "Document type ID should be a positive number");
		}
		if (searchRequestDto.getSortOrder() != null &&
				(!searchRequestDto.getSortOrder().equalsIgnoreCase("ASC") &&
						!searchRequestDto.getSortOrder().equalsIgnoreCase("DESC"))) {
			throw new CustomException(HttpStatus.BAD_REQUEST.value(), "Sort order must be either 'ASC' or 'DESC'");
		}
	}
}
