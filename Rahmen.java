package de.kratzer.horb;

import javax.swing.*;
import java.awt.*;

public class Rahmen extends JFrame{
    
    public static int BildschirmBreite, BildschirmHoehe;
    
    public Rahmen() {   
    	
    	setExtendedState(JFrame.MAXIMIZED_BOTH);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    	
    	BildschirmBreite = Toolkit.getDefaultToolkit().getScreenSize().width; 
    	BildschirmHoehe = Toolkit.getDefaultToolkit().getScreenSize().height;
    	
    	Flaeche OrbitalFlaeche = new Flaeche();
    	add(OrbitalFlaeche);
        pack();        
        setTitle("WasserstoffOrbitale");      
        setVisible(true);            
    } 
}
