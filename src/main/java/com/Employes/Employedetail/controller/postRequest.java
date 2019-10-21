package com.Employes.Employedetail.controller;
//CLASS FOR POST REQUEST WHICH WOULD BE PASSED BY CLIENT
public class postRequest {

    public postRequest(){

    }
    public postRequest(String name, String jobTitle, Integer managerId) {
        this.name = name;
        this.jobTitle = jobTitle;
        this.managerId = managerId;
    }

    String name=null;
    String jobTitle=null;
    Integer managerId=null;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }
}
