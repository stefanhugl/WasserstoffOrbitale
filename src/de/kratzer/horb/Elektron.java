package de.kratzer.horb;

import java.awt.*;

public class Elektron {

	public static double AnfangsKreuzGroesse = 10;
	public static double AnfangsPunktGroesse = 10;

	public static int KrGr, PuGr;
	public static double Alter, KreuzGroesse, PunktGroesse;

	public static void zeichne(int i, double a11, double a12, double a13, double a21, double a22, double a23, double a31, double a32, double a33, Graphics2D ebeneZeichnung) {

		double h = Rahmen.BildschirmHoehe;	double b = Rahmen.BildschirmBreite;
		double t, x, y, z, xx, yy, zz, xs;
				
		 t = Orbital.Fund[i][0];	//Zeit und
		xx = Orbital.Fund[i][1];	//Koordinaten
		yy = Orbital.Fund[i][2];		//des Fundes eines Eelektrons
		zz = Orbital.Fund[i][3];
		x = a11*xx + a12*yy + a13*zz;	//Drehen mit Drehmatrix a11..a33
		y = a21*xx + a22*yy + a23*zz;
		z = a31*xx + a32*yy + a33*zz;	
		
		xs=  x;					//3D->2D
		x =  y - xs/3 + b/2;
		y = -z + xs/5 + h/2;

		float Saettigung = 1f; if (Flaeche.Schnitt == 0) Saettigung = 0.5f-((float)(xs))/((float)(h/2));  //Saettigung = 0.5f-((float)(xs))/((float)(h/2));
		if (Saettigung<0) { Saettigung = 0;}
		if (Saettigung>1) { Saettigung = 1;}						//Farbton bestimmen
																// t ist die Zeit der Messung
		Color Farbe = new Color(1, 1, 1, Saettigung);
		ebeneZeichnung.setColor(Farbe);

		Alter = Flaeche.TaktNummer - t;				// mit zunehmendem Alter...
		Alter = Alter * Flaeche.DeltaT;

		if (Alter < 0) Alter = Flaeche.DeltaT;	//(für den Fall, dass Flaeche.TaktNummer die doubleGröße überstiegen hat)
		KreuzGroesse = AnfangsKreuzGroesse * (Math.exp(Alter * Flaeche.nachlFaktorImExp));	//..verkleinern
		if (KreuzGroesse < 0) KreuzGroesse = 0;
		KrGr = (int)KreuzGroesse;

		PunktGroesse = AnfangsPunktGroesse * (Math.exp(Alter * Flaeche.nachlFaktorImExp / 2));
		if (PunktGroesse < 0) PunktGroesse = 0;
		PuGr = (int)PunktGroesse;

		if (x<=b && y<=h && PunktGroesse>1 && KreuzGroesse>1) {															//falls innerhalb des Bildschirms
			ebeneZeichnung.fillOval((int)x - PuGr/2, (int)y - PuGr/2, PuGr, PuGr);						//Punkt und
			ebeneZeichnung.drawLine((int)x - KrGr, (int)y - KrGr, (int)x + KrGr, (int)y + KrGr);	//Kreuz
			ebeneZeichnung.drawLine((int)x + KrGr, (int)y - KrGr, (int)x - KrGr, (int)y + KrGr);	//zeichnen mit x und y Koord.
		}
	}
}
