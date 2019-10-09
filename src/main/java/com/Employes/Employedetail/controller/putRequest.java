package com.Employes.Employedetail.controller;

public class putRequest {

    String Empname=null;
    String empDesgn=null;
    Integer ParentID=null;
    boolean replace=false;





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

    public boolean isReplace() {
        return replace;
    }

    public void setReplace(boolean replace) {
        this.replace = replace;
    }

}
