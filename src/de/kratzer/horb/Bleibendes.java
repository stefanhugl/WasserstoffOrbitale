package de.kratzer.horb;

import java.awt.*;
public class Bleibendes {

    public static void zeichne(Graphics2D ebeneZeichnung){

        int h = WasserstoffOrbitale.BildschirmHoehe;	int b = WasserstoffOrbitale.BildschirmBreite;
        int mstY = Flaeche.MassstabPosY, msl = (int)Flaeche.MassstabLaenge;	 //Massstab aktualisieren
        Color Farbe1 = new Color(1, 1, 1, 1.0f);
        ebeneZeichnung.setColor(Farbe1);                                     //Massstab zeichnen
        ebeneZeichnung.drawLine(Rand.links, h-mstY-Rand.unten+114, Rand.links+msl, h-mstY-Rand.unten+114);
        ebeneZeichnung.drawLine(Rand.links, h-mstY-Rand.unten+118, Rand.links+msl, h-mstY-Rand.unten+118);
        ebeneZeichnung.fillRect(    Rand.links,        h-mstY-Rand.unten+114, msl/5, 4);
        ebeneZeichnung.fillRect( Rand.links+2*msl/5,h-mstY-Rand.unten+114, msl/5, 4);
        ebeneZeichnung.fillRect( Rand.links+4*msl/5,h-mstY-Rand.unten+114, msl/5, 4);
        ebeneZeichnung.drawLine(Rand.links,           h-mstY-Rand.unten+110, Rand.links, h-mstY-Rand.unten+122);
        ebeneZeichnung.drawLine(Rand.links+msl,           h-mstY-Rand.unten+110,     Rand.links+msl ,    h-mstY-Rand.unten+122);
                                                                                //KoSy zeichnen
        ebeneZeichnung.drawLine(b-Rand.rechts-70,  h-Rand.unten-250, b-Rand.rechts-100, h-Rand.unten-220);
        ebeneZeichnung.drawLine(b-Rand.rechts-100, h-Rand.unten-220, b-Rand.rechts-100, h-Rand.unten-224);
        ebeneZeichnung.drawLine(b-Rand.rechts-100, h-Rand.unten-220, b-Rand.rechts-96,  h-Rand.unten-220);
        ebeneZeichnung.drawLine(b-Rand.rechts-70,  h-Rand.unten-250, b-Rand.rechts-10,  h-Rand.unten-250);
        ebeneZeichnung.drawLine(b-Rand.rechts-10,  h-Rand.unten-250, b-Rand.rechts-13,  h-Rand.unten-253);
        ebeneZeichnung.drawLine(b-Rand.rechts-10,  h-Rand.unten-250, b-Rand.rechts-13,  h-Rand.unten-247);
        ebeneZeichnung.drawLine(b-Rand.rechts-70,  h-Rand.unten-250, b-Rand.rechts-70,  h-Rand.unten-310);
        ebeneZeichnung.drawLine(b-Rand.rechts-70,  h-Rand.unten-310, b-Rand.rechts-73,  h-Rand.unten-307);
        ebeneZeichnung.drawLine(b-Rand.rechts-70,  h-Rand.unten-310, b-Rand.rechts-67,  h-Rand.unten-307);

        int Schn = Flaeche.Schnitt;

        if (Schn == 1) {
            //KoSy für Schnitt-Demo aktualisieren
            int[] XYalleX = {b-Rand.rechts-68, b-Rand.rechts-89, b-Rand.rechts-39, b-Rand.rechts-19};
            int[] XYalleY = {h-Rand.unten-248, h-Rand.unten-228, h-Rand.unten-228, h-Rand.unten-248};
            ebeneZeichnung.fillPolygon(XYalleX, XYalleY, 4);
        }

        if (Schn == 2) ebeneZeichnung.fillRect(b-Rand.rechts-68, h-Rand.unten-302, 50, 50);

        if (Schn == 3) {

            int[] XZalleX = {b-Rand.rechts-72, b-Rand.rechts-72, b-Rand.rechts-92, b-Rand.rechts-92};
            int[] XZalleY = {h-Rand.unten-252, h-Rand.unten-302, h-Rand.unten-282, h-Rand.unten-232};
            ebeneZeichnung.fillPolygon(XZalleX, XZalleY, 4);
        }
    }
}
