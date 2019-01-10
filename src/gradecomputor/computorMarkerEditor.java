/*
 *   ----------------------------
 *  |  computorMarkerEditor.java   
 *   ---------------------------- 
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
 *   January, 2019: First Inscription. 
 */
package gradecomputor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.LCBLayout;
import org.jfree.chart.ui.PaintSample;

/**
 * A JPanel for specific Marker creation and configuration.
 *
 * @author quarkCowboy
 * @version %I%, %G%
 * @see computorMainWindow#computorMainWindow(java.lang.String,
 * java.lang.String)
 * @since 0.10
 */
class computorMarkerEditor extends JPanel implements ActionListener {

    /**
     * The resourceBundle for the localization.
     */
    protected static ResourceBundle localizationResources
            = ResourceBundle.getBundle("org.jfree.chart.editor.LocalizationBundle");

    /**
     * Whether or not Marker is activated.
     */
    private boolean bEnabled;

    /**
     * The JPanle for collective Markers.
     */
    private computorMarkerConfigPanel cMarkerConfigPanel;

    /**
     * The JPanel for specific Marker.
     */
    private JPanel general;

    /**
     * The Marker count of specific Marker.
     */
    private int markerCounter;

    /**
     * The Main Window reference.
     */
    private computorMainWindow sWindow;

    /**
     * The button to use to select a new paint (color) to draw the Marker.
     */
    private JButton selectPaintButton;

    /**
     * The button to set the name of the Marker.
     */
    private JButton setNameButton;

    /**
     * The checkbox to indicate whether or not to activate the Marker.
     */
    private JCheckBox showTitleCheckBox;

    /**
     * A field for displaying/editing the Marker text.
     */
    private JTextField titleField;

    /**
     * The paint (color) used to draw the Marker.
     */
    private PaintSample titlePaint;

