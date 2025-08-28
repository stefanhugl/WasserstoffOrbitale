package de.kratzer.horb;

import java.awt.*;

// Variablen
// n, l, m		Quantenzahlen
// x, y, z, t	Koordinaten
// h, b			Bildschirmhöhe, -breite
// v			Vergrößerung

//Fläche.Schnitt	0: räumliche Darstellung; 1,2,3: x-y,x-z,...-Schnitt

public class WasserstoffOrbitale {

	public static int BildschirmBreite, BildschirmHoehe;

	public static void main(String[] args) {

		Rahmen OrbitalRahmen = new Rahmen();

        BildschirmBreite = OrbitalRahmen.getWidth();
		BildschirmHoehe = OrbitalRahmen.getHeight();
		//System.out.println("1: Width " + BildschirmBreite + " und Height " + BildschirmHoehe);

		Flaeche OrbitalFlaeche = new Flaeche();
		OrbitalRahmen.add(OrbitalFlaeche);
		OrbitalRahmen.setVisible(true);
		OrbitalFlaeche.setVisible(true);
	}
}
