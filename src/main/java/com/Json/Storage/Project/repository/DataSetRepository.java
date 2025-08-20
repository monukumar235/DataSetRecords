package com.Json.Storage.Project.repository;

import com.Json.Storage.Project.entity.DataSetEntityRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataSetRepository extends JpaRepository<DataSetEntityRecord, Integer> {

    List<DataSetEntityRecord> findByDataSetName(String dataSetName);
}
