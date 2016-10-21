/**
 * @author  $Author$
 * @date    $Date$
 * @version $Revision$
 */

package net.east301.keyring;

import net.east301.keyring.util.LockException;

/**
 * Keyring
 */
public class Keyring {

    /**
     * Creates an instance of Keyring
     */
    public static Keyring create() throws BackendNotSupportedException {
        return new Keyring(KeyringBackendFactory.create());
    }

    /**
     * Creates an instance of Keyring with specified backend
     *
     * @param backendType   Backend type
     */
    public static Keyring create(String backendType) throws BackendNotSupportedException {
        return new Keyring(KeyringBackendFactory.create(backendType));
    }

    /**
     * Initializes an instance of Keyring
     *
     * @param backend   Keyring backend instance
     */
    private Keyring(KeyringBackend backend) {
        m_backend = backend;
    }

    /**
     * Returns keyring backend instance
     */
    public KeyringBackend getBackend() {
        return m_backend;
    }

    /**
     * Gets path to key store
     * (Proxy method of KeyringBackend.getKeyStorePath)
     */
    public String getKeyStorePath() {
        return m_backend.getKeyStorePath();
    }

    /**
     * Sets path to key store
     * (Proxy method of KeyringBackend.setKeyStorePath)
     *
     * @param path  Path to key store
     */
    public void setKeyStorePath(String path) {
        m_backend.setKeyStorePath(path);
    }

    /**
     * Returns true if the backend directory uses some file to store passwords
     * (Proxy method of KeyringBackend.isKeyStorePathRequired)
     */
    public boolean isKeyStorePathRequired() {
        return m_backend.isKeyStorePathRequired();
    }

    /**
     * Gets password from key store
     * (Proxy method of KeyringBackend.getPassword)
     *
     * @param service   Service name
     * @param account   Account name
     *
     * @return  Password related to specified service and account
     *
     * @throws PasswordRetrievalException   Thrown when an error happened while getting password
     */
    public String getPassword(String service, String account)
            throws LockException, PasswordRetrievalException {

        return m_backend.getPassword(service, account);
    }

    /**
     * Sets password to key store
     * (Proxy method of KeyringBackend.setPassword)
     *
     * @param service   Service name
     * @param account   Account name
     * @param password  Password
     *
     * @throws PasswordSaveException    Thrown when an error happened while saving the password
     */
    public void setPassword(String service, String account, String password)
            throws LockException, PasswordSaveException {

        m_backend.setPassword(service, account, password);
    }

    /**
     * Keyring backend
     */
    private KeyringBackend m_backend;

} // class Keyring
