/*
 *   -------------------------- 
 *  |  computorMainWindow.java   
 *   -------------------------- 
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

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jdesktop.swingx.JXStatusBar;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.event.PlotChangeEvent;
import org.jfree.chart.event.PlotChangeListener;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * This Class is responsible for the display of the student lineup and relevant
 * information for the judgment of their grades.
 *
 * The judgment encapsulates the state of information and the action performed
 * to determine the academic standing of students. This includes:
 * <ul>
 * <li>The ordered final score of students
 * <li>The appropriate Markers indicating the levelups
 * <li>The clustering of students based on their grade
 * <li>The global data concerning the mean, median and standard deviation
 * </ul>
 * <p>
 * TODO: Write a new Renderer for student bars with the Highlight and Canvas
 * information display feature.
 *
 * @author quarkCowboy
 * @version %I%, %G%
 * @since 0.10
 */
public class computorMainWindow extends ApplicationFrame {

    /**
     * The dragged Marker flag.
     */
    private boolean bIsMarkerBeingDragged = false;

    /**
     * The flag for mouse over Marker.
     */
    private boolean bIsMouseOnMarker = false;

    /**
     * For Marker Highlighting.
     *
     * @see ChartMouseListener#chartMouseMoved
     */
    private CategoryItemEntity cMentity;

    /**
     * The Marker tracker.
     */
    private CategoryItemEntity draggedMarker;

    /**
     * The label of status bar component with global student statistics (number
     * and average GPA).
     */
    private JLabel jGLabel;

    /**
     * The label of status bar component describing the current status.
     */
    private JLabel jLabel;

    /**
     * The label of status bar component describing the student score statistics
     * (mean, median and standard deviation).
     */
    private JLabel jSLabel;

    /**
     * The Marker data to be displayed.
     */
    private DefaultCategoryDataset markdData;

    /**
     * Buffer of Marker levelups.
     */
    private ArrayList<Double> markerCross = new ArrayList<Double>();

    /**
     * Buffer of Marker data.
     */
    private ArrayList<computorMarker> markerList = new ArrayList<computorMarker>();

    /**
     * The Marker being dragged (synonymous to the Bold Marker!). -1 means no
     * Marker is being dragged.
     */
    private int markermNumber = -1;// 

    /**
     * The student data to be displayed.
     */
    private DefaultCategoryDataset studentDData;

    /**
     * Buffer of student data;
     */
    private ArrayList<computorStudent> studentList = new ArrayList<computorStudent>();

    /**
     * The DataMember for generating the bar plots.
     */
    private CategoryPlot studentPlot;

    /**
     * The method generates the barchart corresponding to the Student score and
     * adds the menu and status bar with the plot. It initializes a series of
     * mouse listeners for interactive data handling. Finally it generates the
     * appropriate menubar.
     *
     * @param studentWTitle String variable to appear as the title of
     *                      application Window
     * @param chartTitle    String variable for the title of the bar chart
     *
     * @since version 0.10
     */
    public computorMainWindow(String studentWTitle, String chartTitle) {

        super(studentWTitle);

        studentDData = new DefaultCategoryDataset();
        markdData = new DefaultCategoryDataset();

        // We create the vertical barchart for the given studentData (from CSV)
        // Using custom ChartPanel according to needs
        JFreeChart studentBarChart = ChartFactory.createBarChart(chartTitle,
                "Students", "Score", updateStudentData(), PlotOrientation.VERTICAL,
                true, false, false);

        ChartPanel chartPanel = new ChartPanel(studentBarChart);
        //addStatusBar();
        chartPanel.setPreferredSize(new java.awt.Dimension(1000, 600));
        this.getContentPane().add(chartPanel);
        //setContentPane(chartPanel);

        studentPlot = studentBarChart.getCategoryPlot();
        //studentBarChart.setElementHinting(true);TODO: Write the Method to set the Rendering quality using RenderingHints (Look BarRenderer.drawItems)
        studentPlot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);//Markers over or under the Bars?
        //studentPlot.setBackgroundPaint((Paint) Color.AQUA);

