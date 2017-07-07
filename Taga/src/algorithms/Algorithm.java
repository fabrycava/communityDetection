package algorithms;

import structure.Graph;

public abstract class Algorithm {
	
	Graph g;
	
	
	public Algorithm(Graph g){
		this.g=g;
	}
	
	public abstract void compute();
	protected abstract void createClusters();

}
