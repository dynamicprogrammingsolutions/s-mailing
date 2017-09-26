package dps.reflect;

/**
 *
 * @author ferenci84
 */
public class NoSuchConstcutorError extends RuntimeException {

    /**
     * Creates a new instance of <code>NoSuchConstcutorException</code> without
     * detail message.
     */
    public NoSuchConstcutorError() {
    }

    /**
     * Constructs an instance of <code>NoSuchConstcutorException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoSuchConstcutorError(String msg) {
        super(msg);
    }
}
