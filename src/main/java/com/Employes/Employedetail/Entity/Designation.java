package com.Employes.Employedetail.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
//DESIGNATION TABLE WHICH STORES THE DESIGNATION OF THE EMPLOYEES AND THEIR LEVEL
@Entity
@Table
public class Designation {

// DESIGNATION ID OF THE EMPLOYEE
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @JsonProperty("id")
    @JsonIgnore
    private Integer desgnid;
// JOB TITLE OF THE EMPLOYEE
    @Column
    @JsonProperty("title")
    private  String designation;
//LEVEL OF THE EMPLOYEE
    @Column
    @JsonIgnore
    public Float level;

    //GETTERS AND SETTERS FOR GETTING AND SETTING THE DETAILS IN THE TABLE
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
