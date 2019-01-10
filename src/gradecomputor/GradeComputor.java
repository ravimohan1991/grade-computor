/*
 *   ---------------------
 *  |  GradeComputor.java   
 *   ---------------------
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
 *
 * @author quarkCowboy
 * @since version 0.10 
 */
public class GradeComputor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.setProperty("apple.macos.useScreenJMenuBar", "true");
        System.setProperty("-Dsun.java2d.opengl", "True");

        computorMainWindow sW = new computorMainWindow("Grade Computor", "Student Lineup");
        sW.pack();
        // RefineryUtilities.centerFrameOnScreen(sW);
        sW.setVisible(true);
        sW.setResizable(true);
    }

}
