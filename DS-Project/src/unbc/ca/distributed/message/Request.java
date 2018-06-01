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

public class Request extends LyHudakMsg {

    public Request(int sender, int receiver) {

        super(sender, receiver);

    }

    public String getText() {

        return "Request from : " + senderNode + " to : "
                + receiverNode;

    }

} // Request
