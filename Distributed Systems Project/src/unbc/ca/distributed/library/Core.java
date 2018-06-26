/* Sim_system.java */
package unbc.ca.distributed.library;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javax.swing.table.DefaultTableModel;
import unbc.ca.distributed.management.Configuration;
import unbc.ca.distributed.management.ObjectFactory;

/*
 * @see Sim_entity
 * @see eduni.simanim.Anim_applet
 * @version 2.0, 12 July 2002
 * @author Costas Simatos
 Orginal class name of this class is Sim_system

 But modified to work with the current system and thread implementation has been removed from
 the library and made the execution of the entities sequence in order to speed up the 
 excution of the simulation.
 */
public class Core {

    private static boolean incomplete = true; // Flag for telling whether a simulation is complete or not
    // Private data members
    private static List entities; // The current entity list
    private static Evqueue future; // The future event queue
    private static Evqueue deferred; // The deferred event queue
    private static double clock;  // Holds the current global sim time
    private static boolean running; // Flag for checking if the simulation is running
    private static Semaphore onestopped; // Semaphore for synchronising entities
    private static Semaphore onecompleted; // Semaphore for registering entities having completed    
    // For trace file for animation, hop processing and other stuff
    private static Outfile trcout; // The output object for trace messages
    private static Outfile hopProcessTrace; // The output object for trace messages
    private static Outfile finalSendTrace; // The output object for trace messages
    private static Outfile animationTrace; // The output object for trace messages
    private static int checkIfNothingHappening = 0;
    public static Predicate SIM_ANY = new Predicate();

    /**
     * Do not a constructor for <code>Sim_system</code>. Use an
     * <code>initialise</code> method instead.
     */
    public Core() {
        throw new Error("Attempt to instantiate Sim_system.");
    }

    /**
     * Initialize the simulation for standalone simulations. This function
     * should be called at the start of the simulation.
     */
    public static void initialise() {
        initialise(new Outfile(Configuration.currentTraceFile), null);
    }

    /*
     * Adding processed events in the Console
     */
    public static synchronized void addTableEntry(Object[] data) {
        if (!ObjectFactory.isMultiple()) {
            DefaultTableModel modal = (DefaultTableModel) ObjectFactory.getFooter().executionDetails.getModel();
            modal.addRow(data);
        }
    }

    /**
     * Initialize the system to draw <code>simdiag</code> diagrams. This method
     * should be used directly only by expert users.
     *
     * @param out The <code>Sim_output</code> instance that will receive the
     * simulation trace
     * @param sim The simulation thread.
     */
    public static void initialise(Outfile out, Thread sim) {
        print_message("Initialising...");
        entities = new ArrayList();
        future = new Evqueue();
        deferred = new Evqueue();
        clock = 1.0;
        running = false;
        onestopped = new Semaphore(0);
        onecompleted = new Semaphore(0);
        trcout = out;
        trcout.initialise();

        /* Added by Behnish */
        hopProcessTrace = new Outfile(Configuration.currentTraceFile + "_hop");
        hopProcessTrace.initialise();
        finalSendTrace = new Outfile(Configuration.currentTraceFile + "_final");
        finalSendTrace.initialise();
        animationTrace = new Outfile(Configuration.currentTraceFile + "_animation");
        animationTrace.initialise();
        /* Added by Behnish  ends*/

    }

    /**
     * A standard predicate that does not match any events. /** Get the current
     * simulation time. This method is identical to <code>sim_clock()</code> and
     * is present for compatibility with existing simulations.
     *
     * @return The simulation time
     */
    public static double clock() {
        return clock;
    }

    /**
     * Get the current number of entities in the simulation.
     *
     * @return The number of entities
     */
    public static int get_num_entities() {
        return entities.size();
    }

    /**
     * Get the entity with a given name.
     *
     * @param name The entity's name
     * @return The entity
     * @throws Error If the entity was not found. This error can be left
     * unchecked.
     */
    public static Entity get_entity(String name) {
        Entity ent;
        int entities_size = entities.size();
        for (int i = 0; i < entities_size; i++) {
            ent = (Entity) entities.get(i);
            if (name.compareTo(ent.get_name()) == 0) {
                return ent;
            }
        }
        throw new Error("Entity " + name + " does not exist.");
    }

    public static void add(Entity e) {
        if (e.get_id() == -1) { // Only add once!                
            e.set_id(entities.size());
            entities.add(e);
        }
    }

