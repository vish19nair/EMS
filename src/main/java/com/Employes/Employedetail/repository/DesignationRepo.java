package com.Employes.Employedetail.repository;

import com.Employes.Employedetail.Entity.Designation;
import com.Employes.Employedetail.Entity.Employee;
import org.springframework.data.repository.CrudRepository;

public interface DesignationRepo extends CrudRepository<Designation,Integer> {
Designation findByDesignation(String desg);
}
