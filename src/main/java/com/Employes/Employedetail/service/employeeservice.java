package com.Employes.Employedetail.service;

import com.Employes.Employedetail.Entity.Designation;
import com.Employes.Employedetail.Entity.Employee;
import com.Employes.Employedetail.controller.postRequest;
import com.Employes.Employedetail.controller.putRequest;
import com.Employes.Employedetail.repository.DesignationRepo;
import com.Employes.Employedetail.repository.Employerepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class employeeservice extends employeevalidate{
@Autowired
    Employerepo repo;
@Autowired
    DesignationRepo depo;

//GET THE DETAILS OF EMPLOYEES USING ID
        public ResponseEntity getUserDetails(Integer eid) {

            Employee manager;
            List<Employee> colleagues;
            Map<String, Object> map = new LinkedHashMap<>();
            boolean userExists = false;
            if (eid != null && eid > 0) {
                userExists = userExists(eid);
            }
            else if (eid < 0)
            {
                return new ResponseEntity<>("INVALID_ID", HttpStatus.BAD_REQUEST);
            }
            if (userExists) {
                Employee emp = repo.findByEid(eid);
                map.put("id", emp.getEid());
                map.put("name", emp.getEmpname());
                map.put("jobTitle", emp.getJobtitle());
                map.put("Employee", emp);

                if (emp.getManager() != null) {
                    manager = repo.findByEid(emp.getManager());
                    map.put("manager", manager);
                    colleagues = repo.findAllByManagerAndEidIsNotOrderByDesignation_levelAscEmpnameAsc(emp.getManager(), emp.getEid());
                    map.put("colleagues", colleagues);
                }

                List<Employee> reporting = repo.findAllByManagerAndEidIsNotOrderByDesignation_levelAscEmpnameAsc(emp.getEid(), emp.getEid());
                if (reporting.size() != 0)
                    map.put("subordinates", reporting);

                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("EMP_NOT_EXISTS", HttpStatus.NOT_FOUND);
            }
        }
//GET DETAILS OF ALL EMPLOYEES
        public ResponseEntity getAll()
        {
            List<Employee> list=repo.findAllByOrderByDesignation_levelAscEmpnameAsc();
            if(hasData(list))
                return new ResponseEntity<>(list, HttpStatus.OK);
            else
                return new ResponseEntity<>("No Records Found",HttpStatus.BAD_REQUEST);
        }
//DELETE THE CURRENT EMPLOYEE
        public ResponseEntity deleteUser(Integer eid)
        {
            if(eid<0){
                return new ResponseEntity("INVALID_ID",HttpStatus.BAD_REQUEST);
            }
            boolean userExists=userExists(eid);
            if(userExists)
            {
                Employee emp=repo.findByEid(eid);
                if(emp.getDesignation().getDesignation().equals("Director"))
                {
                    List<Employee> list=repo.findAllByManager(emp.getEid());
                    if(hasData(list))
                    {
                        // Not able to delete
                        return new ResponseEntity("Director having childs cannot be deleted",HttpStatus.BAD_REQUEST);
                    }
                    else
                    {
                        //Able to delete
                        repo.delete(emp);
                        return new ResponseEntity("Deleted Successfully",HttpStatus.NO_CONTENT);
                    }
                }
                else
                {
                    Integer parentId=emp.getManager();
                    List<Employee> childs=repo.findAllByManager(emp.getEid());
                    for(Employee employee:childs)
                    {
                        employee.setManager(parentId);
                        repo.save(employee);
                    }
                    repo.delete(emp);
                    return new ResponseEntity("Deleted Successfully",HttpStatus.NO_CONTENT);
                }
            }
            else
            {
                return new ResponseEntity("Employee Doesn't Exists",HttpStatus.NOT_FOUND);
            }
        }
//CREATE NEW USER AND ADD IT INTO EMS
        public ResponseEntity addUser(postRequest employee)
        {
            String empName=employee.getName();
            String desg=employee.getJobTitle();
            Integer parentId=employee.getManagerId();
           if(parentId!=null){
               if(parentId.intValue()<0){
                   parentId=null;
               }
           }
          if(empName==null && desg==null && parentId==null){
              return new ResponseEntity("Please Enter valid data",HttpStatus.BAD_REQUEST);
          }
         if(desg!=null) {

             if (!isDesignationValid(desg)) {
                 return new ResponseEntity("Please Enter Valid Designation", HttpStatus.BAD_REQUEST);
             }
         }
         else{
             return new ResponseEntity("Designation cannot be null",HttpStatus.BAD_REQUEST);
         }
         if(!isValid(empName)){
             return new ResponseEntity("ENTER VALID NAME PLEASE",HttpStatus.BAD_REQUEST);
         }

         if(parentId==null) {
                Employee director = repo.findByManager(null);
                if (director != null) {
                    return new ResponseEntity("Director Already Exists ParentId cannot be NULL", HttpStatus.BAD_REQUEST);
                }
                else
                {
                    if(desg.equals("Director"))
                    {
                        Designation designation=depo.findByDesignation(desg);
                        Employee emp=new Employee(designation,parentId,empName);
                        repo.save(emp);
                        return new ResponseEntity(emp,HttpStatus.CREATED);
                    }
                    else
                    {
                        return new ResponseEntity("No Director found! Please Add Director First",HttpStatus.BAD_REQUEST);
                    }

                }
            }
            else
            {
                Employee parent=repo.findByEid(parentId);
                if(parent==null)
                {
                    return new ResponseEntity("Parent Does't Exists", HttpStatus.BAD_REQUEST);
                }
                else
                {
                    Designation designation=depo.findByDesignation(desg);

                    float currentLevel=designation.getLevel();

                    Employee parentRecord=repo.findByEid(parentId);
                    float parentLevel=parentRecord.getDesignation().getLevel();

                    if(parentLevel<currentLevel)
                    {
                        Employee emp=new Employee(designation,parentId,empName);
                        repo.save(emp);
                        return new ResponseEntity(emp,HttpStatus.CREATED);
                    }
                    else
                    {
                        return new ResponseEntity("INVALID PARENT",HttpStatus.BAD_REQUEST);
                    }
                }
            }
        }
//PUT API WHERE EMPLOYEE GETS UPDATED
        public ResponseEntity updateUser(Integer eid, putRequest emp)
        {
            //User Is Present
            if(eid<0){
                return new ResponseEntity("INVALID_ID",HttpStatus.BAD_REQUEST);
            }

            if(emp.getName()==null && emp.getJobTitle()==null && emp.getManagerId()==null && emp.isReplace()==false){
                return new ResponseEntity("NO Data",HttpStatus.BAD_REQUEST);
            }
            if(emp.getManagerId()!=null){
               if(!isValidId(emp.getManagerId())){
                   return new ResponseEntity("INVALID_MANAGER",HttpStatus.BAD_REQUEST);
                }
            }

            if(userExists(eid))
            {
                String userDesignation=emp.getJobTitle();

                if(emp.isReplace()) {
                    if (userDesignation == null)
                        return new ResponseEntity("Designation cannot be Null", HttpStatus.BAD_REQUEST);
                    else {
                        if (!isDesignationValid(userDesignation))
                            return new ResponseEntity("Designation does't exists! Please enter valid designation", HttpStatus.BAD_REQUEST);
                    }
                    if (!isValid(emp.getName())) {
                        return new ResponseEntity("ENTER VALID NAME", HttpStatus.BAD_REQUEST);
                    }
                    Integer parent = null;
                    Employee employee = repo.findByEid(eid);
                    if (isGreaterThanEqualCurrentDesignation(eid, userDesignation) && isSmallerThanParent(eid, userDesignation)) {
                        parent = employee.getManager();
                        repo.delete(employee);
                        Employee tempEmployee = new Employee(depo.findByDesignation(userDesignation), parent, emp.getName());
                        repo.save(tempEmployee);
                        List<Employee> list = repo.findAllByManager(eid);
                        for (Employee empTemp : list) {
                            empTemp.setManager(tempEmployee.getEid());
                            repo.save(empTemp);
                        }
                        return getUserDetails(tempEmployee.getEid());
                    } else
                        return new ResponseEntity(" cannot be replaced with ", HttpStatus.BAD_REQUEST);
                }
                else
                {

                    userDesignation=emp.getJobTitle();
                    Employee employee=repo.findByEid(eid);
                    Integer parentId=emp.getManagerId();


                    if(userDesignation!=null)
                    {
                        if (!isDesignationValid(userDesignation))
                            return new ResponseEntity("Designation does't exists! Please enter valid designation",HttpStatus.BAD_REQUEST);
                        else
                        {
                            if(isGreaterThanChilds(eid,userDesignation) && isSmallerThanParent(eid,userDesignation))
                            {
                                employee.setDesignation(depo.findByDesignation(userDesignation));
                            }
                            else
                            {
                                return new ResponseEntity(employee.getDesignation().getDesignation()+" cannot be replaced with "+userDesignation,HttpStatus.BAD_REQUEST);
                            }
                        }
                    }
                    if(emp.getName()!=null){
                        if(emp.getName().trim().equals("")){
                            return new ResponseEntity("NAME CANNOT BE EMPTY",HttpStatus.BAD_REQUEST);
                        }
                    }

                    if(parentId!=null)
                    {
                        if(!userExists(parentId))
                            return new ResponseEntity("Parent does't Exists",HttpStatus.BAD_REQUEST);
                        else
                            {

                            if(isGreaterThanCurrentDesignation(eid,repo.findByEid(parentId).getDesignation().getDesignation()))
                            {
                                employee.setManager(parentId);
                            }
                            else
                            {
                                return new ResponseEntity("Invalid ParentId",HttpStatus.BAD_REQUEST);
                            }
                        }
                    }
                  /*  else
                    {
                        return new ResponseEntity("ParentId cannot be Null",HttpStatus.BAD_REQUEST);
                    }

                   */
                    if(emp.getName()!=null)
                    {
                        employee.setEmpname(emp.getName());
                    }
                    repo.save(employee);
                    return getUserDetails(eid);
                }
            }
            //USER IS NOT PRESENT
            else
            {
                return new ResponseEntity("User does't Exists", HttpStatus.BAD_REQUEST);
            }

        }
    }





