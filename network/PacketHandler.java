package realisticelectricity.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import realisticelectricity.RealisticElectricity;
import thutconcrete.api.network.IPacketProcessor;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.ITinyPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler
{
	
	Map<Integer, IPacketProcessor> packetTypes = new HashMap<Integer, IPacketProcessor>();
	static Map<Integer, Integer> packetCounts = new HashMap<Integer, Integer>();
	
	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload packet, Player player) {

		if(!packet.channel.contentEquals("Realectric")) return;
		
		World world = RealisticElectricity.commproxy.getClientWorld();
		ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
		
		RealisticElectricity.instance.pkthandler.handlePacket(dat, player, world);
	}
	
	public PacketHandler()
	{
	//	packetTypes.put(0, new PacketStampable());
	}
	
	public void handlePacket(ByteArrayDataInput dat,Player player,World world)
	{
		int id = dat.readInt();
	//	System.out.println("Packet ID: "+id);
		packetTypes.get(id).processPacket(dat, player, world);
	}
	
}