/* Sim_any_p.java */
package unbc.ca.distributed.library;

/**
 * A predicate which will match any event on the deferred event queue. There is
 * a publicly accessible instance of this predicate in <code>Sim_system</code>,
 * called <code>Sim_system.SIM_ANY</code>, so no new instances need to be
 * created..
 *
 * @see eduni.simjava.Sim_predicate
 * @see eduni.simjava.Sim_system
 * @version 1.0, 4 September 1996
 * @author Ross McNab
 */
public class Predicate {

    /**
     * The match function called by <code>Sim_system</code>, not used directly
     * by the user
     *
     * @param ev
     * @return
     */
    public boolean match(Event ev) {
        return true;
    }
}
