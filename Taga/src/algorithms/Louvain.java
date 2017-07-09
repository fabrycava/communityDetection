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

		// System.out.println(c.getM());
		// System.out.println(c.getN());
		// System.out.println(c.getName());
		c.computeVolume();
		double in = c.getVolume();
		double tot = incidents(c);

		double ki = i.getDegree();
		double kiin = c.getInternalDegree(i);
		double m = g.getVolume();

		// System.out.println("Cluster " + c.getName());
		// System.out.println(c.getEdges());
		// if (in > 0)
		// System.out.println("in =" + in);
		// System.out.println("tot=" + tot);
		// System.out.println("ki= " + ki);
		// System.out.println("kiin" + kiin);

		double first = ((in + kiin) / (2 * m)) - Math.pow((tot + ki) / (2 * m), 2);
		double second = in / (2 * m) - Math.pow((tot) / (2 * m), 2) - Math.pow((ki) / (2 * m), 2);

		// System.out.println("first ="+ first);
		// System.out.println("second ="+ second);
		// System.out.println(first - second + "\n");

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
			c.addNode(n);
			g.addCluster(c);

		}

		// Assign nodes to clusters
		boolean convergence = false;
		int cont = 0;
		while (!convergence) {
			System.out.println("cont=" + cont);

			cont++;
			convergence = true;
			for (Node n : g.getNodes().values()) {
				String newCluster = computeMaxGain(n);
				if (newCluster != null && !newCluster.equals(n.getLabel())) {

					g.removeNodeFromCluster(n, n.getLabel());
					
					if (g.getCluster(n.getLabel()).isEmpty()) {
						g.removeCluster(n.getLabel());
						System.out.println(n.getLabel() + " rimosso\n");
					}
					System.out.println(n.getLabel());
					n.setLabel(newCluster);
					System.out.println(n.getLabel()+"\n");

					// System.out.println("prima="+g.getCluster(newCluster));
					g.addNodeInCluster(n, newCluster);
					// System.out.println("dopo="+g.getCluster(newCluster));

					// System.out.println();
					convergence = false;
					
					
				}
			}

			// remove empty clusters
//			ListIterator<Cluster> itC = g.getClusters().listIterator();
//
//			while (itC.hasNext()) {
//				Cluster c = itC.next();
//				if (c.isEmpty()) {
//					// System.out.println(c.getN());
//					itC.remove();
//
//				}
//			}

			for (Node n : g.getNodes().values()) {
				boolean flag = false;
				for (Cluster c : g.getClusters())
					if (c.getName().equals(n.getLabel()))
						flag = true;
				if (!flag)
					System.out.println("cluster " + n.getLabel() + " doesn't exist");

			}

			// for(Node n: g.getNodes().values())
			// if(g.getCluster(n.getLabel())==null)
			// System.out.println("sucaaaaaa");

		}

	}

	private String computeMaxGain(Node n) {
		HashMap<String, Double> clustersValues = new HashMap<>();
		double max = 0;
		String newLabel = null;
		for (String s : n.getNeighbors()) {
			Node n1 = g.getNode(s);
			System.out.println("Cluster=" + n1.getLabel());
			System.out.println("Node" + n1);
			Cluster c = g.getCluster(n1.getLabel());
			// System.out.println(c);
			double first = deltaQ(g.getCluster(n1.getLabel()), n);
			double second = deltaQ(g.getCluster(n.getLabel()), n);
			double dQ = first - second;
			// System.out.println("first ="+ first);
			// System.out.println("second ="+ second);

			// System.out.println(dQ);
			// System.out.println("Node " + n+"\n");
			if (dQ > max) {
				// System.out.println(dQ);
				max = dQ;
				newLabel = s;
			}
		}

		// System.out.println(newLabel);
		return newLabel;
	}

}
