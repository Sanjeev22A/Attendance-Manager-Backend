package com.AttendanceManager.att_man.Controller;

import Errors.*;
import com.AttendanceManager.att_man.Utils.PasswordUtils;
import com.AttendanceManager.att_man.model.AttendHr;
import com.AttendanceManager.att_man.model.Student;
import com.AttendanceManager.att_man.Service.StudentService;
import com.AttendanceManager.att_man.model.Subject;
import io.netty.handler.codec.http2.Http2Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;
    @GetMapping("/test")
    public String testing(){
        return "working";
    }
    // Create a new student
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createStudent(@RequestBody Student student) {
        System.out.println("Entering into create student");
        Student savedStudent = null;

        try {
            savedStudent = studentService.saveStudent(student);
        } catch (StudentExistException e) {
            return new ResponseEntity<>(student.getRegno()+" "+e.getMessage(),HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
    }



    // Get all students
    @GetMapping("Student/all")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }
    @GetMapping("Student/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable long id){
        Optional<Student> stud=studentService.getStudentByRegNo(id);
        if(stud.isPresent()){
            return new ResponseEntity<>(stud.get(),HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{regno}")
    public ResponseEntity<?> deleteStudent(@PathVariable long regno){
        try{
            studentService.deleteStudentByRegNo(regno);
            return new ResponseEntity<>("Student with regno : "+regno+" deleted successfully",HttpStatus.OK);

        } catch (StudentNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/Student/update/{regno}")
    public ResponseEntity<?> updateStudent(@PathVariable long regno,@RequestBody Student updateStudent){
        try {
            Student student=studentService.updateStudent(regno, updateStudent);
            return new ResponseEntity<>(student,HttpStatus.OK);
        } catch (StudentNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    //Add a new subject to the subject list for a particular student identified by regno
    @PostMapping("/Student/{regno}/addSubject")
    public ResponseEntity<?> addSubjectToStudent(@PathVariable long regno, @RequestBody Subject subject){
        try{
            Student updateStudent=studentService.addSubjectToStudent(regno, subject);
            return new ResponseEntity<>(updateStudent,HttpStatus.OK);
        } catch (StudentNotFoundException | SubjectExistException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    //Updating a particular subject's details
    @PutMapping("/Student/{regno}/Subject/{subjectCode}")
    public ResponseEntity<?> updateSubject(@PathVariable long regno,@PathVariable String subjectCode,@RequestBody Subject updateSubject){
        try{
            Student updateStudent=studentService.updateSubjectDetails(regno,subjectCode,updateSubject);
            return new ResponseEntity<>(updateStudent,HttpStatus.OK);
        }
        catch (StudentNotFoundException| SubjectNotFoundException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/add-class/{regno}/{subjectCode}/{classDate}")
    public ResponseEntity<?> addClassToSubject(@PathVariable long regno, @PathVariable String subjectCode, @PathVariable String classDate, @RequestBody AttendHr attendHr) {
        try {
            Student updatedStudent = studentService.addClassToSubject(regno, subjectCode, classDate, attendHr);
            return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
        } catch (StudentNotFoundException | SubjectNotFoundException | IllegalArgumentException |
                 ClassDateExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/Student/{regno}/subjects")
    public ResponseEntity<?> fetchStudentSubject(@PathVariable long regno){
        try{
            List<Subject> subjectList = studentService.getStudentSubject(regno);
            return new ResponseEntity<>(subjectList,HttpStatus.OK);
        } catch (StudentNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/Student/validateLogin/{regno}")
    public  ResponseEntity<?> validatePassword(@PathVariable long regno , @RequestBody Student student ){
        try{
            Optional<Student> st = studentService.getStudentByRegNo(regno);
            if(st.isPresent()){
//                System.out.println("Student Password from request: "+student.getPassword());
//                System.out.println("DB: "+st.get().getPassword());
//
                String studentPassword = student.getPassword();
//                System.out.println("hashed Pass: "+hashedPassword);
//                boolean temp = PasswordUtils.validatePassword(st.get().getPassword(),student.getPassword());
                boolean temp =studentPassword.equals(st.get().getPassword());

                System.out.println(temp);
                if(temp)
                    return new ResponseEntity<> (true,HttpStatus.OK);
                else
                    return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);
            }else{
                return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }

    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerStudent(@RequestParam long regNo, @RequestParam String name, @RequestParam String password) {
        try {
            // Create new student object with provided data
            Student newStudent = new Student();
            newStudent.setRegno(regNo);
            newStudent.setName(name);
            newStudent.setPassword(password);  // Assuming there's a password field in Student model

            // Save the student to the database using the service
            Student savedStudent = studentService.saveStudent(newStudent);

            return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
        } catch (StudentExistException e) {
            return new ResponseEntity<>(regNo + " " + e.getMessage(), HttpStatus.CONFLICT);
        }
    }
}
