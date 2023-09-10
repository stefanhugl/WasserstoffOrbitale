package de.kratzer.horb;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.NumberFormat;
import javax.swing.text.NumberFormatter;

public class Flaeche extends JPanel {		//TODO: Spaghetticode bereinigen
	final static double pi = 3.14159265;
	public static int h = Rahmen.BildschirmHoehe, b = Rahmen.BildschirmBreite;
	public static int MassstabPosY = 146;  //TODO: ist 146 allgemein genug?
	public static double Laenge = 0.18897*h;		// Das Atom wird beobachtet in einer Kugel mit  
	public static double vgr = 1.8897*h / Laenge;	// Radius vgr in Einheiten a0=5.291772e-11m
	public static int TimerTakt = 20;		// in ms (mind. 1)
	public static int DeltaT, TaktNummer = 0;		// Startzeit und Intervall des Timers
	//TODO: Festgelegte Anfangsgrössen automatisch in Textfelder schreiben, wie schon bei DeltaT geschehen
	public static int Schnitt = 0;	// Schnittebene für 2D-Darstellung (0: räuml.;  1: x-y-Ebene; ...)
	public static double nachl = 1000;
	public static double nachlFaktorImExp;
	public static int n, l, m;	// Quantenzahlen
	public static int dF;
	
	public static void setSchnitt(int schnitt) {
		Schnitt = schnitt;
	}  // Schnittebene für 2D-Darstellung

	public static int MaxAnzEl = 100;				//maximale Zahl gleichzeitig sichtbarer Elektronenfundorte
	//TODO: 100 mit Orbital.Fund[][] vergleichen
	public static double[] Achse = new double[4];		//Drehachse
	double alpha = 0.0;

	public static double Winkel = 0.0;					//für Drehung
	double a11, a12, a13, a21, a22, a23, a31, a32, a33;	//Drehmatrix
	JTextField WinkelEing = erzeugtesEingabeFeld("0", 135, h - 330, 40);
	JTextField MaxAnzEing = erzeugtesEingabeFeld("100", 132, 254, 40);
	EingabeFeld MessrateEing = new EingabeFeld(); //erzeugtesEingabeFeld("20", 132, 280, 40);
	JTextField NachleuchtZeitEing = erzeugtesEingabeFeld("1000", 132, 310, 40);
	Schild  Chemisch = new Schild() , Magnetisch = new Schild(), Massstab = new Schild(), Angstroem = new Schild(), zieh = new Schild(),
			Raeuml = new Schild(), odr = new Schild(), Schn = new Schild(),
			xAch = new Schild(), yAch = new Schild(),zAch = new Schild(),		//AchsSchild = new Schild(),
			Quantenzahlen = new Schild(), nSchild = new Schild(), lSchild = new Schild(), mSchild = new Schild(),
			Dreh = new Schild(), Geschw = new Schild(), Umdr = new Schild(),
			MaxAnz = new Schild(), Messrate = new Schild(), proS = new Schild(),  NachleuchtZeit = new Schild(), inMs = new Schild();

	Knopf	nPlus = new Knopf(), nMinus = new Knopf(),
			lPlus = new Knopf(), lMinus = new Knopf(),
			mPlus = new Knopf(), mMinus = new Knopf();

	public static double[][] Fund = new double[100001][4];          //TODO: 100001 prüfen    \ Fund[][] besser in anderer Klasse?

	public Flaeche() {

		erzeugeEinstellungenUndBedienelemente();
		ActionListener ZeitNehmer = Takt -> {
			TaktNummer++;
			//System.out.println(DeltaT);
			if (TaktNummer % DeltaT == 0) {		//Division mit Rest, damit die folgenden Aktionen nur nach jedem DeltaT-ten Takt ausgeführt wird

				Atom.suche();					//sucht möglichen Ort des Elektrons
				alpha = alpha + Winkel;			//dreht das Orbital
				if (alpha > 2 * pi)				//fängt nach 2pi// wieder bei 0 an
					alpha = alpha - 2 * pi;		// wieder bei 0 an
			}

			repaint();						//zeichnet auf "Flaeche", wie in der überschriebenen "paintComponent" angegeben
		};

		Timer Uhr = new Timer(TimerTakt, ZeitNehmer);
		Uhr.start();
	}
	@Override
	public void paintComponent(Graphics Zeichnung) {

		super.paintComponent(Zeichnung);
		Graphics2D ebeneZeichnung = (Graphics2D) Zeichnung;

		int nEl = Atom.AnzEl;
		if (nEl > MaxAnzEl) nEl = MaxAnzEl;

		berechneDrehmatrix(alpha, Achse[1], Achse[2], Achse[3]);
		for (int i = 0; i < nEl; i++) {
			Elektron.zeichne(i, a11, a12, a13, a21, a22, a23, a31, a32, a33, ebeneZeichnung);
		}
	}
	
