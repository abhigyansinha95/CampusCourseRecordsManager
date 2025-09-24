package edu.ccrm.cli;

import edu.ccrm.domain.Semester;
import edu.ccrm.domain.Grade;
import edu.ccrm.domain.Student;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== CCRM OOP Inheritance Test ===");
        
        System.out.println("Semester: " + Semester.INTERIM);
        System.out.println("Grade for 85: " + Grade.fromScore(85));
        
        System.out.println("\n=== Testing Student Inheritance ===");
        Student student1 = new Student("S001", "2023001", "John Doe", "john.doe@uni.edu");
        
        student1.displayProfile(); 
       
        student1.enrollInCourse("CS101");
        student1.enrollInCourse("MATH201");
        student1.displayEnrolledCourses();
        
        System.out.println("\nStudent toString(): " + student1);
        
        System.out.println("\nOOP inheritance working correctly!");
    }
}