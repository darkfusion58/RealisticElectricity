package realisticelectricity.api.energy.blocks;

import java.util.HashSet;

import realisticelectricity.api.energy.INetworkConnection;
import realisticelectricity.utils.CubeEdge;
import thut.api.maths.Vector3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 *  If a device implements this interface, It will be considered as only a conductor, and 
 *  will be lumped with other conductors if it occurs in an un-branched chain.  These
 *  devices will only receive onOverVoltage and onOverHeat, and will not be updated
 *  about anything else that happens in the network.
 *  
 *  The tile entity should implement this interface. Valid numbers of provided
 *  conductors are 1 or 2. Any more will cause an error.
 *  
 * @author darkfusion58
 *
 */

public interface IConductorProvider {
	/**
	 * 
	 * @return tileEntity.worldObj
	 */
	public World getWorld();
	
	/**
	 * 
	 * @return The location of this device connected to the network
	 */
	public Vector3 getLocation();
	
	/**
	 * 
	 * This function is specifically meant for dealing with microblock conductors. It can be
	 * used with non-microblock conductors by returning all edges for the output face that you want.
	 * The output should NOT include the input edge.
	 * 
	 * This method skips the IConductor for ease of use, but those are also needed.
	 * 
	 * @param inEdge The input edge.
	 * @return An array of 12 booleans indicating the output edges, given the input edge.
	 */
	public abstract boolean[] getConnectivity(CubeEdge inEdge);
	
	/**
	 * Simply another way of specifying connectivity, see getConnectivity.
	 * 
	 * @param inEdge The input edge.
	 * @return Either a single IConductor representing the conductor going from the specified input edge
	 * @return to the output edge(s), or null if no such conductor exists.
	 */
	public abstract IConductor getConductor(CubeEdge inEdge);
	
	/**
	 * Simply another way of specifying connectivity, see getConnectivity.
	 * 
	 * @param conductor The conductor in question.
	 * @return An array of 12 booleans indicating the output edges, given the input conductor.
	 */
	public abstract boolean[] getOutputSides(IConductor conductor);
	
	/**
	 * Imagine a cable that has two wires in it. Using this interface, the way that one would
	 * implement such a thing is to have one wire have an input and output face that are the
	 * output and input face, respectively, of the other wire. However, consider attempting to
	 * trace such a wire: you find this block, and check all adjacent blocks to see if they are
	 * connected. If one of them is connected, you go to that block, and check blocks adjacent to it, and
	 * so on. The important question is:
	 * 
	 * How can the tracer tell not to trace back to the cable that it just came from?
	 * 
	 * The tracer cannot identify previous wires by IConductor, since the block has two IConductors,
	 * one for each of the two wires in the cable. One could identify them by the IConductorProvider,
	 * but this causes problems also:
	 * 
	 * Consider a microblock wire which can contain 6 possible IConductors, one for each face. Imagine
	 * that this block has one wire on the top face and one on the bottom. Now imagine that an adjacent
	 * block has one wire on the top face, one on the bottom, AND one on one of the side faces.
	 * 
	 * In this case, you actually WANT the tracer to trace back to the block that it just came from.
	 * 
	 * This function outputs a HashSet of which IConductors that it contains cannot be traced directly
	 * back to from an adjacent IConductorProvider.
	 * 
	 * In the case of a cable connecting to a machine, you always want it to ignore this list and go
	 * ahead and backtrace anyway. In that case, this list has a totally different meaning: it is a
	 * list of IConductors for which the polarity of connection to a machine ought to be reversed from
	 * what the machines IPolarDevice sided polarity returns. This allows bundles of two wires to
	 * connect to both the positive and negative nodes of a machine.
	 * 
	 * @param The IConductor that was last traced in this block.
	 * @return This function outputs a HashSet of which IConductors that it contains cannot be traced directly
	 * @return back to from an adjacent IConductorProvider.
	 */
	public abstract HashSet<IConductor> getForbiddenForBacktrace(IConductor c);
}
