package de.kratzer.horb;

public abstract class Orbital {
    final static double pi = 3.14159265;
    //final static double a0 = 5.291772e-11;
    public static double MessZeit;
    public double r, phi, theta;
    public double x, y, z, v;
    public boolean gefunden = false;
    public double Psi;
    public static double[][] Fund = new double[Flaeche.MaxAnzEl][4];
    private final java.util.Random Zufallsgenerator = new java.util.Random();
    //TODO: überlegen, wo h wirklich gebraucht wird
    int h = Rahmen.BildschirmHoehe;

    public boolean beobachte(int Nummer) {

        v = Flaeche.vgr;        // Kantenlänge des Beobachtungswürfels
        gefunden = false;
        int VersuchsZaehler = 0;        // höchstens
        while (!gefunden && VersuchsZaehler < 1000) {        // so viele Orte werden untersucht

            VersuchsZaehler++;
            // zufälliger Ort
            x = (Zufallsgenerator.nextDouble() - 0.5) * v;
            if (x == 0) x = v / 2; // [-v/2 ; v/2] \{0}
            y = (Zufallsgenerator.nextDouble() - 0.5) * v;
            if (y == 0) y = v / 2;
            z = (Zufallsgenerator.nextDouble() - 0.5) * v;
            if (z == 0) z = v / 2;

            switch (Flaeche.Schnitt) {      //TODO: x-z, y-z-Schnitt um 90° drehen
                case 1 -> z = 0;
                // x-y-Schnitt, also z = 0
                case 2 -> x = 0;
                case 3 -> y = 0;
            }

            berechneKugelKoordinaten();

            Psi = Wellenfunktion();                // so normiert, dass die größte...
            double p = Psi * Psi;                    // ...Wahrscheinlichkeit = 1 ist.						//
            double Zz = Zufallsgenerator.nextDouble();    //mit Wahrscheinlichkeit p
            if (Zz < p) gefunden = true;                //entscheiden, ob das Elektron dort ist.
            //Zz=...*0.001+0.018;
            if (gefunden) merkeKoordinaten(Nummer);
        }

        return gefunden;
    }

    abstract protected double Wellenfunktion();

    public void berechneKugelKoordinaten() {

        r = Math.sqrt(x * x + y * y + z * z);                        //Radius
        if (x == 0) phi = Math.signum(y) * pi / 2;
        else {
            phi = Math.atan(y / x);
            if (x < 0) phi = pi + phi;
        }
        theta = Math.acos(z / r);            //theta aus [0;pi]	//phi aus [-pi/2 ; 3pi/2]
    }

    public void merkeKoordinaten(int Nummer) {

        Fund[Nummer][1] = x / (v / 2) * (h / 2);    //anpassen an Bildschirmgröße
        Fund[Nummer][2] = y / (v / 2) * (h / 2);
        Fund[Nummer][3] = z / (v / 2) * (h / 2);
        Fund[Nummer][0] = Flaeche.Zeit;

        if (Flaeche.Schnitt == 1) {
            Fund[Nummer][3] = Fund[Nummer][1];
            Fund[Nummer][1] = 0;
        }    //in Zeichen-
        if (Flaeche.Schnitt == 3) {
            Fund[Nummer][2] = Fund[Nummer][1];
            Fund[Nummer][1] = 0;
        }    //ebene klappen
    }
}
