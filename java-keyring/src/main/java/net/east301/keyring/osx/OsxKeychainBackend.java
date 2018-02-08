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

import java.nio.charset.Charset;

import com.sun.jna.Platform;
import com.sun.jna.Pointer;

import net.east301.keyring.BackendNotSupportedException;
import net.east301.keyring.KeyringBackend;
import net.east301.keyring.PasswordRetrievalException;
import net.east301.keyring.PasswordSaveException;

/**
 * Keyring backend which uses OS X Keychain.
 */
public class OsxKeychainBackend extends KeyringBackend {

  @Override
  public void setup() throws BackendNotSupportedException {
    NativeLibraryManager.loadNativeLibraries();
  }

  /**
   * Returns true when the backend is supported.
   */
  @Override
  public boolean isSupported() {
    return Platform.isMac();
  }

  /**
   * Returns true if the backend directory uses some file to store passwords.
   */
  @Override
  public boolean isKeyStorePathRequired() {
    return false;
  }

  /**
   * Gets password from key store.
   *
   * @param service
   *          Service name
   * @param account
   *          Account name
   *
   * @return Password related to specified service and account
   *
   * @throws PasswordRetrievalException
   *           Thrown when an error happened while getting password
   */
  @Override
  public String getPassword(String service, String account) throws PasswordRetrievalException {

    byte[] serviceBytes;
    byte[] accountBytes;

    Charset charset = Charset.forName("UTF-8");
    serviceBytes = service.getBytes(charset);
    accountBytes = account.getBytes(charset);

    //
    int[] dataLength = new int[1];
    Pointer[] data = new Pointer[1];

    //
    int status = NativeLibraryManager.Security.SecKeychainFindGenericPassword(null, serviceBytes.length, serviceBytes,
        accountBytes.length, accountBytes, dataLength, data, null);
    if (status != 0) {
      throw new PasswordRetrievalException(convertErrorCodeToMessage(status));
    }

    //
    byte[] passwordBytes = data[0].getByteArray(0, dataLength[0]);

    //
    NativeLibraryManager.Security.SecKeychainItemFreeContent(null, data[0]);

    //
    return new String(passwordBytes, charset);
  }

  /**
   * Sets password to key store.
   *
   * @param service
   *          Service name
   * @param account
   *          Account name
   * @param password
   *          Password
   *
   * @throws PasswordSaveException
   *           Thrown when an error happened while saving the password
   */
  @Override
  public void setPassword(String service, String account, String password) throws PasswordSaveException {

    //
    byte[] serviceBytes;
    byte[] accountBytes;
    byte[] passwordBytes;

    Charset charset = Charset.forName("UTF-8");
    serviceBytes = service.getBytes(charset);
    accountBytes = account.getBytes(charset);
    passwordBytes = password.getBytes(charset);

    //
    Pointer[] itemRef = new Pointer[1];

    //
    int status = NativeLibraryManager.Security.SecKeychainFindGenericPassword(null, serviceBytes.length, serviceBytes,
        accountBytes.length, accountBytes, null, null, itemRef);

    if (status != SecurityLibrary.ERR_SEC_SUCCESS && status != SecurityLibrary.ERR_SEC_ITEM_NOT_FOUND) {
      throw new PasswordSaveException(convertErrorCodeToMessage(status));
    }

    //
    if (itemRef[0] != null) {
      status = NativeLibraryManager.Security.SecKeychainItemModifyContent(itemRef[0], null, passwordBytes.length,
          passwordBytes);

      // TODO: add code to release itemRef[0]
    } else {
      status = NativeLibraryManager.Security.SecKeychainAddGenericPassword(Pointer.NULL, serviceBytes.length,
          serviceBytes, accountBytes.length, accountBytes, passwordBytes.length, passwordBytes, null);
    }

    if (status != 0) {
      throw new PasswordSaveException(convertErrorCodeToMessage(status));
    }
  }

  /**
   * Gets backend ID.
   */
  @Override
  public String getID() {
    return "OSXKeychain";
  }

  /**
   * Converts OSStat to error message.
   *
   * @param errorCode
   *          OSStat to be converted
   */
  private String convertErrorCodeToMessage(int errorCode) {
    //
    Pointer msgPtr = NativeLibraryManager.Security.SecCopyErrorMessageString(errorCode, null);
    if (msgPtr == null) {
      return null;
    }

    //
    int bufSize = (int) NativeLibraryManager.CoreFoundation.CFStringGetLength(msgPtr);
    char[] buf = new char[bufSize];

    for (int i = 0; i < buf.length; i++) {
      buf[i] = NativeLibraryManager.CoreFoundation.CFStringGetCharacterAtIndex(msgPtr, i);
    }

    //
    NativeLibraryManager.CoreFoundation.CFRelease(msgPtr);

    //
    return new String(buf);
  }

} // class OSXKeychainBackend
