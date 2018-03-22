/**
 * A super class for all exceptions that occur in the quality control
 */

abstract public class HandlingException extends Exception {

    /**
     * The constructor to be called from subclasses
     */
    public HandlingException(String message) {
        super(message);
    }

    /**
     * Create a new exception with given message, and optionally a nested
     * exception
     */
    public HandlingException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public static void clash (){
    	System.out.println("-----------  a tagged bike is waiting at position 3. A Clash happens -----------");
    	Thread.currentThread().interrupt();
    }
    
}

