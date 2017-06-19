package structure;

import java.util.LinkedList;
import java.util.Locale.Category;

public class Node {

	private LinkedList<String> neighbors;
	private String id;

	private LinkedList<String> categories, clusters;

	private String label;

	public Node(String id) {
		this.id = id;
		neighbors = new LinkedList<>();
		categories = new LinkedList<>();
		clusters = new LinkedList<>();
		label = null;
	}

	public Node(String id, LinkedList<String> neighbors, LinkedList<String> categories, LinkedList<String> clusters) {
		this.id = id;
		this.neighbors = neighbors;
		this.categories = categories;
		this.clusters = clusters;
	}

	public String getId() {
		return id;
	}

	public LinkedList<String> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(LinkedList<String> neighbors) {
		this.neighbors = neighbors;
	}

	public void addNeighbor(String neighbor) {
		neighbors.add(neighbor);
	}

	public int getDegree() {
		return neighbors.size();
	}

	public LinkedList<String> getClusters() {
		return clusters;
	}

	public void addCluster(String s) {
		if (!clusters.contains(s))
			clusters.add(s);
	}

	public void removeClusters(String e) {
		clusters.remove(e);
	}

	public LinkedList<String> getCategories() {
		return categories;
	}

	public void addCategory(String s) {
		categories.add(s);
	}

	public void removeCategory(String e) {
		categories.remove(e);
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public int getNumberSharedCluster(Node n) {
		int count = 0;
		LinkedList<String> clusters = n.getClusters();
		for (String s : this.clusters) {
			if (clusters.contains(s))
				count++;
		}
		return count;
	}

	public int getNumberSharedCategories(Node n) {
		int count = 0;
		LinkedList<String> categories = n.getCategories();
		for (String s : this.categories) {
			if (categories.contains(s))
				count++;
		}
		return count;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(id);
		return sb.toString();

		// for (String string : neighbors) {
		// sb.append(string + ", ");
		// }
		// sb.setLength(sb.length() - 2);

	}

	public boolean equalsProfilo(Node b, double treshold) {
		int count = 0;
		for (String string : b.getNeighbors()) {
			if (neighbors.contains(string)) {
				count++;
			}
		}

		if (count >= treshold) {
			return true;
		}
		return false;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Node))
			return false;
		Node n = (Node) o;
		return n.getId().equals(id);
	}

	public boolean isInCluster(Cluster c) {
		return clusters.contains(c);
	}

}
