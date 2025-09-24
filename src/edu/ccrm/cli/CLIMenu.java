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
import java.util.Scanner;
import java.util.Optional;
import java.io.IOException;

public class CLIMenu {
    private final Scanner scanner;
    private final StudentService studentService;
    private final CourseService courseService;
    private final ImportExportService importExportService;
    private final BackupService backupService;
    private boolean running;
    
    public CLIMenu() {
        this.scanner = new Scanner(System.in);
        this.studentService = new StudentService();
        this.courseService = new CourseService();
        this.importExportService = new ImportExportService(studentService, courseService);
        this.backupService = new BackupService(importExportService);
        this.running = true;
        
        loadInitialData();
    }
    
    private void loadInitialData() {
        try {
            AppConfig config = AppConfig.getInstance();
            Path studentsFile = config.getStudentDataFile();
            Path coursesFile = config.getCourseDataFile();
            
            if (java.nio.file.Files.exists(studentsFile)) {
                importExportService.importStudentsFromCSV(studentsFile);
            }
            if (java.nio.file.Files.exists(coursesFile)) {
                importExportService.importCoursesFromCSV(coursesFile);
            }
        } catch (IOException e) {
            System.out.println("Note: Could not load initial data: " + e.getMessage());
        }
    }
    
    public void start() {
        System.out.println("=== Campus Course Records Manager (CCRM) ===");
        System.out.println("Java SE Console Application\n");
        
        
        mainLoop: while (running) {
            displayMainMenu();
            String choice = scanner.nextLine().trim();
            
            
            switch (choice) {
                case "1" -> manageStudents(); 
                case "2" -> manageCourses();
                case "3" -> manageEnrollments();
                case "4" -> manageGrades();
                case "5" -> importExportData();
                case "6" -> backupOperations();
                case "7" -> generateReports();
                case "8" -> {
                    System.out.println("Exiting CCRM.");
                    break mainLoop; 
                }
                case "debug" -> { 
                    debugMenu();
                    continue mainLoop; 
                }
                default -> {
                    System.out.println("Invalid choice. Please try again.");
                    continue mainLoop; 
                }
            }
        }
        scanner.close();
    }
    
