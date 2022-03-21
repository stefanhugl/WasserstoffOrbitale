package de.kratzer.horb;

public class OrbitalZwPME extends Orbital {
 
	protected double Wellenfunktion() {
	 
		return Math.exp( -r/2) * Math.round(Math.sin(theta)*1e8)/1e8;
	}
}
