package test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import algorithms.LabelPropagation;
import evaluation.QualityFunctions;
import reader.Reader;
import structure.Cluster;
import structure.Graph;
import structure.Node;

public class Main {

	public static void main(String[] args) throws IOException {
		Reader r = null;
		String name = "amazon";
		try {
			r = new Reader(new File("src/datasets/" + name), 1000000, name);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Graph g = r.getGraph();
		g.computeVolume();

		File f = new File("src/datasets/" + name + ".ris");
		if (!f.exists())
			f.createNewFile();
		PrintWriter pw = new PrintWriter(f);

		System.out.println("--------- GRAFO INIZIALE -------");
		System.out.println(g);

		pw.println("--------- GRAFO INIZIALE -------");
		pw.println(g);
		// System.out.println(g.getNodes());
		// System.out.println();
		System.out.println("label propagation started");
		pw.println("label propagation started");
		LabelPropagation lb = new LabelPropagation(g);
		lb.compute();
		for (Cluster c1 : g.getClusters()) {
			c1.computeVolume();
			System.out.println("Size of " + c1.getName() + " = " + c1.getN());
			pw.println("Size of " + c1.getName() + " = " + c1.getN());

		}

		// System.out.println(g.toString());
		System.out.println("label propagation ended\n");
		pw.println("label propagation ended\n");

		double fb3 = QualityFunctions.fBCubed(g);

		System.out.println("Quality functions\n\nGraph Level");
		pw.println("Quality functions\n\nGraph Level");

		double[] graphLevel = QualityFunctions.graphLevel(g.getClusters(), g);
		// System.out.println(Arrays.toString(graphLevel));
		System.out.println("Surprise=" + graphLevel[0] + "\nSignificance=" + graphLevel[1] + "\n");
		pw.println("Surprise=" + graphLevel[0] + "\nSignificance=" + graphLevel[1] + "\n");

		System.out.println("\nCommunity Level");
		pw.println("\nCommunity Level");

		for (Cluster c : g.getClusters()) {
			System.out.println("\nCluster " + c.getName() + "\n");
			pw.println("\nCluster " + c.getName() + "\n");

			int size = c.getN();
			double cut = QualityFunctions.cutRatio(c, g);
			double cond = QualityFunctions.conductance(c, g);
			double comp = QualityFunctions.compactness(c, g);
			double mod = QualityFunctions.modularity(c, g);
			System.out.println("size=" + size);
			System.out.println("cut=" + cut);
			System.out.println("cond=" + cond);
			System.out.println("comp=" + comp);
			System.out.println("mod=" + mod + "\n");

			pw.println("size=" + size);
			pw.println("cut=" + cut);
			pw.println("cond=" + cond);
			pw.println("comp=" + comp);
			pw.println("mod=" + mod + "\n");
		}

		System.out.println("\nVertex Level");
		pw.println("\nVertex Level");

		int tot = 0;
		double clus = 0, perm = 0, flak = 0, fomd = 0;
		double[] vertexLevel = new double[4];
		for (Cluster c : g.getClusters()) {
			for (Node n : c.getNodes().values()) {
				tot++;
				double[] values = QualityFunctions.vertexLevel(n, c, g, g.getClusters());
				double p = QualityFunctions.permanence(n, c, g.getClusters());
				double cl = QualityFunctions.localClusteringCoefficient(n, c);
				double odf = QualityFunctions.flakeODF(n, c, g.getClusters());
				double omd = QualityFunctions.fomd(n, c, g);

				for (int i = 0; i < vertexLevel.length; i++)
					vertexLevel[i] += values[i];
			}
		}

		System.out.println("val tot=" + Arrays.toString(vertexLevel));
		pw.println("val tot=" + Arrays.toString(vertexLevel));

		for (int i = 0; i < vertexLevel.length; i++) {
			vertexLevel[i] /= tot;
		}
		System.out.println("avg val=" + Arrays.toString(vertexLevel));
		pw.println("avg val=" + Arrays.toString(vertexLevel));

		pw.close();
		g.cleanClusters();

		// Graph g1=new Graph();
		// g1.addNode(new Node("1"));
		// g1.addNode(new Node("2"));
		// System.out.println("prima=\n"+g1);
		// g1.removeNode(new Node("1"));
		// System.out.println("dopo=\n"+g1);
		// g1.addNode(new Node("1"));

		// Graph g1=new Graph();
		// Node a = new Node("a");
		// a.setLabel("a");
		// Node b = new Node("b");
		// b.setLabel("b");
		// Node f = new Node("c");
		// f.setLabel("c");
		// Node d = new Node("d");
		// d.setLabel("d");
		//
		// g1.addNode(a);
		// g1.addNode(b);
		// g1.addNode(f);
		// g1.addNode(d);
		//
		// Iterator<Node> it = g1.iterator();
		//
		// // Step1
		// while (it.hasNext()) {
		// Node n = it.next();
		// n.setLabel(n.getId());
		// Cluster c = new Cluster(n.getId());
		// c.addNode(n);
		// g1.addCluster(c);
		//
		// }
		// System.out.println(g1);
		//
		//
		// for(Node n:g1.getNodes().values()){
		//
		// g1.removeNodeFromCluster(n, n.getLabel());
		// Cluster c=g1.getCluster(n.getLabel());
		// if (c.isEmpty())
		// g1.removeCluster(n.getLabel());
		// }
		// System.out.println("aaaaaaaa"+g1);

		// System.out.println("\nLouvain started");
		// Louvain2 louvain = new Louvain2(g);
		// //louvain.compute();
		// for (Cluster c1 : g.getClusters()) {
		// c1.computeVolume();
		// System.out.println("Size of " + c1.getName() + " = " + c1.getN());
		//
		// }
		//
		// // System.out.println(g.toString());
		// System.out.println("Louvain\n");
		// double fb3Louvain = QualityFunctions.fBCubed(g);
		//
		// System.out.println("Quality functions\n\nGraph Level");
		// double[] graphLevelLouvain =
		// QualityFunctions.graphLevel(g.getClusters(), g);
		// // System.out.println(Arrays.toString(graphLevel));
		// System.out.println("Surprise=" + graphLevelLouvain[0] +
		// "\nSignificance=" + graphLevelLouvain[1] + "\n");
		//
		// System.out.println("\nCommunity Level");
		// for (Cluster c : g.getClusters()) {
		// System.out.println("\nCluster " + c.getName() + "\n");
		// int size = c.getN();
		// double cut = QualityFunctions.cutRatio(c, g);
		// double cond = QualityFunctions.conductance(c, g);
		// double comp = QualityFunctions.compactness(c, g);
		// double mod = QualityFunctions.modularity(c, g);
		// System.out.println("size=" + size);
		// System.out.println("cut=" + cut);
		// System.out.println("cond=" + cond);
		// System.out.println("comp=" + comp);
		// System.out.println("mod=" + mod + "\n");
		// }
		//
		// System.out.println("\nVertex Level");
		// int totLouvain = 0;
		// double clusLouvain = 0, permLouvain = 0, flakLouvain = 0, fomdLouvain
		// = 0;
		// double[] vertexLevelLouvain = new double[4];
		// for (Cluster c : g.getClusters()) {
		// for (Node n : c.getNodes().values()) {
		// totLouvain++;
		// double[] valuesLouvain = QualityFunctions.vertexLevel(n, c, g,
		// g.getClusters());
		// double p = QualityFunctions.permanence(n, c, g.getClusters());
		// double cl = QualityFunctions.localClusteringCoefficient(n, c);
		// double odf = QualityFunctions.flakeODF(n, c, g.getClusters());
		// double omd = QualityFunctions.fomd(n, c, g);
		//
		// for (int i = 0; i < vertexLevelLouvain.length; i++)
		// vertexLevelLouvain[i] += valuesLouvain[i];
		// }
		// }
		//
		// System.out.println("val tot=" + Arrays.toString(vertexLevelLouvain));
		//
		// for (int i = 0; i < vertexLevelLouvain.length; i++) {
		// vertexLevelLouvain[i] /= tot;
		// }
		// System.out.println("avg val=" + Arrays.toString(vertexLevelLouvain));

		// MarkovClustering mc = new MarkovClustering();
		// Dataset data = FileHandler.loadDataset(new
		// File("src/datasets/iris.csv"), 4, ",");
		// Dataset data1=ARFFHandler.loadARFF(new
		// File("src/datasets/dolphins"),1);
		// DistanceMeasure dm=new CosineSimilarity();
		// MCL mcl=new MCL(dm);
		// Dataset[] ris=mcl.cluster(data);
		// System.out.println(Arrays.toString(ris));
		// System.out.println(ris.length);
		// System.out.println(data);
		// System.out.println("daaaaaa");
		//
		// System.out.println(vertexLevel);

		// Graph g1 = new Graph();
		// Node a = new Node("a");
		// Node b = new Node("b");
		// Node c = new Node("c");
		// Node d = new Node("d");
		// g1.addNode(a);
		// g1.addNode(b);
		// g1.addNode(c);
		// g1.addNode(d);
		// g1.addEdge(a, c);
		// g1.addEdge(c, b);
		// g1.addEdge(b, d);
		//
		// g.computeDiameter();
		// g1.computeDiameter();
		// System.out.println("diam g=" + g.getDiameter() );
		// System.out.println("diam g1=" + g1.getDiameter() );
		// Dijkstra dijk = new Dijkstra(g);
		// HashMap<String, Double> m = dijk.dijkstra("44");
		// System.out.println("\nlista\n" + m);//
		// Dijkstra dijk1 = new Dijkstra(g1);
		// HashMap<String, Double> m1 = dijk1.dijkstra("a");
		// System.out.println("\nSize from " + a + "\n" + m1);
		// System.out.println("fb3=" + fb3);

	}

}