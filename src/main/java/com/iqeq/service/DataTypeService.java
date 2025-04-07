package com.iqeq.service;

import com.iqeq.dto.DataTypeDto;
import com.iqeq.exception.CustomException;
import com.iqeq.exception.ResourceNotFoundException;
import com.iqeq.model.DataType;
import com.iqeq.repository.DataTypeRepository;
import com.iqeq.util.CommonServiceUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class DataTypeService {

    @Autowired
    private DataTypeRepository dataTypeRepository;

    public DataTypeDto createDataType(DataTypeDto dataTypeDto) throws CustomException {

        DataType dataType = DataTypeDto.buildDataType(dataTypeDto);
        DataType existingDataType = dataTypeRepository.findByNameAndIsArchive(dataTypeDto.getName(), false);
        if (existingDataType != null) {
            throw new CustomException(400, "Data Type name " + dataTypeDto.getName() + " is already exists.");
        }

        dataType.setCreatedDate(LocalDateTime.now());
        dataType.setCreatedBy(CommonServiceUtility.getLoggedInUserEmailId());
        dataType.setIsArchive(false);
        return DataTypeDto.build(dataTypeRepository.save(dataType));
    }

    public DataTypeDto updateDataType(Long dataTypeId, DataTypeDto dataTypeDto) {
        DataType existingDataType = getDataTypeById(dataTypeId);

        existingDataType.setName(dataTypeDto.getName());
        existingDataType.setValue(dataTypeDto.getValue());
        existingDataType.setUpdatedDate(LocalDateTime.now());
        existingDataType.setUpdatedBy(CommonServiceUtility.getLoggedInUserEmailId());

        return DataTypeDto.build(dataTypeRepository.save(existingDataType));
    }

    public DataTypeDto deleteDataType(Long dataTypeId) {
        DataType dataType = getDataTypeById(dataTypeId);

        try {
            dataType.setIsArchive(true);
            dataTypeRepository.save(dataType);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Cannot delete dataType");
        }
        return DataTypeDto.build(dataType);
    }

    public DataType getDataTypeById(Long dataTypeId) {
        Optional<DataType> dataType = dataTypeRepository.findById(dataTypeId);
        if (dataType.isEmpty()) {
            throw new ResourceNotFoundException("DataType", "ID", dataTypeId);
        }
        return dataType.get();
    }
}
