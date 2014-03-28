package realisticelectricity.energy;

import java.util.HashSet;

import realisticelectricity.CircuitSim.interfaces.IPassive;
import realisticelectricity.api.energy.blocks.IConductor;

public class Conductor implements IPassive
{
	//The two ends.  I don't know if to store as Vectors or as IConductors.
	public IConductor nodeAConductor;
	public IConductor nodeBConductor;
	public double resistance;
	public HashSet<IConductor> conductors;
	
	//The two weakest links.
	public IConductor lowestPower;
	public IConductor lowestVoltage;
	
	public int nodeA, nodeB;
	
	public Conductor() {
		conductors = new HashSet<IConductor>();
	}
	
	boolean isInConductor(IConductor c) {
		return conductors.contains(c);
	}
	
	@Override
	public int getFirstNode() {
		return nodeA;
	}
	
	@Override
	public int getSecondNode() {
		return nodeB;
	}
	
	@Override
	public void voltageSolution(double v) {
		
	}
	
	@Override
	public double getResistance() {
		return resistance;
	}
}