package edu.ccrm.domain;

import java.time.LocalDateTime;

public class Enrollment {
    private String enrollmentId;
    private Student student;
    private Course course;
    private LocalDateTime enrollmentDate;
    private Double marks;
    private Grade grade;
    
    public Enrollment(Student student, Course course) {
        this.enrollmentId = generateEnrollmentId();
        this.student = student;
        this.course = course;
        this.enrollmentDate = LocalDateTime.now();
    }
    
    private String generateEnrollmentId() {
        return "ENR" + System.currentTimeMillis();
    }
  
    public void recordMarks(double marks) {
        this.marks = marks;
        this.grade = Grade.fromScore(marks);
    }
    
    public double calculateGradePoints() {
        return (grade != null) ? grade.getPoints() * course.getCredits() : 0.0;
    }
    
    public boolean isGraded() {
        return grade != null;
    }
   
    public String getEnrollmentId() { return enrollmentId; }
    public Student getStudent() { return student; }
    public Course getCourse() { return course; }
    public LocalDateTime getEnrollmentDate() { return enrollmentDate; }
    public Double getMarks() { return marks; }
    public Grade getGrade() { return grade; }
    
    @Override
    public String toString() {
        return String.format("Enrollment[%s: %s in %s, Grade: %s]", 
                           enrollmentId, student.getFullName(), course.getCode(), 
                           grade != null ? grade : "Not Graded");
    }
}