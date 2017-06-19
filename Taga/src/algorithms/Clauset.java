package algorithms;

import structure.Cluster;
import structure.Graph;

public class Clauset {

	public static double e(Cluster i, Cluster j, Graph g) {
		return i.externalDegree(j) / (2 * g.getM());
	}

	public static double a(Cluster i, Graph g) {
		return i.getVolume() / (2 * g.getM());
	}

	public static double Q(Graph g) {
		double sum = 0;
		for (Cluster c : g.getClusters()) {
			sum += (e(c, c, g) - Math.pow(a(c, g), 2));
		}
		return sum;
	}

}
