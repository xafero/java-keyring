/**
 * @author  $Author$
 * @date    $Date$
 * @version $Revision$
 */

package net.east301.keyring.osx;

import com.sun.jna.Library;
import com.sun.jna.Pointer;

/**
 * OS X CoreFoundation library
 */
interface CoreFoundationLibrary extends Library {

    public long CFStringGetLength(              // CFIndex
            Pointer theString);                 // CFStringRef

    public char CFStringGetCharacterAtIndex(    // UniChar
            Pointer theString,                  // CFStringRef
            long idx);                          // CFIndex

    public void CFRelease(                      // void
            Pointer cf);                        // CFTypeRef

} // interface CoreFoundationLibrary
