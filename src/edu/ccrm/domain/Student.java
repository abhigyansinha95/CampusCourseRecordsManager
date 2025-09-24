package edu.ccrm.domain;

import java.util.ArrayList;
import java.util.List;

public class Student extends Person {
    private String regNo;
    private boolean active;
    private List<Enrollment> enrollments; // Now using Enrollment objects
    
    public Student(String id, String regNo, String fullName, String email) {
        super(id, fullName, email);
        this.regNo = regNo;
        this.active = true;
        this.enrollments = new ArrayList<>();
    }
    
    @Override
    public void displayProfile() {
        System.out.println("=== Student Profile ===");
        System.out.println("Registration No: " + regNo);
        System.out.println("Full Name: " + getFullName());
        System.out.println("Email: " + getEmail());
        System.out.println("Status: " + (active ? "Active" : "Inactive"));
        System.out.println("Courses Enrolled: " + enrollments.size());
    }
    
    // Updated enrollment method using Course objects
    public Enrollment enrollInCourse(Course course) {
        Enrollment enrollment = new Enrollment(this, course);
        enrollments.add(enrollment);
        System.out.println("Enrolled " + getFullName() + " in " + course.getCode());
        return enrollment;
    }
    
    public void displayEnrolledCourses() {
        System.out.println("Courses enrolled by " + getFullName() + ":");
        for (Enrollment enrollment : enrollments) {
            System.out.println("- " + enrollment.getCourse().getCode() + 
                             " (" + enrollment.getCourse().getTitle() + ")");
        }
    }
    
    public int getTotalCredits() {
        return enrollments.stream()
                         .mapToInt(e -> e.getCourse().getCredits())
                         .sum();
    }
    
    // Getters and setters
    public String getRegNo() { return regNo; }
    public void setRegNo(String regNo) { this.regNo = regNo; }
    
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    
    public List<Enrollment> getEnrollments() { return enrollments; }
    
    @Override
    public String toString() {
        return String.format("Student[RegNo: %s, Name: %s, Email: %s, Credits: %d]", 
                           regNo, getFullName(), getEmail(), getTotalCredits());
    }
}