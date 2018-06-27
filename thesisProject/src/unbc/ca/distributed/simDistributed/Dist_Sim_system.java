/* Sim_system.java
 */
package unbc.ca.distributed.simDistributed;

// MITRE Modification
import java.util.*;
import java.rmi.*;
import unbc.ca.distributed.management.Configuration;

/**
 * This is the system class which manages the simulation. All of the members of
 * this class are static, so there is no need to create an instance of this
 * class.
 *
 * <p>
 * <b>Modifications:</b>
 * <ul type=disc>
 * <li>Extends UnicastRemoteObject
 * <li>Implements Sim_systemIF
 * </ul>
 *
 * @version 1.0, 4 Spetember 1996
 * @author Ross McNab
 */
public class Dist_Sim_system
        extends java.rmi.server.UnicastRemoteObject
        implements Dist_Sim_systemIF {
    // Private data members

    static private Vector entities;  // The current entity list
    static private Dist_Evqueue future;   // The future event queue
    static private Dist_Evqueue deferred; // The deferred event queue
    static private double clock;     // Holds the current global sim time
    static private boolean running;  // Has the run() member been called yet
    static private Dist_Semaphore onestopped;
    static private int trc_level;
    static private boolean auto_trace; // Should we print auto trace messages?
    static private boolean animation;  // Are we running as an animation applet?
    static private Thread simThread;
    // MITRE Modification
    private int numberExpectedEntities;
    private int numberEntities;
    private Dist_Linker linker;
    
    static private Dist_Sim_output trcout; // The output object for trace messages    
    static Dist_Sim_output hopProcessTrace; // The output object for trace messages
    static Dist_Sim_output finalSendTrace; // The output object for trace messages
    static Dist_Sim_output animationTrace; // The output object for trace messages

    //
    // Public library interface
    //
    /**
     * Create an instance of the Sim_system class.
     *
     * <p>
     * <b>Modifications:</b>
     * <ul type=disc>
     * <li>New method - previously there was no need to create an instance of
     * Sim_system because all of its methods were static.
     * </ul>
     *
     * @param expectedEntities The number of entities that will be added to the
     * Sim_system object.
     * @exception RemoteException If a communication failure occurs.
     */
    public Dist_Sim_system(int expectedEntities, Dist_Linker linker)
            throws RemoteException {
        numberExpectedEntities = expectedEntities;
        this.linker = linker;
    }  // Sim_system

    // Initialise system
    /**
     * Initialise the system, this function should be called at the start of any
     * simulation program. It comes in several flavours depending on what
     * context the simulation is running.<P>
     * This is the simplest, and should be used for standalone simulations It
     * sets up trace output to a file in the current directory called
     * `tracefile'
     */
    public void initialise() {
        initialise(new Dist_Sim_outfile(Configuration.currentTraceFile), null);
    }

    /**
     * This version of intialise() is used by the standard animation package
     * <tt>eduni.simanim</tt>.
     *
     * <p>
     * <b>Modifications:</b>
     * <ul type=disc>
     * <li>Throws exception RemoteException
     * </ul>
     *
     * @param out Reference to <tt>Sim_anim</tt> object.
     * @param sim The thread the simulation is running under.
     * @exception RemoteException If a communication failure occurs.
     */
    /**
     * This version of initialise() is for experienced users who want to use the
     * trace output to draw a graph or an animation as part of the application.
     *
     * @param out The object to be used for trace output
     * @param sim The thread the simulation is running under, (set to
     * <tt>null</tt> if not applicable
     */
    static public void initialise(Dist_Sim_output out, Thread sim) {
        //System.out.println("Java hase V0.1 - Ross McNab");
        entities = new Vector();
        future = new Dist_Evqueue();
        deferred = new Dist_Evqueue();
        clock = 0.0;
        running = false;
        onestopped = new Dist_Semaphore(0);

        trcout = out;
        trcout.initialise();

        hopProcessTrace = new Dist_Sim_outfile(Configuration.currentTraceFile + "_hop");
        hopProcessTrace.initialise();

        finalSendTrace = new Dist_Sim_outfile(Configuration.currentTraceFile + "_final");
        finalSendTrace.initialise();

        animationTrace = new Dist_Sim_outfile(Configuration.currentTraceFile + "_animation");
        animationTrace.initialise();

        trc_level = 0xff;
        auto_trace = true;
        simThread = sim;
    }
    // The two standard predicates
    /**
     * A standard predicate that matches any event.
     */
    static public Dist_Sim_any_p SIM_ANY = new Dist_Sim_any_p();

    /**
     * A standard predicate that does not match any events.
     */
    // Public access methods
    /**
     * Get the current simulation time.
     *
     * <p>
     * <b>Modifications:</b>
     * <ul type=disc>
     * <li>Throws exception RemoteException
     * <li>No longer a static method
     * </ul>
     *
     * @return The simulation time
     * @exception RemoteException If a communication failure occurs.
     */
    public double clock()
            throws RemoteException {
        return clock;
    }

    /**
     * A different name for <tt>Sim_system.clock()</tt>.
     *
     * <p>
     * <b>Modifications:</b>
     * <ul type=disc>
     * <li>Throws exception RemoteException
     * <li>No longer a static method
     * </ul>
     *
     * @return The current simulation time
     * @exception RemoteException If a communication failure occurs.
     */
    public double sim_clock()
            throws RemoteException {
        return clock;
    }

    /**
     * Get the current number of entities in the simulation.
     *
     * <p>
     * <b>Modifications:</b>
     * <ul type=disc>
     * <li>Throws exception RemoteException
     * <li>No longer a static method
     * </ul>
     *
     * @return A count of entities.
     * @exception RemoteException If a communication failure occurs.
     */
    public int get_num_entities()
            throws RemoteException {
        return entities.size();
    }

    /**
     * Get the current trace level (initially <tt>0xff</tt>), which control
     * trace output.
     *
     * <p>
     * <b>Modifications:</b>
     * <ul type=disc>
     * <li>Throws exception RemoteException
     * <li>No longer a static method
     * </ul>
     *
     * @return The trace level flags
     * @exception RemoteException If a communication failure occurs.
     */
    public int get_trc_level()
            throws RemoteException {
        return trc_level;
    }

    /**
     * Find an entity by its id number.
     *
     * <p>
     * <b>Modifications:</b>
     * <ul type=disc>
     * <li>Throws exception RemoteException
     * <li>No longer a static method
     * <li>Return value is interface type
     * </ul>
     *
     * @param id The entity's unique id number
     * @return A reference to the entity, or null if it could not be found
     */
    public Dist_Sim_entityIF get_entity(int id)
            throws RemoteException {
        return (Dist_Sim_entityIF) entities.elementAt(id);
    }

    /**
     * Find an entity by its name.
     *
     * <p>
     * <b>Modifications:</b>
     * <ul type=disc>
     * <li>Throws exception RemoteException
     * <li>No longer a static method
     * <li>Return value is interface type
     * </ul>
     *
     * @param name The entity's name
     * @return A reference to the entity, or null if it could not be found
     * @exception RemoteException If a communication failure occurs.
     */
    public Dist_Sim_entityIF get_entity(String name)
            throws RemoteException {
        Enumeration e;
        //Dist_Sim_entityIF ent;
        Dist_Sim_entityIF ent, found = null;

        for (e = entities.elements(); e.hasMoreElements();) {
            ent = (Dist_Sim_entityIF) e.nextElement();
            // MITRE Modification
            try {
                if (name.compareTo(ent.get_name()) == 0) {
                    found = ent;
                }
            } catch (RemoteException re) {
                System.out.println("Sim_system.get_entity error: " + re.getMessage());
                re.printStackTrace();
            }  // Try catch
        }
        if (found == null) {
            System.out.println("Sim_system: could not find entity " + name);
        }
        return found;
    }

    /**
     * Find out an entities unique id number from its name.
     *
     * <p>
     * <b>Modifications:</b>
     * <ul type=disc>
     * <li>Throws exception RemoteException
     * <li>No longer a static method
     * </ul>
     *
     * @param name The entity's name
     * @return The entity's unique id number, or -1 if it could not be found
     * @exception RemoteException If a communication failure occurs.
     */
    public int get_entity_id(String name)
            throws RemoteException {
        return entities.indexOf(get_entity(name));
    }

    /**
     * Find the currently running entity.
     *
     * @return A reference to the entity, or null if none are running
     */
    static public Dist_Sim_entityIF current_ent() {
        return (Dist_Sim_entityIF) Thread.currentThread();
        //Sim_entity ent, found = null;
        //Enumeration e;
        //for (e = entities.elements(); e.hasMoreElements();) {
        //  ent = (Sim_entity)e.nextElement();
        //  if(ent.is_running())
        //    found = ent;
        //return found;
    }

    // Public update methods
    /**
     * Set the trace level flags which control trace output.
     *
     * <p>
     * <b>Modifications:</b>
     * <ul type=disc>
     * <li>Throws exception RemoteException
     * <li>No longer a static method
     * </ul>
     *
     * @param level The new flags
     * @exception RemoteException If a communication failure occurs.
     */
    public void set_trc_level(int level)
            throws RemoteException {
        trc_level = level;
    }

    /**
     * Switch the auto trace messages on and off.
     *
     * <p>
     * <b>Modifications:</b>
     * <ul type=disc>
     * <li>Throws exception RemoteException
     * <li>No longer a static method
     * </ul>
     *
     * @param on If <tt>true</tt> then the messages are switched on
     * @exception RemoteException If a communication failure occurs.
     */
    public void set_auto_trace(boolean on)
            throws RemoteException {
        auto_trace = on;
    }

    /**
     * Add a new entity to the simulation, before it starts.
     *
     * <p>
     * <b>Modifications:</b>
     * <ul type=disc>
     * <li>Throws exception RemoteException
     * <li>No longer a static method
     * <li>Parameter is interface type
     * </ul>
     *
     * @param e A reference to the new entity
     * @exception RemoteException If a communication failure occurs.
     */
    public void add(Dist_Sim_entityIF e)
            throws RemoteException {
        Dist_Sim_event evt;
        if (running()) {
            /* Post an event to make this entity */
            evt = new Dist_Sim_event(Dist_Sim_event.CREATE, clock, current_ent().get_id(), 0, 0, e);
            future.add(evt);
        } else {
            e.set_id(entities.size());
            entities.addElement(e);
        }
    }

    /**
     * Add a new entity to the simulation, when the simulation is running.
     *
     * <p>
     * <b>Modifications:</b>
     * <ul type=disc>
     * <li>Throws exception RemoteException
     * <li>No longer a static method
     * <li>Parameter is interface type
     * </ul>
     *
     * @param e A reference to the new entity
     * @exception RemoteException If a communication failure occurs.
     */
    public void add_entity_dynamically(Dist_Sim_entityIF e)
            throws RemoteException {
        // MITRE Modification
        try {
            e.set_id(entities.size());
            entities.addElement(e);
            e.start();
            onestopped.p();
        } catch (RemoteException re) {
            System.out.println("Sim_system.add_entity_dynamically error: "
                    + re.getMessage());
            re.printStackTrace();
        }  // Try catch
    }

    /**
     * Link the ports the ports on two entities, so that events secheduled from
     * one port are sent to the other.
     *
     * <p>
     * <b>Modifications:</b>
     * <ul type=disc>
     * <li>Method no longer static
     * </ul>
     *
     * @param ent1 The name of the first entity
     * @param port1 The name of the port on the first entity
     * @param ent2 The name of the second entity
     * @param port2 The name of the port on the second entity
     */
    public void link_ports(String ent1, String port1, String ent2, String port2) {
        // MITRE Modification
        Dist_Sim_portIF p1, p2;
        Dist_Sim_entityIF e1, e2;
        try {
            e1 = (Dist_Sim_entityIF) get_entity(ent1);
            e2 = (Dist_Sim_entityIF) get_entity(ent2);
            p1 = e1.get_port(port1);
            p2 = e2.get_port(port2);
            p1.connect(e2);
            p2.connect(e1);
        } catch (RemoteException re) {
            System.out.println("Sim_system.link_ports error: " + re.getMessage());
        }  // Try catch

    }

    /**
     * Start the simulation running. This should be called after all the
     * entities have been setup and added, and their ports linked.
     *
     * <p>
     * <b>Modifications:</b>
     * <ul type=disc>
     * <li>Simjava applications no longer have to call run. When all expected
     * entities have made the call to <tt>readyToRun</tt>, <tt>run</tt>
     * will be called. Simanim animations must still make the call to run to
     * begin the simulation.
     * <li>Throws exception RemoteException
     * </ul>
     *
     * @exception RemoteException If a communication failure occurs.
     */
    public void run()
            throws RemoteException {
        Dist_Sim_event ev;
        Enumeration e;
        Dist_Sim_entityIF ent;
        int i, num_started;

        running = true;
        // Start all the entities' threads
        System.out.println("Sim_system: Starting entities");
        // MITRE Modifications
        for (e = entities.elements(); e.hasMoreElements();) {
            ent = (Dist_Sim_entityIF) e.nextElement();
            try {
                ent.start();
            } catch (RemoteException re) {
                System.out.println("Sim_system.run error: " + re.getMessage());
            }  // Try catch statement
        }
        System.out.println("Sim_system: Waiting for entities to startup");
        // Wait 'till they're all up and ready
        for (i = 0; i < entities.size(); i++) {
            onestopped.p();
        }

        // Now the main loop
        System.out.println("Sim_system: Entering main loop");
        while (running) {
            // Restart all the entities which are RUNNABLE
            //System.out.println("Sim_system: Restarting RUNNABLE entities");
            num_started = 0;
            // MITRE Modifications
            for (e = entities.elements(); e.hasMoreElements();) {
                ent = (Dist_Sim_entityIF) e.nextElement();
                try {
                    if (ent.get_state() == Dist_Sim_entityIF.RUNNABLE) {
                        ent.restart();
                        num_started++;
                    }
                } catch (RemoteException re) {
                    System.out.println("Sim_system.run error: " + re.getMessage());
                }  // Try catch statement
            }
            // Wait for them all to halt
            //System.out.println("Sim_system: Waiting for "+num_started+" entities to halt");
            for (i = 0; i < num_started; i++) {
                onestopped.p();
            }

            // Give everything else a chance
            if (simThread != null) {
                try {
                    simThread.sleep(5);
                } catch (InterruptedException except) {
                }
            }

            // If there are more future events then deal with them
            //System.out.println("Sim_system: Looking for future event");
            if (future.size() > 0) {
                process_event(future.pop());
            } else {
                running = false;
                System.out.println("Sim_system: No more future events");
            }
        }
        // Attempt to kiiiillll all the enitity threads
        for (e = entities.elements(); e.hasMoreElements();) {
            ent = (Dist_Sim_entityIF) e.nextElement();
            // MITRE Modification
            try {
                ent.poison();
            } catch (RemoteException re) {
                System.out.println("Sim_system.run error: " + re.getMessage());
                re.printStackTrace();
            }  // Try catch
        }
        System.out.println("Exiting Sim_system.run()");
        trcout.close();
        hopProcessTrace.close();
        finalSendTrace.close();
        animationTrace.close();
    }

    //
    // Package level methods
    //
    static boolean running() {
        return running;
    }

    /**
     * Get a reference to the trace output object. This is used by SimJava
     * animations.
     *
     * <p>
     * <b>Modifications:</b>
     * <ul type=disc>
     * <li>Throws exception RemoteException
     * <li>No longer static method
     * <li>Method made public (was package method)
     * </ul>
     *
     * @return Reference to the Sim_anim object.
     * @exception RemoteException if a communication failure occurs.
     */
    // Entity service methods
    // Called by an entity just before it become non-RUNNABLE
    /**
     * Called by an entity just before it becomes non-RUNNABLE.
     *
     * <p>
     * <b>Modifications:</b>
     * <ul type=disc>
     * <li>Throws exception RemoteException
     * <li>No longer static method
     * <li>Method made public (was package method)
     * </ul>
     *
     * @exception RemoteException if a communication failure occurs.
     */
    public void paused()
            throws RemoteException {
        onestopped.v();
    }

    /**
     * Allows an entity to notify the Sim_system that it is delaying for
     * <tt>delay</tt> units of simulation time.
     *
     * <p>
     * <b>Modifications:</b>
     * <ul type=disc>
     * <li>Throws exception RemoteException
     * <li>No longer static method
     * <li>Method made public (was package method)
     * </ul>
     *
     * @param src The unique id number of the entity that is delaying.
     * @param delay The amount of time for the delay.
     * @exception RemoteException if a communication failure occurs.
     */
    public void hold(int src, double delay)
            throws RemoteException {
        Dist_Sim_event e = new Dist_Sim_event(Dist_Sim_event.HOLD_DONE, clock + delay, src);
        future.add(e);
        ((Dist_Sim_entityIF) entities.elementAt(src)).set_state(Dist_Sim_entityIF.HOLDING);
        if (auto_trace) {
            //trace(src,"sim_hold starting");
        }
    }

    /**
     * Allows one entity to send an event to another entity.
     *
     * <p>
     * <b>Modifications:</b>
     * <ul type=disc>
     * <li>Throws exception RemoteException
     * <li>No longer static method
     * <li>Method made public (was package method)
     * </ul>
     *
     * @param src The unique id number of the sending entity.
     * @param dest The unique id number of the destination entity.
     * @param delay How long from the current simulation time the event should
     * be sent.
     * @param tag A user-defined number representing the type of event.
     * @param data A reference to data to be sent with the event.
     * @exception RemoteException if a communication failure occurs.
     */
    @Override
    public void send(int src, int dest, double delay, int tag, Object data)
            throws RemoteException {
        Dist_Sim_event e = new Dist_Sim_event(Dist_Sim_event.SEND, clock + delay,
                src, dest, tag, data);
        if (auto_trace) {
//      trace(src, "scheduling event type "+tag+" for "+
//                 ((Dist_Sim_entityIF)entities.elementAt(dest)).get_name()+
//                 " with delay "+delay);
        }
        future.add(e);
    }

    @Override
    public synchronized void CS(int src, double delay) throws RemoteException {
        Dist_Sim_event e = new Dist_Sim_event(Dist_Sim_event.CS, clock + delay, src);
        future.add(e);
        if (auto_trace) {
            //trace(src, "Adding critical section time");
        }
    }
    // For inter Request entry

    @Override
    public synchronized void interR(int src, double delay) throws RemoteException {
        Dist_Sim_event e = new Dist_Sim_event(Dist_Sim_event.IR, clock + delay, src);
        future.add(e);
        if (auto_trace) {
            //trace(src, "Adding interRequest time");
        }
    }

    /**
     * Allows an entity to notify the Sim_system that an entity is waiting for
     * an event.
     *
     * <p>
     * <b>Modifications:</b>
     * <ul type=disc>
     * <li>Throws exception RemoteException
     * <li>No longer static method
     * <li>Method made public (was package method)
     * </ul>
     *
     * @param src The unique id number of the entity waiting for the event.
     * @exception RemoteException if a communication failure occurs.
     */
    @Override
    public void wait(int src)
            throws RemoteException {
        ((Dist_Sim_entityIF) entities.elementAt(src)).set_state(Dist_Sim_entityIF.WAITING);
        if (auto_trace) {
            trace(src, "waiting for an event");
        }
    }

    /**
     * Print a message to the tracefile.
     *
     * <p>
     * <b>Modifications:</b>
     * <ul type=disc>
     * <li>Throws exception RemoteException
     * <li>No longer static method
     * <li>Method made public (was package method)
     * </ul>
     *
     * @param src The unique id number of the entity requesting that a message
     * be written to the trace file.
     * @param msg The message to be written to the trace file.
     * @exception RemoteException if a communication failure occurs.
     */
    @Override
    public void trace(int src, String msg)
            throws RemoteException {
        //trcout.println("u:"+((Dist_Sim_entityIF)entities.elementAt(src)).get_name()+" at "+clock+": "+msg);
        trcout.println(msg);
    }
    
    @Override
    public void hopTrace(int src, String msg) {
        hopProcessTrace.println(msg);
    }

    @Override
    public void finalTrace(int src, String msg) {
        finalSendTrace.println(msg);
    }

    @Override
    public void animationTrace(int src, String msg) {
        animationTrace.println(msg);
    }

    /**
     * Allows an entity to ask the Sim_system how many events matching a
     * predicate are waiting for it on the deferred queue.
     *
     * <p>
     * <b>Modifications:</b>
     * <ul type=disc>
     * <li>Throws exception RemoteException
     * <li>No longer static method
     * <li>Method made public (was package method)
     * </ul>
     *
     * @param d The unique id number of the entity making the request.
     * @param p The event selection predicate.
     * @return The number of events that are waiting for the entity that match
     * the selection predicate.
     * @exception RemoteException if a communication failure occurs.
     */
    @Override
    public int waiting(int d, Dist_Sim_predicate p)
            throws RemoteException {
        int w = 0;
        Enumeration e;
        Dist_Sim_eventIF ev;

        for (e = deferred.elements(); e.hasMoreElements();) {
            ev = (Dist_Sim_eventIF) e.nextElement();
            // MITRE Modification
            try {
                if (ev.get_dest() == d) {
                    if (p.match((Dist_Sim_event) ev)) {
                        w++;
                    }
                }
            } catch (RemoteException re) {
                System.out.println("Sim_system.waiting error: " + re.getMessage());
            }  // Try catch
        }
        return w;
    }

    /**
     * Allows an entity to notify the Sim_system that it is waiting for an event
     * from the deferred queue, matched by the predicate <tt>p</tt>.
     *
     * <p>
     * <b>Modifications:</b>
     * <ul type=disc>
     * <li>Throws exception RemoteException
     * <li>No longer static method
     * <li>Method made public (was package method)
     * </ul>
     *
     * @param src The unique id number of the entity making the request.
     * @param p An event selection predicate.
     * @exception RemoteException if a communication failure occurs.
     */
    public void select(int src, Dist_Sim_predicate p)
            throws RemoteException {
        Enumeration e;
        Dist_Sim_event ev = null;
        boolean found = false;

        // retrieve + remove event with dest == src
        for (e = deferred.elements(); (e.hasMoreElements()) && !found;) {
            ev = (Dist_Sim_event) e.nextElement();
            if (ev.get_dest() == src) {
                if (p.match(ev)) {
                    deferred.removeElement(ev);
                    found = true;
                }
            }
        }
        if (found) {
            ((Dist_Sim_entityIF) entities.elementAt(src)).set_evbuf((Dist_Sim_event) ev.clone());
            if (auto_trace) {
//       try {
//         //trace(src,"sim_select returning (event time was "+ev.event_time()+")");
//       } catch (java.rmi.RemoteException re) {
//         System.out.println("Sim_system.select error: " + re.getMessage());
//         re.printStackTrace();
//       }  // Try catch
            }
        } else {
            ((Dist_Sim_entityIF) entities.elementAt(src)).set_evbuf(null);
            if (auto_trace) {
//       try {
//        trace(src,"sim_select returning (no event found)");
//       } catch (java.rmi.RemoteException re) {
//         System.out.println("Sim_system.select error: " + re.getMessage());
//         re.printStackTrace();
//       }  // Try catch
            }
        }
    }

    /**
     * Put an event back to the deferred queue.
     *
     * <p>
     * <b>Modifications:</b>
     * <ul type=disc>
     * <li>Throws exception RemoteException
     * <li>No longer static method
     * <li>Method made public (was package method)
     * </ul>
     *
     * @param ev The event to reinsert.
     * @exception RemoteException If a communication failure occurs.
     */
    public void putback(Dist_Sim_eventIF ev)
            throws RemoteException {
        deferred.add((Dist_Sim_event) ev);
    }

    /**
     * Called by each entity when the entity is initialized and ready for
     * execution. Each entity must make this call for the simulation to start
     * execution.
     *
     * <p>
     * <b>Modifications:</b>
     * <ul type=disc>
     * <li>New method
     * </ul>
     *
     * @exception RemoteException If a communication failure occurs.
     */
    public void readyToRun()
            throws RemoteException {
        numberEntities++;
        if (numberEntities == numberExpectedEntities) {
            numberEntities = 0;
            linker.linkPorts();
            if (!animation) {
                java.util.Date d = new java.util.Date();
                System.out.println("Start: " + d.toString());
                run();
                d = new java.util.Date();
                System.out.println("End:   " + d.toString());
                Thread[] tArray = new Thread[Thread.currentThread().activeCount()];
                Thread.enumerate(tArray);
                for (int i = 0; i < tArray.length; i++) {
                    if (tArray[i].getName().equals("KeepAlive")) {
                        tArray[i].stop();
                    }
                }  // For
            }  // If
        }  // If
    }  // readyToRun

    //
    // Private internal methods
    //
    // MITRE Modification -- no longer static
    private void process_event(Dist_Sim_event e) {
        int dest, src;
        Dist_Sim_entityIF dest_ent;

        //System.out.println("Sim_system: Processing event");
        // Update the system's clock
        // MITRE Modification
        try {
            if (((Dist_Sim_eventIF) e).event_time() < clock) {
                System.out.println("Sim_system: Error - past event detected!");
                System.out.println("Time: " + clock + ", event time: " + e.event_time()
                        + ", event type: " + e.get_type());
            }
            clock = e.event_time();
        } catch (RemoteException re) {
            System.out.println("Sim_system.process_event error: " + re.getMessage());
            re.printStackTrace();
        }  // Try catch

        // Ok now process it
        // MITRE Modification
        try {
            switch (e.get_type()) {
                case (Dist_Sim_eventIF.ENULL):
                    System.out.println("Sim_system: Error - event has a null type");
                    break;
                case (Dist_Sim_eventIF.CREATE):
                    Dist_Sim_entityIF newe = (Dist_Sim_entityIF) e.get_data();
                    add_entity_dynamically(newe);
                    break;
                case (Dist_Sim_eventIF.SEND):
                    // Check for matching wait
                    dest = e.get_dest();
                    if (dest < 0) {
                        System.out.println("Sim_system: Error - attempt to send to a null entity");
                    } else {
                        dest_ent = (Dist_Sim_entityIF) entities.elementAt(dest);
                        // MITRE Modification
                        try {
                            if (dest_ent.get_state() == Dist_Sim_entityIF.WAITING) {
                                dest_ent.set_evbuf((Dist_Sim_eventIF) e.clone());
                                dest_ent.set_state(Dist_Sim_entityIF.RUNNABLE);
                            } else {
                                deferred.add(e);
                            }
                        } catch (RemoteException re) {
                            System.out.println("Sim_system.process_event: " + re.getMessage());
                            re.printStackTrace();
                        }  // Try catch
                    }
                    break;
                case (Dist_Sim_eventIF.HOLD_DONE):
                    src = e.get_src();
                    if (src < 0) {
                        System.out.println("Sim_system: Error - NULL entity holding");
                    } else // MITRE Modification
                    {
                        try {
                            ((Dist_Sim_entityIF) entities.elementAt(src)).set_state(
                                    Dist_Sim_entityIF.RUNNABLE);
                        } catch (RemoteException re) {
                            System.out.println("Sim_system.process_event: " + re.getMessage());
                            re.printStackTrace();
                        }  // Try catch
                    }
                    break;
                case (Dist_Sim_eventIF.CS):
                    src = e.get_src();
                    if (src < 0) {
                        System.out.println("Sim_system: Null entity holding.");
                    } else {
                        ((Dist_Sim_entityIF) entities.elementAt(src)).setFlag(false);
                    }
                    break;
                case (Dist_Sim_eventIF.IR):
                    src = e.get_src();
                    if (src < 0) {
                        System.out.println("Sim_system: Null entity holding.");
                    } else {
                        ((Dist_Sim_entityIF) entities.elementAt(src)).setInterR(true);
                    }
                    break;

            }
        } catch (RemoteException re) {
            System.out.println("Sim_system.process_event error: " + re.getMessage());
        }  // Try catch   
    }    
}
