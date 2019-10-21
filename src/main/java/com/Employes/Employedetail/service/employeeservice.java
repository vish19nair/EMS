package com.Employes.Employedetail.service;

import com.Employes.Employedetail.Entity.Designation;
import com.Employes.Employedetail.Entity.Employee;
import com.Employes.Employedetail.controller.postRequest;
import com.Employes.Employedetail.controller.putRequest;
import com.Employes.Employedetail.repository.DesignationRepo;
import com.Employes.Employedetail.repository.Employerepo;
import com.Employes.Employedetail.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
//SERVICE CLASS WHICH CARRY OUT THE REST API SERVICES
@Service
public class employeeservice extends employeevalidate{
@Autowired
    Employerepo repo;
@Autowired
    DesignationRepo depo;
@Autowired
    MessageUtil messageUtil;
//GET THE DETAILS OF EMPLOYEES USING ID
        public ResponseEntity getUserDetails(Integer eid) {

            Employee manager;
            List<Employee> colleagues;
            Map<String, Object> map = new LinkedHashMap<>();
            boolean userExists = false;
            //CHECKING ID IS VALID OR NOT
            if (eid != null && eid > 0) {
                userExists = userExists(eid);
            }
            else if (eid < 0)
            {
                return new ResponseEntity<>(messageUtil.getMessage("INVALID_ID"), HttpStatus.BAD_REQUEST);
            }
            //USER EXISTS THEN
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
            //EMPLOYEE DOESN'T EXIST
            else {
//
                return new ResponseEntity<>( messageUtil.getMessage("EMP_NOT_EXISTS"), HttpStatus.NOT_FOUND);
            }
        }
//GET DETAILS OF ALL EMPLOYEES
        public ResponseEntity getAll()
        {
            List<Employee> list=repo.findAllByOrderByDesignation_levelAscEmpnameAsc();
            //EMPLOYEE DETAILS EXISTS
            if(hasData(list))
                return new ResponseEntity<>(list, HttpStatus.OK);
            //EMPLOYEE DETAILS DOESN'T EXIST
            else
                return new ResponseEntity<>(messageUtil.getMessage("NO_DATA_FOUND"),HttpStatus.BAD_REQUEST);
        }
//DELETE THE CURRENT EMPLOYEE
        public ResponseEntity deleteUser(Integer eid)
        {   //IF ID IS VALID OR NOT
            if(eid<0){

                return new ResponseEntity(messageUtil.getMessage("INVALID_ID"),HttpStatus.BAD_REQUEST);
            }
            boolean userExists=userExists(eid);
            //EMPLOYEE EXISTS THEN
            if(userExists)
            {
                Employee emp=repo.findByEid(eid);
                if(emp.getDesignation().getDesignation().equals("Director"))
                {
                    List<Employee> list=repo.findAllByManager(emp.getEid());
                    if(hasData(list))
                    {
                        // Not able to delete
                        return new ResponseEntity(messageUtil.getMessage("UNABLE_TO_DELETE_DIRECTOR"),HttpStatus.BAD_REQUEST);
                    }
                    else
                    {
                        //Able to delete
                        repo.delete(emp);
                        return new ResponseEntity(messageUtil.getMessage("DELETED"),HttpStatus.NO_CONTENT);
                    }
                }
                //IF NOT DIRECTOR THEN
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
                    return new ResponseEntity(messageUtil.getMessage("DELETED"),HttpStatus.NO_CONTENT);
                }
            }
            //IF EMPLOYEE DOESN'T EXIST
            else
            {
                return new ResponseEntity(messageUtil.getMessage("EMP_NOT_EXISTS"),HttpStatus.NOT_FOUND);
            }
        }
