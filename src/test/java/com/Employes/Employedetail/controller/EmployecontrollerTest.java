package com.Employes.Employedetail.controller;

import com.Employes.Employedetail.EmployedetailApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@EnableWebMvc
@WebAppConfiguration
@ContextConfiguration(classes = {EmployedetailApplication.class})
public class EmployecontrollerTest extends AbstractTransactionalTestNGSpringContextTests
{
    @Autowired
    WebApplicationContext context;
    private MockMvc mvc;
    @BeforeMethod
    public void setUp()
    {
        mvc= MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testGet() throws Exception
    {
        mvc.perform(MockMvcRequestBuilders.get("/rest/employees")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(10)));
    }


   //GET API TESTS------------------------------------------------------------------------------------------------------------


   @Test(priority = 2)
   public void getUserTest() throws Exception
   {
       MvcResult result=mvc.perform(MockMvcRequestBuilders.get("/rest/employees/1"))
               .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andDo(print()).andReturn();
       String jsonOutput=result.getResponse().getContentAsString();
       int length= JsonPath.parse(jsonOutput).read("$.length()");
       System.out.println(length);
       Assert.assertTrue(length>0);
   }


    @Test(priority = 3)
    public void getUserTestInvalidParent() throws Exception
    {
        MvcResult result=mvc.perform(MockMvcRequestBuilders.get("/rest/employees/15"))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andDo(print()).andReturn();
    }

