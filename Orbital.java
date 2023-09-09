package de.kratzer.horb;

public abstract class Orbital {
    final static double pi = 3.14159265;
    //final static double a0 = 5.291772e-11;    //Bohrscher Radius
    public static double Dicke = 100;
    public static double MessZeit;
    public double r, phi, theta;
    public double x, y, z, v;
    public boolean gefunden = false;
    public double Psi;
    //public static double[][] Fund = new double[100001][4];          //TODO: 100001 prüfen
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

            berechneKugelKoordinaten();         //x y z  ->  r phi theta

            Psi = Wellenfunktion();                // so normiert, dass die größte...
            double p = Psi * Psi;                    // ...Wahrscheinlichkeit = 1 ist.						//
            double Zz = Zufallsgenerator.nextDouble();    //mit Wahrscheinlichkeit p
            if (Zz < p) gefunden = true;                //entscheiden, ob das Elektron dort ist.
            //Zz=...*0.001+0.018;
            if (gefunden) {

                merkeKoordinaten(Nummer);

                switch (Flaeche.Schnitt) {
                    case 1 : if (Math.abs(z) > Dicke) gefunden = false;     // x-y-Schnitt, also z ≈ 0
                    case 2 : if (Math.abs(x) > Dicke) gefunden = false;     // Versuch { x = 0; sss = y; y = z; z = sss; }
                    case 3 : if (Math.abs(y) > Dicke) gefunden = false;
                }
            }
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

        Flaeche.Fund[Nummer][1] = x / (v / 2) * ((double) h / 2);    //anpassen an Bildschirmgröße
        Flaeche.Fund[Nummer][2] = y / (v / 2) * ((double) h / 2);
        Flaeche.Fund[Nummer][3] = z / (v / 2) * ((double) h / 2);
        Flaeche.Fund[Nummer][0] = Flaeche.TaktNummer;                     //Zeit der Messung eines Eelektrons

        if (Flaeche.Schnitt == 1) {
            Flaeche.Fund[Nummer][3] = Flaeche.Fund[Nummer][1];
            Flaeche.Fund[Nummer][1] = 0;
        }    //in Zeichen-
        if (Flaeche.Schnitt == 3) {
            Flaeche.Fund[Nummer][2] = Flaeche.Fund[Nummer][1];
            Flaeche.Fund[Nummer][1] = 0;
        }    //-ebene klappen
    }
}
