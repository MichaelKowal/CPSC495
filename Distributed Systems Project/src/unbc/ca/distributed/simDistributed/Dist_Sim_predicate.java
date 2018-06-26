/* Sim_predicate.java
 */

package unbc.ca.distributed.simDistributed;

/**
 * Predicates are used to select events from the deferred queue.
 * This class is abstract and must be subclassed when writing new
 * predicate. Some standard predicates are provided, see below:
 * @see         eduni.simjava.Sim_type_p
 * @see         eduni.simjava.Sim_from_p
 * @see         eduni.simjava.Sim_any_p
 * @see         eduni.simjava.Sim_none_p
 * @see         eduni.simjava.Sim_system
 * @version     1.0, 4 September 1996
 * @author      Ross McNab
 */

public abstract class Dist_Sim_predicate {
  /**
   * The match function which must be overidden when writing a new
   * predicate. The function is called with each event in the deferred
   * queue as its parameter when a <tt>Sim_system.sim_select()</tt>
   * call is made by the user.
   * @param event The event to test for a match.
   * @return The function should return <tt>true</tt> if the
   *         event matches and shoult be selected, of <tt>false</tt>
   *         if it doesn't
   */
  public abstract boolean match(Dist_Sim_event event);
}
