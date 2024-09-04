package net.clydo.msa.responses.error;

/**
 * Represents an error with a message.
 *
 * <p>This interface defines a contract for classes that represent errors with a method to retrieve a descriptive
 * error message. Implementations of this interface should provide the actual error message that describes the error.</p>
 */
public interface IError {

    /**
     * Returns a descriptive error message.
     *
     * <p>Implementations of this interface should return a string that provides details about the error, which
     * can be used for logging, displaying to users, or other error handling purposes.</p>
     *
     * @return a string containing the error message.
     */
    String message();
}