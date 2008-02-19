/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at http://www.netbeans.org/cddl.html
 * or http://www.netbeans.org/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.netbeans.org/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * The Original Software is GraphMaker. The Initial Developer of the Original
 * Software is Nathan L. Fiedler. Portions created by Nathan L. Fiedler
 * are Copyright (C) 2006-2007. All Rights Reserved.
 *
 * Contributor(s): Nathan L. Fiedler.
 *
 * $Id: NewFileVisualPanel1.java 2858 2007-02-26 09:05:03Z nfiedler $
 */

package com.bluemarsh.graphmaker.ui.wizard;

import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.ErrorManager;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.TemplateWizard;
import org.openide.util.NbBundle;
import org.openide.windows.WindowManager;

/**
 * The file location chooser panel for the new file wizard.
 *
 * @author  Nathan Fiedler
 */
public final class NewFileVisualPanel1 extends JPanel implements
        ActionListener, DocumentListener {
    /** silence the compiler warnings */
    private static final long serialVersionUID = 1L;
    /** Name for the 'valid' property. */
    public static final String PROP_VALID = "valid";
    /** The directory that was last selected by the user. Initially this
     * is null and the file chooser will open to a default location. */
    private static File lastOpenedDirectory;

    /**
     * Creates new form NewFileVisualPanel1.
     *
     * @param  wizard  the wizard descriptor.
     */
    public NewFileVisualPanel1(TemplateWizard wizard) {
        initComponents();
        fileNameTextField.setText("newFile");
        try {
            DataFolder folder = wizard.getTargetFolder();
            FileObject fobj = folder.getPrimaryFile();
            String name = FileUtil.getFileDisplayName(fobj);
            folderTextField.setText(name);
        } catch (IOException ioe) {
            ErrorManager.getDefault().notify(ioe);
        }
        browseButton.addActionListener(this);
        fileNameTextField.getDocument().addDocumentListener(this);
        folderTextField.getDocument().addDocumentListener(this);
        updateOutputName();
    }

    public void actionPerformed(ActionEvent event) {
        Object src = event.getSource();
        if (src == browseButton) {
            Frame frame = WindowManager.getDefault().getMainWindow();
            fileChooser.setCurrentDirectory(lastOpenedDirectory);
            int option = fileChooser.showOpenDialog(frame);
            if (option == JFileChooser.APPROVE_OPTION) {
                File dir = fileChooser.getSelectedFile();
                try {
                    String path = dir.getCanonicalPath();
                    folderTextField.setText(path);
                } catch (IOException ioe) {
                    // Unlikely to ever occur.
                    ErrorManager.getDefault().notify(ioe);
                }
                lastOpenedDirectory = fileChooser.getCurrentDirectory();
            }
        }
    }

    public void addNotify() {
        super.addNotify();
        // Perform an initial validation to fire property change event.
        validateInput();
    }

    public void changedUpdate(DocumentEvent event) {
    }

    /**
     * Returns the name of the file to be created (the filename the user
     * chose plus the standard file extension).
     *
     * @return  name of file to be created.
     */
    public String getFilename() {
        return createdFileTextField.getText();
    }

    public String getName() {
        return NbBundle.getMessage(NewFileVisualPanel1.class,
                "LBL_NewFilePanel1_Name");
    }

    public void insertUpdate(DocumentEvent event) {
        validateInput();
        updateOutputName();
    }

    public void removeUpdate(DocumentEvent event) {
        validateInput();
        updateOutputName();
    }

    /**
     * Update the generated file name shown in the text field.
     */
    private void updateOutputName() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                String name = folderTextField.getText() + File.separator +
                        fileNameTextField.getText() + ".gmx";
                createdFileTextField.setText(name);
            }
        });
    }

    /**
     * Validate the input provided by the user, display an error message
     * in the wizard panel, and fire a property change to all listeners.
     */
    private void validateInput() {
        boolean okay = false;
        String folder = folderTextField.getText();
        String file = fileNameTextField.getText();
        if (file.length() == 0) {
            messageLabel.setText(NbBundle.getMessage(NewFileVisualPanel1.class,
                    "LBL_NewFileVisualPanel1_InvalidFile"));
        } else if (folder.length() == 0 || !(new File(folder).exists())) {
            messageLabel.setText(NbBundle.getMessage(NewFileVisualPanel1.class,
                    "LBL_NewFileVisualPanel1_InvalidFolder"));
        } else if (new File(folder, file).exists()) {
            messageLabel.setText(NbBundle.getMessage(NewFileVisualPanel1.class,
                    "LBL_NewFileVisualPanel1_FileExists"));
        } else {
            messageLabel.setText("   ");
            okay = true;
        }
        firePropertyChange(PROP_VALID, !okay, okay);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();
        fileNameLabel = new javax.swing.JLabel();
        fileNameTextField = new javax.swing.JTextField();
        folderLabel = new javax.swing.JLabel();
        folderTextField = new javax.swing.JTextField();
        browseButton = new javax.swing.JButton();
        createdFileLabel = new javax.swing.JLabel();
        createdFileTextField = new javax.swing.JTextField();
        messageLabel = new javax.swing.JLabel();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/bluemarsh/graphmaker/ui/wizard/Form"); // NOI18N
        fileChooser.setDialogTitle(bundle.getString("LBL_NewFile_ChooserTitle")); // NOI18N
        fileChooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);

        fileNameLabel.setDisplayedMnemonic(java.util.ResourceBundle.getBundle("com/bluemarsh/graphmaker/ui/wizard/Form").getString("KEY_NewFile_FileName").charAt(0));
        fileNameLabel.setLabelFor(fileNameTextField);
        org.openide.awt.Mnemonics.setLocalizedText(fileNameLabel, bundle.getString("LBL_NewFile_FileName")); // NOI18N

        folderLabel.setDisplayedMnemonic(java.util.ResourceBundle.getBundle("com/bluemarsh/graphmaker/ui/wizard/Form").getString("KEY_NewFile_Folder").charAt(0));
        folderLabel.setLabelFor(folderTextField);
        org.openide.awt.Mnemonics.setLocalizedText(folderLabel, bundle.getString("LBL_NewFile_Folder")); // NOI18N

        browseButton.setMnemonic(java.util.ResourceBundle.getBundle("com/bluemarsh/graphmaker/ui/wizard/Form").getString("KEY_NewFile_Browse").charAt(0));
        org.openide.awt.Mnemonics.setLocalizedText(browseButton, bundle.getString("LBL_NewFile_Browse")); // NOI18N

        createdFileLabel.setDisplayedMnemonic(java.util.ResourceBundle.getBundle("com/bluemarsh/graphmaker/ui/wizard/Form").getString("KEY_NewFile_CreatedFile").charAt(0));
        createdFileLabel.setLabelFor(createdFileTextField);
        org.openide.awt.Mnemonics.setLocalizedText(createdFileLabel, bundle.getString("LBL_NewFile_CreatedFile")); // NOI18N

        createdFileTextField.setEditable(false);

        messageLabel.setForeground(new java.awt.Color(255, 51, 51));
        org.openide.awt.Mnemonics.setLocalizedText(messageLabel, "   ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(messageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fileNameLabel)
                            .addComponent(folderLabel)
                            .addComponent(createdFileLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(folderTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(browseButton))
                            .addComponent(createdFileTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                            .addComponent(fileNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fileNameLabel)
                    .addComponent(fileNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(folderLabel)
                    .addComponent(browseButton)
                    .addComponent(folderTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(createdFileLabel)
                    .addComponent(createdFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 186, Short.MAX_VALUE)
                .addComponent(messageLabel)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browseButton;
    private javax.swing.JLabel createdFileLabel;
    private javax.swing.JTextField createdFileTextField;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JLabel fileNameLabel;
    private javax.swing.JTextField fileNameTextField;
    private javax.swing.JLabel folderLabel;
    private javax.swing.JTextField folderTextField;
    private javax.swing.JLabel messageLabel;
    // End of variables declaration//GEN-END:variables
}
