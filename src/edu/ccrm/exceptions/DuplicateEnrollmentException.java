package edu.ccrm.exceptions;

public class DuplicateEnrollmentException extends Exception {
    public DuplicateEnrollmentException(String message) {
        super(message);
    }
    
    public DuplicateEnrollmentException(String studentName, String courseCode) {
        super("Student " + studentName + " is already enrolled in course " + courseCode);
    }
}