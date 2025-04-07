package com.iqeq.util;

import com.iqeq.dto.common.SearchRequestDto;
import com.iqeq.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDate;


public class CommonServiceUtility {

    private CommonServiceUtility() {
    }

    public static HttpServletRequest getCurrentHttpRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes requestAttribute) {
            return requestAttribute.getRequest();
        }
        return null;
    }

    public static String getLoggedInUserEmailId() {
        HttpServletRequest request = getCurrentHttpRequest();
        assert request != null;
        final String authHeader = request.getHeader("Authorization");
        String jwt = authHeader.substring(7);
        JwtService jwtService = new JwtService();
        return jwtService.extractUserEmailForOpenId(jwt);
    }

    public static Sort applySortFilter(SearchRequestDto searchRequestDto) {

        Sort sort = null;

        if (searchRequestDto.getSortField() != null) {
            if(searchRequestDto.getSortField().equalsIgnoreCase("batchName")) {
                searchRequestDto.setSortField("documentBatch.name");
            }
            if (searchRequestDto.getSortOrder().equalsIgnoreCase("ASC")) {
                sort = Sort.by(Sort.Direction.ASC, searchRequestDto.getSortField());
            } else if (searchRequestDto.getSortOrder().equalsIgnoreCase("DESC")) {
                sort = Sort.by(Sort.Direction.DESC, searchRequestDto.getSortField());
            }
        }

        return sort;
    }

    public static void setDateRange(SearchRequestDto searchRequestDto) {
        searchRequestDto.setEndDate(LocalDate.now());
        searchRequestDto.setStartDate(searchRequestDto.getEndDate().minusDays(searchRequestDto.getFilterBy()));
    }

}
