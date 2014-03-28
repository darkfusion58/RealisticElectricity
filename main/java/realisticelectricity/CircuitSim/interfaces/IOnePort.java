package realisticelectricity.CircuitSim.interfaces;

//polarity is always second node positive, first negative

public interface IOnePort {
	public abstract int getFirstNode();
	public abstract int getSecondNode();
	
	// feeds back the solution, v(node1) - v(node2)
	public abstract void voltageSolution(double v);
}
