/**
 * @author  $Author$
 * @date    $Date$
 * @version $Revision$
 */

package net.east301.keyring;

import net.east301.keyring.util.LockException;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Test of KeyringBackend class
 */
public class KeyringBackendTest {

    /**
     * Test of getKeyStorePath method, of class KeyringBackend.
     */
    @Test
    public void testGetKeyStorePath() {
        //
        KeyringBackend instance = new KeyringBackendImpl();

        //
        assertNull(instance.getKeyStorePath());

        //
        instance.setKeyStorePath("/path/to/keystore");
        assertEquals("/path/to/keystore", instance.getKeyStorePath());
    }

    /**
     * Test of setKeyStorePath method, of class KeyringBackend.
     */
    @Test
    public void testSetKeyStorePath() {
        //
        KeyringBackend instance = new KeyringBackendImpl();

        //
        instance.setKeyStorePath("/path/to/keystore");
        assertEquals("/path/to/keystore", instance.getKeyStorePath());
    }

    public class KeyringBackendImpl extends KeyringBackend {

        @Override
        public boolean isSupported() {
            return false;
        }

        @Override
        public boolean isKeyStorePathRequired() {
            return false;
        }

        @Override
        public String getPassword(String service, String account)
                throws LockException, PasswordRetrievalException {
            return "";
        }

        @Override
        public void setPassword(String service, String account, String password)
                throws LockException, PasswordSaveException {
        }

        @Override
        public String getID() {
            return "";
        }
    }

} // class KeyringBackendTest
