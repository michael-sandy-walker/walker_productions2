package model.player;

import java.util.List;
import java.util.Random;

import javafx.scene.paint.Color;
import model.Arrow;
import model.cards.Card;
import model.tramstop.Tramstop;

public class PlayerComputer extends Player{
	private Random random = new Random();
	public PlayerComputer(Color color, String name, double positionX, double positionY, double width, double height, double orientation){
		super(color, name, positionX, positionY, width, height, orientation);
	}
	@Override 
	public void makeNextMove(){
		System.out.println("MakeNext");
		dealCardTo(this,false);
		endTurn();
	}
	@Override 
	public void throwDie(){
		activePlayer = this;
		handleDie();
	}
	@Override
	protected void chooseGangster(List<Tramstop> tramstopsToChooseFrom){
		// TODO make a better choice
		Tramstop chosenTramstop = tramstopsToChooseFrom.get(random.nextInt(tramstopsToChooseFrom.size()));
		if(chosenTramstop.hasPolice()){
			removePolice(chosenTramstop);
		} else{
			placePolice(chosenTramstop);
		}
		Tramstop.setClickReason("");
	}
	@Override
	protected void chooseCard(){
		Card chosenCard = cardsToChooseFrom.get(random.nextInt(cardsToChooseFrom.size()));
		System.out.println("Computer chooses: "+chosenCard.getName());
		cardChosen(chosenCard);
	}
	@Override 
	protected void throwAwayHalfYourCards(){
		System.out.println("WTF");
//		if(getCards().size()<=6) return;
		int numCardsToThrow =getCards().size() / 2; 
		for(int i = 0; i < numCardsToThrow; i++){
			discardCard(getCards().get(random.nextInt(getCards().size())));
		}
		
	}
	@Override
	public void selectionChanged(int clickReason) {
		if(getSelectedCards().size() == 2){
			activateArrow("stealCards");
		} else{
			deactivateArrow();
		}

	}

//	@Override
//	protected void doSomething(){
//		String clickReason = "";
////		System.out.println(controller.getDice().get(0).isRolling());
////		if(!Card.getClickReason().equals("")){
//			clickReason = Card.getClickReason();
////		} else if(!Tramstop.getClickReason().equals("")){
////			clickReason = Tramstop.getClickReason();
////		} else if(!Arrow.getClickReason().equals("")){
////			clickReason = Arrow.getClickReason();
////		}
//		switch(clickReason)
//		{
//		case "chooseCard":
//			
////			List<Card> toChooseFrom = getCards().subList(getCards().size() - 4, getCards().size());
////			Card chosenCard = toChooseFrom.get(random.nextInt(toChooseFrom.size()));
//			break;
//		case "throwAway":
////			List<Card> toThrowAway = new ArrayList<Card>();
//			//TODO niet netjes
//			break;
//		case "":
//			break;
//		default :
//			System.out.println(clickReason);
//			break;
//		}
//		
//	}
}
