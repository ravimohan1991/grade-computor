/*
 *   ------------------------- 
 *  |  computorCSVReader.java   
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This Class responsible for reading the CSV (Comma-Separated Values) file and
 * obtain the relevant information for the display and grade judgment.
 *
 * @author quarkCowboy
 * @version %I%, %G%
 * @since 0.10
 */
public class computorCSVReader {

    /**
     * The CSV information protocol.
     */
    private ArrayList<computorString> csvInfo = new ArrayList<computorString>();
    /**
     * The field delimiter.
     */
    private final char fieldDelimiter = '"';

    /**
     * The File to be read.
     */
    private File file;

    /**
     * The Files to be read.
     */
    private File[] files;

    /**
     * The student buffer information.
     */
    private ArrayList<computorStudent> list = new ArrayList<computorStudent>();

    /**
     * The text delimiter.
     */
    private final char textDelimiter = ',';

    /**
     * The overloaded method sets the file to be read and protocol to be
     * followed while reading information from CSV file.
     *
     * @param f File to be read
     *
     * @see computorMainWindow#createMenuBar()
     * @since version 0.10
     */
    public computorCSVReader(File f) {

        file = f;
        setCSVInfo();
    }

    /**
     * The overloaded method sets the file(s) to be read and protocol to be
     * followed while reading information from CSV file.
     *
     * @param f File to be read
     *
     * @see computorMainWindow#createMenuBar()
     * @since version 0.10
     */
    public computorCSVReader(File[] f) {

        this.files = f;
        setCSVInfo();
    }

    /**
     * The method computes the place of the string (fields), listed in the CSV
     * info (from the source), as they appear in the CSV file line and generates
     * the protocol.
     *
     * @param s Line from which the place of the string (fields) is to be
     *          computed. Typically the first line of CSV file.
     *
     * @see #setCSVInfo()
     * @since version 0.10
     */
    private void computeStringPlace(String s) {

        char[] lineChar = s.toCharArray();
        int placeCounter = 0;
        int csvInfoCounter = 0;
        String word = "";

        for (int i = 0; i <= lineChar.length; i++) {
            if (i != lineChar.length && Character.compare(lineChar[i], textDelimiter) != 0) {
                word = word + lineChar[i];
                continue;
            }
            if (this.csvInfo.get(csvInfoCounter).getText().compareTo(word) == 0) {
                csvInfo.get(csvInfoCounter++).setPlace(placeCounter++);
                word = "";
            } else {
                placeCounter++;
                word = "";
            }
        }
    }

    /**
     * The method reads the saved Marker(s) information stored in the CSV and
     * generates the buffer.
     *
     * @return cm The Marker Buffer.
     * @see computorMainWindow#createMenuBar()
     * @throws java.io.IOException
     * @since version 0.10
     */
    public ArrayList<computorMarker> fetchMarkerList() throws IOException {

        ArrayList<computorMarker> cm = new ArrayList<computorMarker>();

        BufferedReader reader = new BufferedReader(new FileReader(this.file));
        String line = reader.readLine();

        while (line != null) {
            cm.add(new computorMarker(Double.valueOf(readWordAtPosition(line, 0)),
                    readWordAtPosition(line, 1), readWordAtPosition(line, 2)));
            line = reader.readLine();
        }

        return cm;
    }

    /**
     * The method reads the student information stored in the CSV (typically
     * exported from Canvas) and generates the buffer. The information is read
     * as per the generated protocol from csvInfo.
     *
     * @return cm The student Buffer.
     * @see computorMainWindow#createMenuBar()
     * @see #setCSVInfo()
     * @throws java.io.IOException
     * @since version 0.10
     */
    public ArrayList<computorStudent> fetchStudentList() throws IOException {

        ArrayList<computorStudent> cm = new ArrayList<computorStudent>();

        for (int i = 0; i < this.files.length; i++) {
            BufferedReader reader = new BufferedReader(new FileReader(this.files[i]));

            // UT Austin CSV specific hack
            String line = reader.readLine();
            if (i == 0) {
                computeStringPlace(line);
            }
            reader.readLine();// Ignore the second line in CSV
            line = reader.readLine();

            while (line != null) {
                cm.add(new computorStudent(Double.valueOf(readWordAtPosition(line,
                        this.csvInfo.get(3).getPlace())), " ", readWordAtPosition(line,
                        this.csvInfo.get(0).getPlace()), readWordAtPosition(line, this.csvInfo.get(1).getPlace()), parseSection(readWordAtPosition(line, this.csvInfo.get(2).getPlace()))));
                line = reader.readLine();
            }
        }

        return cm;
    }

    /**
     * A hack to obtain the section unique from the CSV file. The field is
     * usually written in the form "PHY102M(55345)" and with the extraction it
     * results in "55345".
     *
     * @param s String to be parsed.
     *
     * @return unique The unique string of the section.
     * @since version 0.10
     */
    private String parseSection(String s) {
        return s.substring(s.length() - 6, s.length() - 1);
    }

    /**
     * The method reads the word at specified position in the given line, as per
     * the protocol.
     *
     * @param s   The given line.
     * @param pos The specified position.
     *
     * @return word The word read at position pos.
     * @see #fetchStudentList()
     * @since version 0.10
     */
    private String readWordAtPosition(String s, int pos) {

        char[] lineChar = s.toCharArray();
        int placeCounter = 0;
        boolean pass = false;
        String word = "";

        for (int i = 0; i <= lineChar.length; i++) {
            if (i != lineChar.length && (Character.compare(lineChar[i], textDelimiter) != 0 || pass)) {
                if (Character.compare(lineChar[i], fieldDelimiter) == 0) {
                    if (pass) {
                        pass = false;
                        continue;// Don't let fieldDelimiter enter the word :D
                    } else {
                        pass = true;
                        continue;
                    }
                }
                word = word + lineChar[i];
                continue;
            }
            if (placeCounter == pos) {
                return word;
            }
            placeCounter++;
            word = "";
        }

        return "";
    }

    /**
     * This is "hand-written" method which is to be modified from the source
     * (i.e. here).<br>
     * CAUTION: The readable csvInfo entries are to be entered exactly in the
     * same order as they appear in the CSV file.
     *
     * @since version 0.10
     */
    private void setCSVInfo() {

        this.csvInfo.add(new computorString("Student"));
        this.csvInfo.add(new computorString("SIS Login ID"));
        this.csvInfo.add(new computorString("Section"));
        this.csvInfo.add(new computorString("Final Score"));
        this.csvInfo.add(new computorString("Unposted Final Score"));
    }

}
