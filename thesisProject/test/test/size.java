/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author behnish
 */
public class size {

    public static void main(String[] args) throws UnsupportedEncodingException {
        String s = "s";
        byte[] b = s.getBytes(StandardCharsets.UTF_8);
        System.out.println(b);
    }
}