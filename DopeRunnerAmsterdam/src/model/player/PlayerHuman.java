package model.player;

import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import model.Arrow;
import model.cards.Card;
import model.cards.Deck;
import model.gamepiece.Goal;
import model.tramstop.Boatstop;
import model.tramstop.Tramstop;
import network.ChatMessage;

public class PlayerHuman extends Player{
	private Button btn_endTurn;
	
	public PlayerHuman(Color color, String name, double positionX, double positionY, double width, double height, double orientation){
		super(color, name, positionX, positionY, width, height, orientation);
//		Tramstop.addListener(this);
		if(this == controllingPlayer){
			btn_endTurn = new Button("End Turn");
			btn_endTurn.setMouseTransparent(true);
			btn_endTurn.setTranslateX(520);
			btn_endTurn.setTranslateY(650);
			btn_endTurn.setOnAction(event -> endHumanTurn());
			playField.add(btn_endTurn);
		}
		System.out.println(name);
	}
	
	@Override
	public void throwDie(){
		setHasTraveled(false);
		System.out.println("throwDie");
		Card.setClickReason(Card.THROWDIE);
		Player.getDie().activateDie();
	}

	@Override
	public void makeNextMove(){
		System.out.println("makeNextMove");
		if(this == controllingPlayer){
			activateEndTurn();
		}
	}
	private void oneCardSelected(Card selectedCard) {
		System.out.println("One Card");
		List<Player> players = Player.getPlayers();
    	Tramstop tramstop = getTramstop();
    	boolean isRegular = selectedCard.isRegular();
        String cardName = selectedCard.getName();
        // TODO instead of going through the List<Player> in controller we can use nextPlayer
        if ((tramstop instanceof Boatstop) && (tramstop.getGoal()!=null)){
        	Goal goal = (Goal) (tramstop.getGoal());
        	if(goal.getName().contains(cardName)){
        		tramstop.activate();
        	}
        }
    	switch (cardName){
    	case "PurpleHaze": // place or remove a gangster 
    		List<Tramstop> tramstopsToChooseFrom = tramstopsToChooseFromPolice();
    		activateTramstops(tramstopsToChooseFrom);
    		break;
    		
    	case "OrangeBud": // flip a goal
    		List<Boatstop> boatstops = boatstopsToChooseFromFlip();
			activateTramstops(boatstops);
    		break;
    		
    	case "Top44": // steal a regular card
    		Card.setClickReason(Card.STEALCARD);
    		for(int i=0;i<Player.getNumberOfPlayers();i++){
    			Player playerToStealFrom = players.get(i);
    			if(!(playerToStealFrom == getActivePlayer())){
        			for(Card card : playerToStealFrom.getCards(true)){
    					card.animateCard();
        			}
    			}
    		}
    		break;
    		
    	case "Afghan": // steal two regular cards
    		if(!(Card.getClickReason() == Card.STEALCARDS)){
	    		Card.setClickReason(Card.SHOWCARDS);
	    		for(int i=0;i<Player.getNumberOfPlayers();i++){
	    			Player playerToStealFrom = players.get(i);
	    			if(!(playerToStealFrom == getActivePlayer())){
	        			for(Card card : playerToStealFrom.getCards(true)){
        					card.animateCard();
	        			}
	    			}
	    		}
    		}
    		// when player chooses someone to steal from, program goes to void showCards
			// when player chooses two cards, program goes to void threeCardsSelected
    		// arrow shows and if arrow is clicked program goes to  void stealCards
   		break;
    		
    	case "Hiya": // steal an advanced card
    		Card.setClickReason(Card.STEALCARD);
    		for(int i=0;i<Player.getNumberOfPlayers();i++){
    			Player playerToStealFrom = players.get(i);
    			if(!(players.get(i) == getActivePlayer())){
        			for(Card card : playerToStealFrom.getCards(false)){
    					card.animateCard();
        			}
    			}
    		}
    		break;
    		
    	case "Shiva": // be invisible
    		getActivePlayer().activateArrow("shiva");
    		break;
		default: // tramcard, move your pawn
	    	int subType = selectedCard.getSubType();
        	if(!getActivePlayer().hasTraveled()){
	        	List<Tramstop> connections = tramstop.getConnections(subType, isRegular);
//    	        	System.out.println(connections.toString());
	        	activateTramstops(connections);
	        	Tramstop.setClickReason("movePawn");
        	}
			
			break;
    	}
	}

