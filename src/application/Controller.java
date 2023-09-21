package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class Controller implements Initializable
{
	//Grille 16*16 pour afficher les différentes informations
	@FXML
	private GridPane gridpane;
	
	//Champs de textes divers
	@FXML
	private Text scores;
	
	@FXML
	private Text description;
	
	@FXML
	private Text instructions;
	
	//Objet permettant de gérer l'affichage
	protected GUI gui;
	//Plateau de la partie
	protected Board board;
	
	//2 objets permettant de gérer les évenenments et l'avancement de la partie (voir plus bas)
	protected String stage;
	protected int stage_i;
	
	//Permet de garder en mémoire la carte sélectionnée pendant la phase de placement
	protected PickedCard selected;
	
	//Permet de définir la faction qui commence la manche (0: vert, 1:rouge).
	//C'est un int et non une Faction pour permettre de raccourcir le code (voir plus bas)
	protected int factionStart;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//Début de la partie/initialisations diverses
		Board board = new Board();
		this.board = board;
		
		board.getFaction1().shuffle();
		board.getFaction1().pick();
		board.getFaction2().shuffle();
		board.getFaction2().pick();
		
		gui = new GUI(gridpane, description, scores, instructions, board);
		gui.showBoard(board.getFaction1());
		gui.setInstructions("Souhaitez-vous changer vos cartes ? (y/n)");
		//Choix aléatoire de la faction qui commence à poser ses cartes. Non utilisé pour la phase
		//de pioche car les pioches sont différentes donc ce n'est pas utile
		factionStart = (Math.random() <= 0.5) ? 0 : 1;
		
		//Phase de pioche
		stage = "melpio";
		stage_i = 0;

	}
	
	//Permet d'afficher les descriptions des cartes visibles
	@FXML
	public void onMoveMouseEvent(MouseEvent event)
	{
		ArrayList<Integer> coords = gui.getCoordinates(event.getX(), event.getY());
		gui.getDescription(coords);
	}
	
	//Permet de gérer les clics
	@FXML
	public void onMousePressedEvent(MouseEvent event)
	{
		//Phase de pose des cartes
		if (stage=="pose") {
			// Les deux permiers if gèrent la première carte, qui toujours posé au centre du plateau
			if (stage_i == -1) {
				ArrayList<Integer> coords = gui.getCoordinates(event.getX(), event.getY());
				if (coords.get(0)==-100 && coords.get(1)<board.getFaction1().getMain().size()) {
					selected = board.getFaction1().getMain().get(coords.get(1));
					board.getGrille()[15][15] = selected;
					board.getFaction1().getMain().remove(selected);
					gui.showBoard(board.getFaction2());
					stage_i = 2;
				}
			}
			else if (stage_i == -2) {
				ArrayList<Integer> coords = gui.getCoordinates(event.getX(), event.getY());
				if (coords.get(0)==-200 && coords.get(1)<board.getFaction2().getMain().size()) {
					selected = board.getFaction2().getMain().get(coords.get(1));
					board.getGrille()[15][15] = selected;
					board.getFaction2().getMain().remove(selected);
					gui.showBoard(board.getFaction1());
					stage_i = 0;
				}
			}
			//Choix de la carte de la faction verte
			else if (stage_i%4==0) {
				ArrayList<Integer> coords = gui.getCoordinates(event.getX(), event.getY());
				if (coords.get(0)==-100 && coords.get(1)<board.getFaction1().getMain().size()) {
					selected = board.getFaction1().getMain().get(coords.get(1));
					gui.setInstructions("Choisissez où placer "+selected.getNom());
					stage_i++;
				}
			}
			//Choix des coordonnées pour la carte de la faction verte
			else if (stage_i%4==1) {
				ArrayList<Integer> coords = gui.getCoordinates(event.getX(), event.getY());
				int i = coords.get(0);
				int j = coords.get(1);
				if (i>0 && j>0 && board.getGrille()[i][j]==null && (board.getGrille()[i+1][j]!=null || board.getGrille()[i-1][j]!=null || board.getGrille()[i][j+1]!=null || board.getGrille()[i][j-1]!=null)) {
					board.getGrille()[i][j] = selected;
					board.getFaction1().getMain().remove(selected);
					gui.showBoard(board.getFaction2());
					gui.setInstructions("Choisissez une carte à placer");
					stage_i++;
					//Fin du placement si la faction rouge a commencé : factionStart étant un int, on peut faire des calculs astucieux
					if (stage_i == 32-factionStart*2) {
						//Phase de retournement des cartes
						gui.setInstructionsDDRS();
						stage = "play";
						stage_i = 0;
					}
				}
			}
			//Choix de la carte de la faction rouge
			else if (stage_i%4 == 2) {
				ArrayList<Integer> coords = gui.getCoordinates(event.getX(), event.getY());
				if (coords.get(0)==-200 && coords.get(1)<board.getFaction2().getMain().size()) {
					selected = board.getFaction2().getMain().get(coords.get(1));
					gui.setInstructions("Choisissez où placer "+selected.getNom());
					stage_i++;
				}
			}
			//Choix des coordonnées pour la carte de la faction rouge
			else if (stage_i%4 == 3) {
				ArrayList<Integer> coords = gui.getCoordinates(event.getX(), event.getY());
				int i = coords.get(0);
				int j = coords.get(1);
				if (i>0 && j>0 && board.getGrille()[i][j]==null && (board.getGrille()[i+1][j]!=null || board.getGrille()[i-1][j]!=null || board.getGrille()[i][j+1]!=null || board.getGrille()[i][j-1]!=null)) {
					board.getGrille()[i][j] = selected;
					board.getFaction2().getMain().remove(selected);
					gui.showBoard(board.getFaction1());
					gui.setInstructions("Choisissez une carte à placer");
					stage_i++;
					//Fin du placement si la faction verte a commencé : factionStart étant un int, on peut faire des calculs astucieux
					if (stage_i == 32-factionStart*2) {
						//Phase de retournement des cartes
						gui.setInstructionsDDRS();
						stage = "play";
						stage_i = 0;
					}
				}
			}
		}
	}
	
	//Permet de gérer les touches du clavier
	@FXML
	public void handleKeyPressed(KeyEvent event)
	{
		//Phase de pioche
		if (stage=="melpio") {
			//Faction verte
			if (stage_i==0) {
				//Mélange de la pioche
				if (event.getCode().toString()=="Y") {
					board.getFaction1().handshuffle();
					gui.showBoard(board.getFaction2());
					stage_i++;
					//Permet d'éviter de demander à la faction rouge de piocher si elle a déjà mélangé sa pioche
					if (board.getFaction2().isInterpioche()) {
						gui.setInstructions("Choisissez une carte à placer");
						stage = "pose";
						stage_i = -1-factionStart;
						if (stage_i==-2) gui.showBoard(board.getFaction2());
						else gui.showBoard(board.getFaction1());
					}
				}
				//Pas de mélange de la pioche
				else if (event.getCode().toString()=="N") {
					gui.showBoard(board.getFaction2());
					stage_i++;
					//Permet d'éviter de demander à la faction rouge de piocher si elle a déjà mélangé sa pioche
					if (board.getFaction2().isInterpioche()) {
						gui.setInstructions("Choisissez une carte à placer");
						stage = "pose";
						stage_i = -1-factionStart;
						if (stage_i==-2) gui.showBoard(board.getFaction2());
						else gui.showBoard(board.getFaction1());
					}
				}
			}
			//Faction rouge
			else if (stage_i==1) {
				//Mélange de la pioche
				if (event.getCode().toString()=="Y") {
					board.getFaction2().handshuffle();
					//Phase de placement des cartes
					gui.setInstructions("Choisissez une carte à placer");
					stage = "pose";
					stage_i = -1-factionStart;
					if (stage_i==-2) gui.showBoard(board.getFaction2());
					else gui.showBoard(board.getFaction1());
				}
				//Pas de mélange de la pioche
				else if (event.getCode().toString()=="N") {
					//Phase de placement des cartes
					gui.showBoard(board.getFaction1());
					gui.setInstructions("Choisissez une carte à placer");
					stage = "pose";
					stage_i = -1-factionStart;
					if (stage_i==-2) gui.showBoard(board.getFaction2());
					else gui.showBoard(board.getFaction1());
				}
			}
		}
		//Phase de retournement des cartes
		if (stage=="play") {
			//Une carte est retourné à chaque fois qu'on appuie sur C
			if (event.getCode().toString()=="C") 
				if (board.activateCard()) {
					gui.showBoard(null); //les mains sont vides, on peut utliser showBoard(null)
					gui.setInstructionsDDRS();
				}
				else {
					//Gestion du score à la fin de la manche
					if (board.getFaction1().getPtsDDRS()>board.getFaction2().getPtsDDRS()) board.getFaction1().setScore(board.getFaction1().getScore()+1);
					else if (board.getFaction1().getPtsDDRS()<board.getFaction2().getPtsDDRS()) board.getFaction2().setScore(board.getFaction2().getScore()+1);
					//Cas d'égalité
					else {
				        outerloop : for (int i = 0; i < 32; i++) {
				            for (int j = 0; j < 32; j++) {
				                if (board.getGrille()[j][i] != null) {
				                	board.getGrille()[j][i].getFaction().setScore(board.getGrille()[j][i].getFaction().getScore()+1);
				                	break outerloop;
				                }
				            }
				        }
					}
					gui.showBoard(null);
					
					//Affichage du gagnant si la partie est terminée
					if (board.getFaction1().getScore()>=2) {
						gui.setInstructions("L'équipe verte a gagné!");
					}
					else if (board.getFaction2().getScore()>=2) {
						gui.setInstructions("L'équipe rouge a gagné!");
					}
					//Si suite de la partie
					else {
						//Deuxième tour : l'autre faction commence
						if (board.getFaction1().getScore()+board.getFaction2().getScore()==1) factionStart = (factionStart == 0) ? 1 : 0;
						//Troisième tour : une faction aléatoire commence
						else factionStart = (Math.random() <= 0.5) ? 0 : 1;
						//Pioche/reset du plateau
						board.getFaction1().pick();
						board.getFaction1().setPtsDDRS(0);
						board.getFaction2().pick();
						board.getFaction2().setPtsDDRS(0);
						board.reset();
						//Permet d'éviter de demander aux factions de repiocher si elles l'ont déjà fait
						if (!board.getFaction1().isInterpioche()) {
							gui.showBoard(board.getFaction1());
							gui.setInstructions("Souhaitez-vous changer vos cartes ? (y/n)");
							stage = "melpio";
							stage_i = 0;
						}
						else if (!board.getFaction2().isInterpioche()) {
							gui.showBoard(board.getFaction2());
							gui.setInstructions("Souhaitez-vous changer vos cartes ? (y/n)");
							stage = "melpio";
							stage_i = 1;
						}
						else {
							//Si pas de pioche possible, on retourne à la phase de placement
							gui.setInstructions("Choisissez une carte à placer");
							stage = "pose";
							stage_i = -1-factionStart;
							if (stage_i==-2) gui.showBoard(board.getFaction2());
							else gui.showBoard(board.getFaction1());
						}
					}
			}
		}
	}
}
