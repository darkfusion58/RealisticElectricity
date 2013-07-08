package realisticelectricity.api.energy;

import thutconcrete.api.utils.Vector3;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public interface INetworkConnection 
{
	/**
	 * 
	 * @return tileEntity.worldObj
	 */
	public World getWorld();
	
	/**
	 * 
	 * @return An array of the valid ForgeDirections for connections.  should return null if no valid connections.
	 */
	public ForgeDirection[] validSides();
	
	/**
	 * 
	 * @return The location of this device connected to the network
	 */
	public Vector3 getLocation();
	
	/**
	 * 
	 * @return The resistance of the device in Ohms
	 */
	public double getResistance();

	/**
	 * 
	 * @return The voltage over which this device will have onOverVoltage called.
	 */
	public double getMaxVoltage();
	
	/**
	 * one way to determine this is to decide a maximum current, then set this to return
	 * MaxCurrent * MaxCurrent * resistance, where resistance is the value returned by
	 * getResistance()
	 * 
	 * @return The amount of power over which this device will have onOverHeat called.
	 */
	public double getMaxPowerDissipation();
	
	/**
	 * What does this do when the voltage is over the max voltage reported by getMaxVoltage()?
	 * @param voltage
	 */
	public void onOverVoltage(double voltage);
	
	/**
	 * What does this do when the power is over the max power reported by getMaxPowerDissipation()?
	 * @param power
	 */
	public void onOverHeat(double power);
}
