package application;

/* Objet carte, elle contient le nom, la description ainsi que le nombre de fois que on va retrouver une carte dans la pioche d'un joueur
* */
public class Card {
    protected String nom;
    protected String description;
    protected int nbOccurence;

    public Card(String nom, String description, int nbOccurence) {
        this.nom = nom;
        this.description = description;
        this.nbOccurence = nbOccurence;
    }

    public Card(Card other) {
        this.nom = other.getNom();
        this.description = other.getDescription();
        this.nbOccurence = other.getNbOccurence();
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNbOccurence() {
        return nbOccurence;
    }

    public void setNbOccurence(int nbOccurence) {
        this.nbOccurence = nbOccurence;
    }

    @Override
    public String toString() {
        return "Card{" +
                "nom='" + nom + '\'' +
                '}';
    }
}
