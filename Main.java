import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        System.out.println("Please consult README.md to see images of printed graphs\n");

	//Three tests
        // First test - Common graphe





        // Second test - Graph with preferences

        Sommet s1 = new Sommet(1);
        Sommet s2 = new Sommet(2);
        Sommet s3 = new Sommet(3);
        Sommet s4 = new Sommet(4);
        Arrete a1 = new Arrete(s1,s2);
        Arrete a2 = new Arrete(s1,s3);
        Arrete a3 = new Arrete(s1,s4);
        Arrete a4 = new Arrete(s2,s4);

        Arrete a5 = new Arrete(s1,s4);

        ArrayList<Arrete> listArreteInterf = new ArrayList<>();
        listArreteInterf.add(a1);
        listArreteInterf.add(a2);
        listArreteInterf.add(a3);
        listArreteInterf.add(a4);

        ArrayList<Arrete> listArretePref = new ArrayList<>();
        listArretePref.add(a5);


        ArrayList<Sommet> listSommet = new ArrayList<>();
        listSommet.add(s1);
        listSommet.add(s2);
        listSommet.add(s3);
        listSommet.add(s4);

        Graphe g = new Graphe(listSommet,listArreteInterf,listArretePref);

        colorier(g, 3);
        System.out.println(g);

        // Third test - Graph with spill
        // Graph with "spill" : First print will show the pessimistic version of the algorithm, and second one will show optimistic version.

        Sommet s5 = new Sommet(5);
        Sommet s6 = new Sommet(6);
        Sommet s7 = new Sommet(7);
        Sommet s8 = new Sommet(8);

        Arrete a6 = new Arrete(s5,s6);
        Arrete a7 = new Arrete(s6,s7);
        Arrete a8 = new Arrete(s7,s8);
        Arrete a9 = new Arrete(s8,s5);

        listArreteInterf = new ArrayList<>();
        listArreteInterf.add(a6);
        listArreteInterf.add(a7);
        listArreteInterf.add(a8);
        listArreteInterf.add(a9);

        listArretePref = new ArrayList<>();

        listSommet = new ArrayList<>();
        listSommet.add(s5);
        listSommet.add(s6);
        listSommet.add(s7);
        listSommet.add(s8);

        g = new Graphe(listSommet,listArreteInterf,listArretePref);

        colorier(g, 2);
        System.out.println(g);

    }

    private static void colorier(Graphe graphe, int nbCouleur){
        colorierPessimiste(graphe, nbCouleur);
        colorierOptimiste(graphe,nbCouleur);
    }

    private static void colorierOptimiste(Graphe graphe, int nbCouleur) {
        for(Sommet s : graphe.getSommets()){
            if(s.getCouleur() == -1){
                for(int i = 1; i <= nbCouleur; i++){
                    if(graphe.canBeColored(s,i)){
                        s.setCouleur(i);
                    }
                }
            }
        }
    }

    private static void colorierPessimiste(Graphe graphe, int nbCouleur){

        //Si il existe un s trivialement coloriable
        if(graphe.getSommets().size() != 0) {
            boolean found = false;

            Sommet sTrivial = new Sommet(-1);
            for (Sommet s : graphe.getSommets()) {
                if (graphe.trivialementColor(s, nbCouleur)) {
                    found = true;
                    sTrivial = s;
                    break;
                }
            }
            ArrayList<Sommet> newSommets;
            ArrayList<Arrete> newArretesInterferences;
            ArrayList<Arrete> newArretesPreferences;
            if (found) {
                //Then on lance colorierPessimiste sans ce sommet
                //On attribut une couleur disponible

                newSommets = graphe.getNewSommets(sTrivial);
                newArretesInterferences = graphe.getNewArretesInterferences(sTrivial);
                newArretesPreferences = graphe.getNewArretesPreferences(sTrivial);

                colorierPessimiste(new Graphe(newSommets, newArretesInterferences, newArretesPreferences), nbCouleur);
                graphe.colorationSommet(sTrivial, nbCouleur);

            } else { //Pas de trivialement coloriable

                Sommet sPlusGrandDegre = new Sommet(-1);
                for (Sommet s : graphe.getSommets()) {
                    if (graphe.degreInterf(s) == graphe.degreMax()) {
                        sPlusGrandDegre = s;
                    }
                }

                newSommets = graphe.getNewSommets(sPlusGrandDegre);
                newArretesInterferences = graphe.getNewArretesInterferences(sPlusGrandDegre);
                newArretesPreferences = graphe.getNewArretesPreferences(sPlusGrandDegre);
                colorierPessimiste(new Graphe(newSommets, newArretesInterferences, newArretesPreferences), nbCouleur);
            }
        }

        //Sinon on lance colorierPessimiste sans un sommet (celui qui a le plus grand de degré)
        //On spill s

        //A la fin on regarde pour chaque sommet spillé, s'il y a une couleur disponible.

    }
}

