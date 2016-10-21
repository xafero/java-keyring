/**
 * @author  $Author$
 * @date    $Date$
 * @version $Revision$
 */

package net.east301.keyring.gnome;

import com.sun.jna.Library;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

/**
 * GKLib
 */
interface GKLib extends Library {

	int gnome_keyring_item_get_info_full_sync(String keyring, int id, int flags, PointerByReference item_info);

	void gnome_keyring_item_info_free(Pointer item_info);

	String gnome_keyring_item_info_get_secret(Pointer item_info);

	String gnome_keyring_result_to_message(int r);

	int gnome_keyring_set_network_password_sync(String keyring, String user, String domain, String server,
			String object, String protocol, String authtype, int port, String password, IntByReference item_id);

} // interface GKLib
