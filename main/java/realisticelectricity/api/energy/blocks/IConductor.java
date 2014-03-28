package realisticelectricity.api.energy.blocks;

import net.minecraftforge.common.util.ForgeDirection;
import realisticelectricity.api.color.IColorable;
import realisticelectricity.api.energy.INetworkConnection;

/**
 *  If a device implements this interface, It will be considered as only a conductor, and 
 *  will be lumped with other conductors if it occurs in an un-branched chain.  These
 *  devices will only receive onOverVoltage and onOverHeat, and will not be updated
 *  about anything else that happens in the network.
 *  
 *  Do not have a tile entity implement this interface, the tile entity should instead
 *  implement IConductorProvider.
 *  
 * @author Thutmose
 *
 */
public interface IConductor extends IColorable, INetworkConnection
{
	public abstract IConductorProvider getProvider();
}
