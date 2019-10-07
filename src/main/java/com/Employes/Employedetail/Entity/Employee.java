package com.Employes.Employedetail.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;

@Entity
@Table
public class Employee {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column
private int eid;

@Column
private String empname;


@Column(nullable = true)
private Integer manager;

@OneToOne
@JoinColumn(name="desgnid")
@JsonIgnore
public Designation designation;


@Transient
private String Jobtitle;

    public Employee(){

    }

    public Employee(Designation designation, int manager, String empName) {
        this.designation=designation;
        this.manager=manager;
        this.empname=empName;
    }



    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public Designation getDesignation() {
        return designation;
    }

    public void setDesignation(Designation designation) {
        this.designation = designation;
    }

    public String getJobtitle() {
        return this.designation.getDesignation();
    }

    public void setJobtitle(String jobtitle) {
        Jobtitle = jobtitle;
    }

    public Integer getManager() {
        return manager;
    }

    public void setManager(Integer manager) {
        this.manager = manager;
    }




}
