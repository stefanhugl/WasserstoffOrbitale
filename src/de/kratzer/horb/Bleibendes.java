package de.kratzer.horb;

import java.awt.*;
public class Bleibendes {

    public static void zeichne(Graphics2D ebeneZeichnung){

        int h = Rahmen.BildschirmHoehe;	int b = Rahmen.BildschirmBreite;
        int mstY = Flaeche.MassstabPosY, msl = (int)Flaeche.MassstabLaenge;			//Massstab aktualisieren
        Color Farbe1 = new Color(1, 1, 1, 1.0f);
        ebeneZeichnung.setColor(Farbe1);
        ebeneZeichnung.drawLine(10+msl,     h-mstY-6, 10+msl, h-mstY+6);		//Massstab zeichnen
        ebeneZeichnung.drawLine(10,         h-mstY-2, 10+msl, h-mstY-2);
        ebeneZeichnung.fillRect(10,         h-mstY-2, msl/5,  4);
        ebeneZeichnung.fillRect(10+2*msl/5, h-mstY-2, msl/5,  4);
        ebeneZeichnung.fillRect(10+4*msl/5, h-mstY-2, msl/5,  4);
        ebeneZeichnung.drawLine(10,         h-mstY+2, 10+msl, h-mstY+2);
        ebeneZeichnung.drawLine(10,         h-mstY-6, 10,     h-mstY+6);

        ebeneZeichnung.drawLine(b-100, h-370, b-130, h-340);					//KoSy zeichnen
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
