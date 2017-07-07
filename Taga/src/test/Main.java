package test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import algorithms.LabelPropagation;
import evaluation.QualityFunctions;
import net.sf.javaml.clustering.mcl.MCL;
import net.sf.javaml.clustering.mcl.MarkovClustering;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.distance.CosineSimilarity;
import net.sf.javaml.distance.DistanceMeasure;
import net.sf.javaml.tools.data.ARFFHandler;
import net.sf.javaml.tools.data.FileHandler;
import structure.Cluster;
import structure.Graph;
import structure.Node;
import structure.Reader;

public class Main{

	public static void main(String[] args) throws IOException {
		Reader r = null;
		String name = "dolphins";
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

		// System.out.println(g.toString());
		System.out.println("label propagation ended\n");
		double fb3 = QualityFunctions.fBCubed(g);

		System.out.println("Quality functions\n\nGraph Level");
		double[] graphLevel = QualityFunctions.graphLevel(g.getClusters(), g);
		// System.out.println(Arrays.toString(graphLevel));
		System.out.println("Surprise=" + graphLevel[0] + "\nSignificance=" + graphLevel[1] + "\n");

		System.out.println("\nCommunity Level");
		for (Cluster c : g.getClusters()) {
			System.out.println("\nCluster " + c.getName() + "\n");
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
		}

		System.out.println("\nVertex Level");
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

		for (int i = 0; i < vertexLevel.length; i++) {
			vertexLevel[i] /= tot;
		}
		System.out.println("avg val=" + Arrays.toString(vertexLevel));

		MarkovClustering mc = new MarkovClustering();

		Dataset data = FileHandler.loadDataset(new File("src/datasets/iris.csv"), 4, ",");
		
		
		Dataset data1=ARFFHandler.loadARFF(new File("src/datasets/dolphins"),1);
		
		
		DistanceMeasure dm=new CosineSimilarity();
		
		MCL mcl=new MCL(dm);
		
		Dataset[] ris=mcl.cluster(data);
		
		System.out.println(Arrays.toString(ris));
		System.out.println(ris.length);
		
		System.out.println(data);
		System.out.println("daaaaaa");
		
		
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