#EMP-ORG-CHART
A Spring Boot project for Employee Management System

## Table of Contents

- [Database](#database)
    - [DesignationInformation](#designation)
    - [EmployeeInformation](#employee)
- [Example Data](#example-data)
    - [DesignationInformation](#designation)
    - [EmployeeInformation](#employee)
- [API](#api)
    
## Database

#### Designation
- id: Integer (Primary Key)
- level: Float
- title: String

#### Employee
- id: Integer (Primary Key)
- name: String
- manager: Integer
- designation: Designation (Reference)

### Example Data

Designation

| id         | title       | level     |
| ---------- | ------------| --------- |
| 1          | Director    | 1.0       |
| 2          | Manager     | 10.0      |
| 3          | Lead        | 20.0      |
| 4          | Developer   | 30.0      |
| 5          | Devops      | 30.0      | 
| 6          | QA          | 30.0      |
| 7          | Intern      | 40.0      |

Employee

| id  | name            | manager | designation   |
| --- | --------------- | ------- | ------------- |
| 1   | Thor            | null    | 1 (Director)  |
| 2   | Iron Man        | 1       | 2 (Manager)   |
| 3   | Hulk            | 1       | 3 (Lead)      |
| 4   | Captain America | 1       | 2 (Manager)   |
| 5   | War Machine     | 2       | 6 (QA)        |
| 6   | Vision          | 2       | 5 (DevOps)    |
| 7   | Falcon          | 4       | 4 (Developer) |
| 8   | Ant Man         | 4       | 3 (Lead)      |
| 9   | Spider Man      | 2       | 7 (Intern)    |
| 10  | Black kWidow    | 3       | 4 (Developer) |

## API

#### Error Codes
- 200: OK
- 201: Created
- 400: Bad Request
- 404: Resource Not Found


#### GET /employee

Returns list of all employees

Request
```
GET rest/employees
```

Response
```json

[
  {
    "manager": null,
    "jobtitle": "Director",
    "id": 1,
    "name": "THOR"
  },
  {
    "manager": 1,
    "jobtitle": "Manager",
    "id": 4,
    "name": "CAPTAINAMERICA"
  },
  {
    "manager": 1,
    "jobtitle": "Manager",
    "id": 2,
    "name": "IRONMAN"
  },
  {
    "manager": 4,
    "jobtitle": "Lead",
    "id": 9,
    "name": "ANTMAN"
  },
  {
    "manager": 1,
    "jobtitle": "Lead",
    "id": 3,
    "name": "HULK"
  },
  {
    "manager": 3,
    "jobtitle": "Developer",
    "id": 7,
    "name": "BLACKWIDOW"
  },
  {
    "manager": 4,
    "jobtitle": "DevOps",
    "id": 8,
    "name": "FALCON"
  },
  {
    "manager": 2,
    "jobtitle": "DevOps",
    "id": 6,
    "name": "VISION"
  },
  {
    "manager": 2,
    "jobtitle": "QA",
    "id": 5,
    "name": "WARMACHINE"
  },
  {
    "manager": 2,
    "jobtitle": "Intern",
    "id": 10,
    "name": "SPIDERMAN"
  }
]
```

### POST rest/employees

Add a new employee

Body
```json
{
  "name": "String Required - Employee Name",
  "jobTitle": "String Required - Employee Designation",
  "managerId": "Integer Optional - Manager Employee ID, Required if current employee is not Director"
}
```

Request
```
POST /employee
body: {
    "name": "Dr Strange",
    "jobTitle": "Manager",
    "managerId": 1
}
```

Response

```json
{
    "id": 11,
    "name": "Dr Strange",
    "jobTitle": "Manager",
    "manager": {
        "id": 1,
        "name": "Thor",
        "jobTitle": "Director"
    },
    "colleagues": [
        {
            "id": 4,
            "name": "Captain America",
            "jobTitle": "Manager"
        },
        {
            "id": 2,
            "name": "Iron Man",
            "jobTitle": "Manager"
        },
        {
            "id": 3,
            "name": "Hulk",
            "jobTitle": "Lead"
        }
    ]
}
```

### GET rest/employees/{id}

Returns info of specific employee according to ID

Request
```
GET rest/employees/2
```

Response
```json
{
  "id": 2,
  "name": "IronMan",
  "jobTitle": "Manager",
  "manager": {
    "id": 1,
    "name": "Thor",
    "jobTitle": "Director"         
  },
  "colleagues": [
    {
      "id":4,
      "name":"Captain America",
      "jobTitle":"Manager"
    },
    {
      "id":3,
      "name":"Hulk",
      "jobTitle":"Lead"
    }
  ],
  "subordinates": [
    {
      "id":6,
      "name":"Vision",
      "jobTitle":"DevOps"
    },
    {
      "id":5,
      "name":"War Machine",
      "jobTitle":"QA"
    },
    {
      "id":9,
      "name":"Spider Man",
      "jobTitle":"Intern"
    }
  ]
}
```

#### PUT /rest/employee/${id}

Update or replace employee by ID

Body
```json
{
  "name": "String Required - Employee Name",
  "jobTitle": "String Required - Employee Designation",
  "managerId": "Integer Optional - Manager Employee ID, Required if current employee is not Director",
  "replace": "Boolean Optional - Replace old employee with current employee"
}
```

Request
```
PUT rest/employees/3
body: {
    "name": "Black Panther",
    "jobTitle": "Lead",
    "managerId": 1,
    "replace": true
}
```

Response
```json
{
    "id": 12,
    "name": "Black Panther",
    "jobTitle": "Lead",
    "manager": {
        "id": 1,
        "name": "Thor",
        "jobTitle": "Director"
    },
    "colleagues": [
        {
            "id": 4,
            "name": "Captain America",
            "jobTitle": "Manager"
        },
        {
            "id": 2,
            "name": "Iron Man",
            "jobTitle": "Manager"
        }
    ],
    "subordinates": [
      {
        "id":10,
        "name":"Black Widow",
        "jobTitle":"Developer"
      }
    ]
}
```

### DELETE rest/employees/${id}

Delete employee by ID

Request
```
DELETE rest/employees/10
```

Response
```
OK
```
