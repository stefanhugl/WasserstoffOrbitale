package de.kratzer.horb;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.text.NumberFormat;

public class Flaeche extends JPanel {

    final static double pi = 3.14159265;
    public static int h = WasserstoffOrbitale.BildschirmHoehe, b = WasserstoffOrbitale.BildschirmBreite;
    //public static int h = Rahmen.BildschirmHoehe, b = Rahmen.BildschirmBreite;

    //public static int MassstabPosY = Rand.unten + 10; //Abstand vom unteren Rand
    public static double MassstabLaenge = 0.05 * h;    //Anfangslänge des Maßstabs (entspricht 1 Angström)
    public static double Kante = h / MassstabLaenge;     //Das Atom wird beobachtet in einem
    //Würfel der Kantenlänge "Kante"
    //in Einheiten des Bohrschen Radius 5.291772e-11 m
    public static int TimerTakt = 5, TaktNummer = 0;  //Takt des Timers in ms (mind. 1)
    public static int MessrateWert = 1, DeltaT = 1000 / (MessrateWert * TimerTakt);
    //MessrateWert * TimerTakt darf nicht größer als 1000 sein.
    //MessrateWert gibt an, wie oft pro s das Elektron gesucht wird.
    //DeltaT gibt an, nach wie vielen Timertakten jeweils das Elektron gesucht wird.
    public static int NachleuchtZeitVorgabe = 2000;  //in ms
    public static int Schnitt = 2;    //Schnittebene für 2D-Darstellung zu Beginn (0: räuml.;  1: x-y-Ebene; ...)
    public static int n = 2, l = 1, m = 0;     //Quantenzahlen

    public static void setSchnitt(int schnitt) {
        Schnitt = schnitt;
    }  // Schnittebene für 2D-Darstellung

    public static int MaxAnzEl;
    //maximale Zahl gleichzeitig sichtbarer Elektronenfundorte
    public static double[] Achse = new double[4];        //Drehachse
    double alpha = 0.0;                                    //aktueller Winkel
    public static double Winkel = 0.0;                    //wird addiert bei Drehung
    double a11, a12, a13, a21, a22, a23, a31, a32, a33;    //Drehmatrix
    EingabeFeld WinkelEing = new EingabeFeld();
    EingabeFeld MessrateEing = new EingabeFeld();
    EingabeFeld NachleuchtZeitEing = new EingabeFeld();
    Schild Chemisch = new Schild(), Magnetisch = new Schild(), //Labels
            Massstab = new Schild(), Angstroem = new Schild(), zieh = new Schild(),    //die an den Rändern
            Raeuml = new Schild(), odr = new Schild(), Schn = new Schild(),               //agezeigt werden
            xAch = new Schild(), yAch = new Schild(), zAch = new Schild(),
            Quantenzahlen = new Schild(), nSchild = new Schild(), lSchild = new Schild(), mSchild = new Schild(),
            Dreh = new Schild(), Geschw = new Schild(), Umdr = new Schild(),
            MaxAnz = new Schild(), Messrate = new Schild(), proS = new Schild(),
            NachleuchtZeit = new Schild(), inMs = new Schild(),
            xAchBeschr = new Schild(), yAchBeschr = new Schild(), zAchBeschr = new Schild();


    Knopf nPlus = new Knopf(), nMinus = new Knopf(),  //Buttons für Einstellungen
            lPlus = new Knopf(), lMinus = new Knopf(),
            mPlus = new Knopf(), mMinus = new Knopf(),
            Tipp = new Knopf(), Beend = new Knopf();

    public Flaeche() {

        //JButton closeButton = new JButton("Schließen");
        //closeButton.setBounds(500, 200, 150, 30);
        //closeButton.addActionListener(e -> System.exit(0));
        //add(closeButton);

        erzeugeEinstellungenUndBedienelemente();

        if (DeltaT == 0) DeltaT = 1;   //DeltaT muss > 0 sein
        MaxAnzEl = NachleuchtZeitVorgabe * MessrateWert + 1;   //Maximalanzahl sichtbarer Elektron hängt ab von Nachleuchtzeit und Messrate

        ActionListener ZeitNehmer = Takt -> {
            TaktNummer++;
            if (TaktNummer % DeltaT == 0) {        //Division mit Rest, damit die folgenden Aktionen...
                // ...nur nach jedem DeltaT-ten Takt ausgeführt wird
                Atom.suche();                    //sucht möglichen Ort des Elektrons
                alpha = alpha + Winkel;            //dreht das Orbital
                if (alpha > 2 * pi)                //fängt nach 2pi
                    alpha = alpha - 2 * pi;        // wieder bei 0 an
            }

            repaint();                      //zeichnet den Bildschirm neu, wie in
        };                                        //paintComponent vorgegeben
        Timer Uhr = new Timer(TimerTakt, ZeitNehmer);
        Uhr.start();
    }

