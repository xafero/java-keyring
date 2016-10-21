/**
 * @author  $Author$
 * @date    $Date$
 * @version $Revision$
 */

package net.east301.keyring.memory;

import net.east301.keyring.PasswordRetrievalException;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Test of UncryptedMemoryBackend class
 */
public class UncryptedMemoryBackendTest {

    /**
     * Test of isSupported method, of class UncryptedMemoryBackend.
     */
    @Test
    public void testIsSupported() {
        assertTrue(new UncryptedMemoryBackend().isSupported());
    }

    /**
     * Test of isKeyStorePathRequired method, of class UncryptedMemoryBackend.
     */
    @Test
    public void testIsKeyStorePathRequired() {
        assertFalse(new UncryptedMemoryBackend().isKeyStorePathRequired());
    }

    /**
     * Test of getPassword method, of class UncryptedMemoryBackend
     * by retrieving invalid entry.
     */
    @Test(expected = PasswordRetrievalException.class)
    public void testGetPassword_InalidPassword() throws Exception {
        //
        UncryptedMemoryBackend instance = new UncryptedMemoryBackend();

        //
        instance.getPassword(SERVICE, ACCOUNT);
    }

    /**
     * Test of getPassword method, of class UncryptedMemoryBackend
     * by retrieving valid entry.
     */
    @Test
    public void testGetPassword_ValidPassword() throws Exception {
        //
        UncryptedMemoryBackend instance = new UncryptedMemoryBackend();

        //
        instance.setPassword(SERVICE, ACCOUNT, PASSWORD);
        assertEquals(PASSWORD, instance.getPassword(SERVICE, ACCOUNT));
    }

    /**
     * Test of setPassword method, of class UncryptedMemoryBackend.
     */
    @Test
    public void testSetPassword() throws Exception {
        //
        UncryptedMemoryBackend instance = new UncryptedMemoryBackend();

        //
        instance.setPassword(SERVICE, ACCOUNT, PASSWORD);
        assertEquals(PASSWORD, instance.getPassword(SERVICE, ACCOUNT));
    }

    /**
     * Test of getID method, of class UncryptedMemoryBackend.
     */
    @Test
    public void testGetID() {
        assertEquals("UncryptedMemory", new UncryptedMemoryBackend().getID());
    }

    /**
     *
     */
    private static final String SERVICE = "net.east301.keyring.memory unit test";

    /**
     *
     */
    private static final String ACCOUNT = "tester";

    /**
     *
     */
    private static final String PASSWORD = "HogeHoge2012";

} // class UncryptedMemoryBackendTest
