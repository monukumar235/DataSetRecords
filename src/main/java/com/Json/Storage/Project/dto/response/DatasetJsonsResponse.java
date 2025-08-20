package com.Json.Storage.Project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DatasetJsonsResponse {
    private int recordId;
    private String message;
    private String  datasetName;

    public DatasetJsonsResponse(int id, String datasetName) {
        this.recordId = id;
        this.datasetName = datasetName;
        this.message = "Record added successfully";
    }
}
