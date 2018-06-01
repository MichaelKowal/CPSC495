/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.management;

import java.awt.Color;
import java.util.Random;

/**
 *
 * @author behnish
 */
public class Configuration 
{
    public static final int radius = 13;
    
    public static final int diameter = radius * 2;
    public static final int edgeThickness = 3;
    
    public static Color animationNodeColor = Color.ORANGE;
    public static Color nodeColor = Color.ORANGE;
    public static Color edgeColor = Color.GRAY;   
    public static Color selectedColor = Color.BLUE;    
    
    public static boolean simLibMessage = false;
    
    public static String route = "Hop"; /* Short ---> Shortest route , Hop ---> Less hop count */
    public static double simulationLength = 100.0;
        
    public static boolean runningSimulation = false;
    public static boolean debug = false;
    public static int noOfRandomNodes = 10;
    
    public static final Random rnd = new Random();
    
    public static boolean simulationEndCheck = true;
    
    public static int totalNumberOfSimulations = 1;       
    public static int defaultWeightOnEdges = 1;
    public static String currentTraceFile = "trace";
    
    public static int speed = 5;
    public static int constantValue = 0;
    public static String database = "No";
    
    /* 
     * Parameters for Distribution of simulation over network using Java RMI 
     */
    
    public static String host = "localhost";
    public static String port = "2013";     
    public static String databaseName = "simulation";
    public static String username  ="root";
    public static String password  ="";
    
}