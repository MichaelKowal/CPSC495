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

public class LyHudakMsg extends Message {

    protected int senderNode, receiverNode;

    public LyHudakMsg(int sender, int receiver) {

        this.senderNode = sender;

        this.receiverNode = receiver;

    }

    public String getText() {

        return "Message from : " + senderNode + " to : "
                + receiverNode;

    }  

    public int getSenderNode() {
        return senderNode;
    }

    public int getReceiverNode() {
        return receiverNode;
    }    
} // Msg

