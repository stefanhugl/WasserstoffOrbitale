package de.kratzer.horb;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class Schild extends JLabel {

    public Schild(String Text, int xOrt, int yOrt, int Breite, int Hoehe, float Schriftgrad) {

        setText(Text);
        setBounds(xOrt, yOrt, Breite, Hoehe);
        setFont(getFont().deriveFont(Schriftgrad));
        setForeground(Color.white);
        setBackground(Color.black);
    }
}
