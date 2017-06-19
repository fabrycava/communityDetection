package structure;

public class Edge {

	private Node source;
	private Node destination;

	public Edge(Node source, Node destination) {
		this.source = source;
		this.destination = destination;
	}
	
	public Edge(Edge e){
		source=e.getSource();
		destination=e.getDestination();
	}

	public Node getSource() {
		return source;
	}

	public void setSource(Node source) {
		this.source = source;
	}

	public Node getDestination() {
		return destination;
	}

	public void setDestination(Node destination) {
		this.destination = destination;
	}

	@Override
	public String toString() {
		return "("+source.getId() + "," + destination.getId() + ")";
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Edge))
			return false;
		Edge a = (Edge) o;
		return (a.getSource().equals(source) && a.getDestination().equals(destination))
				|| (a.getSource().equals(destination) && a.getDestination().equals(source));
	}

}
