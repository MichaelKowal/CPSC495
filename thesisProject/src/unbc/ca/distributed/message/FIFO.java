/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.message;

/**
 *
 * @author behnish
 */
public class FIFO {

    ListNode first, last;

    private class ListNode {

        int receiver;

        ListNode next;

        ListNode(int receiver) {

            this.receiver = receiver;

            next = null;

        }

    } // ListNode

    public FIFO() {

        first = null;

        last = null;

    }

    public void addToFIFO(int receiver) {

        ListNode newNode = new ListNode(receiver);

        if (first == null) {

            last = newNode;

            first = last;

        } else {

            last.next = newNode;

            last = newNode;

        }

    } // addToFIFO

// returns id of the next receiver or -1 if there is no
    public int getNextReceiver() {

        int result;

        if (first != null) {

            result = first.receiver;

            first = first.next;

        } else {

            result = -1;

        }

        return result;

    } // getNextReceiver

} // FIFO
