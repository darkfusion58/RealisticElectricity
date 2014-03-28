package realisticelectricity.energy;

import java.util.HashSet;

import net.minecraft.tileentity.TileEntity;

import realisticelectricity.CircuitSim.interfaces.*;
import realisticelectricity.api.energy.INetworkConnection;
import realisticelectricity.api.energy.blocks.IConductor;
import realisticelectricity.api.energy.blocks.IGroundConnection;
import realisticelectricity.api.energy.blocks.IStorage;

public class ElectricNetwork {
	
	public HashSet<INetworkConnection> netpoints;
	
	public HashSet<Component> components;
	public HashSet<Conductor> conductors;
	
	public ElectricNetwork() {
		netpoints = new HashSet<INetworkConnection>();
		
		components = new HashSet<Component>();
		conductors = new HashSet<Conductor>();
	}
	
	public boolean isPointInNetwork(INetworkConnection block) {
		return netpoints.contains(block);
	}
	
	public void merge(ElectricNetwork net) {
		this.netpoints.addAll(net.netpoints);
		this.components.addAll(net.components);
		this.conductors.addAll(net.conductors);
	}
	
	public void addComponent(INetworkConnection block, int node1, int node2) {
		
		Component c = new Component(block);
		
		c.nodeA = node1;
		c.nodeB = node2;
		
		netpoints.add(block);
		
		components.add(c);
	}
	
	public void addConductor(Conductor conductor) {
		
		for(IConductor c : conductor.conductors) {
			netpoints.add(c);
		}
		
		conductors.add(conductor);
	}
	
	public Component getComponent(INetworkConnection block) {
		
		for(Component c : components) {
			if(c.device == block) {
				return c;
			}
		}
		
		return null;
	}
	
	public Conductor getConductor(IConductor conductor) {
		
		for(Conductor c : conductors) {
			if(c.isInConductor(conductor)) {
				return c;
			}
		}
		
		return null;
	}
	
	private static int nextNode;
	
	public static int getNewNode() {
		nextNode++;
		return nextNode;
	}
}
