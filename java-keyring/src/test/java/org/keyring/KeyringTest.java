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
package org.keyring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;
import org.keyring.gnome.GnomeKeyringBackend;
import org.keyring.memory.UnencryptedMemoryBackend;
import org.keyring.osx.OsxKeychainBackend;
import org.keyring.windows.WindowsDpApiBackend;

import com.sun.jna.Platform;

/**
 * Test of Keyring class.
 */
public class KeyringTest {

  private static final String SERVICE = "net.east301.keyring unit test";

  private static final String ACCOUNT = "tester";

  private static final String PASSWORD = "HogeHoge2012";

  private static final String KEYSTORE_PREFIX = "keystore";

  private static final String KEYSTORE_SUFFIX = ".keystore";
  
  /**
   * Test of create method, of class Keyring.
   */
  @Test
  public void testCreateZeroArgs() throws Exception {
    Keyring keyring = Keyring.create();
    assertNotNull(keyring);
    assertNotNull(keyring.getBackend());
    //assertTrue(keyring.getBackend() instanceof KeyringBackend);
  }

  /**
   * Test of create method, of class Keyring.
   */
  @Test
  public void testCreateString() throws Exception {
    Keyring keyring;
    if (Platform.isMac()) {
      keyring = Keyring.create("OSXKeychain");
      assertNotNull(keyring);
      assertNotNull(keyring.getBackend());
      assertTrue(keyring.getBackend() instanceof OsxKeychainBackend);
    } else if (Platform.isWindows()) {
      keyring = Keyring.create("WindowsDPAPI");
      assertNotNull(keyring);
      assertNotNull(keyring.getBackend());
      assertTrue(keyring.getBackend() instanceof WindowsDpApiBackend);
    } else if (Platform.isLinux()) {
      keyring = Keyring.create("GNOMEKeyring");
      assertNotNull(keyring);
      assertNotNull(keyring.getBackend());
      assertTrue(keyring.getBackend() instanceof GnomeKeyringBackend);
    }
    keyring = Keyring.create("UnencryptedMemory");
    assertNotNull(keyring);
    assertNotNull(keyring.getBackend());
    assertTrue(keyring.getBackend() instanceof UnencryptedMemoryBackend);
  }

  /**
   * Test of getBackend method, of class Keyring.
   */
  @Test
  public void testGetBackend() throws Exception {
    Keyring keyring = Keyring.create();

    assertNotNull(keyring.getBackend());

    if (Platform.isMac()) {
      assertTrue(keyring.getBackend() instanceof OsxKeychainBackend);
    } else if (Platform.isWindows()) {
      assertTrue(keyring.getBackend() instanceof WindowsDpApiBackend);
    } else if (Platform.isLinux()) {
      assertTrue(keyring.getBackend() instanceof GnomeKeyringBackend);
    } else {
      assertTrue(keyring.getBackend() instanceof UnencryptedMemoryBackend);
    }
  }

  /**
   * Test of getKeyStorePath method, of class Keyring.
   */
  @Test
  public void testGetKeyStorePath() throws Exception {
    Keyring keyring = Keyring.create();
    assertNull(keyring.getKeyStorePath());
    keyring.setKeyStorePath("/path/to/keystore");
    assertEquals("/path/to/keystore", keyring.getKeyStorePath());
  }

  /**
   * Test of setKeyStorePath method, of class Keyring.
   */
  @Test
  public void testSetKeyStorePath() throws Exception {
    Keyring keyring = Keyring.create();
    keyring.setKeyStorePath("/path/to/keystore");
    assertEquals("/path/to/keystore", keyring.getKeyStorePath());
  }

  /**
   * Test of isKeyStorePathRequired method, of class Keyring.
   */
  @Test
  public void testIsKeyStorePathRequired() throws Exception {
    Keyring keyring = Keyring.create();
    assertEquals(keyring.isKeyStorePathRequired(), keyring.getBackend().isKeyStorePathRequired());
  }

  /**
   * Test of getPassword method, of class Keyring.
   */
  @Test
  public void testGetPassword() throws Exception {
    Keyring keyring = Keyring.create();
    if (keyring.isKeyStorePathRequired()) {
      keyring.setKeyStorePath(File.createTempFile(KEYSTORE_PREFIX, KEYSTORE_SUFFIX).getPath());
    }
    checkExistanceOfPasswordEntry(keyring);
    keyring.setPassword(SERVICE, ACCOUNT, PASSWORD);
    assertEquals(PASSWORD, keyring.getPassword(SERVICE, ACCOUNT));
  }

  /**
   * Test of setPassword method, of class Keyring.
   */
  @Test
  public void testSetPassword() throws Exception {
    Keyring keyring = Keyring.create();
    if (keyring.isKeyStorePathRequired()) {
      keyring.setKeyStorePath(File.createTempFile(KEYSTORE_PREFIX, KEYSTORE_SUFFIX).getPath());
    }
    keyring.setPassword(SERVICE, ACCOUNT, PASSWORD);
    assertEquals(PASSWORD, keyring.getPassword(SERVICE, ACCOUNT));
  }

  private void checkExistanceOfPasswordEntry(Keyring keyring) {
    try {
      keyring.getPassword(SERVICE, ACCOUNT);
      System.err.println(String.format("Please remove password entry '%s' before running the tests", SERVICE));
    } catch (Exception ex) {
      //TODO: better solution needed
    }
  }
}
