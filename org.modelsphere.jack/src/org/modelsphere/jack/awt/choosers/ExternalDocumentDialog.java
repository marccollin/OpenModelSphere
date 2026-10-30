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

package org.modelsphere.jack.awt.choosers;

import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.text.MessageFormat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class ExternalDocumentDialog extends JDialog implements ActionListener {
    private static final String EXTERNAL_REFERENCES = LocaleMgr.screen
            .getString("ExternalReference");
    private static final String BROWSE = LocaleMgr.screen.getString("Browse");
    private static final String CANCEL = LocaleMgr.screen.getString("Cancel");
    private static final String CLOSE = LocaleMgr.screen.getString("Close");
    private static final String APPLY = LocaleMgr.screen.getString("Apply");
    private static final String SELECT = LocaleMgr.screen.getString("Select");
    private static final String OPEN_DOCUMENT = LocaleMgr.screen.getString("OpenDocument");
    private static final String CMD_PATTRN = "cmd /c \"{0}\""; // NOT
    // LOCALIZABLE

    JPanel panel1 = new JPanel();
    private JLabel jLabel1 = new JLabel();
    private JTextField txtFieldExternalDocument = new JTextField();
    private JButton buttonBrowse = new JButton();
    private JButton buttonClose = new JButton();
    private JButton buttonApply = new JButton();
    private JButton buttonOpenDoc = new JButton();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private BorderLayout borderLayout1 = new BorderLayout();
    private String m_command = null;

    public ExternalDocumentDialog(Frame frame, String title, boolean modal, String externalDocument) {
        super(frame, title, modal);
        try {
            m_command = externalDocument;
            jbInit(externalDocument);
            pack();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ExternalDocumentDialog() {
        this(null, "", false, null);
    }

    public String getCommand() {
        return m_command;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        Object src = ev.getSource();
        if (src.equals(buttonClose)) {
            this.dispose();
        } else if (src.equals(buttonBrowse)) {
            File workingDir = null;
            JFileChooser chooser = new JFileChooser(workingDir);
            int result = chooser.showDialog(this, SELECT);
            File file = chooser.getSelectedFile();
            if ((result != JFileChooser.CANCEL_OPTION) && file != null) {
                String filename = file.getAbsolutePath();
                m_command = MessageFormat.format(CMD_PATTRN, new Object[] { filename });
                txtFieldExternalDocument.setText(m_command);
                buttonClose.setText(CANCEL);
                buttonApply.setEnabled(true);
                buttonOpenDoc.setEnabled(true);
            } // end if
        } else if (src.equals(buttonApply)) {
            buttonApply.setEnabled(false);
            buttonClose.setText(CLOSE);
        } else if (src.equals(buttonOpenDoc)) {
            invoke(m_command);
        } // end if
    } // end actionPerformed()

    // Called by org.modelsphere.sms.graphic.NoticeBox
    public static void invoke(String command) {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process proc = runtime.exec(command);
        } catch (IOException ex) {
            // ?
        } // end try
    } // end invoke()

    //
    // private methods
    //

    private void jbInit(String externalDocument) throws Exception {
        panel1.setLayout(gridBagLayout1);
        this.getContentPane().setLayout(borderLayout1);
        jLabel1.setText(EXTERNAL_REFERENCES);
        txtFieldExternalDocument.setText(externalDocument);
        buttonBrowse.setText(BROWSE + "...");
        buttonClose.setText(CLOSE);
        buttonApply.setText(APPLY);
        buttonOpenDoc.setText(OPEN_DOCUMENT);
        buttonApply.setEnabled(false);
        buttonOpenDoc.setEnabled(m_command != null);

        getContentPane().add(panel1, BorderLayout.CENTER);
        panel1.add(jLabel1, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(12, 12, 6, 12),
                0, 0));
        panel1.add(txtFieldExternalDocument, new GridBagConstraints(0, 1, 3, 1, 1.0, 1.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(6, 12, 12,
                        36), 0, 6));
        panel1.add(buttonBrowse, new GridBagConstraints(2, 1, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(6, 6, 12, 12), 0,
                0));
        panel1.add(buttonClose, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(12, 3, 12, 12),
                0, 0));
        panel1.add(buttonApply, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(12, 12, 12, 3),
                5, 0));
        panel1.add(buttonOpenDoc, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(36, 12, 12, 72),
                0, 0));

        // add action listeners
        buttonBrowse.addActionListener(this);
        buttonClose.addActionListener(this);
        buttonApply.addActionListener(this);
        buttonOpenDoc.addActionListener(this);
    } // end jbInit()
} // end ExternalDocumentDialog
