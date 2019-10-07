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
    public ResponseEntity showAllEmployees() {

        List<Employee> list = repo.findAllByOrderByDesignation_levelAscEmpnameAsc();
        if(list.size()>0) {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
        else
        {
          return new ResponseEntity<>("No record found",HttpStatus.NOT_FOUND);
        }
    }




    //get details of employee by ID
    @GetMapping("/employee/{eid}")
    public ResponseEntity getUser(@PathVariable("eid") int eid) {
        Employee manager = null;
        List<Employee> colleagues = null;
        Map<String, Object> map = new LinkedHashMap<>();

        Employee emp = repo.findByEid(eid);
        if(emp==null){
            return new ResponseEntity("no record found",HttpStatus.BAD_REQUEST);

        }
        else {
            map.put("Employee", emp);

            if (emp.getManager() != null) {
                manager = repo.findByEid(emp.getManager());
                map.put("Manager", manager);

                colleagues = repo.findAllByManagerAndEidIsNot(emp.getManager(), emp.getEid());
                map.put("Colleagues", colleagues);
            }

            List<Employee> reporting = repo.findAllByManagerAndEidIsNot(emp.getEid(), emp.getEid());
            if (reporting.size() != 0) {
                map.put("Reporting Too", reporting);
            }

        }
        return new ResponseEntity(map,HttpStatus.OK);
    }

    //Post the details Of the Employee
    @PostMapping(path = "/rest/employees")
    public ResponseEntity PostApi(@RequestBody postRequest employee) {
        String result = "";
        String empName = employee.getEmpname();
        String desg = employee.getEmpDesgn().toUpperCase();
        Integer parent = employee.getParentID();

        Designation designation = depo.findByDesignation(desg);
        float childlevel = designation.getLevel();

        if(repo.findByEid(parent)==null){
           return new ResponseEntity("",HttpStatus.BAD_REQUEST);
        }
        if(!(parent==null && repo.findByEid(parent)==null)) {
            Employee employee1 = repo.findByEid(parent);
            float parentlevel = employee1.getDesignation().getLevel();

            if (parentlevel < childlevel) {
                Employee em = new Employee(designation, parent, empName);
                repo.save(em);
                result = "Data saved";
            }
            else {
                return new ResponseEntity<>(desg+" cannot report to "+repo.findByEid(parent).getJobtitle(),HttpStatus.BAD_REQUEST);
            }
        }
        else if(repo.findAll().size()<=0)
        {
            if(desg=="DIRECTOR") {
                Employee em = new Employee(designation, parent, empName);
                repo.save(em);
                return  new ResponseEntity("Director has been added for the first time",HttpStatus.ACCEPTED);
            }
            else{
                return new ResponseEntity("unable to find the director",HttpStatus.BAD_REQUEST);
            }
        }
        else{
            Employee em=repo.findByManager(null);
            if(em==null){
                Employee em1=new Employee(designation,parent,empName);
                repo.save(em1);
                return new ResponseEntity("Data accepted and saved",HttpStatus.OK);
            }
            else{
                return new ResponseEntity("Please enter valid supervisor",HttpStatus.OK);
            }
        }
               return new ResponseEntity<>( HttpStatus.OK);
    }


    //Put the details of the employee
@PutMapping(path = "/rest/update/employee")
public ResponseEntity putData(@PathVariable("empId") int empId, @RequestBody postRequest emp)
{

    // when replace is true
    if(emp.isReplace())
    {
        Employee emp1 = repo.findByEid(empId);
        if(emp1==null) {
            return new ResponseEntity("",HttpStatus.OK);
        }
        else
        {
            Integer parID= emp1.getManager();
            Employee newEmployee = new Employee();
            newEmployee.setManager(parID);
            newEmployee.setEmpname(emp.getEmpname());
            if(repo.findByEid(empId).getDesignation().getLevel()>=depo.findByDesignation(emp.getEmpDesgn().toUpperCase()).getLevel())
            {
                newEmployee.designation=depo.findByDesignation(emp.getEmpDesgn().toUpperCase());

                repo.save(newEmployee);
                List<Employee> parChange =repo.findAllByManager(empId);
                for(Employee a:parChange)
                {
                    a.setManager(newEmployee.getEid());
                    repo.save(a);
                }

                 //     status=HttpStatus.OK;



            }
            else{
                //result = "Invalid request";
               // status=HttpStatus.BAD_REQUEST;
              return  new ResponseEntity(HttpStatus.OK);
            }

        }
     return  new ResponseEntity(HttpStatus.OK);
    }
    else{

        return new ResponseEntity(HttpStatus.OK);
    }



}


    //delete Employees By  Their ID
    @DeleteMapping(value = "/employee/{eid}")
    public ResponseEntity deleteApi(@PathVariable Integer eid) {
        Employee employee = repo.findByEid(eid);
        Integer par = employee.getManager();
        //List<Employee> child = repo.findAllByManagerAndEidIsNot(employee.getEid(), employee.getEid());

        List<Employee> child = repo.findAllByManager(employee.getEid());
        if (employee == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if(par==null) {
            if(child!=null) {
                return new ResponseEntity("Deleting the Director is Invalid as children are present", HttpStatus.BAD_REQUEST);
            }
            else{
                repo.delete(employee);
                return new ResponseEntity("Director has been deleted as no child was there are no children",HttpStatus.ACCEPTED);
            }

        }

        if(par!=null){

            if(child!=null) {
                for (Employee e : child) {
                    e.setManager(par);
                    repo.save(e);
                }
            }
            else{
                repo.delete(employee);
            }
        }
        repo.delete(employee);
        return new ResponseEntity<Object>("DELETED SUCCESSFULLY",HttpStatus.OK);

    }
}












