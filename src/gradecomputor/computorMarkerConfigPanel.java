/*
 *   ---------------------------------
 *  |  computorMarkerConfigPanel.java   
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
 *   January, 2019: First Inscription. 
 */
package gradecomputor;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import org.jfree.chart.renderer.category.BarRenderer;

/**
 * This Class is displays the collective information about the Markers (in
 * buffer) and their configurations.
 *
 * @author quarkCowboy
 * @version %I%, %G%
 * @since 0.10
 */
public class computorMarkerConfigPanel extends JPanel implements ActionListener {

    /**
     * The collection of Marker configuration.
     */
    private computorMarkerEditor interior[] = new computorMarkerEditor[12];

    /**
     * The JPanel for Marker configuration.
     */
    private JPanel markerStore;

    /**
     * The JButton for refreshing the JPanel.
     */
    private JButton refresh;

    /**
     * The JPanel for refresh JButton.
     */
    private JPanel resetCol;

    /**
     * The Main Window reference.
     */
    private computorMainWindow sWindow;

    /**
     * Generates the structured JPanel for Marker configuration purpose.
     *
     * @param cM The Main Window reference.
     *
     * @since version 0.10
     */
    public computorMarkerConfigPanel(computorMainWindow cM) {

        this.sWindow = cM;
        this.setLayout(new BorderLayout());

        markerStore = new JPanel(new GridLayout(4, 3));

        int i;
        for (i = 0; i < 12; i++) {
            this.interior[i] = new computorMarkerEditor(cM, i, this);
            markerStore.add(this.interior[i]);
        }
        markerStore.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
                "Marker List:"));
        add(markerStore, BorderLayout.CENTER);

        this.resetCol = new JPanel(new BorderLayout());
        this.refresh = new JButton("Reset Colors");
        this.refresh.setActionCommand("ResetColors");
        this.refresh.addActionListener(this);

        this.resetCol.add(refresh);
        add(this.resetCol, BorderLayout.PAGE_END);
    }

    /**
     * The method listens to the Action generated by the refresh JButton and
     * acts accordingly.
     *
     * @param event The event containing the refresh command "ResetColors"
     *
     * @since version 0.10
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();

        if (command.equals("ResetColors")) {
            attemptResetPaint();
        }
    }

    /**
     * The method resets the series paint in the category plot. This results in
     * the appropriate clustering of the students with similar grades.
     *
     * @since version 0.10
     */
    private void attemptResetPaint() {
        for (int i = 0; i < 12; i++) {
            ((BarRenderer) this.sWindow.getCategoryPlot().getRenderer()).setSeriesPaint(i, null, false);//Remove color
            ((BarRenderer) this.sWindow.getCategoryPlot().getRenderer()).lookupSeriesPaint(i);//Generate appropriate color
        }
        this.sWindow.masterUpdateFromBuffer();
        refresh();
    }

    /**
     * The method resets the JPanel representing the Marker configuration.
     *
     * @since version 0.10
     */
    protected void refresh() {
        for (int i = 0; i < this.markerStore.getComponentCount(); i++) {
            this.interior[i].reload();
        }
    }
}
