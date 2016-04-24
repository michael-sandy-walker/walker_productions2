package model.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.geometry.Point2D;
import model.PlayerOrientationConverter;
import model.cards.Card;
import model.cards.CardContainer;

public class PlayerCards extends CardContainer implements PlayerOrientationConverter{
	private ArrayList<Integer> offCenterX = new ArrayList<Integer>();
	private ArrayList<Integer> offCenterY = new ArrayList<Integer>();
	private ArrayList<Integer> rotation = new ArrayList<Integer>();
	private double width;
	private double height;
	private final Player player;
	private List<Card> selectedCards = new ArrayList<Card>();
	PlayerCards(Player player, double x, double y, double width, double height, double orientation, boolean isFrontUp) {
		super(x,y,orientation, isFrontUp);
		this.player = player;
		this.width = width;
		this.height = height;
	}
	
	@ Override
	public void addCard(Card card){
		cards.add(card);
		card.setCardContainer(this);
		boolean frontUp = (getPlayer() == Player.getControllingPlayer());
		relocateCards(frontUp); // also moves the card to the player
	}
	
	@ Override
	public void removeCard(Card card){
		cards.remove(card);
		selectedCards.remove(card);
		relocateCards(getPlayer() == Player.getControllingPlayer());
	}
	
	public void resetCardAnimation(){
		for(Card card : cards){
			card.resetAnimation();
		}
	}
	
	public Card stealCard(boolean regular){
		Random random = new Random();
		ArrayList<Card> availableCards = new ArrayList<Card>();
		for(Card card : cards){
			if(card.isRegular()==regular){
				availableCards.add(card);
			}
		}
		if(availableCards.isEmpty()){
			return null;
		}
		else{
			return availableCards.get(random.nextInt(availableCards.size()));
		}
	}
	
//	public void activateCards(){
//		for(Card card: cards){
//			card.activate();
////			card.setMouseTransparent(false);
//		}
//	}
	
//	public void deactivateCards(){
//		for(Card card: cards){
//			card.deactivate();
//			card.getGhostCard().setStroke(Card.getDefaultcolor());
////			card.setMouseTransparent(true);
//		}
//	}
	//TODO hoeft niet meer zo vaak
	protected void resetSelectedCards(){
		selectedCards = new ArrayList<Card>();
	}
	
	public void stopAnimatingCards(){
		for(Card card: cards){
			card.resetAnimation();
		}
	}

	public void flipCards(boolean frontUp){
		for(Card card: cards){
			System.out.println("kaartje");
			card.flipCard(frontUp);
		}
	}
	
	public void relocateCards(boolean frontUp){
		int numberOfCards = cards.size();
		if(numberOfCards == 0) return;
		double cardWidth = cards.get(0).getWidth();
		double cardHeight = cards.get(0).getHeight();
		int radius = 100;
		double angle = 2 * Math.PI*orientation / 360;
		double anchorPointX = width / 2 - Math.cos(angle)*cardWidth/2 - Math.sin(angle)*cardHeight/2;
		double anchorPointY = height + radius/2 - Math.cos(angle)*cardHeight/2 + Math.sin(angle)*cardWidth/2;;
		double placeOnCircleX = 0;
		double placeOnCircleY = 0;
		double rotation;
		int i = 0;
		for(Card card:cards){
			double angleCard = 2 * Math.PI * (i+1) / 3;
			placeOnCircleX = radius * Math.sin(-Math.PI / 3 + (angleCard) / (numberOfCards+1));
			placeOnCircleY = - radius * Math.cos(-Math.PI / 3 + (angleCard) / (numberOfCards+1));
			rotation = 120*(i+1)/(numberOfCards+1);
			Point2D offcenter = convertToPlayerOrientation(anchorPointX+placeOnCircleX, anchorPointY+placeOnCircleY, orientation);
			card.setRotation(-60+rotation+orientation);
			i++;
			card.moveCardTo(getX() + offcenter.getX(), getY() + offcenter.getY(), frontUp, card.getRotation());
		}
	}
	public Integer getOffCenterX(Card card) {
		return offCenterX.get(cards.indexOf(card));
	}
	public Integer getOffCenterY(Card card) {
		return offCenterY.get(cards.indexOf(card));
	}
	public Integer getRotation(Card card) {
		return rotation.get(cards.indexOf(card));
	}

	public List<Card> getSelectedCards() {
		return selectedCards;
	}

	public void selectCard(Card card) {
		selectedCards.add(card);
	}

	public void deselectCard(Card card) {
		selectedCards.remove(card);
	}

	public Player getPlayer() {
		return player;
	}

}
