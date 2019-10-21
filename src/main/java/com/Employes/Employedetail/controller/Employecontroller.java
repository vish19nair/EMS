package com.Employes.Employedetail.controller;

import com.Employes.Employedetail.Entity.Designation;
import com.Employes.Employedetail.Entity.Employee;
import com.Employes.Employedetail.repository.DesignationRepo;
import com.Employes.Employedetail.repository.Employerepo;
import com.Employes.Employedetail.service.employeeservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.security.krb5.internal.crypto.Des;

import javax.validation.constraints.Null;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest")
public class Employecontroller {
    @Autowired
    Employerepo repo;
    @Autowired
    DesignationRepo depo;
    @Autowired
    employeeservice servo;

    //Get Details Of All Employees
    @RequestMapping(value = "/employees", method = RequestMethod.GET)
    public ResponseEntity showAllEmployees() {
        return servo.getAll();
    }


    //Get details of employee by ID
    @GetMapping("/employees/{eid}")
    public ResponseEntity getUser(@PathVariable("eid") Integer eid) {
      return servo.getUserDetails(eid);
    }


    //Post the details Of the Employee
    @PostMapping(path = "/employees")
    public ResponseEntity PostApi(@RequestBody postRequest employee) {
        return servo.addUser(employee);
    }


    //Put the details of the employee
    @PutMapping(path = "/employees/{empId}")
    public ResponseEntity putData(@PathVariable("empId") Integer empId, @RequestBody putRequest emp)
    {
        return servo.updateUser(empId, emp);
    }

    //delete Employees By  Their ID
    @DeleteMapping(value = "/employees/{eid}")
    public ResponseEntity deleteApi(@PathVariable Integer eid) {
        ResponseEntity entity = servo.deleteUser(eid);
        return entity;
    }
}



