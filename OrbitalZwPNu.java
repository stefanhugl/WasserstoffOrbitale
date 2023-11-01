package de.kratzer.horb;

public class OrbitalZwPNu extends Orbital {
	
	protected double Wellenfunktion() {
		
		return r * Math.exp(-r/2) * Math.round(Math.cos(theta)*1e8)/1e8;
	}
}
