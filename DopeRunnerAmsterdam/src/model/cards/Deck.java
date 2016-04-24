package model.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.transform.Rotate;
import model.Arrow;
import model.player.Player;

public class Deck extends Group{
	private CardContainer drawStack;
	private CardContainer discardStack;
	private Arrow arrow;
	private boolean isRegular;
	public Deck(double drawStackX, double drawStackY, double discardStackX, double discardStackY, boolean isRegular) {
		this.isRegular = isRegular;
		drawStack = new CardContainer(drawStackX, drawStackY, 0, false);
		discardStack = new CardContainer(discardStackX, discardStackY, 0, true);
		String cardBack;
		if(isRegular){
			cardBack = "/cards/img/WeedBack.png";
		} else{
			cardBack = "/cards/img/HashBack.png";
		}
		Image cardBackImage = new Image(cardBack);				
		ImagePattern imageBack = new ImagePattern(cardBackImage);
		Rectangle empty = new Rectangle(60,90);
		empty.setFill(imageBack);
		empty.setArcHeight(10);
		empty.setArcWidth(10);
		empty.setStroke(Color.BLACK);
		empty.setStrokeType(StrokeType.OUTSIDE);
		empty.setStrokeWidth(5.0);
		empty.setOpacity(0.5);
		empty.setMouseTransparent(true);
		empty.setTranslateY(+5);
		arrow = new Arrow();
		arrow.setTranslateX(30);
		getChildren().addAll(empty,arrow);
		getTransforms().add(new Rotate(180,30,45));
		setTranslateX(drawStackX);
		setTranslateY(drawStackY+5);
	}
	
	public void addCardToDiscardStack(Card card){
		discardStack.addCard(card);
		card.moveCardTo(discardStack.getX(), discardStack.getY(), 0);
	}
	
	public void addCardToDrawStack(Card card){
		if (card.getPreviousCardContainer() == null || card.getPreviousCardContainer().isEmpty())
			card.setPreviousCardContainer(drawStack);
		drawStack.addCard(card);
	}
	
	public Card getNextCard(){
		Card card = null;
		System.out.println("kaarten over "+drawStack.getCards().size());
		if(!drawStack.isEmpty()){
			card = drawStack.getCard(0);
		}
		else{
			Collections.shuffle(discardStack.getCards(),Player.getRandom());
			System.out.println("shuffle "+discardStack.getCards().size());
			for(Card cardToMove:discardStack.getCards()){
//				cardToMove.flipCard(false);
				addCardToDrawStack(cardToMove);
				cardToMove.moveCardTo(drawStack.getX(), drawStack.getY(), 0);
			}
			discardStack.setCards(new ArrayList<Card>());
			card = drawStack.getCard(0);
			if(card == null){
				System.out.println("No cards to deal");
			}
		}
		if(card!=null){
			drawStack.removeCard(card);
			if(!drawStack.isEmpty()){
				drawStack.getCard(0).setVisible(true);
			}
		}
		return card;
	}
	
	public void shuffleDrawStack(){
		Collections.shuffle(drawStack.getCards(), Player.getRandom());
		drawStack.getCard(0).setVisible(true);;
	}
	
	public Arrow getArrow(){
		return arrow;
	}

	public double getDrawStackX(){
		return drawStack.getX();
	}
	
	public void activateArrow(String reason){
		arrow.activateArrow(reason);
	}
	
	public void deactivateArrow(){
		arrow.deactivateArrow();
	}
	
	public double getDrawStackY(){
		return drawStack.getY();
	}
//	public ArrayList<Card> getDiscardStack() {
//		return discardStack;
//	}

	public boolean isRegular() {
		return isRegular;
	}

		
		
	
}
