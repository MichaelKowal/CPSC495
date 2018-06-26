/* Sim_entity.java
 */

package unbc.ca.distributed.simDistributed;

import java.util.Vector;
import java.util.Enumeration;
import java.rmi.*;

/**
 * This class represents entities, or processes, running in the system.
 * To create a new type of entity, it should be <tt>extended</tt> and
 * a definition for the <tt>body()</tt> method given. The <tt>body()</tt>
 * method is called by the <tt>Sim_system</tt> and defines the behaviour of
 * the entity during the simulation. <p>
 * The methods with names starting with the prefix <tt>sim_</tt> are
 * runtime methods, and should only be called from within the entity's
 * <tt>body()</tt> method.
 * 
 * <p>
 * <b>Modifications:</b>
 * <ul type=disc>
 * <li>Extends UnicastRemoteObject
 * <li>Implements Sim_entityIF and Runnable
 * </ul>
 *
 *
 * @see         Dist_Sim_system
 * @version     0.1, 25 June 1995
 * @author      Ross McNab
 */

public class Dist_Sim_entity 
       extends java.rmi.server.UnicastRemoteObject
       implements  Dist_Sim_entityIF, Runnable
{
  // Private data members
  private String name;       // The entities name
  private int me;            // Unique id
  private Dist_Sim_eventIF evbuf; // For incoming events
  private int state;         // Our current state from list below
  private Dist_Semaphore restart; // Used by Sim_system to schedule us
  private Vector ports;      // Our outgoing ports

  // MITRE Modification
  private Dist_Sim_systemIF simSystem;  // Reference to Sim_system object

   private boolean flag = false;
    private boolean interR = true;

    public boolean isInterR() {
        return interR;
    }

  @Override
    public void setInterR(boolean interR) {
        this.interR = interR;
    }
        
    public boolean isFlag() {
        return flag;
    }

  @Override
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    public void enterCS(double delay) throws RemoteException {
        
        simSystem.CS(me, delay);
    }
    public void interRequest(double delay) throws RemoteException {
       
        simSystem.interR(me, delay);
    }
  //
  // Public library interface
  //

  // Public constructor
  /** The standard constructor.
   * <p>
   *
   * <b>Modifications:</b>
   * <ul type=disc>
   * <li>Additional parameter: simSys
   * <li> Throws exception RemoteException
   * </ul>
   *
   * @param name The name to be associated with this entity
   * @param simSys Reference to Sim_system object.
   * @return A new instance of the class Sim_entity
   * @exception RemoteException If a communication failure occurs.
   */
  public Dist_Sim_entity(String name, Dist_Sim_systemIF simSys) 
         throws RemoteException
  {
    this.name = name;
    me = -1;
    state = RUNNABLE;
    restart = new Dist_Semaphore(0);
    ports = new Vector();
    // Now a hacky setDaemon so that if entities are the only active
    // threads in a simulation then the runtime will exit.
    // Saves having to explicitly kill still active entity threads
    // at the end of the simulation
    //this.setDaemon(true); FIXED

    // MITRE Corporation addition.
    simSystem = simSys;
  }

  // Anim constructor
  /** The constructor for use with the eduni.simanim animation package.
   *<p>
   *
   * <b>Modifications:</b>
   * <ul type=disc>
   * <li>Additional parameters:  simSys, canvas, and applet
   * <li>Throws exception RemoteException
   * </ul>
   *
   * @param name The name to be associated with this entity
   * @param image_name The name of the gif image file for this entity's
   *                   icon, (without the .gif extension).
   * @param x The X co-ordinate at which the entity should be drawn
   * @param y The Y co-ordinate at which the entity should be drawn
   * @param simSys Reference to Sim_system object.
   * @param canvas Reference to Anim_canvas object.  In this implementation
   *               of the "distributed Simjava", the canvas that an entity
   *               draws itself on can not be a remote object.
   * @param applet Reference to Anim_applet object.  This is passed to the
   *               Anim_entity when it is created.
   * @exception RemoteException If a communication failure occurs.
   */
  public Dist_Sim_entity(String name, String image_name, int x, int y,
                    Dist_Sim_systemIF simSys
                    ) 
         throws RemoteException
  {
    this.name = name;
    me = -1;
    state = RUNNABLE;
    restart = new Dist_Semaphore(0);
    ports = new Vector();
    // Now a hacky setDaemon so that if entities are the only active
    // threads in a simulation then the runtime will exit.
    // Saves having to explicitly kill still active entity threads
    // at the end of the simulation
    //this.setDaemon(true); FIXED
    // Now anim stuff

    // MITRE Corporation modification
    simSystem = simSys;
  }

  // Public access methods
  /** 
   * Get the name of this entity.
   * <p>
   *
   * <b>Modifications:</b>
   * <ul type=disc>
   * <li>Throws exception RemoteException
   * </ul>
   *
   * @return The entity's name
   * @exception RemoteException If a communication failure occurs.
   */
  public String get_name() 
         throws RemoteException
  {
    return name; 
  }

  /**
   * Get the unique id number assigned to this entity.
   * <p>
   *
   * <b>Modifications:</b>
   * <ul type=disc>
   * <li>Throws exception RemoteException
   * </ul>
   *
   * @return The id number
   * @exception RemoteException If a communication failure occurs.
   */
  public int get_id() 
         throws RemoteException
  {
    return me; 
  }

  // Search for the port that the event came from
  /** Search through this entity's ports, for the one which sent this event.
   * <p>
   *
   * <b>Modifications:</b>
   * <ul type=disc>
   * <li>Throws exception RemoteException
   * </ul>
   *
   * @param ev The event
   * @return A reference to the port which sent the event, or null if
   *         it could not be found.
   * @exception RemoteException If a communication failure occurs.
   */
  public Dist_Sim_portIF get_port(Dist_Sim_eventIF ev) 
         throws RemoteException
  {
    Dist_Sim_port found = null, curr;
    Enumeration e;
    for (e = ports.elements(); e.hasMoreElements();) {
      curr = (Dist_Sim_port)e.nextElement();
      if(ev.get_src() == curr.get_dest())
        found = curr;
    }
    return found;
  }
  // Search for a port by name
  /** Search through this entity's ports, for one called <tt>name</tt>.
   * <p>
   *
   * <b>Modifications:</b>
   * <ul type=disc>
   * <li>Throws exception RemoteException
   * </ul>
   *
   * @param name The name of the port to search for
   * @return A reference to the port, or null if it could not be found
   * @exception RemoteException If a communication failure occurs.
   */
  public Dist_Sim_portIF get_port(String name) 
         throws RemoteException
  {
    Dist_Sim_port found = null, curr;
    Enumeration e;
    for (e = ports.elements(); e.hasMoreElements();) {
      curr = (Dist_Sim_port)e.nextElement();
      if(name.compareTo(curr.get_pname()) == 0)
        found = curr;
    }
    if(found == null)
      System.out.println("Sim_entity: could not find port "+name+
                         " on entity "+this.name);
    return found;
  }

  // Public update methods
  /** Add a port to this entity.
   * @param port A reference to the port to add
   */
  public void add_port(Dist_Sim_port port) {
    ports.addElement(port);

    // MITRE Modification
    try {
      port.set_src(this.me);
     
    } catch (RemoteException re) {
      System.out.println("Sim_entity.add_port error: " + re.getMessage());
      re.printStackTrace();
    }  // Try catch
  }
  /** Add a parameter to this entity.
   * Used with the eduni.simanim package for animation.
   * @param param A reference to the parameter to add
   */
  
  // The body function which should be overidden
  /** The method which defines the behavior of the entity. This method
   * should be overidden in subclasses of Sim_entity.
   */
  public void body() {
    System.out.println("Entity "+name+" has no body().");
  }

  // Runtime methods
  /** Causes the entity to hold for <tt>delay</tt> units of simulation time.
   * @param delay The amount of time to hold
   */
  public void sim_hold(double delay) {
    // MITRE Modification
    try {
      simSystem.hold(me,delay);
      /* Pause me now */
      simSystem.paused();
    } catch (RemoteException re) {
      System.out.println("Sim_entity.sim_hold error: " + re.getMessage());
      re.printStackTrace();
    }  // Try catch
    restart.p();
  }
  /** Write a trace message.
   * @param level The level at which the trace should be printed, used
   *              with <tt>Sim_system.set_trc_level()</tt> to control
   *              what traces are printed
   * @param msg The message to be printed
   */
  public void sim_trace(int level, String msg) {
    //  MITRE Modification
    try {
      if((level & simSystem.get_trc_level()) != 0) {
        simSystem.trace(me, msg);
      }
    } catch (RemoteException re) {
      System.out.println("Sim_entity.sim_trace error: " + re.getMessage());
      re.printStackTrace();
    }  // Try catch

  }
   public void hopTrace(int level, String msg) throws RemoteException {
      if((level & simSystem.get_trc_level()) != 0) {
            simSystem.hopTrace(me, msg);
        }
    }
     public void finalTrace(int level, String msg) throws RemoteException {
        if ((level & simSystem.get_trc_level()) != 0) {
            simSystem.finalTrace(me, msg);
        }
    }
      public void animationTrace(int level, String msg) throws RemoteException {
        if ((level & simSystem.get_trc_level()) != 0) {
            simSystem.animationTrace(me, msg);
        }
    }

  // The schedule functions
  /** Send an event to another entity, by id number with data.
   * @param dest The unique id number of the destination entity
   * @param delay How long from the current simulation time the event
   *              should be sent
   * @param tag An user-defined number representing the type of event.
   * @param data A reference to data to be sent with the event.
   */
  public void sim_schedule(int dest, double delay, int tag, Object data) {
    // MITRE Modification
    try {
      simSystem.send(me, dest, delay, tag, data);
    } catch (RemoteException re) {
      System.out.println("Sim_entity.sim_schedule error: " + re.getMessage());
      re.printStackTrace();
    }  // Try catch
  }
  /** Send an event to another entity, by id number and with <b>no</b> data.
   * @param dest The unique id number of the destination entity
   * @param delay How long from the current simulation time the event
   *              should be sent
   * @param tag An user-defined number representing the type of event.
   */
  public void sim_schedule(int dest, double delay, int tag){
    // MITRE Modification
    try {
      simSystem.send(me, dest, delay, tag, null);
    } catch (RemoteException re) {
      System.out.println("Sim_entity.sim_schedule error: " + re.getMessage());
      re.printStackTrace();
    }  // Try catch
  }
  /** Send an event to another entity, by a port reference with data.
   * @param dest A reference to the port to send the event out of
   * @param delay How long from the current simulation time the event
   *              should be sent
   * @param tag An user-defined number representing the type of event.
   * @param data A reference to data to be sent with the event.
   */
  public void sim_schedule(Dist_Sim_port dest, double delay, int tag, Object data) {
    // MITRE Modification
    try {
      simSystem.send(me, ((Dist_Sim_portIF)dest).get_dest(), delay, tag, data);
    } catch (RemoteException re) {
      System.out.println("Sim_entity.sim_schedule error: " + re.getMessage());
      re.printStackTrace();
    }  // Try catch
  }
  /** Send an event to another entity, by a port reference with <b>no</b> data.
   * @param dest A reference to the port to send the event out of
   * @param delay How long from the current simulation time the event
   *              should be sent
   * @param tag An user-defined number representing the type of event.
   */
  public void sim_schedule(Dist_Sim_port dest, double delay, int tag) {
    // MITRE Modification
    try {
      simSystem.send(me, ((Dist_Sim_portIF)dest).get_dest(), delay, tag, null);
    } catch (RemoteException re) {
      System.out.println("Sim_entity.sim_schedule error: " + re.getMessage());
      re.printStackTrace();
    }  // Try catch
  }
  /** Send an event to another entity, by a port name with data.
   * @param dest The name of the port to send the event out of
   * @param delay How long from the current simulation time the event
   *              should be sent
   * @param tag An user-defined number representing the type of event.
   * @param data A reference to data to be sent with the event.
   */
  public void sim_schedule(String dest, double delay, int tag, Object data) {
    // MITRE Modification
    try {
      simSystem.send(me, get_port(dest).get_dest(), delay, tag, data);
    } catch (RemoteException re) {
      System.out.println("Sim_entity.sim_schedule error: " + re.getMessage());
      re.printStackTrace();
    }  // Try catch
  }
  /** Send an event to another entity, by a port name with <b>no</b> data.
   * @param dest The name of the port to send the event out of
   * @param delay How long from the current simulation time the event
   *              should be sent
   * @param tag An user-defined number representing the type of event.
   */
  public void sim_schedule(String dest, double delay, int tag) {
    // MITRE Modification
    try {
      simSystem.send(me, get_port(dest).get_dest(), delay, tag, null);
    } catch (RemoteException re) {
      System.out.println("Sim_entity.sim_schedule error: " + re.getMessage());
      re.printStackTrace();
    }  // Try catch
  }


  /** Hold until the entity recieves an event.
   * @param ev   The event recieved is copied into <tt>ev</tt> if
   *             it points to an blank event, or discarded if <tt>ev</tt> is
   *             <tt>null</tt>
   */
  public void sim_wait(Dist_Sim_event ev) {
    // MITRE Modification
    try {
      simSystem.wait(me);
      simSystem.paused();
    } catch (RemoteException re) {
      System.out.println("Sim_entity.sim_wait error: " + re.getMessage());
      re.printStackTrace();
    }  // Try catch
    restart.p();
    // I think this bit is fairly non-standard and hacky:
    // If they passed us a null event ref then just drop the new event
    // Otherwise copy the new event's values into the one they passed us
    try {
      if(((Dist_Sim_eventIF)ev != null) && (evbuf != null)) 
        ((Dist_Sim_eventIF)ev).copy(evbuf);
    } catch (RemoteException re) {
      System.out.println("Sim_entity.sim_wait error: " + re.getMessage());
      re.printStackTrace();
    }  // Try catch
  }
  /** Count how many events matching a predicate are waiting
   * for this entity on the deferred queue.
   * @param p The event selection predicate
   * @return The count of matching events
   */
  public int sim_waiting(Dist_Sim_predicate p)
  {
    // MITRE Modification
    try {
      return simSystem.waiting(me, p);
    } catch (RemoteException re) {
      System.out.println("Sim_entity.sim_waiting error: " + re.getMessage());
      re.printStackTrace();
      return 0;
    }  // Try catch
  }
  /** Count how many events are waiting of this entity on the deferred queue
   * @return The count of events
   */
  public int sim_waiting()
  {
    // MITRE Modification
    try {
      return simSystem.waiting(me, Dist_Sim_system.SIM_ANY);
    } catch (RemoteException re) {
      System.out.println("Sim_entity.sim_waiting error: " + re.getMessage());
      re.printStackTrace();
      return 0;
    }  // Try catch
  }
  /** Extract the first event waiting for this entity on the deferred
   * queue, matched by the predicate <tt>p</tt>.
   * @param p An event selection predicate
   * @param ev   The event matched is copied into <tt>ev</tt> if
   *             it points to a blank event, or discarded if <tt>ev</tt> is
   *             <tt>null</tt>
   */
  public void sim_select(Dist_Sim_predicate p, Dist_Sim_event ev) {
    // MITRE Modification
    try {
      simSystem.select(me, p);
      if((ev != null) && (evbuf != null)) ev.copy(evbuf);
    } catch (RemoteException re) {
      System.out.println("Sim_entity.sim_select error: " + re.getMessage());
      re.printStackTrace();
    }  // Try catch
  }
  /** Repeatedly <tt>sim_wait()</tt> until the entity receives an event
   * matched by a predicate, all other received events
   * are discarded.
   * @param p The event selection predicate
   * @param ev   The event matched is copied into <tt>ev</tt> if
   *             it points to a blank event, or discarded if <tt>ev</tt> is
   *             <tt>null</tt>
   */
  public void sim_wait_for(Dist_Sim_predicate p, Dist_Sim_event ev) {
    boolean matched = false;
    while(!matched) {
      sim_wait(ev);
      if (p.match(ev)) matched = true;
      else sim_putback(ev);
    }
  }

  /** Put an event back on the defered queue.
   * @param ev The event to reinsert
   */
  public void sim_putback(Dist_Sim_event ev)
  {
    try {
      simSystem.putback(ev);
    } catch (RemoteException re) {
      System.out.println("Sim_entity.sim_putback error: " + re.getMessage());
      re.printStackTrace();
    }  // Try catch
  }

  /** Get the first event matching a predicate from the deferred queue,
   * or, if none match, wait for a matching event to arrive.
   * @param p The predicate to match
   * @param ev   The event matched is copied into <tt>ev</tt> if
   *             it points to a blank event, or discarded if <tt>ev</tt> is
   *             <tt>null</tt>
   */
  public void sim_get_next(Dist_Sim_predicate p, Dist_Sim_event ev) {
    if (sim_waiting(p) > 0)
      sim_select(p, ev);
    else 
      sim_wait_for(p,ev);
  }
  /** Get the first event from the deferred queue waiting on the entity,
   * or, if there are none, wait for an event to arrive.
   * @param ev   The event matched is copied into <tt>ev</tt> if
   *             it points to a blank event, or discarded if <tt>ev</tt> is
   *             <tt>null</tt>
   */
  public void sim_get_next(Dist_Sim_event ev) {
    sim_get_next(Dist_Sim_system.SIM_ANY, ev);
  }
  /** Get the id of the currently running entity
   * @return A unique entity id number
   */
  public int sim_current() {
    // MITRE Modification
    try {
      return this.get_id();
    } catch (RemoteException re) {
      System.out.println("Sim_entity.sim_current error: " + re.getMessage());
      re.printStackTrace();
    }  // Try catch
    // This statement can't be reached -- required to compile.
    return 0;
  }
  /** Send on an event to an other entity, through a port.
   * @param ev A reference to the event to send
   * @param p A reference to the port through which to send
   */
  public void send_on(Dist_Sim_event ev, Dist_Sim_port p) {
    // MITRE Modification
    try {
      sim_schedule(p.get_dest(), 0.0, ev.type(), ev.get_data());
    } catch (RemoteException re) {
      System.out.println("Sim_entity.send_on error: " + re.getMessage());
      re.printStackTrace();
    }  // Try catch
  }


  /**
   * Return the state of the entity.
   * <p>
   *
   * <b>Modifications:</b>
   * <ul type=disc>
   * <li>Throws exception RemoteException
   * <li>Method made public (was package method)
   * </ul>
   *
   * @return The entity's state -- RUNNABLE, WAITING, HOLDING, or FINISHED.
   * @exception RemoteException If a communication failure occurs.
   */
  public int get_state() 
      throws RemoteException
  { 
    return state; 
  }

  /**
   * Restart the entity.
   * <p>
   *
   * <b>Modifications:</b>
   * <ul type=disc>
   * <li>Throws exception RemoteException
   * <li>Method made public (was package method)
   * </ul>
   *
   * @exception RemoteException If a communication failure occurs.
   */
  public void restart() 
       throws RemoteException
  { 
    restart.v(); 
  }

  /**
   * Set the state of this entity.
   * <p>
   *
   * <b>Modifications:</b>
   * <ul type=disc>
   * <li>Throws exception RemoteException
   * <li>Method made public (was package method).
   * </ul>
   *
   * @param state The state to assign to the entity.
   * @exception RemoteException If a communication failure occurs.
   */
  public void set_state(int state) 
       throws RemoteException
  {
    this.state = state; 
  }


   /**
    * Set the entity's id.
    * <p>
    *
    * <b>Modifications:</b>
    * <ul type=disc>
   * <li>Throws exception RemoteException
   * <li>Method made public (was package method).
    * </ul>
    *
    * @param id The id to be assigned to the entity.
    * @exception RemoteException If a communication failure occurs.
    */
  public void set_id(int id) 
       throws RemoteException
  {
    me = id; 
  }


  /**
   * Pass an event to this entity.
   * <p>
   *
   * <b>Modifications:</b>
   * <ul type=disc>
   * <li>Throws exception RemoteException
   * <li>Method made public (was package method).
   * </ul>
   *
   * @param e The event to be passed to the entity.
   * @exception RemoteException if a communication failure occurs.
   */
  public void set_evbuf(Dist_Sim_eventIF e)
       throws RemoteException
  { 
    evbuf = e; 
  }


  /**
   * Kill the entity.
   * <p>
   *
   * <b>Modifications:</b>
   * <ul type=disc>
   * <li>Throws exception RemoteException
   * <li>Method made public (was package method).
   * </ul>
   *
   * @exception RemoteException If a communication failure occurs.
   */
  public void poison() 
       throws RemoteException
  {
    restart.v();

    // MITRE Modification:
    // Find the entity whose name = this.name and stop it.
    // Kill Thread with name "KeepAlive" so RMI "dies" also.
    Thread[] tArray = new Thread[Thread.currentThread().activeCount()];
    Thread.enumerate(tArray);
    for (int i = 0; i < tArray.length; i++) {
      if (tArray[i].getName().equals(name) || 
          tArray[i].getName().equals("KeepAlive")) {
        state = FINISHED;
        simSystem.paused();
        tArray[i].stop();
        break;
      }  // If
    }  // For
  }

  /**
   * Create a Thread of control to run this entity in.  Start the Thread
   * of control.
   * <p>
   *
   * <b>Modifications:</b>
   * <ul type=disc>
   * <li>New method
   * </ul>
   *
   * @exception RemoteException If a communication failure occurs.
   */
  public void start()
         throws RemoteException
  {
    new Thread(this, name).start();
  }  // start


  //
  // Package level methods
  //

  // Package access methods
  Dist_Sim_eventIF get_evbuf() { return evbuf; }

  // The states
  static final int RUNNABLE = 0;
  static final int WAITING  = 1;
  static final int HOLDING  = 2;
  static final int FINISHED = 3;

  // Package update methods
  void set_going() { restart.v(); } // FIXME same as restart, both needed?


  /** Internal method - don't overide */
  public final void run() {
    // Connect all our ports to their destination entities
    //Sim_port curr;
    //Enumeration e;
    //for (e = ports.elements(); e.hasMoreElements();) {
    //  ((Sim_port)e.nextElement()).connect(me);
    //}
    // MITRE Modification
    try {
      simSystem.paused(); // Tell the system we're up and running
      restart.p(); // initially we pause 'till we get the go ahead from system
      body();
      state = FINISHED;
      simSystem.paused();
    } catch (RemoteException re) {
      System.out.println("Sim_entity.run error: " + re.getMessage());
      re.printStackTrace();
    }  // Try catch
  }


}
