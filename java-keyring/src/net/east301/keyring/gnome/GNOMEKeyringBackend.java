/**
 * @author  $Author$
 * @date    $Date$
 * @version $Revision$
 */

package net.east301.keyring.gnome;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

import net.east301.keyring.BackendNotSupportedException;
import net.east301.keyring.KeyringBackend;
import net.east301.keyring.PasswordRetrievalException;
import net.east301.keyring.PasswordSaveException;

/**
 * Keyring backend which uses GNOME Keyring
 */
public class GNOMEKeyringBackend extends KeyringBackend {
	
    @Override
    public void setup() throws BackendNotSupportedException {
        NativeLibraryManager.loadNativeLibraries();
        
    }

    /**
     * Returns true when the backend is supported
     */
    @Override
    public boolean isSupported() {
        return Platform.isLinux();
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
            throws PasswordRetrievalException {

		PointerByReference ptr = new PointerByReference();
		Pointer item = null;
		Map<String, Integer> map = loadMap();
		Integer id = map.get(service + "/" + account);
		if(id == null)
			throw new PasswordRetrievalException("No password stored for this service and account.");
		try {
			int result = NativeLibraryManager.gklib.gnome_keyring_item_get_info_full_sync(null, id, 1, ptr);
			if (result == 0) {
				return NativeLibraryManager.gklib.gnome_keyring_item_info_get_secret(ptr.getValue());
			} else {
				throw new PasswordRetrievalException(NativeLibraryManager.gklib.gnome_keyring_result_to_message(result));
			}
		} finally {
			if (item != null)
				NativeLibraryManager.gklib.gnome_keyring_item_info_free(item);
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
    	IntByReference ref = new IntByReference();
    	int result = NativeLibraryManager.gklib.gnome_keyring_set_network_password_sync(null, account, null, service, null, null, null, 0,
    			password, ref);
		if (result != 0) {
			throw new PasswordSaveException(NativeLibraryManager.gklib.gnome_keyring_result_to_message(result));
		}
		Map<String, Integer> map = loadMap();
		map.put(service + "/" + account, ref.getValue());
		saveMap(map);
    }

    /**
     * Gets backend ID
     */
    @Override
    public String getID() {
        return "GNOMEKeyring";
    }

    /**
     * Loads map from a file.
     * This method is not thread/process safe.
     */
    @SuppressWarnings("unchecked")
	private Map<String, Integer> loadMap() {
		try {
			File f = new File(m_keyStorePath);
			if (f.exists() && f.length() > 0) {
				ObjectInputStream fin = new ObjectInputStream(new FileInputStream(f));
				try {
					return (Map<String, Integer>) fin.readObject();
				} finally {
					fin.close();
				}
			}
		} catch (Exception ex) {
			Logger.getLogger(GNOMEKeyringBackend.class.getName()).log(Level.SEVERE, null, ex);
		}
		return new HashMap<String, Integer>();
    }

    /**
     * Saves account/save to ID map to a file
     * This method is not thread/process safe.
     *
     * @param entries   Map to be saved
     *
     * @throws PasswordSaveException    Thrown when an error happened while writing to a file
     */
    private void saveMap(Map<String, Integer> map)
            throws PasswordSaveException {

        try {
        	ObjectOutputStream fout = new ObjectOutputStream(new FileOutputStream(m_keyStorePath));
            try {
	            fout.writeObject(map);
	            fout.flush();
            }
            finally {
            	fout.close();
            }
        } catch (Exception ex) {
            Logger.getLogger(GNOMEKeyringBackend.class.getName()).log(Level.SEVERE, null, ex);
            throw new PasswordSaveException("Failed to save password entries to a file");
        }
    }
} // class GNOMEKeyringBackend
