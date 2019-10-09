package com.Employes.Employedetail.repository;

import com.Employes.Employedetail.Entity.Employee;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface Employerepo extends CrudRepository<Employee,Integer> {
    public List<Employee> findAll();
    public Employee findByEid(Integer eid);
   public Employee findByManager(Integer eid);
     public List<Employee> findAllByManager(Integer eid);
    public List<Employee> findAllByOrderByDesignation_levelAscEmpnameAsc();
    public List<Employee> findAllByManagerAndEidIsNot(int parentID, Integer empName);
    List<Employee> findAllByManagerOrderByDesignation_levelAsc(Integer eid);
}
