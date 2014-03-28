package realisticelectricity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import realisticelectricity.api.energy.INetworkConnection;
import realisticelectricity.api.energy.IPolarDevice;
import realisticelectricity.api.energy.blocks.IConductor;
import realisticelectricity.api.energy.blocks.IConductorProvider;
import realisticelectricity.api.energy.blocks.IElectricBlock;
import realisticelectricity.api.energy.blocks.IGroundConnection;
import realisticelectricity.api.energy.blocks.IStorage;
import realisticelectricity.energy.Component;
import realisticelectricity.energy.Conductor;
import realisticelectricity.energy.ElectricNetwork;
import realisticelectricity.energy.Ground;
import realisticelectricity.energy.Machine;
import realisticelectricity.energy.Storage;
import realisticelectricity.utils.CubeEdge;
import thut.api.maths.Vector3;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 *  This class traces circuits, creates and modifies electric networks.
 *  
 * @author darkfusion58
 *
 */

/* TODO: cables on a side of a machine will both connect to the same node
 * add some sort of way to detect this and make one connect to the opposite
 * polarity
 */

public class CircuitTracer {
	public ElectricNetwork onNetworkBlockPlaced(ElectricNetwork net, INetworkConnection te) {
		
		HashMap<Integer,HashSet<INetworkConnection>> toTrace = new HashMap<Integer,HashSet<INetworkConnection>>();
		
		toTrace.put(ElectricNetwork.getNewNode(), new HashSet<INetworkConnection>());
		
		while(toTrace != null && !toTrace.isEmpty()) {
			
			HashMap<Integer,HashSet<INetworkConnection>> toTraceNext = new HashMap<Integer,HashSet<INetworkConnection>>();
			
			for(Integer node : toTrace.keySet()) {
				
				HashSet<INetworkConnection> toTraceNextSet = new HashSet<INetworkConnection>();
				
				for(INetworkConnection hte : toTrace.get(node)) {
					
					int endNode = -1;
					
					if(te instanceof IConductor) {
						endNode = traceWire(net, node, (IConductor)te, toTraceNextSet);
					} else if(te instanceof IPolarDevice) {
						endNode = traceMachine(net, node, (IPolarDevice)te, toTraceNextSet);
					} else if(te instanceof IGroundConnection) {
						endNode = traceGround(net, node, (IGroundConnection)te, toTraceNextSet);
					}
					
					if(endNode != -1) {
						toTraceNext.put(endNode, toTraceNextSet);
					}
				}
			}
			
			toTrace = toTraceNext;
		}
		
		return net;
	}
	
	int traceWire(ElectricNetwork net, int startNode, IConductor startwire, HashSet<INetworkConnection> endSet) {
		int endNode = -1;
		
		Conductor c = new Conductor();
		c.nodeA = startNode;
		c.nodeAConductor = startwire;
		
		IConductor curWire = startwire;
		
		while(true) {
			
			for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				
				Vector3 pos = startwire.getLocation();
				
				pos.x += dir.offsetX;
				pos.y += dir.offsetY;
				pos.z += dir.offsetZ;
				
				TileEntity te = startwire.getWorld().getTileEntity(pos.intX(), pos.intY(), pos.intZ());
				
				if(te != null) {
					
					INetworkConnection connect = null;
					
					if(te instanceof IConductorProvider) {
					} else if(te instanceof INetworkConnection) {
						
					}
				}
			}
		}
	}
	
	int traceMachine(ElectricNetwork net, int startNode, IPolarDevice device, HashSet<INetworkConnection> endSet) {
		int endNode = -1;
		
		return endNode;		
	}
	
	int traceGround(ElectricNetwork net, int startNode, IGroundConnection ground, HashSet<INetworkConnection> endSet) {
		int endNode = -1;
		
		return endNode;
	}
}

public class CircuitTracerA {
	
	private ElectricNetwork net;
	
	private HashSet<IConductor> wiresToBeAdded;
	
	private ElectricNetwork traceWireStart(IConductor wire, int startNode) {
		
		Conductor c = new Conductor();
		c.nodeA = startNode;
		c.nodeAConductor = wire;

		ElectricNetwork toMerge = new ElectricNetwork();
		
		if(traceFromWire(toMerge, c, wire, null)) {
			return toMerge;
		}
		
		return null;
	}
	
	private void terminateWire(ElectricNetwork toMerge, Conductor c, IConductor end, int endNode) {
		
		c.nodeBConductor = end;
		c.nodeB = endNode;
		
		toMerge.conductors.add(c);
	}
	
