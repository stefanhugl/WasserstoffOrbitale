package de.kratzer.horb;

import javax.swing.*;
import java.awt.*;

public class Rahmen extends JFrame{
    
    //public static int BildschirmBreite, BildschirmHoehe;

    public Rahmen() {

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        //setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("WasserstoffOrbitale");
    }
}
