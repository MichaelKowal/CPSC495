/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.library;

import java.util.Map;

/**
 *
 * @author behnish Original Idea from DAJ library
 */
public abstract class Assertion {

    public abstract boolean test(Map<Integer, Algorithm> algorithms);

    public String getText() {
        return "(no information)";
    }
}