	private boolean traceFromWire(ElectricNetwork toMerge, Conductor c, IConductor wire, HashSet<IConductor> last) {
		
		int numConnections = 0;
		boolean endConnected = false;
		
		int groundNode = 0;
		
		IConductorProvider cprov = wire.getProvider();
		
		HashSet<INetworkConnection> forbiddenBacktrace = new HashSet<INetworkConnection>();
		
		boolean needsSplit = false, isGround = false;
		
		int endNode = ElectricNetwork.getNewNode();
		
		boolean[] edgeFlags = cprov.getOutputSides(wire);
		
		// add wire to Conductor, modify Conductor characteristics
		c.conductors.add(wire);
		c.resistance += wire.getResistance();
		
		if(c.lowestPower.getMaxPowerDissipation() > wire.getMaxPowerDissipation()) {
			c.lowestPower = wire;
		}
		
		if(c.lowestVoltage.getMaxVoltage() > wire.getMaxVoltage()) {
			c.lowestVoltage = wire;
		}
		
		wiresToBeAdded.add(wire);
		
		for(int i = 0; i < 12; i++) {
			if(edgeFlags[i]) {
				
				CubeEdge edge = CubeEdge.fromIndex(i);
				
				Vector3[] connectPos = edge.getBlocksSharingEdge();
				
				CubeEdge[] connectEdge = edge.getEdgesForBlocksSharingEdge();
				
				for(int j = 0; j < 3; j++) {
					
					Vector3 pos = wire.getLocation();
					pos.x += connectPos[j].x;
					pos.y += connectPos[j].y;
					pos.z += connectPos[j].z;
					
					TileEntity toTE = wire.getWorld().getTileEntity(pos.intX(), pos.intY(), pos.intZ());
					
					// don't trace things that aren't tile entities
					if(toTE == null) {
						continue;
					}
					
					if(toTE instanceof INetworkConnection) {
						
						ElectricNetwork machineNet = net;
						
						// we need to make sure that a machine is not diagonal
						// since we don't want machines to connect diagonally
						if(connectPos[j].intX() + connectPos[j].intY() + connectPos[j].intZ() > 1) {
							continue;
						}
						
						// don't retrace machines already in network
						if(!net.isPointInNetwork(((INetworkConnection)toTE)) &&
						   !toMerge.isPointInNetwork((INetworkConnection)toTE)) {
							
							machineNet = toMerge;
							
							// we can go ahead and trace machines into the network here
							// doneTODO: endConnected = endConnected || traceMachine
							
							endConnected = endConnected || traceFromMachine(toMerge, (INetworkConnection)toTE, wire.getProvider().getForbiddenForBacktrace(wire));
						} else if(net.isPointInNetwork(((INetworkConnection)toTE))) {
							endConnected = true;
						}
						
						// if we trace to a machine, we have to end the Conductor
						// even if the machine has already been traced, the
						// Conductor needs to connect to it
						needsSplit = true;
						
						// since the machine must have been traced by this point,
						// we need to get the node number to connect the wire to
						if(toTE instanceof IGroundConnection) {
							// for grounds, nodeA is always 0
							isGround = true;
							groundNode = machineNet.getComponent((IGroundConnection)toTE).nodeB;
						} else if(toTE instanceof IPolarDevice) {
							// for IPolarDevices, we follow the convention that
							// nodeA is the positive node and nodeB is the negative node
							
							IPolarDevice device = (IPolarDevice) toTE;
							
							// we need the side that the machine is on
							ForgeDirection inFace = directionFromOffset(connectPos[j]).getOpposite();
							
							int polarity = device.canConnectToSide(inFace);
							
							if(polarity != 1 && polarity != 2) { // no connection
								needsSplit = false;
							} else if(polarity == 1) { // positive
								endNode = machineNet.getComponent(device).nodeA;
							} else if(polarity == 2) { // negative
								endNode = machineNet.getComponent(device).nodeB;
							}
						}
					}
					
					if(toTE instanceof IConductorProvider) {
						
						IConductorProvider toCProv = (IConductorProvider) toTE;
						
						IConductor nextConductor = toCProv.getConductor(connectEdge[j]);
						
						// prevent backtracing to the same wire we just came from
						if(last != null && last.contains(nextConductor)) {
							continue;
						}
						
						// ignore Conductors being added in this recursion chain
						if(wiresToBeAdded.contains(nextConductor)) {
							continue;
						}
						
						// if we run into a wire that is already in the network that isn't
						// the one that we just traced, we have to establish a connection to it
						if(net.isPointInNetwork(nextConductor)) {
							Conductor toConductor = net.getConductor(nextConductor);
							
							// if the wire is already a node, our job is easy
							if(nextConductor == toConductor.nodeAConductor) {
								needsSplit = true;
								endNode = toConductor.nodeA;
								endConnected = true;
							} else if(nextConductor == toConductor.nodeBConductor) {
								needsSplit = true;
								endNode = toConductor.nodeB;
								endConnected = true;
							} else {
								// if the wire isn't a node, remove its Conductor
								// from the network and retrace it
								wiresToBeAdded.removeAll(toConductor.conductors);
								net.conductors.remove(toConductor);
							}
						}
						
						numConnections++;
					}
				}
			}
		}
		
		if(numConnections > 2) {
			needsSplit = true;
		}
		
		for(int i = 0; i < 12; i++) {
			if(edgeFlags[i]) {
				
				CubeEdge edge = CubeEdge.fromIndex(i);
				
				Vector3[] connectPos = edge.getBlocksSharingEdge();
				
				CubeEdge[] connectEdge = edge.getEdgesForBlocksSharingEdge();
				
				for(int j = 0; j < 3; j++) {
					
					Vector3 pos = wire.getLocation();
					pos.x += connectPos[j].x;
					pos.y += connectPos[j].y;
					pos.z += connectPos[j].z;
					
					TileEntity toTE = wire.getWorld().getTileEntity(pos.intX(), pos.intY(), pos.intZ());
					
					// don't trace things that aren't INetworkConnections
					if(toTE == null) {
						continue;
					}
					
					if(toTE instanceof IConductorProvider) {
						
						IConductorProvider toCProv = (IConductorProvider) toTE;
						
						IConductor nextConductor = toCProv.getConductor(connectEdge[j]);
						
						// prevent tracing conductors already in the network
						if(net.isPointInNetwork(nextConductor)) {
							continue;
						}
						
						// ignore Conductors being added in this recursion chain
						if(wiresToBeAdded.contains(nextConductor)) {
							continue;
						}
						
						// prevent backtracing to the same wire we just came from
						if(last != null && last.contains(nextConductor)) {
							continue;
						}
						
						if(needsSplit) {
							endConnected = endConnected ||
							 (traceWireStart(nextConductor, isGround ? groundNode : ElectricNetwork.getNewNode()) != null);
						} else {
							endConnected = endConnected || 
							 traceFromWire(toMerge, c, nextConductor, wire.getProvider().getForbiddenForBacktrace(wire));
						}
					}
				}
			}
		}
		
		// only add this wire to the circuit if
		// the end is connected to something already
		// in the network
		if(needsSplit && endConnected) {
			terminateWire(toMerge, c, wire, isGround ? groundNode : endNode);
		}
		
		return endConnected;
	}
	
