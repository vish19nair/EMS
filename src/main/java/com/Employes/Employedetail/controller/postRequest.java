package com.Employes.Employedetail.controller;

public class postRequest {


    String Empname=null;
    String empDesgn=null;
    Integer ParentID=null;






    public String getEmpname() {
        return Empname;
    }

    public void setEmpname(String empname) {
        this.Empname = empname;
    }

    public String getEmpDesgn() {
        return empDesgn.toUpperCase();
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



}
