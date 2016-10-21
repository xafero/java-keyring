/**
 * @author  $Author$
 * @date    $Date$
 * @version $Revision$
 */

package net.east301.keyring.osx;

import com.sun.jna.Native;
import net.east301.keyring.BackendNotSupportedException;

/**
 * Global native library manager
 */
class NativeLibraryManager {

    public static synchronized void loadNativeLibraries() throws BackendNotSupportedException {
        if (CoreFoundation != null && Security != null) { return; }

        try {
            CoreFoundation = (CoreFoundationLibrary)Native.loadLibrary(
                    "CoreFoundation", CoreFoundationLibrary.class);
            Security = (SecurityLibrary)Native.loadLibrary(
                    "Security", SecurityLibrary.class);
        } catch (UnsatisfiedLinkError ex) {
            throw new BackendNotSupportedException("Failed to load native library");
        }
    }

    /**
     * An instance of CoreFoundationLibrary
     */
    public static CoreFoundationLibrary CoreFoundation = null;

    /**
     * An instance of SecurityLibrary
     */
    public static SecurityLibrary Security = null;

} // class NativeLibraryManager
