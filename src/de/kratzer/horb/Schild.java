package de.kratzer.horb;

import javax.swing.*;
import java.awt.*;
public class Schild extends JLabel {

    public Schild() {
        setForeground(Color.white);
        setBackground(Color.black);
    }
    public static void erzeuge(Schild DiesesSchild, String Text, int xOrt, int yOrt, int Breite, int Hoehe) {
        DiesesSchild.setBounds(xOrt, yOrt, Breite, Hoehe);
        DiesesSchild.setText(Text);
    }
}
