package realisticelectricity.CircuitSim.SimpleComponent;

import realisticelectricity.CircuitSim.interfaces.IPassive;

public class SimpleResistor implements IPassive {

	public double resistance;
	public int firstNode, secondNode;
	
	public SimpleResistor() {
		this(0.0d, 0, 0);
	}
	
	public SimpleResistor(double r, int fn, int sn) {
		resistance = r;
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

	public void setResistance(double r) {
		resistance = r;
	}
	
	@Override
	public double getResistance() {
		return resistance;
	}

}
