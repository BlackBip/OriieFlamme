package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Collections;

/*Objet "Faction", elle représente un joueur
* On va retrouver dans les attributs, le nom de la faction, ses points DDRS, son score dans la partie
* ainsi que connaître les cartes qu'elles possèdent dans sa main et sa pioche*/
public class Faction {
	
    private String nom;
    private int ptsDDRS;
    private int score;
    private ArrayList<PickedCard> main;
    private Deck pioche;
    private boolean interpioche;

    /*On crée une faction que à partir de son nom
    * on initie tous les autres attributs qui seront les mêmes pour tous le monde au début*/
    public Faction(String nom) {
        this.nom = nom;
        ptsDDRS = 0;
        score = 0;
        main = new ArrayList<PickedCard>();
        pioche = new Deck(this);
        interpioche = false;
    }
    
    String getNom() {
    	return nom;
    }

    public int getPtsDDRS() {
        return ptsDDRS;
    }

    public void setPtsDDRS(int ptsDDRS) {
        this.ptsDDRS = Math.max(ptsDDRS,0);
    }

    /*Les points DDRS ne peuvent être négatif*/
    public void addPtsDDRS(int diff){
        this.ptsDDRS = Math.max(this.ptsDDRS+diff,0);
    }
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isInterpioche() {
        return interpioche;
    }

    public void setInterpioche(boolean interpioche) {
        this.interpioche = interpioche;
    }

    public int tailleMain(){
        return main.size();
    }
    
    public ArrayList<PickedCard> getMain(){
        return main;
    }

    /*On utilise la fonction shuffle de la classe Collections pour mélanger notre pioche*/
    public void shuffle(){
        Collections.shuffle(pioche);
    }

    /*On pioche la main du joueur*/
    public void pick(){
    	main = new ArrayList<PickedCard>();
        for (int i = 0; i < 8; i++) {
            main.add(pioche.pop());
        }
    }

    /*La fonction utilisé si une faction cherche à remélanger sa main */
    public void handshuffle(){
        if(!interpioche){
            for (int i = 0; i < 8; i++) {
                pioche.push(main.get(i));
            }
            shuffle();
            pick();
            interpioche=true;
        }
    }
    
    public ImageView getFactionImageView() {
    	return new ImageView(new Image("/design/dos_carte/Dos_"+getNom()+".png"));
    }
}
