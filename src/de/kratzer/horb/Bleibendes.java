package de.kratzer.horb;

import java.awt.*;
public class Bleibendes {

    public static void zeichne(Graphics2D ebeneZeichnung){

        int h = WasserstoffOrbitale.BildschirmHoehe;	int b = WasserstoffOrbitale.BildschirmBreite;
        int mstX = Rand.links, mstY = h-Rand.unten-4,
            msl = (int)Flaeche.MassstabLaenge;	 //Massstab aktualisieren
        Color Farbe1 = new Color(1, 1, 1, 1.0f);
        ebeneZeichnung.setColor(Farbe1);                                     //Massstab zeichnen
        ebeneZeichnung.drawLine(    mstX,       mstY-2, mstX+msl, mstY-2);  //obere waagrechte Linie
        ebeneZeichnung.drawLine(    mstX,       mstY+2, mstX+msl, mstY+2);  //untere...
        ebeneZeichnung.fillRect(    mstX,        mstY-2, msl/5, 4);     //schwarz-weiß-Muster
        ebeneZeichnung.fillRect( mstX+2*msl/5,mstY-2, msl/5, 4);     //   "
        ebeneZeichnung.fillRect( mstX+4*msl/5,mstY-2, msl/5, 4);     //   "
        ebeneZeichnung.drawLine(    mstX,       mstY+6,       mstX,   mstY-6);  //linker Rand
        ebeneZeichnung.drawLine(mstX+msl,    mstY+6,  mstX+msl,mstY-6);  //rechter Rand

        int NullX = b-Rand.rechts-70, NullY = h-Rand.unten-210;                             //KoSy zeichnen

        ebeneZeichnung.drawLine(    NullX,       NullY,     NullX-30, NullY+30);    //x-Achse
        ebeneZeichnung.drawLine(NullX-30, NullY+30, NullX-30, NullY+26);       //Pfeil
        ebeneZeichnung.drawLine(NullX-30, NullY+30, NullX-26,  NullY+30);       // "
        ebeneZeichnung.drawLine(    NullX,       NullY,     NullX+60,  NullY);          //y-Achse
        ebeneZeichnung.drawLine(NullX+60,    NullY,     NullX+57,  NullY+3);
        ebeneZeichnung.drawLine(NullX+60,    NullY,     NullX+57,  NullY-3);
        ebeneZeichnung.drawLine(    NullX,       NullY,        NullX,  NullY-60);       //z-Achse
        ebeneZeichnung.drawLine(    NullX,   NullY-60, NullX-3,  NullY-57);
        ebeneZeichnung.drawLine(    NullX,   NullY-60, NullX+3,  NullY-57);

        int Schn = Flaeche.Schnitt;

        if (Schn == 1) {
            //Ebenen für Schnitt-Demo aktualisieren
            int[] XYalleX = {NullX+2, NullX-19, NullX+32, NullX+51};
            int[] XYalleY = {NullY+2, NullY+22, NullY+22, NullY+2};
            ebeneZeichnung.fillPolygon(XYalleX, XYalleY, 4);
        }

        if (Schn == 2) ebeneZeichnung.fillRect(NullX+2, NullY-52, 50, 50);

        if (Schn == 3) {

            int[] XZalleX = {NullX-2, NullX-2 , NullX-22, NullX-22};
            int[] XZalleY = {NullY-2, NullY-52, NullY-32, NullY+18};
            ebeneZeichnung.fillPolygon(XZalleX, XZalleY, 4);
        }
    }
}
