package com.AttendanceManager.att_man.Repository;

import Errors.StudentExistException;
import com.AttendanceManager.att_man.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends MongoRepository<Student,Long> {

    Optional<Student> findByregno(long regno);
    void deleteByregno(long regno);
//    Optional<Student> findByname(String name);
}
