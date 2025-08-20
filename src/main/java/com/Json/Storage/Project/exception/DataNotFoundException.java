package com.Json.Storage.Project.exception;

public class DataNotFoundException extends RuntimeException{

    public DataNotFoundException(String datasetName){
        super("Data not found "+datasetName);
    }
}