        /*Image im = null;
         try {
         im = ImageIO.read(new File("darkknight.jpg"));
         } catch (IOException e) {
         }
         studentPlot.setBackgroundImage(im);*/// Just for fun :D
        // Setting a custom renderer for the Marker.  Default (Value) Markers in JFreePlot
        // don't attach ChartEntity with them.  Therefore we use our 
        // custom renderer to draw ValueMarker which can be detected by MouseCLick
        studentPlot.setDataset(1, updateMarkerData()); // A channel (CategoryPlot) through which relevant Marker data goes to the computorMarkerRenderer
        computorMarkerRenderer mRenderer = new computorMarkerRenderer(this, 6.0f, computeNumOfMarkers());// Marker width.
        studentPlot.setRenderer(studentPlot.getRendererCount(), mRenderer); // Add last in the array of renderers and send notification

        // Custom CategoryAxis to generate DomainAxis (x-axis) without labels
        computorCategoryAxis studentcDomainCategory = new computorCategoryAxis("Students");
        studentPlot.setDomainAxis(studentcDomainCategory);

        // Set the orientation and location of label for each individual bar (The primary BarRenderer)
        // TODO: label more clearly with appropriate orientation.  Fonts etc?
        CategoryItemRenderer studentRenderer = studentPlot.getRenderer();
        //studentRenderer.setItemLabelFont(SANS_SERIF);
        ItemLabelPosition studentLabelPosition = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE3,
                TextAnchor.CENTER, TextAnchor.BOTTOM_CENTER, -3.14 / 2);

        // Bar Renderer settings
        studentRenderer.setDefaultPositiveItemLabelPosition(studentLabelPosition);
        studentRenderer.setDefaultItemLabelsVisible(true);
        if (studentPlot.getRenderer() instanceof org.jfree.chart.renderer.category.BarRenderer) {
            BarRenderer bR = ((BarRenderer) studentPlot.getRenderer());
            bR.setShadowVisible(true);
            bR.setLegendItemLabelGenerator(new computorItemLabelGenerator(this));// Generator to show Student count in the Legend
            //bR.setSeriesVisible(0, false);
        }
        studentRenderer.setDefaultItemLabelFont(new Font("Sanserif", Font.BOLD, 13));// Hook for setting Name font etc

        // Hack to label bar by student name
        // {2} - double value (in DefaultCategoryDataset.addValue())
        // {1} - column
        // {0} - rowkey
        StandardCategoryItemLabelGenerator studentLabel = new StandardCategoryItemLabelGenerator("{1}" + " " + "({2}): " + "{0}", NumberFormat.getInstance());
        studentRenderer.setDefaultItemLabelGenerator(studentLabel);

        //Switch for zoom
        chartPanel.setMouseZoomable(false);

        // Add ChartMouseListener for mouse events
        // Lets us know when the mouse is over Marker or student bars
        chartPanel.addChartMouseListener(new ChartMouseListener() {
            @Override
            public void chartMouseClicked(ChartMouseEvent e) {
                final ChartEntity entity = e.getEntity();
                if (entity != null && entity instanceof org.jfree.chart.entity.CategoryItemEntity) {/*
                     final CategoryItemEntity centity = (CategoryItemEntity) entity;
                     System.out.println(entity + " " + entity.getArea() + e.toString() + e.getSource());
                     if(centity.getColumnKey() == "Marker")
                     handleMarkerClicked(centity);*/

                }
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent e) {
                ChartEntity entity = e.getEntity();
                Point2D mouseP = e.getTrigger().getPoint();
                if (entity != null && entity instanceof org.jfree.chart.entity.CategoryItemEntity && !bIsMouseOnMarker) {
                    cMentity = (CategoryItemEntity) entity;
                    final int cNumber = markdData.getColumnIndex(cMentity.getColumnKey());
                    if (cNumber > -1) {
                        bIsMouseOnMarker = true;
                        markermNumber = cNumber;
                        studentPlot.markerChanged(null);
                    }
                }
                if (cMentity != null && !cMentity.getArea().contains(mouseP)) {
                    bIsMouseOnMarker = false;
                    markermNumber = -1;//Experimental
                    studentPlot.markerChanged(null);
                }
            }
        });

        // Add Java MouseListener to keep track of mouse button release
        chartPanel.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent event) {
                if (bIsMarkerBeingDragged) {
                    bIsMarkerBeingDragged = false;
                    int x = event.getX();
                    int y = event.getY();
                    handleMarkerMouseReleased(chartPanel, x, y);
                    studentPlot.markerChanged(null);//Reset renderer if required!
                }

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        // Add Java MouseMotionListener to track when Mouse drags Marker
        chartPanel.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent event) {
                if (bIsMarkerBeingDragged) {
                    return;
                }
                Insets insets = chartPanel.getInsets();
                int x = (int) ((event.getX() - insets.left) / chartPanel.getScaleX());
                int y = (int) ((event.getY() - insets.top) / chartPanel.getScaleY());

                ChartEntity entity = null;
                if (chartPanel.getChartRenderingInfo() != null) {
                    EntityCollection entities = chartPanel.getChartRenderingInfo().getEntityCollection();
                    if (entities != null) {
                        entity = entities.getEntity(x, y);
                    }
                }

                if (entity instanceof org.jfree.chart.entity.CategoryItemEntity) {
                    final CategoryItemEntity centity = (CategoryItemEntity) entity;
                    final int cNumber = markdData.getColumnIndex(centity.getColumnKey());
                    if (cNumber > -1) {
                        handleMarkerDragged(centity, chartPanel, true, y);
                        bIsMarkerBeingDragged = true;
                        markermNumber = cNumber;
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });

        // Now the GradeComputer is initialized.  Let us add the appropriate components in the Menu bar    
        createMenuBar();
        // Add the Status bar
        addStatusBar(chartPanel);
    }

    /**
     * Method to dynamically load the icons from the jar file and add along the
     * side of menu items.
     *
     * @param jI The menuitem to be added.
     * @param n  The name of the icon.
     *
     * @see #createMenuBar()
     * @since version 0.10
     */
    private void addIcon(JMenuItem jI, String n) {

        InputStream is = this.getClass().getResourceAsStream("/resources/icons/" + n);

        try {
            ImageIcon imageIcon = new ImageIcon(ImageIO.read(is));
            jI.setIcon(imageIcon);
        } catch (IOException ex) {
            Logger.getLogger(computorMainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Method to add Marker(s) in the buffer as per the requirement.
     *
     * @see computorMarkerEditor#attemptAddMarkerTitle
     * @param count Marker number to be added
     *
     * @since version 0.10
     */
    public void addNewMarker(int count) {
        for (int i = this.markerList.size(); i < count + 1; i++) {
            this.markerList.add(new computorMarker(100 - 10 * i, mCountToGrade(i), "Mark " + i));
        }
        masterUpdateFromBuffer();
    }

    /**
     * Method to add status bar with global statistics. This method uses the
     * SwingX library to construct the bar.
     *
     * @param cp The chartpanel to which status bar is to be added.
     *
     * @since version 0.10
     */
    private void addStatusBar(ChartPanel cp) {

        JXStatusBar j = new JXStatusBar();

        JXStatusBar jStatus = new JXStatusBar();
        this.jLabel = new JLabel("Ready!");
        this.jLabel.setPreferredSize(new Dimension(80, 10));

        jStatus.add(jLabel);

        JXStatusBar jGlobal = new JXStatusBar();
        this.jGLabel = new JLabel("No Students");

        this.studentPlot.addChangeListener(new PlotChangeListener() {
            @Override
            public void plotChanged(PlotChangeEvent event) {
                somethingInPlotChanged();
            }
        });

        jGlobal.add(jGLabel);

        JXStatusBar jStatistics = new JXStatusBar();

        this.jSLabel = new JLabel("No Data");
        this.jSLabel.setPreferredSize(new Dimension(500, 10));

        jStatistics.add(this.jSLabel);

        JButton jCButton = new JButton();
        jCButton.setText("Clear");
        jCButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearAll();
            }
        });

        jStatus.add(jGlobal);
        jStatus.add(jStatistics);
        jStatus.add(jCButton);

        jStatus.setPreferredSize(new Dimension(500, 35));
        this.getContentPane().add(jStatus, java.awt.BorderLayout.SOUTH);
    }

    /**
     * Method to assign grades to the studentList from markerList.
     *
     * @see #handleMarkerMouseReleased(ChartPanel cP, int x, int y)
     * @see #masterUpdateFromBuffer()
     * @since version 0.10
     */
    public void assignGradesToStudents() {

        // Assume Lists are sorted
        int markerCounter = 0;

        for (int i = 0; i < this.studentList.size(); i++) {
            if (this.markerList.size() < markerCounter + 1) {
                this.studentList.get(i).setGrade("n/a");
                continue;
            }
            if (this.studentList.get(i).getScore() > this.markerList.get(markerCounter).getValue()) {
                this.studentList.get(i).setGrade(this.markerList.get(markerCounter).getGrade());
            } else {
                ++markerCounter;
                if (this.markerList.size() >= markerCounter + 1) {
                    this.studentList.get(i).setGrade(this.markerList.get(markerCounter).getGrade());
                } else {
                    this.studentList.get(i).setGrade("n/a");
                    continue;
                }
            }
            this.markerList.get(markerCounter).ascent();
        }

        updateStudentData();
    }

    /**
     * Method to clear the marker and student buffer. The "Clean Slate" routine.
     *
     * @since version 0.10
     */
    private void clearAll() {
        this.studentList.clear();
        this.markerList.clear();
        masterUpdateFromBuffer();
    }

    /**
     * Method to compute the crossover percentage amongst two consecutive
     * category of students separated by the Marker in the buffer.
     *
     * @since version 0.10
     */
    public void computeMarkerCrossPercentage() {

        int markcrossCounter = 0;
        int diff = 0;

        this.markerCross.clear();

        for (int i = 0; i < this.studentList.size(); i++) {
            if (this.studentList.get(i).getScore() < this.markerList.get(markcrossCounter).getValue()) {
                if (i == 0) {
                    continue;
                }
                double perc = (this.studentList.get(i - 1).getScore() - this.studentList.get(i).getScore()) / this.studentList.get(i).getScore() * 100;
                this.markerCross.add(Math.round((perc) * 100D) / 100D);
                markcrossCounter++;
            }
        }

        diff = this.markerList.size() - markcrossCounter;
        if (diff > 0) {
            for (int i = 0; i < diff; i++) {
                this.markerCross.add(Double.NaN);
            }
        }
    }

    /**
     * Method to compute the number of Markers displayed on the plot.
     *
     * @return result number of Markers.
     * @since version 0.10
     */
    public int computeNumOfMarkers() {

        int result = 0;
        int rowCount = this.markdData.getRowCount();
        int columnCount = this.markdData.getColumnCount();

        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                if (this.markdData.getValue(row, column) != null) {
                    result++;
                }
            }
        }

        return result;
    }

    /**
     * Method to compute the number of students displayed on the plot.
     *
     * @return result number of students.
     * @since version 0.10
     */
    public int computeNumOfStudents() {

        int result = 0;
        int rowCount = this.studentDData.getRowCount();
        int columnCount = this.studentDData.getColumnCount();

        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                if (this.studentDData.getValue(row, column) != null) {
                    result++;
                }
            }
        }

        return result;
    }

    /**
     * Method to compute the menu bar for the Main window to equip with data
     * loading and handling capabilities (from/to CSV files). Dynamically loads
     * the icons from jar package. Armed with powerful color coding interface
     * for the Markers.
     *
     * @since version 0.10
     */
    public void createMenuBar() {

        Container c = this;

        JFileChooser jFC = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV (comma-seperated values)", "csv");

        jFC.setFileFilter(filter);
        jFC.setDragEnabled(true);
        jFC.setMultiSelectionEnabled(true);

        JMenuBar jBar = new JMenuBar();

        JMenu file = new JMenu("File");

        JMenuItem openFile = new JMenuItem("Open File...");
        openFile.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                jFC.setDialogTitle("Open Student Records");
                int returnVal = jFC.showOpenDialog(c);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    computorCSVReader reader = new computorCSVReader(jFC.getSelectedFiles());
                    try {
                        updateStudentList(reader.fetchStudentList());
                    } catch (IOException ex) {
                        Logger.getLogger(computorMainWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        addIcon(openFile, "open.png");

        JMenuItem saveFile = new JMenuItem("Memorise Markers");
        saveFile.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                jFC.setDialogTitle("Save Markers");
                int returnVal = jFC.showSaveDialog(c);
                if (returnVal == 0) {
                    saveMarkers(jFC.getSelectedFile());
                }
            }
        });
        addIcon(saveFile, "memorise.png");

        JMenuItem loadmarkers = new JMenuItem("Load Markers");
        loadmarkers.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                jFC.setDialogTitle("Load Markers");
                int returnVal = jFC.showOpenDialog(c);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    computorCSVReader reader = new computorCSVReader(jFC.getSelectedFile());
                    try {
                        updateMarkerList(reader.fetchMarkerList());
                    } catch (IOException ex) {
                        Logger.getLogger(computorMainWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        addIcon(loadmarkers, "restore.png");

        JMenuItem registrar = new JMenuItem("Registrar Export");
        registrar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                jFC.setDialogTitle("Registrar Export");
                int returnVal = jFC.showSaveDialog(c);
                if (returnVal == 0) {
                    prepareRegistrarFile(jFC.getSelectedFile());
                    playRegistrarSound();
                }
            }
        });
        addIcon(registrar, "registrar.png");

        file.add(openFile);
        file.add(saveFile);
        file.add(loadmarkers);
        file.add(registrar);

        JMenu gNome = new JMenu("gNome");

        JMenuItem colorEncoder = new JMenuItem("Color Encoder");
        colorEncoder.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                openColorEncoder();
            }
        });
        addIcon(colorEncoder, "gnome.png");

        JMenuItem shapeandfeel = new JMenuItem("Shape and Feel");
        shapeandfeel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                openShapeAndFeel();
            }
        });
        addIcon(shapeandfeel, "feel.png");

        gNome.add(colorEncoder);
        gNome.add(shapeandfeel);

        JMenu help = new JMenu("Help");

        JMenuItem about = new JMenuItem("About");
        about.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                openAboutWindow();
            }
        });
        addIcon(about, "about.png");

        help.add(about);

        jBar.add(file);
        jBar.add(gNome);
        jBar.add(help);
        this.setJMenuBar(jBar);
    }

    /**
     * The method shows the structure of data stored by CategoryDataset. Never
     * used during the runtime. This is only for debugging and experimentation.
     *
     * @param dc the data on display.
     *
     * @since version 0.10
     */
    public void displayDataSet(CategoryDataset dc) {

        int rowCount = dc.getRowCount();
        int columnCount = dc.getColumnCount();

        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                if (dc.getValue(dc.getRowKey(row), dc.getColumnKey(column)) == null) {
                    System.out.print("null  ");
                } else {
                    System.out.print(dc.getValue(dc.getRowKey(row), dc.getColumnKey(column)) + " ");
                }
                if (dc.getRowKey(row) == null) {
                    System.out.print("null  ");
                } else {
                    System.out.print(dc.getRowKey(row) + " ");
                }
                if (dc.getColumnKey(column) == null) {
                    System.out.print("null  ");
                } else {
                    System.out.print(dc.getColumnKey(column) + "\n");
                }
            }
        }
    }

    /**
     * The method to find mean score of the students.
     *
     * @param c The student buffer.
     *
     * @return double Mean
     * @since version 0.10
     */
    private double findMeanFromList(ArrayList<computorStudent> c) {

        double l = 0;
        for (int i = 0; i < c.size(); i++) {
            l += c.get(i).getScore();
        }
        l /= c.size();
        l = Math.round((l) * 100D) / 100D;
        return l;
    }

    /**
     * The method to find median score of the students.
     *
     * @param c The student buffer.
     *
     * @return double Median
     * @since version 0.10
     */
    private double findMedianFromList(ArrayList<computorStudent> c) {

        double l = 0;
        if (c.size() % 2 == 0) {
            l = (c.get(c.size() / 2 - 1).getScore() + c.get(c.size() / 2).getScore()) / 2;
        } else {
            l = c.get(c.size() / 2 - 1).getScore();
        }
        return Math.round((l) * 100D) / 100D;
    }

    /**
     * The method to find standard deviation of the score of the students.
     *
     * @param c The student buffer.
     *
     * @return double standard deviation.
     * @since version 0.10
     */
    private double findSTDdevFromList(ArrayList<computorStudent> c) {

        double m = findMeanFromList(c);
        double l = 0;
        for (int i = 0; i < c.size(); i++) {
            l += Math.pow((c.get(i).getScore() - m), 2);
        }
        l /= c.size() - 1;
        l = Math.sqrt(l);
        return Math.round((l) * 100D) / 100D;
    }

    /**
     * The method to return the CategoryPlot object.
     *
     * @return studentPlot The CategoryPlot with custom Marker renderer.
     *
     * @since version 0.10
     */
    public CategoryPlot getCategoryPlot() {
        return this.studentPlot;
    }

    /**
     * The method is self explanatory.
     *
     * @param i Marker counter.
     *
     * @return double the cross-percentage generated by the Marker.
     * @since version 0.10
     */
    public double getMarkerCrossPercentage(int i) {
        return this.markerCross.get(i);
    }

    /**
     * The method returns the Marker buffer.
     *
     * @return Dynamic Array of computorMarker
     * @since version 0.10
     */
    public ArrayList<computorMarker> getMarkerList() {
        return this.markerList;
    }

    /**
     * The method returns the number of Marker being dragged. 0 based.
     *
     * @return int The number.
     * @since version 0.10
     */
    public int getMarkerNumber() {
        return this.markermNumber;
    }

    /**
     * The method called when the mouse clicks the Marker. Does nothing, maybe
     * it will be useful in future :).
     *
     * @param cI The item entity for Marker.
     *
     * @since version 0.10
     */
    private void handleMarkerClicked(CategoryItemEntity cI) {
        //System.out.print("Dragging Marker");
    }

    /**
     * The method is called when the Marker is being dragged by the mouse. TODO:
     * Write the mechanism for rendering the Marker line in "Realtime".
     *
     * @param cI The CategoryItemEntity associated with the Marker.
     * @param cP The ChartPanel container of the studentWindow.
     * @param y  The location of mouse projected on y-axis.
     *
     * @since version 0.10
     */
    private void handleMarkerDragged(CategoryItemEntity cI, ChartPanel cP, boolean xor, int y) {

        // System.out.print(cI.getColumnKey());
        this.draggedMarker = cI;
        /*   Graphics2D g2 = (Graphics2D) cP.getGraphics();
        //System.out.print(g2.toString());
        g2.setXORMode(Color.red);
        g2.setPaint(Color.RED);
        
        
         //Calculate the dragline parameters
         Rectangle2D screenArea = cP.getScreenDataArea();
         double value = (double) cI.getDataset().getValue(0, 0);
        
         Line2D line = new Line2D.Double(screenArea.getMinX(), y, screenArea.getMaxX(), y);
         // System.out.print(line.getX1()+""+ line.getX2()+""+ line.getY1()+""+line.getY2());
         g2.draw(line);*/

    }

    /**
     * The method is called when the Marker is no more being dragged by the
     * mouse.
     *
     * @param cP The ChartPanel container of the studentWindow.
     * @param x  The x coordinate of the mouse in the ChartPanel window.
     * @param y  The x coordinate of the mouse in the ChartPanel window.
     *
     * @since version 0.10
     */
    private void handleMarkerMouseReleased(ChartPanel cP, int x, int y) {

        CategoryPlot cPl = cP.getChart().getCategoryPlot();
        ValueAxis VA = cPl.getRangeAxisForDataset(1);// Careful with the dataset
        double value = VA.java2DToValue(y, cP.getScreenDataArea(x, y), cPl.getRangeAxisEdge());
        this.markdData.setValue(value, this.draggedMarker.getRowKey(), this.draggedMarker.getColumnKey());
        updateMarkerList();
        computeMarkerCrossPercentage();
        assignGradesToStudents();
        this.draggedMarker = null;
    }

    /**
     * The method returns the points associated with the grades as per
     * University's prescription.
     *
     * @param g The letter grade.
     *
     * @return double Points corresponding to the letter grade.
     * @since version 0.10
     */
    private double letterToPoints(String g) {
        if (g.compareTo("A") == 0) {
            return 4;
        }
        if (g.compareTo("A-") == 0) {
            return 3.67;
        }
        if (g.compareTo("B+") == 0) {
            return 3.33;
        }
        if (g.compareTo("B") == 0) {
            return 3;
        }
        if (g.compareTo("B-") == 0) {
            return 2.67;
        }
        if (g.compareTo("C+") == 0) {
            return 2.33;
        }
        if (g.compareTo("C") == 0) {
            return 2;
        }
        if (g.compareTo("C-") == 0) {
            return 1.67;
        }
        if (g.compareTo("D+") == 0) {
            return 1.33;
        }
        if (g.compareTo("D") == 0) {
            return 1;
        }
        if (g.compareTo("D-") == 0) {
            return 0.67;
        }
        if (g.compareTo("F") == 0) {
            return 0;
        }
        return -1;
    }

    /**
     * The method returns grade associated with the Marker count. 0 based.
     *
     * @param i The Marker count.
     *
     * @return String grades corresponding to Marker counter.
     * @since version 0.10
     */
    public String mCountToGrade(int i) {
        if (i == 0) {
            return "A";
        }
        if (i == 1) {
            return "A-";
        }
        if (i == 2) {
            return "B+";
        }
        if (i == 3) {
            return "B";
        }
        if (i == 4) {
            return "B-";
        }
        if (i == 5) {
            return "C+";
        }
        if (i == 6) {
            return "C";
        }
        if (i == 7) {
            return "C-";
        }
        if (i == 8) {
            return "D+";
        }
        if (i == 9) {
            return "D";
        }
        if (i == 10) {
            return "D-";
        }
        if (i == 11) {
            return "F";
        }
        return "";
    }

    /**
     * The method generates the update of data on display (on the plot) from the
     * buffer data. Called whenever changes are rendered in the buffer.
     *
     * @since version 0.10
     */
    public void masterUpdateFromBuffer() {
        updateMarkerData();
        updateStudentData();
        computeMarkerCrossPercentage();
        assignGradesToStudents();
        this.studentPlot.markerChanged(null);
    }

    /**
     * Opens the About window containing the relevant information about this
     * project.
     *
     * @since version 0.10
     */
    private void openAboutWindow() {
        JFrame about = new JFrame("About");
        computorProductInformationPanel cPan = new computorProductInformationPanel();

        about.add(cPan);
        about.pack();
        about.setVisible(true);
        about.setLocationRelativeTo(this);
    }

    /**
     * Opens the ColorEncoder window with appropriate configuration tools.
     *
     * @since version 0.10
     */
    private void openColorEncoder() {
        JFrame jF = new JFrame("Color Encoder");
        computorMarkerConfigPanel pan = new computorMarkerConfigPanel(this);

        jF.add(pan);
        jF.pack();
        jF.setVisible(true);
        jF.setResizable(true);
        jF.setLocationRelativeTo(this);

    }

    /**
     * Opens the JFileChooser dialog for choosing the appropriate background for
     * the plot. TODO: Maybe write a new Renderer for the bars for nice feel.
     *
     * @since version 0.10
     */
    private void openShapeAndFeel() {

        JFileChooser jFC = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpeg", "gif", "png", "jpg", "bmp");

        jFC.setFileFilter(filter);
        jFC.setDragEnabled(false);
        jFC.setMultiSelectionEnabled(false);
        jFC.setDialogTitle("Set Background Image");

        if (jFC.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            System.out.print("Set Background");

            try {
                BufferedImage image = ImageIO.read(jFC.getSelectedFile());
                this.studentPlot.setBackgroundImage(image);
            } catch (IOException ex) {
                Logger.getLogger(computorMainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * The method generates plays the "Church Bell" sound after saving the
     * Registrar file.
     *
     * @see #createMenuBar()
     * @since version 0.10
     */
    public void playRegistrarSound() {
        try {
            URL is = this.getClass().getResource("/resources/sounds/hero.aiff");
            AudioInputStream ais = AudioSystem.getAudioInputStream(is);
            Clip aClip = AudioSystem.getClip();
            if (aClip.isRunning()) {
                aClip.stop();   // Stop the player if it is still running
            }
            aClip.setFramePosition(0); // rewind to the beginning
            aClip.open(ais);
            aClip.start();     // Start playing
        } catch (LineUnavailableException ex) {
            Logger.getLogger(computorMainWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(computorMainWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(computorMainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns the lettergrade associated with the point prescribed by the
     * University
     *
     * @param pt The grade point.
     *
     * @return String Letter grade corresponding to the points.
     * @see #letterToPoints(String g)
     * @since version 0.10
     */
    private String pointsToLetter(double pt) {
        if (pt == 4) {
            return "A";
        }
        if (pt < 4 && pt >= 3.67) {
            return "A-";
        }
        if (pt < 3.67 && pt >= 3.33) {
            return "B+";
        }
        if (pt < 3.33 && pt >= 3) {
            return "B";
        }
        if (pt < 3 && pt >= 2.67) {
            return "B-";
        }
        if (pt < 2.67 && pt >= 2.33) {
            return "C+";
        }
        if (pt < 2.33 && pt >= 2) {
            return "C";
        }
        if (pt < 2 && pt >= 1.67) {
            return "C-";
        }
        if (pt < 1.67 && pt >= 1.33) {
            return "D+";
        }
        if (pt < 1.33 && pt >= 1) {
            return "D";
        }
        if (pt < 1 && pt >= 1.67) {
            return "D-";
        }
        if (pt < 1.67 && pt >= 0) {
            return "F";
        }
        return "";
    }

    /**
     * The method generates appropriate Registrar file for reporting the judged
     * grades.
     *
     * @param f The file which must contain the information.
     *
     * @since version 0.10
     */
    private void prepareRegistrarFile(File f) {
        computorCSVWriter cCSV = new computorCSVWriter(f);
        try {
            cCSV.writerCSVFile(this.studentList);
        } catch (IOException ex) {
            Logger.getLogger(computorMainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Method to remove Marker(s) in the buffer as per the requirement.
     *
     * @see computorMarkerEditor#attemptRemoveMarkerTitle()
     * @param count Marker number to be removed.
     *
     * @since version 0.10
     */
    public void removeMarker(int count) {
        for (int i = this.markerList.size() - 1; i >= count; i--) {
            this.markerList.remove(i);
        }
        masterUpdateFromBuffer();
    }

    /**
     * The method saves the Marker placements for future accessability
     * (accessibility).
     *
     * @param f The file which must contain the information.
     *
     * @since version 0.10
     */
    private void saveMarkers(File f) {
        computorCSVWriter cCSV = new computorCSVWriter(f);
        //  System.out.print(filename+path+"\n");
        try {
            cCSV.writeCSVFile(this.markerList);
        } catch (IOException ex) {
            Logger.getLogger(computorMainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Method to compute the global statistics whenever the changes are rendered
     * in the plot.
     *
     * @see #addStatusBar(ChartPanel cp)
     * @since version 0.10
     */
    private void somethingInPlotChanged() {
        if (markermNumber > -1) {
            this.jLabel.setText("Marker " + markermNumber + "\n");
        } else {
            this.jLabel.setText("Ready!");
        }

        if (this.studentList.size() > 0) {
            double avGpa = 0;
            for (int i = 0; i < this.studentList.size(); i++) {
                avGpa += letterToPoints(this.studentList.get(i).getGrade());
            }
            avGpa = avGpa / this.studentList.size();
            this.jGLabel.setText(this.studentList.size() + " Students,  " + "Average GPA: " + Math.round((avGpa) * 100D) / 100D + " (" + pointsToLetter(Math.round((avGpa) * 100D) / 100D) + ")");
            this.jSLabel.setText("Mean: " + findMeanFromList(this.studentList) + ", Median: "
                    + findMedianFromList(this.studentList) + ", STDdev: " + findSTDdevFromList(this.studentList));
        } else {
            this.jGLabel.setText("No Students");
            this.jSLabel.setText("No Data");
        }

    }

    /**
     * Method to update the Marker data from the buffer.
     *
     * @return markData Dataset with the value, grade and Marker name
     *         information.
     * @since version 0.10
     */
    private CategoryDataset updateMarkerData() {

        this.markdData.clear();

        for (int i = 0; i < this.markerList.size(); i++) {
            this.markdData.addValue(this.markerList.get(i).getValue(),
                    this.markerList.get(i).getGrade(), this.markerList.get(i).getName());
        }

        return this.markdData;
    }

    /**
     * The overloaded method to update the Marker list from the appropriately
     * manipulated Marker data on the plot or from the memory.
     *
     * @see #createMenuBar()
     * @since version 0.10
     */
    private void updateMarkerList() {

        this.markerList.clear();

        for (int i = 0; i < computeNumOfMarkers(); i++) {
            this.markerList.add(new computorMarker((double) this.markdData.getValue(i, i),
                    (String) this.markdData.getRowKey(i),
                    (String) this.markdData.getColumnKey(i)));
        }
    }

    /**
     * The overloaded method to update the Marker list from the appropriately
     * manipulated Marker data on the plot or from the memory.
     *
     * @param m List of Markers.
     *
     * @see #createMenuBar()
     * @since version 0.10
     */
    private void updateMarkerList(ArrayList<computorMarker> m) {
        this.markerList.clear();
        this.markerList = m;
        masterUpdateFromBuffer();
    }

    /**
     * Method to update the student data from the buffer. The data is always in
     * descending order with respect to the student score.
     *
     * @return studentData Dataset with the score, grade and Name information.
     * @since version 0.10
     */
    private CategoryDataset updateStudentData() {

        this.studentDData.clear();

        for (int i = 0; i < this.studentList.size(); i++) {
            this.studentDData.addValue(this.studentList.get(i).getScore(),
                    this.studentList.get(i).getGrade(), this.studentList.get(i).getName());
        }
        return studentDData;
    }

    /**
     * Method to update the student data from the CSV file. The data is
     * appropriately sorted for the functioning of the program.
     *
     * @param c The student buffer.
     *
     * @since version 0.10
     */
    private void updateStudentList(ArrayList<computorStudent> c) {
        this.studentList.clear();
        this.markerList.clear();
        this.studentList = c;
        this.studentList.sort(new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                if (((computorStudent) o1).getScore() > ((computorStudent) o2).getScore()) {
                    return -1;
                }
                if (((computorStudent) o1).getScore() < ((computorStudent) o2).getScore()) {
                    return 1;
                }
                return 0;
            }
        });
        masterUpdateFromBuffer();
    }
}
