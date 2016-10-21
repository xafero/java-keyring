/**
 * @author  $Author$
 * @date    $Date$
 * @version $Revision$
 */

package net.east301.keyring.util;

/**
 * Represents an error while lock operation
 */
public class LockException extends Exception {

    /**
     * Initializes an instance of LockException
     *
     * @param message   Error message
     */
    public LockException(String message, Throwable innerException) {
        super(message, innerException);
    }

} // class LockException
