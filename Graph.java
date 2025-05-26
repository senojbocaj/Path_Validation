// Jacob Jones | CS2336.501
// NetID : JDJ240000
// Date : 12/1/2024

import java.util.*;

public class Graph {
    // A mapping of vertices to their list of edges
    private HashMap<String, List<Edge>> adjacencyMap;

    // Constructor
    public Graph() {
        adjacencyMap = new HashMap<>(); 
    }

    // Adds a vertex to the graph, if it doesn't already exist
    public void addVertex(String vertex) {
        if (!adjacencyMap.containsKey(vertex)) { 
            adjacencyMap.put(vertex, new ArrayList<>());
        }
    }

    // Adds an edge between two vertices with a specified weight
    public void addEdge(String from, String to, int weight) {
        addVertex(from); // Ensure 'from' vertex exists
        addVertex(to);   // Ensure 'to' vertex exists
        
        // Fetch edges for the 'from' vertex
        List<Edge> edges = adjacencyMap.get(from);
        for (Edge edge : edges) {
            if (edge.destination.equals(to)) {
                return; // Avoid dupes
            }
        }

        edges.add(new Edge(to, weight)); // Add the edge
    }

    // Checks if a given path is valid in the graph
    public boolean isPathValid(List<String> path) {
        if (path == null || path.isEmpty()) { 
            return false;
        }

        for (int i = 0; i < path.size() - 1; i++) {
            String from = path.get(i);
            String to = path.get(i + 1);

            // If 'from' vertex doesn't exist
            if (!adjacencyMap.containsKey(from)) {
                return false;
            }

            boolean edgeExists = false;
            for (Edge edge : adjacencyMap.get(from)) {
                if (edge.destination.equals(to)) {
                    edgeExists = true;
                    break;
                }
            }

            if (!edgeExists) {
                return false; // Missing edge in path
            }
        }

        return true; // All edges in path are valid
    }

    // Computes the total weight of a given path
    public int calculatePathWeight(List<String> path) {
        if (path == null || path.size() < 2) { 
            return 0;
        }

        int totalWeight = 0;

        for (int i = 0; i < path.size() - 1; i++) {
            String from = path.get(i);
            String to = path.get(i + 1);

            boolean edgeFound = false;
            for (Edge edge : adjacencyMap.getOrDefault(from, Collections.emptyList())) {
                if (edge.destination.equals(to)) {
                    totalWeight += edge.weight;
                    edgeFound = true;
                    break;
                }
            }

            if (!edgeFound) {
                return 0; // Invalid path
            }
        }

        return totalWeight;
    }

    // Private class to represent an edge in the graph
    private static class Edge {
        String destination; // Where this edge points to
        int weight;         // Weight of the edge

        public Edge(String destination, int weight) {
            this.destination = destination;
            this.weight = weight;
        }
    }
}