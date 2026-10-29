/*************************************************************************

Copyright (C) 2009 Grandite

This file is part of Open ModelSphere.

Open ModelSphere is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
or see http://www.gnu.org/licenses/.

You can redistribute and/or modify this particular file even under the
terms of the GNU Lesser General Public License (LGPL) as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

You should have received a copy of the GNU Lesser General Public License 
(LGPL) along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
or see http://www.gnu.org/licenses/.

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.jack.io;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * 
 * A writer that writes a temporary file and automatically deletes it. Cannot inherit directly from
 * FileWriter because cannot call its constructor
 * 
 * @author Marco Savard
 * 
 */
public final class TemporaryFileWriter extends Writer {
    private static final String PREF = "zzz"; // NOT LOCALIZABLE
    private static final String SUFF = "zzz"; // NOT LOCALIZABLE
    private File m_file;
    private FileWriter m_writer;

    // Create a temporary file
    public TemporaryFileWriter() throws IOException {
        m_file = File.createTempFile(PREF, SUFF);
        m_file.deleteOnExit();
        try {
            m_writer = new FileWriter(m_file);
        } catch (IOException ex) {
            try {
                java.nio.file.Files.deleteIfExists(m_file.toPath());
            } catch (IOException cleanupEx) {
                ex.addSuppressed(cleanupEx);
            }
            throw ex;
        }
    }

    // ////////////////////////
    // OVERRIDES Object
    public String toString() {
        if (m_file == null) {
            return "";
        }
        try {
            if (m_writer != null) {
                m_writer.flush();
            }
            try (FileReader reader = new FileReader(m_file)) {
                StringWriter writer = new StringWriter();
                char[] buffer = new char[1024];
                int charsRead;
                while ((charsRead = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, charsRead);
                }
                return writer.toString();
            }
        } catch (IOException ex) {
            return ex.toString();
        }
    }

    //
    // ////////////////////////

    // ////////////////////////
    // OVERRIDES Writer
    public void write(char[] cbuf, int off, int len) throws IOException {
        if (m_writer == null) {
            throw new IOException("Temporary file writer is closed");
        }
        m_writer.write(cbuf, off, len);
    }

    public void flush() throws IOException {
        if (m_writer == null) {
            throw new IOException("Temporary file writer is closed");
        }
        m_writer.flush();
    }

    public void close() throws IOException {
        if (m_writer != null) {
            m_writer.close();
            m_writer = null;
        }
    }

    //
    // ////////////////////////

    // Dump the content of a reader into the temporary file
    public void dump(Reader reader) throws IOException {
        char[] buffer = new char[1024];
        int count;
        while ((count = reader.read(buffer)) != -1) {
            write(buffer, 0, count);
        }
        flush();
    }

    public File getFile() {
        return m_file;
    }

    public void cleanup() {
        if (m_file == null) {
            return;
        }
        File file = m_file;
        IOException failure = null;
        try {
            close();
        } catch (IOException ex) {
            failure = ex;
        }
        boolean deleted = false;
        try {
            java.nio.file.Files.deleteIfExists(file.toPath());
            deleted = true;
        } catch (IOException ex) {
            if (failure == null) {
                failure = ex;
            } else {
                failure.addSuppressed(ex);
            }
        }
        if (deleted) {
            m_file = null;
            m_writer = null;
        }
        if (failure != null) {
            throw new java.io.UncheckedIOException(
                    "Could not clean up temporary file: " + file, failure);
        }
    }
}
