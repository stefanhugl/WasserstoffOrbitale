package de.kratzer.horb;

import javax.swing.*;
import java.awt.*;

public class Rahmen extends JFrame{
    
        public Rahmen() {

        setUndecorated(true);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setResizable(false);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("WasserstoffOrbitale");
        }
}
