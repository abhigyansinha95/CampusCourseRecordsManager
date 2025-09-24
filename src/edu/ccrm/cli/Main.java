package edu.ccrm.cli;

import edu.ccrm.domain.Semester;
import edu.ccrm.domain.Grade;
import edu.ccrm.domain.Student;
import edu.ccrm.domain.Course;
import edu.ccrm.domain.Enrollment;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== CCRM Course Management Test ===");
        
        // Create courses using Builder pattern
        Course cs101 = new Course.Builder("CS101", "Introduction to Programming")
            .credits(3)
            .instructor("Dr. Tom")
            .semester(Semester.INTERIM)
            .department("Computer Science")
            .build();
            
        Course math201 = new Course.Builder("MATH201", "Calculus I")
            .credits(4)
            .instructor("Dr. Ram")
            .semester(Semester.INTERIM)
            .department("Mathematics")
            .build();
        
        // Create student
        Student student1 = new Student("2456", "24BCE1234", "Tommy Innit", "tommy.doe@uni.edu");
        
        // Test enrollment
        Enrollment enrollment1 = student1.enrollInCourse(cs101);
        Enrollment enrollment2 = student1.enrollInCourse(math201);
        
        // Record grades
        enrollment1.recordMarks(85.5);
        enrollment2.recordMarks(92.0);
        
        // Display results
        System.out.println("\n=== Student Summary ===");
        student1.displayProfile();
        student1.displayEnrolledCourses();
        
        System.out.println("\n=== Grade Report ===");
        System.out.println("CS101 - Marks: " + enrollment1.getMarks() + ", Grade: " + enrollment1.getGrade());
        System.out.println("MATH201 - Marks: " + enrollment2.getMarks() + ", Grade: " + enrollment2.getGrade());
        System.out.println("Total Credits: " + student1.getTotalCredits());
        
        System.out.println("\nCourse management working correctly!");
    }
}