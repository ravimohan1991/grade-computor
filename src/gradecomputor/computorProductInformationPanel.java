/*
 *   ----------------------------------
 *  |  computorProductInformation.java   
 *   ---------------------------------- 
 *   This file is part of Grade Computor.
 *
 *   Grade Computor is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Grade Computor is distributed in the hope and belief that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Grade Computor.  If not, see <https://www.gnu.org/licenses/>.
 *
 *   Timeline:
 *   January, 2019: First rip from org.netbeans.core.ui.ProductInformationPanel.java
 */
package gradecomputor;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

/**
 * A JPanel for describing the purpose of the application and current state of the
 * system it is running on.  Heavily relies on the use of 3rd party java libraries
 * <ul>
 * <li>  <a href="https://github.com/oshi/oshi/">oshi</a>   (https://github.com/oshi/oshi)
 * <li> <a href="https://github.com/java-native-access/jna/"> JNA </a>  (https://github.com/java-native-access/jna)
 * <li> <a href="https://www.slf4j.org">SLF4J</a>
 * </ul>
 * for obtaining the required information.
 * @author quarkCowboy
 * @version %I%, %G%
 * @since 0.10
 */
public class computorProductInformationPanel extends JPanel implements HyperlinkListener {

    private URL url = null;
    private Icon about;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane copyright;
    private javax.swing.JTextPane description;
    private javax.swing.JLabel imageLabel;
    private javax.swing.JPanel imagePanel;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    // End of variables declaration//GEN-END:variables

    /**
     * Standard constructor to display license and the source wall!
     */
    public computorProductInformationPanel() {

        SystemInfo si = new SystemInfo();

        HardwareAbstractionLayer hal = si.getHardware();
        OperatingSystem os = si.getOperatingSystem();

        try {
            initComponents();
        } catch (IOException ex) {
            Logger.getLogger(computorProductInformationPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        imageLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        imageLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                showUrl("https://dcau.fandom.com/wiki/Source_Wall");
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        description.setText("<b>Grade Computor: </b>" + " Version 1.0." + "<br>" + "<b>Operating System:</b> " + os
                + ".<br>" + "<b>Current Directory:</b> " + getUserDirValue() + ".<br>" + "<b>Java Runtime Environment: </b>"
                + getJavaRuntime() + ".<br>" + "<b>Java Virtual Machine: </b>" + getVMValue() + ".<br>" + "<b>Processor: </b>"
                + hal.getProcessor() + ", Available Processors: " + Runtime.getRuntime().availableProcessors() + ".");

        description.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        description.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        description.setCaretPosition(0); // so that text is not scrolled down
        // description.addHyperlinkListener(this);
        //copyright.addHyperlinkListener(this);
        copyright.setBackground(getBackground());
        copyright.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);

        InputStream is = this.getClass().getResourceAsStream("/resources/about/image/The_Source_Wall.jpg");
        try {
            about = new ImageIcon(ImageIO.read(is));
        } catch (IOException ex) {
            Logger.getLogger(computorMainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }

        imageLabel.setIcon(about);

    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() throws IOException {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        copyright = new javax.swing.JTextPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        description = new javax.swing.JTextPane();
        imagePanel = new javax.swing.JPanel();
        imageLabel = new javax.swing.JLabel();

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jButton2.setMnemonic(0);
        jButton2.setText("Roger"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jButton2, gridBagConstraints);

        jScrollPane3.setBorder(null);

        copyright.setBorder(null);
        copyright.setContentType("text/html");
        copyright.setEditable(false);
        copyright.setText(getCopyrightText());
        copyright.setCaretPosition(0); // so that text is not scrolled down
        copyright.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
              //  copyrightMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(copyright);

        description.setContentType("text/html");
        description.setEditable(false);
        description.setText("<div style=\"font-size: 12pt; font-family: Verdana, 'Verdana CE',  Arial, 'Arial CE', 'Lucida Grande CE', lucida, 'Helvetica CE', sans-serif;\">\n    <b>Product Version:</b> {0}<br> <b>Java:</b> {1}; {2}<br> <b>System:</b> {3}; {4}; {5}<br><b>Userdir:</b> {6}</div>");
        jScrollPane2.setViewportView(description);

        imagePanel.setLayout(new java.awt.BorderLayout());

        imageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imagePanel.add(imageLabel, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(imagePanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(imagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                .addGap(14, 14, 14)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    closeDialog();
}//GEN-LAST:event_jButton2ActionPerformed

    private void closeDialog() {
        Window w = SwingUtilities.getWindowAncestor(this);
        w.setVisible(false);
        w.dispose();
    }

    private void showUrl(String urlString) {
        try {
            Desktop.getDesktop().browse(new URL(urlString).toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getJavaValue() {
        return System.getProperty("java.version", "unknown");
    }

    public static String getVMValue() {
        return System.getProperty("java.vm.name", "unknown") + " " + System.getProperty("java.vm.version", "");
    }

    public static String getJavaRuntime() {
        return System.getProperty("java.runtime.name", "unknown") + " " + System.getProperty("java.runtime.version", "");
    }

    private String getUserDirValue() {
        return System.getProperty("user.dir");
    }

    public static String getEncodingValue() {
        return System.getProperty("file.encoding", "unknown");
    }

    public void hyperlinkUpdate(HyperlinkEvent event) {
        if (HyperlinkEvent.EventType.ENTERED == event.getEventType()) {
            url = event.getURL();
        } else if (HyperlinkEvent.EventType.EXITED == event.getEventType()) {
            url = null;
        }
    }

    private String getCopyrightText() throws IOException {

        String copyrighttext = "Nothing is impossible once you have the Source Code (and know "
                + "how to use it!)." + "<P ALIGN=RIGHT>-The_Cowboy </P><br>"; // NOI18
        copyrighttext += "This software is released with the copyleft license and all ''available'' rights are reversed by The_Cowboy!.<br>";
        copyrighttext += "For more information about the license, scroll down.<br><br>";
        InputStream fo = this.getClass().getResourceAsStream("/resources/about/license/license.txt");
        copyrighttext += loadLicenseText(fo);

        return copyrighttext;
    }

    /**
     * Tries to load text stored in given file object.
     *
     * @param fo Filestream to retrieve text from
     *
     * @return String containing text from the file, or null if file can't be
     *         found or some kind of I/O error appeared.
     * @throws java.io.FileNotFoundException
     */
    private static String loadLicenseText(InputStream fo) throws FileNotFoundException {

        BufferedReader in = new BufferedReader(new InputStreamReader(fo));
        StringWriter result = new StringWriter();
        int curChar;
        try {
            // reading content of license file
            while ((curChar = in.read()) != -1) {
                result.write(curChar);
            }
        } catch (IOException ex) {
            // don't return anything if any problem during read
            return null;
        }

        return result.toString();
    }
}
