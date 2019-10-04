package com.Employes.Employedetail.controller;

public class postRequest {

    int empID=-1;
    String Empname;
    String empDesgn;
    Integer ParentID;
    boolean replace=false;



    public int getEmpID() {
        return empID;
    }

    public void setEmpID(int empID) {
        this.empID = empID;
    }

    public String getEmpname() {
        return Empname;
    }

    public void setEmpname(String empname) {
        this.Empname = empname;
    }

    public String getEmpDesgn() {
        return empDesgn;
    }

    public void setEmpDesgn(String empDesgn) {
        this.empDesgn = empDesgn;
    }

    public Integer getParentID() {
        return ParentID;
    }

    public void setParentID(Integer parentID) {
        ParentID = parentID;
    }

    public boolean isReplace() {
        return replace;
    }

    public void setReplace(boolean replace) {
        this.replace = replace;
    }


}
