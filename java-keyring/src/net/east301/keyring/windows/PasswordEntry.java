/**
 * @author  $Author$
 * @date    $Date$
 * @version $Revision$
 */

package net.east301.keyring.windows;

import java.io.Serializable;

/**
 * Password Entry
 */
class PasswordEntry implements Serializable {

    /**
     * Initializes an instance of PasswordEntry
     *
     * @param service   Service name
     * @param account   Account name
     * @param password  Password
     */
    public PasswordEntry(String service, String account, byte[] password) {
        m_service = service;
        m_account = account;
        m_password = password;
    }

    /**
     * Returns service name
     */
    public String getService() {
        return m_service;
    }

    /**
     * Sets service name
     */
    public void setService(String service) {
        m_service = service;
    }

    /**
     * Returns account name
     */
    public String getAccount() {
        return m_account;
    }

    /**
     * Sets account name
     */
    public void setAccount(String account) {
        m_account = account;
    }

    /**
     * Returns password
     */
    public byte[] getPassword() {
        return m_password;
    }

    /**
     * Sets password
     */
    public void setPassword(byte[] password) {
        m_password = password;
    }

    /**
     * Service name
     */
    private String m_service;

    /**
     * Account name
     */
    private String m_account;

    /**
     * Password
     */
    private byte[] m_password;

} // class PasswordEntry
