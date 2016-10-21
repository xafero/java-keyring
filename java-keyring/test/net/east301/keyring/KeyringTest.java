/**
 * @author  $Author$
 * @date    $Date$
 * @version $Revision$
 */

package net.east301.keyring;

import com.sun.jna.Platform;
import java.io.File;

import net.east301.keyring.gnome.GNOMEKeyringBackend;
import net.east301.keyring.memory.UncryptedMemoryBackend;
import net.east301.keyring.osx.OSXKeychainBackend;
import net.east301.keyring.windows.WindowsDPAPIBackend;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Test of Keyring class
 */
public class KeyringTest {

    /**
     * Test of create method, of class Keyring.
     */
    @Test
    public void testCreate_0args() throws Exception {
        Keyring keyring = Keyring.create();

        assertNotNull(keyring);
        assertNotNull(keyring.getBackend());
        assertTrue(keyring.getBackend() instanceof KeyringBackend);
    }

    /**
     * Test of create method, of class Keyring.
     */
    @Test
    public void testCreate_String() throws Exception {
        //
        Keyring keyring;

        //
        if (Platform.isMac()) {
            keyring = Keyring.create("OSXKeychain");

            assertNotNull(keyring);
            assertNotNull(keyring.getBackend());
            assertTrue(keyring.getBackend() instanceof OSXKeychainBackend);
        } else if (Platform.isWindows()) {
            keyring = Keyring.create("WindowsDPAPI");

            assertNotNull(keyring);
            assertNotNull(keyring.getBackend());
            assertTrue(keyring.getBackend() instanceof WindowsDPAPIBackend);
        }

        //
        if (true) {
            keyring = Keyring.create("UncryptedMemory");

            assertNotNull(keyring);
            assertNotNull(keyring.getBackend());
            assertTrue(keyring.getBackend() instanceof UncryptedMemoryBackend);
        }
    }

    /**
     * Test of getBackend method, of class Keyring.
     */
    @Test
    public void testGetBackend() throws Exception {
        Keyring keyring = Keyring.create();

        assertNotNull(keyring.getBackend());

        if (Platform.isMac()) {
            assertTrue(keyring.getBackend() instanceof OSXKeychainBackend);
        } else if (Platform.isWindows()) {
            assertTrue(keyring.getBackend() instanceof WindowsDPAPIBackend);
        } else if (Platform.isLinux()) {
            assertTrue(keyring.getBackend() instanceof GNOMEKeyringBackend);
        } else {
            assertTrue(keyring.getBackend() instanceof UncryptedMemoryBackend);
        }
    }

    /**
     * Test of getKeyStorePath method, of class Keyring.
     */
    @Test
    public void testGetKeyStorePath() throws Exception {
        //
        Keyring keyring = Keyring.create();

        //
        assertNull(keyring.getKeyStorePath());

        //
        keyring.setKeyStorePath("/path/to/keystore");
        assertEquals("/path/to/keystore", keyring.getKeyStorePath());
    }

    /**
     * Test of setKeyStorePath method, of class Keyring.
     */
    @Test
    public void testSetKeyStorePath() throws Exception {
        //
        Keyring keyring = Keyring.create();

        //
        keyring.setKeyStorePath("/path/to/keystore");
        assertEquals("/path/to/keystore", keyring.getKeyStorePath());
    }

    /**
     * Test of isKeyStorePathRequired method, of class Keyring.
     */
    @Test
    public void testIsKeyStorePathRequired() throws Exception {
        //
        Keyring keyring = Keyring.create();

        //
        assertEquals(
                keyring.isKeyStorePathRequired(),
                keyring.getBackend().isKeyStorePathRequired());
    }

    /**
     * Test of getPassword method, of class Keyring.
     */
    @Test
    public void testGetPassword() throws Exception {
        //
        Keyring keyring = Keyring.create();

        if (keyring.isKeyStorePathRequired()) {
            keyring.setKeyStorePath(
                    File.createTempFile(KEYSTORE_PREFIX, KEYSTORE_SUFFIX).getPath());
        }

        //
        checkExistanceOfPasswordEntry(keyring);

        //
        keyring.setPassword(SERVICE, ACCOUNT, PASSWORD);
        assertEquals(PASSWORD, keyring.getPassword(SERVICE, ACCOUNT));
    }

    /**
     * Test of setPassword method, of class Keyring.
     */
    @Test
    public void testSetPassword() throws Exception {
        //
        Keyring keyring = Keyring.create();

        if (keyring.isKeyStorePathRequired()) {
            keyring.setKeyStorePath(
                    File.createTempFile(KEYSTORE_PREFIX, KEYSTORE_SUFFIX).getPath());
        }

        //
        keyring.setPassword(SERVICE, ACCOUNT, PASSWORD);
        assertEquals(PASSWORD, keyring.getPassword(SERVICE, ACCOUNT));
    }

    /**
     *
     * @param backend
     */
    private void checkExistanceOfPasswordEntry(Keyring keyring) {
        try {
            keyring.getPassword(SERVICE, ACCOUNT);

            System.err.println(String.format(
                    "Please remove password entry '%s' before running the tests",
                    SERVICE));
        } catch (Exception ex) {
            // do nothing
        }
    }

    /**
     *
     */
    private static final String SERVICE = "net.east301.keyring unit test";

    /**
     *
     */
    private static final String ACCOUNT = "tester";

    /**
     *
     */
    private static final String PASSWORD = "HogeHoge2012";

    /**
     *
     */
    private static final String KEYSTORE_PREFIX = "keystore";

    /**
     *
     */
    private static final String KEYSTORE_SUFFIX = ".keystore";

} // class KeyringTest
