/* Sim_from_p.java
 */

package unbc.ca.distributed.simDistributed;

// MITRE Modification
import java.rmi.*;

/**
 * A predicate which selects events from a specific entity, out of the
 * deferred event queue.
 * @see         eduni.simjava.Dist_Sim_predicate
 * @version     1.0, 4 September
 * @author      Ross McNab
 */

public class Dist_Sim_from_p extends Dist_Sim_predicate {
  private int src_e;
  /** Constructor.
   * @param source_ent The id number of the source entity to look for
   * @returns A new predicate which selects events sent from the entity
   *          <tt>source_ent</tt>
   */
  public Dist_Sim_from_p(int source_ent) { src_e = source_ent; }
  /** The match function called by Sim_system.sim_select(),
   * not used directly by the user.
   */
  public boolean match(Dist_Sim_event ev) 
  {
    // MITRE Modification
    try {
      return (ev.get_src() == src_e);
    } catch (RemoteException re) {
      System.out.println("Sim_from_p.match error: " + re.getMessage());
      re.printStackTrace();
      return false;
    } // Try catch
  }
}
