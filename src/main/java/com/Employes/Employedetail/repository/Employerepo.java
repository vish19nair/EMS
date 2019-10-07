package com.Employes.Employedetail.repository;

import com.Employes.Employedetail.Entity.Employee;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface Employerepo extends CrudRepository<Employee,Integer> {
    List<Employee> findAll();
    Employee findByEid(int eid);
    Employee findByManager(Integer eid);
    List<Employee> findAllByManager(Integer eid);
    List<Employee> findAllByOrderByDesignation_levelAscEmpnameAsc();
    List<Employee> findAllByManagerAndEidIsNot(int eid,int ans);

}
