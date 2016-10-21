/**
 * @author  $Author$
 * @date    $Date$
 * @version $Revision$
 */

package net.east301.keyring;

/**
 * Represents an error while retrieving password
 */
public class PasswordRetrievalException extends Exception {

    /**
     * Initializes an instance of PasswordRetrievalException
     *
     * @param message   Error message
     */
    public PasswordRetrievalException(String message) {
        super(message);
    }

} // class PasswordRetrievalException
