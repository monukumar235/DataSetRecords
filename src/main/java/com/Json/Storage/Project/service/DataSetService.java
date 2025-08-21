package com.Json.Storage.Project.service;

import com.Json.Storage.Project.dto.request.DataSetJsonRequest;
import com.Json.Storage.Project.dto.response.DatasetJsonsResponse;
import com.Json.Storage.Project.entity.DataSetEntityRecord;
import com.Json.Storage.Project.exception.DataNotFoundException;
import com.Json.Storage.Project.exception.InvalidQueryParamException;
import com.Json.Storage.Project.repository.DataSetRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DataSetService {

    @Autowired
    private DataSetRepository dataSetRepository;

    @Autowired
    ObjectMapper objectMapper;

    public DatasetJsonsResponse insertRecord(DataSetJsonRequest request, String datasetName) throws JsonProcessingException {

        DataSetEntityRecord dataSetEntityRecord = new DataSetEntityRecord();

        dataSetEntityRecord.setId(request.getId());
        dataSetEntityRecord.setDataSetName(datasetName);
        dataSetEntityRecord.setDataSets(objectMapper.writeValueAsString(request));

        dataSetRepository.save(dataSetEntityRecord);

        return new DatasetJsonsResponse(request.getId(), datasetName);
    }

    public Map<String,Object> queryDataSet(String datasetName, String groupedBy, String sortedBy, String order) throws JsonProcessingException {
        List<DataSetEntityRecord> dataSetEntityRecords = dataSetRepository.findByDataSetName(datasetName);

        if(dataSetEntityRecords.isEmpty()){
            throw new DataNotFoundException(datasetName);
        }

        if(groupedBy!=null && sortedBy!=null){
            throw new InvalidQueryParamException("cann't use both groupby and sortby at the same time!");
        }

        List<Map<String,Object>> recordMap = new ArrayList<>();

        for (DataSetEntityRecord dataSetEntityRecord : dataSetEntityRecords){
            recordMap.add(objectMapper.readValue(dataSetEntityRecord.getDataSets(),Map.class));
        }

        if(groupedBy!=null){
            return  groupedByField(groupedBy,recordMap);
        }

        if(sortedBy!=null){
            return sortByField(sortedBy,order,recordMap);
        }

        return Map.of("records",recordMap);
    }

    private Map<String, Object> sortByField(String sortedBy, String order, List<Map<String, Object>> recordMap) {

        Comparator<Map<String, Object>> comparator = Comparator.comparing(r -> (Comparable) r.get(sortedBy));

        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }

        List<Map<String, Object>> sorted = recordMap.stream().sorted(comparator).toList();
        return Map.of("sortedRecords",sorted);
    }

    private Map<String, Object> groupedByField(String groupedBy, List<Map<String, Object>> recordMap) {

        Map<Object, List<Map<String, Object>>> grouped = recordMap.stream()
                .collect(Collectors.groupingBy(r -> r.get(groupedBy)));
        return Map.of("groupedRecords",grouped);
    }
}
