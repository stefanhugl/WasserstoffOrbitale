package de.kratzer.horb;

public class OrbitalZweiS extends Orbital {
	
	protected double Wellenfunktion() {
		
		return (1 - r/2) * Math.exp(-r/2);
	}
}