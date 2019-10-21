package com.Employes.Employedetail.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
//EMPLOYEE WHICH STORES THE DETAILS OF EMPLOYEE
@Entity
@Table
public class Employee {
// EMPLOYEE ID OF THE EMPLOYEE
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column
@JsonProperty("id")
private int eid;
//NAME OF THE EMPLOYEE
@Column
@JsonProperty("name")
private String empname;

//MANAGER OF THE EMPLOYEE
@Column(nullable = true)
private Integer manager;

//DESIGNATION ID OF THE EMPLOYEE
@OneToOne
@JoinColumn(name="desgnid")
@JsonIgnore
public Designation designation;

//JOB TITLE OF THE EMPLOYEE
@Transient
private String Jobtitle;

    public Employee(){

    }

    public Employee(Designation designation, Integer manager, String empName) {
        this.designation=designation;
        this.manager=manager;
        this.empname=empName;
    }


//GETTERS AND SETTERS FOR GETTING AND SETTING THE DETAILS IN THE TABLE
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
