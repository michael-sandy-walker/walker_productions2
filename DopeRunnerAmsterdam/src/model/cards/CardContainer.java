package model.cards;

import java.util.ArrayList;
import java.util.List;

public class CardContainer {
	protected List<Card> cards = new ArrayList<Card>();
	protected double X;
	protected double Y;
	protected double orientation;
	private final boolean isFrontUp; 
	public CardContainer(double x, double y, double orientation, boolean isFrontUp) {
		super();
		X = x;
		Y = y;
		this.orientation = orientation;
		this.isFrontUp = isFrontUp;
	}
	public void addCard(Card card){
		card.setCardContainer(this);
		cards.add(card);
	}
	
	public void removeCard(Card card){
		cards.remove(card);
	}
	
	public List<Card> getCards() {
		return cards;
	}
	
	public List<Card> getCards(boolean isRegular) {
		List<Card> toChooseFrom = new ArrayList<Card>();
		for (Card card:cards){
			if(card.isRegular() == isRegular){
				toChooseFrom.add(card);
			}
		}
		return toChooseFrom;
	}
	
	public void setCards(List<Card> cards) {
		this.cards = cards;
	}
	public Card getCard(int index) {
		return cards.get(index);
	}
	
	public double getX() {
		return X;
	}
	
	public double getY() {
		return Y;
	}
	
	public double getOrientation() {
		return orientation;
	}
	
	public boolean isEmpty(){
		return cards.size()==0;
	}
	public boolean isFrontUp() {
		return isFrontUp;
	}
}
