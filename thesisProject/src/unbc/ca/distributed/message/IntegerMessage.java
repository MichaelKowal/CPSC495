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
public class IntegerMessage extends Message {

    public int value;

    public IntegerMessage(int value) {
        this.value = value;
        switch (value) {
            case -1:
                super.setContent("+ve ACK");
                break;
            case -2:
                super.setContent("-ve ACK");
                break;

            default:
                super.setContent("Phase "+value);
                break;

        }
    }

}
