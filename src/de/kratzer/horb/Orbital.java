package de.kratzer.horb;

public abstract class Orbital {
    final static double pi = 3.14159265;
    public static double MessZeit;
    public double r, phi, theta;            //Kugelkoordinaten
    public double x, y, z, Würfelseite;     //karthesische Koord. und Kantenlänge, des Beobachtungswürfels
    public boolean gefunden = false;
    public double Psi;
    private final java.util.Random Zufallsgenerator = new java.util.Random();
    double h = Rahmen.BildschirmHoehe;
    public static double[][] Fund = new double[Flaeche.MaxAnzEl][4];
    public boolean beobachte(int Nummer) {

        Würfelseite = Flaeche.Kante;        // Kantenlänge des Würfels, in dem das Elektron gesucht wird
        gefunden = false;
        int VersuchsZaehler = 0;                             // höchstens

        while (!gefunden && VersuchsZaehler < 1000) {        // so viele Orte werden untersucht

            VersuchsZaehler++;
            // zufälliger Ort
            x = (Zufallsgenerator.nextDouble() - 0.5)* Würfelseite;
            if (Flaeche.Schnitt == 2) x = 0;
            y = (Zufallsgenerator.nextDouble() - 0.5)* Würfelseite;
            if (Flaeche.Schnitt == 3) y = 0;
            z = (Zufallsgenerator.nextDouble() - 0.5)* Würfelseite;
            if (Flaeche.Schnitt == 1) z = 0;

            berechneKugelKoordinaten();         //x y z  ->  r phi theta

            Psi = Wellenfunktion();     // p ist die Wahrscheinlichkeit, an der Stelle r,phi,theta das Elektron zu finden,
            double p = Psi * Psi;       // so normiert, dass die größte Wahrscheinlichkeit = 1 ist.

            double Zz = Zufallsgenerator.nextDouble();    //mit Wahrscheinlichkeit p
            if (Zz < p) gefunden = true;                  //entscheiden, ob das Elektron dort ist.
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

        Fund[Nummer][1] = x / (Würfelseite /2) * (h/2);    //anpassen an Bildschirmgröße
        Fund[Nummer][2] = y / (Würfelseite /2) * (h/2);
        Fund[Nummer][3] = z / (Würfelseite /2) * (h/2);
        Fund[Nummer][0] = Flaeche.TaktNummer;              //Zeit der Messung eines Eelektrons

        if (Flaeche.Schnitt == 1) {                         //bei 2D-Schnitt
            Fund[Nummer][3] = Orbital.Fund[Nummer][1];
            Fund[Nummer][1] = 0;
        }                                                       //in Zeichen-
        if (Flaeche.Schnitt == 3) {
            Fund[Nummer][2] = Fund[Nummer][1];
            Fund[Nummer][1] = 0;
        }                                                      //-ebene klappen
    }
}
