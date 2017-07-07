package structure;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import algorithms.DijkstraCluster;

public class Cluster extends Graph {

	public Cluster(String name) {
		this.name = name;
	}
	public Cluster(){
		
	}

	@Override
	public void addNode(Node node) {
		if (super.nodes.containsKey(node.getId()))
			throw new RuntimeException("Node " + node.getId() + " is already present in the Cluster");
		this.nodes.put(node.getId(), node);
		LinkedList<String> neighbors = node.getNeighbors();

		Iterator<String> it = neighbors.iterator();

		LinkedList<String> inside = new LinkedList<>();
		while (it.hasNext()) {
			String n = it.next();
			if (containsNode(n) && !containsEdge(getNode(n), node)) {
				inside.add(n);
			}
		}
		for (String s : inside) {
			addEdge(node, getNode(s));
		}

	}

	@Override
	public void addEdge(Node n1, Node n2) {
		if (!nodes.containsKey(n1.getId()) || !(nodes.containsKey(n2.getId())))
			throw new RuntimeException("Some of the node is not present in the Cluster!" + getName());

		if (n1.equals(n2))
			throw new RuntimeException("Self-loop not allowed for edge " + new Edge(n1, n2).toString());

		Edge e = new Edge(n1, n2);
		Edge e1 = new Edge(n2, n1);

		if (containsEdge(e) || containsEdge(e1))
			throw new RuntimeException("Edge " + e.toString() + " is already present in the Cluster!" + getName());

		edges.add(e);

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Cluster:" + getName() + "\n#Nodes:" + nodes.size() + "\n#Edges:" + edges.size() + "\nVol="
				+ getVolume());
		sb.append("\nNodes: \n");

		for (Node n : nodes.values()) {
			sb.append(n.getId() + " ");
		}

		// sb.append("\nEdges: \n");
		// for (Edge e : edges) {
		// sb.append(e.toString() + "\t");
		// }
		return sb.toString();

	}

	public int externalDegree() {
		int tot = 0;
		for (Node n : nodes.values()) {
			LinkedList<String> neighbors = n.getNeighbors();
			for (String s : neighbors) {
				if (!containsNode(s))
					tot++;
			}
		}

		return tot;
	}

	public int externalDegree(Cluster c) {
		int tot = 0;
		for (Edge e : edges) {
			if (containsNode(e.getDestination().getId()) && e.getSource().isInCluster(c)) {
				tot++;
			} else if (containsNode(e.getSource().getId()) && e.getDestination().isInCluster(c)) {
				tot++;
			}
		}
		return tot;
	}

	public LinkedList<String> getInternalNeighbors(Node n) {
		LinkedList<String> l = n.getNeighbors();
		// System.out.println(l.size());
		ListIterator<String> it = l.listIterator();
		while (it.hasNext()) {
			if (!containsNode(it.next()))
				it.remove();
		}
		// System.out.println(l.size());
		return l;
	}

	@Override
	public void computeDiameter() {
		DijkstraCluster dijkstra = new DijkstraCluster(this);
		diameter = 0;
		for (Node n : nodes.values()) {
			double v = dijkstra.getLongestDistance(n.getId());
			// System.out.println(v);
			if (v > diameter)
				diameter = v;
		}

	}

	// @Override
	// public String toString() {
	// return nodes.toString() + " frequent " + frequentItem.toString();
	// }
	//
	// @Override
	// public boolean equals(Object o) {
	// if (o == this)
	// return true;
	// if (!(o instanceof Cluster))
	// return false;
	// Cluster t = (Cluster) o;
	//
	// return frequentItem.equals(t.getFrequentItem()) && t.nodes.equals(nodes);
	// }
}
