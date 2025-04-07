package com.iqeq.controller;

import com.iqeq.dto.FieldDto;
import com.iqeq.dto.common.Response;
import com.iqeq.dto.common.SearchRequestDto;
import com.iqeq.exception.CustomException;
import com.iqeq.service.FieldService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class FieldController extends BaseController {

    @Autowired
    private FieldService fieldService;

    private static final String FIELD = "field";

    @GetMapping("/search/fields")
    public ResponseEntity<Response> getFieldsWithColumns(@Valid @ModelAttribute SearchRequestDto searchRequestDto) throws CustomException {
        return buildResponse(HttpStatus.OK, "Fields fetched successfully", fieldService.getFieldsWithColumns(searchRequestDto));
    }

    @PostMapping("/fields")
    public ResponseEntity<Response> createField(@Valid @RequestBody FieldDto fieldDto) throws CustomException {
        FieldDto fieldCreated = fieldService.createField(fieldDto);

        Map<String, Object> data = new HashMap<>();
        data.put(FIELD, fieldCreated);

        Response rs = new Response();
        rs.setCode(HttpStatus.OK.value());
        rs.setData(data);
        rs.setMessage("Field created successfully.");
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }

    @PutMapping("/fields/{fieldId}")
    public ResponseEntity<Response> updateField(@PathVariable Long fieldId, @Valid @RequestBody FieldDto fieldDto) throws CustomException {
        FieldDto fieldUpdated = fieldService.updateField(fieldId, fieldDto);

        Map<String, Object> data = new HashMap<>();
        data.put(FIELD, fieldUpdated);

        Response rs = new Response();
        rs.setCode(HttpStatus.OK.value());
        rs.setData(data);
        rs.setMessage("Field updated successfully.");
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }

    @DeleteMapping("/fields/{fieldId}")
    public ResponseEntity<Response> deleteField(@PathVariable Long fieldId) {
        FieldDto fieldDeleted = fieldService.deleteField(fieldId);

        Map<String, Object> data = new HashMap<>();
        data.put(FIELD, fieldDeleted);

        Response rs = new Response();
        rs.setCode(HttpStatus.OK.value());
        rs.setData(data);
        rs.setMessage("Field deleted successfully.");
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }

    @GetMapping("/fields/{fieldId}")
    public ResponseEntity<Response> getFieldById(@PathVariable Long fieldId) {
        FieldDto fieldDto = FieldDto.build(fieldService.getFieldById(fieldId));

        Map<String, Object> data = new HashMap<>();
        data.put(FIELD, fieldDto);

        Response rs = new Response();
        rs.setCode(HttpStatus.OK.value());
        rs.setData(data);
        rs.setMessage("Field fetched successfully.");
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }
}
