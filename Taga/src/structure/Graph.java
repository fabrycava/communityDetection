package structure;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import algorithms.Dijkstra;

public class Graph {

	protected HashMap<String, Node> nodes = new HashMap<>();
	protected HashSet<Edge> edges = new HashSet<>();
	private LinkedList<Cluster> clusters = new LinkedList<>();

	private int vol;

	protected LinkedList<String> nodeList = new LinkedList<>();

	protected double diameter = 0;

	protected String name = null;

	public String getName() {
		return name;
	}

	public void createCluster(String name) {
		Cluster c = new Cluster(name);
		clusters.add(c);
	}

	public void addCluster(Cluster c) {
		clusters.add(c);
	}

	public void removeCluster(String name) {
		clusters.remove(name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashMap<String, Node> getNodes() {
		return nodes;
	}

	public HashSet<Edge> getEdges() {
		return edges;
	}

	public int getM() {
		return edges.size();
	}

	public int getN() {
		return nodes.size();
	}

	public int getInternalDegree(Node n) {
		int count = 0;
		for (String n1 : n.getNeighbors()) {
			if (containsNode(n1) && !(n.equals(n1)))
				count++;
		}
		return count;
	}

	public void computeVolume() {
		vol = 0;
		for (Node n : nodes.values()) {
			vol += n.getDegree();
		}
	}

	public boolean containsEdge(Node i, Node j) {
		Edge e = new Edge(i, j);
		for (Edge e1 : edges) {
			if (e.equals(e1))
				return true;
		}
		return false;
	}

	public void addNode(Node node) {
		if (nodes.containsKey(node.getId()))
			throw new RuntimeException("Node " + node.getId() + " is already present in the graph");
		this.nodes.put(node.getId(), node);
		this.nodeList.add(node.getId());

		// System.out.println("node " + node.getId() + " added in the graph");

	}

	public LinkedList<Cluster> getClusters() {
		return clusters;
	}

	public Node getNode(String s) {
		if (!containsNode(s))
			throw new RuntimeException("Node " + s + " is not present");
		return nodes.get(s);

	}

	public void addEdge(Node n1, Node n2) {
		if (!nodes.containsKey(n1.getId()) || !(nodes.containsKey(n2.getId())))
			throw new RuntimeException("Some of the node is not present in the graph!");

		if (n1.equals(n2))
			throw new RuntimeException("Self-loop not allowed for edge " + new Edge(n1, n2).toString());

		Edge e = new Edge(n1, n2);
		Edge e1 = new Edge(n2, n1);

		if (containsEdge(e) || containsEdge(e1))
			throw new RuntimeException("Edge " + e.toString() + " is already present in the graph");

		edges.add(e);

		n1.addNeighbor(n2.getId());
		n2.addNeighbor(n1.getId());

	}

	public void removeNode(Node node) {

		this.nodes.remove(node);
		this.nodeList.remove(node.getId());

		for (Edge e : edges) {
			if (node.equals(e.getDestination()) || node.equals(e.getSource())) {
				removeEdge(e);
			}
		}
	}

	protected void removeEdge(Edge e) throws RuntimeException {
		if (!containsEdge(e))
			throw new RuntimeException("Edge (" + e.getSource() + "," + e.getDestination() + ") doesn't exist");
		edges.remove(e);
	}

	public boolean containsNode(String s) {
		return nodes.containsKey(s);
	}

	protected boolean containsEdge(Edge e) {
		for (Edge e1 : edges) {
			if (e.equals(e1))
				return true;
		}
		return false;
	}

	public double medianDegree() {
		int tot = 0;
		for (Node n : nodes.values()) {
			tot += n.getDegree();
		}
		return (double) tot / nodes.size();

	}

	public void addNodeInCluster(Node n, String c) {
		for (Cluster c1 : clusters) {
			if (c.equals(c1.getName()))
				c1.addNode(n);
		}
	}

	public void addNodeInCluster(Node n) {
		boolean flag = false;
		for (Cluster c : clusters) {
			if (c.getName().equals(n.getLabel())) {
				c.addNode(n);
				// n.addCluster(n.getLabel());
				flag = true;
				// System.out.println("Nodo " + n.getId() + " aggiunto al
				// cluster " + c.getName());
				break;
			}
		}
		if (!flag) {
			Cluster c1 = new Cluster();
			c1.setName(n.getLabel());
			// System.out.println("Cluster " + c1.getName() + " creato");
			c1.addNode(n);
			// n.addCluster(n.getLabel());
			clusters.add(c1);

		}

	}

	public Cluster getCluster(String s) {
		if (!clusters.contains(s)) {
			throw new RuntimeException("Cluster " + s + " is not contained in the graph");
		}
		for (Cluster c : clusters)
			if (c.getName().equalsIgnoreCase(s))
				return c;
		return null;
	}

	public Iterator<Node> iterator() {
		return nodes.values().iterator();
	}

	public Iterator<Node> shuffledIterator() {
		return new GraphShuffledIterator(nodeList.listIterator(), nodes);
	}

	public void shuffleNodes() {
		Collections.shuffle(nodeList);
	}

	public void setClusters(LinkedList<Cluster> c) {
		this.clusters = c;
	}

	public String toString() {

		StringBuilder sb = new StringBuilder();

		sb.append("Graph:" + getName() + "\n#Nodes:" + nodes.size() + "\n#Edges:" + edges.size() + "\nVol="
				+ getVolume());

		// for (Node n : nodes.values()) {
		// sb.append(n.toString() + " ");
		// }
		//
		// sb.append(nodes.values().toString());
		//
		// sb.append("\nEdges: \n");
		// for (Edge e : edges) {
		// sb.append(e.toString() + "\n");
		// }

		sb.append("\n\nClusters(" + clusters.size() + "):" + "\n\n");
		for (Cluster c : clusters) {
			sb.append(c.toString() + "\n\n");
		}
		return sb.toString();

	}

	public int getVolume() {
		return vol;
	}

	public void removeNodeFromCluster(Node n, String c) {
		for (Cluster c1 : clusters)
			if (c1.getName().equals(c))
				c1.removeNode(n);
	}

	protected class GraphShuffledIterator implements Iterator<Node> {

		private ListIterator<String> it;
		private HashMap<String, Node> map;

		public GraphShuffledIterator(ListIterator<String> it, HashMap<String, Node> map) {
			this.it = it;
			this.map = map;
		}

		@Override
		public boolean hasNext() {
			return it.hasNext();
		}

		@Override
		public Node next() {
			return map.get(it.next());
		}

	}

	public void computeDiameter() {
		Dijkstra dijkstra = new Dijkstra(this);
		diameter = 0;
		for (Node n : nodes.values()) {
			double v = dijkstra.getLongestDistance(n.getId());
			// System.out.println(v);
			if (v > diameter)
				diameter = v;
		}

	}

	public double getDiameter() {
		return diameter;
	}

}
