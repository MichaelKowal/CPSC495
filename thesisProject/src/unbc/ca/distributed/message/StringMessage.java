/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.message;

/**
 *
 * @author behnish
 */
public class StringMessage extends Message
{
    
   private String message;   
    
    public StringMessage(String msg)
    {
        message = msg;  
        setContent();
    }
    
    public String getMessage() {
        return message;
    }
    
    @Override
    public String toString() {
        return message.toString();
    }    

    @Override
    public StringMessage clone() {
        StringMessage c = new StringMessage(this.getMessage());
        
        c.setClock(this.getClock());
        c.setFinalReceiver(this.getFinalReceiver());
        c.setFinalSender(this.getFinalSender());
        c.setHopCount(this.getHopCount());
        
        c.setParent(this.getParent());
        c.setReceiver(this.getReceiver());
        c.setSender(this.getSender());
        return c;
    }   
    private void setContent()
    {
        super.setContent(message);
    }
}
