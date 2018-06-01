/* Sim_outfile.java
 */

package unbc.ca.distributed.simDistributed;

import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * The default trace output class, which implements the Sim_output
 * interface. This class implements output to a file called tracefile
 * in the current directory, and is used by default in full Java applications.
 * Note that it can't be used in applets, because they do not allow access
 * to files.
 * @see         Dist_Sim_output
 * @version     1.0, 4 September
 * @author      Ross McNab
 */

public class Dist_Sim_outfile implements Dist_Sim_output {
  private PrintStream trcstream;
  private String outputFile;

  /** Constructor */
  public Dist_Sim_outfile(String out)  {
      this.outputFile = out;
  }
  /** Attempts to open the file */
  public void initialise() {
    try {
      trcstream = new PrintStream(new FileOutputStream(outputFile));
    } catch(IOException e) {
      System.out.println("Sim_system: Error - could not open trace file");
    }
  }
  /** Prints a trace line to the file
   * @param msg The message to print
   */
  public void println(String msg) {
    trcstream.println(msg);
  }

  /** Closes the file */
  public void close() {
    trcstream.close();
  }
}
