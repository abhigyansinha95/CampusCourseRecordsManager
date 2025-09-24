package edu.ccrm.cli;

import edu.ccrm.config.AppConfig;
import edu.ccrm.domain.*;
import edu.ccrm.service.StudentService;
import edu.ccrm.service.CourseService;
import edu.ccrm.exceptions.DuplicateEnrollmentException;
import edu.ccrm.exceptions.MaxCreditLimitExceededException;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== CCRM Service Layer Test ===");
        
        // Demonstrate Singleton pattern
        AppConfig config = AppConfig.getInstance();
        System.out.println("Data directory: " + config.getDataDirectory());
        
        // Initialize services
        StudentService studentService = new StudentService();
        CourseService courseService = new CourseService();
        
        // Create courses
        Course cs101 = new Course.Builder("CS101", "Introduction to Programming")
            .credits(3).instructor("Dr. Smith").semester(Semester.INTERIM).department("Computer Science").build();
        
        Course math201 = new Course.Builder("MATH201", "Calculus I")
            .credits(4).instructor("Dr. Johnson").semester(Semester.INTERIM).department("Mathematics").build();
        
        Course phys101 = new Course.Builder("PHYS101", "Physics I")
            .credits(4).instructor("Dr. Brown").semester(Semester.INTERIM).department("Physics").build();
            
        courseService.addCourse(cs101);
        courseService.addCourse(math201);
        courseService.addCourse(phys101);
        
        // Create students
        Student student1 = new Student("S001", "2023001", "Tommy", "john.doe@uni.edu");
        Student student2 = new Student("S002", "2023002", "Jane Tame", "jane.smith@uni.edu");
        
        studentService.addStudent(student1);
        studentService.addStudent(student2);
        
        // Test enrollment with exception handling
        try {
            System.out.println("\n=== Testing Enrollment ===");
            studentService.enrollStudentInCourse(student1, cs101);
            studentService.enrollStudentInCourse(student1, math201);
            
            // This should throw MaxCreditLimitExceededException
            studentService.enrollStudentInCourse(student1, phys101);
            
        } catch (DuplicateEnrollmentException e) {
            System.err.println("Enrollment error: " + e.getMessage());
        } catch (MaxCreditLimitExceededException e) {
            System.err.println("Credit limit error: " + e.getMessage());
        }
        
        // Test Stream API search functionality
        System.out.println("\n=== Testing Search Functionality ===");
        System.out.println("Courses in Computer Science:");
        courseService.searchByDepartment("Computer Science").forEach(c -> 
            System.out.println("- " + c.getCode() + ": " + c.getTitle()));
        
        System.out.println("\nSearch courses with 'Introduction':");
        courseService.search("Introduction").forEach(c -> 
            System.out.println("- " + c.getCode() + ": " + c.getTitle()));
        
        // Test GPA calculation
        System.out.println("\n=== Testing GPA Calculation ===");
        System.out.println(student1.getFullName() + " GPA: " + studentService.calculateGPA(student1));
        
        System.out.println("\nService layer working correctly!");
    }
}