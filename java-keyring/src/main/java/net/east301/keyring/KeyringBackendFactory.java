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

package net.east301.keyring;

import java.util.ArrayList;

import net.east301.keyring.gnome.GnomeKeyringBackend;
import net.east301.keyring.memory.UncryptedMemoryBackend;
import net.east301.keyring.osx.OsxKeychainBackend;
import net.east301.keyring.windows.WindowsDpApiBackend;

/**
 * Factory of KeyringBackend.
 */
class KeyringBackendFactory {

  /**
   * Creates an instance of KeyringBackend.
   */
  public static KeyringBackend create() throws BackendNotSupportedException {

    for (Object[] entry : KeyringBackendFactory.KEYRING_BACKENDS) {
      //String name = (String) entry[0];
      Class<?> cls = (Class<?>) entry[1];

      KeyringBackend backend = tryToCreateBackend(cls);
      if (backend != null) {
        return backend;
      }
    }

    throw new BackendNotSupportedException("No available keyring backend found");
  }

  /**
   * Creates an instance of KeyringBackend.
   *
   * @param preferred
   *          Preferred backend name
   */
  public static KeyringBackend create(String preferred) throws BackendNotSupportedException {
    //
    Class<?> backendClass = null;

    for (Object[] entry : KeyringBackendFactory.KEYRING_BACKENDS) {
      String name = (String) entry[0];
      Class<?> cls = (Class<?>) entry[1];

      if (name.equals(preferred)) {
        backendClass = cls;
        break;
      }
    }

    if (backendClass == null) {
      throw new BackendNotSupportedException(String.format("The backend '%s' is not registered", preferred));
    }

    //
    KeyringBackend backend = tryToCreateBackend(backendClass);
    if (backend == null) {
      throw new BackendNotSupportedException(String.format("The backend '%s' is not supported", preferred));
    }

    //
    return backend;
  }

  /**
   * Returns names of registered keyring backends.
   */
  public static String[] getAllBackendNames() {
    ArrayList<String> result = new ArrayList<>();
    for (Object[] entry : KeyringBackendFactory.KEYRING_BACKENDS) {
      result.add((String) entry[0]);
    }

    return result.toArray(new String[0]);
  }

  /**
   * Try to create keyring backend instance from Class.
   *
   * @param backendClass
   *          Target backend class
   */
  private static KeyringBackend tryToCreateBackend(Class<?> backendClass) {
    //
    KeyringBackend backend;
    try {
      backend = (KeyringBackend) backendClass.newInstance();
    } catch (Exception ex) {
      // TODO: add code to handle exception
      return null;
    }

    //
    if (!backend.isSupported()) {
      return null;
    }

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
   * All keyring backends.
   */
  private static Object[][] KEYRING_BACKENDS = { { "OSXKeychain", OsxKeychainBackend.class },
      { "GNOMEKeyring", GnomeKeyringBackend.class }, { "WindowsDPAPI", WindowsDpApiBackend.class },
      { "UncryptedMemory", UncryptedMemoryBackend.class } };

} // class KeyringBackendFactory
