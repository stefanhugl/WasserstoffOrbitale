package de.kratzer.horb;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.NumberFormat;

import javax.swing.text.NumberFormatter;

public class Flaeche extends JPanel {		//TODO: Spaghetticode bereinigen

	final static double pi = 3.14159265;	
	public static int h = Rahmen.BildschirmHoehe, b = Rahmen.BildschirmBreite;
	public static int MassstabPosY = 146;
	public static double Laenge = 0.18897*h;		// Das Atom wird beobachtet in einer Kugel mit  
	public static double vgr = 1.8897*h / Laenge;	// Radius vgr in Einheiten a0=5.291772e-11m
	public static int Zeit = 0;
	public static int Schnitt = 0;	// Schnittebene für 2D-Darstellung
	public static int n, l, m;	// Quantenzahlen
	
	public static void setSchnitt(int schnitt) {
		Schnitt = schnitt;
	}  // Schnittebene für 2D-Darstellung

	final static int MaxAnzEl = 1000;				//maximale Zahl gleichzeitig sichtbarer Elektronenfundorte
	public static double[] Achse = new double[4];		//Drehachse
	double alpha = 0.0, Winkel = 0.0;					//für Drehung
	double a11, a12, a13, a21, a22, a23, a31, a32, a33;	//Drehmatrix

