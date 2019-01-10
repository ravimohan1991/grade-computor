/*
 *   -----------------------
 *  |  computorStudent.java   
 *   -----------------------
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
 * This Class is responsible for storing students' name, score and grade.
 *
 * @author quarkCowboy
 * @version %I%, %G%
 * @since 0.10
 */
public class computorStudent {
    // Hidden Information.  Only for CSV purpose
    /**
     * Student EID.
     */
    private String EID;

    /**
     * Student grade.
     */
    private String sGrade;

    /**
     * Student name.
     */
    private String sName;
    /**
     * Student score.
     */
    private double sScore;

    /**
     * Student section.
     */
    private String section;

    /**
     * Standard constructor.
     *
     * @param d   Double Score.
     * @param sg  String Grade.
     * @param sn  String Name.
     * @param id  String EID.
     * @param sec String Section.
     *
     * @since 0.10
     */
    public computorStudent(double d, String sg, String sn, String id, String sec) {
        this.sScore = d;
        this.sGrade = sg;
        this.sName = sn;
        this.EID = id;
        this.section = sec;
    }

    /**
     * Get EID
     *
     * @return string
     * @since 0.10
     */
    public String getEID() {
        return this.EID;
    }

    /**
     * Get Grade.
     *
     * @return string grade
     * @since 0.10
     */
    public String getGrade() {
        return this.sGrade;
    }

    /**
     * Get name.
     *
     * @return string name
     * @since 0.10
     */
    public String getName() {
        return this.sName;
    }
    /**
     * Get score.
     *
     * @return double score
     * @since 0.10
     */
    public double getScore() {
        return this.sScore;
    }

    /**
     * Get section.
     *
     * @return string section
     * @since 0.10
     */
    public String getSection() {
        return this.section;
    }
    
    /**
     * Set EID.
     *
     * @param s String
     *
     * @since 0.10
     */
    public void setEID(String s) {
        this.EID = s;
    }
    
    /**
     * Set Grade.
     *
     * @param s String
     *
     * @since 0.10
     */
    public void setGrade(String s) {
        this.sGrade = s;
    }
    
    /**
     * Set name.
     *
     * @param s name.
     *
     * @since 0.10
     */
    public void setName(String s) {
        this.sName = s;
    }
    
    /**
     * Set Score.
     *
     * @param s score
     *
     * @since 0.10
     */
    public void setScore(double s) {
        this.sScore = s;
    }

    /**
     * Set section.
     *
     * @param s section
     *
     * @since 0.10
     */
    public void setSection(String s) {
        this.section = s;
    }
}
