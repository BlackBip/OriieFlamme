package application;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;


// Objet permettant de gérer l'affichage d'informations
public class GUI {
	
	//Grille 16*16 pour afficher les cartes/les informations diverses
	private GridPane gridpane;
	// Champs de texte divers
	private Text scores;
	private Text description;
	private Text instructions;
	// Plateau
	private Board board;
	// Coordonnées permettant de centrer la grille 32*32 qu'est en réalité le plateau sur une grille 16*16
	private int start_x;
	private int start_y;
	// Permet de toggle la mise à jour automatique de la description des cartes. Non utilisé sur cette version
	private boolean update_card_desc;
	// Permet d'indiquer quelle main doit être visible
	private Faction card_visibility;
	
	public GUI(GridPane gridpane, Text description, Text scores, Text instructions, Board b) {
		this.gridpane = gridpane;
		this.description = description;
		this.scores = scores;
		this.board = b;
		this.instructions = instructions;
		clearDescription();
		clearScores();
		clearInstruction();
		start_x = 0;
		start_y = 0;
		update_card_desc = true;
		card_visibility = board.getFaction1();
	}
	
	//Donne les coordonnées associées à la grille 32*32 du plateau ou aux mains à partir de celles de la souris sur la scène :
	//Main de la faction verte : (-100,numéro de la carte dans la main)
	//Main de la faction rouge : (-200,numéro de la carte dans la main)
	//Sinon : (i,j) associés à la grille 32*32 du plateau
	public ArrayList<Integer> getCoordinates(double mouseX, double mouseY) {
		int x = (int) (mouseX/100);
		int y = (int) (mouseY/60);
		ArrayList<Integer> output = new ArrayList<Integer>();
		
		//Mains
		if (((y>=14) && (x<=3)) || ((y<=1) && (x>=12))) {
			if (y==15) {
				output.add(-100);
				output.add(x);
			}
			else if (y==14){
				output.add(-100);
				output.add(x+4);
			}
			else if (y==0){
				output.add(-200);
				output.add(15-x);
			}
			else{
				output.add(-200);
				output.add(19-x);
			}
		}
		
		//Plateau
		else {
			output.add(x+start_x);
			output.add(y+start_y);
		}
		return output;
	}
	
	//Fonctions pour afficher du texte prédéfini ou non dans les différents champs de textes
	public void setInstructions(String s) {
		instructions.setText("    "+s);
	}
	
	public void clearInstruction() {
		setInstructions("");
	}
	
	public void setDescription(String s) {
		description.setText(s);
	}
	
	public void clearDescription() {
		setDescription("");
	}
	
	public void setInstructionsDDRS() {
		instructions.setText("    Points DDRS :\n    Equipe rouge - "+board.getFaction2().getPtsDDRS()+"\n    Equipe verte - "+board.getFaction1().getPtsDDRS()+"  (c pour continuer)");
	}
	
	public void toggleUpdateCardDesc() {
		update_card_desc = !update_card_desc;
	}
	
	//Affiche la description des cartes si elles sont visibles ou appartiennent à la faction qui joue
	public void getDescription(ArrayList<Integer> coords) {
		if (update_card_desc) {
			//Main verte
			if (coords.get(0)==-100 && card_visibility==board.getFaction1()) {
				if (coords.get(1)<board.getFaction1().getMain().size()) {
					setDescription(board.getFaction1().getMain().get(coords.get(1)).getDescription());
				}
				else {
					clearDescription();
				}
			}
			//Main rouge
			else if (coords.get(0)==-200 && card_visibility==board.getFaction2()) {
				if (coords.get(1)<board.getFaction2().getMain().size()) {
					setDescription(board.getFaction2().getMain().get(coords.get(1)).getDescription());
				}
				else {
					clearDescription();
				}
			}
			//Plateau
			else {
				if (coords.get(0)>=0 && coords.get(1)>=0 && board.getGrille()[coords.get(0)][coords.get(1)] != null && board.getGrille()[coords.get(0)][coords.get(1)].isReturned()) {
					setDescription(board.getGrille()[coords.get(0)][coords.get(1)].getDescription());
				}
				else {
					clearDescription();
				}
			}
		}
	}
	
	public void setScores(int a, int b) {
		scores.setText("Equipe rouge - "+a+"\nEquipe verte - "+b);
	}
	
	public void clearScores() {
		setScores(0,0);
	}
	
	// Met une image sur la grille 16*16
	public void setImage(ImageView image, int x, int y) {
		gridpane.add(image, x, y);
	}
	
