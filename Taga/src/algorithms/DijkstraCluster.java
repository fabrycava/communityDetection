package algorithms;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import structure.Graph;
import structure.Node;

public class DijkstraCluster extends Dijkstra {

	public DijkstraCluster(Graph g) {
		super(g);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HashMap<String, Double> dijkstra(String nodoPartenza) {
		// int n = g.getN();
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
			//System.out.println("vicini di " + nodoCorrente + "\n");
			while (adnn.hasNext()) {
				String a = adnn.next();

				//System.out.println(a);
				if (!ragg.contains(a) && g.containsNode(a)) {
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

}
