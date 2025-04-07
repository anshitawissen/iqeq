package com.iqeq.dto.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BasePaginationDto {

	private int numberOfElements = 0;
	private Long totalElements = 0L;
	private int totalPages = 0;
	private int number = 0;
	private int size = 0;

}