    @Override
    public void paintComponent(Graphics Zeichnung) {

        super.paintComponent(Zeichnung);
        Graphics2D ebeneZeichnung = (Graphics2D) Zeichnung;
        Bleibendes.zeichne(ebeneZeichnung);                    //zeichneet, was dauerhaft gleich bleibt
        int nEl = Atom.AnzEl;
        berechneDrehmatrix(alpha, Achse[1], Achse[2], Achse[3]);
        for (int i = 0; i < nEl; i++) {
            Elektron.zeichne(i, a11, a12, a13, a21, a22, a23, a31, a32, a33, ebeneZeichnung);
            //i: Nummer des Elektronenfundortes; a11..a33: Drehmatrix
        }
    }

    public void berechneDrehmatrix(double alpha, double n1, double n2, double n3) {
        //alpha: Drehwinkel; n: Vektor der Drehachse
        double co, si, mi;
        co = Math.cos(alpha);
        si = Math.sin(alpha);
        mi = 1 - co;

        a11 = n1 * n1 * mi + co;
        a22 = n2 * n2 * mi + co;
        a33 = n3 * n3 * mi + co;

        a12 = n1 * n2 * mi - n3 * si;
        a13 = n1 * n3 * mi + n2 * si;
        a21 = n2 * n1 * mi + n3 * si;
        a23 = n2 * n3 * mi - n1 * si;
        a31 = n3 * n1 * mi - n2 * si;
        a32 = n3 * n2 * mi + n1 * si;
    }

    public void erzeugeEinstellungenUndBedienelemente() {
        setLayout(null); setForeground(Color.white); setBackground(Color.black);
        richteQuantenzahlWahlEin();
        richteOrbitalBenennungEin();
        richteSchnittWahlEin();
        richteMasstabWahlEin();
        Achse[1] = 0; Achse[2] = 0; Achse[3] = 1; // Drehachse
        erzeugeDrehWahl();
        erzeugeMassstabsAenderung();
        erzeugeElektronenWahl();
        erzeugeTipp(); erzeugeBeendSchild();
    }

    public void richteQuantenzahlWahlEin() {

        erzeugeSchilderUndKnoepfe(nSchild, "n = 2", nPlus, nMinus, Rand.links + 5, Rand.oben + 45);
        erzeugeSchilderUndKnoepfe(lSchild, "l = 1", lPlus, lMinus, Rand.links + 75, Rand.oben + 45);
        erzeugeSchilderUndKnoepfe(mSchild, "m = 0", mPlus, mMinus, Rand.links + 143, Rand.oben + 45);
        richteActionListenerEin();
    }

