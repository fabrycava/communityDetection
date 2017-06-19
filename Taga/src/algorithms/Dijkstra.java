package algorithms;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import structure.Graph;
import structure.Node;

public class Dijkstra {
	Graph g = new Graph();

	public Dijkstra(Graph g) {
		this.g = g;
	}

	public HashMap<String, Double> dijkstra(String nodoPartenza) {
		int n = g.getN();
		// System.out.println("N=" + n);
		HashMap<String, Double> dist = new HashMap<>();

		for (Node node : g.getNodes().values())
			dist.put(node.getId(), Double.POSITIVE_INFINITY);

		// System.out.println("\nsize=" + dist.size());
		HashSet<String> ragg = new HashSet<>();

		dist.put(nodoPartenza, 0.0);

		String nodoCorrente = nodoPartenza;
		while (nodoCorrente != null) {
			ragg.add(nodoCorrente);
			Iterator<String> adnn = g.getNode(nodoCorrente).getNeighbors().iterator();
			 //System.out.println("vicini di " + nodoCorrente+"\n");
			while (adnn.hasNext()) {
				String a = adnn.next();
				// System.out.println(a);
				if (!ragg.contains(a)) {
					double nuovaDist = dist.get(nodoCorrente) + 1;
					if (nuovaDist < dist.get(a)) {
						dist.put(a, nuovaDist);
					}
				}
			}
			nodoCorrente = null;
			double minPeso = Double.POSITIVE_INFINITY;
			for (String s : dist.keySet())
				if (!ragg.contains(s) && dist.get(s) < minPeso) {
					nodoCorrente = s;
					minPeso = dist.get(s);
				}

		}
		return dist;
	}

	public double getLongestDistance(String s) {
		HashMap<String, Double> map = dijkstra(s);
		double max = 0;
		for (Double n : map.values()) {
			if (n > max)
				max = n;
		}
		return max;
	}
	
}
