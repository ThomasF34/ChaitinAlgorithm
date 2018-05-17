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

        Edge a6 = new Edge(s6,s7);
        Edge a7 = new Edge(s6,s8);
        Edge a8 = new Edge(s8,s9);

        Edge a9 = new Edge(s7,s8); //Preference

        listEdgeInterf = new ArrayList<>();
        listEdgeInterf.add(a6);
        listEdgeInterf.add(a7);
        listEdgeInterf.add(a8);

        ArrayList<Edge> listEdgePref = new ArrayList<>();
        listEdgePref.add(a9);

        listVertex = new ArrayList<>();
        listVertex.add(s6);
        listVertex.add(s7);
        listVertex.add(s8);
        listVertex.add(s9);

        g = new Graph(listVertex, listEdgeInterf, listEdgePref);
        colorGraph(g, 2);
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

    private static void colorGraph(Graph graph, int nbCouleur){
        colorGraphPessimistic(graph, nbCouleur);
        colorGraphOptimistic(graph,nbCouleur);
    }

    private static void colorGraphOptimistic(Graph graph, int nbCouleur) {
        for(Vertex s : graph.getVertices()){
            if(s.getColor() == -1){
                for(int i = 1; i <= nbCouleur; i++){
                    if(graph.canBeColored(s,i)){
                        s.setColor(i);
                    }
                }
            }
        }
    }

    private static void colorGraphPessimistic(Graph graph, int nbCouleur){

        //Si il existe un s trivialement coloriable
        if(graph.getVertices().size() != 0) {
            boolean found = false;

            Vertex sTrivial = new Vertex(-1);
            for (Vertex s : graph.getVertices()) {
                if (graph.triviallyColoriable(s, nbCouleur)) {
                    found = true;
                    sTrivial = s;
                    break;
                }
            }
            ArrayList<Vertex> newVertices;
            ArrayList<Edge> newArretesInterferences;
            ArrayList<Edge> newArretesPreferences;
            if (found) {
                //Then on lance colorGraphPessimistic sans ce sommet
                //On attribut une color disponible

                newVertices = graph.getNewVertex(sTrivial);
                newArretesInterferences = graph.getNewInterEdges(sTrivial);
                newArretesPreferences = graph.getNewPrefEdges(sTrivial);
                colorGraphPessimistic(new Graph(newVertices, newArretesInterferences, newArretesPreferences), nbCouleur);
                graph.coloringVertex(sTrivial, nbCouleur);

            } else { //Pas de trivialement coloriable

                Vertex sPlusGrandDegre = new Vertex(-1);
                for (Vertex s : graph.getVertices()) {
                    if (graph.interDegre(s) == graph.maxDegree()) {
                        sPlusGrandDegre = s;
                    }
                }

                newVertices = graph.getNewVertex(sPlusGrandDegre);
                newArretesInterferences = graph.getNewInterEdges(sPlusGrandDegre);
                newArretesPreferences = graph.getNewPrefEdges(sPlusGrandDegre);
                colorGraphPessimistic(new Graph(newVertices, newArretesInterferences, newArretesPreferences), nbCouleur);
            }
        }

        //Sinon on lance colorGraphPessimistic sans un sommet (celui qui a le plus grand de degré)
        //On spill s

        //A la fin on regarde pour chaque sommet spillé, s'il y a une color disponible.

    }
}

class Graph {

    private ArrayList<Vertex> vertices;
    private ArrayList<Edge> interfEdges;
    private ArrayList<Edge> prefEdges;

    Graph(ArrayList<Vertex> vertices, ArrayList<Edge> interferences, ArrayList<Edge> preferences){
        prefEdges = preferences;
        interfEdges = interferences;
        this.vertices = vertices;
    }

    Graph(ArrayList<Vertex> vertices, ArrayList<Edge> interferences){
        prefEdges = new ArrayList<>();
        interfEdges = interferences;
        this.vertices = vertices;
    }

    public int interDegre(Vertex s) {
        int count = 0;
        for (Edge a : interfEdges) {
            if (a.concern(s)) {
                count++;
            }
        }
        return count;
    }

    public int maxDegree(){
        int max = 0;
        for(Vertex s : vertices){
            if(interDegre(s) > max){
                max = interDegre(s);
            }
        }
        return max;
    }

    public boolean triviallyColoriable(Vertex s, int nbCouleur){
        return interDegre(s) < nbCouleur;
    }

    public ArrayList<Vertex> getNewVertex(Vertex vertex) {
        ArrayList<Vertex> res = new ArrayList<>();
        for(Vertex s : vertices){
            if(!s.equals(vertex)){
                res.add(s);
            }
        }
        return res;
    }

    public ArrayList<Edge> getNewInterEdges(Vertex sTrivial) {
        ArrayList<Edge> res = new ArrayList<>();
        for(Edge a : interfEdges){
            if(!a.concern(sTrivial)){
                res.add(a);
            }
        }
        return res;
    }

    public ArrayList<Edge> getNewPrefEdges(Vertex sTrivial) {
        ArrayList<Edge> res = new ArrayList<>();
        for(Edge a : prefEdges){
            if(!a.concern(sTrivial)){
                res.add(a);
            }
        }
        return res;
    }

    public void coloringVertex(Vertex sTrivial, int nbColor) {
        if(prefColor(sTrivial) != -1 && canBeColored(sTrivial, prefColor(sTrivial))){
            sTrivial.setColor(prefColor(sTrivial));
        } else {
            for (int i = 1; i <= nbColor; i++) {
                if(canBeColored(sTrivial,i)){
                    sTrivial.setColor(i);
                    break;
                }
            }
        }
    }

    public boolean canBeColored(Vertex sTrivial, int color){
        for(Edge a : interfEdges){
            if(a.concern(sTrivial)){
                if(a.vertices[0].getColor() == color||a.vertices[1].getColor() == color){
                    return false;
                }
            }
        }
        return true;
    }

    private int prefColor(Vertex sTrivial){
        for(Edge a : prefEdges){
            if(a.concern(sTrivial)){
                if(a.vertices[0].equals(sTrivial)){
                    if(a.vertices[1].getColor() != -1){
                        return a.vertices[1].getColor();
                    }
                } else {
                    if(a.vertices[0].getColor() != -1){
                        return a.vertices[0].getColor();
                    }
                }
            }
        }
        return -1;
    }

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Vertex s : vertices){
            sb.append("Vertex ");
            sb.append(s.name);
            sb.append(" is colored with : ");
            sb.append(s.color);
            sb.append("\n");
        }
        return sb.toString();
    }
}

class Vertex {
    int color = -1;
    int name;

    Vertex(int name){
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return name + "  colored with  " + color;
    }
}

class Edge {
    Vertex vertices[] = new Vertex[2];

    Edge(Vertex s1, Vertex s2){
        this.vertices[0] = s1;
        this.vertices[1] = s2;
    }

    public boolean concern(Vertex s) {
        return (vertices[0].equals(s)|| vertices[1].equals(s));
    }

    @Override
    public String toString() {
        return "This edge concerns the vertex " + vertices[0].toString() + " and the vertex " + vertices[1].toString();
    }
}