class Graphe{

    private ArrayList<Sommet> sommets;
    private ArrayList<Arrete> arretesInterferences;
    private ArrayList<Arrete> arretesPreferences;

    Graphe(ArrayList<Sommet> sommets, ArrayList<Arrete> interferences, ArrayList<Arrete> preferences){
        arretesPreferences = preferences;
        arretesInterferences = interferences;
        this.sommets = sommets;
    }

    Graphe(ArrayList<Sommet> sommets, ArrayList<Arrete> interferences){
        arretesPreferences = new ArrayList<>();
        arretesInterferences = interferences;
        this.sommets = sommets;
    }

    public int degreInterf(Sommet s) {
        int count = 0;
        for (Arrete a : arretesInterferences) {
            if (a.concern(s)) {
                count++;
            }
        }
        return count;
    }

    public int degreMax(){
        int max = 0;
        for(Sommet s : sommets){
            if(degreInterf(s) > max){
                max = degreInterf(s);
            }
        }
        return max;
    }

    public boolean trivialementColor(Sommet s, int nbCouleur){
        return degreInterf(s) < nbCouleur;
    }

    public ArrayList<Sommet> getNewSommets(Sommet sommet) {
        ArrayList<Sommet> res = new ArrayList<>();
        for(Sommet s : sommets){
            if(!s.equals(sommet)){
                res.add(s);
            }
        }
        return res;
    }

    public ArrayList<Arrete> getNewArretesInterferences(Sommet sTrivial) {
        ArrayList<Arrete> res = new ArrayList<>();
        for(Arrete a : arretesInterferences){
            if(!a.concern(sTrivial)){
                res.add(a);
            }
        }
        return res;
    }

    public ArrayList<Arrete> getNewArretesPreferences(Sommet sTrivial) {
        ArrayList<Arrete> res = new ArrayList<>();
        for(Arrete a : arretesPreferences){
            if(!a.concern(sTrivial)){
                res.add(a);
            }
        }
        return res;
    }

    public void colorationSommet(Sommet sTrivial, int nbCouleur) {
        if(couleurPref(sTrivial) != -1 && canBeColored(sTrivial, couleurPref(sTrivial))){
            sTrivial.setCouleur(couleurPref(sTrivial));
        } else {
            for (int i = 1; i <= nbCouleur; i++) {
                if(canBeColored(sTrivial,i)){
                    sTrivial.setCouleur(i);
                    break;
                }
            }
        }
    }

    public boolean canBeColored(Sommet sTrivial, int color){
        for(Arrete a : arretesInterferences){
            if(a.concern(sTrivial)){
                if(a.sommets[0].getCouleur() == color||a.sommets[1].getCouleur() == color){
                    return false;
                }
            }
        }
        return true;
    }

    private int couleurPref(Sommet sTrivial){
        for(Arrete a : arretesPreferences){
            if(a.concern(sTrivial)){
                if(a.sommets[0].equals(sTrivial)){
                    if(a.sommets[0].getCouleur() != -1){
                        return a.sommets[0].getCouleur();
                    }
                } else {
                    if(a.sommets[1].getCouleur() != -1){
                        return a.sommets[1].getCouleur();
                    }
                }
            }
        }
        return -1;
    }

    public ArrayList<Sommet> getSommets() {
        return sommets;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Sommet s : sommets){
            sb.append("Le sommet ");
            sb.append(s.nom);
            sb.append(" est de couleur ");
            sb.append(s.couleur);
            sb.append("\n");
        }
        return sb.toString();
    }
}

class Sommet{
    int couleur = -1;
    int nom;

    Sommet(int nom){
        this.nom = nom;
    }

    public int getCouleur() {
        return couleur;
    }

    public void setCouleur(int couleur) {
        this.couleur = couleur;
    }

    @Override
    public String toString() {
        return nom + "  de couleur  " + couleur;
    }
}

class Arrete{
    Sommet sommets[] = new Sommet[2];

    Arrete(Sommet s1, Sommet s2){
        this.sommets[0] = s1;
        this.sommets[1] = s2;
    }

    public boolean concern(Sommet s) {
        return (sommets[0].equals(s)|| sommets[1].equals(s));
    }

    @Override
    public String toString() {
        return "L'arrete concerne le sommet " + sommets[0].toString() + " et le sommet " + sommets[1].toString();
    }
}
