package de.kratzer.horb;

import javax.swing.*;

public class EingabeFeld extends JTextField {

    public EingabeFeld() {

        setColumns(4);
    }

    public static void richteEin(EingabeFeld DiesesEingabeFeld, String VorgabeText, int xOrt, int yOrt) {

        DiesesEingabeFeld.setText(VorgabeText);
        DiesesEingabeFeld.setBounds(xOrt, yOrt, 45, 20);
    }

    public static int pruefe(EingabeFeld DiesesEingabeFeld, int Eing, int uGr, int oGr) {
                                                          // uG, oG: untere und obere Grenze
        if (Eing < uGr) { Eing = uGr; DiesesEingabeFeld.setText(Integer.toString(uGr)); }
        if (Eing > oGr) { Eing = oGr; DiesesEingabeFeld.setText(Integer.toString(oGr)); }
        return Eing;
    }
}
