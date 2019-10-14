package com.Employes.Employedetail.controller;

public class putRequest {

    String name=null;
    String jobTitle=null;
    Integer managerId=null;
    boolean replace=false;

    public putRequest(){

    }
    public putRequest(String name, String jobTitle, Integer managerId, boolean replace) {
        this.name = name;
        this.jobTitle = jobTitle;
        this.managerId = managerId;
        this.replace = replace;
    }

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

    public boolean isReplace() {
        return replace;
    }

    public void setReplace(boolean replace) {
        this.replace = replace;
    }
}
