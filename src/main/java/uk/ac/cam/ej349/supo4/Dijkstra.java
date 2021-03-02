package uk.ac.cam.ej349.supo4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

public class Dijkstra {

    private static class Vertex implements Comparable<Vertex> {
        int x;
        int y;
        int distance;
        List<Vertex> path;

        Vertex(int x, int y) {
            this.x = x;
            this.y = y;
            this.distance = 0;
            this.path = null;
        }

        @Override
        public String toString() {
            return String.format("(%d, %d)", this.x, this.y);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Vertex vertex = (Vertex) o;
            return x == vertex.x && y == vertex.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public int compareTo(Vertex o) {
            return this.distance - o.distance;
        }
    }

    private static Set<Vertex> getVertices(List<List<Integer>> maze) {
        Set<Vertex> vertices = new HashSet<>();
        for (int i = 0; i < maze.size(); i++) {
            for (int j = 0; j < maze.get(0).size(); j++) {
                if (maze.get(i).get(j) == 0 || maze.get(i).get(j) == 2 || maze.get(i).get(j) == 3) {
                    vertices.add(new Vertex(j, i));
                }
            }
        }
        return vertices;
    }

    private static Map<Vertex, Set<Vertex>> getEdges(Set<Vertex> vertices) {
        Map<Vertex, Set<Vertex>> edges = new HashMap<>();
        for (Vertex vertex : vertices) {
            edges.put(vertex, new HashSet<>());
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if ((i + j) % 2 != 0) {
                        Vertex candidateNeighbour = new Vertex(vertex.x + j, vertex.y + i);
                        for (Vertex v : vertices) {
                            if (v.equals(candidateNeighbour)) {
                                edges.get(vertex).add(v);
                            }
                        }
                    }
                }
            }
        }
        return edges;
    }

    private static Vertex getStart(List<List<Integer>> maze) {
        Vertex start = null;
        for (int i = 0; i < maze.size(); i++) {
            for (int j = 0; j < maze.get(0).size(); j++) {
                if (maze.get(i).get(j) == 2) {
                    start = new Vertex(j, i);
                }
            }
        }
        return start;
    }

    private static Vertex getEnd(List<List<Integer>> maze) {
        Vertex end = null;
        for (int i = 0; i < maze.size(); i++) {
            for (int j = 0; j < maze.get(0).size(); j++) {
                if (maze.get(i).get(j) == 3) {
                    end = new Vertex(j, i);
                }
            }
        }
        return end;
    }

    private static List<Vertex> shortestPath(Set<Vertex> vertices, Map<Vertex, Set<Vertex>> edges, Vertex start, Vertex end) {
        List<Vertex> shortestPath = new ArrayList<>();
        PriorityQueue<Vertex> toExplore = new PriorityQueue<>();
        for (Vertex vertex : vertices) {
            if (vertex.equals(start)) {
                vertex.distance = 0;
                vertex.path = new ArrayList<>(List.of(vertex));
                toExplore.add(vertex);
            } else {
                vertex.distance = Integer.MAX_VALUE;
            }
        }
        while (!toExplore.isEmpty()) {
            Vertex v = toExplore.poll();
            for (Vertex neighbour : edges.get(v)) {
                int dist_w = v.distance + 1;
                if (dist_w < neighbour.distance) {
                  neighbour.distance = dist_w;
                  neighbour.path = new ArrayList<>(v.path);
                  neighbour.path.add(neighbour);
                  toExplore.remove(neighbour);
                  toExplore.add(neighbour);
                }
            }
        }
        for (Vertex vertex : vertices) {
            if (vertex.equals(end)) {
                shortestPath = vertex.path;
            }
        }
        return shortestPath;
    }

    private static List<List<Integer>> toList(String input) throws IOException {
        List<List<String>> symbols = new ArrayList<>();
        List<List<Integer>> output = new ArrayList<>();
        String[] lines = input.split("\n");
        int maxlinesize = 0;
        for (String line : lines) {
            String[] characters = line.split("");
            List<String> symline = new ArrayList<String>(Arrays.asList(characters));
            if (symline.size() > maxlinesize) {
                maxlinesize = symline.size();
            }
            symbols.add(symline);
        }
        for (List<String> symline : symbols) {
            while (symline.size() < maxlinesize) {
                symline.add(" ");
            }
        }
        for (int i = 0; i < symbols.size(); i++) {
            List<Integer> line = new ArrayList<>();
            for (int j = 0; j < symbols.get(0).size(); j++) {
                if (symbols.get(i).get(j).equals("#")) {
                    line.add(1);
                } else if (symbols.get(i).get(j).equals(" ")) {
                    line.add(0);
                } else if (symbols.get(i).get(j).equals("S")) {
                    line.add(2);
                } else {
                    line.add(3);
                }
            }
            output.add(line);
        }
        return output;
    }

    private static String dijkstra(String input) throws IOException {
        List<List<Integer>> list = toList(input);
        Set<Vertex> vertices = getVertices(list);
        Map<Vertex, Set<Vertex>> edges = getEdges(vertices);
        Vertex start = getStart(list);
        Vertex end = getEnd(list);

        List<Vertex> path = shortestPath(vertices, edges, start, end);

        StringBuilder output = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(0).size(); j++) {
                if (path.contains(new Vertex(j, i)) && list.get(i).get(j) != 2 && list.get(i).get(j) != 3) {
                    list.get(i).set(j, 4);
                }
            }
        }
        for (List<Integer> row : list) {
            for (Integer column : row) {
                switch (column) {
                    case 0 -> output.append(' ');
                    case 1 -> output.append('#');
                    case 2 -> output.append('S');
                    case 3 -> output.append('G');
                    case 4 -> output.append('.');
                }
            }
            output.append('\n');
        }
        return output.toString();
    }

    public static void main(String[] args) throws IOException {

        String input = """
                #####   #####
                      #S  # G
                 #### ### # #
                 # #  # ###  
                   ##     ## 
                ##    ###  # 
                 ######        
                """;

        System.out.println(dijkstra(input));
    }
}
