package edu.ccrm.cli;

import edu.ccrm.config.AppConfig;
import edu.ccrm.domain.*;
import edu.ccrm.service.StudentService;
import edu.ccrm.service.CourseService;
import edu.ccrm.io.ImportExportService;
import edu.ccrm.io.BackupService;
import edu.ccrm.exceptions.DuplicateEnrollmentException;
import edu.ccrm.exceptions.MaxCreditLimitExceededException;

import java.nio.file.Path;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== CCRM File I/O with NIO.2 Test ===");
        
        AppConfig config = AppConfig.getInstance();
      
        StudentService studentService = new StudentService();
        CourseService courseService = new CourseService();
        ImportExportService importExportService = new ImportExportService(studentService, courseService);
        BackupService backupService = new BackupService(importExportService);
        
        try {
         
            System.out.println("\n=== Testing CSV Import ===");
            Path studentsFile = config.getStudentDataFile();
            Path coursesFile = config.getCourseDataFile();
            
            int studentsImported = importExportService.importStudentsFromCSV(studentsFile);
            int coursesImported = importExportService.importCoursesFromCSV(coursesFile);
            
            System.out.println("Imported: " + studentsImported + " students, " + coursesImported + " courses");
           
            System.out.println("\n=== Testing Enrollment with Imported Data ===");
            Student Shyam = studentService.findStudentById("1").orElseThrow();
            Course CSE1001 = courseService.getCourseByCode("CSE1001").orElseThrow();
            Course MAT2001 = courseService.getCourseByCode("MAT2001").orElseThrow();
            
            try {
                studentService.enrollStudentInCourse(Shyam, CSE1001);
                studentService.enrollStudentInCourse(Shyam, MAT2001);
                
                Shyam.getEnrollments().get(0).recordMarks(85.0);
                Shyam.getEnrollments().get(1).recordMarks(92.0);
                
            } catch (DuplicateEnrollmentException | MaxCreditLimitExceededException e) {
                System.err.println("Enrollment error: " + e.getMessage());
            }
            
            System.out.println("\n=== Testing CSV Export ===");
            Path exportDir = config.getDataDirectory().resolve("export");
            java.nio.file.Files.createDirectories(exportDir);
            
            importExportService.exportStudentsToCSV(exportDir.resolve("students_export.csv"));
            importExportService.exportCoursesToCSV(exportDir.resolve("courses_export.csv"));
            importExportService.generateStudentReport(exportDir.resolve("report.txt"));
            
            System.out.println("\n=== Testing Backup Service ===");
            Path backupDir = backupService.createBackup();
            
            System.out.println("\n=== Testing Recursive Directory Size ===");
            long backupSize = backupService.calculateDirectorySize(backupDir);
            System.out.println("Backup size: " + backupSize + " bytes");
            
            System.out.println("\n=== Testing Recursive File Listing (Depth 2) ===");
            backupService.listFilesByDepth(backupDir, 0, 2);
            
            System.out.println("\n=== Testing Backup Analysis ===");
            backupService.analyzeBackups();
            
        } catch (IOException e) {
            System.err.println("File I/O error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\nFile I/O with NIO.2 working correctly!");
    }
}