package de.kratzer.horb;

public class OrbitalDreiS extends Orbital {
	
	protected double Wellenfunktion() {
		
		return (1/27d) * (27 - 18*r + 2*r*r) * Math.exp(-r/3);
	}
}