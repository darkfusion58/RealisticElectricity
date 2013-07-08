package realisticelectricity.api.energy.blocks;

import realisticelectricity.api.energy.INetworkConnection;

/**
 *  If a device implements this interface, It will be considered as only a conductor, and 
 *  will be lumped with other conductors if it occurs in an un-branched chain.  These
 *  devices will only receive onOverVoltage and onOverHeat, and will not be updated
 *  about anything else that happens in the network.
 *  
 *  
 * @author Thutmose
 *
 */
public interface IConductor extends INetworkConnection
{
	
}