	public void berechneDrehmatrix(double alpha, double n1, double n2, double n3) {

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
		nachlFaktorImExp = Math.log(1/Elektron.AnfangsKreuzGroesse) / nachl;
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

		erzeugeSchild(Quantenzahlen,"<html><u>Quantenzahlen</u></<html>", 10, 15, 150, 20);
		add(Quantenzahlen);
		erzeugeSchild(nSchild,"n = 1", 15, 60, 50, 20);
		add(nSchild);
		erzeugeSchild(lSchild,"l = 0", 85, 60, 50, 20);
		add(lSchild);
		erzeugeSchild(mSchild,"m = 0",153, 60, 50, 20);
		add(mSchild);

		erzeugeKnopf(nPlus, "+", 10, 35, 45, 21);
		add(nPlus);
		
		ActionListener nPlusWarter = Erhoehe -> {
			if (n < 3) n++;
			nSchild.setText("n = " + n);
			Atom.getOrbital(n, l, m);
			Chemisch.setText(Atom.Chem);
			//Magnetisch.setText(Atom.Magn);
			Atom.setzeZurueck();
		};
		
		nPlus.addActionListener(nPlusWarter);

		erzeugeKnopf(nMinus, "-", 10, 86, 45, 21);
		add(nMinus);
		
		ActionListener nMinusWarter = Erniedrige -> {
			//erzeugeTextFeld("   ", 15, 65, 50);
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

		erzeugeKnopf(lPlus, "+", 80, 35, 45, 21);
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

		erzeugeKnopf(lMinus, "-", 80, 86, 45, 21);
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

		erzeugeKnopf(mPlus, "+", 150, 35, 45, 21);
		add(mPlus);
		
		ActionListener mPlusWarter = Erhoehe -> {
			if (m < l) m++;
			mSchild.setText("m = " + m);
			Atom.getOrbital(n, l, m);
			Magnetisch.setText(Atom.Magn);
			Atom.setzeZurueck();
		};
		
		mPlus.addActionListener(mPlusWarter);

		erzeugeKnopf(mMinus, "-", 150, 86, 45, 21);
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

				int mstY = h-MassstabPosY, mouY = Pos.getY(), mouX = Pos.getX(), mstX = (int)(10+Laenge);
				if (mouY > mstY-8 && mouY < mstY+8 && mouX > mstX-8 && mouX < mstX+8) {

					Atom.setzeZurueck();
					Laenge = mouX - 10;
					vgr = 1.8897*h / Laenge;
					Angstroem.setBounds(10 + (int)Laenge / 2 - 5, h - 140, 40, 20);
					zieh.setBounds(10 + (int)Laenge-88, h - 180, 140, 30);
				}
			}
		});
	}
	public void richteSchnittWahlEin() {

		erzeugeSchild(xAch,"x", b - 142, h - 336, 12, 12);
		add(xAch);
		erzeugeSchild(yAch,"y", b -  30, h - 380, 12, 16);
		add(yAch);
		erzeugeSchild(zAch,"z", b - 104, h - 449, 12, 12);
		add(zAch);
		erzeugeSchild(Raeuml,"räumlich",b - 60, h - 292, 60, 30);
		add(Raeuml);
		erzeugeSchild(odr,"oder", b - 60, h - 236, 60, 12);
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

		erzeugeSchild(Schn,"-Schnitt", b - 60, h - 130, 60, 12);
		add(Schn);
	}

	public void erzeugeDrehWahl() {

		erzeugeSchild(Dreh,"<html><u>Drehung</u></<html>", 10, h - 350, 200, 20);
		erzeugeSchild(Geschw,"Geschwindigkeit", 10, h - 330, 200, 20);
		erzeugeSchild(Umdr,"Umdr. pro min", 180, h - 330, 190, 20);
		add(Umdr); add(Dreh); add(Geschw);

		ActionListener DrehgeschwWarter = Eing -> {

			if (Schnitt == 0) Winkel = (Double.parseDouble(WinkelEing.getText()) * pi * DeltaT / 30000);
			if (Schnitt != 0) WinkelEing.setText("0");
		};
/*
		Runnable r = ()-> System.out.print("Run method");

		is equivalent to

		Runnable r = new Runnable() {
			@Override
			public void run() {
				System.out.print("Run method");
			}
		};
*/
		WinkelEing.addActionListener(DrehgeschwWarter);

		String Text, VorgabeText;
		int xOrt, yOrt;

		erzeugeSchild(xAch, "             Achse: x", 13, h - 290, 160, 20);
		add(xAch);
		VorgabeText = "0";
		xOrt = 13;
		yOrt = h - 290;
		erzeugeAchsEingabe(VorgabeText, xOrt, yOrt, 1, 2, 3);

		erzeugeSchild(yAch, "                          y", 13, h - 260, 160, 20);
		add(yAch);
		VorgabeText = "0";
		yOrt = h - 260;
		erzeugeAchsEingabe(VorgabeText, xOrt, yOrt, 2, 3, 1);

		erzeugeSchild(zAch, "                          z", 13, h - 230, 160, 20);
		add(zAch);
		VorgabeText = "1";
		yOrt = h - 230;
		erzeugeAchsEingabe(VorgabeText, xOrt, yOrt, 3, 1, 2);
	}

	public void erzeugeElektronenWahl() {

		EingabeFeld.richteEin(MessrateEing, "20", 132, 280); add(MessrateEing);
		dF = Integer.parseInt(MessrateEing.getText()) * TimerTakt;  //TODO: prüfen ... wie unten
		if (dF > 0 && dF < 1001) DeltaT = 1000 / dF;
		System.out.println("   " + DeltaT);

		erzeugeSchild(MaxAnz,"<html><body>Max. Anz. sichtb.<br>El.-Fundorte</body></html>", 10, 247, 200, 30);
		erzeugeSchild(Messrate,"Messrate",  58, 280, 100, 20);
		erzeugeSchild(proS,"pro s",  177, 280, 200, 20);
		erzeugeSchild(NachleuchtZeit,"Nachleuchtzeit", 18, 310, 190, 20);
		erzeugeSchild(inMs,"ms",  177, 310, 200, 20);
		add(MaxAnz); add(proS); add(NachleuchtZeit); add(inMs); add(Messrate);

		ActionListener MaxAnzWarter = Eing -> {

			int MaxAnzEingabe = Integer.parseInt(MaxAnzEing.getText());
			if (MaxAnzEingabe > 0 && MaxAnzEingabe < 1001)	MaxAnzEl = MaxAnzEingabe;
		};
		MaxAnzEing.addActionListener(MaxAnzWarter);

		//richteEingabeFeldEin(MessrateEing, "20", 132, 280);

		ActionListener MessrateWarter = Eing -> {

			int mE = Integer.parseInt(MessrateEing.getText());
			int uG = 1; int oG = 1000 / Flaeche.TimerTakt;
			mE = EingabeFeld.pruefe(MessrateEing, mE, uG, oG);
			DeltaT = 1000 / (mE * TimerTakt);
		};
		MessrateEing.addActionListener(MessrateWarter);

		ActionListener NachleuchtZeitWarter = Eing -> {

			int NachleuchtZeitEingabe = Integer.parseInt(NachleuchtZeitEing.getText());
			if (NachleuchtZeitEingabe > 4 && NachleuchtZeitEingabe < 10001) {
				nachl = NachleuchtZeitEingabe;
				nachlFaktorImExp = Math.log(1 / Elektron.AnfangsKreuzGroesse) / nachl;
			}
		};
		NachleuchtZeitEing.addActionListener(NachleuchtZeitWarter);
 
/*
		String Text, VorgabeText;
		int xOrt, yOrt;

		erzeugeSchild(xAch, "             Achse: x", 13, h - 290, 160, 20);
		add(xAch);
		VorgabeText = "0";
		xOrt = 13;
		yOrt = h - 290;
		erzeugeAchsEingabe(VorgabeText, xOrt, yOrt, 1, 2, 3);

		erzeugeSchild(yAch, "                          y", 13, h - 260, 160, 20);
		add(yAch);
		VorgabeText = "0";
		yOrt = h - 260;
		erzeugeAchsEingabe(VorgabeText, xOrt, yOrt, 2, 3, 1);

		erzeugeSchild(zAch, "                          z", 13, h - 230, 160, 20);
		add(zAch);
		VorgabeText = "1";
		yOrt = h - 230;
		erzeugeAchsEingabe(VorgabeText, xOrt, yOrt, 3, 1, 2);					*/
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
/*
	public void richteEingabeFeldEin(EingabeFeld DiesesEingabeFeld, String VorgabeText, int xOrt, int yOrt) {

		DiesesEingabeFeld.setText(VorgabeText);
		DiesesEingabeFeld.setBounds(xOrt, yOrt, 40, 20);

	}
*/
	public void richteOrbitalBenennungEin() {

		erzeugeSchild(Chemisch, "1s", 40, 30, 360, 300);
		Chemisch.setFont(Chemisch.getFont().deriveFont(48f));				//.setFont(new Font( "Times New Roman", Font.BOLD, 48));
		erzeugeSchild(Magnetisch,"",110, 50, 360, 300);
		Magnetisch.setFont(Magnetisch.getFont().deriveFont(24f));
		add(Chemisch); add(Magnetisch);
	}
	public void richteMasstabWahlEin() {

		erzeugeSchild(Massstab,"<html><u>Massstab</u></<html>", 10, h - 195, 200, 20);
		add(Massstab);
		erzeugeSchild(Angstroem,"1Å", 10 + (int)Laenge / 2 - 5, h - 140, 40, 30);
		Angstroem.setFont(Angstroem.getFont().deriveFont(16f));
		add(Angstroem);
		erzeugeSchild(zieh,"hier ziehen ↓", 10 + (int)Laenge-88, h - 180, 140, 30);
		add(zieh);
	}

	public void erzeugeSchild(Schild DiesesSchild, String Text, int xOrt, int yOrt, int Breite, int Hoehe) {
		DiesesSchild.setBounds(xOrt, yOrt, Breite, Hoehe);
		DiesesSchild.setText(Text);
	}
	public void erzeugeKnopf(Knopf DieserKnopf, String Text, int xOrt, int yOrt, int Breite, int Hoehe) {
		DieserKnopf.setBounds(xOrt, yOrt, Breite, Hoehe);
		DieserKnopf.setText(Text);
	}
}
