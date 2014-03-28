package realisticelectricity.CircuitSim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;

import realisticelectricity.CircuitSim.interfaces.IOnePort;
import realisticelectricity.CircuitSim.interfaces.IPassive;
import realisticelectricity.CircuitSim.interfaces.IVoltageSource;

public class Circuit {

	protected List<Equation> system;
	
	protected Map<Integer, IPassive> passives;
	protected Map<Integer, IVoltageSource> voltageSources;
	protected int currentIndex;
	
	protected int referenceNode;
	
	public Circuit() {
		system = new ArrayList<Equation>();

		passives = new HashMap<Integer, IPassive>();
		voltageSources = new HashMap<Integer, IVoltageSource>();
		currentIndex = 0;
	}
	
	public int addNetList(NetList net) {
		int beginIndex = currentIndex;
		
		for(IPassive p : net.passives) {
			passives.put(currentIndex, p);
			currentIndex++;
		}
		
		for(IVoltageSource vs : net.voltageSources) {
			voltageSources.put(currentIndex, vs);
			currentIndex++;
		}
		
		return beginIndex;
	}
	
	public int addComponent(IOnePort c) {
		
		if(c instanceof IPassive) {
			passives.put(currentIndex, (IPassive) c);
			currentIndex++;
		} else if(c instanceof IVoltageSource) {
			voltageSources.put(currentIndex, (IVoltageSource) c);
			currentIndex++;
		}
		
		return currentIndex - 1;
	}
	
	public boolean removeComponent(int index) {
		boolean found = false;
		
		if(passives.containsKey(index)) {
			passives.remove(index);
			found = true;
		} else if(voltageSources.containsKey(index)) {
			voltageSources.remove(index);
			found = true;
		}
		
		return found;
	}
	
	public void setReferenceNode(int node) {
		referenceNode = node;
	}
	
	protected void feedbackSolutions(Map<Integer, Double> voltages) {
		for(int i : passives.keySet()) {
			double vDrop = voltages.get(passives.get(i).getSecondNode()) - voltages.get(passives.get(i).getFirstNode());
			
			passives.get(i).voltageSolution(vDrop);
		}
		
		for(int i : voltageSources.keySet()) {
			double vDrop = voltages.get(voltageSources.get(i).getSecondNode()) - voltages.get(voltageSources.get(i).getFirstNode());
			
			voltageSources.get(i).voltageSolution(vDrop);
		}
	}
	
	public Map<Integer, Double> solve() {
		
		Map<Integer, Double> voltages = new HashMap<Integer, Double>();
		
		if(system.size() == 0) {
			return null;
		}
		
		int maxNode = getMaxNode();
		
		DenseMatrix64F A = new DenseMatrix64F(system.size() + 1, maxNode + 1);
		DenseMatrix64F x = new DenseMatrix64F(maxNode + 1, 1);
		DenseMatrix64F b = new DenseMatrix64F(system.size() + 1, 1);
		
		for(int i = 0; i < system.size(); i++) {
			Equation eq = system.get(i);
			
			for(int j = 0; j <= maxNode; j++) {
				A.set(i, j, eq.getCoefficient(j));
			}
			
			b.set(i, 0, eq.getConstPart());
		}
		
		A.set(system.size(), referenceNode, 1.0d);
		b.set(system.size(), 0.0d);
		
//		System.out.println("A: " + A.toString());
//		System.out.println("b: " + b.toString());
		
		if(!CommonOps.solve(A, b, x)) {
			throw new IllegalArgumentException("System is singular.");
		}
		
//		System.out.println("x: " + x.toString());
		
		for(int i = 0; i <= maxNode; i++) {
			voltages.put(i, x.get(i, 0));
		}
		
		this.feedbackSolutions(voltages);
		
		return voltages;
	}
	
	public void buildEquations() {
		
		int maxNode = getMaxNode();
		
		system.clear();
		
		for(int i = 0; i <= maxNode; i++) {
			if(i != referenceNode) {
				system.add(buildEquation(i));
			}
		}
	}
	
	private int getMaxNode() {
		
		int maxNode = 0;
		
		for(int i : passives.keySet()) {
			if(passives.get(i).getFirstNode() > maxNode) {
				maxNode = passives.get(i).getFirstNode();
			}
			
			if(passives.get(i).getSecondNode() > maxNode) {
				maxNode = passives.get(i).getSecondNode();
			}
		}
		
		for(int i : voltageSources.keySet()) {
			if(voltageSources.get(i).getFirstNode() > maxNode) {
				maxNode = voltageSources.get(i).getFirstNode();
			}
			
			if(voltageSources.get(i).getSecondNode() > maxNode) {
				maxNode = voltageSources.get(i).getSecondNode();
			}
		}
		
		return maxNode;
	}
	
	protected Equation buildEquation(int node) {
		
		Equation eq = new Equation();
		
		for(int i : passives.keySet()) {
			
			double resist = passives.get(i).getResistance();
			
			if(passives.get(i).getFirstNode() == node) {
				eq.Add(1.0d/resist, node);
				eq.Add(-1.0d/resist, passives.get(i).getSecondNode());
			} else if(passives.get(i).getSecondNode() == node) {
				eq.Add(1.0d/resist, node);
				eq.Add(-1.0d/resist, passives.get(i).getFirstNode());
			}
		}
		
		for(int i : voltageSources.keySet()) {
			
			double resist = voltageSources.get(i).getResistance();
			double voltage = voltageSources.get(i).getVoltage();
			
			if(voltageSources.get(i).getFirstNode() == node) {
				eq.Add(1.0d/resist, node);
				eq.Add(-1.0d/resist, voltageSources.get(i).getSecondNode());
				eq.AddConst(-voltageSources.get(i).getVoltage()/voltageSources.get(i).getResistance());
			} else if(voltageSources.get(i).getSecondNode() == node) {
				eq.Add(1.0d/resist, node);
				eq.Add(-1.0d/resist, voltageSources.get(i).getFirstNode());
				eq.AddConst(voltageSources.get(i).getVoltage()/voltageSources.get(i).getResistance());
			}
		}
		
		return eq;
	}
}
