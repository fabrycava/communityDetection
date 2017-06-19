package structure;

import java.util.LinkedList;

public class Community {

	private LinkedList<Node> nodes = new LinkedList<Node>();
	private LinkedList<LinkedList<String>> frequentItem;
	private String name = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LinkedList<Node> getTransazioni() {
		return nodes;
	}

	public void addNode(Node node) {
		this.nodes.add(node);
	}

	public void removeNode(Node node) {
		this.nodes.remove(node);
	}

	public LinkedList<LinkedList<String>> getFrequentItem() {
		return frequentItem;
	}

	public void setFrequentItem(LinkedList<LinkedList<String>> item) {
		this.frequentItem = item;
	}

	@Override
	public String toString() {
		return nodes.toString() + " frequent " + frequentItem.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Community))
			return false;
		Community t = (Community) o;

		return frequentItem.equals(t.getFrequentItem()) && t.nodes.equals(nodes);
	}
	
}
