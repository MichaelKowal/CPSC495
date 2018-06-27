/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.Scanner;

/**
 *
 * @author behnish
 */
class group {

    public static void main(String arng[]) {
        int num, i, sum = 0;
        go:
        {
            Scanner data = new Scanner(System.in);
            System.out.println("Enter a number");
            num = data.nextInt();
            for (i = 0; i < 100; i++) {
                sum = sum + i;
                if (i == num) {
                    break go;
                }
            }
        }
        System.out.println("Sum of odd number:" + sum);
    }
}


