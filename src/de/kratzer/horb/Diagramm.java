package de.kratzer.horb;

public class Diagramm {
	
	public static short[] Saeule = new short[1000];
	
	public Diagramm() {
		
		setzeZurueck();
	}
	
	public static void erhoeheSaeule(int i) {
		
		Saeule[i]++;
		if (Saeule[i]>1000) Saeule[i]=1000;
	}
	
	public static void setzeZurueck(){
	
		for (int i = 0; i<1000; i++) Saeule[i] = 0;
	}
	
}