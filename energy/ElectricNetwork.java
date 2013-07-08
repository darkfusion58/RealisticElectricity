package realisticelectricity.energy;

import static net.minecraftforge.common.ForgeDirection.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import realisticelectricity.api.energy.INetworkConnection;
import realisticelectricity.api.energy.blocks.IConductor;
import realisticelectricity.api.energy.blocks.IElectricBlock;
import thutconcrete.api.utils.Vector3;
 
public class ElectricNetwork
{
	//Used to check if something new is added. this is just a collection of all devices in the network.
	public HashSet<INetworkConnection> points = new HashSet<INetworkConnection>();
	
	
	//This has an easy access to the resistance of the devices
	public HashSet<Component> components = new HashSet<Component>();
	
	public HashSet<Conductor> conductors = new HashSet<Conductor>();
	
	public boolean contains(INetworkConnection component)
	{
		return points.contains(component);
	}
	
	public void connectToNetwork(INetworkConnection component)
	{
		if(component instanceof IConductor)
		{
			HashSet<Conductor> connected = new HashSet<Conductor>();
			for(Conductor c: conductors)
			{
				for(IConductor c1: c.conductors)
					if(areConnected(c1,component))
					{
						connected.add(c);
						break;
					}
			}
			if(connected.size()>0)
			{
				conductors.removeAll(connected);
				HashSet<IConductor> iConductors = new HashSet<IConductor>();
				iConductors.add((IConductor)component);
				for(Conductor c: connected)
				{
					points.removeAll(c.conductors);
					iConductors.addAll(c.conductors);
				}
				combineAndAddIConductors(iConductors);
				
			}
		}
		else
		{
			components.add(new Component(component));
			points.add(component);
		}
	}
	
	public void disconnectFromNetwork(INetworkConnection component)
	{
		if(points.contains(component))
		{
			if(component instanceof IConductor)
			{
				HashSet<Conductor> connected = new HashSet<Conductor>();
				for(Conductor c: conductors)
				{
					for(IConductor c1: c.conductors)
						if(areConnected(c1,component))
						{
							connected.add(c);
							break;
						}
				}
				if(connected.size()>0)
				{
					conductors.removeAll(connected);
					HashSet<IConductor> iConductors = new HashSet<IConductor>();
					for(Conductor c: connected)
					{
						points.removeAll(c.conductors);
						iConductors.addAll(c.conductors);
					}
					combineAndAddIConductors(iConductors);
					
				}
			}
			else
			{
				for(Component c: components)
				{
					if(c.device==component)
					{
						components.remove(c);
						break;
					}
				}
				points.remove(component);
			}
		}
	}
	
	
	public void combineAndAddIConductors(HashSet<IConductor> iConductors)
	{
		for(IConductor c:iConductors)
		{
			if(!points.contains(c))
			{
				IConductor[] ends = connectedConductors(c);
				HashSet<IConductor> toAdd = new HashSet<IConductor>();
				if(ends==null||ends.length==0)
					continue;
				IConductor end1 = ends[0];
				IConductor end2 = c;
				IConductor otherDir = ends.length==2?ends[1]:null;
				IConductor start = c;
				IConductor end = c;
				while(end1!=c)
				{
					ends = connectedConductors(end1);
					if(!toAdd.contains(end1));
						toAdd.add(end1);
					if(ends.length!=2)
					{
						end = end1;
						break;
					}
					IConductor temp = end1;
					end1 = ends[0]!=end2?ends[0]:ends[1];
					end2 = ends[0]!=temp?ends[0]:ends[1];
				}
				if(otherDir!=null)
				{
					end1 = otherDir;
					end2 = c;
					while(end1!=c)
					{
						ends = connectedConductors(end1);
						if(!toAdd.contains(end1));
							toAdd.add(end1);
						if(ends.length!=2)
						{
							start = end1;
							break;
						}
						IConductor temp = end1;
						end1 = ends[0]!=end2?ends[0]:ends[1];
						end2 = ends[0]!=temp?ends[0]:ends[1];
					}
				}
				points.addAll(toAdd);
				conductors.add(new Conductor(start, end, toAdd));
			}
		}
	}
	
	public boolean toConnectNetwork(INetworkConnection component)
	{
		boolean ret = false;
		for(INetworkConnection existing: points)
		{
			ret = ret || areConnected(component, existing);
			if(ret)
				return ret;
		}
		
		return false;
	}
	
	public int numberConnections(INetworkConnection component)
	{
		int ret = 0;
		if(component.validSides()==null)
			return ret;
		
		
		Vector3 A = component.getLocation();
		for(ForgeDirection side: component.validSides())
		{
			if(A.offset(side).getTileEntity(component.getWorld()) instanceof INetworkConnection
			&&areConnected(component,(INetworkConnection) A.offset(side).getTileEntity(component.getWorld())))
			{
				ret++;
			}
		}
		
		return ret;
	}
	
	public IConductor[] connectedConductors(IConductor component)
	{
		List<IConductor> ret = new ArrayList<IConductor>();
		if(component.validSides()==null)
			return null;
		
		
		Vector3 A = component.getLocation();
		for(ForgeDirection side: component.validSides())
		{
			if(A.offset(side).getTileEntity(component.getWorld()) instanceof IConductor
			&&areConnected(component,(INetworkConnection) A.offset(side).getTileEntity(component.getWorld())))
			{
				ret.add(((IConductor)A.offset(side).getTileEntity(component.getWorld())));
			}
		}
		if(ret.size()>0)
			return ret.toArray(new IConductor[ret.size()]);
		return null;
	}
	
	public static boolean areConnected(INetworkConnection pointA, INetworkConnection pointB)
	{
		Vector3 A = pointA.getLocation();
		Vector3 B = pointB.getLocation();
		
		if(pointA.validSides()==null||pointB.validSides()==null)
			return false;
		
		for(ForgeDirection side : pointA.validSides())
		{
			if(A.offset(side).sameBlock(B))
				return true;
		}
		
		return false;
	}
	
	public static class Component
	{
		public double resistance;
		public INetworkConnection device;
		
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
	}
	
	
	
	public static class Conductor
	{
		//The two ends.  I don't know if to store as Vectors or as IConductors.
		public IConductor nodeA;
		public IConductor nodeB;
		public double resistance;
		public HashSet<IConductor> conductors;
		
		//The two weakest links.
		public IConductor lowestPower;
		public IConductor lowestVoltage;
		
		/**
		 * 
		 * @param start One end Conductor
		 * @param end The other end Conductor
		 * @param conductors The set of Conductors, including the two end conductors.
		 */
		public Conductor(IConductor start, IConductor end, HashSet<IConductor> conductors)
		{
			nodeA = start;
			nodeB = end;
			this.conductors = conductors;
			init();
		}
		
		public void init()
		{
			resistance = 0;
			lowestPower = lowestVoltage = nodeA;
			for(IConductor c: conductors)
			{
				resistance += c.getResistance();
				if(c.getMaxPowerDissipation()<lowestPower.getMaxPowerDissipation())
				{
					lowestPower = c;
				}
				if(c.getMaxVoltage()<lowestVoltage.getMaxVoltage())
				{
					lowestVoltage = c;
				}
			}
		}
		
		public World getWorld()
		{
			if(nodeA!=null)
				return nodeA.getWorld();
			if(nodeB!=null)
				return nodeB.getWorld();			
			if(lowestPower!=null)
				return lowestPower.getWorld();
			if(lowestVoltage!=null)
				return lowestVoltage.getWorld();
			return null;
		}
	}
	
}