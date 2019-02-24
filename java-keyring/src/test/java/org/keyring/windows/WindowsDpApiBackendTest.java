/*
 * Copyright © 2017, Rex Hoffman
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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.keyring.windows;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.keyring.BackendNotSupportedException;
import org.keyring.PasswordRetrievalException;
import org.keyring.PasswordSaveException;
import org.keyring.util.LockException;

import com.sun.jna.Platform;

/**
 * Test of WindowsDPAPIBackend class.
 */
public class WindowsDpApiBackendTest {

  private static final String SERVICE = "net.east301.keyring.windows unit test";

  private static final String ACCOUNT = "tester";

  private static final String PASSWORD = "HogeHoge2012";

  private static final String KEYSTORE_PREFIX = "keystore";

  private static final String KEYSTORE_SUFFIX = ".keystore";

  /**
   * Test of isSupported method, of class WindowsDPAPIBackend.
   */
  @Test
  public void testIsSupported() {
    //
    assumeTrue(Platform.isWindows());

    //
    assertTrue(new WindowsDpApiBackend().isSupported());
  }

  /**
   * Test of isKeyStorePathRequired method, of class WindowsDPAPIBackend.
   */
  @Test
  public void testIsKeyStorePathRequired() {
    //
    assumeTrue(Platform.isWindows());

    //
    assertTrue(new WindowsDpApiBackend().isKeyStorePathRequired());
  }

  /**
   * Test of getPassword method, of class WindowsDPAPIBackend by specifying
   * invalid entry.
   */
  @Test(expected = PasswordRetrievalException.class)
  public void testGetPassword_InvalidEntry() throws Exception {
    //
    assumeTrue(Platform.isWindows());

    //
    File keystore = File.createTempFile(KEYSTORE_PREFIX, KEYSTORE_SUFFIX);

    //
    WindowsDpApiBackend backend = new WindowsDpApiBackend();
    backend.setKeyStorePath(keystore.getPath());
    backend.setup();

    //
    backend.getPassword(SERVICE, ACCOUNT);
  }

  /**
   * Test of getPassword method, of class WindowsDPAPIBackend by specifying
   * valid entry.
   * 
   * @throws IOException xxx.
   * @throws BackendNotSupportedException xxx.
   * @throws LockException xxx.
   * @throws PasswordSaveException xxx.
   * @throws PasswordRetrievalException xxx.
   */
  public void testGetPassword_ValidEntry() throws IOException, BackendNotSupportedException, LockException,
      PasswordSaveException, PasswordRetrievalException {
    //
    assumeTrue(Platform.isWindows());

    //
    File keystore = File.createTempFile(KEYSTORE_PREFIX, KEYSTORE_SUFFIX);

    //
    WindowsDpApiBackend backend = new WindowsDpApiBackend();
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
    WindowsDpApiBackend backend = new WindowsDpApiBackend();
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
  public void testGetId() {
    //
    assumeTrue(Platform.isWindows());

    //
    assertEquals("WindowsDPAPI", new WindowsDpApiBackend().getId());
  }

  /**
   * Test of getLockPath method, of class WindowsDPAPIBackend.
   */
  @Test
  public void testGetLockPath() throws Exception {
    //
    assumeTrue(Platform.isWindows());

    //
    WindowsDpApiBackend backend = new WindowsDpApiBackend();
    backend.setKeyStorePath("/path/to/keystore");
    backend.setup();

    //
    assertEquals("/path/to/keystore.lock", backend.getLockPath());
  }

} // class WindowsDPAPIBackendTest