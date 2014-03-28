package realisticelectricity.CircuitSim.SimpleComponent;

import realisticelectricity.CircuitSim.interfaces.IVoltageSource;

public class SimpleVoltageSource implements IVoltageSource {

	public double resistance, voltage;
	public int firstNode, secondNode;
	
	public SimpleVoltageSource() {
		this(0.0d, 0.0d, 0, 0);
	}
	
	public SimpleVoltageSource(double r, double v, int fn, int sn) {
		resistance = r;
		voltage = v;
		firstNode = fn;
		secondNode = sn;
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

	}

	public void setVoltage(double v) {
		voltage = v;
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
