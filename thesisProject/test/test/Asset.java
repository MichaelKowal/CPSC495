/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author IDontKnow
 */
public class Asset {

    public static void main(String[] args) {
        Asset a = new Asset();
        a.acquireFoo(0);
    }

    public void acquireFoo(int id) {
        
        if (id > 50) {
            System.out.println("Value is "+id);
        } else if (id > 0) {
            System.out.println("Value is "+id);
        }
        assert id != 0;
    }
}
