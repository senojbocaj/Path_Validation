// Jacob Jones | CS2336.501
// NetID : JDJ240000
// Date : 12/1/2024

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        // Get file names from the user
        System.out.print("Enter the galaxy map file name: ");
        String galaxyFileName = scanner.nextLine();
        System.out.print("Enter the pilot routes file name: ");
        String routesFileName = scanner.nextLine();

        Graph galaxy = new Graph();
        try {
            // Load galaxy map from file
            readGalaxyMap(galaxyFileName, galaxy);
        } catch (IOException e) {
            System.err.println("Error reading galaxy map file: " + e.getMessage());
            scanner.close();
            return;
        }

        List<Pilot> pilots;
        try {
            // Load pilot routes from file
            pilots = readPilotRoutes(routesFileName, galaxy);
        } catch (IOException e) {
            System.err.println("Error reading pilot routes file: " + e.getMessage());
            scanner.close();
            return; 
        }

        // Sort the pilots by path weight and name using quickSort method
        quickSort(pilots, 0, pilots.size() - 1);

        // Write results to output file
        String outputFileName = "patrols.txt";
        try {
            writeResults(pilots, outputFileName);
        } catch (IOException e) {
            System.err.println("Error writing results file: " + e.getMessage());
        }

        scanner.close();
    }

    private static void readGalaxyMap(String fileName, Graph galaxy) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length < 2) {
                    continue;
                }
                String vertex = parts[0];
                galaxy.addVertex(vertex);
                for (int i = 1; i < parts.length; i++) {
                    String[] edgeParts = parts[i].split(",");
                    if (edgeParts.length == 2) {
                        try {
                            String to = edgeParts[0];
                            int weight = Integer.parseInt(edgeParts[1]);
                            galaxy.addEdge(vertex, to, weight);
                        } catch (NumberFormatException e) {
                            System.err.println("Skipping malformed edge: " + parts[i]);
                        }
                    }
                }
            }
        }
    }

    private static List<Pilot> readPilotRoutes(String fileName, Graph galaxy) throws IOException {
        List<Pilot> pilots = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length < 2) {
                    continue; 
                }
                String name = parts[0];
                List<String> path = Arrays.asList(parts).subList(1, parts.length);
                boolean valid = galaxy.isPathValid(path);
                int weight = valid ? galaxy.calculatePathWeight(path) : 0;
                pilots.add(new Pilot(name, weight, valid));
            }
        }
        return pilots;
    }

    private static void writeResults(List<Pilot> pilots, String fileName) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (Pilot pilot : pilots) {
                // Write pilot data to file
                bw.write(pilot.name + " " + pilot.pathWeight + " " + (pilot.valid ? "valid" : "invalid") + "\n");
            }
        }
    }

    private static void quickSort(List<Pilot> pilots, int low, int high) { // quickSort method to sort output based on pathWeight low to high
        if (low < high) {
            int pivotIndex = partition(pilots, low, high);
            quickSort(pilots, low, pivotIndex - 1);
            quickSort(pilots, pivotIndex + 1, high);
        }
    }

    private static int partition(List<Pilot> pilots, int low, int high) {
        Pilot pivot = pilots.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (comparePilots(pilots.get(j), pivot) <= 0) {
                i++;
                swap(pilots, i, j);
            }
        }
        swap(pilots, i + 1, high);
        return i + 1;
    }

    private static int comparePilots(Pilot x, Pilot y) {
        if (x.pathWeight != y.pathWeight) {
            return Integer.compare(x.pathWeight, y.pathWeight);
        }
        return x.name.compareTo(y.name); // Secondary comparison by alphabet
    }

    private static void swap(List<Pilot> pilots, int i, int j) {
        // Swap two pilots in the list
        Pilot temp = pilots.get(i);
        pilots.set(i, pilots.get(j));
        pilots.set(j, temp);
    }

    private static class Pilot {
        String name;
        int pathWeight;
        boolean valid;

        public Pilot(String name, int pathWeight, boolean valid) {
            this.name = name;
            this.pathWeight = pathWeight;
            this.valid = valid;
        }
    }
}
