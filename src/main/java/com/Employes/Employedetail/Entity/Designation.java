package com.Employes.Employedetail.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table
public class Designation {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @JsonProperty("id")
    @JsonIgnore
    private Integer desgnid;

    @Column
    @JsonProperty("title")
    private  String designation;

    @Column
    @JsonIgnore
    public Float level;


    public Integer getDesgnid() {
        return desgnid;
    }

    public void setDesgnid(Integer desgnid) {
        this.desgnid = desgnid;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Float getLevel() {
        return level;
    }

    public void setLevel(Float level) {
        this.level = level;
    }
}
