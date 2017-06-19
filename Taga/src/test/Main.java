package test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import algorithms.Dijkstra;
import algorithms.LabelPropagation;
import evaluation.QualityFunctions;
import structure.Cluster;
import structure.Graph;
import structure.Node;
import structure.Reader;

public class Main {

	public static void main(String[] args) {
		Reader r = null;
		String name = "jazz";
		try {
			r = new Reader(new File("src/datasets/" + name), 1000, name);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Graph g = r.getGraph();
		g.computeVolume();
		System.out.println("--------- GRAFO INIZIALE -------");
		System.out.println(g);
		// System.out.println(g.getNodes());
		// System.out.println();
		System.out.println("label propagation started");
		LabelPropagation lb = new LabelPropagation(g);
		lb.compute();
		for (Cluster c1 : g.getClusters()) {
			c1.computeVolume();
			System.out.println("Size of " + c1.getName() + " = " + c1.getN());

		}

		System.out.println(g.toString());
		System.out.println("label propagation ended\n");
		double fb3 = QualityFunctions.fBCubed(g);
		double[] vertex = QualityFunctions.graphLevel(g.getClusters(), g);

		for (Cluster c : g.getClusters()) {
			System.out.println("\nCluster" + c.getName() + "\n");
			double cut = QualityFunctions.cutRatio(c, g);
			double cond = QualityFunctions.conductance(c, g);
			double comp = QualityFunctions.compactness(c, g);
			double mod = QualityFunctions.modularity(c, g);
			System.out.println("cut=" + cut);
			System.out.println("cond=" + cond);
			System.out.println("comp=" + comp);
			System.out.println("mod=" + mod + "\n");

		}
		System.out.println(Arrays.toString(vertex));

		Graph g1 = new Graph();
		Node a = new Node("a");
		Node b = new Node("b");
		Node c = new Node("c");
		Node d = new Node("d");

		g1.addNode(a);
		g1.addNode(b);
		g1.addNode(c);
		g1.addNode(d);

		g1.addEdge(a, c);
		g1.addEdge(c, b);
		g1.addEdge(b, d);
		
		
		g.computeDiameter();
		g1.computeDiameter();
//		System.out.println("diam g=" + g.getDiameter() );
//		System.out.println("diam g1=" + g1.getDiameter() );

		

//		Dijkstra dijk = new Dijkstra(g);
//		HashMap<String, Double> m = dijk.dijkstra("44");
//		System.out.println("\nlista\n" + m);
//
//		Dijkstra dijk1 = new Dijkstra(g1);
//		HashMap<String, Double> m1 = dijk1.dijkstra("a");
//		System.out.println("\nSize from " + a + "\n" + m1);

		// System.out.println("fb3=" + fb3);

	}

	// public static void main(String[] args) {
	// int kc=60;
	// double coeffK=CombinatoricsUtils.binomialCoefficientDouble(kc,2);
	// int a=(908+753+528);
	// double x=a/coeffK;
	// double coeffN=CombinatoricsUtils.binomialCoefficientDouble(198,2);
	// double y=2742/coeffN;
	// double sign=coeffK*QualityFunctions.deviation(x, y);
	//
	// System.out.println("coeffK=" + coeffK);
	// System.out.println("coeffN=" + coeffN);
	// System.out.println("x=" + x);
	// System.out.println("y=" + y);
	//
	//
	//
	// System.out.println(sign);
	// }

}