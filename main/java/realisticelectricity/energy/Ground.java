package realisticelectricity.energy;

import realisticelectricity.CircuitSim.interfaces.IPassive;
import realisticelectricity.api.energy.INetworkConnection;
import realisticelectricity.api.energy.blocks.IGroundConnection;

public class Ground extends Component implements IPassive {

	public Ground(INetworkConnection device) {
		super(device);
		
		if(!(device instanceof IGroundConnection)) {
			throw new IllegalArgumentException("device is not an IGroundConnection");
		}
	}
	
	@Override
	public void voltageSolution(double v) {

	}

	@Override
	public double getResistance() {
		return this.resistance;
	}
	
	@Override
	public int getFirstNode() {
		return 0;
	}

}
