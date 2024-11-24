# att-man
 backend for the attendance manager application
Apologies for the confusion! Here's the full `README.md` formatted correctly as a single document:

```markdown
# Attendance Manager REST API

## Overview
The Attendance Manager API provides endpoints for managing students, subjects, and attendance. It supports operations such as creating students, updating records, adding subjects, and managing attendance for specific classes.

## Base URL
`http://<your-server-address>/students`

---

## Endpoints

### 1. Test the API
- **Endpoint**: `/test`  
- **Method**: `GET`  
- **Description**: Check if the API is working.  
- **Response**:  
  `working`

---

### 2. Create a New Student
- **Endpoint**: `/create`  
- **Method**: `POST`  
- **Description**: Create a new student record.  
- **Request Body**:
  ```json
  {
    "regno": 123456,
    "name": "John Doe",
    "password": "securepassword",
    "subjectList": [],
    "profilePic": "base64EncodedString"
  }
  ```
- **Response**:
  - HTTP 201 Created
    ```json
    {
      "regno": 123456,
      "name": "John Doe",
      "password": "hashedPassword",
      "subjectList": [],
      "profilePic": "base64EncodedString"
    }
    ```
  - HTTP 409 Conflict (if student already exists)
    ```json
    Registration number already exists.
    ```

---

### 3. Get All Students
- **Endpoint**: `/Student/all`  
- **Method**: `GET`  
- **Description**: Retrieve all student records.  
- **Response**:
  - HTTP 200 OK
    ```json
    [
      {
        "regno": 123456,
        "name": "John Doe",
        "password": "hashedPassword",
        "subjectList": [],
        "profilePic": "base64EncodedString"
      }
    ]
    ```

---

### 4. Get a Student by Registration Number
- **Endpoint**: `/Student/{id}`  
- **Method**: `GET`  
- **Description**: Retrieve a student by their registration number.  
- **Path Variable**: `{id}` - The student's registration number.  
- **Response**:
  - HTTP 302 Found
    ```json
    {
      "regno": 123456,
      "name": "John Doe",
      "password": "hashedPassword",
      "subjectList": [],
      "profilePic": "base64EncodedString"
    }
    ```
  - HTTP 404 Not Found
    ```json
    Student not found.
    ```

---

### 5. Delete a Student
- **Endpoint**: `/delete/{regno}`  
- **Method**: `DELETE`  
- **Description**: Delete a student by their registration number.  
- **Path Variable**: `{regno}` - The student's registration number.  
- **Response**:
  - HTTP 200 OK
    ```json
    Student with regno: 123456 deleted successfully.
    ```
  - HTTP 404 Not Found
    ```json
    Student not found.
    ```

---

### 6. Update a Student
- **Endpoint**: `/Student/update/{regno}`  
- **Method**: `PUT`  
- **Description**: Update an existing student's details.  
- **Path Variable**: `{regno}` - The student's registration number.  
- **Request Body**:
  ```json
  {
    "regno": 123456,
    "name": "John Smith",
    "password": "newpassword",
    "subjectList": [],
    "profilePic": "newBase64EncodedString"
  }
  ```
- **Response**:
  - HTTP 200 OK
    ```json
    {
      "regno": 123456,
      "name": "John Smith",
      "password": "updatedHashedPassword",
      "subjectList": [],
      "profilePic": "newBase64EncodedString"
    }
    ```
  - HTTP 404 Not Found
    ```json
    Student not found.
    ```

---

### 7. Add a Subject to a Student
- **Endpoint**: `/Student/{regno}/addSubject`  
- **Method**: `POST`  
- **Description**: Add a new subject to a student's subject list.  
- **Path Variable**: `{regno}` - The student's registration number.  
- **Request Body**:
  ```json
  {
    "subjectCode": "CS101",
    "subjectName": "Computer Science",
    "contactHours": 45,
    "hoursAttended": 0,
    "classMap": {}
  }
  ```
- **Response**:
  - HTTP 200 OK
    ```json
    {
      "regno": 123456,
      "name": "John Doe",
      "password": "hashedPassword",
      "subjectList": [
        {
          "subjectCode": "CS101",
          "subjectName": "Computer Science",
          "contactHours": 45,
          "hoursAttended": 0,
          "classMap": {}
        }
      ],
      "profilePic": "base64EncodedString"
    }
    ```
  - HTTP 404 Not Found
    ```json
    Student not found.
    ```

---

### 8. Update Subject Details
- **Endpoint**: `/Student/{regno}/Subject/{subjectCode}`  
- **Method**: `PUT`  
- **Description**: Update the details of a subject for a student.  
- **Path Variables**:
  - `{regno}`: The student's registration number.
  - `{subjectCode}`: The subject's code.  
- **Request Body**:
  ```json
  {
    "subjectCode": "CS101",
    "subjectName": "Advanced Computer Science",
    "contactHours": 50,
    "hoursAttended": 10,
    "classMap": {}
  }
  ```
- **Response**:
  - HTTP 200 OK
    ```json
    {
      "regno": 123456,
      "name": "John Doe",
      "password": "hashedPassword",
      "subjectList": [
        {
          "subjectCode": "CS101",
          "subjectName": "Advanced Computer Science",
          "contactHours": 50,
          "hoursAttended": 10,
          "classMap": {}
        }
      ],
      "profilePic": "base64EncodedString"
    }
    ```
  - HTTP 404 Not Found
    ```json
    Subject not found.
    ```

---

### 9. Add Class Attendance
- **Endpoint**: `/add-class/{regno}/{subjectCode}/{classDate}`  
- **Method**: `POST`  
- **Description**: Add attendance details for a specific class.  
- **Path Variables**:
  - `{regno}`: The student's registration number.
  - `{subjectCode}`: The subject's code.
  - `{classDate}`: The date of the class in `YYYY-MM-DD` format.  
- **Request Body**:
  ```json
  {
    "attend": true,
    "hours": 2
  }
  ```
- **Response**:
  - HTTP 200 OK
    ```json
    {
      "regno": 123456,
      "name": "John Doe",
      "password": "hashedPassword",
      "subjectList": [
        {
          "subjectCode": "CS101",
          "subjectName": "Computer Science",
          "contactHours": 45,
          "hoursAttended": 2,
          "classMap": {
            "2024-11-24": {
              "attend": true,
              "hours": 2
            }
          }
        }
      ],
      "profilePic": "base64EncodedString"
    }
    ```
  - HTTP 400 Bad Request
    ```json
    Invalid class date.
    ```

---
```

This should now be the complete `README.md` with all your API endpoints, formatted and ready for use.