	private boolean traceFromMachine(ElectricNetwork toMerge, INetworkConnection current, HashSet<IConductor> last) {
		
		boolean endConnected = false;
		
		int nodeA, nodeB;
		
		nodeA = -1;
		nodeB = -1;
		
		// this pass finds existing node to connect to the machine
		for(ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
			
			Vector3 loc = current.getLocation();
			
			loc.x += d.offsetX;
			loc.y += d.offsetY;
			loc.z += d.offsetZ;
			
			TileEntity te = current.getWorld().getTileEntity(loc.intX(), loc.intY(), loc.intZ());
			
			if(te instanceof IConductorProvider) {
				IConductorProvider cprov = (IConductorProvider) te;
				
				for(CubeEdge e : CubeEdge.getEdgesFromFace(d)) {
					
					ForgeDirection f1 = e.face1, f2 = e.face2;
					
					// this gets opposite edge
					// CubeEdge.getEdgesForBlocksSharingEdge normally does this
					// but we only want to check block mirrored across this face
					// not all the blocks sharing this edge
					// because we don't want diagonal connections to machines
					if(f1 == d) {
						f1 = f1.getOpposite();
					}
					if(f2 == d) {
						f2 = f2.getOpposite();
					}
					
					CubeEdge edge = CubeEdge.getEdgeFromFaces(f1, f2);
					
					IConductor nextConductor = cprov.getConductor(edge);
					
					if(last != null && last.contains(nextConductor)) {
						continue;
					}
					
					// if a wire is already in the network,
					// we need to connect to it
					if(net.isPointInNetwork(nextConductor)) {
						Conductor toConductor = net.getConductor(nextConductor);
						
						// if the wire is already a node, our job is (not so) easy
						if(nextConductor == toConductor.nodeAConductor) {
							if(current instanceof IPolarDevice) {
								IPolarDevice dev = (IPolarDevice) current;
								int polarity = dev.canConnectToSide(d);
								// for IPolarDevices, we follow the convention that
								// nodeA is the positive node and nodeB is the negative node
								if(polarity == 1) {
									nodeA = toConductor.nodeA;
								} else if(polarity == 2) {
									nodeB = toConductor.nodeA;
								}
							} else if(current instanceof IGroundConnection) {
								nodeA = 0;
								nodeB = toConductor.nodeA;
							}
							endConnected = true;
						} else if(nextConductor == toConductor.nodeBConductor) {
							if(current instanceof IPolarDevice) {
								IPolarDevice dev = (IPolarDevice) current;
								int polarity = dev.canConnectToSide(d);
								// for IPolarDevices, we follow the convention that
								// nodeA is the positive node and nodeB is the negative node
								if(polarity == 1) {
									nodeA = toConductor.nodeB;
								} else if(polarity == 2) {
									nodeB = toConductor.nodeB;
								}
							} else if(current instanceof IGroundConnection) {
								nodeA = 0;
								nodeB = toConductor.nodeA;
							}
							endConnected = true;
						} else {
							// if the wire isn't a node, remove its Conductor
							// from the network and retrace it
							wiresToBeAdded.removeAll(toConductor.conductors);
							net.conductors.remove(toConductor);
						}
					}
					
					// do the same thing with wires already traced in this network,
					// except do not set endConnected
					if(wiresToBeAdded.contains(nextConductor)) {
						continue;
					}
					
//					if(!endConnected) {
//						traceWireStart(nextConductor, 0);
//					}
				}
			}
		}
		
		if(nodeA == -1) {
			if(current instanceof IGroundConnection) {
				nodeA = 0;
			} else {
				nodeA = ElectricNetwork.getNewNode();
			}
		}
		
		if(nodeB == -1) {
			nodeB = ElectricNetwork.getNewNode();
		}
		
		// this pass traces adjacent wires
		for(ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
			
			Vector3 loc = current.getLocation();
			
			loc.x += d.offsetX;
			loc.y += d.offsetY;
			loc.z += d.offsetZ;
			
			TileEntity te = current.getWorld().getTileEntity(loc.intX(), loc.intY(), loc.intZ());
			
			if(te instanceof IConductorProvider) {
				IConductorProvider cprov = (IConductorProvider) te;
				
				for(CubeEdge e : CubeEdge.getEdgesFromFace(d)) {
					
					ForgeDirection f1 = e.face1, f2 = e.face2;
					
					// this gets opposite edge
					// CubeEdge.getEdgesForBlocksSharingEdge normally does this
					// but we only want to check block mirrored across this face
					// not all the blocks sharing this edge
					// because we don't want diagonal connections to machines
					if(f1 == d) {
						f1 = f1.getOpposite();
					}
					if(f2 == d) {
						f2 = f2.getOpposite();
					}
					
					CubeEdge edge = CubeEdge.getEdgeFromFaces(f1, f2);
					
					IConductor nextConductor = cprov.getConductor(edge);
					
					if(last != null && last.contains(nextConductor)) {
						continue;
					}
					
					if(net.isPointInNetwork(nextConductor)) {
						continue;
					}
					
					// ignore Conductors being added in this recursion chain
					if(wiresToBeAdded.contains(nextConductor)) {
						continue;
					}
					
					if(current instanceof IPolarDevice) {
						IPolarDevice dev = (IPolarDevice) current;
						int polarity = dev.canConnectToSide(d);
						// for IPolarDevices, we follow the convention that
						// nodeA is the positive node and nodeB is the negative node
						if(polarity == 1) {
							endConnected = endConnected || (traceWireStart(nextConductor, nodeA) != null);
						} else if(polarity == 2) {
							endConnected = endConnected || (traceWireStart(nextConductor, nodeB) != null);
						}
					} else if(current instanceof IGroundConnection) {
						endConnected = endConnected || (traceWireStart(nextConductor, nodeB) != null);
					}
				}
			}
		}
		
		if(endConnected) {
			toMerge.addComponent(current, nodeA, nodeB);
		}
		
		return endConnected;
	}
	
	private static ForgeDirection directionFromOffset(Vector3 offset) {
		for(ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
			if(offset.intX() == d.offsetX && offset.intY() == d.offsetY && offset.intZ() == d.offsetZ) {
				return d;
			}
		}
		
		return ForgeDirection.UNKNOWN;
	}
	
	private static boolean getConnectionExists(boolean[] conInfo) {
		
		for(boolean b : conInfo) {
			if(b) {
				return true;
			}
		}
		
		return false;
	}
	
	private static HashSet hashSetFromArray(Object[] array) {
		
		HashSet hset = new HashSet();
		
		for(Object o : array) {
			hset.add(o);
		}
		
		return hset;
	}

}
