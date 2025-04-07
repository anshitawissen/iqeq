package com.iqeq.controller;

import com.iqeq.dto.DataTypeDto;
import com.iqeq.dto.common.Response;
import com.iqeq.exception.CustomException;
import com.iqeq.service.DataTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DataTypeController {

    @Autowired
    private DataTypeService dataTypeService;

    private static final String DATA_TYPE = "dataType";

    @PostMapping("/dataTypes")
    public ResponseEntity<Response> createDataType(@RequestBody DataTypeDto dataTypeDto) throws CustomException {
        DataTypeDto dataTypeCreated = dataTypeService.createDataType(dataTypeDto);

        Map<String, Object> data = new HashMap<>();
        data.put(DATA_TYPE, dataTypeCreated);

        Response rs = new Response();
        rs.setCode(HttpStatus.OK.value());
        rs.setData(data);
        rs.setMessage("DataType created successfully.");
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }

    @PutMapping("/dataTypes/{dataTypeId}")
    public ResponseEntity<Response> updateDataType(@PathVariable Long dataTypeId, @RequestBody DataTypeDto dataTypeDto) {
        DataTypeDto dataTypeUpdated = dataTypeService.updateDataType(dataTypeId, dataTypeDto);

        Map<String, Object> data = new HashMap<>();
        data.put(DATA_TYPE, dataTypeUpdated);

        Response rs = new Response();
        rs.setCode(HttpStatus.OK.value());
        rs.setData(data);
        rs.setMessage("DataTypes updated successfully.");
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }

    @DeleteMapping("/dataTypes/{dataTypeId}")
    public ResponseEntity<Response> deleteDataType(@PathVariable Long dataTypeId) {
        DataTypeDto dataTypeDeleted = dataTypeService.deleteDataType(dataTypeId);

        Map<String, Object> data = new HashMap<>();
        data.put(DATA_TYPE, dataTypeDeleted);

        Response rs = new Response();
        rs.setCode(HttpStatus.OK.value());
        rs.setData(data);
        rs.setMessage("DataType deleted successfully.");
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }

    @GetMapping("/dataTypes/{dataTypeId}")
    public ResponseEntity<Response> getDataTypeById(@PathVariable Long dataTypeId) {
        DataTypeDto dataTypeDto = DataTypeDto.build(dataTypeService.getDataTypeById(dataTypeId));

        Map<String, Object> data = new HashMap<>();
        data.put(DATA_TYPE, dataTypeDto);

        Response rs = new Response();
        rs.setCode(HttpStatus.OK.value());
        rs.setData(data);
        rs.setMessage("DataType fetched successfully.");
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }
}
