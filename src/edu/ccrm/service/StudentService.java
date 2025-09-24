package edu.ccrm.service;

import edu.ccrm.domain.Student;
import edu.ccrm.domain.Course;
import edu.ccrm.domain.Enrollment;
import edu.ccrm.domain.Grade;
import edu.ccrm.exceptions.DuplicateEnrollmentException;
import edu.ccrm.exceptions.MaxCreditLimitExceededException;

import java.util.*;
import java.util.stream.Collectors;

public class StudentService {
    private final List<Student> students;
    private static final int MAX_CREDITS_PER_SEMESTER = 18;
    
    public StudentService() {
        this.students = new ArrayList<>();
    }
 
    public void addStudent(Student student) {
        students.add(student);
        System.out.println("Added student: " + student.getFullName());
    }
   
    public Optional<Student> findStudentById(String id) {
        return students.stream()
                      .filter(s -> s.getId().equals(id))
                      .findFirst();
    }
    
    public Optional<Student> findStudentByRegNo(String regNo) {
        return students.stream()
                      .filter(s -> s.getRegNo().equals(regNo))
                      .findFirst();
    }
    
    public Enrollment enrollStudentInCourse(Student student, Course course) 
            throws DuplicateEnrollmentException, MaxCreditLimitExceededException {
        
        boolean alreadyEnrolled = student.getEnrollments().stream()
            .anyMatch(e -> e.getCourse().getCode().equals(course.getCode()));
        
        if (alreadyEnrolled) {
            throw new DuplicateEnrollmentException(student.getFullName(), course.getCode());
        }
        
        int currentCredits = student.getTotalCredits();
        int attemptedCredits = currentCredits + course.getCredits();
        
        if (attemptedCredits > MAX_CREDITS_PER_SEMESTER) {
            throw new MaxCreditLimitExceededException(currentCredits, course.getCredits(), MAX_CREDITS_PER_SEMESTER);
        }
        
        Enrollment enrollment = student.enrollInCourse(course);
        System.out.println("Successfully enrolled " + student.getFullName() + " in " + course.getCode());
        return enrollment;
    }
    
    public List<Student> getActiveStudents() {
        return students.stream()
                      .filter(Student::isActive)
                      .collect(Collectors.toList());
    }
    
    public List<Student> getStudentsByDepartment(String department) {
        return students.stream()
                      .filter(s -> s.getEnrollments().stream()
                                  .anyMatch(e -> e.getCourse().getDepartment().equals(department)))
                      .collect(Collectors.toList());
    }
    
    public double calculateGPA(Student student) {
        if (student.getEnrollments().isEmpty()) {
            return 0.0;
        }
        
        double totalGradePoints = student.getEnrollments().stream()
            .filter(Enrollment::isGraded)
            .mapToDouble(Enrollment::calculateGradePoints)
            .sum();
            
        int totalCredits = student.getEnrollments().stream()
            .filter(Enrollment::isGraded)
            .mapToInt(e -> e.getCourse().getCredits())
            .sum();
            
        return totalCredits > 0 ? totalGradePoints / totalCredits : 0.0;
    }
    
    public List<Student> getTopStudents(int count) {
        return students.stream()
                      .filter(s -> !s.getEnrollments().isEmpty())
                      .sorted((s1, s2) -> Double.compare(calculateGPA(s2), calculateGPA(s1)))
                      .limit(count)
                      .collect(Collectors.toList());
    }
    
    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }
}