package realisticelectricity.CircuitSim.interfaces;

// resistance of voltage sources CANNOT be zero!
// polarity is always second node positive, first negative

public interface IVoltageSource extends IOnePort {
	public abstract double getVoltage();
	public abstract double getResistance();
}
