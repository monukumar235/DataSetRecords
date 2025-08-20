package com.Json.Storage.Project.controller;

import com.Json.Storage.Project.dto.request.DataSetJsonRequest;
import com.Json.Storage.Project.dto.response.DatasetJsonsResponse;
import com.Json.Storage.Project.service.DataSetService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dataset/api")
public class DataSetController {


    @Autowired
    private DataSetService dataSetService;

    @PostMapping("/v1/{datasetName}/record")
    public ResponseEntity<DatasetJsonsResponse> insertRecord(@RequestBody DataSetJsonRequest request, @PathVariable("datasetName") String datasetName) throws JsonProcessingException {
        DatasetJsonsResponse datasetJsonsResponse = dataSetService.insertRecord(request, datasetName);
        return ResponseEntity.ok(datasetJsonsResponse);
    }



    @GetMapping("/v1/{datasetName}/query")
    public ResponseEntity<?> queryDataSet(@PathVariable("datasetName") String datasetName,
                                          @RequestParam(required = false) String groupedBy,
                                          @RequestParam(required = false) String sortedBy,
                                          @RequestParam(required = false ,defaultValue = "asc") String order) throws JsonProcessingException {

        return ResponseEntity.ok(dataSetService.queryDataSet(datasetName,groupedBy,sortedBy,order));
    }
}
