package de.kratzer.horb;

import javax.swing.*;
import java.awt.*;

public class Rahmen extends JFrame{

    Rand dieserRand = new Rand();
    
    public Rahmen() {

        setUndecorated(false);
        //setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setResizable(false);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("WasserstoffOrbitale");
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());

            int width = screenSize.width - insets.left - insets.right;
            int height = screenSize.height - insets.top - insets.bottom;
            System.out.println("     left right" + insets.left + "  " + insets.right + " und top bottom " + insets.top + "  " + insets.bottom);

            //setSize(width, height);

            GraphicsDevice device;
            device=GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
            device.setFullScreenWindow(this);

            //if (device.isFullScreenSupported()){
            //    setUndecorated(true);
            //    device.setFullScreenWindow(this);
            //}


        }
}