    public void erzeugeMassstabsAenderung () {
            addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseMoved(MouseEvent Pos) {}

                @Override
                public void mouseDragged(MouseEvent Pos) {

                    int mstY = h - Rand.unten,
                            mouX = Pos.getX(), mouY = Pos.getY(),
                            mstX = (int) (Rand.links + MassstabLaenge);

                    if (mouY > mstY - 8 && mouY < mstY + 8 && mouX > mstX - 48 && mouX < mstX + 48 && mouX > 10) {

                        Atom.setzeZurueck();
                        MassstabLaenge = mouX - Rand.links;
                        Kante = 1.8897 * h / MassstabLaenge;
                        Angstroem.setBounds(Rand.links + (int) MassstabLaenge / 2 - 5, h - Rand.unten + 6, 40, 20);
                        zieh.setBounds(Rand.links + (int) MassstabLaenge - 3, h - Rand.unten - 34, 140, 30);
                    }
                }
            });
        }

        public void richteSchnittWahlEin () {

            Schild.erzeuge(xAchBeschr, "x", b-Rand.rechts - 113, h-Rand.unten-178, 12, 12);
            add(xAchBeschr);
            Schild.erzeuge(yAchBeschr, "y", b-Rand.rechts - 3, h - Rand.unten-200, 12, 16);
            add(yAchBeschr);
            Schild.erzeuge(zAchBeschr, "z", b-Rand.rechts - 74, h - Rand.unten-289, 12, 12);
            add(zAchBeschr);
            Schild.erzeuge(Raeuml, "räumlich", b-Rand.rechts - 60, h - Rand.unten-160, 60, 30);
            add(Raeuml);
            Schild.erzeuge(odr, "oder", b-Rand.rechts - 60, h - Rand.unten-100, 60, 12);
            add(odr);

            JRadioButton Raeumlich = new JRadioButton("3D", false);
            JRadioButton ZSchnitt = new JRadioButton("x-y", false);
            JRadioButton XSchnitt = new JRadioButton("y-z", true);
            JRadioButton YSchnitt = new JRadioButton("x-z", false);

            Raeumlich.setBounds(b-Rand.rechts-60, h-Rand.unten-138, 60, 25);
            ZSchnitt.setBounds(b-Rand.rechts-60, h-Rand.unten-86, 60, 25);
            XSchnitt.setBounds(b-Rand.rechts-60, h-Rand.unten-58, 60, 25);
            YSchnitt.setBounds(b-Rand.rechts-60, h-Rand.unten-30, 60, 25);

            ButtonGroup SchnittGruppe = new ButtonGroup();
            SchnittGruppe.add(Raeumlich);
            SchnittGruppe.add(ZSchnitt);
            SchnittGruppe.add(XSchnitt);
            SchnittGruppe.add(YSchnitt);

            add(Raeumlich);
            add(ZSchnitt);
            add(XSchnitt);
            add(YSchnitt);

            ActionListener SchnittKnopfWarter = Drueck1 -> {

                if (Drueck1.getSource() == Raeumlich) {
                    setSchnitt(0);
                    Atom.setzeZurueck();
                }

                if (Drueck1.getSource() == ZSchnitt) {
                    setSchnitt(1);
                    Winkel = 0;
                    WinkelEing.setText("0");
                    alpha = 0;
                    Atom.setzeZurueck();
                }

                if (Drueck1.getSource() == XSchnitt) {
                    setSchnitt(2);
                    Winkel = 0;
                    WinkelEing.setText("0");
                    alpha = 0;
                    Atom.setzeZurueck();
                }

                if (Drueck1.getSource() == YSchnitt) {
                    setSchnitt(3);
                    Winkel = 0;
                    WinkelEing.setText("0");
                    alpha = 0;
                    Atom.setzeZurueck();
                }
            };

            Raeumlich.addActionListener(SchnittKnopfWarter);
            ZSchnitt.addActionListener(SchnittKnopfWarter);
            XSchnitt.addActionListener(SchnittKnopfWarter);
            YSchnitt.addActionListener(SchnittKnopfWarter);

            Schild.erzeuge(Schn, "-Schnitt", b-Rand.rechts - 60, h-Rand.unten, 60, 12);
            add(Schn);
        }

        public void erzeugeDrehWahl () {

            Schild.erzeuge(Dreh, "<html><u>Drehung</u></<html>", Rand.links, h - Rand.unten - 270, 200, 20);
            Schild.erzeuge(Geschw, "Geschwindigkeit", Rand.links, h - Rand.unten - 240, 200, 20);
            Schild.erzeuge(Umdr, "Umdr. pro min", Rand.links + 172, h - Rand.unten - 240, 190, 20);
            add(Umdr);
            add(Dreh);
            add(Geschw);

            EingabeFeld.richteEin(WinkelEing, "0", Rand.links + 125, h - Rand.unten - 240);
            add(WinkelEing);
            Winkel = Integer.parseInt(WinkelEing.getText()) * pi * DeltaT / 30000;
            ActionListener DrehgeschwWarter = Eing -> {
                int Ein = Integer.parseInt(WinkelEing.getText());
                int uG = 0;
                int oG = 100;
                if (Schnitt == 0) {
                    Winkel = EingabeFeld.pruefe(WinkelEing, Ein, uG, oG) * pi * DeltaT * TimerTakt / 30000;
                } else {
                    WinkelEing.setText("0");
                }
            };
            WinkelEing.addActionListener(DrehgeschwWarter);

            String VorgabeText;
            int xOrt, yOrt;

            Schild.erzeuge(xAch, "             Achse: x", Rand.links + 3, h - Rand.unten - 200, 160, 20);
            add(xAch);
            VorgabeText = "0";
            xOrt = Rand.links + 3;
            yOrt = h - Rand.unten - 200;
            erzeugeAchsEingabe(VorgabeText, xOrt, yOrt, 1, 2, 3);

            Schild.erzeuge(yAch, "                          y", Rand.links + 3, h - Rand.unten - 170, 160, 20);
            add(yAch);
            VorgabeText = "0";
            yOrt = h - Rand.unten - 170;
            erzeugeAchsEingabe(VorgabeText, xOrt, yOrt, 2, 3, 1);

            Schild.erzeuge(zAch, "                          z", Rand.links + 3, h - Rand.unten - 140, 160, 20);
            add(zAch);
            VorgabeText = "1";
            yOrt = h - Rand.unten - 140;
            erzeugeAchsEingabe(VorgabeText, xOrt, yOrt, 3, 1, 2);
        }

        public void erzeugeElektronenWahl () {

            Schild.erzeuge(Messrate, "Messrate", Rand.links + 48, Rand.oben + 265, 100, 20);
            Schild.erzeuge(proS, "pro s", Rand.links + 170, Rand.oben + 265, 200, 20);
            Schild.erzeuge(NachleuchtZeit, "Nachleuchtzeit", Rand.links + 8, Rand.oben + 295, 190, 20);
            Schild.erzeuge(inMs, "ms", Rand.links + 170, Rand.oben + 295, 200, 20);
            add(MaxAnz);
            add(proS);
            add(Messrate);
            add(NachleuchtZeit);
            add(inMs);
            EingabeFeld.richteEin(MessrateEing, String.valueOf(MessrateWert), Rand.links + 122, Rand.oben + 265);
            add(MessrateEing);
            DeltaT = 1000 / (Integer.parseInt(MessrateEing.getText()) * TimerTakt);
            ActionListener MessrateWarter = Eing -> {
                int mE = Integer.parseInt(MessrateEing.getText());
                int uG = 1;
                int oG = 1000 / TimerTakt;                //damit DeltaT>1 bleibt
                mE = EingabeFeld.pruefe(MessrateEing, mE, uG, oG);
                DeltaT = (int) ((1000f / ((float) mE * (float) TimerTakt)) + 0.5);
                MaxAnzEl = NachleuchtZeitVorgabe * mE + 1;
            };
            MessrateEing.addActionListener(MessrateWarter);

            String ErsteNachleuchtZeit = Integer.toString(NachleuchtZeitVorgabe);
            EingabeFeld.richteEin(NachleuchtZeitEing, ErsteNachleuchtZeit, Rand.links + 122, Rand.oben + 295);
            add(NachleuchtZeitEing);
            NachleuchtZeitVorgabe = Integer.parseInt(NachleuchtZeitEing.getText());
            ActionListener NachleuchtZeitWarter = Eing -> {
                int Ein = Integer.parseInt(NachleuchtZeitEing.getText());
                int uG = 1;
                int oG = 5000;
                NachleuchtZeitVorgabe = EingabeFeld.pruefe(NachleuchtZeitEing, Ein, uG, oG);
                MaxAnzEl = NachleuchtZeitVorgabe * MessrateWert + 1;
            };
            NachleuchtZeitEing.addActionListener(NachleuchtZeitWarter);
        }

        public void erzeugeAchsEingabe (String VorgabeText,int xOrt, int yOrt, int i, int j, int k){

            JTextField AchsEing = erzeugtesEingabeFeld(VorgabeText, xOrt + 121, yOrt, 20);

            ActionListener AchsWarter = EingEreig -> {

                double nn = Double.parseDouble(AchsEing.getText());
                double n0 = Math.sqrt(nn * nn + Achse[j] * Achse[j] + Achse[k] * Achse[k]);

                if (n0 != 0) {
                    Achse[i] = nn / n0;
                    Achse[j] = Achse[j] / n0;
                    Achse[k] = Achse[k] / n0;
                }
            };

            AchsEing.addActionListener(AchsWarter);
        }

        public JTextField erzeugtesEingabeFeld (String VorgabeText,int xOrt, int yOrt, int Breite){

            NumberFormat ZahlForm = NumberFormat.getInstance();
            ZahlForm.setGroupingUsed(false);
            JFormattedTextField DiesesEingabefeld = new JFormattedTextField(NumberFormat.getInstance());
            ((NumberFormatter) DiesesEingabefeld.getFormatter()).setAllowsInvalid(false);
            DiesesEingabefeld.setColumns(4);
            DiesesEingabefeld.setText(VorgabeText);
            DiesesEingabefeld.setBounds(xOrt, yOrt, Breite, 20);
            add(DiesesEingabefeld);
            return DiesesEingabefeld;
        }

        public void richteOrbitalBenennungEin () {
            Schild.erzeuge(Chemisch, "2p", Rand.links + 30, Rand.oben + 15, 360, 300);
            Chemisch.setFont(Chemisch.getFont().deriveFont(48f));                //.setFont(new Font( "Times New Roman", Font.BOLD, 48));
            Schild.erzeuge(Magnetisch, "", Rand.links + 100, Rand.oben + 45, 360, 300);
            Magnetisch.setFont(Magnetisch.getFont().deriveFont(24f));
            add(Chemisch);
            add(Magnetisch);
        }

        public void richteMasstabWahlEin () {
            Schild.erzeuge(Massstab, "<html><u>Massstab</u></<html>", Rand.links, h - Rand.unten - 51, 200, 20);
            add(Massstab);
            Schild.erzeuge(Angstroem, "1Å", Rand.links + (int) MassstabLaenge / 2 - 5, h - Rand.unten + 6, 40, 30);
            Angstroem.setFont(Angstroem.getFont().deriveFont(16f));
            add(Angstroem);
            Schild.erzeuge(zieh, "↓ hier ziehen", Rand.links + (int) MassstabLaenge - 3, h - Rand.unten - 34, 140, 30);
            add(zieh);
        }

        public void erzeugeTipp () {
            Knopf.erzeuge(Tipp, "Erhöhe Messrate und Nachleuchtzeit, bis das Muster erkennbar ist.        X", Rand.links + 240, h - Rand.unten, 560, 20);
            Tipp.setBackground(Color.red);
            Tipp.addActionListener(e -> Tipp.setVisible(false));
            add(Tipp);
        }

        public void erzeugeBeendSchild () {
            Knopf.erzeuge(Beend, "", b - Rand.rechts-10, Rand.oben+10, 20, 20);
            Beend.setOpaque(false);
            Beend.addActionListener(e -> System.exit(0));
            add(Beend);
        }

        public void erzeugeSchilderUndKnoepfe(Schild QZahlSchild, String Text, Knopf PlusKnopf, Knopf MinusKnopf,
        int xOrt, int yOrt){

            Schild.erzeuge(QZahlSchild, Text, xOrt, yOrt, 50, 20);
            add(QZahlSchild);

            Knopf.erzeuge(PlusKnopf, "+", xOrt - 5, yOrt - 25, 45, 21);
            add(PlusKnopf);

            Knopf.erzeuge(MinusKnopf, "-", xOrt - 5, yOrt + 26, 45, 21);
            add(MinusKnopf);
        }

    public void richteActionListenerEin() {

        ActionListener nPlusWarter = Erhoehe -> {
            if (n < 3) n++;
            nSchild.setText("n = " + n);
            Atom.getOrbital(n, l, m);
            Chemisch.setText(Atom.Chem);
            Atom.setzeZurueck();
        };
        nPlus.addActionListener(nPlusWarter);

        ActionListener nMinusWarter = Erniedrige -> {
            if (n > 1) n--;
            if (l > n - 1) l--;
            if (m > l) m--;
            if (m < -l) m++;
            nSchild.setText("n = " + n);
            lSchild.setText("l = " + l);
            mSchild.setText("m = " + m);
            Atom.getOrbital(n, l, m);
            Chemisch.setText(Atom.Chem);
            Magnetisch.setText(Atom.Magn);
            Atom.setzeZurueck();
        };
        nMinus.addActionListener(nMinusWarter);

        ActionListener lPlusWarter = Erhoehe -> {
            if (l < n - 1) l++;
            if (m < -l) m++;
            lSchild.setText("l = " + l);
            mSchild.setText("m = " + m);
            Atom.getOrbital(n, l, m);
            Chemisch.setText(Atom.Chem);
            Atom.setzeZurueck();
        };
        lPlus.addActionListener(lPlusWarter);

        ActionListener lMinusWarter = Erniedrige -> {
            if (l > 0) l--;
            if (m > l) m--;
            if (m < -l) m++;
            lSchild.setText("l = " + l);
            mSchild.setText("m = " + m);
            Atom.getOrbital(n, l, m);
            Chemisch.setText(Atom.Chem);
            Magnetisch.setText(Atom.Magn);
            Atom.setzeZurueck();
        };
        lMinus.addActionListener(lMinusWarter);

        ActionListener mPlusWarter = Erhoehe -> {
            if (m < l) m++;
            mSchild.setText("m = " + m);
            Atom.getOrbital(n, l, m);
            Magnetisch.setText(Atom.Magn);
            Atom.setzeZurueck();
        };
        mPlus.addActionListener(mPlusWarter);

        ActionListener mMinusWarter = Erniedrige -> {
            if (m > -l) m--;
            mSchild.setText("m = " + m);
            Atom.getOrbital(n, l, m);
            Chemisch.setText(Atom.Chem);
            Magnetisch.setText(Atom.Magn);
            Atom.setzeZurueck();
        };
        mMinus.addActionListener(mMinusWarter);
    }
}

