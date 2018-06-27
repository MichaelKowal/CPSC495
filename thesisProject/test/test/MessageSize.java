/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.IOException;
import java.io.Serializable;
import unbc.ca.distributed.message.Msg;
import unbc.ca.distributed.message.TimeLogical;

/**
 *
 * @author behnish
 */
public class MessageSize {

    public static void main(String[] args) throws IOException {
        TimeLogical t = new TimeLogical(1, 1);
        Msg m = new Msg(1, t);
        m.setContent("534534534534gfgfgfffffffffffffffffffffffffffffffff34tggdgerqgqrgdtghtrytryhtuukiuawfrqtyrjur7uey7rwegerjghkuo8p5eadhdhdfhdgfghdgdf"
                + "23423423423423qqqqgfhgfhdghsgyu345hshtgjdghkkoidhretrjuetyjrqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
      
        System.out.println(sizeOf(m)+" bits");
    }

    static int sizeOf(Serializable obj) {
        try {
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            new java.io.ObjectOutputStream(baos).writeObject(obj);
            return baos.size();
        } catch (IOException ignore) {
        }
        return 0;
    }
}