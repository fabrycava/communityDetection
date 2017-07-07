package algorithms;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import structure.Cluster;
import structure.Graph;
import structure.Node;

public class Louvain extends Algorithm {

	public Louvain(Graph g) {
		super(g);
	}

	@Override
	public void createClusters() {
		// TODO Auto-generated method stub

	}

	private double Q() {
		double a = 0, m = g.getVolume(), sum = 0, temp = 0;
		m /= 2;

		for (Node n : g.getNodes().values()) {
			for (Node n1 : g.getNodes().values()) {

				temp = 1 - (n.getDegree() * n1.getDegree()) / (2 * m);
				int d = 0;
				if (n.getNumberSharedCluster(n1) >= 1)
					d = 1;
				sum += temp * d;
			}
		}
		return (double) sum / (2 * m);
	}

	private double incidents(Cluster c) {
		double tot = 0;
		for (Node n : g.getNodes().values()) {
			LinkedList<String> neighbors = n.getNeighbors();
			Iterator<String> it = neighbors.iterator();
			while (it.hasNext()) {
				String s = it.next();
				Node n1 = g.getNode(s);
				if (c.containsNode(s) && g.containsEdge(n, n1))
					tot++;
			}
		}
		return tot;

	}

	private double deltaQ(Cluster c, Node i) {
		double in = c.getVolume();
		double tot = incidents(c);

		double ki = i.getDegree();
		double kiin = c.getInternalDegree(i);
		double m = g.getVolume();

		double first = ((in + kiin) / (2 * m)) - Math.pow((tot + ki) / (2 * m), 2);
		double second = in / (2 * m) - Math.pow((tot) / (2 * m), 2) - Math.pow((ki) / (2 * m), 2);
		return first - second;
	}

	@Override
	public void compute() {

		Iterator<Node> it = g.iterator();

		// Step1
		while (it.hasNext()) {
			Node n = it.next();
			n.setLabel(n.getId());
			Cluster c = new Cluster(n.getId());
			g.addCluster(c);

		}

		// Assign nodes to clusters
		boolean convergence = false;
		while (!convergence) {
			convergence = true;
			for (Node n : g.getNodes().values()) {
				String newCluster = computeMaxGain(n);
				if (newCluster != null) {// change cluster & label
					g.removeNodeFromCluster(n, n.getLabel());
					n.setLabel(newCluster);
					g.addNodeInCluster(n, newCluster);
					convergence = false;
				}
			}

			// remove empty clusters
			ListIterator<Cluster> itC = g.getClusters().listIterator();
			while (itC.hasNext())
				if (itC.next().getM() == 0)
					itC.remove();

		}
		createClusters();

	}

	private String computeMaxGain(Node n) {
		HashMap<String, Double> clustersValues = new HashMap<>();
		double max = 0;
		String newLabel = null;
		for (String s : n.getNeighbors()) {
			Node n1 = g.getNode(s);
			double dQ = deltaQ(g.getCluster(s), n) - deltaQ(g.getCluster(n.getId()), n);
			if (dQ > max) {
				System.out.println(dQ);
				max = dQ;
				newLabel = s;
			}
		}

		return newLabel;
	}

}
