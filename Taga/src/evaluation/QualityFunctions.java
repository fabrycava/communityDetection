package evaluation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import structure.Cluster;
import structure.Graph;
import structure.Node;

import org.apache.commons.math3.util.CombinatoricsUtils;
import org.omg.CORBA.DATA_CONVERSION;

public class QualityFunctions {
	public static double localClusteringCoefficient(Node v, Cluster c) {
		int internalDegree = c.getInternalDegree(v);
		double fclus = (double) (2 * internalDegree) / (internalDegree * (internalDegree - 1));
		return fclus;
	}

	public static double permanence(Node v, Cluster c, LinkedList<Cluster> clusters) {
		int m = c.getInternalDegree(v);
		int maxValue = 0;
		int temp = 0;
		Cluster maxCluster;
		for (Cluster c1 : clusters) {
			if (!c1.equals(c)) {
				temp = c1.getInternalDegree(v);
				if (temp > maxValue) {
					maxValue = temp;
					maxCluster = c1;
				}
			}
		}
		double fclus = (2 * m) / (m * (m - 1));
		return (double) (fclus - 1 + m / (maxValue * v.getDegree()));

	}

	public static int flakeODF(Node n, Cluster c, LinkedList<Cluster> clusters) {
		int m = c.getInternalDegree(n);
		int sum = 0;
		for (Cluster c1 : clusters) {
			if (!c1.equals(c)) {
				sum += c.getInternalDegree(n);
			}
		}
		return m > sum ? 1 : 0;
	}

	public static int fomd(Node n, Cluster c, Graph g) {
		int m = c.getInternalDegree(n);
		double median = g.medianDegree();
		return m > median ? 1 : 0;

	}

	public static double[] vertexLevel(Node n, Cluster c, Graph g, LinkedList<Cluster> clusters) {
		double perm = 0, flak = 0, fomd = 0, clus;
		int mnc = c.getInternalDegree(n);
		int sum = 0;
		double median = g.medianDegree();
		int maxValue = 0;
		int temp = 0;
		Cluster maxCluster;
		for (Cluster c1 : clusters) {
			if (!c1.equals(c)) {
				temp = c1.getInternalDegree(n);
				sum += temp;
				if (temp > maxValue) {
					maxValue = temp;
					maxCluster = c1;
				}
			}
		}
		clus = localClusteringCoefficient(n, c);
		fomd = mnc > median ? 1 : 0;
		flak = mnc > sum ? 1 : 0;
		perm = (clus - 1 + mnc / (maxValue * n.getDegree()));

		double[] values = { clus, perm, flak, fomd };
		return values;
	}

	public static double modularity(Cluster c, Graph g) {
		double mc = c.getM();
		double m = g.getM();
		double v = c.getVolume();
		return ((mc / m) - Math.pow(v / (2 * m), 2));
	}

	public static double cutRatio(Cluster c, Graph g) {
		double m = c.externalDegree();
		System.out.println("external "+m);
		double kc = c.getN();
		double fcut = (1 - (m / (kc * (g.getN() - kc))));
		return fcut * (kc / g.getN());
	}

	public static double conductance(Cluster c, Graph g) {
		double m = c.externalDegree();
		double vol = c.getVolume();
		double kc = c.getN();
		double fcond = (1 - (m / vol));
		return fcond * (kc / g.getN());
	}

	public static double compactness(Cluster c, Graph g) {
		double m = c.getM();
		c.computeDiameter();
		double diam = c.getDiameter();
		System.out.println("Diam di "+ c.getName()+"="+diam);
		return (double) m / diam;
	}

	public static double surprise(LinkedList<Cluster> clusters, Graph g) {
		double x = 0, y = 0;
		for (Cluster c : clusters) {
			x += c.getM();
			int kc = c.getN();
			y += CombinatoricsUtils.binomialCoefficientDouble(kc, 2);
		}
		x /= g.getM();
		y /= CombinatoricsUtils.binomialCoefficientDouble(g.getN(), 2);
		return deviation(x, y);

	}

	public static double significance(LinkedList<Cluster> clusters, Graph g) {
		double significance = 0, x = 0, y = 0;
		y = (double) g.getM() / CombinatoricsUtils.binomialCoefficient(g.getN(), 2);

		for (Cluster c : clusters) {
			int kc = c.getN();
			double coeffK = CombinatoricsUtils.binomialCoefficientDouble(kc, 2);
			x = c.getM() / coeffK;
			if (x == 1)
				x -= 0.0000000000000001;
			significance += (coeffK * deviation(x, y));
			// System.out.println("sign=" + significance);
		}
		return significance;
	}

