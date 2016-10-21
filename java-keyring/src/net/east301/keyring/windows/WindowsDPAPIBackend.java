/**
 * @author  $Author$
 * @date    $Date$
 * @version $Revision$
 */

package net.east301.keyring.windows;

import com.sun.jna.Platform;
import com.sun.jna.platform.win32.Crypt32Util;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.east301.keyring.KeyringBackend;
import net.east301.keyring.PasswordRetrievalException;
import net.east301.keyring.PasswordSaveException;
import net.east301.keyring.util.FileBasedLock;
import net.east301.keyring.util.LockException;

/**
 * Keyring backend which uses Windows DPAPI
 */
public class WindowsDPAPIBackend extends KeyringBackend {

    /**
     * Returns true when the backend is supported
     */
    @Override
    public boolean isSupported() {
        return Platform.isWindows();
    }

    /**
     * Returns true if the backend directory uses some file to store passwords
     */
    @Override
    public boolean isKeyStorePathRequired() {
        return true;
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

        FileBasedLock fileLock = new FileBasedLock(getLockPath());

        try {
            //
            fileLock.lock();

            //
            PasswordEntry targetEntry = null;

            for (PasswordEntry entry : loadPasswordEntries()) {
                if (entry.getService().equals(service) && entry.getAccount().equals(account)) {
                    targetEntry = entry;
                    break;
                }
            }

            if (targetEntry == null) {
                throw new PasswordRetrievalException(
                        "Password related to the specified service and account is not found");
            }

            //
            byte[] decryptedBytes;

            try {
                decryptedBytes = Crypt32Util.cryptUnprotectData(targetEntry.getPassword());
            } catch (Exception ex) {
                throw new PasswordRetrievalException("Failed to decrypt password");
            }

            //
            try {
                return new String(decryptedBytes, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                throw new PasswordRetrievalException("Unsupported encoding 'UTF-8' specified");
            }

        } finally {
            try {
                fileLock.release();
            } catch (Exception ex) {
                Logger.getLogger(WindowsDPAPIBackend.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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

        FileBasedLock fileLock = new FileBasedLock(getLockPath());

        try {
            //
            fileLock.lock();

            //
            byte[] encryptedBytes;

            try {
                encryptedBytes = Crypt32Util.cryptProtectData(password.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                throw new PasswordSaveException("Unsupported encoding 'UTF-8' specified");
            } catch (Exception ex) {
                throw new PasswordSaveException("Failed to encrypt password");
            }

            //
            ArrayList<PasswordEntry> entries = loadPasswordEntries();
            PasswordEntry targetEntry = null;

            for (PasswordEntry entry : entries) {
                if (entry.getService().equals(service) && entry.getAccount().equals(account)) {
                    targetEntry = entry;
                    break;
                }
            }

            if (targetEntry != null) {
                targetEntry.setPassword(encryptedBytes);
            } else {
                entries.add(new PasswordEntry(service, account, encryptedBytes));
            }

            //
            savePasswordEnetires(entries);
        } finally {
            try {
                fileLock.release();
            } catch (Exception ex) {
                Logger.getLogger(WindowsDPAPIBackend.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Gets backend ID
     */
    @Override
    public String getID() {
        return "WindowsDPAPI";
    }

    /**
     * Returns path to a file for lock
     */
    public String getLockPath() {
        return m_keyStorePath + ".lock";
    }

    /**
     * Loads password entries to a file.
     * This method is not thread/process safe.
     */
    private ArrayList<PasswordEntry> loadPasswordEntries() {
        ArrayList<PasswordEntry> entries = new ArrayList<PasswordEntry>();

        try {
        	ObjectInputStream fin = new ObjectInputStream(new FileInputStream(m_keyStorePath));
            try {
            	entries.addAll(Arrays.asList((PasswordEntry[])fin.readObject()));
            }
            finally {
            	fin.close();
            }
        } catch (Exception ex) {
            Logger.getLogger(WindowsDPAPIBackend.class.getName()).log(Level.SEVERE, null, ex);
        }

        return entries;
    }

    /**
     * Saves password entries to a file
     * This method is not thread/process safe.
     *
     * @param entries   Password entries to be saved
     *
     * @throws PasswordSaveException    Thrown when an error happened while writing to a file
     */
    private void savePasswordEnetires(ArrayList<PasswordEntry> entries)
            throws PasswordSaveException {

        try {
            ObjectOutputStream fout = new ObjectOutputStream(new FileOutputStream(m_keyStorePath));
            try {
	            fout.writeObject(entries.toArray(new PasswordEntry[0]));
	            fout.flush();
            }
            finally {
            	fout.close();
            }
        } catch (Exception ex) {
            Logger.getLogger(WindowsDPAPIBackend.class.getName()).log(Level.SEVERE, null, ex);
            throw new PasswordSaveException("Failed to save password entries to a file");
        }
    }

} // class WindowsDPAPIBackend
