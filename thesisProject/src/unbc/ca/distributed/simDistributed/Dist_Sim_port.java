/* Sim_port.java
 */

package unbc.ca.distributed.simDistributed;


// MITRE Modification
import java.rmi.*;

/**
 * This class represents ports which are used to connect entities for
 * event passing.
 *
 * <p>
 * <b>Modifications:</b>
 * <ul type=disc>
 * <li>Extends UnicastRemoteObject
 * <li>Implements Sim_portIF
 * </ul>
 *
 * @see         Sim_system
 * @version     1.0, 4 September 1996
 * @author      Ross McNab
 */

public class Dist_Sim_port 
       extends java.rmi.server.UnicastRemoteObject
       implements Dist_Sim_portIF
{
  // Private data members
  private String pname;      // The port's name
  private String dest_ename; // The destination entity's name
  private int srce;          // Index of the source entity
  private int deste;         // Index of the destination entity

  //
  // Public library interface
  //

  // Constructors
  //public Sim_port(String d) {
  //  dest_ename = d;
  //  deste = -1;
  //}
  //public Sim_port(int d) {
  //  dest_ename = null;
  //  deste = d;
  //}
  /**
   * Constructor, for stand-alone simulations.
   *
   * <p>
   * <b>Modifications:</b>
   * <ul type=disc>
   * <li>Throws exception RemoteException
   * </ul>
   *
   * @param port_name The name to identify this port
   * @return A new instance of the class Sim_port
   * @exception RemoteException If a communication failure occurs.
   */
  public Dist_Sim_port(String port_name) 
         throws RemoteException
  {
    pname = port_name;
    dest_ename = null;
    srce = -1;
    deste = -1;
  }
  // Anim constructor
  /**
   * Constructor for use with the eduni.simanim package for animations.
   *
   * <p>
   * <b>Modifications:</b>
   * <ul type=disc>
   * <li>Additional parameters: applet and appletHost
   * <li>Throws exception RemoteException
   * </ul>
   *
   * @param port_name The name to identify this port
   * @param image_name The name of the gif graphics file to use for this port's
   *                   icon, (without the ".gif" extension)
   * @param side Which side of the parent entity the port should be drawn on,
   *             one of Anim_port.LEFT, Anim_port.RIGHT, Anim_port.TOP, or
   *             Anim_port.BOTTOM.
   * @param pos How many pixels along that side the port should be drawn.
   * @param applet The applet that this port will be displayed on.
   * @param appletHost The name of the host that the applet is running on.
   *                   This is used to determine if the port that this port
   *                   is connected to is remote or local.
   * @return A new instance of the class Sim_port
   * @exception RemoteException If a communication failure occurs.
   */
  public Dist_Sim_port(String port_name, String image_name, int side, int pos,
                   String appletHost) 
         throws RemoteException
  {
    pname = port_name;
    dest_ename = null;
    srce = -1;
    deste = -1;
  }

  // Public access methods
  /** Get the name of the destination entity of this port.
   *
   * <p>
   * <b>Modifications:</b>
   * <ul type=disc>
   * <li>Throws exception RemoteException
   * </ul>
   *
   * @return The name of the entity
   * @exception RemoteException If a communication failure occurs.
   */
  public String get_dest_ename()
         throws RemoteException
  {
    return dest_ename;
  }

  /** Get the name of this port.
   *
   * <p>
   * <b>Modifications:</b>
   * <ul type=disc>
   * <li>Throws exception RemoteException
   * </ul>
   *
   * @return The name
   * @exception RemoteException If a communication failure occurs.
   */
  public String get_pname()
         throws RemoteException
  {
    return pname;
  }

  /**
   * Get the unique id number of the destination entity of this port.
   *
   * <p>
   * <b>Modifications:</b>
   * <ul type=disc>
   * <li>Throws exception RemoteException
   * </ul>
   *
   * @return The id number
   * @exception RemoteException If a communication failure occurs.
   */
  public int get_dest() 
         throws RemoteException
  { 
    return deste; 
  }

  /** Get the unique id number of the source entity of this port.
   *
   * <p>
   * <b>Modifications:</b>
   * <ul type=disc>
   * <li>Throws exception RemoteException
   * </ul>
   *
   * @return The id number
   * @exception RemoteException If a communication failure occurs.
   */
  public int get_src()
         throws RemoteException
  {
    return srce;
  }

  /**
   * Get the animation port associated with this port.
   *
   * <p>
   * <b>Modifications:</b>
   * <ul type=disc>
   * <li>Throws exception RemoteException
   * <li>Method made public (was package method)
   * </ul>
   *
   * @return The animation port.
   * @exception RemoteException if a communication failure occurs.
   */
 
  /**
   * Set the unique id number of the source entity of this port.
   *
   * <p>
   * <b>Modifications:</b>
   * <ul type=disc>
   * <li>Throws exception RemoteException
   * <li>Method made public (was package method)
   * </ul>
   *
   * @param s The source entity id number.
   * @exception RemoteException if a communication failure occurs.
   */
  public void set_src(int s) 
       throws RemoteException
  { 
    srce = s; 
  }

  /**
   * Connect this port for this entity to a port belonging to another entity.
   *
   * <p>
   * <b>Modifications:</b>
   * <ul type=disc>
   * <li>Throws exception RemoteException
   * <li>Method made public (was package method)
   * </ul>
   *
   * @param dest The destination entity to make the connection to.
   * @exception RemoteException if a communication failure occurs.
   */
  public void connect(Dist_Sim_entityIF dest) 
       throws RemoteException
  {
    dest_ename = dest.get_name();
    deste = dest.get_id();
  }

  // Set and return the destination entity id for this port
  //int connect(int s) {
  //  /* Initialise deste from destename */
  //  srce = s;
  //  if(deste==-1) {
  //    if(dest_ename != null)
  //      deste = Sim_system.get_entity_id(dest_ename);
  //  }
  //  return deste;
  //}

  //void set_pname(String pn) { pname = pn; }
}
