package unbc.ca.distributed.simDistributed;

import java.rmi.*;

/**
 * Interface for the Sim_port class.  Methods in this interface may
 * be called from Remote objects.
 */
public interface Dist_Sim_portIF
       extends Remote
{
   /**
    * Get the name of the destination entity of this port.
    * @return The name of the entity.
    * @exception RemoteException If a communication failure occurs.
    */
   String get_dest_ename() throws RemoteException;

   /**
    * Get the name of this port.
    * @return The name.
    * @exception RemoteException If a communication failure occurs.
    */
   String get_pname() throws RemoteException;

   /**
    * Get the unique id number of the destination entity of this port.
    * @return The id number.
    * @exception RemoteException if a communication failure occurs.
    */
   int get_dest() throws java.rmi.RemoteException;

   /**
    * Get the unique id number of the source entity of this port.
    * @return The id number.
    * @exception RemoteException if a communication failure occurs.
    */
   int get_src() throws java.rmi.RemoteException;

   /**
    * Get the animation port associated with this port.
    * @return The animation port.
    * @exception RemoteException if a communication failure occurs.
    */
//   Anim_portIF get_aport() throws java.rmi.RemoteException;

   /**
    * Set the unique id number of the source entity of this port.
    * @param s The source entity id number.
    * @exception RemoteException if a communication failure occurs.
    */
   void set_src(int s) throws java.rmi.RemoteException;

   /**
    * Connect this port for this entity to a port belonging to another entity.
    * @param dest The destination entity to make the connection to.
    * @exception RemoteException if a communication failure occurs.
    */
   void connect(Dist_Sim_entityIF dest) throws java.rmi.RemoteException;

}  // Sim_portIF
