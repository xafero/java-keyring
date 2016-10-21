/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.east301.keyring.windows;

import com.sun.jna.Platform;
import java.io.File;
import net.east301.keyring.PasswordRetrievalException;
import static org.junit.Assert.*;
import static org.junit.Assume.*;
import org.junit.Test;

/**
 * Test of WindowsDPAPIBackend class
 */
public class WindowsDPAPIBackendTest {

    /**
     * Test of isSupported method, of class WindowsDPAPIBackend.
     */
    @Test
    public void testIsSupported() {
        //
        assumeTrue(Platform.isWindows());

        //
        assertTrue(new WindowsDPAPIBackend().isSupported());
    }

    /**
     * Test of isKeyStorePathRequired method, of class WindowsDPAPIBackend.
     */
    @Test
    public void testIsKeyStorePathRequired() {
        //
        assumeTrue(Platform.isWindows());

        //
        assertTrue(new WindowsDPAPIBackend().isKeyStorePathRequired());
    }

    /**
     * Test of getPassword method, of class WindowsDPAPIBackend
     * by specifying invalid entry.
     */
    @Test(expected = PasswordRetrievalException.class)
    public void testGetPassword_InvalidEntry() throws Exception {
        //
        assumeTrue(Platform.isWindows());

        //
        File keystore = File.createTempFile(KEYSTORE_PREFIX, KEYSTORE_SUFFIX);

        //
        WindowsDPAPIBackend backend = new WindowsDPAPIBackend();
        backend.setKeyStorePath(keystore.getPath());
        backend.setup();

        //
        backend.getPassword(SERVICE, ACCOUNT);
    }

    /**
     * Test of getPassword method, of class WindowsDPAPIBackend
     * by specifying valid entry.
     */
    public void testGetPassword_ValidEntry() throws Exception {
        //
        assumeTrue(Platform.isWindows());

        //
        File keystore = File.createTempFile(KEYSTORE_PREFIX, KEYSTORE_SUFFIX);

        //
        WindowsDPAPIBackend backend = new WindowsDPAPIBackend();
        backend.setKeyStorePath(keystore.getPath());
        backend.setup();

        //
        backend.setPassword(SERVICE, ACCOUNT, PASSWORD);
        assertEquals(PASSWORD, backend.getPassword(SERVICE, ACCOUNT));
    }

    /**
     * Test of setPassword method, of class WindowsDPAPIBackend.
     */
    @Test
    public void testSetPassword() throws Exception {
        //
        assumeTrue(Platform.isWindows());

        //
        File keystore = File.createTempFile(KEYSTORE_PREFIX, KEYSTORE_SUFFIX);

        //
        WindowsDPAPIBackend backend = new WindowsDPAPIBackend();
        backend.setKeyStorePath(keystore.getPath());
        backend.setup();

        //
        backend.setPassword(SERVICE, ACCOUNT, PASSWORD);
        assertEquals(PASSWORD, backend.getPassword(SERVICE, ACCOUNT));
    }

    /**
     * Test of getID method, of class WindowsDPAPIBackend.
     */
    @Test
    public void testGetID() {
        //
        assumeTrue(Platform.isWindows());

        //
        assertEquals("WindowsDPAPI", new WindowsDPAPIBackend().getID());
    }

    /**
     * Test of getLockPath method, of class WindowsDPAPIBackend.
     */
    @Test
    public void testGetLockPath() throws Exception {
        //
        assumeTrue(Platform.isWindows());

        //
        WindowsDPAPIBackend backend = new WindowsDPAPIBackend();
        backend.setKeyStorePath("/path/to/keystore");
        backend.setup();

        //
        assertEquals("/path/to/keystore.lock", backend.getLockPath());
    }

    /**
     *
     */
    private static final String SERVICE = "net.east301.keyring.windows unit test";

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

} // class WindowsDPAPIBackendTest
