package edu.ccrm.cli;

import edu.ccrm.domain.Semester;
import edu.ccrm.domain.Grade;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== CCRM Starting ===");
        
        // Test Semester
        System.out.println("Semesters: ");
        for (Semester s : Semester.values()) {
            System.out.println("- " + s);
        }
        
        // Test Grade
        System.out.println("Grade A: " + Grade.A + " (" + Grade.A.getPoints() + " points)");
        System.out.println("Score 85 gets: " + Grade.fromScore(85));
        
        System.out.println("Enums working correctly!");
    }
}