package com.Json.Storage.Project.service;

import com.Json.Storage.Project.dto.request.DataSetJsonRequest;
import com.Json.Storage.Project.dto.response.DatasetJsonsResponse;
import com.Json.Storage.Project.entity.DataSetEntityRecord;
import com.Json.Storage.Project.exception.DataNotFoundException;
import com.Json.Storage.Project.exception.InvalidQueryParamException;
import com.Json.Storage.Project.repository.DataSetRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.TestComponent;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DatasetRecordServiceTest {

    @Mock
    private DataSetRepository dataSetRepository;

    @InjectMocks
    private DataSetService dataSetService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        dataSetService.objectMapper = objectMapper;
    }

    @Test
    public void testInsertRecord_Success() throws JsonProcessingException {
        DataSetJsonRequest input = new DataSetJsonRequest();
        input.setId(1);
        input.setAge(23);
        input.setName("mohit");

        DataSetEntityRecord save = new DataSetEntityRecord();
        save.setId(1);
        save.setDataSetName("employee_set");
        save.setDataSets(objectMapper.writeValueAsString(input));

        when(dataSetRepository.save(any())).thenReturn(save);

        DatasetJsonsResponse employeeSet = dataSetService.insertRecord(input, "employee_set");

        assertNotNull(employeeSet);
        assertEquals("employee_set",employeeSet.getDatasetName());

        verify(dataSetRepository,times(1)).save(any(DataSetEntityRecord.class));
    }

    @Test
    public void testQueryRecords() throws JsonProcessingException {
        String json1 = "{\"id\":1,\"name\":\"John\",\"department\":\"Engineering\"}";
        String json2 = "{\"id\":2,\"name\":\"Jane\",\"department\":\"Engineering\"}";
        String json3 = "{\"id\":3,\"name\":\"Alice\",\"department\":\"Marketing\"}";

        DataSetEntityRecord dataSet1 = new DataSetEntityRecord();
        dataSet1.setId(1);
        dataSet1.setDataSetName("employee_dataset");
        dataSet1.setDataSets(json1);

        DataSetEntityRecord dataSet2 = new DataSetEntityRecord();
        dataSet2.setId(2);
        dataSet2.setDataSets(json2);
        dataSet2.setDataSetName("employee_dataset");

        DataSetEntityRecord dataSet3 = new DataSetEntityRecord();
        dataSet3.setId(3);
        dataSet3.setDataSetName("employee_dataset");
        dataSet3.setDataSets(json3);

        when(dataSetRepository.findByDataSetName("employee_dataset")).thenReturn(List.of(dataSet1,dataSet2,dataSet3));

        Map<String, Object> results = dataSetService.queryDataSet("employee_dataset", null, null, null);

        assertNotNull(results);
        assertTrue(results.containsKey("records"));
        List<Map<String,Object>> records  = (List<Map<String,Object>>)  results.get("records");
    }

    @Test
    public void testQueryRecords_GroupBy() throws JsonProcessingException {
        String json1 = "{\"id\":1,\"name\":\"John\",\"department\":\"Engineering\"}";
        String json2 = "{\"id\":2,\"name\":\"Jane\",\"department\":\"Engineering\"}";
        String json3 = "{\"id\":3,\"name\":\"Alice\",\"department\":\"Marketing\"}";

        DataSetEntityRecord dataSet1 = new DataSetEntityRecord();
        dataSet1.setId(1);
        dataSet1.setDataSetName("employee_dataset");
        dataSet1.setDataSets(json1);

        DataSetEntityRecord dataSet2 = new DataSetEntityRecord();
        dataSet2.setId(2);
        dataSet2.setDataSets(json2);
        dataSet2.setDataSetName("employee_dataset");

        DataSetEntityRecord dataSet3 = new DataSetEntityRecord();
        dataSet3.setId(3);
        dataSet3.setDataSetName("employee_dataset");
        dataSet3.setDataSets(json3);

        when(dataSetRepository.findByDataSetName("employee_dataset")).thenReturn(List.of(dataSet1,dataSet2,dataSet3));

        Map<String, Object> results = dataSetService.queryDataSet("employee_dataset", "department", null, "asc");

        assertNotNull(results);
        assertTrue(results.containsKey("groupedRecords"));
        Map<String, List<Map<String, Object>>> groupedRecords  = (Map<String, List<Map<String, Object>>>)  results.get("groupedRecords");
        assertEquals(2,groupedRecords.get("Engineering").size());
        assertEquals(1,groupedRecords.get("Marketing").size());

    }

    @Test
    public void testQueryRecords_SortBy() throws JsonProcessingException {
        String json1 = "{\"id\":1,\"name\":\"John\",\"department\":\"Engineering\",\"age\":30}";
        String json2 = "{\"id\":2,\"name\":\"Jane\",\"department\":\"Engineering\",\"age\":20}";
        String json3 = "{\"id\":3,\"name\":\"Alice\",\"department\":\"Marketing\",\"age\":10}";

        DataSetEntityRecord dataSet1 = new DataSetEntityRecord();
        dataSet1.setId(1);
        dataSet1.setDataSetName("employee_dataset");
        dataSet1.setDataSets(json1);

        DataSetEntityRecord dataSet2 = new DataSetEntityRecord();
        dataSet2.setId(2);
        dataSet2.setDataSets(json2);
        dataSet2.setDataSetName("employee_dataset");

        DataSetEntityRecord dataSet3 = new DataSetEntityRecord();
        dataSet3.setId(3);
        dataSet3.setDataSetName("employee_dataset");
        dataSet3.setDataSets(json3);

        when(dataSetRepository.findByDataSetName("employee_dataset")).thenReturn(List.of(dataSet1,dataSet2,dataSet3));

        Map<String, Object> queryDataSet = dataSetService.queryDataSet("employee_dataset", null, "age", "asc");

        assertNotNull(queryDataSet);
        assertTrue(queryDataSet.containsKey("sortedRecords"));
        List<Map<String, Object>> sortedRecords  = (List<Map<String, Object>>)  queryDataSet.get("sortedRecords");
        assertEquals(10,sortedRecords.get(0).get("age"));
        assertEquals(20,sortedRecords.get(1).get("age"));
        assertEquals(30,sortedRecords.get(2).get("age"));
    }

    @Test
    public void testQueryRecords_SortByDesc() throws JsonProcessingException {
        String json1 = "{\"id\":1,\"name\":\"John\",\"department\":\"Engineering\",\"age\":30}";
        String json2 = "{\"id\":2,\"name\":\"Jane\",\"department\":\"Engineering\",\"age\":20}";
        String json3 = "{\"id\":3,\"name\":\"Alice\",\"department\":\"Marketing\",\"age\":10}";

        DataSetEntityRecord dataSet1 = new DataSetEntityRecord();
        dataSet1.setId(1);
        dataSet1.setDataSetName("employee_dataset");
        dataSet1.setDataSets(json1);

        DataSetEntityRecord dataSet2 = new DataSetEntityRecord();
        dataSet2.setId(2);
        dataSet2.setDataSets(json2);
        dataSet2.setDataSetName("employee_dataset");

        DataSetEntityRecord dataSet3 = new DataSetEntityRecord();
        dataSet3.setId(3);
        dataSet3.setDataSetName("employee_dataset");
        dataSet3.setDataSets(json3);

        when(dataSetRepository.findByDataSetName("employee_dataset")).thenReturn(List.of(dataSet1,dataSet2,dataSet3));

        Map<String, Object> queryDataSet = dataSetService.queryDataSet("employee_dataset", null, "age", "desc");

        assertNotNull(queryDataSet);
        assertTrue(queryDataSet.containsKey("sortedRecords"));
        List<Map<String, Object>> sortedRecords  = (List<Map<String, Object>>)  queryDataSet.get("sortedRecords");
        assertEquals(30,sortedRecords.get(0).get("age"));
        assertEquals(20,sortedRecords.get(1).get("age"));
        assertEquals(10,sortedRecords.get(2).get("age"));
    }

    @Test
    public  void testQueryRecords_EmptyDataset(){
        when(dataSetRepository.findByDataSetName("employee_dataset")).thenReturn(Collections.emptyList());

        assertThrows(DataNotFoundException.class,
                ()->dataSetService.queryDataSet("employee_dataset",null,null,"asc"));
    }

    @Test
    public void testQueryRecords_BothGroupByAndSortedBy(){
        when(dataSetRepository.findByDataSetName("employee_dataset")).thenReturn(List.of(new DataSetEntityRecord()));

        assertThrows(InvalidQueryParamException.class,
                ()->dataSetService.queryDataSet("employee_dataset","department","age","asc"));

    }
}
