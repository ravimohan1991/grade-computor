/*
 *   ------------------------------
 *  |  computorMarkerRenderer.java   
 *   ------------------------------ 
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

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.chart.renderer.category.AbstractCategoryItemRenderer;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.chart.text.TextUtils;
import org.jfree.chart.ui.LengthAdjustmentType;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.chart.util.Args;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;

/**
 * A custom renderer for generating dynamical Markers in the plot with high
 * sensitivity to the user interaction. This is achieved by associating the
 * ChartEntity with the Marker and linking the mouse listener with the
 * CategoryPlot.<br>
 *
 * The default Java routine {@link java.awt.Shape#getBounds2D()} is implemented
 * to generate high precision bounding box of the Marker and avoid "collisions"
 * with other Markers with different value or grade.<br>
 *
 * After performing several tests, the Marker width is set to <code>6.0f</code>
 * ({@link computorMainWindow#computorMainWindow(java.lang.String, java.lang.String)})
 * .<p>
 *
 * More labels representing the relevant information are added
 * <ul>
 * <li> a label representing the current value of the Marker on the plot.
 * <li> a label representing the percent increase in the scores categorised by
 * the Marker.
 * </ul>
 * <br>
 * <IMG SRC=".././doc-files/marker.png" alt="Marker" >
 *
 * @author quarkCowboy
 * @version %I%, %G%
 * @see org.jfree.chart.entity.CategoryItemEntity
 * @since 0.10
 */
