package com.Json.Storage.Project.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "data_records")
public class DataSetEntityRecord {


    @Id
    @Column(name = "data_set_id")
    private  int id;
    @Column(name = "data_set_name")
    private String dataSetName;
    @Column(columnDefinition = "json",name = "data_sets")
    private String dataSets;
}
