/*
 *   ----------------------------------
 *  |  computorItemLabelGenerator.java   
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

import org.jfree.chart.labels.StandardCategorySeriesLabelGenerator;
import org.jfree.data.category.CategoryDataset;

/**
 * This is a custom Class overwriting the JFreeChart's default series legend
 * labels with the appropriate student counts as series legend labels.
 *
 * @author quarkCowboy
 * @version %I%, %G%
 * @see computorMainWindow#computorMainWindow(java.lang.String,
 * java.lang.String)
 * @since 0.10
 */
public class computorItemLabelGenerator extends StandardCategorySeriesLabelGenerator {

    /**
     * Reference to the main object.
     */
    private computorMainWindow sWindow;

    /**
     * Self explanatory.
     *
     * @param cMW Main Window object.
     *
     * @since version 0.10
     */
    public computorItemLabelGenerator(computorMainWindow cMW) {
        super();
        this.sWindow = cMW;
    }

    /**
     * Overridden method to write the student count in the series legend.
     *
     * @param dataset The student data with assigned grades.
     * @param series  The default series generated.
     *
     * @see org.jfree.chart.renderer.category.BarRenderer#getLegendItem(int,
     * int)
     * @since version 0.10
     */
    @Override
    public String generateLabel(CategoryDataset dataset, int series) {

        String result = "";

        if (this.sWindow.getMarkerList().size() >= series + 1) {
            result = Integer.toString(this.sWindow.getMarkerList().get(series).getStudentCount());
        }
        return result;
    }
}
