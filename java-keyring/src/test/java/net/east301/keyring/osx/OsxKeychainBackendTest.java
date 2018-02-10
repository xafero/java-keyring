/*
 * Copyright © 2017, Saleforce.com, Inc
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/**
 * @author  $Author$
 * @date    $Date$
 * @version $Revision$
 */

package net.east301.keyring.osx;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;

import org.junit.Test;

import com.sun.jna.Platform;

import net.east301.keyring.BackendNotSupportedException;
import net.east301.keyring.PasswordRetrievalException;

/**
 * Test of OSXKeychainBackend class.
 */
public class OsxKeychainBackendTest {

  /**
   * Test of setup method, of class OSXKeychainBackend.
   */
  @Test
  public void testSetup() throws Exception {
    //
    assumeTrue(Platform.isMac());

    //
    try {
      new OsxKeychainBackend().setup();
    } catch (BackendNotSupportedException ex) {
      fail();
    }
  }

  /**
   * Test of isSupported method, of class OSXKeychainBackend.
   */
  @Test
  public void testIsSupported() {
    //
    assumeTrue(Platform.isMac());

    //
    assertTrue(new OsxKeychainBackend().isSupported());
  }

  /**
   * Test of isKeyStorePathRequired method, of class OSXKeychainBackend.
   */
  @Test
  public void testIsKeyStorePathRequired() {
    //
    assumeTrue(Platform.isMac());

    //
    assertFalse(new OsxKeychainBackend().isKeyStorePathRequired());
  }

  /**
   * Test of getPassword method, of class OSXKeychainBackend.
   */
  @Test
  public void testGetPassword() throws Exception {
    //
    assumeTrue(Platform.isMac());

    //
    OsxKeychainBackend backend = new OsxKeychainBackend();
    backend.setup();

    //
    checkExistanceOfPasswordEntry(backend);

    //
    backend.setPassword(SERVICE, ACCOUNT, PASSWORD);
    assertTrue(PASSWORD.equals(backend.getPassword(SERVICE, ACCOUNT)));
  }

  /**
   * Test of setPassword method, of class OSXKeychainBackend.
   */
  @Test
  public void testSetPassword() throws Exception {
    //
    assumeTrue(Platform.isMac());

    //
    OsxKeychainBackend backend = new OsxKeychainBackend();
    backend.setup();

    //
    backend.setPassword(SERVICE, ACCOUNT, PASSWORD);
    assertTrue(PASSWORD.equals(backend.getPassword(SERVICE, ACCOUNT)));
  }

  /**
   * Test of getID method, of class OSXKeychainBackend.
   */
  @Test
  public void testGetId() {
    assertTrue("OSXKeychain".equals(new OsxKeychainBackend().getId()));
  }

  private static void checkExistanceOfPasswordEntry(OsxKeychainBackend backend) {
    try {
      backend.getPassword(SERVICE, ACCOUNT);

      System.err.println(String
          .format("Please remove password entry '%s' " + "by using Keychain Access before running the tests", SERVICE));
    } catch (PasswordRetrievalException ex) {
      // do nothing
    }
  }

  private static final String SERVICE = "net.east301.keyring.osx.part2 unit test";

  private static final String ACCOUNT = "testerpart2";

  private static final String PASSWORD = "HogeHoge2012part2";

} // class OSXKeychainBackendTest
