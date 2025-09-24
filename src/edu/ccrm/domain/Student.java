package edu.ccrm.domain;

import java.util.ArrayList;
import java.util.List;

public class Student extends Person {
    private String regNo;
    private boolean active;
    private List<String> enrolledCourses; 
    
    public Student(String id, String regNo, String fullName, String email) {
        super(id, fullName, email); 
        this.regNo = regNo;
        this.active = true;
        this.enrolledCourses = new ArrayList<>();
    }
    
    @Override
    public void displayProfile() {
        System.out.println("=== Student Profile ===");
        System.out.println("Registration No: " + regNo);
        System.out.println("Full Name: " + getFullName());
        System.out.println("Email: " + getEmail());
        System.out.println("Status: " + (active ? "Active" : "Inactive"));
        System.out.println("Courses Enrolled: " + enrolledCourses.size());
    }
    
    public void enrollInCourse(String courseCode) {
        enrolledCourses.add(courseCode);
        System.out.println("Enrolled in: " + courseCode);
    }
    
    public void displayEnrolledCourses() {
        System.out.println("Courses enrolled by " + getFullName() + ":");
        for (String course : enrolledCourses) {
            System.out.println("- " + course);
        }
    }
    
    public String getRegNo() { return regNo; }
    public void setRegNo(String regNo) { this.regNo = regNo; }
    
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    
    public List<String> getEnrolledCourses() { return enrolledCourses; }
    
    @Override
    public String toString() {
        return String.format("Student[RegNo: %s, Name: %s, Email: %s]", 
                           regNo, getFullName(), getEmail());
    }
}