	public static double[] graphLevel(LinkedList<Cluster> clusters, Graph g) {
		double[] values = new double[2];
		double coeffK = 0, a = 0, b = 0, x = 0, y = 0, significance = 0, surprise = 0;
		double coeffN = CombinatoricsUtils.binomialCoefficient(g.getN(), 2);
		y = (double) g.getM() / coeffN;

		for (Cluster c : clusters) {
			a += c.getM();
			int kc = c.getN();
			coeffK = CombinatoricsUtils.binomialCoefficientDouble(kc, 2);
			b += coeffK;
			x = (double) c.getM() / coeffK;
			if (x == 1)
				x -= 0.0000000000000001;

			// System.out.println(c.getM() == coeffK);
			significance += (coeffK * deviation(x, y));
			// System.out.println(deviation(x, y));
			// System.out.println("sign " + c.getName() + " = " + significance);

		}

		a /= (double) g.getM();
		b /= CombinatoricsUtils.binomialCoefficientDouble(g.getN(), 2);
		surprise = deviation(a, b);

		values[0] = surprise;
		values[1] = significance;
		return values;

	}

	public static double deviation(double x, double y) {
		double dev = x * Math.log(x / y) + (1 - x) * Math.log((1 - x) / (1 - y));
//		 System.out.println("x=" + x);
//		 System.out.println("y=" + y);
//		System.out.println("dev=" + dev);
//		if (y == 0)
//			System.err.println("y=" + y);
		return dev;
	}

	public static double multiplicityPrecision(Node n1, Node n2) {
		int sharedClusters = n1.getNumberSharedCluster(n2);
		if (sharedClusters == 0)
			return Double.MAX_VALUE;
		int sharedCategories = n1.getNumberSharedCategories(n2);

		return Math.min(sharedClusters, sharedCategories) / sharedClusters;
	}

	public static double multiplicityRecall(Node n1, Node n2) {
		int sharedCategories = n1.getNumberSharedCategories(n2);
		if (sharedCategories == 0)
			return Double.MAX_VALUE;
		int sharedClusters = n1.getNumberSharedCluster(n2);
		return Math.min(sharedClusters, sharedCategories) / sharedCategories;
	}

	public static double[] multiplicityPrecisionRecall(Node n1, Node n2) {

		int sharedClusters = n1.getNumberSharedCluster(n2);
		int sharedCategories = n1.getNumberSharedCategories(n2);
		double precision = sharedClusters == 0 ? Double.MAX_VALUE
				: Math.min(sharedClusters, sharedCategories) / sharedClusters;
		double recall = sharedCategories == 0 ? Double.MAX_VALUE
				: Math.min(sharedClusters, sharedCategories) / sharedCategories;
		double[] results = { precision, recall };
		return results;
	}

	public static double bCubedPrecision(Graph g) {
		double precision, internalSum = 0, externalSum = 0;
		HashMap<String, Node> nodes = g.getNodes();
		Iterator<Node> it1 = nodes.values().iterator();
		Iterator<Node> it2 = nodes.values().iterator();
		while (it1.hasNext()) {
			Node n1 = it1.next();
			int n = g.getN();
			internalSum = 0;
			while (it2.hasNext()) {
				Node n2 = it2.next();
				precision = multiplicityPrecision(n1, n2);
				if (precision != Double.MAX_VALUE) {
					internalSum += precision;
				} else
					n--;
			}
			externalSum += (internalSum / n);
		}
		precision = externalSum / g.getN();
		return precision;
	}

	public static double bCubedRecall(Graph g) {
		double recall, internalSum = 0, externalSum = 0;
		HashMap<String, Node> nodes = g.getNodes();
		Iterator<Node> it1 = nodes.values().iterator();
		Iterator<Node> it2 = nodes.values().iterator();

		while (it1.hasNext()) {
			Node n1 = it1.next();
			int n = g.getN();
			internalSum = 0;
			while (it2.hasNext()) {
				Node n2 = it2.next();
				recall = multiplicityRecall(n1, n2);

				if (recall != Double.MAX_VALUE) {
					internalSum += recall;
				} else
					n--;
			}
			externalSum += (internalSum / n);
		}
		recall = externalSum / g.getN();
		return recall;
	}

	public static double[] bCubedPrecisionRecall(Graph g) {
		double precision, recall, internalSumR = 0, externalSumR = 0, internalSumP = 0, externalSumP = 0;
		HashMap<String, Node> nodes = g.getNodes();

		double[] values = new double[2];

		Iterator<Node> it1 = nodes.values().iterator();
		Iterator<Node> it2 = nodes.values().iterator();

		while (it1.hasNext()) {
			Node n1 = it1.next();
			int nP = g.getN();
			int nR = g.getN();

			internalSumP = 0;
			internalSumR = 0;

			while (it2.hasNext()) {
				Node n2 = it2.next();
				values = multiplicityPrecisionRecall(n1, n2);
				// System.out.println(Arrays.toString(values));

				if (values[0] != Double.MAX_VALUE) {
					internalSumP += values[0];
				} else
					nP--;

				if (values[1] != Double.MAX_VALUE) {
					internalSumR += values[1];
				} else
					nR--;
			}
			externalSumP += (internalSumP / nP);
			externalSumR += (internalSumR / nR);
		}

		precision = externalSumP / g.getN();
		recall = externalSumR / g.getN();
		values[0] = precision;
		values[1] = recall;

		return values;
	}

	public static double fBCubed(Graph g) {
		double[] values = bCubedPrecisionRecall(g);
		// System.out.println("p=" + values[0]+"\tr="+values[1]);

		return 1 / (1 / (2 * values[0]) + 1 / (2 * values[1]));
	}

}
