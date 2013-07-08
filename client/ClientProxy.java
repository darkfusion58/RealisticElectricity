package realisticelectricity.client;

import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import realisticelectricity.CommonProxy;

public class ClientProxy extends CommonProxy
{
	public static ClientTickHandler TH = new ClientTickHandler();
	
	
	@Override
	public void initClient()
	{
		TickRegistry.registerTickHandler(TH, Side.CLIENT);
	}
	
    @Override
    public World getClientWorld()
    {
        return FMLClientHandler.instance().getClient().theWorld;
    }
}