    /**
     * Link the ports of two entities so that events can be scheduled.
     *
     * @param ent1 The name of the first entity
     * @param port1 The name of the port on the first entity
     * @param ent2 The name of the second entity
     * @param port2 The name of the port on the second entity
     */
    public static void link_ports(String ent1, String port1, String ent2, String port2) {
        Port p1, p2;
        Entity e1, e2;
        e1 = get_entity(ent1);
        e2 = get_entity(ent2);
        if (e1 == null) {
            throw new Error("Sim_system: " + ent1 + " not found.");
        } else if (e2 == null) {
            throw new Error("Sim_system: " + ent2 + " not found.");
        } else {
            p1 = e1.get_port(port1);
            p2 = e2.get_port(port2);
            if (p1 == null) {
                throw new Error("Sim_system: " + port1 + " not found.");
            } else if (p2 == null) {
                throw new Error("Sim_system: " + port2 + " not found.");
            } else {
                p1.connect(e2);
                p2.connect(e1);
            }
        }
    }

    /**
     * Internal method used to run one tick of the simulation. This method
     * should <b>not</b> be called in simulations.
     *
     * @return <code>true</code> if the event queue is empty, <code>false</code>
     * otherwise
     */
    public static boolean run_tick() {
        Iterator e;
        Entity ent;

        for (e = entities.iterator(); e.hasNext();) {
            ent = (Entity) e.next();
            ent.body();
        }

        boolean queue_empty;
        // If there are more future events then deal with them
        if (future.size() > 0 && Configuration.simulationLength > clock) {
            queue_empty = false;
            Event first = future.pop();
            process_event(first);
            // Check if next events are at same time...            
            boolean trymore = (future.size() > 0);
            while (trymore) {
                Event next = future.top();
                if (next.event_time() == first.event_time()) {
                    process_event(future.pop());
                    trymore = (future.size() > 0);
                } else {
                    trymore = false;
                }
            }
        } else {
            if (Configuration.simulationLength > clock && checkIfNothingHappening < 1000) {
                 //if (Configuration.simulationLength > clock) {
                queue_empty = false;
                checkIfNothingHappening++;
            } else {
                if (checkIfNothingHappening >= 999) {
                    System.out.println("#############################Simulation deadlocked#############################################################################################");
                }                
                queue_empty = true;
                running = false;
                print_message("Sim_system: No more future events");
            }
        }

        return queue_empty;
    }

    /**
     * Internal method used to stop the simulation. This method should
     * <b>not</b> be used directly.
     */
    public static void run_stop() {
        print_message("Simulation completed.");
        trcout.close();
        hopProcessTrace.close();
        finalSendTrace.close();
        animationTrace.close();
        ObjectFactory.getMessagePanel().messageDisplay.setText("Simulation completed.");
    }

    // Entity service methods
    // Called by an entity just before it become non-RUNNABLE
    public static void paused() {
        onestopped.v();
    }

    // For CS entry
    public static synchronized void CS(int src, double delay) {
        Event e = new Event(Event.CS, clock + delay, src);
        future.add_event(e);
    }
    // For inter Request entry

    public static synchronized void interR(int src, double delay) {
        Event e = new Event(Event.IR, clock + delay, src);
        future.add_event(e);

    }
    // Used to send an event from one entity to another

    public static synchronized void send(int src, int dest, double delay, int tag, Object data) {
        if (delay < 0.0) {
            throw new Error("Sim_system: Send delay can't be negative.");
        }
        Event e = new Event(Event.SEND, clock + delay, src, dest, tag, data);
        future.add_event(e);
    }

    // Checks if events for a specific entity are present in the deferred event queue
    public static synchronized int waiting(int d, Predicate p) {
        int count = 0;
        Event event;
        ListIterator iterator = deferred.listIterator();
        while (iterator.hasNext()) {
            event = (Event) iterator.next();
            if ((event.get_dest() == d) && (p.match(event))) {
                count++;
            }
        }
        return count;
    }

    // Selects an event matching a predicate
    public static synchronized void select(int src, Predicate p) {
        Event ev = null;
        boolean found = false;
        ListIterator iterator = deferred.listIterator();
        while (iterator.hasNext()) {
            ev = (Event) iterator.next();
            if (ev.get_dest() == src) {
                if (p.match(ev)) {
                    iterator.remove();
                    found = true;
                    break;
                }
            }
        }
        if (found) {
            ((Entity) entities.get(src)).set_evbuf((Event) ev.clone());
        } else {
            ((Entity) entities.get(src)).set_evbuf(null);
        }
    }

