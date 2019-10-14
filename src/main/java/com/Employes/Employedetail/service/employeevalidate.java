package com.Employes.Employedetail.service;

import com.Employes.Employedetail.Entity.Designation;
import com.Employes.Employedetail.Entity.Employee;
import com.Employes.Employedetail.repository.DesignationRepo;
import com.Employes.Employedetail.repository.Employerepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class employeevalidate {
    @Autowired
    Employerepo repo;
    @Autowired
    DesignationRepo depo;

    public boolean isGreaterThanEqualCurrentDesignation(Integer eid,String desg)
    {
        Employee employee=repo.findByEid(eid);
        float selfLevel=employee.getDesignation().getLevel();
        float parentLevel=depo.findByDesignation(desg).getLevel();
        if(selfLevel>=parentLevel)
            return true;
        else
            return false;
    }

    public boolean isSmallerThanParent(Integer eid,String desg)
    {
        Employee employee=repo.findByEid(eid);
        if(employee.getManager()!=null)
        {
            float parentLevel=repo.findByEid(employee.getManager()).getDesignation().getLevel();
            float selfLevel=depo.findByDesignation(desg).getLevel();
            if(selfLevel>parentLevel)
                return true;
            else
                return false;
        }
        else
            return true;
    }

    public boolean isGreaterThanChilds(Integer eid,String desg)
    {
        float selfLevel=depo.findByDesignation(desg).getLevel();
        List<Employee> list=repo.findAllByManagerOrderByDesignation_levelAsc(eid);
        if(list.size()>0)
        {
            float childLevel=list.get(0).getDesignation().getLevel();
            if(selfLevel<childLevel)
                return true;
            else
                return false;
        }
        else
        {
            return true;
        }
    }
    public boolean isGreaterThanCurrentDesignation(Integer eid,String desg)
    {
        Employee employee=repo.findByEid(eid);
        float selfLevel=employee.getDesignation().getLevel();
        Designation designation=depo.findByDesignation(desg);
        float parentLevel=designation.getLevel();

        if(selfLevel>parentLevel)
            return true;
        else
            return false;
    }

    public boolean userExists(Integer eid)
    {
        Employee emp=repo.findByEid(eid);

        if(emp!=null)
            return true;
        else
            return false;
    }

    public boolean hasData(List<Employee> list)
    {
        if(list.size()>0)
            return true;
        else
            return false;
    }

    public boolean isDesignationValid(String desg)
    {
        Designation designation=depo.findByDesignation(desg);
        return (designation!=null);
    }

    public boolean isValid(String name){
        if(name.trim().equals("") || name==null){
            return  false;
        }
        else{
            return true;
        }
    }

}




