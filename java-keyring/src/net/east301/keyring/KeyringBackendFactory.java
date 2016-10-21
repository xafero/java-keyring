/**
 * @author  $Author$
 * @date    $Date$
 * @version $Revision$
 */

package net.east301.keyring;

import java.util.ArrayList;

import net.east301.keyring.gnome.GNOMEKeyringBackend;
import net.east301.keyring.memory.UncryptedMemoryBackend;
import net.east301.keyring.osx.OSXKeychainBackend;
import net.east301.keyring.windows.WindowsDPAPIBackend;

/**
 * Factory of KeyringBackend
 */
class KeyringBackendFactory {

    /**
     * Creates an instance of KeyringBackend
     */
    public static KeyringBackend create() throws BackendNotSupportedException {

        for (Object[] entry : KeyringBackendFactory.KEYRING_BACKENDS) {
            String name = (String)entry[0];
            Class cls = (Class)entry[1];

            KeyringBackend backend = tryToCreateBackend(cls);
            if (backend != null) { return backend; }
        }

        throw new BackendNotSupportedException("No available keyring backend found");
    }

    /**
     * Creates an instance of KeyringBackend
     *
     * @param preferred Preferred backend name
     */
    public static KeyringBackend create(String preferred) throws BackendNotSupportedException {
        //
        Class backendClass = null;

        for (Object[] entry : KeyringBackendFactory.KEYRING_BACKENDS) {
            String name = (String)entry[0];
            Class cls = (Class)entry[1];

            if (name.equals(preferred)) {
                backendClass = cls;
                break;
            }
        }

        if (backendClass == null) {
            throw new BackendNotSupportedException(
                    String.format("The backend '%s' is not registered", preferred));
        }

        //
        KeyringBackend backend = tryToCreateBackend(backendClass);
        if (backend == null) {
            throw new BackendNotSupportedException(
                    String.format("The backend '%s' is not supported", preferred));
        }

        //
        return backend;
    }

    /**
     * Returns names of registered keyring backends
     */
    public static String[] getAllBackendNames() {
        ArrayList<String> result = new ArrayList<String>();
        for (Object[] entry : KeyringBackendFactory.KEYRING_BACKENDS) {
            result.add((String)entry[0]);
        }

        return result.toArray(new String[0]);
    }

    /**
     * Try to create keyring backend instance from Class
     *
     * @param backendClass  Target backend class
     */
    private static KeyringBackend tryToCreateBackend(Class backendClass) {
        //
        KeyringBackend backend;
        try {
            backend = (KeyringBackend)backendClass.newInstance();
        } catch (Exception ex) {
            // TODO: add code to handle exception
            return null;
        }

        //
        if (!backend.isSupported()) { return null; }

        //
        try {
            backend.setup();
        } catch (BackendNotSupportedException ex) {
            // TODO: add code to handle exception
            return null;
        }

        return backend;
    }

    /**
     * All keyring backends
     */
    private static Object[][] KEYRING_BACKENDS = {
        { "OSXKeychain",        OSXKeychainBackend.class },
        { "GNOMEKeyring",		GNOMEKeyringBackend.class },
        { "WindowsDPAPI",       WindowsDPAPIBackend.class },
        { "UncryptedMemory",    UncryptedMemoryBackend.class }
    };

} // class KeyringBackendFactory
