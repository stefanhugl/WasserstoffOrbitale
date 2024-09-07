package de.kratzer.horb;

public class OrbitalEinsS extends Orbital {
	
	protected double Wellenfunktion() {
		
		return Math.exp(-r);
   }
}
