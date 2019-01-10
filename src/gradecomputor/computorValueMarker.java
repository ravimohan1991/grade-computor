/*
 *   ---------------------------
 *  |  computorValueMarker.java   
 *   ---------------------------
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

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Stroke;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.ui.LengthAdjustmentType;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.ui.TextAnchor;

/**
 * This Class is for the custom Marker with additional complexities.
 *
 * @author quarkCowboy
 * @version %I%, %G%
 * @since 0.10
 */
public class computorValueMarker extends ValueMarker {

    /**
     * The second label.
     */
    private String label2 = null;

    /**
     * The font for second label.
     */
    private Font label2Font;

    /**
     * The paint for second label.
     */
    private transient Paint label2Paint;

    /**
     * The background color for second label.
     */
    private Color label2BackgroundColor;

    /**
     * The label position for second label.
     */
    private RectangleAnchor label2Anchor;

    /**
     * The text anchor for the second label.
     */
    private TextAnchor label2TextAnchor;

    /**
     * The second label offset from the marker rectangle.
     */
    private RectangleInsets label2Offset;

    /**
     * The offset type for the domain or range axis (never {@code null}).
     *
     */
    private LengthAdjustmentType label2OffsetType;

    /**
     * The quantified measure of the difference generated by the Marker.
     */
    private String markCrossPercent = null;

    /**
     * The font for second label.
     */
    private Font markCrossPercentFont;

    /**
     * The paint for second label.
     */
    private transient Paint markCrossPercentPaint;

    /**
     * The background color for second label.
     */
    private Color markCrossPercentBackgroundColor;

    /**
     * The label position for second label.
     */
    private RectangleAnchor markCrossPercentAnchor;

    /**
     * The text anchor for the second label.
     */
    private TextAnchor markCrossPercentTextAnchor;

    /**
     * The second label offset from the marker rectangle.
     */
    private RectangleInsets markCrossPercentOffset;

    /**
     * The offset type for the domain or range axis (never {@code null}).
     *
     */
    private LengthAdjustmentType markCrossPercentOffsetType;

    /**
     * Standard constructor.
     *
     * @param value
     * @param paint
     * @param stroke
     *
     * @since 0.10
     */
    public computorValueMarker(double value, Paint paint, Stroke stroke) {
        super(value, paint, stroke);

        /**
         * Font is overwritten. See
         * {@link computorMarkerRenderer#computorMarkerRenderer(computorMainWindow sF, double w, int num)}
         */
        this.label2Font = new Font("SansSerif", Font.PLAIN, 9);
        this.label2Paint = Color.BLACK;
        this.label2BackgroundColor = new Color(100, 100, 100, 100);
        this.label2Anchor = RectangleAnchor.TOP_RIGHT;
        this.label2Offset = new RectangleInsets(3.0, 3.0, 3.0, 3.0);
        this.label2OffsetType = LengthAdjustmentType.CONTRACT;
        this.label2TextAnchor = TextAnchor.CENTER;

        this.markCrossPercentFont = new Font("SansSerif", Font.BOLD, 14);
        this.markCrossPercentPaint = Color.WHITE;
        this.markCrossPercentBackgroundColor = new Color(40, 218, 174);
        this.markCrossPercentAnchor = RectangleAnchor.TOP;
        this.markCrossPercentOffset = new RectangleInsets(3.0, 3.0, 3.0, 3.0);
        this.markCrossPercentOffsetType = LengthAdjustmentType.CONTRACT;
        this.markCrossPercentTextAnchor = TextAnchor.CENTER;

    }

    /**
     * Set the crossing percentage.
     *
     * @param st Value of crossing percentage.
     *
     * @since 0.10
     */
    public void setMarkCrossPercent(String st) {
        this.markCrossPercent = st;
    }

    /**
     * Set the label.
     *
     * @param st Label.
     *
     * @since 0.10
     */
    public void setLabel2(String st) {
        this.label2 = st;
    }

    /**
     * Set offset.
     *
     * @param rI Offset.
     *
     * @since 0.10
     */
    public void setLabel2Offset(RectangleInsets rI) {
        this.label2Offset = rI;
    }

    /**
     * Set offset.
     *
     * @param rI offset.
     *
     * @since 0.10
     */
    public void setMarkCrossPercentOffset(RectangleInsets rI) {
        this.markCrossPercentOffset = rI;
    }

    /**
     * Get label.
     *
     * @return string
     * @since 0.10
     */
    public String getLabel2() {
        return this.label2;
    }

    /**
     * Get cross percentage.
     *
     * @return string
     * @since 0.10
     */
    public String getMarkCrossPercent() {
        return this.markCrossPercent;
    }

    /**
     * Get label font.
     *
     * @return font.
     * @since 0.10
     */
    public Font getLabel2Font() {
        return this.label2Font;
    }

    /**
     * Get font.
     *
     * @return font.
     * @since 0.10
     */
    public Font getMarkCrossPercentFont() {
        return this.markCrossPercentFont;
    }

    /**
     * Get label anchor.
     *
     * @return RectangleAnchor
     * @since 0.10
     */
    public RectangleAnchor getLabel2Anchor() {
        return this.label2Anchor;
    }

    /**
     * Get another anchor
     *
     * @return RectangleAnchor
     * @since 0.10
     */
    public RectangleAnchor getMarkCrossPercentAnchor() {
        return this.markCrossPercentAnchor;
    }

    /**
     * Get yet another anchor.
     *
     * @return TextAnchor
     * @since 0.10
     */
    public TextAnchor getLabel2TextAnchor() {
        return this.label2TextAnchor;
    }

    /**
     * Get yet another anchor still.
     *
     * @return TextAnchor
     * @since 0.10
     */
    public TextAnchor getMarkCrossPercentTextAnchor() {
        return this.markCrossPercentTextAnchor;
    }

    /**
     * Get color.
     *
     * @return Color
     * @since 0.10
     */
    public Color getLabel2BackgroundColor() {
        return this.label2BackgroundColor;
    }

    /**
     * Get another color.
     *
     * @return color
     * @since 0.10
     */
    public Color getMarkCrossPercentBackgroundColor() {
        return this.markCrossPercentBackgroundColor;
    }

    /**
     * Get paint.
     *
     * @return Paint
     * @since 0.10
     */
    public Paint getLabel2Paint() {
        return this.label2Paint;
    }

    /**
     * Get another paint.
     *
     * @return paint.
     * @since 0.10
     */
    public Paint getMarkCrossPercentPaint() {
        return this.markCrossPercentPaint;
    }

    /**
     * Get offset.
     *
     * @return RectangleInsets
     * @since 0.10
     */
    public RectangleInsets getLabel2Offset() {
        return this.label2Offset;
    }

    /**
     * Get another offset.
     *
     * @return RectangleInsets
     * @since 0.10
     */
    public RectangleInsets getMarkCrossPercentOffset() {
        return this.markCrossPercentOffset;
    }

    /**
     * Set background color.
     *
     * @param c color.
     *
     * @since 0.10
     */
    public void setLabel2BackgroundColor(Color c) {
        this.label2BackgroundColor = c;
    }

    /**
     * Set another background color.
     *
     * @param c color
     *
     * @since 0.10
     */
    public void setMarkCrossPercentBackgroundColor(Color c) {
        this.markCrossPercentBackgroundColor = c;
    }

}
