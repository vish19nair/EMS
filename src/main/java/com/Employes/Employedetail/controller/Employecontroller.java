package com.Employes.Employedetail.controller;

import com.Employes.Employedetail.Entity.Designation;
import com.Employes.Employedetail.Entity.Employee;
import com.Employes.Employedetail.repository.DesignationRepo;
import com.Employes.Employedetail.repository.Employerepo;
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
public class Employecontroller {
    @Autowired
    Employerepo repo;
    @Autowired
    DesignationRepo depo;


    //Get Details Of All Employees
    @RequestMapping(value = "/employees", method = RequestMethod.GET)
    public ResponseEntity<Object> showAllEmployees() {
        //  List<Employee> employees=repo.findAll();


        Object list = repo.findAllByOrderByDesignation_levelAscEmpnameAsc();


        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    //get details of employee by ID
    @GetMapping("/employee/{eid}")
    public Map<String, Object> getUser(@PathVariable("eid") int eid) {
        Employee manager = null;
        List<Employee> colleagues = null;
        Map<String, Object> map = new LinkedHashMap<>();

        Employee emp = repo.findByEid(eid);
        map.put("Employee", emp);

        if (emp.getManager() != null) {
            manager = repo.findByEid(emp.getManager());
            map.put("Manager", manager);

            colleagues = repo.findAllByManagerAndEidIsNot(emp.getManager(), emp.getEid());
            map.put("Colleagues", colleagues);
        }

        List<Employee> reporting = repo.findAllByManagerAndEidIsNot(emp.getEid(), emp.getEid());
        if (reporting.size() != 0)
            map.put("Reporting Too", reporting);

        return map;
    }
    //Post the details Of the Employee
    @PostMapping(path = "/rest/employees")
    public ResponseEntity PostApi(@RequestBody postRequest employee) {
        String result = "";
        String empName = employee.getEmpname();
        String desg = employee.getEmpDesgn();
        int parent = employee.getParentID();

        Designation designation = depo.findByDesignation(desg);
        float childlevel = designation.getLevel();
        Employee employee1 = repo.findByEid(parent);
        float parentlevel = employee1.getDesignation().getLevel();

        if (parentlevel < childlevel) {
            Employee em = new Employee(designation, parent, empName);
            repo.save(em);
            result = "Post Implemented";
        }
          else {
            return new ResponseEntity<>("Invalid Entry",HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(employee, HttpStatus.OK);
    }
//Put the details of the employee
@PutMapping(path = "/rest/update/employee")
public ResponseEntity PutApi(@RequestBody postRequest employee){
         String answer="";
         String empName = employee.getEmpname();
         String desg = employee.getEmpDesgn();
         int parent = employee.getParentID();
         Boolean replacing=employee.isReplace();
         int emp =employee.getEmpID();
         Employee era=repo.findByEid(emp);



         if(replacing){
              repo.delete(era);
              PostApi(employee);
              return new ResponseEntity(HttpStatus.ACCEPTED);
         }
         else {
             return new ResponseEntity(HttpStatus.OK);
         }
    }




    //delete Employees By  Their ID
    @DeleteMapping(value = "/employee/{eid}")
    public ResponseEntity deleteApi(@PathVariable Integer eid) {
        Employee employee = repo.findByEid(eid);
        Integer par = employee.getManager();
        if(par==null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);

        }
        if (employee == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }



        repo.delete(employee);
        return new ResponseEntity<Object>(HttpStatus.OK);

    }


}












