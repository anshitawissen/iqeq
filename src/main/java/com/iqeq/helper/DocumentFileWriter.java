package com.iqeq.helper;

import com.iqeq.exception.CustomException;
import org.springframework.core.io.InputStreamResource;

import java.util.List;

public interface DocumentFileWriter {
    public InputStreamResource export(List<?> data, List<String> headers, List<String> fields, Class<?> classType, String filename) throws CustomException;
}
