package test;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Scanner;

/**
 *
 * @author dhanoa
 */
public class Fibonacci {

    public static long fib(int n) {
        // base case
        if (n <= 1) {
            return n;
        } else {
            // recussion
            return fib(n - 1) + fib(n - 2);
        }
    }

    public static void main(String[] args) {
        int num, i;


        main:
        {
            while(true){
                Scanner data = new Scanner(System.in);
                System.out.println();
                System.out.println("Enter a number upto which you want to print the series or enter 9999 to quit");
                num = data.nextInt();
                if(num == 9999)
                {
                    break main;
                }
                i = 1;
                go:
                {
                    while (true) {
                        System.out.print(fib(i)+", ");
                        if (i == num) {
                            break go;
                        }
                        i++;
                    }

                }
            }
        }
    }
}
/*
 * Write a program which show a fibonacci series using labels. First label will be the main label which will print the 
 * fabonaci series until user quits. Internal label will have a while(true) loop to print fabonacci series until limit 
 * reached and then break the internal label.
 *
*/
 