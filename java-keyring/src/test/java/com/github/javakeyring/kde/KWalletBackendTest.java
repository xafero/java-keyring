package com.github.javakeyring.kde;

import com.github.javakeyring.Keyring;
import com.github.javakeyring.KeyringStorageType;
import com.github.javakeyring.PasswordAccessException;
import com.github.javakeyring.internal.KeyringBackend;
import com.github.javakeyring.internal.kde.KWalletBackend;
import com.sun.jna.Platform;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assume.assumeTrue;

public class KWalletBackendTest {

  private static final String SERVICE = "com.github.javakeyring.kde.test";

  private static final String ACCOUNT = "username_kde";

  private static final String PASSWORD = "password_kde";

  /**
   * Test of setup method, of class KWalletKeyringBackend.
   */
  @Test
  public void testSetup() throws Exception {
    assumeTrue(Platform.isLinux() && Keyring.create().getKeyringStorageType() == KeyringStorageType.KWALLET);
    assertThat(catchThrowable(KWalletBackend::new)).as("Setup should succeed").doesNotThrowAnyException();
  }

  /**
   * Test of getPassword method, of class KWalletKeyringBackend.
   */
  @Test
  public void testPasswordFlow() throws Exception {
    assumeTrue(Platform.isLinux() && Keyring.create().getKeyringStorageType() == KeyringStorageType.KWALLET);
    KeyringBackend backend = new KWalletBackend();
    catchThrowable(() -> backend.deletePassword(SERVICE, ACCOUNT));
    checkExistanceOfPasswordEntry(backend);
    backend.setPassword(SERVICE, ACCOUNT, PASSWORD);
    assertThat(backend.getPassword(SERVICE, ACCOUNT)).isEqualTo(PASSWORD);
    backend.deletePassword(SERVICE, ACCOUNT);
    assertThatThrownBy(() -> backend.getPassword(SERVICE, ACCOUNT)).isInstanceOf(PasswordAccessException.class);
  }

  private static void checkExistanceOfPasswordEntry(KeyringBackend backend) {
    assertThatThrownBy(() -> backend.getPassword(SERVICE, ACCOUNT))
            .as("Please remove password entry '%s' " + "by using Keychain Access before running the tests", SERVICE)
            .isNotNull();
  }
}