    /**
     * Standard constructor: builds a panel for displaying/editing the
     * properties of the specified Marker.
     *
     * @param sW    The Main Window.
     * @param count The Marker count.
     * @param cMC   The JPanel for collective Marker representation.
     *
     * @since version 0.10
     */
    public computorMarkerEditor(computorMainWindow sW, int count, computorMarkerConfigPanel cMC) {

        // Standart datamember initialization
        this.sWindow = sW;
        this.cMarkerConfigPanel = cMC;
        this.markerCounter = count;

        TextTitle title;
        if (count < sWindow.getMarkerList().size()) {
            title = new TextTitle(sWindow.getMarkerList().get(count).getName());
        } else {
            title = new TextTitle("disabled");
        }

        TextTitle t = (title != null ? (TextTitle) title
                : new TextTitle(localizationResources.getString("Title")));

        this.bEnabled = !(t.getText().compareTo("disabled") == 0);
        this.titleField = new JTextField(t.getText());
        this.titlePaint = new PaintSample(((BarRenderer) sW.getCategoryPlot().getRenderer(0)).lookupSeriesPaint(count));

        setLayout(new BorderLayout());

        this.general = new JPanel(new BorderLayout());
        this.general.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(),
                        t.getText() + ":"
                )
        );

        JPanel interior = new JPanel(new LCBLayout(3));
        interior.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        interior.add(new JLabel("Enabled:"));
        this.showTitleCheckBox = new JCheckBox();
        this.showTitleCheckBox.setSelected(this.bEnabled);
        this.showTitleCheckBox.setActionCommand("EnableMarkerTitle");
        this.showTitleCheckBox.addActionListener(this);
        interior.add(new JPanel());
        interior.add(this.showTitleCheckBox);

        JLabel titleLabel = new JLabel("Name:");
        this.setNameButton = new JButton("Set");
        this.setNameButton.setActionCommand("SetName");
        this.setNameButton.addActionListener(this);
        interior.add(titleLabel);
        interior.add(this.titleField);
        interior.add(this.setNameButton);

        JLabel colorLabel = new JLabel("Color:");
        this.selectPaintButton = new JButton(
                localizationResources.getString("Select...")
        );
        this.selectPaintButton.setActionCommand("SelectPaint");
        this.selectPaintButton.addActionListener(this);
        interior.add(colorLabel);
        interior.add(this.titlePaint);
        interior.add(this.selectPaintButton);

        this.enableOrDisableControls();

        general.add(interior);
        add(general, BorderLayout.NORTH);
    }

    /**
     * Handles button clicks by passing control to an appropriate handler
     * method.
     *
     * @param event the event
     */
    @Override
    public void actionPerformed(ActionEvent event) {

        String command = event.getActionCommand();

        if (command.equals("SelectPaint")) {
            attemptPaintSelection();
        } else if (command.equals("EnableMarkerTitle")) {
            //  System.out.print("Listen checkbox \n");
            if (this.showTitleCheckBox.isSelected()) {
                attemptAddMarkerTitle();
            } else {
                attemptRemoveMarkerTitle();
            }
        } else if (command.equals("SetName")) {
            attemptModifyName();
        }
    }

    /**
     * Method to add Marker(s) in the buffer as per the requirement.
     *
     * @since version 0.10
     */
    private void attemptAddMarkerTitle() {

        this.sWindow.addNewMarker(this.markerCounter);

        this.cMarkerConfigPanel.refresh();
    }

    /**
     * Method to modify the Marker name.
     *
     * @since version 0.10
     */
    private void attemptModifyName() {
        sWindow.getMarkerList().get(this.markerCounter).setmName(this.titleField.getText());
        sWindow.masterUpdateFromBuffer();
    }

    /**
     * Allow the user the opportunity to select a Paint object. For now, we just
     * use the standard color chooser - all colors are Paint objects, but not
     * all Paint objects are colors (later we can implement a more general Paint
     * chooser).
     *
     * @since version 0.10
     */
    public void attemptPaintSelection() {
        Paint p = this.titlePaint.getPaint();
        Color defaultColor = (p instanceof Color ? (Color) p : Color.blue);
        Color c = JColorChooser.showDialog(
                this, localizationResources.getString("Title_Color"), defaultColor
        );
        if (c != null) {
            this.titlePaint.setPaint(c);
            ((BarRenderer) this.sWindow.getCategoryPlot().getRenderer(0)).setSeriesPaint(this.markerCounter, c, true);
        }
    }

    /**
     * Method to remove Marker(s) in the buffer as per the requirement.
     *
     * @since version 0.10
     */
    private void attemptRemoveMarkerTitle() {

        this.sWindow.removeMarker(this.markerCounter);

        this.cMarkerConfigPanel.refresh();
    }

    /**
     * If we are supposed to show the title, the controls are enabled.
     *
     * @since version 0.10
     */
    private void enableOrDisableControls() {
        boolean enabled = (this.bEnabled == true);
        this.titleField.setEnabled(enabled);
        this.selectPaintButton.setEnabled(enabled);
        this.setNameButton.setEnabled(enabled);
        this.showTitleCheckBox.setSelected(enabled);
        if (this.markerCounter >= sWindow.getMarkerList().size()) {
            this.titleField.setText("disabled");
            return;
        }
        if (enabled) {
            this.titleField.setText(sWindow.getMarkerList().get(this.markerCounter).getName());
        } else {
            this.titleField.setText("disabled");
        }
    }

    /**
     * Returns the paint selected in the panel.
     *
     * @return The paint selected in the panel.
     * @since version 0.10
     */
    public Paint getTitlePaint() {
        return this.titlePaint.getPaint();
    }

    /**
     * Returns the title (Marker) text entered in the panel.
     *
     * @return The title text entered in the panel.
     * @since version 0.10
     */
    public String getTitleText() {
        return this.titleField.getText();
    }

    /**
     * Reloads the Marker JPanel
     *
     * @since version 0.10
     */
    public void reload() {
        TextTitle title;
        if (this.markerCounter < sWindow.getMarkerList().size()) {
            title = new TextTitle(sWindow.getMarkerList().get(this.markerCounter).getName());
        } else {
            title = new TextTitle("disabled");
        }
        TextTitle t = (title != null ? (TextTitle) title
                : new TextTitle(localizationResources.getString("Title")));
        this.bEnabled = !(t.getText().compareTo("disabled") == 0);
        this.general.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(),
                        t.getText() + ":"
                )
        );
        this.titlePaint.setPaint(((BarRenderer) this.sWindow.getCategoryPlot().getRenderer(0)).lookupSeriesPaint(this.markerCounter));
        enableOrDisableControls();
    }
}
