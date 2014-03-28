package realisticelectricity.api.energy.blocks;

import realisticelectricity.api.energy.INetworkConnection;

/**
 *  Grounds are necessary for circuits to work, since they need
 *  a reference node. the circuit tracer also starts at ground
 *  connections.
 *  
 * @author darkfusion58
 *
 */

public interface IGroundConnection extends INetworkConnection {

}