//CREATE NEW USER AND ADD IT INTO EMS
        public ResponseEntity addUser(postRequest employee)
        {
            String empName=employee.getName();
            String desg=employee.getJobTitle();
            Integer parentId=employee.getManagerId();
            //CHECKING MANAGER IS PRESENT OR NOT
           if(parentId!=null){
               if(parentId.intValue()<0){
                   parentId=null;
               }
           }
           //IF NO DETAILS GIVEN FOR NEW CREATION OF EMPLOYEE
          if(empName==null && desg==null && parentId==null){
              return new ResponseEntity(messageUtil.getMessage("INSUFFICIENT_DATA"),HttpStatus.BAD_REQUEST);
          }
          //IF DESIGNATION AVAILABLE
         if(desg!=null) {

             if (!isDesignationValid(desg)) {
                 return new ResponseEntity(messageUtil.getMessage("INVALID_DESIGNATION"), HttpStatus.BAD_REQUEST);
             }
         }
         else{
             return new ResponseEntity(messageUtil.getMessage("NULL_DESIGNATION"),HttpStatus.BAD_REQUEST);
         }
         //IF NAME IS VALID OR NOT
         if(!isValid(empName)){
             return new ResponseEntity(messageUtil.getMessage("INVALID_EMP_NAME"),HttpStatus.BAD_REQUEST);
         }
          //IF MANAGER IS NULL
         if(parentId==null) {
                Employee director = repo.findByManager(null);
                if (director != null) {
                    return new ResponseEntity(messageUtil.getMessage("DIRECTOR_EXISTS"), HttpStatus.BAD_REQUEST);
                }
                else
                {   //MANAGER IS NULL IT MEANS IT IS DIRECTOR
                    if(desg.equals("Director"))
                    {
                        Designation designation=depo.findByDesignation(desg);
                        Employee emp=new Employee(designation,parentId,empName);
                        repo.save(emp);
                        return new ResponseEntity(emp,HttpStatus.CREATED);
                    }
                    else
                    {
                        return new ResponseEntity(messageUtil.getMessage("NO_DIRECTOR_EXISTS"),HttpStatus.BAD_REQUEST);
                    }

                }
            }
           //IF MANAGER ID NOT NULL
            else
            {
                Employee parent=repo.findByEid(parentId);
                //IF MANAGER IS NOT PRESENT
                if(parent==null)
                {
                    return new ResponseEntity(messageUtil.getMessage("PARENT_NOT_EXISTS"), HttpStatus.BAD_REQUEST);
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
                        return new ResponseEntity(messageUtil.getMessage("INVALID_PARENT"),HttpStatus.BAD_REQUEST);
                    }
                }
            }
        }
//PUT API WHERE EMPLOYEE GETS UPDATED
        public ResponseEntity updateUser(Integer eid, putRequest emp)
        {
            //User Is Present
            if(eid<0){
                return new ResponseEntity(messageUtil.getMessage("INVALID_ID"),HttpStatus.BAD_REQUEST);
            }
              //NO DATA IS AVAILABLE
            if(emp.getName()==null && emp.getJobTitle()==null && emp.getManagerId()==null && emp.isReplace()==false){
                return new ResponseEntity("NO DATA",HttpStatus.BAD_REQUEST);
            }
            if(emp.getManagerId()!=null){
               if(!isValidId(emp.getManagerId())){
                   return new ResponseEntity(messageUtil.getMessage("INVALID_MANAGER"),HttpStatus.BAD_REQUEST);
                }
            }
             //IF EMPLOYEE EXISTS
            if(userExists(eid))
            {
                String userDesignation=emp.getJobTitle();
               //IF REPLACING IS TRUE
                if(emp.isReplace()) {
                    //CHECK DESIGNATION PRESENT OR NOT
                    if (userDesignation == null)
                        return new ResponseEntity(messageUtil.getMessage("NULL_DESIGNATION"), HttpStatus.BAD_REQUEST);
                    else {
                        if (!isDesignationValid(userDesignation))
                            return new ResponseEntity(messageUtil.getMessage("INVALID_DESIGNATION"), HttpStatus.BAD_REQUEST);
                    }
                    if (!isValid(emp.getName())) {
                        return new ResponseEntity(messageUtil.getMessage("INVALID_EMP_NAME"), HttpStatus.BAD_REQUEST);
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
                    }
                    //MANAGER IS NOT PRESENT
                    else
                        return new ResponseEntity(messageUtil.getMessage("INVALID_PARENT"), HttpStatus.BAD_REQUEST);
                }
                else
                {

                    userDesignation=emp.getJobTitle();
                    Employee employee=repo.findByEid(eid);
                    Integer parentId=emp.getManagerId();


                    if(userDesignation!=null)
                    {
                        if (!isDesignationValid(userDesignation))
                            return new ResponseEntity(messageUtil.getMessage("INVALID_DESIGNATION"),HttpStatus.BAD_REQUEST);
                        else
                        {
                            if(isGreaterThanChilds(eid,userDesignation) && isSmallerThanParent(eid,userDesignation))
                            {
                                employee.setDesignation(depo.findByDesignation(userDesignation));
                            }
                            else
                            {
                                return new ResponseEntity(messageUtil.getMessage("INVALID_PARENT"),HttpStatus.BAD_REQUEST);
                            }
                        }
                    }
                    if(emp.getName()!=null){
                        if(emp.getName().trim().equals("")){
                            return new ResponseEntity(messageUtil.getMessage("BLANK_NAME"),HttpStatus.BAD_REQUEST);
                        }
                    }

                    if(parentId!=null)
                    {
                        if(!userExists(parentId))
                            return new ResponseEntity(messageUtil.getMessage("PARENT_NOT_EXISTS"),HttpStatus.BAD_REQUEST);
                        else
                            {

                            if(isGreaterThanCurrentDesignation(eid,repo.findByEid(parentId).getDesignation().getDesignation()))
                            {
                                employee.setManager(parentId);
                            }
                            else
                            {
                                return new ResponseEntity(messageUtil.getMessage("INVALID_PARENT"),HttpStatus.BAD_REQUEST);
                            }
                        }
                    }

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
                return new ResponseEntity(messageUtil.getMessage("EMP_NOT_EXISTS"), HttpStatus.BAD_REQUEST);
            }

        }
    }





