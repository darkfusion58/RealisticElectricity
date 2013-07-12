package realisticelectricity;

import net.minecraftforge.common.MinecraftForge;
import realisticelectricity.energy.NetworkManager;
import realisticelectricity.network.PacketHandler;
import realisticelectricity.ticks.TickHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;


@Mod( modid = "Realectric", name="Realistic Electricity", version="0.0.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = true, 
channels={"Realectric"},
packetHandler = PacketHandler.class
)

public class RealisticElectricity 
{
	@SidedProxy(clientSide = "realisticelectricity.client.ClientProxy", serverSide = "realisticelectricity.CommonProxy")
	public static CommonProxy commproxy;
	public static TickHandler tickHandler = new TickHandler();
	public static CreativeTabRE creativeTab = new CreativeTabRE();
	
	@Instance("Realectric")
	public static RealisticElectricity instance;
	
	public PacketHandler pkthandler;
	public NetworkManager networkManager;
	
	@PreInit
	public void preInit(FMLPreInitializationEvent e)
	{
		networkManager = new NetworkManager();
		MinecraftForge.EVENT_BUS.register(networkManager);
	}
	
	@Init
	public void load(FMLInitializationEvent evt)
	{
		pkthandler = new PacketHandler();
		TickRegistry.registerTickHandler(tickHandler, Side.SERVER);
	}
	
	@PostInit
	public void postInit(FMLPostInitializationEvent e)
	{
		
	}
}
