/*
 * Copyright © 2019, Java Keyring
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
package com.github.javakeyring.internal.osx;

import com.github.javakeyring.BackendNotSupportedException;
import com.github.javakeyring.PasswordAccessException;
import com.github.javakeyring.internal.KeyringBackend;

import pt.davidafsilva.apple.OSXKeychain;
import pt.davidafsilva.apple.OSXKeychainException;

/**
 * Keyring backend which uses modern OS X Keychain.
 */
public class ModernOsxKeychainBackend implements KeyringBackend {

  private OSXKeychain keychain;

  public ModernOsxKeychainBackend() throws BackendNotSupportedException {
    if(System.getProperty("os.name", "").toLowerCase().contains("mac os")) {
	  try {
		keychain = OSXKeychain.getInstance();
	  } catch (OSXKeychainException e) {
		  throw new BackendNotSupportedException("Modern OSX Keychain not supported.", e);
	  }
    }
    else {
      throw new BackendNotSupportedException("Not running on Mac OS.");
    }
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
   * @throws PasswordAccessException
   *           Thrown when an error happened while getting password
   */
  @Override
  public String getPassword(String service, String account) throws PasswordAccessException {
	try {
		return keychain.findGenericPassword(service, account).orElseThrow(() -> new PasswordAccessException("No stored credentials match " + service + " account: " + account));
	} catch (OSXKeychainException e) {
		throw new PasswordAccessException("Failed to get credential. " + e.getMessage());
	}
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
   * @throws PasswordAccessException
   *           Thrown when an error happened while saving the password
   */
  @Override
  public void setPassword(String service, String account, String password) throws PasswordAccessException {
	try {
      try {
        getPassword(service, account);
        keychain.modifyGenericPassword(service, account, password);
      }
	  catch(PasswordAccessException pae) {
        keychain.addGenericPassword(service, account, password);
      }
    }
    catch(OSXKeychainException e) {
      throw new PasswordAccessException("Failed to set credential. " + e.getMessage());
	}
  }

  /**
   * Delete a password from the key store.
   *
   * @param service
   *          Service name
   * @param account
   *          Account name
   *
   * @throws PasswordAccessException
   *           Thrown when an error happened while saving the password
   */
  public void deletePassword(String service, String account) throws PasswordAccessException {
	try {
       keychain.deleteGenericPassword(service, account);
    }
    catch(OSXKeychainException e) {
	  throw new PasswordAccessException("Failed to set credential. " + e.getMessage());
    }
  }

  @Override
  public void close() throws Exception {
  }
}
