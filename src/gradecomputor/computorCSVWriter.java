/*
 *   -------------------------
 *  |  computorCSVWriter.java   
 *   ------------------------- 
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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This Class responsible for writing the CSV (Comma-Separated Values) file as
 * per the University's specified
 * <a href = https://utdirect.utexas.edu/registrar/egrades/upload_info.html>instructions</a>
 * for Registrar office upload. This Class is also used while saving the Marker
 * information.
 *
 * @author quarkCowboy
 * @version %I%, %G%
 * @since 0.10
 */
public class computorCSVWriter {

    /**
     * The CSV file to be saved.
     */
    private File file;

    /**
     * Self explanatory.
     *
     * @param f File to be read
     *
     * @since version 0.10
     */
    public computorCSVWriter(File f) {
        this.file = f;
    }

    /**
     * For writing the Marker information.
     *
     * @param c Marker buffer to be memorized.
     *
     * @see computorMainWindow#saveMarkers(java.io.File)
     * @throws java.io.IOException
     * @since version 0.10
     */
    public void writeCSVFile(ArrayList<computorMarker> c) throws IOException {

        FileWriter fwriter = new FileWriter(file);

        for (int i = 0; i < c.size(); i++) {
            fwriter.write(Double.toString(c.get(i).getValue()) + "," + c.get(i).getGrade() + "," + c.get(i).getName() + "\n");
        }
        fwriter.flush();
        fwriter.close();

    }

    /**
     * For writing the Student information.
     *
     * @param c Marker buffer to be memorized.
     *
     * @see computorMainWindow#prepareRegistrarFile(java.io.File)
     * @throws java.io.IOException
     * @since version 0.10
     */
    public void writerCSVFile(ArrayList<computorStudent> c) throws IOException {
        FileWriter fwriter = new FileWriter(file);

        fwriter.write("Name" + "\t" + "EID" + "\t" + "Grade" + "\t" + "Absences" + "\t" + "Remarks" + "\t" + "Unique" + "\n");
        for (int i = 0; i < c.size(); i++) {
            fwriter.write(c.get(i).getName() + "\t" + c.get(i).getEID() + "\t" + c.get(i).getGrade() + "\t" + " " + "\t" + " " + "\t" + c.get(i).getSection() + "\n");
            // System.out.print("Writing...\n");
        }
        fwriter.flush();
        fwriter.close();
    }

}
