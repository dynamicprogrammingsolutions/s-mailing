/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
