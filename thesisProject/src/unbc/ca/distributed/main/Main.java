package unbc.ca.distributed.main;

import unbc.ca.distributed.GUI.MainFrame;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author behnish
 * 
 * Main class which will call the mainFrame object and will show the graphical user interface for the
 * simulator. Object factory class will hold the object of the mainframe TOO.
 * 
 */

public class Main 
{
    public static void main(String[] args)
    {          
        java.awt.EventQueue.invokeLater(new Runnable() 
        {            
            @Override
            public void run() 
            {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            }
        });
    }    
}