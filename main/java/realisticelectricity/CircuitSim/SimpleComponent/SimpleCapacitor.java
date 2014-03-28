package realisticelectricity.CircuitSim.SimpleComponent;

import realisticelectricity.CircuitSim.interfaces.ICapacitor;
import realisticelectricity.CircuitSim.interfaces.IVoltageSource;

public class SimpleCapacitor implements ICapacitor {

	public double capacitance, resistance;
	public int firstNode, secondNode;
	
	private double voltage, step;
	
	private double steadystate;
	
	public SimpleCapacitor() {
		this(0.0d, 0.0d, 0, 0);
	}
	
	public SimpleCapacitor(double r, double c, int fn, int sn) {
		resistance = r;
		capacitance = c;
		firstNode = fn;
		secondNode = sn;
		
		step = 1.0d;
		voltage = 0.0d;
		
		steadystate = 0.0d;
	}

	@Override
	public int getFirstNode() {
		return firstNode;
	}

	@Override
	public int getSecondNode() {
		return secondNode;
	}

	@Override
	public void voltageSolution(double v) {
		
		double dQ = ((v - voltage) / resistance) * step;
		
		voltage = voltage + dQ/capacitance;
	}
	
	public void setStep(double t) {
		step = t;
	}
	
	public void setVoltage(double v) {
		voltage = v;
	}
	
	public void setCapacitance(double c) {
		capacitance = c;
	}
	
	public void setResistance(double r) {
		resistance = r;
	}

	@Override
	public double getVoltage() {
		return voltage;
	}

	@Override
	public double getResistance() {
		return resistance;
	}

}