    @Test(priority = 4)
    public void getUserTestNullParent() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/rest/employees/null"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andDo(print()).andReturn();
    }





    //POST API TESTS----------------------------------------------------------------------------------------------------------



    @Test(priority = 0)
    public void createEmployeeTest() throws Exception                //post is working or not
    {
         postRequest p1 =new postRequest("wonder woman","INTERN",2);
        ObjectMapper mapper=new ObjectMapper();
        String jsonInput=mapper.writeValueAsString(p1);
        MvcResult result=mvc.perform(MockMvcRequestBuilders.post("/rest/employees").content(jsonInput).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String resultOutput=result.getResponse().getContentAsString();
        Assert.assertEquals("Employee added successfully",resultOutput);
    }

    @Test(priority = 1)
    public void directorValidationForManager() throws Exception                //Assigning director with manager
    {
        postRequest p1 =new postRequest("wonder woman","Director",2);
        ObjectMapper mapper=new ObjectMapper();
        String jsonInput=mapper.writeValueAsString(p1);
        mvc.perform(MockMvcRequestBuilders.post("/rest/employees").content(jsonInput).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }

    @Test
    public void multipleDirector() throws Exception                             //Adding second Director Work On THIS
    {
        postRequest p1 =new postRequest("wonder woman","Director",2);
        ObjectMapper mapper=new ObjectMapper();
        String jsonInput=mapper.writeValueAsString(p1);
        mvc.perform(MockMvcRequestBuilders.post("/rest/employees").content(jsonInput).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }

    @Test
    public void noData () throws Exception                //Adding employee with no data
    {
        postRequest p1 =new postRequest();
        ObjectMapper mapper=new ObjectMapper();
        String jsonInput=mapper.writeValueAsString(p1);
        mvc.perform(MockMvcRequestBuilders.post("/rest/employees").content(jsonInput).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }

    @Test
    public void partialData () throws Exception                //Adding employee with partial data
    {
        postRequest p1 =new postRequest(null,"wonder woman",2);
        ObjectMapper mapper=new ObjectMapper();
        String jsonInput=mapper.writeValueAsString(p1);
        mvc.perform(MockMvcRequestBuilders.post("/rest/employees").content(jsonInput).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }

    @Test
    public void invalidParentId () throws Exception                //Adding employee with non existing manager
    {
        postRequest p1 =new postRequest("wonder woman","Lead",-2);
        ObjectMapper mapper=new ObjectMapper();
        String jsonInput=mapper.writeValueAsString(p1);
        mvc.perform(MockMvcRequestBuilders.post("/rest/employees").content(jsonInput).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }
    @Test
    public void hierarchyViolation () throws Exception                //Adding employee with violating organisation hierarchy
    {
        postRequest p1 =new postRequest("wonder woman","Lead",8);
        ObjectMapper mapper=new ObjectMapper();
        String jsonInput=mapper.writeValueAsString(p1);
        mvc.perform(MockMvcRequestBuilders.post("/rest/employees").content(jsonInput).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();

        p1.setJobTitle("manager");
        mvc.perform(MockMvcRequestBuilders.post("/rest/employees").content(mapper.writeValueAsString(p1)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }

    @Test
    public void invalidDesignation () throws Exception                //Adding employee with non existing Designation
    {
        postRequest p1 =new postRequest("wonder woman","Laed",12);
        ObjectMapper mapper=new ObjectMapper();
        String jsonInput=mapper.writeValueAsString(p1);
        mvc.perform(MockMvcRequestBuilders.post("/rest/employees").content(jsonInput).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }

//DELETE API TESTS-----------------------------------------------------------------------------------------------------------


    @Test
    public void delInvalidId () throws Exception                //Deleting non existing employee
    {
        mvc.perform(MockMvcRequestBuilders.delete("/rest/employees/121"))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }

    @Test
    public void delDirectorWithChild () throws Exception                //Deleting director with children
    {
        mvc.perform(MockMvcRequestBuilders.delete("/rest/employees/1")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }

    @Test
    public void delDirectorWithoutChild () throws Exception                //Deleting director with children
    {

        mvc.perform(MockMvcRequestBuilders.delete("/rest/employees/1"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }

    @Test
    public void delEmployee() throws Exception                //Deleting director without children
    {
        for(int i=10;i>0;i--)
        {

            mvc.perform(MockMvcRequestBuilders.delete("/rest/employees/"+i))
                    .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        }

    }

    @Test
    public void addDirectorToEmptyDatabase() throws Exception                //Re-adding director to the empty database after removing all the employees
    {
        for(int i=10;i>0;i--)
        {

            mvc.perform(MockMvcRequestBuilders.delete("/rest/employees/"+i))
                    .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        }

         postRequest p2=new postRequest("wonder woman","Director",null);
        ObjectMapper mapper=new ObjectMapper();
        String jsonInput=mapper.writeValueAsString(p2);
        mvc.perform(MockMvcRequestBuilders.post("/rest/employees").content(jsonInput).contentType(MediaType.APPLICATION_JSON_VALUE)).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    }

    @Test
    public void addManagerToEmptyDatabase() throws Exception                //Re-adding manager to the empty database after removing all the employees
    {
        for(int i=10;i>0;i--)
        {

            mvc.perform(MockMvcRequestBuilders.delete("/rest/employees/"+i))
                    .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        }

        postRequest p2=new postRequest("wonder woman","Manager",null);
        ObjectMapper mapper=new ObjectMapper();
        String jsonInput=mapper.writeValueAsString(p2);
        mvc.perform(MockMvcRequestBuilders.post("/rest/employees").content(jsonInput).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();

    }


   // PUT API  TEST CASES-------------------------------------------------------------------------------------------------------

    @Test
    public void updateEmpInvalidId() throws Exception
    {
        putRequest employee = new putRequest("Rajat","Manager",2,false);
        ObjectMapper mapper = new ObjectMapper();
        String jsonInput = mapper.writeValueAsString(employee);
        mvc.perform(MockMvcRequestBuilders.put("/rest/employees/13").content(jsonInput).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }
    @Test
    public void updateEmpNoData() throws Exception        //error happened
    {
        putRequest employee = new putRequest(null,null,null,false);
        ObjectMapper mapper = new ObjectMapper();
        String jsonInput = mapper.writeValueAsString(employee);
        mvc.perform(MockMvcRequestBuilders.put("/rest/employees/2").content(jsonInput).contentType(MediaType.APPLICATION_JSON_VALUE)).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }
    @Test
    public void updateEmpInvalidParId() throws Exception
    {
        putRequest employee = new putRequest("Mohit","Lead",12343,false);
        ObjectMapper mapper = new ObjectMapper();
        String jsonInput = mapper.writeValueAsString(employee);
        mvc.perform(MockMvcRequestBuilders.put("/rest/employees/2").content(jsonInput).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }
    @Test
    public void updateEmpPromotion() throws Exception           //error happened 2
    {
        putRequest employee = new putRequest("Mohit","Director",1,false);
        ObjectMapper mapper = new ObjectMapper();
        String jsonInput = mapper.writeValueAsString(employee);
        mvc.perform(MockMvcRequestBuilders.put("/rest/employees/2").content(jsonInput).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }
    @Test
    public void updateEmpDemotion() throws Exception
    {
        putRequest employee = new putRequest("Mohit",null,1,false);
        ObjectMapper mapper = new ObjectMapper();
        String jsonInput = mapper.writeValueAsString(employee);
        mvc.perform(MockMvcRequestBuilders.put("/rest/employees/21").content(jsonInput).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }

    @Test
    public void updateEmpDemoteDirector() throws Exception       //error 3
    {
        putRequest employee = new putRequest(null,"Lead",null,false);
        ObjectMapper mapper = new ObjectMapper();
        String jsonInput = mapper.writeValueAsString(employee);
        mvc.perform(MockMvcRequestBuilders.put("/rest/employees/1").content(jsonInput).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }
    @Test
    public void updateEmpDirectorName() throws Exception //error4
    {
        putRequest employee = new putRequest("Rajat",null,null,false);
        ObjectMapper mapper = new ObjectMapper();
        String jsonInput = mapper.writeValueAsString(employee);
        mvc.perform(MockMvcRequestBuilders.put("/rest/employees/1").content(jsonInput).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }
    @Test
    public void updateEmpDirectorWithDirector() throws Exception //error 5
    {
        putRequest employee = new putRequest(null,"director",null,false);
        ObjectMapper mapper = new ObjectMapper();
        String jsonInput = mapper.writeValueAsString(employee);
        mvc.perform(MockMvcRequestBuilders.put("/rest/employees/1").content(jsonInput).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }
    @Test
    public void updateEmpDirectorWithOutDirector() throws Exception //error6
    {
        putRequest employee = new putRequest(null,"manager",null,false);
        ObjectMapper mapper = new ObjectMapper();
        String jsonInput = mapper.writeValueAsString(employee);
        mvc.perform(MockMvcRequestBuilders.put("/rest/employees/1").content(jsonInput).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }
    @Test
    public void updateEmpDirectorParChange() throws Exception //error 7
    {
        putRequest employee = new putRequest(null,null,2,false);
        ObjectMapper mapper = new ObjectMapper();
        String jsonInput = mapper.writeValueAsString(employee);
        mvc.perform(MockMvcRequestBuilders.put("/rest/employees/1").content(jsonInput).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }
    @Test
    public void hulkChildOfCaptain() throws Exception //error8
    {
        putRequest employee = new putRequest(null,null,4,false);
        ObjectMapper mapper = new ObjectMapper();
        String jsonInput = mapper.writeValueAsString(employee);
        mvc.perform(MockMvcRequestBuilders.put("/rest/employees/3").content(jsonInput).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }


}