package realisticelectricity.api.energy;

import net.minecraftforge.common.util.ForgeDirection;

public interface IPolarDevice extends INetworkConnection {
	/**
	 * 
	 * @param dir - the side in question
	 * @return Can this connect to side dir? 0: no 1: positive 2: negative
	 */
	public byte canConnectToSide(ForgeDirection dir);
}
