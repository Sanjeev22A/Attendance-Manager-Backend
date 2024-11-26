package com.AttendanceManager.att_man.Service;

import Errors.*;
import com.AttendanceManager.att_man.Repository.StudentRepository;
import com.AttendanceManager.att_man.model.AttendHr;
import com.AttendanceManager.att_man.model.Student;
import com.AttendanceManager.att_man.model.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private StudentRepository studentRepository;
    //To save a particular student into the repository/db - logic checks if the student exist and if it does throws error else saves the student
    public Student saveStudent(Student student) throws StudentExistException {
        Optional<Student> check=studentRepository.findByregno(student.getRegno());
        if(check.isPresent()){
            throw new StudentExistException();
        }
        else{
            return studentRepository.save(student);
        }

    }
    //Returns info of all the students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    //returns info of a particular student by regno
    public Optional<Student> getStudentByRegNo(long regno){
        return studentRepository.findByregno(regno);
    }

    //To delete a particular student detail by regno
    public void deleteStudentByRegNo(long regno) throws StudentNotFoundException{
        Optional<Student> existingStudent=studentRepository.findByregno(regno);
        if(existingStudent.isPresent()){
            studentRepository.deleteByregno(regno);
        }
        else{
            throw new StudentNotFoundException("Student with regno: "+regno+" doesnt exist!!");
        }
    }

    //To update a student record
    public Student updateStudent(long regno,Student updateStudent) throws StudentNotFoundException{
        Query query=new Query();
        query.addCriteria(Criteria.where("regno").is(regno));
        Update update=new Update();
        System.out.println("Name : "+updateStudent.getName());
        if(updateStudent.getName()!=null){
            update.set("name",updateStudent.getName());

        }
        if(updateStudent.getPassword()!=null){
            update.set("password",updateStudent.getPassword());
        }
        if(updateStudent.getProfilePic()!=null){
            update.set("profilePic",updateStudent.getProfilePic());
        }
        Student updateStudentInDb=mongoTemplate.findAndModify(query,update, Student.class);
        if(updateStudentInDb==null){
            throw new StudentNotFoundException("Student with regno : "+regno+"Doesnt exist!");
        }
        return updateStudentInDb;
    }

    //Fetch Student's Subject
    public List<Subject> getStudentSubject(long regno) throws StudentNotFoundException {
        Optional<Student> st = getStudentByRegNo(regno);

        if(st.isPresent()){
            return st.get().getSubjectList();
        }else{
            throw new StudentNotFoundException("Student with regno :"+regno+" not found");
        }
    }

    //Add a subject to the subject list
    public Student addSubjectToStudent(long regno, Subject newSubject) throws StudentNotFoundException, SubjectExistException {
        Query query=new Query();
        query.addCriteria(Criteria.where("regno").is(regno));
        Student student=mongoTemplate.findOne(query,Student.class);
        System.out.println("Subject code:"+newSubject.getSubjectCode());
        if(student==null){
            throw new StudentNotFoundException("Student with regno :"+regno+" not found");
        }
        List<Subject> subjectList=student.getSubjectList();
        if(subjectList!=null){
            for(Subject sub:subjectList){
                if(sub.getSubjectCode().equals(newSubject.getSubjectCode())){
                    throw new SubjectExistException("For the student with regno : "+regno+" The subject : "+newSubject.getSubjectCode()+" already exist");
                }
            }
        }
        Update update=new Update();
        update.addToSet("subjectList",newSubject);
        mongoTemplate.updateFirst(query,update,Student.class);
        return mongoTemplate.findOne(query, Student.class);
    }


    //Update the subject details for a particular student(identified by register number)
    public Student updateSubjectDetails(long regno, String subjectCode, Subject updateDetails) throws SubjectNotFoundException, StudentNotFoundException {
        Query query = new Query();
        query.addCriteria(Criteria.where("regno").is(regno));
        Student student = mongoTemplate.findOne(query, Student.class);

        if (student == null) {
            throw new StudentNotFoundException("Student with regno: " + regno + " not found.");
        }

        boolean subjectFound = false;
        for (Subject subject : student.getSubjectList()) {
            if (subject.getSubjectCode().equals(subjectCode)) {
                if (updateDetails.getSubjectName() != null) {
                    subject.setSubjectName(updateDetails.getSubjectName());
                }
                if (updateDetails.getContactHours() > 0) {
                    subject.setContactHours(updateDetails.getContactHours());
                }
                if (updateDetails.getHoursAttended() >= 0) {
                    subject.setHoursAttended(updateDetails.getHoursAttended());
                }

                subjectFound = true;
                break;
            }
        }

        if (!subjectFound) {
            throw new SubjectNotFoundException("Subject with code " + subjectCode + " not found for student with regno " + regno);
        }
        Update update=new Update();
        update.set("subjectList",student.getSubjectList());
        mongoTemplate.updateFirst(query,update,Student.class);
        return student;
    }
    //Adding a particular class to the class map
    public Student addClassToSubject(long regno, String subjectCode, String classDate, AttendHr attendHr) throws StudentNotFoundException, SubjectNotFoundException, ClassDateExistException {
        Query query = new Query();
        query.addCriteria(Criteria.where("regno").is(regno));
        Student student = mongoTemplate.findOne(query, Student.class);

        if (student == null) {
            throw new StudentNotFoundException("Student with regno: " + regno + " not found.");
        }

        Subject subject = null;
        for (Subject sub : student.getSubjectList()) {
            if (sub.getSubjectCode().equals(subjectCode)) {
                subject = sub;
                break;
            }
        }

        if (subject == null) {
            throw new SubjectNotFoundException("Subject with code " + subjectCode + " not found for student with regno " + regno);
        }

        if (subject.getClassMap().containsKey(classDate)) {
            throw new ClassDateExistException("Class for date " + classDate + " already exists.");
        }

        subject.addClassAttendance(classDate, attendHr);

        Update update = new Update();
        update.set("subjectList", student.getSubjectList());

        mongoTemplate.updateFirst(query, update, Student.class);

        return student;
    }


}