	public Flaeche() {
		
		setBackground(Color.BLACK);
		setLayout(null);
		n = 1;
		l = 0;
		erzeugeQuantenzahlWahl();

		JLabel Magnetisch = new JLabel("iii");
		Magnetisch.setBounds(110, 50, 360, 300);
		Magnetisch.setFont(Magnetisch.getFont().deriveFont(24f));
		Magnetisch.setForeground(Color.white);
		Magnetisch.setBackground(Color.black);
		add(Magnetisch);

		JLabel Chemisch	= new JLabel("1s");
		Chemisch.setBounds(40, 30, 360, 300);
		Chemisch.setFont(Chemisch.getFont().deriveFont(48f));
		Chemisch.setForeground(Color.white);
		Chemisch.setBackground(Color.black);
		add(Chemisch);
		
		erzeugeSchnittWahl();

		erzeugeTextFeld("Massstab", 10, Rahmen.BildschirmHoehe - 195, 200);

		JLabel Angstroem = new JLabel("1Å");
		Angstroem.setFont(Angstroem.getFont().deriveFont(16f));
		Angstroem.setBounds(10 + (int)Laenge / 2 - 5, Rahmen.BildschirmHoehe - 140, 40, 30);
		Angstroem.setForeground(Color.WHITE);
		Angstroem.setBackground(Color.BLACK);
		add(Angstroem);
		JLabel zieh = new JLabel("hier ziehen ↓");
		zieh.setBounds(10 + (int)Laenge-88, Rahmen.BildschirmHoehe - 180, 140, 30);
		zieh.setForeground(Color.WHITE);
		zieh.setBackground(Color.BLACK);
		add(zieh);

		Achse[1] = 0;
		Achse[2] = 0;
		Achse[3] = 1; // Drehachse
		erzeugeDrehWahl();
		
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
					Angstroem.setBounds(10 + (int)Laenge / 2 - 5, Rahmen.BildschirmHoehe - 140, 40, 20);
					zieh.setBounds(10 + (int)Laenge-88, Rahmen.BildschirmHoehe - 180, 140, 30);
				}
			}
		});

		ActionListener ZeitNehmer = Takt -> {
			Zeit++;
			Atom.suche();
			Chemisch.setText(Atom.Chem);
			Magnetisch.setText(Atom.Magn);
			alpha = alpha + Winkel;
			if (alpha > 2 * pi)
				alpha = alpha - 2 * pi;
			repaint();
		};

		Timer Uhr = new Timer(20, ZeitNehmer);
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

	public void erzeugeQuantenzahlWahl() {

		erzeugeTextFeld("Quantenzahlen", 10, 15, 150);		

		JLabel nFeld = new JLabel("n = " + n);
		nFeld.setBounds(15, 60, 50, 20);
		nFeld.setForeground(Color.WHITE);
		add(nFeld);
		
		JButton nPlus = new JButton("+");
		nPlus.setBounds(10, 35, 45, 21);
		nPlus.setForeground(Color.black);
		nPlus.setBackground(Color.white);
		add(nPlus);
		
		ActionListener nPlusWarter = Erhoehe -> {
			if (n < 3) n++;
			nFeld.setText("n = " + n);
			Atom.setzeZurueck();
		};
		
		nPlus.addActionListener(nPlusWarter);		

		JButton nMinus = new JButton("-");
		nMinus.setBounds(10, 86, 45, 21);
		nMinus.setForeground(Color.black);
		nMinus.setBackground(Color.white);
		add(nMinus);
		
		ActionListener nMinusWarter = Erniedrige -> {
			//erzeugeTextFeld("   ", 15, 65, 50);
			if (n > 1) n--;
			nFeld.setText("n = " + n);
			Atom.setzeZurueck();
		};
		
		nMinus.addActionListener(nMinusWarter);
		
		
		JLabel lFeld = new JLabel("l = " + l);
		lFeld.setBounds(85, 60, 50, 20);
		lFeld.setForeground(Color.WHITE);
		add(lFeld);
		
		JButton lPlus = new JButton("+");
		lPlus.setBounds(80, 35, 45, 21);
		lPlus.setForeground(Color.black);
		lPlus.setBackground(Color.white);
		add(lPlus);
		
		ActionListener lPlusWarter = Erhoehe -> {
			if (l < n-1) l++;
			lFeld.setText("l = " + l);
			Atom.setzeZurueck();
		};
		
		lPlus.addActionListener(lPlusWarter);		

		JButton lMinus = new JButton("-");
		lMinus.setBounds(80, 86, 45, 21);
		lMinus.setForeground(Color.black);
		lMinus.setBackground(Color.white);
		add(lMinus);
		
		ActionListener lMinusWarter = Erniedrige -> {
			if (l > 0) l--;
			lFeld.setText("l = " + l);
			Atom.setzeZurueck();
		};
		
		lMinus.addActionListener(lMinusWarter);
		
		
		JLabel mFeld = new JLabel("m = " + m);
		mFeld.setBounds(155, 60, 50, 20);
		mFeld.setForeground(Color.WHITE);
		add(mFeld);
		
		JButton mPlus = new JButton("+");
		mPlus.setBounds(150, 35, 45, 21);
		mPlus.setForeground(Color.black);
		mPlus.setBackground(Color.white);
		add(mPlus);
		
		ActionListener mPlusWarter = Erhoehe -> {
			if (m < l) m++;
			mFeld.setText("m = " + m);
			Atom.setzeZurueck();
		};
		
		mPlus.addActionListener(mPlusWarter);		

		JButton mMinus = new JButton("-");
		mMinus.setBounds(150, 86, 45, 21);
		mMinus.setForeground(Color.black);
		mMinus.setBackground(Color.white);
		add(mMinus);
		
		ActionListener mMinusWarter = Erniedrige -> {
			if (m > -l) m--;
			mFeld.setText("m = " + m);
			Atom.setzeZurueck();
		};
		
		mMinus.addActionListener(mMinusWarter);
	}

	public void erzeugeSchnittWahl() {

		JLabel xAch = new JLabel("x");
		xAch.setBounds(b - 142, h - 336, 12, 12);
		xAch.setForeground(Color.white);
		xAch.setBackground(Color.black);
		add(xAch);
		
		JLabel yAch = new JLabel("y");
		yAch.setBounds(b - 30, h - 380, 12, 16);
		yAch.setForeground(Color.white);
		yAch.setBackground(Color.black);
		add(yAch);
		
		JLabel zAch = new JLabel("z");
		zAch.setBounds(b - 104, h - 449, 12, 12);
		zAch.setForeground(Color.white);
		zAch.setBackground(Color.black);
		add(zAch);
		
		JLabel Raeuml = new JLabel("räumlich");
		Raeuml.setBounds(b - 60, h - 292, 60, 30);
		Raeuml.setForeground(Color.white);
		Raeuml.setBackground(Color.black);
		add(Raeuml);
		
		JLabel odr = new JLabel("oder");
		odr.setBounds(b - 60, h - 236, 60, 12);
		odr.setForeground(Color.white);
		odr.setBackground(Color.black);
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
				Atom.setzeZurueck();
			}

			if (Drueck1.getSource() == XSchnitt) {
				setSchnitt(2);
				Atom.setzeZurueck();
			}

			if (Drueck1.getSource() == YSchnitt) {
				setSchnitt(3);
				Atom.setzeZurueck();
			}
		};

		Raeumlich.addActionListener(SchnittKnopfWarter);
		ZSchnitt.addActionListener(SchnittKnopfWarter);
		XSchnitt.addActionListener(SchnittKnopfWarter);
		YSchnitt.addActionListener(SchnittKnopfWarter);
		
		JLabel Schn = new JLabel("-Schnitt");
		Schn.setBounds(b - 60, h - 130, 60, 12);
		Schn.setForeground(Color.white);
		Schn.setBackground(Color.black);
		add(Schn);
	}

	public void erzeugeDrehWahl() {			//TODO: Eingabe verbessern

		erzeugeTextFeld("Drehung", 10, Rahmen.BildschirmHoehe - 350, 200);
		erzeugeTextFeld("Geschwindigkeit", 10, Rahmen.BildschirmHoehe - 330, 200);
		JTextField WinkelEing = erzeugtesEingabeFeld("0", 135, Rahmen.BildschirmHoehe - 330, 40);

		ActionListener DrehgeschwWarter = Eing -> Winkel = Double.parseDouble(WinkelEing.getText()) / 1000;

		WinkelEing.addActionListener(DrehgeschwWarter);

		String Text, VorgabeText;
		int xOrt, yOrt;

		Text = "             Achse: x";
		VorgabeText = "0";
		xOrt = 13;
		yOrt = Rahmen.BildschirmHoehe - 290;
		erzeugeAchsEingabe(Text, VorgabeText, xOrt, yOrt, 1, 2, 3);

		Text = "                          y";
		VorgabeText = "0";
		yOrt = Rahmen.BildschirmHoehe - 260;
		erzeugeAchsEingabe(Text, VorgabeText, xOrt, yOrt, 2, 3, 1);

		Text = "                          z";
		VorgabeText = "1";
		yOrt = Rahmen.BildschirmHoehe - 230;
		erzeugeAchsEingabe(Text, VorgabeText, xOrt, yOrt, 3, 1, 2);
	}

	public void erzeugeAchsEingabe(String Text, String VorgabeText, int xOrt, int yOrt, int i, int j, int k) {

		erzeugeTextFeld(Text, xOrt, yOrt, 160);
		JTextField AchsEing = erzeugtesEingabeFeld(VorgabeText, xOrt + 121, yOrt, 20);

		ActionListener AchsWarter = EingEreig -> {

			double nn = Double.parseDouble(AchsEing.getText());
			double n0 = Math.sqrt(nn * nn + Achse[j] * Achse[j] + Achse[k] * Achse[k]);
			if (n0 == 0) System.out.println("n0 = 0");
			if (n0 != 0) {
				Achse[i] = nn / n0;
				Achse[j] = Achse[j] / n0;
				Achse[k] = Achse[k] / n0;
			}
		};

		AchsEing.addActionListener(AchsWarter);
	}

	public void erzeugeTextFeld(String Text, int xOrt, int yOrt, int Breite) {

		JLabel DiesesTextfeld = new JLabel(Text);
		DiesesTextfeld.setBounds(xOrt, yOrt, Breite, 20);
		DiesesTextfeld.setForeground(Color.WHITE);
		add(DiesesTextfeld);
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
}
