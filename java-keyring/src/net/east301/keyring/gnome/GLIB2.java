/**
 * @author  $Author$
 * @date    $Date$
 * @version $Revision$
 */

package net.east301.keyring.gnome;

import com.sun.jna.Library;
import com.sun.jna.Pointer;

/**
 * GLib2 library
 */
interface GLIB2 extends Library {
	void g_set_application_name(String string);
} // interface GLIB2
