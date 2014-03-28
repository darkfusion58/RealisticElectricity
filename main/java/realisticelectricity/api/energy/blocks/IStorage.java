package realisticelectricity.api.energy.blocks;

import net.minecraftforge.common.util.ForgeDirection;
import realisticelectricity.api.energy.INetworkConnection;
import realisticelectricity.api.energy.IPolarDevice;

public interface IStorage extends INetworkConnection, IPolarDevice {
	/**
	 * The capacitance is simply a multiplier on the stored energy, and
	 * can be used to have differing storage amounts given the same voltage.
	 * 
	 * @return The device capacitance
	 */
	public double getCapacitance();
	/**
	 * Storage devices don't control their own stored energy, that is
	 * taken care of by the circuit simulator. This method is used
	 * by the circuit simulator to get the stored energy of this device.
	 * 
	 * @return The energy stored in the device
	 */
	public double getStoredEnergy();
	/**
	 * Storage devices don't control their own stored energy, that is
	 * taken care of by the circuit simulator. This method is used
	 * by the circuit simulator to set the stored energy of this device.
	 * 
	 * @param The energy stored in the device
	 */
	public void setStoredEnergy(double energy);
}