	//Affiche une main, cachée ou non
	public void showHand(ArrayList<PickedCard> main, Boolean hidden) {
		if (main.size() != 0) {
			if (main.get(0).getFaction().getNom()=="vert") {
				if (hidden) {
					for(int j=0;j<8;j++) {
						clearImage(j%4,15-(j/4));
						if (j<main.size()) setImage(main.get(j).getFaction().getFactionImageView(),j%4,15-(j/4));
					}
				}
				else {
					for(int j=0;j<8;j++) {
						clearImage(j%4,15-(j/4));
						if (j<main.size()) setImage(main.get(j).getImageView(),j%4,15-(j/4));
					}
				}
			}
			else if (main.get(0).getFaction().getNom()=="rouge") {
				if (hidden) {
					for(int j=0;j<8;j++) {
						clearImage(15-(j%4),j/4);
						if (j<main.size()) setImage(main.get(j).getFaction().getFactionImageView(),15-(j%4),j/4);
					}
				}
				else {
					for(int j=0;j<8;j++) {
						clearImage(15-(j%4),j/4);
						if (j<main.size()) setImage(main.get(j).getImageView(),15-(j%4),j/4);
					}
				}
			}
		}
		else {
			if (main==board.getFaction1().getMain()) clearImage(0,15);
			else clearImage(15,0);
		}
			
	}
	
	//Affiche la grille après avoir calculer start_x et start_y pour la centrer et avoir effacer les cartes précédement présentes
	public void showGrid(PickedCard[][] grid) {
		int min_x = 0; 
		int max_x = 0;
		int min_y = 0;
		int max_y = 0;
		
		for(int i=2; i<14;i++) {
			for(int j=0; j<16;j++) {
				clearImage(j,i);
			}
		}
		
		for(int i=0; i<2;i++) {
			for(int j=4; j<12;j++) {
				clearImage(j,i);
			}
		}
		
		for(int i=14; i<16;i++) {
			for(int j=4; j<12;j++) {
				clearImage(j,i);
			}
		}
		
		outerloop: for(int i=0; i<32;i++) {
			for(int j=0; j<32;j++) {
				if ((grid[i][j]) != null) {
					min_x = i;
					break outerloop;
				}
			}
		}
		outerloop: for(int i=31; i>0;i--) {
			for(int j=0; j<32;j++) {
				if ((grid[i][j]) != null) {
					max_x = i;
					break outerloop;
				}
			}
		}

		outerloop: for(int i=0; i<32;i++) {
			for(int j=0; j<32;j++) {
				if ((grid[j][i]) != null) {
					min_y = i;
					break outerloop;
				}
			}
		}
		outerloop: for(int i=31; i>0;i--) {
			for(int j=0; j<32;j++) {
				if ((grid[j][i]) != null) {
					max_y = i;
					break outerloop;
				}
			}
		}
		
		int center_x = (max_x-min_x)/2+Math.min(max_x, min_x);
		int center_y = (max_y-min_y)/2+Math.min(max_y, min_y);
		
		start_x = center_x-7;
		start_y = center_y-7;
		
		for(int i=0; i<32;i++) {
			for(int j=0; j<32;j++) {
				if (grid[i][j] != null && !grid[i][j].isReturned()) {
					setImage(grid[i][j].getFaction().getFactionImageView(),i-start_x,j-start_y);
				}
				else if (grid[i][j] != null) {
					setImage(grid[i][j].getImageView(),i-start_x,j-start_y);
				}
			}
		}
	}
	
	//Affiche tout le plateau en prenant une faction en paramètre
	//Note : si les 2 mains sont vides, on peut appeler showBoard(null) sans problème
	public void showBoard(Faction f) {
		if (f == board.getFaction1()) {
			showHand(board.getFaction1().getMain(), false);
			showHand(board.getFaction2().getMain(), true);
			card_visibility = board.getFaction1();
		}
		else {
			showHand(board.getFaction1().getMain(), true);
			showHand(board.getFaction2().getMain(), false);
			card_visibility = board.getFaction2();
		}
		showGrid(board.getGrille());
		setScores(board.getFaction2().getScore(), board.getFaction1().getScore());
	}
	
	//Enlève une image de la grille 16*16 si elle existe
	public void clearImage(int x, int y) {
		ObservableList<Node> childrens = gridpane.getChildren();
		for(Node node : childrens) {
			if (node instanceof ImageView && GridPane.getRowIndex(node)==y && GridPane.getColumnIndex(node)==x) {
				gridpane.getChildren().remove(node);
				break;
			}
		} 
	}
	

}
