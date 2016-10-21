/**
 * @author  $Author$
 * @date    $Date$
 * @version $Revision$
 */

package net.east301.keyring;

/**
 * Represents an error while retrieving password
 */
public class PasswordSaveException extends Exception {

    /**
     * Initializes an instance of PasswordSaveException
     *
     * @param message   Error message
     */
    public PasswordSaveException(String message) {
        super(message);
    }

} // class PasswordSaveException
