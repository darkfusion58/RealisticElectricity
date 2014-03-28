package realisticelectricity;

import net.minecraftforge.common.MinecraftForge;
import realisticelectricity.energy.NetworkManager;
import realisticelectricity.network.PacketPipeline;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;


@Mod( modid = "Realectric", name="Realistic Electricity", version="0.0.0")

public class RealisticElectricity 
{
	@SidedProxy(clientSide = "realisticelectricity.client.ClientProxy", serverSide = "realisticelectricity.CommonProxy")
	public static CommonProxy commproxy;
	
	public static CreativeTabRE creativeTab = new CreativeTabRE();
	
	@Instance("Realectric")
	public static RealisticElectricity instance;
	
	public NetworkManager networkManager;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		networkManager = new NetworkManager();
		MinecraftForge.EVENT_BUS.register(networkManager);
	}
	
	@EventHandler
	public void load(FMLInitializationEvent evt)
	{
		PacketPipeline.packetPipeline.initalise();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent e)
	{
		PacketPipeline.packetPipeline.postInitialise();
	}
}
