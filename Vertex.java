public class Vertex {
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
