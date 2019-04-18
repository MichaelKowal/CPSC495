package unbc.ca.distributed.simDistributed;

import java.rmi.*;

/**
 * Interface for the Sim_entity class.  Methods in this interface may
 * be called by Remote objects.
 */
public interface Dist_Sim_entityIF
       extends Remote
{

   /**
    * Integer that indicates that this entity is in a RUNNING state.
    */
   int RUNNABLE = 0;

   /**
    * Integer that indicates that this entity is in a WAITING state.
    */
   int WAITING  = 1;

   /**
    * Integer that indicates that this entity is in a HOLDING state.
    */
   int HOLDING  = 2;

   /**
    * Integer that indicates that this entity is in a FINISHED state.
    */
   int FINISHED = 3;

   /**
    * Get the name of this entity.
    * @return The entity's name
    * @exception RemoteException If a communication failure occurs.
    */
   String get_name() throws RemoteException;

   /**
    * Get the unique id number assigned to this entity.
    * @return The id number.
    * @exception RemoteException If a communication failure occurs.
    */
   int get_id() throws RemoteException;

   /**
    * Search through this entity's ports, for the one which sent this event.
    * @param ev The event.
    * @return A reference to the port which sent the event, or null if
    *         it could not be found.
    * @exception RemoteException If a communication failure occurs.
    */
   Dist_Sim_portIF get_port(Dist_Sim_eventIF ev) throws RemoteException;

   /**
    * Search through this entity's ports, for one called <tt>name</tt>.
    * @param name The name of the port to search for.
    * @return A reference to the port, or null if it could not be found.
    * @exception RemoteException If a communication failure occurs.
    */
   Dist_Sim_portIF get_port(String name) throws RemoteException;

   /**
    * Return the state of the entity.
    * @return The entity's state -- RUNNABLE, WAITING, HOLDING, or FINISHED.
    * @exception RemoteException If a communication failure occurs.
    */
   int get_state() throws RemoteException;

   /**
    * Restart the entity.
    * @exception RemoteException If a communication failure occurs.
    */
   void restart() throws RemoteException;

   /**
    * Start the entity in a Thread of control.
    * @exception RemoteException If a communication failure occurs.
    */
   void start() throws RemoteException;

   /**
    * Set the state of this entity.
    * @param state The state to assign to the entity.
    * @exception RemoteException If a communication failure occurs.
    */
   void set_state(int state) throws RemoteException;

   /**
    * Set the entity's id.
    * @param id The id to be assigned to the entity.
    * @exception RemoteException If a communication failure occurs.
    */
   void set_id(int id) throws RemoteException;

   /**
    * Pass an event to this entity.
    * @param e The event to be passed to the entity.
    * @exception RemoteException If a communication failure occurs.
    */
   void set_evbuf(Dist_Sim_eventIF e) throws RemoteException;

   /**
    * Kill the entity.
    * @exception RemoteException If a communication failure occurs.
    */
   void poison() throws RemoteException;

    void setFlag(boolean b) throws RemoteException;

    void setInterR(boolean b) throws RemoteException;
   
}  // Sim_entityIF