	@Override
	public void throwAwayHalfYourCards() {
		System.out.println("Human");
		needsToThrowAway = true;
		System.out.println(getName());
		for (Card card:getCards()){
			card.setStrokeColor(Card.getThrowAwayColor());
		}
	}
	
	private void deactivateTramstops(){
		for(Tramstop tramstop : Tramstop.getTramstops()){
			tramstop.deactivate();
		}
		Tramstop.setClickReason("");
	}
	
	private void stopAnimatingAllCards(){
		for(Player player : Player.getPlayers()){
			player.stopAnimatingCards();
		}
		Card.setClickReason(Card.SELECT);
	}
	
	private <Trmstp> void activateTramstops(List<Trmstp> tramstops){
		for(Trmstp tramstop:tramstops){
			((Tramstop) tramstop).activate();
		}
	}
	
	protected void endHumanTurn(){
//		Player player = activePlayer;
//		Tramstop tramstop = getTramstop();
		// choose whether you want two regular cards or one advanced card
		if(!getActivePlayer().hasDoneAnything() && !getTramstop().hasPolice()&& !(getTramstop().hasOwner() && getTramstop().getOwner() != this)){
			Player.getDeck(true).activateArrow("chooseArrow");
			Player.getDeck(false).activateArrow("chooseArrow");
			deactivateEndTurn();
			return;
		}
		deactivateEndTurn();
		stopAnimatingAllCards();
		deactivateArrows();
		deactivateTramstops();
		resetSelectedCards();
//		activePlayer.flipCards(false);
		endTurn();
	}
	