    private void displayMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Manage Students");
        System.out.println("2. Manage Courses");
        System.out.println("3. Manage Enrollments");
        System.out.println("4. Manage Grades");
        System.out.println("5. Import/Export Data");
        System.out.println("6. Backup Operations");
        System.out.println("7. Generate Reports");
        System.out.println("8. Exit");
        System.out.print("Enter your choice (1-8): ");
    }
    
    private void manageStudents() {
        studentLoop: while (true) {
            System.out.println("\n=== STUDENT MANAGEMENT ===");
            System.out.println("1. List All Students");
            System.out.println("2. Add New Student");
            System.out.println("3. Find Student by ID");
            System.out.println("4. View Student Profile");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter choice: ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    listAllStudents();
                    break;
                case "2":
                    addNewStudent();
                    break;
                case "3":
                    findStudentById();
                    break;
                case "4":
                    viewStudentProfile();
                    break;
                case "5":
                    break studentLoop; 
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
    
    private void listAllStudents() {
        System.out.println("\n=== ALL STUDENTS ===");
        if (studentService.getAllStudents().isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        
        studentService.getAllStudents().forEach(student -> {
            double gpa = studentService.calculateGPA(student);
            System.out.printf("%s - %s (GPA: %.2f, Credits: %d)%n",
                student.getRegNo(), student.getFullName(), gpa, student.getTotalCredits());
        });
    }
    
    private void addNewStudent() {
        System.out.println("\n=== ADD NEW STUDENT ===");
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Registration No: ");
        String regNo = scanner.nextLine();
        System.out.print("Enter Full Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        
        Student student = new Student(id, regNo, name, email);
        studentService.addStudent(student);
        System.out.println("Student added successfully!");
    }
    
    private void findStudentById() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine();
        
        Optional<Student> student = studentService.findStudentById(id);
        if (student.isPresent()) {
            student.get().displayProfile();
        } else {
            System.out.println("Student not found.");
        }
    }
    
    private void viewStudentProfile() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine();
        
        studentService.findStudentById(id).ifPresentOrElse(
            student -> {
                student.displayProfile();
                System.out.println("\nEnrolled Courses:");
                if (student.getEnrollments().isEmpty()) {
                    System.out.println("No courses enrolled.");
                } else {
                    for (Enrollment enrollment : student.getEnrollments()) { // Enhanced for
                        Course course = enrollment.getCourse();
                        String gradeInfo = enrollment.isGraded() ? 
                            "Grade: " + enrollment.getGrade() + " (Marks: " + enrollment.getMarks() + ")" : 
                            "Not graded";
                        System.out.printf("- %s: %s - %s%n", 
                            course.getCode(), course.getTitle(), gradeInfo);
                    }
                }
            },
            () -> System.out.println("Student not found.")
        );
    }
    
    private void manageCourses() {
        while (true) {
            System.out.println("\n=== COURSE MANAGEMENT ===");
            System.out.println("1. List All Courses");
            System.out.println("2. Search Courses");
            System.out.println("3. Add New Course");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter choice: ");
            
            switch (scanner.nextLine()) {
                case "1" -> listAllCourses();
                case "2" -> searchCourses();
                case "3" -> addNewCourse();
                case "4" -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }
    
    private void listAllCourses() {
        System.out.println("\n=== ALL COURSES ===");
        courseService.getAllCourses().forEach(course -> 
            System.out.printf("%s: %s (%d credits) - %s%n",
                course.getCode(), course.getTitle(), course.getCredits(), course.getInstructor()));
    }
    
    private void searchCourses() {
        System.out.println("\n=== SEARCH COURSES ===");
        System.out.println("1. Search by Keyword");
        System.out.println("2. Search by Instructor");
        System.out.println("3. Search by Department");
        System.out.print("Enter choice: ");
        
        String choice = scanner.nextLine();
        System.out.print("Enter search term: ");
        String term = scanner.nextLine();
        
        Runnable searchAction = () -> {
            var results = switch (choice) {
                case "1" -> courseService.search(term);
                case "2" -> courseService.searchByInstructor(term);
                case "3" -> courseService.searchByDepartment(term);
                default -> { 
                    System.out.println("Invalid choice!");
                    yield java.util.Collections.<Course>emptyList();
                }
            };
            
            if (results.isEmpty()) {
                System.out.println("No courses found.");
            } else {
                results.forEach(course -> System.out.println("- " + course));
            }
        };
        
        searchAction.run(); 
    }
    
    private void addNewCourse() {
        System.out.println("\n=== ADD NEW COURSE ===");
        System.out.print("Course Code: ");
        String code = scanner.nextLine();
        System.out.print("Course Title: ");
        String title = scanner.nextLine();
        System.out.print("Credits: ");
        int credits = Integer.parseInt(scanner.nextLine());
        System.out.print("Instructor: ");
        String instructor = scanner.nextLine();
        System.out.print("Department: ");
        String department = scanner.nextLine();
        
        Course course = new Course.Builder(code, title)
            .credits(credits)
            .instructor(instructor)
            .semester(Semester.INTERIM) 
            .department(department)
            .build();
            
        courseService.addCourse(course);
        System.out.println("Course added successfully!");
    }
    
    private void manageEnrollments() {
        System.out.println("\n=== ENROLLMENT MANAGEMENT ===");
        System.out.print("Student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Course Code: ");
        String courseCode = scanner.nextLine();
        
        Optional<Student> student = studentService.findStudentById(studentId);
        Optional<Course> course = courseService.getCourseByCode(courseCode);
        
        if (student.isEmpty() || course.isEmpty()) {
            System.out.println("Student or course not found.");
            return;
        }
        
        try {
            studentService.enrollStudentInCourse(student.get(), course.get());
            System.out.println("Enrollment successful!");
        } catch (DuplicateEnrollmentException e) {
            System.out.println("Enrollment failed: " + e.getMessage());
        } catch (MaxCreditLimitExceededException e) {
            System.out.println("Enrollment failed: " + e.getMessage());
        }
    }
    
    private void manageGrades() {
        System.out.println("\n=== GRADE MANAGEMENT ===");
        System.out.print("Student ID: ");
        String studentId = scanner.nextLine();
        
        studentService.findStudentById(studentId).ifPresentOrElse(
            student -> {
                System.out.println("Courses enrolled by " + student.getFullName() + ":");
                int i = 0;
                if (!student.getEnrollments().isEmpty()) {
                    do {
                        Enrollment enrollment = student.getEnrollments().get(i);
                        Course course = enrollment.getCourse();
                        System.out.printf("%d. %s: %s%n", i + 1, course.getCode(), course.getTitle());
                        i++;
                    } while (i < student.getEnrollments().size()); // Do-while
                }
                
                System.out.print("Select course (number): ");
                int courseIndex = Integer.parseInt(scanner.nextLine()) - 1;
                
                if (courseIndex >= 0 && courseIndex < student.getEnrollments().size()) {
                    Enrollment enrollment = student.getEnrollments().get(courseIndex);
                    System.out.print("Enter marks: ");
                    double marks = Double.parseDouble(scanner.nextLine());
                    enrollment.recordMarks(marks);
                    System.out.println("Grade recorded: " + enrollment.getGrade());
                } else {
                    System.out.println("Invalid selection.");
                }
            },
            () -> System.out.println("Student not found.")
        );
    }
    
    private void importExportData() {
        System.out.println("\n=== IMPORT/EXPORT DATA ===");
        System.out.println("1. Export Students to CSV");
        System.out.println("2. Export Courses to CSV");
        System.out.println("3. Generate Student Report");
        System.out.println("4. Back to Main Menu");
        System.out.print("Enter choice: ");
        
        try {
            switch (scanner.nextLine()) {
                case "1" -> {
                    Path file = AppConfig.getInstance().getDataDirectory().resolve("students_export.csv");
                    importExportService.exportStudentsToCSV(file);
                }
                case "2" -> {
                    Path file = AppConfig.getInstance().getDataDirectory().resolve("courses_export.csv");
                    importExportService.exportCoursesToCSV(file);
                }
                case "3" -> {
                    Path file = AppConfig.getInstance().getDataDirectory().resolve("student_report.txt");
                    importExportService.generateStudentReport(file);
                }
                case "4" -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void backupOperations() {
        System.out.println("\n=== BACKUP OPERATIONS ===");
        System.out.println("1. Create Backup");
        System.out.println("2. Show Backup Size (Recursive)");
        System.out.println("3. List Backup Files (Recursive)");
        System.out.println("4. Analyze Backups");
        System.out.println("5. Back to Main Menu");
        System.out.print("Enter choice: ");
        
        try {
            switch (scanner.nextLine()) {
                case "1" -> {
                    Path backupDir = backupService.createBackup();
                    System.out.println("Backup created: " + backupDir.getFileName());
                }
                case "2" -> {
                    Path backupDir = AppConfig.getInstance().getBackupDirectory();
                    long size = backupService.calculateDirectorySize(backupDir);
                    System.out.println("Total backup size: " + size + " bytes");
                }
                case "3" -> {
                    Path backupDir = AppConfig.getInstance().getBackupDirectory();
                    backupService.listFilesByDepth(backupDir, 0, 3);
                }
                case "4" -> backupService.analyzeBackups();
                case "5" -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void generateReports() {
        System.out.println("\n=== REPORTS ===");
        System.out.println("1. Top Students by GPA");
        System.out.println("2. GPA Distribution");
        System.out.println("3. Department-wise Summary");
        System.out.println("4. Back to Main Menu");
        System.out.print("Enter choice: ");
        
        switch (scanner.nextLine()) {
            case "1" -> {
                System.out.println("\n=== TOP 3 STUDENTS BY GPA ===");
                studentService.getTopStudents(3).forEach(student -> {
                    double gpa = studentService.calculateGPA(student);
                    System.out.printf("%s: %.2f GPA (%d credits)%n", 
                        student.getFullName(), gpa, student.getTotalCredits());
                });
            }
            case "2" -> {
                System.out.println("\n=== GPA DISTRIBUTION ===");
                double averageGPA = studentService.getAllStudents().stream()
                    .mapToDouble(studentService::calculateGPA)
                    .average()
                    .orElse(0.0);
                System.out.printf("Average GPA: %.2f%n", averageGPA);
            }
            case "3" -> {
                System.out.println("\n=== DEPARTMENT SUMMARY ===");
                courseService.getAllCourses().stream()
                    .collect(java.util.stream.Collectors.groupingBy(
                        Course::getDepartment,
                        java.util.stream.Collectors.counting()
                    ))
                    .forEach((dept, count) -> System.out.printf("%s: %d courses%n", dept, count));
            }
            case "4" -> { return; }
            default -> System.out.println("Invalid choice!");
        }
    }
    
    private void debugMenu() {
        System.out.println("\n=== DEBUG MENU ===");
        
        Runnable debugAction = new Runnable() {
            @Override
            public void run() {
                System.out.println("Students: " + studentService.getAllStudents().size());
                System.out.println("Courses: " + courseService.getAllCourses().size());
                System.out.println("Data directory: " + AppConfig.getInstance().getDataDirectory());
            }
        };
        
        debugAction.run();
    }
}