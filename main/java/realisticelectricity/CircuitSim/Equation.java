package realisticelectricity.CircuitSim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Equation {

	private static class EquationTerm {
		
		public double coeff;
		public int term;
		
		public EquationTerm() {
			this(0.0d, 0);
		}
		
		public EquationTerm(int t) {
			this(0.0d, t);
		}
		
		public EquationTerm(double c, int t) {
			coeff = c;
			term = t;
		}
	}
	
	private List<EquationTerm> terms;
	private double constTerm;
	
	private EquationTerm lastfind;
	private int lastfindterm;
	
	public Equation() {
		terms = new ArrayList<EquationTerm>();
		lastfind = null;
		lastfindterm = 0;
		constTerm = 0;
	}
	
	public void Add(Equation e) {
		
		for(EquationTerm et : e.terms) {
			if(this.containsTerm(et.term)) {
				this.getTerm(et.term).coeff += et.coeff;
			} else {
				this.terms.add(et);
			}
		}
	}

	public void Add(double coeff, int term) {

		if(this.containsTerm(term)) {
			this.getTerm(term).coeff += coeff;
		} else {
			this.terms.add(new EquationTerm(coeff, term));
		}
	}
	
	public void AddConst(double c) {
		
		constTerm += c;
	}

	public double getCoefficient(int term) {
		
		if(containsTerm(term)) {
			EquationTerm et = getTerm(term);
			return et.coeff;
		}
		
		return 0.0d;
	}
	
	public double getConstPart() {
		return constTerm;
	}
	
	private boolean containsTerm(int term) {
		
		for(EquationTerm et : terms) {
			if(et.term == term) {
				lastfindterm = term;
				lastfind = et;
				return true;
			}
		}
		
		lastfind = null;
		
		return false;
	}
	
	private EquationTerm getTerm(int term) {
		
		if(lastfindterm == term && lastfind != null) {
			return lastfind;
		}
		
		for(EquationTerm et : terms) {
			if(et.term == term) {
				lastfindterm = term;
				lastfind = et;
				return et;
			}
		}
		
		lastfind = null;
		
		return null;
	}
	
	public String toString() {
		
		String s = new String();
		
		for(EquationTerm et : terms) {
			s += et.coeff;
			s += "v";
			s += et.term;
			
			if(terms.get(terms.size() - 1) != et) {
				s += " + ";
			}
		}
		
		s += " = ";
		s += constTerm;
		
		return s;
		
	}
}
