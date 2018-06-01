/* Sim_outfile.java */

package unbc.ca.distributed.library;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * The default trace output class, which implements the Sim_output
 * interface. This class implements output to a file called tracefile
 * in the current directory, and is used by default in full Java applications.
 * Note that it can't be used in applets, because they do not allow access
 * to files.
 * @see         Sim_output
 * @version     1.0, 4 September
 * @version     1.1, 24 March 1997
 * @author      Ross McNab, Fred Howell
 */
public class Outfile {
  private PrintWriter trcstream;

  /**
   * Constructor
   */
  String outputFile;
  public Outfile(String out)  {
      this.outputFile = out;
  }

  /**
   * Attempt to open the file
   */
  public void initialise() {
    try {
      trcstream = new PrintWriter(new FileOutputStream(outputFile));
    } catch(FileNotFoundException e) {
      System.out.println("Sim_system: Error - could not open trace file");
    }
  }

  /**
   * Print a trace line to the file
   * @param msg The message to print
   */
  public void println(String msg) {
    trcstream.println(msg);
  }

  /**
   * Close the file
   */
  public void close() {
    trcstream.close();
  }
}