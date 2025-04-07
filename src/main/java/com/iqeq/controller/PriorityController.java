package com.iqeq.controller;

import com.iqeq.dto.PriorityDto;
import com.iqeq.dto.common.Response;
import com.iqeq.exception.CustomException;
import com.iqeq.service.PriorityService;
import com.iqeq.util.CommonConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PriorityController extends BaseController{

    private final PriorityService priorityService;

    @GetMapping("/priorities")
    public ResponseEntity<Response> getPriorities() {
        log.info("Get priorities controller");
        return buildResponse(HttpStatus.OK, CommonConstants.GET_PRIORITIES_SUCCESS, priorityService.getAllPriority());
    }

    @PostMapping("/priorities")
    public ResponseEntity<Response> createPriority(@Valid @RequestBody PriorityDto priorityDto) throws CustomException {
        log.info("Create priorities controller");
        return buildResponse(HttpStatus.OK, CommonConstants.CREATE_PRIORITIES_SUCCESS, priorityService.createPriority(priorityDto));
    }


}
