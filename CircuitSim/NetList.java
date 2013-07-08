package realisticelectricity.CircuitSim;

import java.util.ArrayList;
import java.util.List;

import realisticelectricity.CircuitSim.interfaces.IOnePort;
import realisticelectricity.CircuitSim.interfaces.IPassive;
import realisticelectricity.CircuitSim.interfaces.IVoltageSource;

public class NetList {

	public List<IPassive> passives;
	public List<IVoltageSource> voltageSources;
	
	public NetList() {
		
		passives = new ArrayList<IPassive>();
		voltageSources = new ArrayList<IVoltageSource>();
	}
	
	public void addComponent(IOnePort c) {
		
		if(c instanceof IPassive) {
			passives.add((IPassive)c);
		} else if(c instanceof IVoltageSource) {
			voltageSources.add((IVoltageSource)c);
		}
	}
	
	public void merge(NetList net) {

		passives.addAll(net.passives);
		voltageSources.addAll(net.voltageSources);
	}

}
