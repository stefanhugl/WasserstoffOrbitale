package de.kratzer.horb;

public class OrbitalDrPPE extends Orbital {
	
	protected double Wellenfunktion() {
		
		return (1-r/6) * r * Math.exp(-r/3) * Math.round(Math.sin(theta)*1e8)/1e8;
	}
}
