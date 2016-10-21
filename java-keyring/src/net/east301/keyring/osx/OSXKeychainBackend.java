/**
 * @author  $Author$
 * @date    $Date$
 * @version $Revision$
 */

package net.east301.keyring.osx;

import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import java.io.UnsupportedEncodingException;
import net.east301.keyring.BackendNotSupportedException;
import net.east301.keyring.KeyringBackend;
import net.east301.keyring.PasswordRetrievalException;
import net.east301.keyring.PasswordSaveException;

/**
 * Keyring backend which uses OS X Keychain
 */
public class OSXKeychainBackend extends KeyringBackend {

    @Override
    public void setup() throws BackendNotSupportedException {
        NativeLibraryManager.loadNativeLibraries();
    }

    /**
     * Returns true when the backend is supported
     */
    @Override
    public boolean isSupported() {
        return Platform.isMac();
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
            throws PasswordRetrievalException {

        //
        byte[] serviceBytes, accountBytes;

        try {
            serviceBytes = service.getBytes("UTF-8");
            accountBytes = account.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new PasswordRetrievalException("Unsupported encoding 'UTF-8' specified");
        }

        //
        int[] dataLength = new int[1];
        Pointer[] data = new Pointer[1];

        //
        int status = NativeLibraryManager.Security.SecKeychainFindGenericPassword(
                null, serviceBytes.length, serviceBytes,
                accountBytes.length, accountBytes,
                dataLength, data, null);
        if (status != 0) {
            throw new PasswordRetrievalException(convertErrorCodeToMessage(status));
        }

        //
        byte[] passwordBytes = data[0].getByteArray(0, dataLength[0]);

        //
        NativeLibraryManager.Security.SecKeychainItemFreeContent(null, data[0]);

        //
        try {
            return new String(passwordBytes, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new PasswordRetrievalException("Unsupported encoding 'UTF-8' specified");
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
            throws PasswordSaveException {

        //
        byte[] serviceBytes, accountBytes, passwordBytes;

        try {
            serviceBytes = service.getBytes("UTF-8");
            accountBytes = account.getBytes("UTF-8");
            passwordBytes = password.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new PasswordSaveException("Unsupported encoding 'UTF-8' specified");
        }

        //
        Pointer[] itemRef = new Pointer[1];

        //
        int status = NativeLibraryManager.Security.SecKeychainFindGenericPassword(
                    null, serviceBytes.length, serviceBytes,
                    accountBytes.length, accountBytes,
                    null, null, itemRef);

        if (status != SecurityLibrary.ERR_SEC_SUCCESS
                && status != SecurityLibrary.ERR_SEC_ITEM_NOT_FOUND) {
            throw new PasswordSaveException(convertErrorCodeToMessage(status));
        }

        //
        if (itemRef[0] != null) {
            status = NativeLibraryManager.Security.SecKeychainItemModifyContent(
                    itemRef[0], null, passwordBytes.length, passwordBytes);

            // TODO: add code to release itemRef[0]
        } else {
            status = NativeLibraryManager.Security.SecKeychainAddGenericPassword(
                    Pointer.NULL, serviceBytes.length, serviceBytes,
                    accountBytes.length, accountBytes,
                    passwordBytes.length, passwordBytes, null);
        }

        if (status != 0) {
            throw new PasswordSaveException(convertErrorCodeToMessage(status));
        }
    }

    /**
     * Gets backend ID
     */
    @Override
    public String getID() {
        return "OSXKeychain";
    }

    /**
     * Converts OSStat to error message
     *
     * @param errorCode OSStat to be converted
     */
    private String convertErrorCodeToMessage(int errorCode) {
        //
        Pointer msgPtr = NativeLibraryManager.Security.SecCopyErrorMessageString(errorCode, null);
        if (msgPtr == null) { return null; }

        //
        int bufSize = (int)NativeLibraryManager.CoreFoundation.CFStringGetLength(msgPtr);
        char[] buf = new char[bufSize];

        for (int i = 0; i < buf.length; i++) {
            buf[i] = NativeLibraryManager.CoreFoundation.CFStringGetCharacterAtIndex(msgPtr, i);
        }

        //
        NativeLibraryManager.CoreFoundation.CFRelease(msgPtr);

        //
        return new String(buf);
    }

} // class OSXKeychainBackend
