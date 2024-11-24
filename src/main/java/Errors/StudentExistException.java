package Errors;

public class StudentExistException extends Exception{
    public StudentExistException(){
        super("Student already exist!!");
    }
}
