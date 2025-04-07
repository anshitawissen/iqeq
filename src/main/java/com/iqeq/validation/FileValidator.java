package com.iqeq.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

public class FileValidator implements ConstraintValidator <ValidFile, MultipartFile> {

    @Value("${file.max-size}")
    private long maxFileSize;

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {

        if (file == null || file.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File must not be empty")
                    .addConstraintViolation();
            return false;
        }

        if (file.getSize() > maxFileSize) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File size must not exceed 100MB")
                    .addConstraintViolation();
            return false;
        }

        String contentType = file.getContentType();
        if (Objects.isNull(contentType) || !contentType.equalsIgnoreCase("application/pdf")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Only PDF files are allowed")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
