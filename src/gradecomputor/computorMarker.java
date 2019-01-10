/*
 *   ----------------------
 *  |  computorMarker.java   
 *   ----------------------
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

/**
 * This Class contains the information about the Markers displayed on the plot.
 *
 * @author quarkCowboy
 * @version %I%, %G%
 * @since 0.10
 */
public class computorMarker {
    
   /**
    * The Marker name.
    */
    private String mName;

   /**
    * The Marker value.
    */
    private double mValue;
    
   /**
    * The Marker grade.
    */
    private String mgrade;

   /**
    * Number of students marked by this Marker.
    */
    private int studentCount;

    /**
     * Self explanatory.
     *
     * @param d Marker value.
     * @param mg Marker grade.
     * @param mN Marker name.
     *
     * @since version 0.10
     */
    public computorMarker(double d, String mg, String mN) {
        this.mName = mN;
        this.mgrade = mg;
        this.mValue = d;
    }

    /**
     * For appropriate increment.
     *
     * @see computorMainWindow#assignGradesToStudents() 
     * @since version 0.10
     */
    public void ascent() {
        studentCount++;
    }

    /**
     * Self explanatory.
     * 
     * @return String grade
     * @since version 0.10
     */
    public String getGrade() {
        return this.mgrade;
    }

    /**
     * Self explanatory.
     * 
     * @return String name
     * @since version 0.10
     */
    public String getName() {
        return this.mName;
    }
    
    /**
     * Self explanatory.
     * 
     * @return int Number of students.
     * @since version 0.10
     */
    public int getStudentCount() {
        return this.studentCount;
    }
    
    /**
     * Self explanatory.
     * 
     * @return double Value of the Marker.
     * @since version 0.10
     */
    public double getValue() {
        return this.mValue;
    }
    
    /**
     * Self explanatory.
     * 
     * @param s Grade string
     * @since version 0.10
     */
    public void setGrade(String s) {
        this.mgrade = s;
    }
    
    /**
     * Self explanatory.
     * 
     * @param s Marker value.
     * @since version 0.10
     */
    public void setValue(double s) {
        this.mValue = s;
    }
    
    /**
     * Self explanatory.
     * 
     * @param s Marker name.
     * @since version 0.10
     */
    public void setmName(String s) {
        this.mName = s;
    }

}
