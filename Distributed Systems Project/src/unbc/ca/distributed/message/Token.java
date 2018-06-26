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
public class Token extends LyHudakMsg {

    protected FIFO map;

    public Token(int sender, int receiver, FIFO map) {

        super(sender, receiver);

        this.map = map;

    }

    public FIFO getMap() {

        return map;

    }

    public String getText() {

        return "Token from : " + senderNode + " to : "
                + receiverNode;

    }

} // Token

