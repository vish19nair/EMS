package com.Employes.Employedetail.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table
public class Designation {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @JsonIgnore
    private Integer desgnid;

    @Column
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
