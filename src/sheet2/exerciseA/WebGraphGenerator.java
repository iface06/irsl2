package sheet2.exerciseA;

import edu.uci.ics.jung.algorithms.scoring.PageRank;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import java.io.*;
import java.util.*;
import org.apache.lucene.util.IOUtils;

public class WebGraphGenerator {

    // LAMBDA corresponds to the notation in the lecture
    public static final double LAMBDA = 0.15d;
    // ID counter of the edges
    public static int edgeId = 0;
    public Map<String, Collection<String>> documentsToUrlsMapping = new HashMap<>();
    public Map<String, Collection<String>> urlsToDocumentsMapping = new HashMap<>();

    // see exercise 10.3 part 1
    public Graph<String, Integer> generateWebGraph(String filename) throws FileNotFoundException {

        createUrlsToDocumentMapping(filename);
        Graph<String, Integer> graph = new DirectedSparseGraph<>();
        for (String vertex : documentsToUrlsMapping.keySet()) {
            graph.addVertex(vertex);
            for (String url : documentsToUrlsMapping.get(vertex)) {
                Collection<String> eadgesByUrls = urlsToDocumentsMapping.get(url);
                if (eadgesByUrls != null && !eadgesByUrls.isEmpty()) {
                    for (String document : eadgesByUrls) {
                        graph.addEdge(edgeId++, vertex, document);
                    }
                }
            }

        }

        return graph;

    }

    // see exercise 10.3 part 2
    public void computeAndPrintPageRanks(Graph<String, Integer> webgraph, int numResults, int maxItertions) {
        // TODO: Your code should go here!
        PageRank<String, Integer> ranking = new PageRank<>(webgraph, 0.15);
        ranking.setMaxIterations(10);
        ranking.evaluate();


        TreeMap<Double, String> mapping = new TreeMap<>();
        for (String document : documentsToUrlsMapping.keySet()) {
            double score = ranking.getVertexScore(document);
            mapping.put(score, document);
        }

        SortedSet<Double> scores = mapping.descendingKeySet().subSet(1.0, 0.0);
        int i = 0;
        for (Double score : scores) {
            if (i < 5) {
                System.out.println(score + " -> " + mapping.get(score));
                i++;
            } else {
                break;
            }
        }

    }

    // Main method for testing
    public static void main(String[] args) throws FileNotFoundException {

        // the web graph generator
        WebGraphGenerator generator = new WebGraphGenerator();

        // filename where 'source -> dest' pairs are storend
        String filename = "linkage.txt";

        // generate the web graph
        Graph<String, Integer> webgraph = generator.generateWebGraph(filename);

        // check if a valid result is generated
        if (webgraph == null) {
            throw new RuntimeException("Work on the TODOs");
        }

        System.out.println("WebGraph is built.");

        // the desired number of results
        int numResults = 5;
        // maximum number of iterations
        int maxIterations = 10;
        // compute and print page ranks
        generator.computeAndPrintPageRanks(webgraph, numResults, maxIterations);

    }

    private void createUrlsToDocumentMapping(String filename) throws FileNotFoundException {
        Scanner in = new Scanner(new FileReader(filename));
        in.useDelimiter("\\n");
        while (in.hasNext()) {
            String line = in.next();
            String[] splittedLine = line.split(" -> ");
            if (splittedLine.length == 2) {
                if (!urlsToDocumentsMapping.containsKey(splittedLine[1])) {
                    urlsToDocumentsMapping.put(splittedLine[1], new ArrayList());
                }

                if (!documentsToUrlsMapping.containsKey(splittedLine[0])) {
                    documentsToUrlsMapping.put(splittedLine[0], new ArrayList());
                }

                urlsToDocumentsMapping.get(splittedLine[1]).add(splittedLine[0]);
                documentsToUrlsMapping.get(splittedLine[0]).add(splittedLine[1]);

            }
        }
    }
}
