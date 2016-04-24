package model;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import model.cards.CardRectangle;
import model.cards.GhostCard;
public class PlayField extends Pane{
	
	public PlayField(){
		super();
	}

	public void add(Node node){
		this.getChildren().add(node);
	}
	
	public void remove(Node node){
		this.getChildren().remove(node);
	}

	public void moveCardToFront(CardRectangle card) {
		int index = getChildren().indexOf(card);
		getChildren().remove(card);
		while(!(getChildren().get(index) instanceof GhostCard)){
			index++;
		}
		getChildren().add(index, card);
	}

	
}
