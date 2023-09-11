package de.kratzer.horb;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;
import java.text.NumberFormat;
import javax.swing.text.NumberFormatter;

public class EingabeFeld extends JTextField {

    public EingabeFeld() {

        setColumns(4);          //    setSize(int, int), setBounds(int, int, int, int), invalidate()

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
/*
		ActionListener NachleuchtZeitWarter = Eing -> {

			int NachleuchtZeitEingabe = Integer.parseInt(NachleuchtZeitEing.getText());
			if (NachleuchtZeitEingabe > 4 && NachleuchtZeitEingabe < 10001) {
				nachl = NachleuchtZeitEingabe;
				nachlFaktorImExp = Math.log(1 / Elektron.AnfangsKreuzGroesse) / nachl;
			} */
}
