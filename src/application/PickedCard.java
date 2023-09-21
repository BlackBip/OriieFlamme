package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/*Objet "CartePiochee"
* La classe hérite de la classe Card, on va connaître à qui appartient une carte ainsi que si celle ci est retournée
* C'est l'objet qui va se retrouver sur le plateau de jeu, dans la main et la pioche des deux factions.
* */
public class PickedCard extends Card{
    private Faction faction;
    private boolean returned;

    public PickedCard(String nom, String description, int nbOccurence,Faction faction) {
        super(nom, description, nbOccurence);
        this.faction=faction;
    }

    public PickedCard(Card other,Faction faction) {
        super(other);
        this.faction = faction;
    }

    public Faction getFaction() {
        return faction;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
    }

    @Override
    public String toString() {
        return "Card{" +
                "nom='" + nom + '\'' +
                '}';
    }

    /*Fonction pour obtenir l'image d'une carte à partir du nom de la carte et de la faction de la carte*/
    public ImageView getImageView() {
    	return new ImageView(new Image("/design/Les_cartes/"+faction.getNom()+"_png/"+getNom().replaceAll(" ", "_")+"_"+faction.getNom()+".png"));
    }

    public boolean isReturned(){
        return this.returned;
    }

    /*Fonction pour retrourner une carte
    * Si elle est déjà retournée, on va le mettre façe cachée,sinon elle sera façe découverte*/
    public void returnCard() {this.returned = !this.returned;}
}
