/**
 * @author  $Author$
 * @date    $Date$
 * @version $Revision$
 */

package net.east301.keyring.example;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.east301.keyring.BackendNotSupportedException;
import net.east301.keyring.Keyring;
import net.east301.keyring.PasswordRetrievalException;
import net.east301.keyring.PasswordSaveException;
import net.east301.keyring.util.LockException;

/**
 * Usage example of java-keyring library
 */
public class Program {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //
        // setup a Keyring instance
        //
        Keyring keyring;

        // create an instance of Keyring by invoking Keyring.create method
        //
        // Keyring.create method finds appropriate keyring backend, and sets it up for you.
        // On Mac OS X environment, OS X Keychain is used, and On Windows environment,
        // DPAPI is used for encryption of passwords.
        // If no supported backend is found, BackendNotSupportedException is thrown.
        try {
            keyring = Keyring.create();
        } catch (BackendNotSupportedException ex) {
            Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }

        // some backend directory handles a file to store password to disks.
        // in this case, we must set path to key store file by Keyring.setKeyStorePath
        // before using Keyring.getPassword and Keyring.getPassword.
        if (keyring.isKeyStorePathRequired()) {
            try {
                File keyStoreFile = File.createTempFile("keystore", ".keystore");
                keyring.setKeyStorePath(keyStoreFile.getPath());
            } catch (IOException ex) {
                Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //
        // store password to key store
        //

        // Password can be stored to key store by using Keyring.setPassword method.
        // PasswordSaveException is thrown when some error happened while saving password.
        // LockException is thrown when keyring backend failed to lock key store file.
        try {
            keyring.setPassword("My service name", "My account name", "My password");
        } catch (LockException ex) {
            Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
            return;
        } catch (PasswordSaveException ex) {
            Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }

        //
        // Retrieve password from key store
        //

        // Password can be retrieved by using Keyring.getPassword method.
        // PasswordRetrievalException is thrown when some error happened while getting password.
        // LockException is thrown when keyring backend failed to lock key store file.
        try {
            String password = keyring.getPassword("My service name", "My account name");
            System.out.println(password);
        } catch (LockException ex) {
            Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PasswordRetrievalException ex) {
            Logger.getLogger(Program.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
} // class Program
