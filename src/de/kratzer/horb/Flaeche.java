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
	public static int h = Rahmen.BildschirmHoehe, b = Rahmen.BildschirmBreite;
	public static int MassstabPosY = 146; //Abstand vom unteren Rand
	public static double MassstabLaenge = 0.1*h; 	//Anfangslänge des Maßstabs (entspricht 1 Angström)
	public static double Kante = h / MassstabLaenge;	// Das Atom wird beobachtet in einem
												// Würfel der Kantenlänge "Kante"
												// in Einheiten des Bohrschen Radius 5.291772e-11 m
	//kann bei schnelleren Computern die der Takt kleiner als 10 ms gewählt werden?
	public static int TimerTakt = 20, TaktNummer = 0;	// Takt des Timers in ms (mind. 1)
	public static int MessrateWert = 10, DeltaT = 1000 / (MessrateWert * TimerTakt);
	//MessrateWert * TimerTakt darf nicht größer als 1000 sein.
	//MessrateWert gibt an, wie oft pro s das Elektron gesucht wird.
	//DeltaT gibt an, nach wie vielen Timertakten jeweils das Elektron gesucht wird.
	public static int NachleuchtZeitVorgabe = 2000; // in ms
	public static int Schnitt = 0;	// Schnittebene für 2D-Darstellung (0: räuml.;  1: x-y-Ebene; ...)
	public static int n, l, m;	// Quantenzahlen
	public static void setSchnitt(int schnitt) {
		Schnitt = schnitt;
	}  // Schnittebene für 2D-Darstellung
	public static int MaxAnzEl;
	//maximale Zahl gleichzeitig sichtbarer Elektronenfundorte
	public static double[] Achse = new double[4];		//Drehachse
	double alpha = 0.0;
	public static double Winkel = 0.0;					//für Drehung
	double a11, a12, a13, a21, a22, a23, a31, a32, a33;	//Drehmatrix
	EingabeFeld         WinkelEing = new EingabeFeld();
	EingabeFeld       MessrateEing = new EingabeFeld();
	EingabeFeld NachleuchtZeitEing = new EingabeFeld();
	Schild  Chemisch = new Schild() , Magnetisch = new Schild(), Massstab = new Schild(), Angstroem = new Schild(), zieh = new Schild(),
			Raeuml = new Schild(), odr = new Schild(), Schn = new Schild(),
			xAch = new Schild(), yAch = new Schild(),zAch = new Schild(),
			Quantenzahlen = new Schild(), nSchild = new Schild(), lSchild = new Schild(), mSchild = new Schild(),
			Dreh = new Schild(), Geschw = new Schild(), Umdr = new Schild(),
			MaxAnz = new Schild(), Messrate = new Schild(), proS = new Schild(), NachleuchtZeit = new Schild(), inMs = new Schild(),
			xAchBeschr = new Schild(), yAchBeschr = new Schild(), zAchBeschr = new Schild();

	Knopf	nPlus = new Knopf(), nMinus = new Knopf(),
			lPlus = new Knopf(), lMinus = new Knopf(),
			mPlus = new Knopf(), mMinus = new Knopf();

	public Flaeche() {

		erzeugeEinstellungenUndBedienelemente();

		if (DeltaT == 0) DeltaT =1;
		MaxAnzEl = NachleuchtZeitVorgabe/(DeltaT*TimerTakt) + 1;

		ActionListener ZeitNehmer = Takt -> {
			TaktNummer++;
				if (TaktNummer % DeltaT == 0) {        //Division mit Rest, damit die folgenden Aktionen...
													  // ...nur nach jedem DeltaT-ten Takt ausgeführt wird
					Atom.suche();                    //sucht möglichen Ort des Elektrons
					alpha = alpha + Winkel;            //dreht das Orbital
					if (alpha > 2 * pi)                //fängt nach 2pi// wieder bei 0 an
						alpha = alpha - 2 * pi;        // wieder bei 0 an
				}

				repaint();
				//if (TaktNummer % DeltaT == 0) { repaint(); }
				// zeichnet nach jedem DeltaT-ten Takt
				// auf "Flaeche", wie in der
				// überschriebenen "paintComponent" angegeben
		};
		Timer Uhr = new Timer(TimerTakt, ZeitNehmer);
		Uhr.start();
	}

	@Override
	public void paintComponent(Graphics Zeichnung) {

		super.paintComponent(Zeichnung);
		Graphics2D ebeneZeichnung = (Graphics2D) Zeichnung;
		Bleibendes.zeichne(ebeneZeichnung);
		int nEl = Atom.AnzEl;
		berechneDrehmatrix(alpha, Achse[1], Achse[2], Achse[3]);
		for (int i = 0; i < nEl; i++) {
			Elektron.zeichne(i, a11, a12, a13, a21, a22, a23, a31, a32, a33, ebeneZeichnung);
			//i: Nummer des Elektronenfundortes; a11..a33: Drehmatrix
		}
		System.out.println();
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
		setBackground(Color.black); setLayout(null);
		n = 1; l = 0;
		richteQuantenzahlWahlEin();
		richteOrbitalBenennungEin();
		richteSchnittWahlEin();
		richteMasstabWahlEin();
		Achse[1] = 0; Achse[2] = 0; Achse[3] = 1; // Drehachse
		erzeugeDrehWahl();
		erzeugeMassstabsAenderung();
		erzeugeElektronenWahl();
	}

	public void richteQuantenzahlWahlEin() {

		Schild.erzeuge(Quantenzahlen,"<html><u>Quantenzahlen</u></<html>", 10, 15, 150, 20);
		add(Quantenzahlen);
		Schild.erzeuge(nSchild,"n = 1", 15, 60, 50, 20);
		add(nSchild);
		Schild.erzeuge(lSchild,"l = 0", 85, 60, 50, 20);
		add(lSchild);
		Schild.erzeuge(mSchild,"m = 0",153, 60, 50, 20);
		add(mSchild);

		Knopf.erzeuge(nPlus, "+", 10, 35, 45, 21);
		add(nPlus);
		
		ActionListener nPlusWarter = Erhoehe -> {
			if (n < 3) n++;
			nSchild.setText("n = " + n);
			Atom.getOrbital(n, l, m);
			Chemisch.setText(Atom.Chem);
			Atom.setzeZurueck();
		};
		
		nPlus.addActionListener(nPlusWarter);

		Knopf.erzeuge(nMinus, "-", 10, 86, 45, 21);
		add(nMinus);
		
		ActionListener nMinusWarter = Erniedrige -> {
			if (n > 1) n--;
			if (l > n-1) l--;
			if (m > l) m--;
			if (m <-l) m++;
			nSchild.setText("n = " + n);
			lSchild.setText("l = " + l);
			mSchild.setText("m = " + m);
			Atom.getOrbital(n, l, m);
			Chemisch.setText(Atom.Chem);
			Magnetisch.setText(Atom.Magn);
			Atom.setzeZurueck();
		};
		
		nMinus.addActionListener(nMinusWarter);

		Knopf.erzeuge(lPlus, "+", 80, 35, 45, 21);
		add(lPlus);

		ActionListener lPlusWarter = Erhoehe -> {
			if (l < n-1) l++;
			if (m <-l) m++;
			lSchild.setText("l = " + l);
			mSchild.setText("m = " + m);
			Atom.getOrbital(n, l, m);
			Chemisch.setText(Atom.Chem);
			Atom.setzeZurueck();
		};
		
		lPlus.addActionListener(lPlusWarter);

		Knopf.erzeuge(lMinus, "-", 80, 86, 45, 21);
		add(lMinus);
		
		ActionListener lMinusWarter = Erniedrige -> {
			if (l > 0) l--;
			if (m > l) m--;
			if (m <-l) m++;
			lSchild.setText("l = " + l);
			mSchild.setText("m = " + m);
			Atom.getOrbital(n, l, m);
			Chemisch.setText(Atom.Chem);
			Magnetisch.setText(Atom.Magn);
			Atom.setzeZurueck();
		};
		
		lMinus.addActionListener(lMinusWarter);

		Knopf.erzeuge(mPlus, "+", 150, 35, 45, 21);
		add(mPlus);
		
		ActionListener mPlusWarter = Erhoehe -> {
			if (m < l) m++;
			mSchild.setText("m = " + m);
			Atom.getOrbital(n, l, m);
			Magnetisch.setText(Atom.Magn);
			Atom.setzeZurueck();
		};
		
		mPlus.addActionListener(mPlusWarter);

		Knopf.erzeuge(mMinus, "-", 150, 86, 45, 21);
		add(mMinus);
		
		ActionListener mMinusWarter = Erniedrige -> {
			if (m >-l) m--;
			mSchild.setText("m = " + m);
			Atom.getOrbital(n, l, m);
			//Chemisch.setText(Atom.Chem);
			Magnetisch.setText(Atom.Magn);
			Atom.setzeZurueck();
		};
		
		mMinus.addActionListener(mMinusWarter);
	}
	public void erzeugeMassstabsAenderung() {
		addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent Pos) {}
			@Override
			public void mouseDragged(MouseEvent Pos) {

				int mstY = h-MassstabPosY, mouY = Pos.getY(), mouX = Pos.getX(), mstX = (int)(10+ MassstabLaenge);
				if (mouY > mstY-8 && mouY < mstY+8 && mouX > mstX-48 && mouX < mstX+48 && mouX > 10) {

					Atom.setzeZurueck();
					MassstabLaenge = mouX - 10;
					Kante = 1.8897*h / MassstabLaenge;
					Angstroem.setBounds(10 + (int) MassstabLaenge / 2 - 5, h - MassstabPosY + 6, 40, 20);
					zieh.setBounds(10 + (int) MassstabLaenge -88, h - MassstabPosY - 34, 140, 30);
				}
			}
		});
	}
	public void richteSchnittWahlEin() {

		Schild.erzeuge(xAchBeschr,"x",  b - 142, h - 336, 12, 12);
		add(xAchBeschr);
		Schild.erzeuge(yAchBeschr,"y", b -  30, h - 380, 12, 16);
		add(yAchBeschr);
		Schild.erzeuge(zAchBeschr,"z", b - 104, h - 449, 12, 12);
		add(zAchBeschr);
		Schild.erzeuge(Raeuml,"räumlich",b - 60, h - 292, 60, 30);
		add(Raeuml);
		Schild.erzeuge(odr,"oder", b - 60, h - 236, 60, 12);
		add(odr);

		JRadioButton Raeumlich = new JRadioButton("3D", true);
		JRadioButton ZSchnitt = new JRadioButton("x-y", false);
		JRadioButton XSchnitt = new JRadioButton("y-z", false);
		JRadioButton YSchnitt = new JRadioButton("x-z", false);

		Raeumlich.setBounds(b - 60, h - 268, 60, 25);
		 ZSchnitt.setBounds(b - 60, h - 216, 60, 25);
		 XSchnitt.setBounds(b - 60, h - 188, 60, 25);
		 YSchnitt.setBounds(b - 60, h - 160, 60, 25);

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
				Winkel = 0; WinkelEing.setText("0");
				alpha = 0;
				Atom.setzeZurueck();
			}

			if (Drueck1.getSource() == XSchnitt) {
				setSchnitt(2);
				Winkel = 0; WinkelEing.setText("0");
				alpha = 0;
				Atom.setzeZurueck();
			}

			if (Drueck1.getSource() == YSchnitt) {
				setSchnitt(3);
				Winkel = 0; WinkelEing.setText("0");
				alpha = 0;
				Atom.setzeZurueck();
			}
		};

		Raeumlich.addActionListener(SchnittKnopfWarter);
		ZSchnitt.addActionListener(SchnittKnopfWarter);
		XSchnitt.addActionListener(SchnittKnopfWarter);
		YSchnitt.addActionListener(SchnittKnopfWarter);

		Schild.erzeuge(Schn,"-Schnitt", b - 60, h - 130, 60, 12);
		add(Schn);
	}

	public void erzeugeDrehWahl() {

		Schild.erzeuge(Dreh,"<html><u>Drehung</u></<html>", 10, h - 350, 200, 20);
		Schild.erzeuge(Geschw,"Geschwindigkeit", 10, h - 330, 200, 20);
		Schild.erzeuge(Umdr,"Umdr. pro min", 182, h - 330, 190, 20);
		add(Umdr); add(Dreh); add(Geschw);

		EingabeFeld.richteEin(WinkelEing, "0", 135, h - 330); add(WinkelEing);
		Winkel = Integer.parseInt(WinkelEing.getText()) * pi * DeltaT / 30000;
		ActionListener DrehgeschwWarter = Eing -> {
			int Ein = Integer.parseInt(WinkelEing.getText());
			int uG = 0; int oG = 100;
			Winkel = EingabeFeld.pruefe(WinkelEing, Ein, uG, oG) * pi * DeltaT * TimerTakt / 30000;
		};
		WinkelEing.addActionListener(DrehgeschwWarter);

		String VorgabeText;
		int xOrt, yOrt;

		Schild.erzeuge(xAch, "             Achse: x", 13, h - 290, 160, 20);
		add(xAch);
		VorgabeText = "0";
		xOrt = 13;
		yOrt = h - 290;
		erzeugeAchsEingabe(VorgabeText, xOrt, yOrt, 1, 2, 3);

		Schild.erzeuge(yAch, "                          y", 13, h - 260, 160, 20);
		add(yAch);
		VorgabeText = "0";
		yOrt = h - 260;
		erzeugeAchsEingabe(VorgabeText, xOrt, yOrt, 2, 3, 1);

		Schild.erzeuge(zAch, "                          z", 13, h - 230, 160, 20);
		add(zAch);
		VorgabeText = "1";
		yOrt = h - 230;
		erzeugeAchsEingabe(VorgabeText, xOrt, yOrt, 3, 1, 2);
	}

	public void erzeugeElektronenWahl() {

		Schild.erzeuge(Messrate,"Messrate",  58, 280, 100, 20);
		Schild.erzeuge(proS,"pro s",  180, 280, 200, 20);
		Schild.erzeuge(NachleuchtZeit,"Nachleuchtzeit", 18, 310, 190, 20);
		Schild.erzeuge(inMs,"ms",  180, 310, 200, 20);
		add(MaxAnz); add(proS); add(Messrate); add(NachleuchtZeit); add(inMs);
		EingabeFeld.richteEin(MessrateEing, String.valueOf(MessrateWert), 132, 280); add(MessrateEing);
		DeltaT = 1000 / (Integer.parseInt(MessrateEing.getText()) * TimerTakt);
		ActionListener MessrateWarter = Eing -> {
			int mE = Integer.parseInt(MessrateEing.getText());
			int uG = 1; int oG = 1000 / TimerTakt;				//damit DeltaT>1 bleibt
			mE = EingabeFeld.pruefe(MessrateEing, mE, uG, oG);
			DeltaT = (int)((1000f / ((float)mE * (float)TimerTakt)) + 0.5);
		};
		MessrateEing.addActionListener(MessrateWarter);

		String ErsteNachleuchtZeit = Integer.toString(NachleuchtZeitVorgabe);
		EingabeFeld.richteEin(NachleuchtZeitEing, ErsteNachleuchtZeit, 132, 310); add(NachleuchtZeitEing);
		NachleuchtZeitVorgabe = Integer.parseInt(NachleuchtZeitEing.getText());
		ActionListener NachleuchtZeitWarter = Eing -> {
			int Ein = Integer.parseInt(NachleuchtZeitEing.getText());
			int uG = 1; int oG = 10000;
			NachleuchtZeitVorgabe = EingabeFeld.pruefe(NachleuchtZeitEing, Ein, uG, oG);
			//nachlFaktorImExp = Math.log(1 / Elektron.AnfangsKreuzGroesse) / NachleuchtZeitVorgabe;
		};
		NachleuchtZeitEing.addActionListener(NachleuchtZeitWarter);
	}

	public void erzeugeAchsEingabe(String VorgabeText, int xOrt, int yOrt, int i, int j, int k) {

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

	public JTextField erzeugtesEingabeFeld(String VorgabeText, int xOrt, int yOrt, int Breite) {

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

	public void richteOrbitalBenennungEin() {

		Schild.erzeuge(Chemisch, "1s", 40, 30, 360, 300);
		Chemisch.setFont(Chemisch.getFont().deriveFont(48f));				//.setFont(new Font( "Times New Roman", Font.BOLD, 48));
		Schild.erzeuge(Magnetisch,"",110, 50, 360, 300);
		Magnetisch.setFont(Magnetisch.getFont().deriveFont(24f));
		add(Chemisch); add(Magnetisch);
	}
	public void richteMasstabWahlEin() {
		Schild.erzeuge(Massstab,"<html><u>Massstab</u></<html>", 10, h - MassstabPosY - 51, 200, 20);
		add(Massstab);
		Schild.erzeuge(Angstroem,"1Å", 10 + (int) MassstabLaenge / 2 - 5, h - MassstabPosY + 6, 40, 30);
		Angstroem.setFont(Angstroem.getFont().deriveFont(16f));
		add(Angstroem);
		Schild.erzeuge(zieh,"hier ziehen ↓", 10 + (int) MassstabLaenge -88, h - MassstabPosY - 34, 140, 30);
		add(zieh);
	}
}
																		//Bsp. zum Pfeil ->
/*
		Runnable r = ()-> System.out.print("Run method");

		ist äquivalent zu

		Runnable r = new Runnable() {
			@Override
			public void run() {
				System.out.print("Run method");
			}
		};
*/