package de.kratzer.horb;

import javax.swing.*;
import java.awt.*;

public class Rahmen extends JFrame{

    public Rahmen() {

        setUndecorated(true);
        setResizable(false);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GraphicsDevice bildschirm;
        bildschirm = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
        if (bildschirm.isFullScreenSupported()) bildschirm.setFullScreenWindow(this);
        else{
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Insets einschuebe = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
            WasserstoffOrbitale.BildschirmBreite = screenSize.width - einschuebe.left - einschuebe.right;
            WasserstoffOrbitale.BildschirmHoehe = screenSize.height - einschuebe.top  - einschuebe.bottom;
            setSize(WasserstoffOrbitale.BildschirmBreite, WasserstoffOrbitale.BildschirmHoehe);
        }
    }
}