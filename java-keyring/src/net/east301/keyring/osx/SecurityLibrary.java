/**
 * @author  $Author$
 * @date    $Date$
 * @version $Revision$
 */

package net.east301.keyring.osx;

import com.sun.jna.Library;
import com.sun.jna.Pointer;

/**
 * OS X Security library
 */
interface SecurityLibrary extends Library {

    public static final int ERR_SEC_SUCCESS = 0;
    public static final int ERR_SEC_ITEM_NOT_FOUND = -25300;

    public int SecKeychainFindGenericPassword(  // OSStatus
            Pointer keychainOrArray,            // CFTypeRef
            int serviceNameLength,              // UInt32
            byte[] serviceName,                 // const char*
            int accountNameLength,              // UInt32
            byte[] accountName,                 // const char*
            int[] passwordLength,               // UInt32*
            Pointer[] passwordData,             // void**
            Pointer[] itemRef);                 // SecKeychaingItemRef*

    public int SecKeychainAddGenericPassword(   // OSStatus
            Pointer keychain,                   // SecKeychainRef
            int serviceNameLength,              // UInt32
            byte[] serviceName,                 // const char*
            int accountNameLength,              // UInt32
            byte[] accountName,                 // const char*
            int passwordLength,                 // UInt32
            byte[] passwordData,                // const void*
            Pointer itemRef);                   // SecKeychainItemRef

    public int SecKeychainItemModifyContent(    // OSStatus
            Pointer itemRef,                    // SecKeychainItemRef
            Pointer attrList,                   // const SecKeychainAttributeList*
            int length,                         // UInt32
            byte[] data);                       // const void*

    public int SecKeychainItemDelete(           // OSStatus
            Pointer itemRef);                   // SecKeychainItemRef

    public Pointer SecCopyErrorMessageString(   // CFStringRef
            int status,                         // OSStatus
            Pointer reserved);                  // void*

    public int SecKeychainItemFreeContent(      // OSStatus
            Pointer[] attrList,                 // SecKeychainAttributeList*
            Pointer data);                      // void*

} // interface SecurityLibrary
