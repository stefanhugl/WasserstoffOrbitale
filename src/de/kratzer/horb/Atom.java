package de.kratzer.horb;

public class Atom {
	
	public static int MessungNummer = 0;
	public static int AnzEl = 0;
	public static boolean gefunden; //ist true, wenn in Klasse Orbital ein Elektronenort gefunden wurde
	public static int Zustand;
	public static String Chem="1s", Magn="";	//Texte für die Anzeige des Zustandes
	
	public static void suche() {

		Orbital aktuellesOrbital = getOrbital(Flaeche.n, Flaeche.l, Flaeche.m);	//gemäß den Quantenzahlen
																		// wird das aktuelle Orbital definiert
		gefunden = aktuellesOrbital.beobachte(MessungNummer);
		if (gefunden) { MessungNummer++; if (AnzEl < Flaeche.MaxAnzEl) AnzEl++; }
		if (MessungNummer >= Flaeche.MaxAnzEl) MessungNummer = 0;

	}
	
	public static Orbital getOrbital(int n, int l, int m) {
		
		Zustand = n*100 + l*10 + m;  //die drei Quantenzahlen werden in EINE Zahl verwandelt, damit man mit "case" entscheiden kann
		switch (Zustand) {
			//die Methode getOrbital gibt als Orbital die mit der aktuellen Wellenfunktion
			//ausgestattete Klasse Orbital zurück
			case 100: { OrbitalEinsS Orbital = new OrbitalEinsS(); Chem="1s"; Magn="";  return Orbital;}
			case 200: { OrbitalZweiS Orbital = new OrbitalZweiS(); Chem="2s"; Magn="";  return Orbital;}
			case 209: { OrbitalZwPME Orbital = new OrbitalZwPME(); Chem="2p"; Magn="-"; return Orbital;}
			case 210: { OrbitalZwPNu Orbital = new OrbitalZwPNu(); Chem="2p"; Magn="0"; return Orbital;}
			case 211: { OrbitalZwPPE Orbital = new OrbitalZwPPE(); Chem="2p"; Magn="+"; return Orbital;}
			case 300: { OrbitalDreiS Orbital = new OrbitalDreiS(); Chem="3s"; Magn="";  return Orbital;}
			case 309: { OrbitalDrPME Orbital = new OrbitalDrPME(); Chem="3p"; Magn="-"; return Orbital;}
			case 310: { OrbitalDrPNu Orbital = new OrbitalDrPNu(); Chem="3p"; Magn="0"; return Orbital;}
			case 311: { OrbitalDrPPE Orbital = new OrbitalDrPPE(); Chem="3p"; Magn="+"; return Orbital;}
			case 318: { OrbitalDrDMZ Orbital = new OrbitalDrDMZ(); Chem="3d"; Magn="2-";return Orbital;}
			case 319: { OrbitalDrDME Orbital = new OrbitalDrDME(); Chem="3d"; Magn="-"; return Orbital;}
			case 320: { OrbitalDrDNu Orbital = new OrbitalDrDNu(); Chem="3d"; Magn="0"; return Orbital;}
			case 321: { OrbitalDrDPE Orbital = new OrbitalDrDPE(); Chem="3d"; Magn="+"; return Orbital;}
			case 322: { OrbitalDrDPZ Orbital = new OrbitalDrDPZ(); Chem="3d"; Magn="2+";return Orbital;}
			default:  { OrbitalEinsS Orbital = new OrbitalEinsS(); Chem="XX"; Magn="";  return Orbital;}
		}
	}
	
	public static void setzeZurueck() {

		AnzEl = 0;
		MessungNummer = 0;
		Orbital.MessZeit = 0;
		Flaeche.Winkel = 0;
	}
}
