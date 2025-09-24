package edu.ccrm.io;

import edu.ccrm.domain.Student;
import edu.ccrm.domain.Course;
import edu.ccrm.domain.Semester;
import edu.ccrm.service.StudentService;
import edu.ccrm.service.CourseService;
import edu.ccrm.config.AppConfig;

import java.nio.file.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImportExportService {
    private final StudentService studentService;
    private final CourseService courseService;
    private final AppConfig config;
    
    public ImportExportService(StudentService studentService, CourseService courseService) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.config = AppConfig.getInstance();
    }
    
    // Import students from CSV using NIO.2 and Stream API
    public int importStudentsFromCSV(Path filePath) throws IOException {
        if (!Files.exists(filePath)) {
            throw new IOException("File not found: " + filePath);
        }
        
        List<Student> importedStudents = Files.lines(filePath) // Stream of lines
            .skip(1) // Skip header
            .filter(line -> !line.trim().isEmpty()) // Filter empty lines
            .map(this::parseStudentFromCSV) // Map to Student objects
            .filter(Objects::nonNull) // Filter out nulls
            .collect(Collectors.toList());
        
        // Add to service
        importedStudents.forEach(studentService::addStudent);
        
        System.out.println("Imported " + importedStudents.size() + " students from " + filePath.getFileName());
        return importedStudents.size();
    }
    
    private Student parseStudentFromCSV(String csvLine) {
        try {
            String[] fields = csvLine.split(",");
            if (fields.length >= 4) {
                String id = fields[0].trim();
                String regNo = fields[1].trim();
                String fullName = fields[2].trim();
                String email = fields[3].trim();
                
                return new Student(id, regNo, fullName, email);
            }
        } catch (Exception e) {
            System.err.println("Error parsing student from CSV: " + csvLine);
        }
        return null;
    }
    
    // Import courses from CSV using NIO.2
    public int importCoursesFromCSV(Path filePath) throws IOException {
        if (!Files.exists(filePath)) {
            throw new IOException("File not found: " + filePath);
        }
        
        List<Course> importedCourses = Files.lines(filePath)
            .skip(1)
            .filter(line -> !line.trim().isEmpty())
            .map(this::parseCourseFromCSV)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        
        importedCourses.forEach(courseService::addCourse);
        
        System.out.println("Imported " + importedCourses.size() + " courses from " + filePath.getFileName());
        return importedCourses.size();
    }
    
    private Course parseCourseFromCSV(String csvLine) {
        try {
            String[] fields = csvLine.split(",");
            if (fields.length >= 6) {
                String code = fields[0].trim();
                String title = fields[1].trim();
                int credits = Integer.parseInt(fields[2].trim());
                String instructor = fields[3].trim();
                Semester semester = Semester.valueOf(fields[4].trim().toUpperCase());
                String department = fields[5].trim();
                
                return new Course.Builder(code, title)
                    .credits(credits)
                    .instructor(instructor)
                    .semester(semester)
                    .department(department)
                    .build();
            }
        } catch (Exception e) {
            System.err.println("Error parsing course from CSV: " + csvLine + " - " + e.getMessage());
        }
        return null;
    }
    
    // Export students to CSV using NIO.2
    public void exportStudentsToCSV(Path filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("id,regNo,fullName,email,status"); // Header
        
        // Using Stream API to transform students to CSV lines
        List<String> studentLines = studentService.getAllStudents().stream()
            .map(student -> String.format("%s,%s,%s,%s,%s",
                student.getId(),
                student.getRegNo(),
                student.getFullName(),
                student.getEmail(),
                student.isActive() ? "ACTIVE" : "INACTIVE"))
            .collect(Collectors.toList());
        
        lines.addAll(studentLines);
        
        // Write using NIO.2
        Files.write(filePath, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        System.out.println("Exported " + studentLines.size() + " students to " + filePath.getFileName());
    }
    
    // Export courses to CSV using NIO.2
    public void exportCoursesToCSV(Path filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("code,title,credits,instructor,semester,department,active");
        
        List<String> courseLines = courseService.getAllCourses().stream()
            .map(course -> String.format("%s,%s,%d,%s,%s,%s,%s",
                course.getCode(),
                course.getTitle(),
                course.getCredits(),
                course.getInstructor(),
                course.getSemester(),
                course.getDepartment(),
                course.isActive()))
            .collect(Collectors.toList());
        
        lines.addAll(courseLines);
        Files.write(filePath, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        System.out.println("Exported " + courseLines.size() + " courses to " + filePath.getFileName());
    }
    
    // Generate student report using Stream API
    public void generateStudentReport(Path filePath) throws IOException {
        List<String> reportLines = new ArrayList<>();
        reportLines.add("=== STUDENT REPORT ===");
        reportLines.add("Generated: " + LocalDateTime.now());
        reportLines.add("");
        
        // Using Stream API for complex data transformation
        String reportContent = studentService.getAllStudents().stream()
            .map(student -> {
                double gpa = studentService.calculateGPA(student);
                int credits = student.getTotalCredits();
                return String.format("Student: %s (%s)\nEmail: %s\nGPA: %.2f\nCredits: %d\nCourses: %d\n",
                    student.getFullName(), student.getRegNo(), student.getEmail(),
                    gpa, credits, student.getEnrollments().size());
            })
            .collect(Collectors.joining("\n"));
        
        reportLines.add(reportContent);
        Files.write(filePath, reportLines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        System.out.println("Generated student report: " + filePath.getFileName());
    }
}