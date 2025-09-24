package edu.ccrm.exceptions;

public class MaxCreditLimitExceededException extends Exception {
    private final int currentCredits;
    private final int attemptedCredits;
    private final int maxAllowed;
    
    public MaxCreditLimitExceededException(int current, int attempted, int max) {
        super(String.format("Credit limit exceeded! Current: %d, Attempted: %d, Max: %d", 
                          current, attempted, max));
        this.currentCredits = current;
        this.attemptedCredits = attempted;
        this.maxAllowed = max;
    }
    
    public int getCurrentCredits() { return currentCredits; }
    public int getAttemptedCredits() { return attemptedCredits; }
    public int getMaxAllowed() { return maxAllowed; }
}