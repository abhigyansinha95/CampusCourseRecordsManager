package edu.ccrm.cli;

public class Main {
    public static void main(String[] args) {
        try {
            displayPlatformInfo();
            
            CLIMenu menu = new CLIMenu();
            menu.start();
            
        } catch (Exception e) {
            System.err.println("Application error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void displayPlatformInfo() {
        System.out.println("=== Java Platform Information ===");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("Java Vendor: " + System.getProperty("java.vendor"));
        System.out.println("Operating System: " + System.getProperty("os.name"));
        System.out.println("\nJava SE vs ME vs EE:");
        System.out.println("- Java SE (Standard Edition): Desktop applications");
        System.out.println("- Java ME (Micro Edition): Mobile/embedded systems");
        System.out.println("- Java EE (Enterprise Edition): Server-side applications");
        System.out.println("This application uses: Java SE");
        System.out.println("=================================\n");
    }
}