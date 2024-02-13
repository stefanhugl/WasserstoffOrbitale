package de.kratzer.horb;

import java.awt.Color;

public abstract class Orbital {
    final static double pi = 3.14159265;
    //final static double a0 = 5.291772e-11;    //Bohrscher Radius
    public static double Dicke = 1;
    public static double MessZeit;
    public double r, phi, theta;
    public double x, y, z, v, q;
    public boolean gefunden = false;
    public double Psi;
    private final java.util.Random Zufallsgenerator = new java.util.Random();
    double h = Rahmen.BildschirmHoehe;
    public static double[][] Fund = new double[Flaeche.MaxAnzElObergrenze+1][4];

    public boolean beobachte(int Nummer) {

        v = Flaeche.vgr;        // Kantenlänge des Beobachtungswürfels
        gefunden = false;
        int VersuchsZaehler = 0;                             // höchstens
        while (!gefunden && VersuchsZaehler < 1000) {        // so viele Orte werden untersucht

            VersuchsZaehler++;
            // zufälliger Ort
            x = (Zufallsgenerator.nextDouble() - 0.5)*v;
            if (Flaeche.Schnitt == 2) x = 0;                          // [-v/2 import java.awt.Color;; v/2] \{0}
            y = (Zufallsgenerator.nextDouble() - 0.5)*v;
            if (Flaeche.Schnitt == 3) y = 0;
            z = (Zufallsgenerator.nextDouble() - 0.5)*v;
            if (Flaeche.Schnitt == 1) z = 0;

            berechneKugelKoordinaten();         //x y z  ->  r phi theta

            Psi = Wellenfunktion();                // so normiert, dass die größte...
            double p = Psi * Psi;                  // ...Wahrscheinlichkeit = 1 ist.

            //if (p != 0) q = Math.log(110*p);
            //if (q < 0) q = 0;                    //wäre eine Möglichkeit,
            //if (q > 1) q = 1;                    //die Maxima zu überhöhen
            //p = q;                            //bloß passt 110 nur für 2s

            //weitere Möglichkeit:
            //double n = 10;
            //if (p < (1/Math.exp(4))) p =   0.5*Math.exp(4*n)*Math.pow(p,n);
            //                    else p = -(0.5/(Math.pow((1/Math.exp(4)-1),2)))*(p-1)*(p-1)+1;

            double Zz = Zufallsgenerator.nextDouble();    //mit Wahrscheinlichkeit p
            if (Zz < p) gefunden = true;                //entscheiden, ob das Elektron dort ist.
            if (gefunden) merkeKoordinaten(Nummer);
        }

        return gefunden;
    }

    abstract protected double Wellenfunktion();

    public void berechneKugelKoordinaten() {

        r = Math.sqrt(x*x + y*y + z*z);                        //Radius
        if (x == 0) phi = Math.signum(y) * pi/2;
        else {
            phi = Math.atan(y/x);
            if (x < 0) phi = pi + phi;
        }
        if (r == 0) theta = 0;
        if (r != 0) theta = Math.acos(z/r);            //theta aus [0;pi]	//phi aus [-pi/2 ; 3pi/2]
    }

    public void merkeKoordinaten(int Nummer) {

        Fund[Nummer][1] = x / (v/2) * (h/2);    //anpassen an Bildschirmgröße
        Fund[Nummer][2] = y / (v/2) * (h/2);
        Fund[Nummer][3] = z / (v/2) * (h/2);
        Fund[Nummer][0] = Flaeche.TaktNummer;                     //Zeit der Messung eines Eelektrons

        if (Flaeche.Schnitt == 1) {
            Fund[Nummer][3] = Orbital.Fund[Nummer][1];
            Fund[Nummer][1] = 0;
        }                                                       //in Zeichen-
        if (Flaeche.Schnitt == 3) {
            Fund[Nummer][2] = Fund[Nummer][1];
            Fund[Nummer][1] = 0;
        }                                                      //-ebene klappen
    }
}
