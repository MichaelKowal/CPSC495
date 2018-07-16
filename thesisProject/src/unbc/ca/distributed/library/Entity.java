package unbc.ca.distributed.library;

import java.util.ArrayList;
import java.util.List;
import org.github.com.jvec.JVec;
/*
 * @see Sim_event
 * @see Sim_stat
 * @see Sim_system
 * @version 0.2, 11 July 2002
 * @author Costas Simatos
 */
public abstract class Entity {

    // Private data members
    private String name;       // The entitys name
    private int me;            // The entitys id
    private Event evbuf;   // For incoming events
    private int state;         // The entity's current state
    private Semaphore restart; // Used by Sim_system to schedule the entity
    private Semaphore reset;   // Used by Sim_system to reset the simulation
    private List ports;        // The entitys outgoing ports

    // The entity states
    static final int RUNNABLE = 0;
    static final int WAITING = 1;
    static final int HOLDING = 2;
    static final int FINISHED = 3;

    private boolean csflag = false;
    private boolean interR = true;

    public boolean isInterR() {
        return interR;
    }

    public void setInterR(boolean interR) {
        this.interR = interR;
    }

    public boolean isCSFlag() {
        return csflag;
    }

    public void setCSFlag(boolean flag) {
        this.csflag = flag;
    }

    /**
     * Creates a new entity.
     *
     * @param name The name to be associated with this entity
     */
    public Entity(String name) {
        //super(name);
        if (name.indexOf(" ") != -1) {
            throw new Error("Sim_entity: Entity names can't contain spaces.");
        }
        this.name = name;
        //setName(name);
        me = -1;
        state = RUNNABLE;
        restart = new Semaphore(0);
        reset = new Semaphore(0);
        ports = new ArrayList();
        // Add this to Sim_system automatically        
        Core.add(this);
    }

    /**
     * Get the name of this entity
     *
     * @return The entity's name
     */
    public String get_name() {
        return name;
    }

    /**
     * Get the unique id number assigned to this entity
     *
     * @return The id number
     */
    public int get_id() {
        return me;
    }

    /**
     * Get the port through which an event arrived.
     *
     * @param ev The event
     * @return The port which sent the event or <code>null</code> if it could
     * not be found
     */
    public Port get_port(Event ev) {
        Port curr;
        int size = ports.size();
        for (int i = 0; i < size; i++) {
            curr = (Port) ports.get(i);
            if (ev.get_src() == curr.get_dest()) {
                return curr;
            }
        }
        return null;
    }

    /**
     * Get the port with a given name.
     *
     * @param name The name of the port to search for
     * @return The port or <code>null</code> if it could not be found
     */
    public Port get_port(String name) {
        Port curr;
        int size = ports.size();
        for (int i = 0; i < size; i++) {
            curr = (Port) ports.get(i);
            if (name.compareTo(curr.get_pname()) == 0) {
                return curr;
            }
        }
        System.out.println("Sim_entity: could not find port " + name
                + " on entity " + this.name);
        return null;
    }

    /**
     * Add a port to this entity.
     *
     * @param port The port to add
     */
    public void add_port(Port port) {
        //   Anim_port aport;
        ports.add(port);
        port.set_src(this.me);
    }

    /**
     * Define a <code>Sim_stat</code> object for this entity.
     *
     * @param param The <code>Sim_stat</code> object
     */
    /**
     * Get the entity's <code>Sim_stat</code> object.
     *
     * @return The <code>Sim_stat</code> object defined for this entity * * or
     * <code>null</code> if none is defined.
     */
    /**
     * The method which defines the behaviour of the entity. This method should
     * be overridden in subclasses of Sim_entity.
     */
    public void body() {
        System.out.println("Entity " + name + " has no body().");
    }

    /**
     * Write a trace message.
     *
     * @param level The level at which the trace should be printed, used * with
     * <code>Sim_system.set_trace_level()</code> to control what traces are
     * printed
     * @param msg The message to be printed
     */
    public void sim_trace(int level, String msg) {
        Core.ent_trace(me, msg);
    }

    public void hopTrace(int level, String msg) {
        Core.hopTrace(me, msg);
    }

    public void finalTrace(int level, String msg) {
        Core.finalTrace(me, msg);
    }

    public void animationTrace(int level, String msg) {
        Core.animationTrace(me, msg);
    }

    /**
     * Signal that an event has completed service.
     *
     * @param e The event that has completed service
     */
    public void sim_completed(Event e) {
        if (!Core.running()) {
            return;
        }
    }

   

    public void enterCS(double duration) {
        if (!Core.running()) {
            return;
        }
        Core.CS(me, duration);
    }

    public void interRequest(double duration) {
        if (!Core.running()) {
            return;
        }
        Core.interR(me, duration);
    }

    /**
     * Send an event to another entity by id number and with <b>no</b> data.
     * Note that the tag <code>9999</code> is reserved.
     *
     * @param dest The unique id number of the destination entity
     * @param delay How long from the current simulation time the event should
     * be sent
     * @param tag An user-defined number representing the type of event.
     */
    public void sim_schedule(int dest, double delay, int tag) {
        if (!Core.running()) {
            return;
        }

        Core.send(me, dest, delay, tag, null);
    }

