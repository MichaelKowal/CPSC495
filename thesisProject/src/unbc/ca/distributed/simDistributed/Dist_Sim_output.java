/* Sim_output.java
 */

package unbc.ca.distributed.simDistributed;

/**
 * This defines the interface that a trace output class must provide.
 * @see         Sim_system
 * @version     1.0, 4 September 1996
 * @author      Ross McNab
 */

public interface Dist_Sim_output {
  /** Called by the Sim_system before the start of the simulation. */
  void initialise();
  /** Called by the Sim_system to output a trace line.
   * @param msg The trace message
   */
  void println(String msg);
  /** Called by the Sim_system at the end of the simulation. */
  void close();
}
