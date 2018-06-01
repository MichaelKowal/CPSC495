/* Sim_event.java
 */

package unbc.ca.distributed.simDistributed;

// MITRE Modification
import java.rmi.*;

/**
 * This class represents events which are passed between the entities
 * in the simulation.
 *
 * <p>
 * <b>Modifications:</b>
 * <ul type=disc>
 * <li>Extends UnicastRemoteObject
 * <li>Implements Sim_eventIF
 * </ul>
 *
 * @see         Sim_system
 * @version     1.0, 4 September 1996
 * @author      Ross McNab
 */

public class Dist_Sim_event 
       extends java.rmi.server.UnicastRemoteObject
       implements Cloneable, Dist_Sim_eventIF
{
  // Private data members
  private int etype;      // internal event type
  private double time;    // sim time event should occur
  private int ent_src;    // id of entity who scheduled event
  private int ent_dst;    // id of entity event will be sent to
  private int tag;        // the user defined type of the event
  private Object data;    // any data the event is carrying

  //
  // Public library interface
  //

  // Internal event types
  static final int ENULL = 0;
  static final int SEND = 1;
  static final int HOLD_DONE = 2;
  static final int CREATE = 3;
      static final int CS = 4;
    static final int IR = 5;


  // Constructors
  /** Contructor, create a blank event. Usefull for fetching events
   * using methods such as <tt>Sim_entity.sim_wait(ev)</tt>.
   *
   * <p>
   * <b>Modifications:</b>
   * <ul type=disc>
   * <li>Throws exception RemoteException
   * </ul>
   *
   * @return A blank instance of the class Sim_event
   * @exception RemoteException If a communication failure occurs.
   */
  public Dist_Sim_event() 
         throws RemoteException
  {
    etype = ENULL;
    this.time = -1.0;
    ent_src = -1;
    ent_dst = -1;
    this.tag = -1;
    data = null;
  }

  // Package level constructors
  Dist_Sim_event(int evtype, double time, int src,
            int dest, int tag, Object edata)
           throws RemoteException
  {
    etype = evtype;
    this.time = time;
    ent_src = src;
    ent_dst = dest;
    this.tag = tag;
    data = edata;
  }
  Dist_Sim_event(int evtype, double time, int src)
           throws RemoteException
  {
    etype = evtype;
    this.time = time;
    ent_src = src;
    ent_dst = -1;
    this.tag = -1;
    data = null;
  }

  // Public access methods
  /**
   * Get the unique id number of the entity which recieved this event.
   *
   * <p>
   * <b>Modifications:</b>
   * <ul type=disc>
   * <li>Throws exception RemoteException
   * </ul>
   *
   * @return the id number
   * @exception RemoteException If a communication failure occurs.
   */
  public int get_dest() 
         throws RemoteException
  { 
    return ent_dst; 
  }

  /**
   * Get the unique id number of the entity which scheduled this event.
   *
   * <p>
   * <b>Modifications:</b>
   * <ul type=disc>
   * <li>Throws exception RemoteException
   * </ul>
   *
   * @return the id number
   * @exception RemoteException If a communication failure occurs.
   */
  public int get_src() 
         throws RemoteException
  { 
    return ent_src; 
  }

  /**
   * Get the simulation time that this event was scheduled.
   *
   * <p>
   * <b>Modifications:</b>
   * <ul type=disc>
   * <li>Throws exception RemoteException
   * </ul>
   *
   * @return The simulation time
   * @exception RemoteException If a communication failure occurs.
   */
  public double event_time() 
         throws RemoteException
  { 
    return time; 
  }

  /** Get the user-defined tag in this event
   * @return The tag
   */
  public int type() { return tag; }             // The user defined type
  /** Get the unique id number of the entity which scheduled this event.
   * @return the id number
   */
  public int scheduled_by() { return ent_src; }

  /**
   * Get the user-defined tag in this event.
   *
   * <p>
   * <b>Modifications:</b>
   * <ul type=disc>
   * <li>Throws exception RemoteException
   * </ul>
   *
   * @return The tag
   * @exception RemoteException If a communication failure occurs.
   */
  public int get_tag()
         throws RemoteException
  { 
    return tag; 
  }

  /**
   * Get the data passed in this event.
   *
   * <p>
   * <b>Modifications:</b>
   * <ul type=disc>
   * <li>Throws exception RemoteException
   * </ul>
   *
   * @return A reference to the data
   * @excpetion RemoteException If a communication failure occurs.
   */
  public Object get_data() 
         throws RemoteException
  {
    return data;
  }
  /** Determine if the event was sent from a given port.
   * @param p The port to test
   * @return <tt>true</tt> if the event was scheduled through the port
   */
  public boolean from_port(Dist_Sim_port p)
  { 
    // MITRE Modification
    try {
      return (get_src()==p.get_dest()); 
    } catch (RemoteException re) {
      System.out.println("Sim_event.from_port error: " + re.getMessage());
      re.printStackTrace();
    }  // Try catch
    //  Following statement is never reached
    return false;
  }
  // Public modifying methods
  /** Create an exact copy of this event.
   * @return A reference to the copy
   */
  public Object clone() 
  {
    // MITRE Modification
    try {
      return new Dist_Sim_event(etype, time, ent_src, ent_dst, tag, data);
    } catch (RemoteException re) {
      System.out.println("Sim_event.clone error: " + re.getMessage());
      re.printStackTrace();
    }  // Try catch
    //  Following statement is never reached -- required to compile
    return null;
  }
  /** Set the source entity of this event.
   * @param s The unique id number of the entity
   */
  public void set_src(int s) { ent_src = s; }
  /** Set the destination entity of this event.
   * @param d The unique id number of the entity
   */
  public void set_dest(int d) { ent_dst = d; }


  /**
   * Get the type of the event.
   *
   * <p>
   * <b>Modifications:</b>
   * <ul type=disc>
   * <li>Throws exception RemoteException
   * <li>Method made public (was package method).
   * </ul>
   *
   * @return The type of the event -- ENULL, SEND, HOLD_DONE, or CREATE.
   * @exception RemoteException if a communication failure occurs.
   */
  public int get_type()
         throws RemoteException 
  { 
    return etype; // The internal type
  }

  /**
   * Copy the contents of an event.
   *
   * <p>
   * <b>Modifications:</b>
   * <ul type=disc>
   * <li>Throws exception RemoteException
   * <li>Method made public (was package method).
   * </ul>
   *
   * @param ev The event to copy.
   * @exception RemoteException if a communication failure occurs.
   */
  public void copy(Dist_Sim_eventIF ev) 
         throws RemoteException
  {
    ent_dst = ev.get_dest();
    ent_src = ev.get_src();
    time = ev.event_time();
    etype = ev.get_type();
    tag = ev.get_tag();
    data = ev.get_data();
  }
}
