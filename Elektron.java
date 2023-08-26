package de.kratzer.horb;

import java.awt.*;

public class Elektron {

	public static void zeichne(int i, double a11, double a12, double a13, double a21, double a22, double a23, double a31, double a32, double a33, Graphics2D ebeneZeichnung) {

		int h = Rahmen.BildschirmHoehe;	int b = Rahmen.BildschirmBreite;
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
		
		float Saettigung = 1f; if (Flaeche.Schnitt == 0) Saettigung = 0.5f-((float)(xs))/((float)(h/2));
		if (Saettigung<0) { Saettigung = 0;}
		if (Saettigung>1) { Saettigung = 1;}						//Farbton bestimmen
		Color Farbe = new Color(1, 1, 1, Saettigung);		
		ebeneZeichnung.setColor(Farbe);
		
		double Alter = Flaeche.Zeit - t;				//mit zunehmendem Alter..
		double KreuzGroesse = 10;
		KreuzGroesse = KreuzGroesse * Math.pow(2, -Alter / 16);	//..verkleinern
		if (KreuzGroesse < 0) KreuzGroesse = 0; 
		int KrGr = (int)KreuzGroesse;
		double PunktGroesse = 10;
		PunktGroesse = PunktGroesse * Math.pow(2, -Alter / 32);
		if (PunktGroesse < 0) PunktGroesse = 0; 
		int PuGr = (int)PunktGroesse;
		
		if (x<=b && y<=h) {			//falls innerhalb des Bildschirms
			ebeneZeichnung.fillOval((int)x - PuGr/2, (int)y - PuGr/2, PuGr, PuGr);	//zeichnen mit und y Koord.
			ebeneZeichnung.drawLine((int)x - KrGr, (int)y - KrGr, (int)x + KrGr, (int)y + KrGr);
			ebeneZeichnung.drawLine((int)x + KrGr, (int)y - KrGr, (int)x - KrGr, (int)y + KrGr);
		}
		
		int mstY = Flaeche.MassstabPosY, msl = (int)Flaeche.Laenge;			//Massstab aktualisieren
		Color Farbe1 = new Color(1, 1, 1, 1.0f);		
		ebeneZeichnung.setColor(Farbe1);
		ebeneZeichnung.drawLine(10+msl,     h-mstY-6, 10+msl, h-mstY+6);
		ebeneZeichnung.drawLine(10,         h-mstY-2, 10+msl, h-mstY-2);
 		ebeneZeichnung.fillRect(10,         h-mstY-2, msl/5,  4);
 		ebeneZeichnung.fillRect(10+2*msl/5, h-mstY-2, msl/5,  4);
 		ebeneZeichnung.fillRect(10+4*msl/5, h-mstY-2, msl/5,  4);
 		ebeneZeichnung.drawLine(10,         h-mstY+2, 10+msl, h-mstY+2);		
		ebeneZeichnung.drawLine(10,         h-mstY-6, 10,     h-mstY+6);
		
		ebeneZeichnung.drawLine(b-100, h-370, b-130, h-340);
		ebeneZeichnung.drawLine(b-130, h-340, b-130, h-344);
		ebeneZeichnung.drawLine(b-130, h-340, b-126, h-340);
		ebeneZeichnung.drawLine(b-100, h-370, b-40,  h-370);
		ebeneZeichnung.drawLine(b-40, h-370, b-43, h-373);
		ebeneZeichnung.drawLine(b-40, h-370, b-43, h-367);
		ebeneZeichnung.drawLine(b-100, h-370, b-100, h-430);
		ebeneZeichnung.drawLine(b-100, h-430, b-103, h-427);
		ebeneZeichnung.drawLine(b-100, h-430, b- 97, h-427);

		int Schn = Flaeche.Schnitt;
		
		if (Schn == 1) {
																		//KoSy für Schnitt-Demo aktualisieren
			int[] XYalleX = {b-98, b-119, b-69, b-49};
			int[] XYalleY = {h-368, h-348, h-348, h-368};		 
			ebeneZeichnung.fillPolygon(XYalleX, XYalleY, 4);
		}
		
		if (Schn == 2) ebeneZeichnung.fillRect(b-98, h-422, 50, 50);
		
		if (Schn == 3) {
			
			int[] XZalleX = {b-102, b-102, b-122, b-122};
			int[] XZalleY = {h-372, h-422, h-402, h-352};		 
			ebeneZeichnung.fillPolygon(XZalleX, XZalleY, 4);
		}		
	}
}
