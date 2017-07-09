package reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import structure.Graph;
import structure.Node;

public class Reader {
	private Graph g;
	private File f;
	private int n;
	private String name;

	public Reader(File f, int n, String name) throws IOException {
		this.f = f;
		this.n = n;
		this.name = name;
		g = new Graph();
		build();
	}

	private void build() throws IOException {
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			int count = 0;
			String s = br.readLine();
			
			
			while (s != null&&count<n) {
				if(name.equals("amazon"))
					System.out.println(count);
				count++;
				try {
					StringTokenizer st = new StringTokenizer(s);
					if (st.countTokens() == 2) {
						// System.out.println("SI");
						String nodoDa = st.nextToken();
						String nodoA = st.nextToken();
						// String hashTag = st.nextToken();
						createNew(nodoDa, nodoA);
						s = br.readLine();
					}

				} catch (IOException ew) {
					br.close();
					break;
				}

			}
			if (s == null) {
				System.out.println("EOF");

			}

		} catch (FileNotFoundException e) {
			// XXX Auto-generated catch block
			e.printStackTrace();
		}
		g.setName(name);

	}

	private void createNew(String nodeFrom, String nodeTo) {
		Node from = new Node(nodeFrom);
		Node to = new Node(nodeTo);
		boolean flagFrom = false, flagTo = false;
		if (g.containsNode(nodeFrom)) {
			flagFrom = true;
			from = g.getNode(nodeFrom);
		}
		if (g.containsNode(nodeTo)) {
			flagTo = true;
			to = g.getNode(nodeTo);
		}
		if (!flagFrom)
			g.addNode(from);
		if (!flagTo)
			g.addNode(to);
		// from.addNeighbor(nodeTo);
		// to.addNeighbor(nodeFrom);
		g.addEdge(from, to);
		// System.out.println("Letto arco (" + nodeFrom + "," + nodeTo + ")");
	}

	public Graph getGraph() {
		return g;
	}

}
