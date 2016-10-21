/**
 * @author  $Author$
 * @date    $Date$
 * @version $Revision$
 */

package net.east301.keyring.memory;

import java.util.HashMap;
import java.util.Map;
import net.east301.keyring.KeyringBackend;
import net.east301.keyring.PasswordRetrievalException;
import net.east301.keyring.PasswordSaveException;
import net.east301.keyring.util.LockException;

/**
 * On-memory key store
 */
public class UncryptedMemoryBackend extends KeyringBackend {

    /**
     * Initializes an instance of UncryptedMemoryBackend
     */
    public UncryptedMemoryBackend() {
        m_store = new HashMap<String[], String>();
    }

    /**
     * Returns true when the backend is supported
     */
    @Override
    public boolean isSupported() {
        return true;
    }

    /**
     * Returns true if the backend directory uses some file to store passwords
     */
    @Override
    public boolean isKeyStorePathRequired() {
        return false;
    }

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
    @Override
    public String getPassword(String service, String account)
            throws LockException, PasswordRetrievalException {

        synchronized (m_store) {
            //
            for (Map.Entry<String[], String> entries : m_store.entrySet()) {
                String[] serviceAndAccount = entries.getKey();

                if (serviceAndAccount[0].equals(service) && serviceAndAccount[1].equals(account)) {
                    return entries.getValue();
                }
            }

            //
            throw new PasswordRetrievalException(
                    "Password related to the specified service and account is not found");
        } // synchronized
    }

    /**
     * Sets password to key store
     *
     * @param service   Service name
     * @param account   Account name
     * @param password  Password
     *
     * @throws PasswordSaveException    Thrown when an error happened while saving the password
     */
    @Override
    public void setPassword(String service, String account, String password)
            throws LockException, PasswordSaveException {

        synchronized (m_store) {
            //
            String[] targetKey = null;

            for (Map.Entry<String[], String> entries : m_store.entrySet()) {
                String[] serviceAndAccount = entries.getKey();

                if (serviceAndAccount[0].equals(service) && serviceAndAccount[1].equals(account)) {
                    targetKey = serviceAndAccount;
                    break;
                }
            }

            //
            if (targetKey == null) { targetKey = new String[] { service, account }; }
            m_store.put(targetKey, password);
        } // synchronized
    }

    /**
     * Gets backend ID
     */
    @Override
    public String getID() {
        return "UncryptedMemory";
    }

    /**
     * Password container
     */
    private HashMap<String[], String> m_store;  // { {ServiceName, AccountName} => Password }

} // class UncryptedMemoryBackend
