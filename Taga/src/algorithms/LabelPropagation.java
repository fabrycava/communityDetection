package algorithms;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import structure.Cluster;
import structure.Graph;
import structure.Node;

public class LabelPropagation extends Algorithm{

	

	public LabelPropagation(Graph g) {
		super(g);
	}

	public void compute() {

		Iterator<Node> it = g.iterator();

		// Step1
		while (it.hasNext()) {
			Node n = it.next();
			n.setLabel(n.getId());
		}

		// Step2
		boolean convergence = false;
		while (!convergence) {
			g.shuffleNodes();
			it = g.shuffledIterator();
			while (it.hasNext()) {
				Node n = it.next();
				n.setLabel(computeMaxNeighbor(n));
				//System.out.println("nodo " + n + " settato a "+ n.getLabel());
			}

			it = g.iterator();
			while (it.hasNext()) {
				Node n = it.next();
				if (n.getLabel() != computeMaxNeighbor(n)) {
					convergence = false;
					break;
				} else {
					convergence = true;
				}

			}
		}
		
		//System.out.println("inizio creazione cluster\n");
		createClusters();

	}

	protected void createClusters() {

		for (Node n : g.getNodes().values()) {
			//System.out.println("node " + n + " va in cluster " + n.getLabel());
			g.addNodeInCluster(n);
		}
	}

	private String computeMaxNeighbor(Node n) {

		LinkedList<String> neighbors = n.getNeighbors();
		Collections.shuffle(neighbors);
		HashMap<String, Integer> map = new HashMap<>();
		int max = 0;
		String stringMax = null;
		for (String s1 : neighbors) {
			String s=g.getNode(s1).getLabel();
			if (map.containsKey(s)) {
				int v = map.get(s);
				v++;
				map.put(s, v);

				if (v > max) {
					max = v;
					stringMax = s;
				}
			} else {
				if (map.isEmpty())
					stringMax = s;
				map.put(s, 1);
			}

		}
		
		//System.err.println(map);
		Object[] values = new Object[2];
		values[0] = max;
		values[1] = stringMax;
		//System.out.println("best Neigh of " + n.getId() + " is " + stringMax);
		return stringMax;
	}

}
