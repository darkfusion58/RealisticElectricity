package realisticelectricity.api.energy.blocks;

import net.minecraftforge.common.util.ForgeDirection;
import realisticelectricity.api.energy.EnergyPack;
import realisticelectricity.api.energy.INetworkConnection;
import realisticelectricity.api.energy.IPolarDevice;

public interface IElectricBlock extends INetworkConnection, IPolarDevice
{
	/**
	 * In the case of a source, this would return the maximum voltage output of the device.  
	 * In the case of A sink (something that requires/takes energy) this will return 0
	 * 
	 * @return The device specific voltage
	 */
	public double getWorkingVoltage();
	
	/**
	 * This is called whenever the network takes energy from this device to send to another.
	 * @param energy - the energy pack sent from the device, in Joules.
	 */
	public void onEnergySent(EnergyPack energy);
	
	/**
	 * This is called whenever this device receives energy from the network.  This will not correspond to more power
	 * than the value stated in INetworkConnection's max power dissipation, and will not correspond to a higher voltage
	 * than the maximum voltage, those two cases will be handled first via their own methods.
	 * @param energy - The energy packet given to the device, in Joules.
	 */
	public void onEnergyFeedback(EnergyPack energy);
}