	public void tramstopSelected(Tramstop selectedTramstop, String clickReason){
		Card.setClickReason(Card.SELECT);
		System.out.println("Tramstop selected");
		// see if the player has made a deal
		if(getTramstop() == selectedTramstop && (selectedTramstop instanceof Boatstop) && !getSelectedCards().isEmpty()){
			if(selectedTramstop.hasGoal()){
				Goal goal = (Goal) (((Boatstop) selectedTramstop).getGoal());
				if(goal.isFrontUp()){
					Card card = getSelectedCards().get(0);
					if(goal.getName().contains(card.getName())){
						makeDeal(selectedTramstop);
						discardCard(card);
						resetSelectedCards();
						stopAnimatingCards();
						deactivateTramstops();
						deactivateArrows();
						return;
					}
				}
			}
		}
		if(clickReason.equals("movePawn")){
			Card selectedCard = getSelectedCards().get(0);
			resetSelectedCards();
			getActivePlayer().setHasTraveled(true);
			movePawnTo(getActivePlayer(),selectedTramstop);
			discardCard(selectedCard);
		} 
		else if(clickReason.equals("choosePolice")){
			// moves the selected police to a free base outside the play area
			if(selectedTramstop.hasPolice()){
				removePolice(selectedTramstop);
			}
			// moves the police to the selected tramstop 
			else{
				placePolice(selectedTramstop);
			}
			if(!getSelectedCards().isEmpty()){
				discardCard(getSelectedCards().get(0));
				resetSelectedCards();
			}
		} 
//		else if(tramstopClickReason.equals("chooseGoal")){
//			List<Boatstop> boatstopsOutside = controller.getGoalBases();
//			for(Boatstop boatstop : boatstopsOutside){
//				boatstop.deactivate();
//			}
//			Goal goal = ((Boatstop) selectedTramstop).getGoal();
//			movePieceTo(goal, Boat.getBoat().getTramstop());
//			Tramstop.setClickReason("");
//		}
		else if(clickReason.equals("flipGoal")){
			if(activePlayer==controllingPlayer)
				networkController.sendMessage(new ChatMessage(ChatMessage.FLIPGOAL,0,0,selectedTramstop.getTramstopID()));
			((Goal) (((Boatstop) selectedTramstop).getGoal())).flipGoal();
			if(!getSelectedCards().isEmpty()){
				discardCard(getSelectedCards().get(0));
				resetSelectedCards();
			}
		}
		deactivateTramstops();
	}
//	@Override
//	protected void doSomething(){
//		
//	}
	@Override
	protected void chooseGangster(List<Tramstop> tramstops){
		activateTramstops(tramstops);
		Card.setClickReason(Card.THROWDIE);
	}
//	@Override
//	public void dieThrown(){
//		if(this == Player.controllingPlayer)
//			Player.getNetworkController().sendMessage(new ChatMessage(ChatMessage.DIETHROWN));
//		String dieResult = die.getNextResult();
//		handleDie(dieResult);
//	}
	@Override
	public void selectionChanged(int clickReason) {
		List<Card> selectedCards = getSelectedCards();
		if(clickReason == Card.THROWAWAY){
			if(selectedCards.size() == getCards().size()/2){
				if(this == controllingPlayer)
					activateArrow("throwAway");
			}
			else{
				if(this == controllingPlayer)
					deactivateArrow();
			}
			return;
		}
		if(Player.getActivePlayer()!=Player.getControllingPlayer())
			return;
//		if(clickReason == null){
//			networkController.sendMessage("cardSelected");
//		} else{
//		}
		if(Arrow.getClickReason().equals("chooseArrow")){
			deactivateArrows();
		}
//		System.out.println(name);
//		System.out.println(needsToThrowAway);
		if(this != activePlayer){
			if(selectedCards.size() == 2){
				//TODO verder gaan
				activateArrow("stealCards");
			} else{
				deactivateArrow();
			}
		}
		else if(selectedCards.size()==3){
			System.out.println("threeCardsSelected");
			Deck advancedDeck = Player.getDeck(false);
			if(getTramstop().getType().equals("coffeeshop")){
//				Card.setClickReason("buyHash");
				advancedDeck.activateArrow("buyHash");
			}
		} else{
			if(selectedCards.size()==1 && clickReason != Card.AFGHAN){
				oneCardSelected(selectedCards.get(0));
			} else if(selectedCards.size() == 0 || selectedCards.size() == 2 || selectedCards.size() == 4){
				deactivateTramstops();
				stopAnimatingCards();
				deactivateArrows();
				// TODO het zou wel kloppen maar ik denk dat de clickreason "" gemaakt moet worden
			}
		} 
	}
	@Override
	public void deckArrowClicked(Deck deck, String clickReason) {
		System.out.println(clickReason);
		if(clickReason.equals("buyHash")){
			buyHash(getSelectedCards());
		} else if (clickReason.equals("chooseArrow")){
//			System.out.println("HAYEE");
			Player player = getActivePlayer();
			player.setHasDoneAnything(true);
			dealCardTo(player, deck.isRegular());
			if(deck.isRegular()){
				dealCardTo(getActivePlayer(), deck.isRegular());
			}
			for(int i = 0; i < player.getNumberOfCardsToGet("PurpleHazeOrangeBudTop44"); i++){
				dealCardTo(getActivePlayer(), true);
			}
			for(int i = 0; i < player.getNumberOfCardsToGet("HiyaAfghanShiva"); i++){
				dealCardTo(getActivePlayer(), false);
			}
			Player.getDeck(!deck.isRegular()).deactivateArrow();
			Card.setClickReason(Card.SELECT);
			endHumanTurn();
		}
	}
	
	private void deactivateArrows(){
		for(Deck deck:Player.getDecks()){
			deck.deactivateArrow();
		}
		for (Player player:Player.getPlayers()){
			player.deactivateArrow();
		}
		Arrow.setClickReason("");
	}
	public void activateEndTurn(){
		btn_endTurn.setMouseTransparent(false);
	}
	
	public void deactivateEndTurn(){
		btn_endTurn.setMouseTransparent(true);
	}

}
