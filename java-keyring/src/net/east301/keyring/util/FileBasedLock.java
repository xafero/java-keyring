/**
 * @author  $Author$
 * @date    $Date$
 * @version $Revision$
 */

package net.east301.keyring.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * File based lock
 */
public class FileBasedLock {

    /**
     * Initializes an instance of FileBasedLock
     *
     * @param path  Path to a file to be used to lock
     */
    public FileBasedLock(String path) {
        m_path = path;
    }

    /**
     * Lock
     */
    public synchronized void lock() throws LockException {
        if (m_file != null || m_channel != null || m_lock != null) {
            throw new LockException("Already locked", null);
        }

        try {
            m_file = new File(m_path);
            m_file.createNewFile();

            m_channel = new RandomAccessFile(m_file, "rw").getChannel();
            m_lock = m_channel.lock();
        } catch (IOException ex) {
            throw new LockException("Failed to obtain lock", ex);
        }
    }

    /**
     * Release lock
     */
    public synchronized void release() throws LockException {
        //
        try {
            if (m_lock != null && m_lock.isValid()) { m_lock.release(); }
        } catch (Exception ex) {
            Logger.getLogger(FileBasedLock.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            if (m_channel != null && m_channel.isOpen()) { m_channel.close(); }
        } catch (Exception ex) {
            Logger.getLogger(FileBasedLock.class.getName()).log(Level.SEVERE, null, ex);
        }

        //
        m_file = null;
        m_channel = null;
        m_lock = null;
    }

    /**
     * Returns path to a file to be used to lock
     */
    public String getPath() {
        return m_path;
    }

    /**
     * Path to a file to be used to lock
     */
    private final String m_path;

    /**
     * A File instance to be used to lock
     */
    private File m_file;

    /**
     * A FileChannel instance obtained from m_file
     */
    private FileChannel m_channel;

    /**
     * A FileLock instance obtained from m_channel
     */
    private FileLock m_lock;

} // class FileBasedLock
