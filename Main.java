import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        System.out.println("Please consult README.md to see image of printed graphs");

    //Three tests
        // First test - Common graphe
        Vertex s1 = new Vertex(1);
        Vertex s2 = new Vertex(2);
        Vertex s3 = new Vertex(3);
        Vertex s4 = new Vertex(4);
        Vertex s5 = new Vertex(5);

        Edge a1 = new Edge(s1,s2);
        Edge a2 = new Edge(s1,s3);
        Edge a3 = new Edge(s2,s3);
        Edge a4 = new Edge(s3,s4);
        Edge a5 = new Edge(s2,s5);


        ArrayList<Edge> listEdgeInterf = new ArrayList<>();
        listEdgeInterf.add(a1);
        listEdgeInterf.add(a2);
        listEdgeInterf.add(a3);
        listEdgeInterf.add(a4);
        listEdgeInterf.add(a5);

        ArrayList<Vertex> listVertex = new ArrayList<>();
        listVertex.add(s1);
        listVertex.add(s2);
        listVertex.add(s3);
        listVertex.add(s4);
        listVertex.add(s5);

        Graph g = new Graph(listVertex, listEdgeInterf);
        colorGraph(g, 3);
        System.out.println(g);

        // Second test - Graph with preferences

        Vertex s6 = new Vertex(6);
        Vertex s7 = new Vertex(7);
        Vertex s8 = new Vertex(8);
        Vertex s9 = new Vertex(9);
        Vertex s14 = new Vertex(10);

        Edge a6 = new Edge(s6,s7);
        Edge a7 = new Edge(s6,s8);
        Edge a8 = new Edge(s8,s9);
        Edge a14 = new Edge(s14,s7);
        Edge a15 = new Edge(s14,s6);


        Edge a9 = new Edge(s7,s8); //Preference

        listEdgeInterf = new ArrayList<>();
        listEdgeInterf.add(a6);
        listEdgeInterf.add(a7);
        listEdgeInterf.add(a8);
        listEdgeInterf.add(a14);
        listEdgeInterf.add(a15);


        ArrayList<Edge> listEdgePref = new ArrayList<>();
        listEdgePref.add(a9);

        listVertex = new ArrayList<>();
        listVertex.add(s6);
        listVertex.add(s7);
        listVertex.add(s8);
        listVertex.add(s9);
        listVertex.add(s14);


        g = new Graph(listVertex, listEdgeInterf, listEdgePref);
        colorGraph(g, 3);
        System.out.println(g);

        // Third test - Graph with spill
        // Graph with "spill" : First print will show the pessimistic version of the algorithm, and second one will show optimistic version.

        Vertex s10 = new Vertex(10);
        Vertex s11 = new Vertex(11);
        Vertex s12 = new Vertex(12);
        Vertex s13 = new Vertex(13);

        Edge a10 = new Edge(s10,s11);
        Edge a11 = new Edge(s11,s12);
        Edge a12 = new Edge(s12,s13);
        Edge a13 = new Edge(s13,s10);

        listEdgeInterf = new ArrayList<>();
        listEdgeInterf.add(a10);
        listEdgeInterf.add(a11);
        listEdgeInterf.add(a12);
        listEdgeInterf.add(a13);

        listEdgePref = new ArrayList<>();

        listVertex = new ArrayList<>();
        listVertex.add(s10);
        listVertex.add(s11);
        listVertex.add(s12);
        listVertex.add(s13);

        g = new Graph(listVertex, listEdgeInterf, listEdgePref);

        colorGraph(g, 2);
        System.out.println(g);

    }

    private static void colorGraph(Graph graph, int nbColor){
        colorGraphPessimistic(graph, nbColor);
        colorGraphOptimistic(graph,nbColor);
    }

    private static void colorGraphOptimistic(Graph graph, int nbColor) {
        for(Vertex s : graph.getVertices()){
            if(s.getColor() == -1){
                for(int i = 1; i <= nbColor; i++){
                    if(graph.canBeColored(s,i)){
                        s.setColor(i);
                    }
                }
            }
        }
    }

    private static void colorGraphPessimistic(Graph graph, int nbColor){

        if(graph.getVertices().size() != 0) {
            boolean found = false;

            Vertex sTrivial = new Vertex(-1);
            for (Vertex s : graph.getVertices()) {
                if (graph.triviallyColoriable(s, nbColor)) {
                    found = true;
                    sTrivial = s;
                    break;
                }
            }
            ArrayList<Vertex> newVertices;
            ArrayList<Edge> newArretesInterferences;
            ArrayList<Edge> newArretesPreferences;

            //If a trivially colorable vertex exists
            if (found) {
                //Then we execute colorGraphPessimistic without this vertex
                newVertices = graph.getNewVertex(sTrivial);
                newArretesInterferences = graph.getNewInterEdges(sTrivial);
                newArretesPreferences = graph.getNewPrefEdges(sTrivial);
                colorGraphPessimistic(new Graph(newVertices, newArretesInterferences, newArretesPreferences), nbColor);

                //Coloring the vertex with available color
                graph.coloringVertex(sTrivial, nbColor);

            } else { //No trivally coloriable vertex

                Vertex verBigestDegree = new Vertex(-1);
                for (Vertex s : graph.getVertices()) {
                    if (graph.interDegre(s) == graph.maxDegree()) {
                        verBigestDegree = s;
                    }
                }

                newVertices = graph.getNewVertex(verBigestDegree);
                newArretesInterferences = graph.getNewInterEdges(verBigestDegree);
                newArretesPreferences = graph.getNewPrefEdges(verBigestDegree);
                colorGraphPessimistic(new Graph(newVertices, newArretesInterferences, newArretesPreferences), nbColor);
            }
        }
    }
}
