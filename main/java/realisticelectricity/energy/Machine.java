package realisticelectricity.energy;

import realisticelectricity.CircuitSim.interfaces.IVoltageSource;
import realisticelectricity.api.energy.INetworkConnection;
import realisticelectricity.api.energy.blocks.IElectricBlock;

public class Machine extends Component implements IVoltageSource {
	
	public Machine(INetworkConnection device) {
		super(device);
		
		if(!(device instanceof IElectricBlock)) {
			throw new IllegalArgumentException("device is not an IElectricBlock");
		}
	}
	
	@Override
	public double getResistance() {
		return this.device.getResistance();
	}
	
	@Override
	public double getVoltage() {
		return ((IElectricBlock)this.device).getWorkingVoltage();
	}
	
	@Override
	public void voltageSolution(double v) {
		
	}
}