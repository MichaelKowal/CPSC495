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
public class RaymondMsg extends Message
{
    public static final int REQUEST = 1;
    public static final int PREVILIGE = 5;

    public int type;    

    public RaymondMsg(int type) {
        this.type = type;
        setContent();
    }

    private void setContent() {
        super.setContent(String.valueOf(type));
    }
}
