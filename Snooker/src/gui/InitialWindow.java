/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

public class InitialWindow extends JFrame{
    private static final long serialVersionUID = 1L;
    
    private JProgressBar progressBar = new JProgressBar();
    ImagePanel initialPanel = new ImagePanel("src/gui/res/SnookerLogo.jpg");
    MainMenu mainMenu = new MainMenu();
    
    public InitialWindow(){
        setResizable(false);
        setTitle("Welcome to Snooker!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(Utilities.dimScreen.width - 150, Utilities.dimScreen.height - 150);
        setLocation(
                    (Utilities.dimScreen.width - getWidth()) / 2,
                (Utilities.dimScreen.height - getHeight()) / 2);
        
        initialPanel.setSize(getSize());
        add(initialPanel);
        
        mainMenu.setVisible(true);
    }
    
    public void startWindow(){
        new Thread(new Time()).start();
    }
    
    private class Time implements Runnable{
        @Override
        public void run(){
            for(int i=1; i<= 100; i++){
                progressBar.setValue(i);
                progressBar.repaint();
                
                try{
                    Thread.sleep(50);
                }catch(InterruptedException error){}
            }
            mainMenu.setVisible(true);
        setVisible(false);
        }      
    }
}
