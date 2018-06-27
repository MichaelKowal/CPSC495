package unbc.ca.distributed.simDistributed;

import java.rmi.*;

/**
 * Interface for the Sim_event class.  Methods in this interface may
 * be called from Remote objects.
 */
public interface Dist_Sim_eventIF
       extends Remote
{

   /**
    * Integer indicating a null event type.
    */
   final int ENULL     = 0;

   /**
    * Integer indicating a send event type.
    */
   final int SEND      = 1;

   /**
    * Integer indicating a hold_done event type.
    */
   final int HOLD_DONE = 2;

   /**
    * Integer indicating a create event type.
    */
   final int CREATE    = 3;
   
         final int CS = 4;
    final int IR = 5;


   /**
    * Get the unique id number of the entity which received this event.
    * @return The id number.
    * @exception RemoteException if a communication failure occurs.
    */
   int get_dest() throws RemoteException;

   /**
    * Get the unique id number of the entity which scheduled this event.
    * @return The id number.
    * @exception RemoteException if a communication failure occurs.
    */
   int get_src() throws RemoteException;

   /**
    * Get the simulation time that this event was scheduled.
    * @return The simulation time.
    * @exception RemoteException if a communication failure occurs.
    */
   double event_time() throws RemoteException;

   /**
    * Get the user-defined tag in this event.
    * @return The tag.
    * @exception RemoteException if a communication failure occurs.
    */
   int get_tag() throws RemoteException;

   /**
    * Get the data passed in this event.
    * @return A reference to the data.
    * @exception RemoteException if a communication failure occurs.
    */
   Object get_data() throws RemoteException;

   /**
    * Get the type of the event.
    * @return The type of the event -- ENULL, SEND, HOLD_DONE, or CREATE.
    * @exception RemoteException if a communication failure occurs.
    */
   int get_type() throws RemoteException;

   /**
    * Copy the contents of an event.
    * @param ev The event to copy.
    * @exception RemoteException if a communication failure occurs.
    */
   void copy(Dist_Sim_eventIF ev) throws RemoteException;

}  // Sim_eventIF
