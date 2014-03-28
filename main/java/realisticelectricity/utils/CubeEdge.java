package realisticelectricity.utils;

import java.util.HashSet;

import thut.api.maths.Vector3;

import net.minecraftforge.common.util.ForgeDirection;

public enum CubeEdge
{
	
	DOWN_NORTH(ForgeDirection.DOWN, ForgeDirection.NORTH),
	DOWN_EAST(ForgeDirection.DOWN, ForgeDirection.EAST),
	DOWN_SOUTH(ForgeDirection.DOWN, ForgeDirection.SOUTH),
	DOWN_WEST(ForgeDirection.DOWN, ForgeDirection.WEST),
	NORTH_EAST(ForgeDirection.NORTH, ForgeDirection.EAST),
	EAST_SOUTH(ForgeDirection.EAST, ForgeDirection.SOUTH),
	SOUTH_WEST(ForgeDirection.SOUTH, ForgeDirection.WEST),
	WEST_NORTH(ForgeDirection.WEST, ForgeDirection.NORTH),
	UP_NORTH(ForgeDirection.UP, ForgeDirection.NORTH),
	UP_EAST(ForgeDirection.UP, ForgeDirection.EAST),
	UP_SOUTH(ForgeDirection.UP, ForgeDirection.SOUTH),
	UP_WEST(ForgeDirection.UP, ForgeDirection.WEST),

    /**
     * Used only by getOrientation, for invalid inputs
     */
    UNKNOWN(ForgeDirection.UNKNOWN, ForgeDirection.UNKNOWN);

    public final ForgeDirection face1, face2;
    public static final CubeEdge[] VALID_EDGES = {DOWN_NORTH, DOWN_EAST, DOWN_SOUTH, DOWN_WEST,
    											  NORTH_EAST, EAST_SOUTH, SOUTH_WEST, WEST_NORTH,
    											  UP_NORTH, UP_EAST, UP_SOUTH, UP_WEST};

    private CubeEdge(ForgeDirection f1, ForgeDirection f2) {
    	face1 = f1;
    	face2 = f2;
    }
    
    public static CubeEdge fromIndex(int index) {
    	return VALID_EDGES[index];
    }
    
    public static CubeEdge[] getEdgesFromFace(ForgeDirection face) {
    	
    	CubeEdge[] edges = new CubeEdge[4];
    	int index = 0;
    	
    	for(CubeEdge edge : CubeEdge.VALID_EDGES) {
    		if(edge.face1 == face || edge.face2 == face) {
    			edges[index] = edge;
    			index++;
    		}
    	}
    	
    	return edges;
    }
    
    public static CubeEdge getEdgeFromFaces(ForgeDirection f1, ForgeDirection f2) {
    	
    	for(CubeEdge edge : CubeEdge.VALID_EDGES) {
    		if((edge.face1 == f1 && edge.face2 == f2) || (edge.face1 == f2 && edge.face2 == f1)) {
    			return edge;
    		}
    	}
    	
    	return UNKNOWN;
    }
    
    public CubeEdge getOppositeOnFace(ForgeDirection face) {
    	
    	ForgeDirection f1, f2;
    	
    	f1 = this.face1;
    	f2 = this.face2;
    	
    	if(f1 == face) {
    		f1 = f1.getOpposite();
    	}
    	
    	if(f2 == face) {
    		f2 = f2.getOpposite();
    	}
    	
    	return getEdgeFromFaces(f1, f2);
    }
    
    /**
     * 
     * @return An array of three coordinates specifying the other blocks that share this edge.
     */
    public Vector3[] getBlocksSharingEdge() {
    	
    	Vector3[] adj = new Vector3[3];
    	
    	Vector3 pos = new Vector3();
    	
    	pos.x += this.face1.offsetX;
    	pos.y += this.face1.offsetY;
    	pos.z += this.face1.offsetZ;
    	
    	adj[0] = pos;
    	
    	pos.x += this.face2.offsetX;
    	pos.y += this.face2.offsetY;
    	pos.z += this.face2.offsetZ;
    	
    	adj[1] = pos;
    	
    	pos.x -= this.face1.offsetX;
    	pos.y -= this.face1.offsetY;
    	pos.z -= this.face1.offsetZ;
    	
    	adj[2] = pos;
    	
    	return adj;
    }
    
    /**
     * 
     * @return An array of CubeEdges that represent this edge for the other blocks sharing this edge.
     */
    public CubeEdge[] getEdgesForBlocksSharingEdge() {
    	
    	CubeEdge[] adj = new CubeEdge[3];
    	
    	adj[0] = CubeEdge.getEdgeFromFaces(this.face1.getOpposite(), this.face2);
    	adj[1] = CubeEdge.getEdgeFromFaces(this.face1.getOpposite(), this.face2.getOpposite());
    	adj[2] = CubeEdge.getEdgeFromFaces(this.face1, this.face2.getOpposite());
    	
    	return adj;
    }
}