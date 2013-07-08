package realisticelectricity.api.events;

import realisticelectricity.api.energy.blocks.IElectricBlock;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class ConnectEvent extends WorldEvent
{
	public IElectricBlock component;
	public ConnectEvent(IElectricBlock block, World world) 
	{
		super(world);
		component = block;
	}

}
