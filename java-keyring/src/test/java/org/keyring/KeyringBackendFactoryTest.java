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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;

import java.util.Arrays;

import org.junit.Test;
import org.keyring.gnome.GnomeKeyringBackend;
import org.keyring.memory.UnencryptedMemoryBackend;
import org.keyring.osx.OsxKeychainBackend;
import org.keyring.windows.WindowsDpApiBackend;

import com.sun.jna.Platform;

/**
 * Test of KeyringBackendFactory class.
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
      assertTrue(backend instanceof OsxKeychainBackend);
    } else if (Platform.isWindows()) {
      assertTrue(backend instanceof WindowsDpApiBackend);
    } else if (Platform.isLinux()) {
      assertTrue(backend instanceof GnomeKeyringBackend);
    } else {
      fail("Unsupported platform");
    }
  }

  /**
   * Test of create method, of class KeyringBackendFactory by specifying
   * OSXKeychain.
   */
  @Test
  public void testCreateStringOsxKeychain() throws Exception {
    //
    assumeTrue(Platform.isMac());

    //
    KeyringBackend backend = KeyringBackendFactory.create("OSXKeychain");

    assertNotNull(backend);
    assertTrue(backend instanceof OsxKeychainBackend);
  }

  /**
   * Test of create method, of class KeyringBackendFactory by specifying
   * WindowsDPAPI.
   */
  @Test
  public void testCreateStringWindowsDpApi() throws Exception {
    //
    assumeTrue(Platform.isWindows());

    //
    KeyringBackend backend = KeyringBackendFactory.create("WindowsDPAPI");

    assertNotNull(backend);
    assertTrue(backend instanceof WindowsDpApiBackend);
  }

  /**
   * Test of create method, of class KeyringBackendFactory by specifying
   * UncryptedMemory.
   */
  @Test
  public void testCreate_String_UncryptedMemory() throws Exception {
    //
    KeyringBackend backend = KeyringBackendFactory.create("UncryptedMemory");

    assertNotNull(backend);
    assertTrue(backend instanceof UnencryptedMemoryBackend);
  }

  /**
   * Test of create method, of class KeyringBackendFactory by specifying invalid
   * backend name.
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