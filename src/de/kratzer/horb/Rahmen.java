package de.kratzer.horb;

import javax.swing.*;
import java.awt.*;

public class Rahmen extends JFrame{

    Rand dieserRand = new Rand();
    
    public Rahmen() {

        setUndecorated(true);
        setResizable(false);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setTitle("WasserstoffOrbitale");
        GraphicsDevice device;
        device=GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
        //if (device.isFullScreenSupported()) System.out.println("Vollbild möglich");
        if (device.isFullScreenSupported()) device.setFullScreenWindow(this);
        else{
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
            //System.out.println("     left right" + insets.left + "  " + insets.right + " und top bottom " + insets.top + "  " + insets.bottom);
            WasserstoffOrbitale.BildschirmBreite = screenSize.width - insets.left - insets.right;
            WasserstoffOrbitale.BildschirmHoehe = screenSize.height - insets.top - insets.bottom;
            setSize(WasserstoffOrbitale.BildschirmBreite, WasserstoffOrbitale.BildschirmHoehe);
                    }
    }
}