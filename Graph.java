import java.util.ArrayList;

public class Graph {

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

    public boolean triviallyColoriable(Vertex s, int nbColor){
        return interDegre(s) < nbColor;
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
