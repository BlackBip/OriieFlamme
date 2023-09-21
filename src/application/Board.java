package application;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

/*Objet représentant le plateau de jeu, on va avoir deux factions qui vont jouer entre eux,
* la grille qui sera le plateau, ainsi que des ArrayLists qui ne seront pas accessibles et utile pour les joueurs*/
public class Board {

    private Faction faction1;
    private Faction faction2;
    private PickedCard[][] grille;
    private ArrayList<PickedCard> returnedCard = new ArrayList<>();
    private ArrayList<Integer> returnedCard_cord_x = new ArrayList<>();
    private ArrayList<Integer> returnedCard_cord_y = new ArrayList<>();
    
    public Board(String nom1,String nom2){
        faction1 = new Faction(nom1);
        faction2 = new Faction(nom2);
        grille = new PickedCard[32][32];
    }
    
    public Board () {
    	this("vert","rouge");
    }
    public Faction getFaction1() {
        return faction1;
    }

    public void setFaction1(Faction faction1) {
        this.faction1 = faction1;
    }

    public Faction getFaction2() {
        return faction2;
    }

    public void setFaction2(Faction faction2) {
        this.faction2 = faction2;
    }

    public PickedCard[][] getGrille() {
        return grille;
    }

    /*On réinitialise les différents tableaux pour une nouvelle manche*/
    public void reset() {
        grille = new PickedCard[32][32];
        returnedCard = new ArrayList<>();
        returnedCard_cord_x = new ArrayList<>();
        returnedCard_cord_y = new ArrayList<>();
    }

    /*On cherche la prochaine carte à retourner*/
    public Boolean activateCard() {
        for (int i = 0; i < 32; i++) {
            for (int j = 0; j < 32; j++) {
                if (grille[j][i] != null && !grille[j][i].isReturned()) {
                    effectCard(j,i);
                    return true;
                }
            }
        }
        return false;
    }