    /**
     * Send an event to another entity through a port, with data. Note that the
     * tag <code>9999</code> is reserved.
     *
     * @param dest The port to send the event through
     * @param delay How long from the current simulation time the event should
     * be sent
     * @param tag An user-defined number representing the type of event.
     * @param data The data to be sent with the event.
     */
    public synchronized void sim_schedule(Port dest, double delay, int tag, Object data) {
        if (!Core.running()) {
            return;
        }
        Core.send(me, dest.get_dest(), delay, tag, data);
    }

    /**
     * Send an event to another entity through a port, with <b>no</b> data. Note
     * that the tag <code>9999</code> is reserved.
     *
     * @param dest The port to send the event through
     * @param delay How long from the current simulation time the event should
     * be sent
     * @param tag An user-defined number representing the type of event.
     */
    public void sim_schedule(Port dest, double delay, int tag) {
        if (!Core.running()) {
            return;
        }
        Core.send(me, dest.get_dest(), delay, tag, null);
    }

    /**
     * Send an event to another entity through a port with a given name, with
     * data. Note that the tag <code>9999</code> is reserved.
     *
     * @param dest The name of the port to send the event through
     * @param delay How long from the current simulation time the event should
     * be sent
     * @param tag An user-defined number representing the type of event.
     * @param data The data to be sent with the event.
     */
    public void sim_schedule(String dest, double delay, int tag, Object data) {
        if (!Core.running()) {
            return;
        }
        Core.send(me, get_port(dest).get_dest(), delay, tag, data);
    }

    /**
     * Send an event to another entity through a port with a given name, with
     * <b>no</b> data. Note that the tag <code>9999</code> is reserved.
     *
     * @param dest The name of the port to send the event through
     * @param delay How long from the current simulation time the event should
     * be sent
     * @param tag An user-defined number representing the type of event.
     */
    public void sim_schedule(String dest, double delay, int tag) {
        if (!Core.running()) {
            return;
        }
        Core.send(me, get_port(dest).get_dest(), delay, tag, null);
    }

    /**
     * Count how many events matching a predicate are waiting in the entity's
     * deferred queue.
     *
     * @param p The event selection predicate
     * @return The count of matching events
     */
    public int sim_waiting(Predicate p) {
        return Core.waiting(me, p);
    }

    /**
     * Count how many events are waiting in the entiy's deferred queue
     *
     * @return The count of events
     */
    public int sim_waiting() {
        return Core.waiting(me, Core.SIM_ANY);
    }

    /**
     * Extract the first event matching a predicate waiting in the entity's
     * deferred queue.
     *
     * @param p The event selection predicate
     * @param ev The event matched is copied into <body>ev</body> if it points
     * to a blank event, or discarded if <code>ev</code> is <code>null</code>
     */
    public void sim_select(Predicate p, Event ev) {
        if (!Core.running()) {
            return;
        }
        Core.select(me, p);
        if ((ev != null) && (evbuf != null)) {
            ev.copy(evbuf);

        }
        evbuf = null;       // ADA MSI
    }

    /**
     * Get the first event matching a predicate from the deferred queue, or if
     * none match, wait for a matching event to arrive.
     *
     * @param p The predicate to match
     * @param ev The event matched is copied into <code>ev</code> if it points
     * to a blank event, or discarded if <code>ev</code> is <code>null</code>
     */
    public void sim_get_next(Predicate p, Event ev) {
        if (!Core.running()) {
            return;
        }
        if (sim_waiting(p) > 0) {
            sim_select(p, ev);
        } else {
            sim_wait_for(p, ev);
        }
    }

    /**
     * Get the first event waiting in the entity's deferred queue, or if there
     * are none, wait for an event to arrive.
     *
     * @param ev The event matched is copied into <code>ev</code> if it points
     * to a blank event, or discarded if <code>ev</code> is <code>null</code>
     */
    public void sim_get_next(Event ev) {
        sim_get_next(Core.SIM_ANY, ev);
    }

    /**
     * Get the id of the currently running entity
     *
     * @return The currently running entity's id number
     */
    public int sim_current() {
        return this.get_id();
    }
    

    //
    // Package level methods
    //
    // Package access methods
    int get_state() {
        return state;
    }

    public Event get_evbuf() {
        return evbuf;
    }

    // Package update methods
    void restart() {
        restart.v();
    }

    void set_going() {
        restart.v();
    }

    void set_state(int state) {
        this.state = state;
    }

    void set_id(int id) {
        me = id;
    }

    void set_evbuf(Event e) {
        evbuf = e;
    }

    

    // Used to set a cloned entity's name
    private void set_name(String new_name) {
        name = new_name;
    }

    /**
     * Executes the entity's thread. This is an internal method and should not
     * be overridden in subclasses.
     */
//    @Override
    public final void run() {
        body();
        state = FINISHED;
        Core.completed();
    }    
    /**
     * Wait for an event matching a specific predicate. This method doesn't
     * check the entity's deferred queue.
     * <p>
     * Since 2.0 <code>Sim_syztem</code> checks the predicate for the entity.
     * This avoids unnecessary context switches for non-matching events.
     *
     * @param p The predicate to match
     * @param ev The event to which the arriving event will be copied to
     */
    public void sim_wait_for(Predicate p, Event ev) {
        if (!Core.running()) {
            return;
        }
        do {
            if (!Core.running()) {
                return;
            }
        } while (evbuf == null);
        if ((ev != null) && (evbuf != null)) {
            ev.copy(evbuf);
        }
        // There in no need to check the predicate since Sim_system has done this for us
        evbuf = null;
    }
}
