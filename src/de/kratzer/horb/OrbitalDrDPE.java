package de.kratzer.horb;

public class OrbitalDrDPE extends Orbital {
	
	protected double Wellenfunktion() {
		
		return r*r * Math.exp(-r/3) 
				   * Math.round(Math.sin(theta)*1e8)/1e8 
				   * Math.round(Math.cos(theta)*1e8)/1e8;
	}
}