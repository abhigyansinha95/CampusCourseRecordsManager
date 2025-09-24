package edu.ccrm.io;

import edu.ccrm.config.AppConfig;

import java.nio.file.*;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.stream.Stream;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Demonstrates recursive file operations and NIO.2 backup functionality
 */
public class BackupService {
    private final AppConfig config;
    private final ImportExportService importExportService;
    
    public BackupService(ImportExportService importExportService) {
        this.config = AppConfig.getInstance();
        this.importExportService = importExportService;
    }
    
    // Create timestamped backup using NIO.2
    public Path createBackup() throws IOException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        Path backupDir = config.getBackupDirectory().resolve("backup_" + timestamp);
        
        // Create backup directory
        Files.createDirectories(backupDir);
        
        // Export current data to backup directory
        Path studentsBackup = backupDir.resolve("students_backup.csv");
        Path coursesBackup = backupDir.resolve("courses_backup.csv");
        Path reportBackup = backupDir.resolve("student_report.txt");
        
        importExportService.exportStudentsToCSV(studentsBackup);
        importExportService.exportCoursesToCSV(coursesBackup);
        importExportService.generateStudentReport(reportBackup);
        
        System.out.println("Backup created: " + backupDir.getFileName());
        return backupDir;
    }
    
    // Recursive method to calculate directory size - DEMONSTRATES RECURSION
    public long calculateDirectorySize(Path directory) throws IOException {
        // Base case: if it's a file, return its size
        if (Files.isRegularFile(directory)) {
            return Files.size(directory);
        }
        
        // Recursive case: if it's a directory, sum sizes of all contents
        if (Files.isDirectory(directory)) {
            try (Stream<Path> paths = Files.list(directory)) {
                return paths.mapToLong(path -> {
                    try {
                        return calculateDirectorySize(path); // RECURSIVE CALL
                    } catch (IOException e) {
                        System.err.println("Error accessing: " + path);
                        return 0L;
                    }
                }).sum();
            }
        }
        
        return 0L;
    }
    
    // Recursive method to list files by depth - DEMONSTRATES RECURSION
    public void listFilesByDepth(Path directory, int currentDepth, int maxDepth) throws IOException {
        // Base case: stop if max depth reached
        if (currentDepth > maxDepth) {
            return;
        }
        
        String indent = "  ".repeat(currentDepth);
        
        if (Files.isDirectory(directory)) {
            System.out.println(indent + "📁 " + directory.getFileName() + "/");
            
            // Recursive case: list contents
            try (Stream<Path> paths = Files.list(directory)) {
                paths.forEach(path -> {
                    try {
                        if (Files.isDirectory(path)) {
                            listFilesByDepth(path, currentDepth + 1, maxDepth); // RECURSIVE CALL
                        } else {
                            System.out.println(indent + "  📄 " + path.getFileName() + 
                                             " (" + Files.size(path) + " bytes)");
                        }
                    } catch (IOException e) {
                        System.err.println("Error accessing: " + path);
                    }
                });
            }
        }
    }
    
    // Demonstrate Files.walk for backup analysis
    public void analyzeBackups() throws IOException {
        System.out.println("=== BACKUP ANALYSIS ===");
        
        if (!Files.exists(config.getBackupDirectory())) {
            System.out.println("No backups found.");
            return;
        }
        
        // Using Files.walk with Stream API
        try (Stream<Path> backupPaths = Files.walk(config.getBackupDirectory())) {
            backupPaths
                .filter(Files::isDirectory)
                .filter(path -> path.getFileName().toString().startsWith("backup_"))
                .forEach(backupDir -> {
                    try {
                        long size = calculateDirectorySize(backupDir);
                        long fileCount = Files.list(backupDir).count();
                        
                        System.out.println("Backup: " + backupDir.getFileName());
                        System.out.println("  Size: " + size + " bytes");
                        System.out.println("  Files: " + fileCount);
                        
                    } catch (IOException e) {
                        System.err.println("Error analyzing backup: " + backupDir);
                    }
                });
        }
    }
    
    // Utility method to format file size
    private String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        else if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        else return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
    }
}