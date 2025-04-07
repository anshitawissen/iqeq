package com.iqeq.repository;

import com.iqeq.model.DataType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataTypeRepository extends JpaRepository<DataType, Long> {
    DataType findByNameAndIsArchive(String name, boolean isArchive);

}