    //
    // Private internal methods
    //
    // Processes an event
    private static void process_event(Event e) {
        int dest, src;
        Entity dest_ent;
        // Update the system's clock
        if (e.event_time() < clock) {
            throw new Error("Sim_system: Past event detected.");
        }
        clock = e.event_time();
        // Ok now process it
        switch (e.get_type()) {
            case (Event.ENULL):
                throw new Error("Sim_system: Event has a null type.");
            case (Event.SEND):
                // Check for matching wait
                dest = e.get_dest();
                if (dest < 0) {
                    throw new Error("Sim_system: Attempt to send to a null entity detected.");
                } else {
                    dest_ent = (Entity) entities.get(dest);
                    deferred.add_event(e);
                }
                break;
            case (Event.CS):
                src = e.get_src();
                if (src < 0) {
                    throw new Error("Sim_system: Null entity holding.");
                } else {
                    ((Entity) entities.get(src)).setCSFlag(false);
                }
                break;
            case (Event.IR):
                src = e.get_src();
                if (src < 0) {
                    throw new Error("Sim_system: Null entity holding.");
                } else {
                    ((Entity) entities.get(src)).setInterR(true);
                }
                break;
        }
    }

    /**
     * Internal method used to start the simulation. This method should
     * <b>not</b> be used by user simulations.
     */
    public static void run_start() {
        running = true;
        // Start all the entities' threads
        int entities_size = entities.size();
        for (int i = 0; i < entities_size; i++) {
            ((Entity) entities.get(i)).run();
        }
        print_message("Entities started.");
    }

    /**
     * Check if the simulation is still running. This method should be used by
     * entities to check if they should continue executing.
     *
     * @return <code>true</code> if the simulation is still * * * * * running,
     * <code>false</code> otherwise
     */
    public static synchronized boolean running() {
        return running;
    }

    /**
     * Start the simulation running. This should be called after all the
     * entities have been setup and added, and their ports linked.
     */
    public static void run() {
        while (incomplete) {
            if (!running) {
                run_start();
            }

            if (run_tick()) {
                break;
            }
            end_current_run();
        }
        run_stop();
    }

    /**
     * Internal method that allows the entities to terminate. This method should
     * <b>not</b> be used in user simulations.
     */
    public static void end_current_run() {

        if (incomplete) {
            // A termination condition of interval accuracy is being used
            running = true;
        } else {
            // Allow all entities to exit their body method
            Entity ent;
            int entities_size = entities.size();
            for (int i = 0; i < entities_size; i++) {
                ent = (Entity) entities.get(i);
                if (ent.get_state() != Entity.FINISHED) {
                    ent.restart();
                }
            }
            // Wait for all entities to complete
            for (int i = 0; i < entities_size; i++) {
                onecompleted.p();
            }
        }
    }

    // Used by entities to signal to Sim_system that they have completed
    static void completed() {
        paused();
        onecompleted.v();
    }

    // Prints a message about the progress of the simulation
    private static void print_message(String message) {
        if (Configuration.simLibMessage) {
            System.out.println(message);
        }
    }

    // METHODS USED BY SIM_ANIM
    /**
     * Internal method used to check if the simulation has completed. This
     * method is used for animation purposes and should <b>not</b> be used in
     * user simulations.
     *
     * @return
     */
    public static boolean incomplete() {
        return incomplete;
    }

    // Adds a trace message
    static synchronized void trace(int src, String msg) {
        trcout.println(msg);
    }

    static synchronized void hopTraceInternal(int src, String msg) {
        hopProcessTrace.println(msg);
    }

    static synchronized void finalTraceInternal(int src, String msg) {
        finalSendTrace.println(msg);
    }

    static synchronized void animationTraceInternal(int src, String msg) {
        animationTrace.println(msg);
    }

    static synchronized void ent_trace(int src, String msg) {
        trace(src, msg);
    }

    static synchronized void hopTrace(int src, String msg) {
        hopTraceInternal(src, msg);
    }

    static synchronized void finalTrace(int src, String msg) {
        finalTraceInternal(src, msg);
    }

    static synchronized void animationTrace(int src, String msg) {
        animationTraceInternal(src, msg);
    }

    public static void resetAll() {
        Core.incomplete = true; // Flag for telling whether a simulation is complete or not
        Core.checkIfNothingHappening = 0;
        if (Core.running) {
            future.clear();
            deferred.clear();
            entities.clear();
            clock = 0.0;
            onestopped = new Semaphore(0);
            onecompleted = new Semaphore(0);
        }
    }
}
