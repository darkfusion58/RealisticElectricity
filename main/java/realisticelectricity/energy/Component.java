package realisticelectricity.energy;

import net.minecraft.world.World;
import realisticelectricity.api.energy.INetworkConnection;

public class Component
{
	public double resistance;
	public INetworkConnection device;
	public int nodeA, nodeB;
	
	public Component(INetworkConnection device)
	{
		this.device = device;
		this.resistance = device.getResistance();
	}
	
	public World getWorld()
	{
		if(device!=null)
			return device.getWorld();
		return null;
	}
	
	public int getFirstNode() {
		return nodeA;
	}
	
	public int getSecondNode() {
		return nodeB;
	}
}