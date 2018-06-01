/* Semaphore.java
 */

package unbc.ca.distributed.simDistributed;

/**
 * This is a counting semaphore class. It is used internally by the
 * Sim_system to
 * synchronise the running of the threaded Sim_entities. It should not
 * be needed in user simulations.
 * @see		eduni.simjava.Sim_system
 * @version     1.0, 4 September 1996
 * @author      Ross McNab
 */

public class Dist_Semaphore {
  private int count;

  // Constructors
  /**
   * Allocate a new Semaphore object, with an initial count of zero.
   */
  public Dist_Semaphore() {
    count = 1;
  }

  /**
   * Allocates a new Semaphore object, with a given initial count.
   * @param count	The initial count of the semaphore.
   */
  public Dist_Semaphore(int count) {
    this.count = count;
  }

  /**
   * Try to obtain the Semaphore. If the count is above zero, the
   * function decrements it, then return so the calling thread can
   * continue. If the count is zero then the calling thread is suspended
   * until it becomes non-zero.
   */
  public synchronized void p() {
    while (count == 0) {
      try { wait(); } catch (InterruptedException e) { }
    }
    count--;
  }

  /**
   * Free the Semaphore, by incrementing the internal count.
   */
  public synchronized void v() {
    count++;
    notify();
  }
}
