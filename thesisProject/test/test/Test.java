/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author behnish
 */
public class Test {

    public static void main(String[] args) {
        Map<String, Double> map = new HashMap<String, Double>();
        map.put("1.1", 1.1);
        map.put("0.1", 0.1);
        map.put("2.1", 2.1);

        Double min = Collections.min(map.values());
        System.out.println(min); // 0.1
    }
}
