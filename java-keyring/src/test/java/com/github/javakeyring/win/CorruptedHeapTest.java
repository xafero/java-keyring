package com.github.javakeyring.win;

import com.github.javakeyring.internal.windows.WinCredentialStoreBackend;
import com.sun.jna.Platform;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.Assume.assumeTrue;

/**
 * Test for crash
 * @see <a href="https://github.com/javakeyring/java-keyring/issues/30">issue #30</a>
 */
public final class CorruptedHeapTest {
    private static final String SERVICE = "com.github.javakeyring.wincred.testcrash";
    private static final String ACCOUNT = "username_wincred";
    private static final String PASSWORD = "password_wincred";

    @Test
    public void testIfCrashHappens() throws Exception {
        assumeTrue(Platform.isWindows());
        WinCredentialStoreBackend backend = new WinCredentialStoreBackend();
        catchThrowable(() -> backend.deletePassword(SERVICE, ACCOUNT));
        backend.setPassword(SERVICE, ACCOUNT, PASSWORD);
        for (int i = 0; i < 50; i++) {
            assertThat(backend.getPassword(SERVICE, ACCOUNT)).isEqualTo(PASSWORD);
            Runtime.getRuntime().gc(); // greatly increases chances of a crash
        }
        backend.deletePassword(SERVICE, ACCOUNT);
    }
}
