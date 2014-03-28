package realisticelectricity.energy;

import java.util.HashSet;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import realisticelectricity.api.events.*;

public class NetworkManager 
{
	public static HashSet<ElectricNetworkOld> networks = new HashSet<ElectricNetworkOld>();
	
	@SubscribeEvent
	public void onConnectEvent(ConnectEvent evt)
	{
		for(ElectricNetworkOld net : networks)
		{
		}
	}
	
	@SubscribeEvent
	public void onDisconnectEvent(DisconnectEvent evt)
	{
		for(ElectricNetworkOld net : networks)
		{
		}
	}
}
