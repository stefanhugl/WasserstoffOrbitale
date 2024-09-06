package de.kratzer.horb;

public class OrbitalZwPPE extends Orbital {
	
	protected double Wellenfunktion() {
		
		return r * Math.exp( -r/2) * Math.round(Math.sin(theta)*1e8)/1e8;
	}
}
