/**
 * @author  $Author$
 * @date    $Date$
 * @version $Revision$
 */

package net.east301.keyring;

import com.sun.jna.Platform;
import java.util.Arrays;

import net.east301.keyring.gnome.GNOMEKeyringBackend;
import net.east301.keyring.memory.UncryptedMemoryBackend;
import net.east301.keyring.osx.OSXKeychainBackend;
import net.east301.keyring.windows.WindowsDPAPIBackend;
import static org.junit.Assert.*;
import static org.junit.Assume.*;
import org.junit.Test;

/**
 * Test of KeyringBackendFactory class
 */
public class KeyringBackendFactoryTest {

    /**
     * Test of create method, of class KeyringBackendFactory.
     */
    @Test
    public void testCreate_0args() throws Exception {
        //
        KeyringBackend backend = KeyringBackendFactory.create();
        assertNotNull(backend);

        //
        if (Platform.isMac()) {
            assertTrue(backend instanceof OSXKeychainBackend);
        } else if (Platform.isWindows()) {
            assertTrue(backend instanceof WindowsDPAPIBackend);
        } else if (Platform.isLinux()) {
            assertTrue(backend instanceof GNOMEKeyringBackend);
        } else {
            fail("Unsupported platform");
        }
    }

    /**
     * Test of create method, of class KeyringBackendFactory
     * by specifying OSXKeychain.
     */
    @Test
    public void testCreate_String_OSXKeychain() throws Exception {
        //
        assumeTrue(Platform.isMac());

        //
        KeyringBackend backend = KeyringBackendFactory.create("OSXKeychain");

        assertNotNull(backend);
        assertTrue(backend instanceof OSXKeychainBackend);
    }

    /**
     * Test of create method, of class KeyringBackendFactory
     * by specifying WindowsDPAPI.
     */
    @Test
    public void testCreate_String_WindowsDPAPI() throws Exception {
        //
        assumeTrue(Platform.isWindows());

        //
        KeyringBackend backend = KeyringBackendFactory.create("WindowsDPAPI");

        assertNotNull(backend);
        assertTrue(backend instanceof WindowsDPAPIBackend);
    }

    /**
     * Test of create method, of class KeyringBackendFactory
     * by specifying UncryptedMemory.
     */
    @Test
    public void testCreate_String_UncryptedMemory() throws Exception {
        //
        KeyringBackend backend = KeyringBackendFactory.create("UncryptedMemory");

        assertNotNull(backend);
        assertTrue(backend instanceof UncryptedMemoryBackend);
    }

    /**
     * Test of create method, of class KeyringBackendFactory
     * by specifying invalid backend name.
     */
    @Test(expected = BackendNotSupportedException.class)
    public void testCreate_String_Invalid() throws Exception {
        KeyringBackendFactory.create("MyInvalidBackendName");
    }

    /**
     * Test of getAllBackendNames method, of class KeyringBackendFactory.
     */
    @Test
    public void testGetAllBackendNames() {
        //
        String[] backends = KeyringBackendFactory.getAllBackendNames();

        //
        assertTrue(backends.length == 4);
        assertTrue(Arrays.asList(backends).contains("OSXKeychain"));
        assertTrue(Arrays.asList(backends).contains("WindowsDPAPI"));
        assertTrue(Arrays.asList(backends).contains("GNOMEKeyring"));
        assertTrue(Arrays.asList(backends).contains("UncryptedMemory"));
    }

} // class KeyringBackendFactoryTest
