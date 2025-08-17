package routine.log.exception;

public class RangeException extends RuntimeException{

    public RangeException(String message, Throwable cause) {
        super(message, cause);
    }

    public RangeException(String message) {
        super(message);
    }
}
