package unbc.ca.distributed.simDistributed;

import java.rmi.*;

/**
 * Interface for the Sim_system class. Methods in this interface may be called
 * from Remote objects.
 */
public interface Dist_Sim_systemIF
        extends java.rmi.Remote {

    /**
     * This version of initialise is used by the standard animation package
     * <tt>eduni.simanim</tt>.
     *
     * @param out Reference to <tt>Sim_anim</tt> object.
     * @param sim The thread the simulation is running under.
     * @exception RemoteException If a communication failure occurs.
     */
    /**
     * Get the current simulation time.
     *
     * @return The simulation time.
     * @exception RemoteException If a communication failure occurs.
     */
    double clock() throws RemoteException;

    /**
     * A different name for <tt>Sim_system.clock()</tt>.
     *
     * @return The current simulation time.
     * @exception RemoteException If a communication failure occurs.
     */
    double sim_clock() throws RemoteException;

    /**
     * Get the current number of entities in the simulation.
     *
     * @return A count of the entities.
     * @exception RemoteException If a communication failure occurs.
     */
    int get_num_entities() throws RemoteException;

    /**
     * Get the current trace level (initially <tt>0xff</tt>, which controls
     * trace output.
     *
     * @return The trace level flags.
     * @exception RemoteException If a communication failure occurs.
     */
    int get_trc_level() throws RemoteException;

    /**
     * Find an entity by its id number.
     *
     * @param id The entity's unique id number.
     * @return A reference to the entity, or null if it could not be found.
     * @exception RemoteException If a communication failure occurs.
     */
    Dist_Sim_entityIF get_entity(int id) throws RemoteException;

    /**
     * Find an entity by its name.
     *
     * @param name The entity's name.
     * @return 
     * @exception RemoteException If a communication failure occurs.
     */
    Dist_Sim_entityIF get_entity(String name) throws RemoteException;

    /**
     * @param name The entity's name.
     * @return The entity's unique id number, or -1 if it could not be found.
     * @exception RemoteException If a communication failure occurs.
     */
    int get_entity_id(String name) throws RemoteException;

    /**
     * Set the trace level flags which control trace output.
     *
     * @param level The new flags.
     * @exception RemoteException If a communication failure occurs.
     */
    void set_trc_level(int level) throws RemoteException;

    /**
     * Switch the auto trace messages on and off.
     *
     * @param on If <tt>true</tt> then the messages are switched on.
     * @exception RemoteException If a communication failure occurs.
     */
    void set_auto_trace(boolean on) throws RemoteException;

    /**
     * Add a new entity to the simulation, before it starts.
     *
     * @param e A reference to the new entity.
     * @exception RemoteException If a communication failure occurs.
     */
    void add(Dist_Sim_entityIF e) throws RemoteException;

    /**
     * Add a new entity to the simulation, when the simulation is running.
     *
     * @param e A reference to the new entity.
     * @exception RemoteException If a communication failure occurs.
     */
    void add_entity_dynamically(Dist_Sim_entityIF e) throws RemoteException;

    /**
     * Start the simulation running.
     *
     * @exception RemoteException If a communication failure occurs.
     */
    void run() throws RemoteException;

    /**
     * Get a reference to the trace output object. This is used by SimJava
     * animations.
     *
     * @return Reference to the Sim_anim object.
     * @exception RemoteException If a communication failure occurs.
     */
    /**
     * Called by an entity just before it becomes non-RUNNABLE.
     *
     * @exception RemoteException If a communication failure occurs.
     */
    void paused() throws RemoteException;

    /**
     * Allows an entity to notify the Sim_system that it is delaying for
     * <tt>delay</tt> units of simulation time.
     *
     * @param src The unique id number of the entity that is delaying.
     * @param delay The amount of time for the delay.
     * @exception RemoteException If a communication failure occurs.
     */
    void hold(int src, double delay) throws RemoteException;

    /**
     * Allows one entity to send an event to another entity.
     *
     * @param src The unique id number of the sending entity.
     * @param dest The unique id number of the destination entity.
     * @param delay How long from the current simulation time the event should
     * be sent.
     * @param tag A user-defined number representing the type of event.
     * @param data A reference to data to be sent with the event.
     * @exception RemoteException If a communication failure occurs.
     */
    void send(int src, int dest, double delay, int tag,
            Object data) throws RemoteException;

    void CS(int src, double delay) throws RemoteException;

    void interR(int src, double delay) throws RemoteException;

    /**
     * Allows an entity to notify the Sim_system that an entity is waiting for
     * an event.
     *
     * @param src The unique id number of the entity waiting for the event.
     * @exception RemoteException If a communication failure occurs.
     */
    void wait(int src) throws RemoteException;

    /**
     * Print a message to the tracefile.
     *
     * @param src The unique id number of the entity requesting that a message
     * be written to the trace file.
     * @param msg The message to be written to the trace file.
     * @exception RemoteException If a communication failure occurs.    
     */
    void trace(int src, String msg) throws RemoteException;

    void hopTrace(int src, String msg) throws RemoteException;

    void finalTrace(int src, String msg) throws RemoteException;

    void animationTrace(int src, String msg) throws RemoteException;

    /**
     * Allows an entity to ask the Sim_system how many events matching a
     * predicate are waiting for it on the deferred queue.
     *
     * @param d The unique id number of the entity making the request.
     * @param p The event selection predicate.
     * @return The number of events that are waiting for the entity that match
     * the selection predicate.
     * @exception RemoteException If a communication failure occurs.
     */
    int waiting(int d, Dist_Sim_predicate p) throws RemoteException;

    /**
     * Allows an entity to notify the Sim_system that it is waiting for an event
     * from the deferred queue, matched by the predicate <tt>p</tt>.
     *
     * @param src The unique id number of the entity making the request.
     * @param p An event selection predicate.
     * @exception RemoteException If a communication failure occurs.
     */
    void select(int src, Dist_Sim_predicate p) throws RemoteException;

    /**
     * Put an event back to the deferred queue.
     *
     * @param ev The event to reinsert.
     * @exception RemoteException If a communication failure occurs.
     */
    void putback(Dist_Sim_eventIF ev) throws RemoteException;

    /**
     * Called by each entity when the entity is initialized and ready for
     * execution. Each entity must make this call for the simulation to start
     * execution.
     *
     * @exception RemoteException If a communication failure occurs.
     */
    void readyToRun() throws RemoteException;
}  // Sim_systemIF
