package realisticelectricity.client;

import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import realisticelectricity.CommonProxy;

public class ClientProxy extends CommonProxy
{
	@Override
	public void initClient()
	{
		
	}
	
    @Override
    public World getClientWorld()
    {
        return FMLClientHandler.instance().getClient().theWorld;
    }
}
