/**
 * @author  $Author$
 * @date    $Date$
 * @version $Revision$
 */

package net.east301.keyring;

import net.east301.keyring.util.LockException;

/**
 * java-keyring backend interface
 */
public abstract class KeyringBackend {

    /**
     * Setup actual key store
     */
    public void setup() throws BackendNotSupportedException {
        // to be overrode
    }

    /**
     * Gets path to key store
     */
    public String getKeyStorePath() {
        return m_keyStorePath;
    }

    /**
     * Sets path to key store
     *
     * @param path  Path to key store
     */
    public void setKeyStorePath(String path) {
        m_keyStorePath = path;
    }

    /**
     * Returns true when the backend is supported
     */
    public abstract boolean isSupported();

    /**
     * Returns true if the backend directory uses some file to store passwords
     */
    public abstract boolean isKeyStorePathRequired();

    /**
     * Gets password from key store
     *
     * @param service   Service name
     * @param account   Account name
     *
     * @return  Password related to specified service and account
     *
     * @throws PasswordRetrievalException   Thrown when an error happened while getting password
     */
    public abstract String getPassword(String service, String account)
            throws LockException, PasswordRetrievalException;

    /**
     * Sets password to key store
     *
     * @param service   Service name
     * @param account   Account name
     * @param password  Password
     *
     * @throws PasswordSaveException    Thrown when an error happened while saving the password
     */
    public abstract void setPassword(String service, String account, String password)
            throws LockException, PasswordSaveException;

    /**
     * Gets backend ID
     */
    public abstract String getID();

    /**
     * Path to key store
     */
    protected String m_keyStorePath;

} // class KeyringBackend
