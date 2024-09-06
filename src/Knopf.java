package de.kratzer.horb;

import javax.swing.*;
import java.awt.*;
public class Knopf extends JButton {

    public Knopf() {

        setForeground(Color.black);
        setBackground(Color.white);
    }

    public static void erzeuge(Knopf DieserKnopf, String Text, int xOrt, int yOrt, int Breite, int Hoehe) {
        DieserKnopf.setBounds(xOrt, yOrt, Breite, Hoehe);
        DieserKnopf.setText(Text);
    }
}
