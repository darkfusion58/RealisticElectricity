package realisticelectricity.energy;

import realisticelectricity.CircuitSim.interfaces.ICapacitor;
import realisticelectricity.api.energy.INetworkConnection;

public class Storage extends Component implements ICapacitor {
	
	public double capacitance;
	
	private double voltage, step;
	
	private double steadystate;
	
	public Storage(INetworkConnection device) {
		super(device);
	}
	
	@Override
	public double getResistance() {
		return this.resistance;
	}
	
	@Override
	public double getVoltage() {
		return voltage;
	}
	
	@Override
	public void voltageSolution(double v) {
		
		double dQ = ((v - voltage) / resistance) * step;
		
		voltage = voltage + dQ/capacitance;
	}
	
	public void setStep(double t) {
		step = t;
	}
}