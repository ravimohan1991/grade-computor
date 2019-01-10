/*
 *   ----------------------------
 *  |  computorCategoryAxis.java   
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

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.axis.AxisState;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.PlotRenderingInfo;

/**
 * This is a custom Class for suppressing the unnecessary labels shown on the
 * x-axis of the plot.
 *
 * @author quarkCowboy
 * @version %I%, %G%
 * @see computorMainWindow#computorMainWindow(java.lang.String,
 * java.lang.String)
 * @since 0.10
 */
public class computorCategoryAxis extends CategoryAxis {

    /**
     * Self explanatory.
     *
     * @param label String for setting the DomainAxis title.
     *
     * @since version 0.10
     */
    public computorCategoryAxis(String label) {
        super(label);
    }

    /**
     * This method overrides the original method to prevent the printing of
     * labels (Student Names).
     *
     * @param g2        The graphics device ({@code null} not permitted).
     * @param plotArea  The area within which the axis should be drawn
     *                  ({@code null} not permitted).
     * @param dataArea  The area within which the plot is being drawn
     *                  ({@code null} not permitted).
     * @param edge      The location of the axis ({@code null} not permitted).
     * @param state     state information for an axis during the drawing
     *                  process.
     * @param plotState Collects information about the plot ({@code null}
     *                  permitted).
     *
     *
     * @return Returns the original AxisState without writing names.
     * @see
     * org.jfree.chart.axis.CategoryAxis#drawCategoryLabels(java.awt.Graphics2D,
     * java.awt.geom.Rectangle2D, java.awt.geom.Rectangle2D,
     * org.jfree.chart.ui.RectangleEdge, org.jfree.chart.axis.AxisState,
     * org.jfree.chart.plot.PlotRenderingInfo)
     * @see org.jfree.chart.plot.CategoryPlot#drawAxes(java.awt.Graphics2D,
     * java.awt.geom.Rectangle2D, java.awt.geom.Rectangle2D,
     * org.jfree.chart.plot.PlotRenderingInfo)
     * @since version 0.10
     */
    @Override
    protected AxisState drawCategoryLabels(Graphics2D g2, Rectangle2D plotArea,
            Rectangle2D dataArea, org.jfree.chart.ui.RectangleEdge edge,
            AxisState state, PlotRenderingInfo plotState) {
        //Don't show the labels
        return state;
    }

}
