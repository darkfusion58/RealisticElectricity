package realisticelectricity.energy;

import java.util.HashSet;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import realisticelectricity.api.events.*;

public class NetworkManager 
{
	public static HashSet<ElectricNetwork> networks = new HashSet<ElectricNetwork>();
	
	@SubscribeEvent
	public void onConnectEvent(ConnectEvent evt)
	{
		for(ElectricNetwork net : networks)
		{
			if(net.toConnectNetwork(evt.component))
			{
				net.connectToNetwork(evt.component);
			}
		}
	}
	
	@SubscribeEvent
	public void onDisconnectEvent(DisconnectEvent evt)
	{
		for(ElectricNetwork net : networks)
		{
			if(net.contains(evt.component))
			{
				net.disconnectFromNetwork(evt.component);
			}
		}
	}
}