    /* Fonction qui va être utilisé pour activer l'effet d'une carte, pour toutes ces cartes, on peut avoir besoin à divers variables.
    * Je ne vais pas expliquer ce que font chaque carte, la description des effets sont trouvables sur le site du projet*/
    public void effectCard(int cord_y,int cord_x){
        PickedCard carte = grille[cord_y][cord_x];
        int cpt;
        boolean flag;
        Faction f = carte.getFaction();
        returnedCard.add(carte);
        returnedCard_cord_y.add(cord_y);
        returnedCard_cord_x.add(cord_x);
        carte.returnCard();
        Random r = new Random();
        int resultatrandom;
        switch(carte.getNom()){
            case "FISE":
                f.addPtsDDRS(1);
                break;
            case "FISA":
                if(returnedCard.size() % 2 == 0) f.addPtsDDRS(2);
                break;
            case "FC":
            	Iterator<PickedCard> it = returnedCard.iterator();
                while (it.hasNext()) {
                    if(it.next().getNom().equals("FC") && it.hasNext()) {
                        f.addPtsDDRS(4);
                        break;
                    }
                }
                break;
            case "EcologIIE":
                cpt=0;
                for (PickedCard cmp: returnedCard) {
                    if(cmp.getNom().equals("FISE") || cmp.getNom().equals("FISA") || cmp.getNom().equals("FC")) cpt++;
                }
                f.addPtsDDRS(cpt);
                break;
            case "lIIEns":
            	ArrayList<PickedCard> temp = new ArrayList<PickedCard>();
            	
                for (int i=returnedCard.size()-1;i>=0;i--) {
                    PickedCard cmp = returnedCard.get(i);
                    if(cmp.getNom().equals("FISE") || cmp.getNom().equals("FISA") || cmp.getNom().equals("FC")) {
                        removeCard(returnedCard_cord_y.get(i),returnedCard_cord_x.get(i));
                        cmp.returnCard();
                        temp.add(cmp);

                    }
                }
                if (temp.size()==0) return;
                Collections.shuffle(temp);
                int ip = 0;
                int xp = 0;
                int yp = 0;
                outlerloop : for (int i = 0 ; i < 32; i++) {
                	for (int j = 0 ; j < 32; j++) {
                		if (grille[j][i] != null) {
                			xp = j;
                			yp = i;
                			break outlerloop;
                		}
                	}
                }
                outlerloop : for (int i = yp ; i >= 0; i--) {
                	for (int j = (i==yp) ? xp-1 : 31; j >= 0; j--) {
                		if (grille[j][i]==null) {
                			grille[j][i]=temp.get(ip);
                			ip++;
                			if (ip==temp.size()) break outlerloop;
                		}
                	}
                }
                break;
            case "Soirée sans alcool":
                flag=false;
                for (PickedCard cmp: returnedCard) {
                    if(cmp.getNom().equals("Alcool")) flag=true;
                }
                if(!flag){
                    f.addPtsDDRS(5);
                }
                else{
                    for (int i = returnedCard.size()-1; i >=0 ; i--) {
                        if(returnedCard.get(i).getNom()=="FISE" || returnedCard.get(i).getNom()=="FISA" || returnedCard.get(i).getNom()=="FC"){
                           removeCard(returnedCard_cord_y.get(i), returnedCard_cord_x.get(i));
                        }
                    }
                    boolean tmp = false;
                    for (int i = 0; i < 32; i++) {
                    	for (int j = 0; j < 32; j++) {
    	                    if(grille[j][i]!=null){
    	                    	tmp = true;
    	                    	removeCard(j,i);
    	                    }
                    	}
                    if (tmp) break;
                    }
                    tmp = false;
                    for (int i = 31; i >= 0; i--) {
                    	for (int j = 0; j < 32; j++) {
    	                    if(grille[j][i]!=null){
    	                    	tmp = true;
    	                    	removeCard(j,i);
    	                    }
                    	}
                    if (tmp) break;
                    }
                }
                break;
            case "Alcool":
            	if (grille[cord_y+1][cord_x]!= null) removeCard(cord_y+1,cord_x);
            	if (grille[cord_y-1][cord_x]!= null) removeCard(cord_y-1,cord_x);
            	if (grille[cord_y][cord_x+1]!= null) removeCard(cord_y,cord_x+1);
            	if (grille[cord_y][cord_x-1]!= null) removeCard(cord_y,cord_x-1);
                break;
            case "Café":
                flag = false;
                for (int i = returnedCard.size()-1; i >=0 ; i--) {
                    if(returnedCard.get(i).getNom()=="Thé" || returnedCard.get(i).getNom()=="Alcool"){
                       removeCard(returnedCard_cord_y.get(i), returnedCard_cord_x.get(i));
                    }
                    if(returnedCard.get(i).getNom().equals("Ecocup")) flag=true;
                }
                if(flag) f.addPtsDDRS(+1);
                else f.addPtsDDRS(-1);
                break;
            case "Thé":
                flag = false;
                for (int i = returnedCard.size()-1; i >=0 ; i--) {
                    if(returnedCard.get(i).getNom()=="Café" || returnedCard.get(i).getNom()=="Alcool"){
                        removeCard(returnedCard_cord_y.get(i), returnedCard_cord_x.get(i));
                    }
                    if(returnedCard.get(i).getNom().equals("Ecocup")) flag=true;
                }
                if(flag) f.addPtsDDRS(1);
                else f.addPtsDDRS(-1);
                break;
            case "Reprographie":
                cpt=0;
                for (int i = 0; i < returnedCard.size(); i++) {
                    for (int j = i+1; j < returnedCard.size(); j++) {
                        if(returnedCard.get(i).getNom().equals(returnedCard.get(j).getNom())) cpt++;
                    }
                }
                if(f==faction1) faction2.addPtsDDRS(-cpt);
                else faction1.addPtsDDRS(-cpt);
                break;
            case "Isolation du bâtiment":
                int cptf1=0;
                int cptf2=0;
                for (int i = 0; i < 32; i++) {
                    for (int j = 0; j < 32; j++) {
                        if (grille[i][j] != null && !grille[i][j].isReturned()) {
                            if(grille[i][j].getFaction()==faction1) cptf1++;
                            else cptf2++;

                        }
                    }
                }
                faction1.addPtsDDRS(cptf1);
                faction2.addPtsDDRS(cptf2);
                break;
            case "Parcours sobriété numérique":
                for (int i = 0; i < 32; i++) {
                	for (int j = 0; j < 32; j++) {
	                    if(grille[j][i]!=null){
	                    	if (!grille[j][i].isReturned()) {
		                        returnedCard.add(grille[j][i]);
		                        returnedCard_cord_x.add(i);
		                        returnedCard_cord_y.add(j);
		                        grille[j][i].returnCard();
		                        break;
	                    	}
	                    	else {
	                    		break;
	                    	}
	                    }
                	}
                	for (int j = 31; j >= 0; j--) {
	                    if(grille[j][i]!=null){
	                    	if (!grille[j][i].isReturned()) {
		                        returnedCard.add(grille[j][i]);
		                        returnedCard_cord_x.add(i);
		                        returnedCard_cord_y.add(j);
		                        grille[j][i].returnCard();
		                        break;
	                    	}
	                    	else {
	                    		break;
	                    	}
	                    }
                	}
                }
                break;
            case "Heures supplémentaires":
                cpt = 0;
                for (PickedCard cmp: returnedCard) {
                    if(cmp.getNom().equals("Heures supplémentaires")) cpt++;
                }
                if(f==faction1) faction2.addPtsDDRS(-3*cpt);
                else faction1.addPtsDDRS(-3*cpt);
                break;
            case "Kahina Bouchama":
                ArrayList<Integer> temp_cord_x = new ArrayList<>();
                ArrayList<Integer> temp_cord_y = new ArrayList<>();
                for (int i = 0; i < 32; i++) {
                    for (int j = 0; j < 32; j++) {
                        if (grille[i][j] != null && !grille[i][j].isReturned()) {
                           temp_cord_x.add(j);
                           temp_cord_y.add(i);
                        }
                    }
                }
                if (temp_cord_x.size()==0) return;
                resultatrandom = r.nextInt(temp_cord_x.size());
                removeCard(temp_cord_y.get(resultatrandom),temp_cord_x.get(resultatrandom));
                break;
            case "Kevin Goilard":
            	ArrayList<Integer> ind_not_empty = new ArrayList<>();
                for (int j = 0; j < 32; j++) { 
	                for (int i = 0; i < 32; i++) {
	                    if(grille[i][j] != null){
	                    	ind_not_empty.add(j);
	                    	break;
	                    }
	                }
                }
                cpt=0;
                resultatrandom = r.nextInt(ind_not_empty.size());
                for (int i = 0; i < 32; i++) {
                    if(grille[i][ind_not_empty.get(resultatrandom)] != null){
                    	removeCard(i,ind_not_empty.get(resultatrandom));
                    	cpt++;
                    }
                }
                
                f.addPtsDDRS(2*cpt);

                break;
            case "Massinissa Merabet":
            	//Permet de vérifier si la faction de la carte précédente est différente
            	Boolean tmp = false;
            	//Recherche de la carte précédente est différente
                outerloop : for (int i = cord_x ; i >= 0; i--) {
                    for (int h = (i==cord_x) ? cord_y-1 : 31; h >= 0; h--) {
                    	if (grille[h][i] != null) {
                    		//Inversion de faction si nécessaire
                    		if (grille[h][i].getFaction() != f) {
                    			tmp = true;
                    			if (tmp && f==this.getFaction1()) grille[h][i].setFaction((this.getFaction1()));
                        		else if (tmp && f==this.getFaction2()) grille[h][i].setFaction((this.getFaction2()));
                    			
                    		}
                    		//Réactivement de la carte précédente
                    		effectCard(h,i);
                    		//Restauration de ses caractéristiques précédentes
                    		grille[h][i].returnCard();
                    		if (tmp && f==this.getFaction1()) grille[h][i].setFaction((this.getFaction2()));
                    		else if (tmp && f==this.getFaction2()) grille[h][i].setFaction((this.getFaction1()));
                    		//Suppression de cette activation fictive dans la liste des cartes retournées
                    		returnedCard.remove(returnedCard.size()-1);
                    		returnedCard_cord_y.remove(returnedCard_cord_y.size()-1);
                    		returnedCard_cord_x.remove(returnedCard_cord_x.size()-1);
                    		break outerloop;
                    	}
                    }
                }
                break;
            case "Vitéra Y":
                if(faction1.getPtsDDRS()> faction2.getPtsDDRS()) faction2.addPtsDDRS(3);
                else faction1.addPtsDDRS(3);
                break;
            case "Jonas Senizergues":
                for (int i=0;i<returnedCard.size();i++) {
                    if(returnedCard.get(i).getNom().equals("Heures supplémentaires")) {
                        removeCard(returnedCard_cord_y.get(i),returnedCard_cord_x.get(i));
                        i--;
                    }
                }
                break;
            case "Fetia Bannour":
                flag = false;
                cpt=0;
                for (int i=0;i<returnedCard.size();i++) {
                    if(returnedCard.get(i).getNom().equals("Heures supplémentaires")) {
                        flag=true;
                        for(int j=0;j<32;j++){
                            if(grille[j][cord_x]!=null){
                                removeCard(j,cord_x);
                            }
                            if(grille[cord_y][j]!=null){
                                removeCard(cord_y,j);
                            }
                        }
                        break;
                    }
                }
                if(!flag){
                    for (PickedCard cmp: returnedCard) {
                        if(cmp.getNom().equals("Catherine Dubois") || cmp.getNom().equals("Anne-Laure Ligozat") || cmp.getNom().equals("Guillaume Brunel") || cmp.getNom().equals("Christophe Mouilleron") || cmp.getNom().equals("Thomas Lim") || cmp.getNom().equals("Julien Forest") || cmp.getNom().equals("Dimitri Watel")) cpt++;
                    }
                    f.addPtsDDRS(cpt);
                }
                break;
            case "Catherine Dubois":
                for(int i=0;i<32;i++) {
                    if(grille[cord_y][i]!=null) {
                        removeCard(cord_y,i);
                        break;
                    }
                }
                for(int i=31;i>=0;i--) {
                    if(grille[cord_y][i]!=null) {
                        removeCard(cord_y,i);
                        break;
                    }
                }
                for(int i=0;i<32;i++) {
                    if(grille[i][cord_x]!=null) {
                        removeCard(i,cord_x);
                        break;
                    }
                }
                for(int i=31;i>=0;i--) {
                    if(grille[i][cord_x]!=null) {
                        removeCard(i,cord_x);
                        break;
                    }
                }
                break;
            case "Anne-Laure Ligozat":
                cpt=0;
                for (PickedCard cmp: returnedCard) {
                    if(cmp.getNom().equals("EcologIIE") || cmp.getNom().equals("Ecocup") || cmp.getNom().equals("Isolation du bâtiment") || cmp.getNom().equals("Parcours sobriété numérique")) cpt++;
                }
                f.addPtsDDRS(cpt*3);
                flag=false;
                for (int i = 31; i >= 0 ; i--) {
                    for (int j = 31; j >=0; j--) {
                        if (grille[i][j] != null && !grille[i][j].isReturned()) {
                            removeCard(i,j);
                            flag=true;
                            break;
                        }
                    }
                    if(flag) break;
                }
                break;
            case "Guillaume Burel":
                if(f==faction1){
                    if (faction1.getPtsDDRS()< faction2.getPtsDDRS()) {
                        faction1.addPtsDDRS(3);
                        faction2.addPtsDDRS(-3);
                    }
                }
                else{
                    if (faction1.getPtsDDRS()> faction2.getPtsDDRS()) {
                        faction1.addPtsDDRS(-3);
                        faction2.addPtsDDRS(3);
                    }
                }
                break;
            case "Christophe Mouilleron":
                flag=false;
                for (int i=0;i<returnedCard.size();i++) {
                    if(returnedCard.get(i).getNom().equals("Heures supplémentaires")) {
                        flag=true;
                        break;
                    }
                }
                if(flag){
                    for (int i=0;i<returnedCard.size();i++) {
                        if(!returnedCard.get(i).getNom().equals("Christophe Mouilleron") &&!returnedCard.get(i).getNom().equals("Heures supplémentaires")) {
                            removeCard(returnedCard_cord_y.get(i),returnedCard_cord_x.get(i));
                            i--;
                        }
                    }
                }
                break;
            case "Thomas Lim":
                flag = false;
                cpt=0;
                for (int i=0;i<returnedCard.size();i++) {
                    if(returnedCard.get(i).getNom().equals("Julien Forest")) flag=true;
                    if(returnedCard.get(i).getNom().equals("FISE")) cpt++;
                }
                if(flag) {
                    if(f==faction1){
                        faction2.addPtsDDRS(-cpt);
                    }
                    else{
                        faction1.addPtsDDRS(-cpt);
                    }
                }
                else f.addPtsDDRS(3*cpt);
                break;
            case "Julien Forest":
                flag = false;
                cpt=0;
                for (int i=0;i<returnedCard.size();i++) {
                    if(returnedCard.get(i).getNom().equals("Café")) flag=true;
                    if(returnedCard.get(i).getNom().equals("FISE")) cpt++;
                }
                if(flag) f.addPtsDDRS(cpt*6);
                break;
            case "Dimitri Watel":
                flag = false;
                cpt=0;
                for (int i=0;i<returnedCard.size();i++) {
                    if(returnedCard.get(i).getNom().equals("Thé")) flag=true;
                    if(returnedCard.get(i).getNom().equals("FISA") || returnedCard.get(i).getNom().equals("FC")) cpt++;
                }
                if(flag) f.addPtsDDRS(cpt*3);
                break;
            case "Djibril-Aurélien Dembele-Cabot":
                cpt=0;
                for(int i=0;i<returnedCard_cord_x.size();i++){
                    if(returnedCard_cord_x.get(i)==cord_x) cpt++;
                }
                if(cpt>=3) f.addPtsDDRS(5);
                break;
            case "Eric Lejeune":
            	ArrayList<PickedCard> temp2 = new ArrayList<PickedCard>();
            	
                ArrayList<Integer> indiceretenu = new ArrayList<>();
                if(returnedCard.size()<=6){
                    for (int i=0; i<returnedCard.size()-1;i++) {
                    	temp2.add(returnedCard.get(i));
                    	indiceretenu.add(i);
                    }
                }
                else{
                    while(indiceretenu.size()<5){
                        resultatrandom = r.nextInt(returnedCard.size()-1);
                        if(!indiceretenu.contains(resultatrandom)) indiceretenu.add(resultatrandom);
                    }
                    Collections.sort(indiceretenu);
                    for (int i = 0; i < 5; i++) {
                        temp2.add(returnedCard.get(indiceretenu.get(i)));
                    }
                }
                flag=false;
                for (int i = 0; i < temp2.size(); i++) {
                    if(temp2.get(i).getNom().equals("Catherine Dubois") || temp2.get(i).getNom().equals("Anne-Laure Ligozat") || temp2.get(i).getNom().equals("Guillaume Brunel") || temp2.get(i).getNom().equals("Christophe Mouilleron") || temp2.get(i).getNom().equals("Thomas Lim") || temp2.get(i).getNom().equals("Julien Forest") || temp2.get(i).getNom().equals("Dimitri Watel")) flag=true;
                }
                for (int i = indiceretenu.size()-1; i >=0 ; i--) {
                    removeCard(returnedCard_cord_y.get(indiceretenu.get(i)),returnedCard_cord_x.get(indiceretenu.get(i)));
                    temp2.get(i).returnCard();
                }
                if(flag) {
                    Collections.shuffle(temp2);
                    int ip2 = 0;
                    int xp2 = 0;
                    int yp2 = 0;
                    outlerloop : for (int i = 0 ; i < 32; i++) {
                    	for (int j = 0 ; j < 32; j++) {
                    		if (grille[j][i] != null) {
                    			xp2 = j;
                    			yp2 = i;
                    			break outlerloop;
                    		}
                    	}
                    }
                    outlerloop : for (int i = yp2 ; i >= 0; i--) {
                    	for (int j = (i==yp2) ? xp2-1 : 31; j >= 0; j--) {
                    		if (grille[j][i]==null) {
                    			grille[j][i]=temp2.get(ip2);
                    			ip2++;
                    			if (ip2==temp2.size()) break outlerloop;
                    		}
                    	}
                    }
                }
                break;
            case "Lucienne Pacavé":
                for (int i = 0; i < returnedCard_cord_x.size(); i++) {
                    if(returnedCard.get(i).getNom().equals("FISA") && (returnedCard_cord_x.get(i)==cord_x || returnedCard_cord_y.get(i)==cord_y)) {
                        f.addPtsDDRS(5);
                        break;
                    }
                }
                break;
            case "Katrin Salhab":
                boolean f1 = false;
                boolean f2 = false;
                boolean f3 = false;
                for (int i = 0; i < returnedCard.size(); i++) {
                    if(returnedCard.get(i).getNom().equals("Djibril-Aurélien Dembele-Cabot")) f1=true;
                    if(returnedCard.get(i).getNom().equals("Eric Lejeune")) f2=true;
                    if(returnedCard.get(i).getNom().equals("Lucienne Pacavé")) f3=true;
                }
                if(f1 && f2 && f3){
                    f.addPtsDDRS(10);
                }
                else{
                    for (int i = 0; i < 32; i++) {
                        if(grille[i][cord_x] != null && !grille[i][cord_x].isReturned()) grille[i][cord_x].returnCard();
                    }
                }
                break;
            case "Laurent Prével":
                for (int i = cord_x; i < 32; i++) {
                    for (int j= (i==cord_x) ? cord_y : 0; j < 32; j++) {
                    	if (grille[j][i] != null && !grille[j][i].isReturned()) {
                    	return;
                    	}
                    }
                }
                f.setPtsDDRS(10000);
                break;
            default:
                break;
        }
    }

    /*Procédure pour supprimer complètement une carte du jeu*/
    public void removeCard(int cord_y,int cord_x){
        if(cord_x>=0 && cord_x<=31 && cord_y>=0 && cord_y<=31){
            if(grille[cord_y][cord_x].isReturned()){
                returnedCard.remove(grille[cord_y][cord_x]);
                for (int i = 0; i < returnedCard_cord_x.size(); i++) {
                    if(returnedCard_cord_x.get(i)==cord_x && returnedCard_cord_y.get(i)==cord_y){
                        returnedCard_cord_y.remove(i);
                        returnedCard_cord_x.remove(i);
                        break;
                    }
                }
            }
            grille[cord_y][cord_x] = null;
        }
    }
}
