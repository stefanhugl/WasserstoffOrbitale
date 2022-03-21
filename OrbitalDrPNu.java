package de.kratzer.horb;

public class OrbitalDrPNu extends Orbital {
	
	protected double Wellenfunktion() {
		
		return (1-r/6) * r * Math.exp(-r/3) * Math.round(Math.cos(theta)*1e8)/1e8;
	}
}