public class computorMarkerRenderer extends AbstractCategoryItemRenderer
        implements Cloneable, PublicCloneable, Serializable {

    /**
     * The special font for Marker label.
     */
    private static Font customFont;

    /**
     * The width of the bounding box of Marker.
     */
    private double markerWidth;

    /**
     * The number of Markers on the plot.
     */
    private int numOfMarkers;

    /**
     * The Main Window reference.
     */
    public computorMainWindow sWindow;

    /**
     * The standard constructor method.
     *
     * @param sF  The Main Window.
     * @param w   Width of the Marker bounding box.
     * @param num number of Markers
     *
     * @since 0.10
     */
    public computorMarkerRenderer(computorMainWindow sF, double w, int num) {

        this.sWindow = sF;
        this.markerWidth = w;
        this.numOfMarkers = num;

        //For loading the special font
        InputStream is = this.getClass().getResourceAsStream("/resources/fonts/cac_champagne.ttf");

        try {
            this.customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(17f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to associate ChartEntity
     * ({@link org.jfree.chart.entity.CategoryItemEntity} with the Marker.)
     *
     * @param entities The CategoryPlot EntityCollection
     * @param dataset  The Marker dataset
     * @param row      Row in Marker dataset
     * @param column   Column in Marker dataset
     * @param hotspot  Bounding Shape computed by Java
     *
     * @since 0.10
     */
    @Override
    protected void addItemEntity(EntityCollection entities,
            CategoryDataset dataset, int row, int column, Shape hotspot) {
        Args.nullNotPermitted(hotspot, "hotspot");
        // if (!getItemCreateEntity(row, column)) {
        //     return;
        // }
        String tip = null;
        CategoryToolTipGenerator tipster = getToolTipGenerator(row, column);
        if (tipster != null) {
            tip = tipster.generateToolTip(dataset, row, column);
        }
        String url = null;
        CategoryURLGenerator urlster = getItemURLGenerator(row, column);
        if (urlster != null) {
            url = urlster.generateURL(dataset, row, column);
        }
        CategoryItemEntity entity = new CategoryItemEntity(hotspot, tip, url,
                dataset, dataset.getRowKey(row), dataset.getColumnKey(column));
        entities.add(entity);
    }

    /**
     * Method to render the dynamical Marker with appropriate settings. No need
     * to trim the rectangle area or anything. This method gets called by the
     * CategoryPlot ({@link org.jfree.chart.plot.CategoryPlot#render(java.awt.Graphics2D, java.awt.geom.Rectangle2D, int, org.jfree.chart.plot.PlotRenderingInfo, org.jfree.chart.plot.CategoryCrosshairState)
     * }) with all the adjustments already done.
     *
     * @param gd   the graphics device.
     * @param cirs state information for one chart.
     * @param rd   the data plot area.
     * @param cp   the plot.
     * @param ca   the domain axis.
     * @param va   the range axis.
     * @param cd   the Marker data.
     * @param i    the row index (zero-based).
     * @param i1   the column index (zero-based).
     * @param i2   the pass index.
     *
     * @see org.jfree.chart.plot.CategoryPlot#draw(java.awt.Graphics2D,
     * java.awt.geom.Rectangle2D, java.awt.geom.Point2D,
     * org.jfree.chart.plot.PlotState, org.jfree.chart.plot.PlotRenderingInfo)
     * @since 0.10
     */
    @Override
    public void drawItem(Graphics2D gd, CategoryItemRendererState cirs,
            Rectangle2D rd, CategoryPlot cp, CategoryAxis ca, ValueAxis va, CategoryDataset cd, int i, int i1, int i2) {

        Stroke str;
        this.numOfMarkers = sWindow.computeNumOfMarkers();
        computorValueMarker[] vm = new computorValueMarker[this.numOfMarkers];
        Rectangle2D[] r2D = new Rectangle2D[this.numOfMarkers];
        RectangleInsets loffset = new RectangleInsets(3.0, 19.0, 3.0, 3.0);// So that we may not smash into The Wall (range axis).
        RectangleInsets roffset = new RectangleInsets(3.0, 3.0, 3.0, 19.0);
        RectangleInsets markoffset = new RectangleInsets(3.0, 3.0, 3.0, 3.0);

        int store = -1;

        for (int mCount = 0; mCount < this.numOfMarkers; mCount++) {
            double value = (double) cd.getValue(mCount, mCount);

            if (mCount == sWindow.getMarkerNumber()) {
                store = sWindow.getMarkerNumber(); //Store for rendering in the end.
                str = new BasicStroke(4.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);// Set the Marker as Bold
                vm[mCount] = new computorValueMarker(value, cp.getRenderer(0).getItemPaint(mCount, mCount), str);
            } else {
                str = AbstractRenderer.DEFAULT_STROKE;
                vm[mCount] = new computorValueMarker(value, cp.getRenderer(0).getItemPaint(mCount, mCount), str);
            }

            vm[mCount].setLabel("  " + cd.getColumnKey(mCount) + " ");//Experimental
            vm[mCount].setLabelFont(customFont);//Hook for setting label Font
            vm[mCount].setLabel2(" " + (Double.toString(Math.round((value) * 100D) / 100D)) + " ");
            vm[mCount].setMarkCrossPercent(" " + Double.toString(sWindow.getMarkerCrossPercentage(mCount))
                    + (Double.isNaN(sWindow.getMarkerCrossPercentage(mCount)) ? "" : "%") + " ");
            vm[mCount].setLabelOffset(loffset);
            vm[mCount].setLabel2Offset(roffset);
            vm[mCount].setMarkCrossPercentOffset(roffset);
            vm[mCount].setLabelBackgroundColor(Color.WHITE);
            vm[mCount].setLabel2BackgroundColor(Color.WHITE);

            if (mCount != store) {
                r2D[mCount] = drawRMarker(gd, cp, va, vm[mCount], rd);
            }
        }

        if (store > -1) {
            r2D[store] = drawRMarker(gd, cp, va, vm[store], rd);
        }

        // Add item entity to the Marker (for tracking purpose)
        EntityCollection entities = cirs.getEntityCollection();
        if (entities != null) {
            int rowCount = cd.getRowCount();
            int columnCount = cd.getColumnCount();

            for (int row = 0; row < rowCount; row++) {
                for (int column = 0; column < columnCount; column++) {
                    if (cd.getValue(row, column) != null && r2D[row] != null) {
                        addItemEntity(entities, cd, row, column, r2D[row]);
                    }
                }
            }
        }

    }

    /**
     * Method to draw the Marker and estimate the high precision bound box.
     * Ripped and modified from
     * {@link org.jfree.chart.renderer.category.AbstractCategoryItemRenderer#drawRangeMarker(java.awt.Graphics2D, org.jfree.chart.plot.CategoryPlot, org.jfree.chart.axis.ValueAxis, org.jfree.chart.plot.Marker, java.awt.geom.Rectangle2D)}
     *
     * @param g2       the graphics device (not {@code null}).
     * @param plot     the plot (not {@code null}).
     * @param axis     the range axis (not {@code null}).
     * @param marker   the marker to be drawn (not {@code null}).
     * @param dataArea the area inside the axes (not {@code null}).
     *
     * @return Rectangle2D The bounding Shape
     * @since 0.10
     */
    private Rectangle2D drawRMarker(Graphics2D g2, CategoryPlot plot,
            ValueAxis axis, Marker marker, Rectangle2D dataArea) {

        Rectangle2D rMarker = new Rectangle2D.Double();

        if (marker instanceof ValueMarker) {
            computorValueMarker vm = (computorValueMarker) marker;
            double value = vm.getValue();
            Range range = axis.getRange();

            if (!range.contains(value)) {
                return null;
            }

            final Composite savedComposite = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, marker.getAlpha()));

            PlotOrientation orientation = plot.getOrientation();
            double v = axis.valueToJava2D(value, dataArea,
                    plot.getRangeAxisEdge());
            Line2D line = null;
            if (orientation == PlotOrientation.HORIZONTAL) {
                line = new Line2D.Double(v, dataArea.getMinY(), v,
                        dataArea.getMaxY());
            } else if (orientation == PlotOrientation.VERTICAL) {
                line = new Line2D.Double(dataArea.getMinX(), v,
                        dataArea.getMaxX(), v);
            } else {
                throw new IllegalStateException();
            }

            g2.setPaint(marker.getPaint());
            g2.setStroke(marker.getStroke());
            g2.draw(line);

            Line2D temp = new Line2D.Double(line.getX1(), line.getY1() - markerWidth / 2, line.getX2(), line.getY2() + markerWidth / 2);
            rMarker = temp.getBounds2D();

            String label = marker.getLabel();
            RectangleAnchor anchor = marker.getLabelAnchor();
            if (label != null) {
                Font labelFont = marker.getLabelFont();
                g2.setFont(labelFont);
                Point2D coordinates = calculateRangeMarkerTextAnchorPoint(
                        g2, orientation, dataArea, line.getBounds2D(),
                        marker.getLabelOffset(), LengthAdjustmentType.EXPAND,
                        anchor);
                Rectangle2D rect = TextUtils.calcAlignedStringBounds(label, g2,
                        (float) coordinates.getX(), (float) coordinates.getY(),
                        marker.getLabelTextAnchor());
                g2.setPaint(marker.getLabelBackgroundColor());
                g2.fill(rect);
                g2.setPaint(marker.getLabelPaint());
                TextUtils.drawAlignedString(label, g2,
                        (float) coordinates.getX(), (float) coordinates.getY(),
                        marker.getLabelTextAnchor());
            }
            //g2.setComposite(savedComposite);

            String label2 = ((computorValueMarker) marker).getLabel2();
            RectangleAnchor anchor2 = ((computorValueMarker) marker).getLabel2Anchor();
            if (label2 != null) {
                Font label2Font = ((computorValueMarker) marker).getLabel2Font();
                g2.setFont(label2Font);
                Point2D coordinates = calculateRangeMarkerTextAnchorPoint(
                        g2, orientation, dataArea, line.getBounds2D(),
                        ((computorValueMarker) marker).getLabel2Offset(), LengthAdjustmentType.EXPAND,
                        anchor2);
                Rectangle2D rect = TextUtils.calcAlignedStringBounds(label2, g2,
                        (float) coordinates.getX(), (float) coordinates.getY(),
                        ((computorValueMarker) marker).getLabel2TextAnchor());
                g2.setPaint(((computorValueMarker) marker).getLabel2BackgroundColor());
                g2.fill(rect);
                g2.setPaint(((computorValueMarker) marker).getLabel2Paint());
                TextUtils.drawAlignedString(label2, g2,
                        (float) coordinates.getX(), (float) coordinates.getY(),
                        ((computorValueMarker) marker).getLabel2TextAnchor());
            }

            String markcross = ((computorValueMarker) marker).getMarkCrossPercent();
            RectangleAnchor markcrossanchor = ((computorValueMarker) marker).getMarkCrossPercentAnchor();
            if (markcross != null) {
                Font markcrossFont = ((computorValueMarker) marker).getMarkCrossPercentFont();
                g2.setFont(markcrossFont);
                Point2D coordinates = calculateRangeMarkerTextAnchorPoint(
                        g2, orientation, dataArea, line.getBounds2D(),
                        ((computorValueMarker) marker).getMarkCrossPercentOffset(), LengthAdjustmentType.EXPAND,
                        markcrossanchor);
                Rectangle2D rect = TextUtils.calcAlignedStringBounds(markcross, g2,
                        (float) coordinates.getX(), (float) coordinates.getY(),
                        ((computorValueMarker) marker).getMarkCrossPercentTextAnchor());
                g2.setPaint(((computorValueMarker) marker).getMarkCrossPercentBackgroundColor());
                g2.fill(rect);
                g2.setPaint(((computorValueMarker) marker).getMarkCrossPercentPaint());
                TextUtils.drawAlignedString(markcross, g2,
                        (float) coordinates.getX(), (float) coordinates.getY(),
                        ((computorValueMarker) marker).getMarkCrossPercentTextAnchor());
            }
            g2.setComposite(savedComposite);
        }

        return rMarker;
    }

    /**
     * Method to return null LegendItem corresponding to Markers.
     *
     * @return null
     */
    @Override
    public LegendItemCollection getLegendItems() {
        return new LegendItemCollection();
    }

    /**
     * Method to return the width of the Marker bounding box.
     *
     * @return double Marker width
     */
    public double getMarkerWidth() {
        return markerWidth;
    }

    /**
     * Method to set Marker width of bounding box.
     *
     * @param mW Width.
     */
    public void setMarkerWidth(double mW) {
        markerWidth = mW;
    }

}
