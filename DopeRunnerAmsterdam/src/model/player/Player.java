package model.player;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import network.ChatMessage;
import network.DopeMain;
//import gui.DopeGUI;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import model.tramstop.Boatstop;
import model.tramstop.GamePieceContainer;
import model.tramstop.Tramstop;
//import network.Client;
//import network.ClientGUI;
import model.Arrow;
import model.Die;
import model.PlayField;
import model.PlayerOrientationConverter;
import model.cards.Card;
import model.cards.Deck;
import model.gamepiece.Boat;
import model.gamepiece.GamePiece;
import model.gamepiece.Goal;
import javafx.util.Duration;
public class Player implements PlayerOrientationConverter{
	protected final PlayerRectangle playerRectangle;
	protected Pawn pawn;
//	private List<Gangster> gangsters = new ArrayList<Gangster>();
	protected String name;
	protected static Player activePlayer;
	protected static Player choosingPlayer;
	protected boolean hasTraveled = false;
	protected boolean hasDoneAnything = false;
	protected Player nextPlayer;
//	private int orientation;
	protected boolean needsToThrowAway = false;
	//TODO timer alla pokerstars with timebank
	protected final Color color;
	protected static int numberOfPlayers;
//	protected static Main controller;
	protected static PlayField playField;
	protected static int numberOfThrowAwayPlayers = 0;
	protected static List<Card> cardsToChooseFrom;
	protected int numberOfMovingCards;
	
	protected static DopeMain networkController;
	protected static Die die;
	protected static Button btn_endTurn;
//	protected static List<Tramstop> tramstops = new ArrayList<Tramstop>();
//	protected static List<Boatstop> boatstops = new ArrayList<Boatstop>();
	protected static List<Deck> decks = new ArrayList<Deck>();
	protected static GamePieceContainer policeBase;
	protected static GamePieceContainer hashBase;
	protected static List<GamePieceContainer> goalBases = new ArrayList<GamePieceContainer>();
	protected static Boat boat;
	protected static Duration animationSpeed;
	protected static List<Player> players = new ArrayList<Player>();
	private static Random random;
	private static int controllingPlayerId;
	protected static Player controllingPlayer;
	private final int playerID;
	private static boolean readyFlag = false;
	private static boolean busyFlag = false;
	private final double positionX;
	private final double positionY;
	private final double orientation;
	private static int activePlayerID;
	
	public Player (Color color, String name, double positionX, double positionY, double width, double height, double orientation){
		this.positionX = positionX;
		this.positionY = positionY;
		this.orientation = orientation;
		playerID = players.size();
		if(playerID == activePlayerID)
			setActivePlayer(this);
		if(playerID == controllingPlayerId)
			controllingPlayer=this;
		players.add(this);
		this.color = color;
//		Card.addThrowAwayListener(this);
		playerRectangle = new PlayerRectangle(this, color, positionX, positionY, width, height, orientation);
		pawn = new Pawn(color);
		pawn.setGamePieceBase(Tramstop.getTramstopStart());
		this.name = name;
	}
	
	public void getStartingCards(){
		dealCardTo(this,true);
		dealCardTo(this,true);
		dealCardTo(this,true);
	}
	
	protected void kickoutGangsters(Player player, Tramstop tramstop){
		if(tramstop.hasOwner() && tramstop.getOwner() != player){
			tramstop.getGangster().moveToBase();
		}
		if(tramstop.hasPolice()){
			tramstop.getPolice().moveToBase();
		}
	}
	
	public void movePawnTo(Player player, Tramstop tramstopNew){
		if(this == Player.controllingPlayer)
			networkController.sendMessage(new ChatMessage(ChatMessage.MOVEPAWN, 0,player.getPlayerID(),tramstopNew.getTramstopID()));

		if(player.getPawnScale() == 1.5){
			kickoutGangsters(player, tramstopNew);
		}
		player.getPawn().moveToContainer(tramstopNew);
//		player.setTramstop(tramstopNew);
	}
	
	protected void moveBoatTo(Boat boat, Boatstop boatstop){
		boat.moveTo(boatstop.getPlaceOnPlayfieldX()+boatstop.getOffsetX(), boatstop.getPlaceOnPlayfieldY()+boatstop.getOffsetY());
		boat.setContainer(boatstop);
		GamePiece goal = boatstop.getGoal();
		if(goal != null){
			((Goal) (goal)).flipGoal();
		}
		else{
			if(!boatstop.hasOwner())
				placeGoal(boatstop);
		}
	}
	
	public void placePolice(Tramstop tramstop){
		if(this == Player.controllingPlayer)
			networkController.sendMessage(new ChatMessage(ChatMessage.PLACEPOLICE, 0,0,tramstop.getTramstopID()));
		GamePiece pol=null;
		pol = policeBase.getPolice();
		pol.moveToContainer(tramstop);
	}
	
	public void removePolice(Tramstop tramstop){
		if(this == Player.controllingPlayer)
			networkController.sendMessage(new ChatMessage(ChatMessage.REMOVEPOLICE, 0,0,tramstop.getTramstopID()));
		GamePiece pol = tramstop.getPolice();
		pol.moveToBase();
	}
	
	public void placeGoal(Boatstop boatstop){
		boolean isWhite = boatstop.isWhite();
		List<GamePieceContainer> goalBases = Player.getGoalBases();
		GamePiece goal=null;
		List<GamePieceContainer> allowedGoalBases = new ArrayList<GamePieceContainer>();
		for(GamePieceContainer goalBase : goalBases){
			if(goalBase.getGoal()!=null && goalBase.getGoal().isWhite() == isWhite){
				allowedGoalBases.add(goalBase);
			}
		}
		GamePieceContainer chosenGoalBase = allowedGoalBases.get(getRandom().nextInt(allowedGoalBases.size()));
		goal = chosenGoalBase.getGoal();
		System.out.println(boatstop.getType());
		goal.moveToContainer(boatstop);
	}

	public void makeDeal(Tramstop tramstop){
		if(this == Player.controllingPlayer)
			networkController.sendMessage(new ChatMessage(ChatMessage.MAKEDEAL, 0,0,tramstop.getTramstopID()));
		tramstop.setDealMade(true);
		Goal goal = (Goal) (tramstop.getGoal());
		Player player = activePlayer;
		player.addGoal(goal);
//		player.moveGangsterTo(tramstop);
		tramstop.removeGamePiece(goal);
		player.setHasTraveled(true);
	}
	
	public Card dealCardTo(Player player, boolean isRegular){
		if(this == Player.controllingPlayer){
			if(isRegular)
				networkController.sendMessage(new ChatMessage(ChatMessage.DEALREGULARCARD, 0,player.getPlayerID(),0));
			else
				networkController.sendMessage(new ChatMessage(ChatMessage.DEALADVANCEDCARD, 0,player.getPlayerID(),0));
		}
		Deck deck = Player.getDeck(isRegular);
		Card cardToDeal = deck.getNextCard();
		if(player!=activePlayer)
			cardToDeal.getGhostCard().setStroke(Color.BLACK);
//		playField.remove(cardToDeal.getCard());
//		playField.add(cardToDeal.getCard());
		System.out.println("actual dealRegular "+player.getPlayerID());
		player.addCard(cardToDeal);
		return cardToDeal;
	}
	
	public void discardCard(Card card){
//		if(playField.getChildren().contains(card.getCard()))
//		playField.remove(card.getCard());
//		playField.add(card.getCard());
		if(activePlayer == controllingPlayer)
			networkController.sendMessage(new ChatMessage(ChatMessage.DISCARDCARD, card.getCardID(),0,0));
		card.getCard().setMouseTransparent(true);
		Player playerOld = ((PlayerCards)card.getCardContainer()).getPlayer();
		playerOld.setHasDoneAnything(true);
		playerOld.removeCard(card);
//		card.setPlayer(null);
		Deck deck = Player.getDeck(card.isRegular());
		deck.addCardToDiscardStack(card);
	}
	
	public void moveCardTo(Card card, Player playerNew){
//		playField.remove(card.getCard());
//		playField.add(card.getCard());
		if(activePlayer == controllingPlayer)
			networkController.sendMessage(
					new ChatMessage(ChatMessage.MOVECARD,card.getCardID(),playerNew.getPlayerID(),0));
		Player playerOld = ((PlayerCards) card.getCardContainer()).getPlayer();
		playerOld.removeCard(card);
		playerNew.addCard(card);
	}

	public void endTurn(){
		// loose your cards if in danger
		if(activePlayer == controllingPlayer){
			networkController.sendMessage(new ChatMessage(ChatMessage.ENDTURN));
			for(Card card : getCards()){
				card.getGhostCard().setStroke(Color.BLACK);
			}
		}
		busyFlag = true;
		activePlayer.resetPawn();
		
		if(getTramstop().hasPolice() || (getTramstop().hasOwner() && getTramstop().getOwner() != activePlayer)){
			List<Card> cards = activePlayer.getCards();
			activePlayer.setHasDoneAnything(true);
			while(!cards.isEmpty()){
				activePlayer.discardCard(cards.get(0));
			}
		// get hashcards for hashtokens
		} else if(getTramstop().getType().equals("start")){
			for(int i=0;i<getTramstop().getNumberOfHashTokens();i++){
				dealCardTo(activePlayer, false);
			}
			getTramstop().resetHashTokens();
		// move your gangster to the tramstop
		}else if(!getTramstop().hasOwner() && (getTramstop().getType().equals("coffeeshop")|| getTramstop().getType().equals("regular") || getTramstop().isDealMade())){
//				System.out.println("MoveGangster");
			moveGangsterTo(getTramstop());
		}
		System.out.println("Beurt Beeindigd");
		activePlayer = activePlayer.getNextPlayer();
		Point2D coord = convertToPlayerOrientation(die.getPositionX(), die.getPositionY(), activePlayer.getOrientation());
		die.moveDieTo(activePlayer.getPositionX()+coord.getX(), activePlayer.getPositionY()+coord.getY(), activePlayer.getOrientation());
		Card.setClickReason(Card.THROWDIE);
	}
	
	protected List<Tramstop> tramstopsToChooseFromPolice(){
		List<Tramstop> tramstopsToChooseFrom = new ArrayList<Tramstop>();
		List<Tramstop> tramstops = Tramstop.getTramstops();
//		List<GamePieceContainer> policeBases = Player.getPoliceBases();
		boolean policeAvailable = false;
//		for(GamePieceContainer policeBase : policeBases){
			if(policeBase.hasPolice()){
				policeAvailable =true;
//				System.out.println(policeAvailable);
//				break;
			}
//		}
		for(Tramstop tramstop : tramstops){
    		if(policeAvailable){
    			if(!(tramstop instanceof Boatstop) && !tramstop.getType().equals("start") && tramstop.getOwner() == null){
    				tramstopsToChooseFrom.add(tramstop);
    			}
    		}
    		else{
    			if(tramstop.hasPolice()){
    				tramstopsToChooseFrom.add(tramstop);
    			}
    		}
		}
		Tramstop.setClickReason("choosePolice");
		return tramstopsToChooseFrom;
	}
	
	protected List<Boatstop> boatstopsToChooseFromFlip(){
		// TODO hmmm
		List<Boatstop> boatstops = Boatstop.getBoatstops();
		List<Boatstop> toChooseFrom = new ArrayList<Boatstop>();
		for(Boatstop boatstop : boatstops){
			if(boatstop.hasGoal()){
				toChooseFrom.add(boatstop);
				Tramstop.setClickReason("flipGoal");
			}
		}
		return toChooseFrom;
	}

	protected void addHashToStart(){
		Tramstop tramstopStart = Tramstop.getTramstopStart();
		int hashTokensInPlay = tramstopStart.getNumberOfHashTokens(); 
		
		if(hashTokensInPlay<5){
			Player.getHashBase().getGamePieces().get(0).moveToContainer(tramstopStart);
		}
	}
	
	protected boolean addCardsBasedOnDieThrow(Player player, String dieResult, boolean isRegular){
		boolean any = false;
		int numberOfcardsToGet = player.getNumberOfCardsToGet(dieResult);
		for(int i = 0; i < numberOfcardsToGet; i++){
			any = true;
			dealCardTo(player,isRegular);

//			Card card = controller.getDeck(isRegular).getNextCard();
//			moveCardTo(card, player);
		}
		return any;
	}

	protected void buyHash(List<Card> selectedCards) {
		while(!selectedCards.isEmpty()){
			Card card = selectedCards.remove(selectedCards.size()-1);
			discardCard(card);
		}
//		makeNextMove();
		//TODO only when humanplayer
		resetSelectedCards();
		Card.setClickReason(Card.SELECT);
		
//		Card card = controller.getDeck(false).getNextCard();
//		card.resetAnimation();
		dealCardTo(activePlayer,false);
	}
	
	protected void stealCards(List<Card> selectedCards, Player playerToStealFrom) {
		// TODO werkt niet
		System.out.println("StealCards");
		moveCardTo(getSelectedCards().get(0), activePlayer);
		moveCardTo(getSelectedCards().get(0), activePlayer);
		discardCard(activePlayer.getSelectedCards().get(0));
		playerToStealFrom.flipCards(false);
//		selectedCards.get(0).resetAnimation();
//		for (Card card : playerToStealFrom.getCards()){
//			if(card.isRegular()){
//				card.flipCard(true);
//			}
//		}
//		resetSelectedCards();
		Card.setClickReason(Card.SELECT);
	}
	
	public void throwAwayCards(Player player) {
		if(player == controllingPlayer)
			networkController.sendMessage(new ChatMessage(ChatMessage.THROWAWAYCARDS,0,player.getPlayerID(),0));
		List<Card>selectedCards = player.getSelectedCards();
		List<Card> cards = player.getCards(); 
		int index = 0;
		while(index < cards.size()){
			Card card = cards.get(index);
			card.setStrokeColor(Card.getDefaultcolor());
			if(selectedCards.contains(card)){
				selectedCards.remove(card);
				discardCard(card);
			}
			else{
				index++;
			}
		}
		numberOfThrowAwayPlayers--;
		if(numberOfThrowAwayPlayers == 0){
			Card.setClickReason(Card.SELECT);
			Arrow.setClickReason("");
		}
	}
	protected void chooseGangster(List<Tramstop> tramstopsToChooseFrom){
		
	}
	
	protected void chooseCard(){};
	
	public void handleDie(){
		Card.setClickReason(Card.SELECT);
//		if(activePlayer!=controllingPlayer)
		if(activePlayer == controllingPlayer){
			Player.getNetworkController().sendMessage(new ChatMessage(ChatMessage.DIETHROWN));
			for(Card card : getCards()){
				card.getGhostCard().setStroke(Color.DARKGRAY);
			}
		}
		busyFlag = true;
		String dieResult = die.getNextResult();

		List<Player> players = Player.getPlayers();
		boolean someCardsGiven = false;
		int boatSteps = 0;
//		dieResult = "gangster";
		switch (dieResult)
		{
		case "any":
			addHashToStart();
			boatSteps = 1;
			break;
		case "weed":
			for(Player player:players){
				if(addCardsBasedOnDieThrow(player, "PurpleHaze", true)) someCardsGiven = true;
				if(addCardsBasedOnDieThrow(player, "OrangeBud", true)) someCardsGiven = true;
				if(addCardsBasedOnDieThrow(player, "Top44", true)) someCardsGiven = true;
			}
			if(!someCardsGiven){
				for(Player player:players){
					dealCardTo(player,true);
				}
			}
			boatSteps = 2;
			break;
		case "hash":
			for(Player player:players){
				if(addCardsBasedOnDieThrow(player, "Hiya", false)) someCardsGiven = true;
				if(addCardsBasedOnDieThrow(player, "Afghan", false)) someCardsGiven = true;
				if(addCardsBasedOnDieThrow(player, "Shiva", false)) someCardsGiven = true;
			}
			if(!someCardsGiven){
				for(Player player:players){
					dealCardTo(player,true);
				}
			}
			boatSteps = 3;
			break;
		case "fourCards":
			// four cards are dealt to active player
			// active player chooses the best card, gives the three remaining cards to the next player
			// next player chooses one, gives remaining two cards to next next player...
			// next next player chooses one, gives the last card to next next next player...
			setChoosingPlayer(activePlayer);
			cardsToChooseFrom = new ArrayList<Card>();
			cardsToChooseFrom.add(dealCardTo(activePlayer,true));
			cardsToChooseFrom.add(dealCardTo(activePlayer,true));
			cardsToChooseFrom.add(dealCardTo(activePlayer,true));
			cardsToChooseFrom.add(dealCardTo(activePlayer,true));
			for(Card card : cardsToChooseFrom){
				card.setStrokeColor(Card.getChoicecolor());
				if(activePlayer == controllingPlayer)
					card.getCard().setMouseTransparent(false);
			}
			busyFlag = false;
			Card.setClickReason(Card.CHOOSECARD);
			activePlayer.chooseCard();
////			boatSteps = 4;
			break;
		case "gangster":
			if(activePlayer == controllingPlayer){
				List<Tramstop> tramstopsToChooseFrom = tramstopsToChooseFromPolice();
				chooseGangster(tramstopsToChooseFrom);
			}
			busyFlag = false;
   		break;
		case "moreThenSix":
//			boolean any = false;
			busyFlag = false;
			for (Player player : players){
				if(player.getCards().size()>6){
					player.throwAwayHalfYourCards();
					if(player instanceof PlayerHuman){
						numberOfThrowAwayPlayers++;
					}
					// playerCards are shown and when half the cards are selected arrow shows
					// when arrow is clicked program goes to void throwAway
					// when all the players that needed to throw away card have done so, active player can start his/her turn
				}
			}
			if(numberOfThrowAwayPlayers==0){
				makeNextMove();
			}
			else{
				Card.setClickReason(Card.THROWAWAY);
//				activePlayer.throwAwayHalfYourCards();
			}
//			boatSteps = 6;
			break;
		default: break;
		}
		if(boatSteps>0){
			Boat boat = Boat.getBoat();
			Boatstop nextBoatstop = ((Boatstop) boat.getGamePieceContainer());
			for( int i = 0; i<boatSteps; i++){
				nextBoatstop = nextBoatstop.getNextBoatStop();
			}
			moveBoatTo(boat, nextBoatstop);
		}
	}
	
	public void cardChosen(Card chosenCard){
		System.out.println("choosingPlayer: "+choosingPlayer.getPlayerID());
		if(activePlayer==controllingPlayer)
			networkController.sendMessage(new ChatMessage(ChatMessage.CARDCHOSEN,chosenCard.getCardID(),0,0));
    	chosenCard.getCard().setStroke(Card.DefaultColor);
		Player playerNew = activePlayer.getNextPlayer();
		cardsToChooseFrom.remove(chosenCard);
		if(cardsToChooseFrom.size() == 1){
			cardsToChooseFrom.get(0).setStrokeColor(Card.getDefaultcolor());
			setActivePlayer(choosingPlayer);
			moveCardTo(cardsToChooseFrom.get(0), playerNew);
			Card.setClickReason(Card.SELECT);
		} else{
			setActivePlayer(playerNew);
			for(Card cardToPass: cardsToChooseFrom){
				moveCardTo(cardToPass, playerNew);
				if(activePlayer == controllingPlayer)
					cardToPass.getCard().setMouseTransparent(false);
//				if(playerNew!=Player.getChoosingPlayer() && !playerNew.isAI()){
//					cardToPass.setMouseTransparent(false);
//				}
			}
//			activePlayer.doSomething();
		}

	}
	
	public void makeNextMove(){
		
	}
	
	public void throwDie(){
		
	}
	
	public void stopAnimatingCards(){
		Player player = this;
		do{
			player = player.getNextPlayer();
			player.resetCardAnimation();
		} while (player.getNextPlayer() != this);
		playerRectangle.stopAnimatingCards();
	}
	
	public void getCardsForGrowers(){
		for(Goal goal : playerRectangle.getGoals()){
			if(goal.getName().equals("PurpleHazeOrangeBudTop44")){
				//TODO ??
			}
		}
		
	}
	
	public int getNumberOfCardsToGet(String dieResult){
		int numberOfCardsToGet = 0;
		for(Goal goal : playerRectangle.getGoals()){
			if(goal.getName().equals(dieResult)){
				numberOfCardsToGet++;
			}
		}
		return numberOfCardsToGet;
	}
	
	
	public List<Card> getCards() {
		return playerRectangle.getCards();
	}
	
	public List<Card> getCards(boolean isRegular) {
		return playerRectangle.getCards(isRegular);
	}
	
	public void addCard(Card card){
		playerRectangle.addCard(card);
	}
	
	public void removeCard(Card card){
		playerRectangle.removeCard(card);
	}

	public Pawn getPawn() {
		return pawn;
	}
	
	public List<Goal> getGoals() {
		return playerRectangle.getGoals();
	}
	
	public void addGoal(Goal goal){
		playerRectangle.addGoal(goal);
	}

	public String getName() {
		return name;
	}
	
//	public void movePawnTo(Tramstop tramstop){
//		pawn.moveToContainer(tramstop);
//	}
//	
	public void moveGangsterTo(Tramstop tramstop){
		GamePiece gangster = activePlayer.playerRectangle.getGangster();
		gangster.moveToContainer(tramstop);
		activePlayer.playerRectangle.resetGangsterText();
	}
	
	public void growPawn(){
		if(this == Player.controllingPlayer)
			networkController.sendMessage(new ChatMessage(ChatMessage.GROWPAWN));
		getPawn().growPawn();
	}
	
	public void resetPawn(){
		getPawn().resetPawn();
	}
	
	public Tramstop getTramstop() {
		return (Tramstop) activePlayer.pawn.getGamePieceContainer();
	}
	
	public void setTramstop(Tramstop tramstop) {
		pawn.setContainer(tramstop);
	}
	
	public static void setActivePlayer(Player activePlayer) {
		Player.activePlayer = activePlayer;
	}
	
	public void flipCards(boolean frontUp){
		System.out.println("Flip:");
		playerRectangle.flipCards(frontUp);
	}
	
	public void resetCardAnimation(){
		playerRectangle.resetCardAnimation();
	}
	
	public Player getNextPlayer() {
		return nextPlayer;
	}
	
	public void setNextPlayer(Player nextPlayer) {
		this.nextPlayer = nextPlayer;
	}
	
	public boolean hasTraveled() {
		return hasTraveled;
	}
	
	public void setHasTraveled(boolean hasTraveled) {
		hasDoneAnything = hasTraveled;
		this.hasTraveled = hasTraveled;
	}
	
	public boolean hasDoneAnything() {
		return hasDoneAnything;
	}
	
	public void setHasDoneAnything(boolean hasDoneAnything) {
		this.hasDoneAnything = hasDoneAnything;
	}
	
	public Arrow getArrow() {
		return playerRectangle.getArrow();
	}

	public void activateArrow(String reason){
		playerRectangle.activateArrow(reason);
	}
	
	public void deactivateArrow(){
		playerRectangle.deactivateArrow();
	}
	
	public void throwAway() {
		needsToThrowAway = false;
	}
	
	protected void throwAwayHalfYourCards() {
	}

	public Color getColor() {
		return color;
	}

	public static Player getChoosingPlayer() {
		return choosingPlayer;
	}

	public static void setChoosingPlayer(Player choosingPlayer) {
		Player.choosingPlayer = choosingPlayer;
	}

	public List<GamePiece> getGangsters() {
		return playerRectangle.getGangsters();
	}

	public static int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	public static void setNumberOfPlayers(int numberOfPlayers) {
		Player.numberOfPlayers = numberOfPlayers;
	}
	public double getPawnScale(){
		return pawn.getScale();
	}

	public Group getPlayerRectangle() {
		return playerRectangle;
	}

	public static void setPlayField(PlayField playField) {
		Player.playField = playField;
	}
	public List<Card> getSelectedCards(){
		return playerRectangle.getSelectedCards();
	}
	
	public void stoppedMoving(){
		System.out.println("StoppedMoving cards" + Card.getNumberOfCardsMoving()+"gp: "+GamePiece.getMovingGamePieces());
		if(Card.getNumberOfCardsMoving()==0 && GamePiece.getMovingGamePieces() == 0 ){
			busyFlag = false;
			if(!readyFlag){
				networkController.sendMessage(new ChatMessage(ChatMessage.READY));
				return;
			}
			System.out.println("CardClickReason" + Card.getClickReason()+"TramstopClickreason "+Tramstop.getClickReason()+"ArrowClickreason "+Arrow.getClickReason()+"dierolling "+Player.getDie().isRolling());
			// TODO if die is moving its also rolling...
			if(Card.getClickReason() == Card.SELECT && Tramstop.getClickReason().equals("") && Arrow.getClickReason().equals("") && !Player.getDie().isRolling()){
				activePlayer.makeNextMove();
			}
			else{
				if(Card.getClickReason() == Card.CHOOSECARD){
					activePlayer.chooseCard();
				}else if(Card.getClickReason() == Card.THROWDIE && !Player.getDie().isRolling()){
					System.out.println("throw stopped moving");
					activePlayer.throwDie();
//					Card.setClickReason("");
				}
				
			}
		}
	}
	public void giveCardToActivePlayer(Card clickedCard){
		Card selectedCard = activePlayer.getSelectedCards().get(0);
		List<Player> players = Player.getPlayers();
		for(Player player : players){
			if (!(player == activePlayer)){
				player.resetCardAnimation();			
			}
		}
		Card stolenCard = playerRectangle.stealCard(selectedCard.isRegular());
		moveCardTo(stolenCard,activePlayer);
		discardCard(selectedCard);
		resetSelectedCards();
		Card.setClickReason(Card.SELECT);
	}

	protected void resetSelectedCards(){
		playerRectangle.resetSelectedCards();
	}
	
	public void showCards(Card clickedCard){
		if(activePlayer instanceof PlayerHuman){
			((PlayerHuman) (activePlayer)).deactivateEndTurn();
		}
		stopAnimatingCards();
//		for(Card card : Player.activePlayer.getCards()){
//			card.deactivate();
//		}
		Player player = ((PlayerCards) clickedCard.getCardContainer()).getPlayer();
		List<Card> toChooseFrom = new ArrayList<Card>();
		for(Card card : player.getCards(true)){
//			if(card.isRegular()){
				toChooseFrom.add(card);
//			}
		}
		if(toChooseFrom.size()>2){
			for(Card card:toChooseFrom){
//				card.activate();
				card.flipCard(true);
			}
			Card.setClickReason(Card.AFGHAN);
		}
		else{
			discardCard(activePlayer.getSelectedCards().get(0));
			for(Card card:toChooseFrom){
				moveCardTo(card,activePlayer);
			}
			resetSelectedCards();
			Card.setClickReason(Card.SELECT);
		}
	}
	// TODO previous selection...
	public void selectionChanged(int clickReason) {}

	public int getNumberOfMovingCards() {
		return numberOfMovingCards;
	}
	public void raiseNumberOfMovingCards() {
		numberOfMovingCards++;
	}
	public void lowerNumberOfMovingCards() {
		numberOfMovingCards--;
	}
	public static Player getActivePlayer(){
		return activePlayer;
	}
	public void selectCard(Card card) {
		playerRectangle.selectCard(card);
	}
	public void deselectCard(Card card) {
		playerRectangle.deselectCard(card);
	}
	public void playerArrowClicked(Player player, String clickReason) {
		// TODO FUck
		System.out.println(clickReason);
		if(clickReason.equals("stealCards")){
			stealCards(getSelectedCards(), player);
		} else if(clickReason.equals("throwAway")){
			throwAwayCards(player);
		} else if(clickReason.equals("shiva")){
			 player.growPawn();
			 kickoutGangsters(player, getTramstop());
			 discardCard(getSelectedCards().get(0));
			 resetSelectedCards();
		}
	}
	public void deckArrowClicked(Deck parent, String clickReason) {
	}

	
	
	public static Deck getDeck(boolean isRegular) {
		if(isRegular) return decks.get(0);
		else return decks.get(1);
	}
	
	public static List<Deck> getDecks() {
		return decks;
	}
	
	public static void addDeck(Deck deck){
		decks.add(deck);
	}

	public static void setAnimationSpeed(Duration animationSpeed) {
		Player.animationSpeed = animationSpeed;
	}
	
	public static Die getDie() {
		return die;
	}
	
	public static void setDie(Die die){
		Player.die = die;
		playField.add(die);
	}
	
//	public static void addTramstop(Tramstop tramstop) {
//		tramstops.add(tramstop);
//	}
//	
//	public static List<Tramstop> getTramstops() {
//		return tramstops;
//	}
	
//	public static List<Boatstop> getGoalTramstops() {
//		return boatstops;
//	}
	
//	public static void addGoalTramstop(Boatstop boatstop) {
//		Player.boatstops.add(boatstop);
//	}

	public static GamePieceContainer getPoliceBase() {
		return policeBase;
	}
	
	public static void setPoliceBase(GamePieceContainer policeBase) {
		Player.policeBase = policeBase;
	}
	
	public static List<GamePieceContainer> getGoalBases() {
		return goalBases;
	}
	
	public static void addGoalBase(GamePieceContainer goalBase) {
		goalBases.add(goalBase);
	}
	
	public static void setHashBase(GamePieceContainer hashBase) {
		Player.hashBase = hashBase;
	}

	public static GamePieceContainer getHashBase() {
		return Player.hashBase;
	}

	public static List<Player> getPlayers() {
		return players;
	}
	public static Random getRandom() {
		return random;
	}
	public static void setRandom(Random random) {
		Player.random = random;
	}

	public static DopeMain getNetworkController() {
		return networkController;
	}

	public static void setNetworkController(DopeMain networkController) {
		Player.networkController = networkController;
	}

	public static int getControllingPlayerId() {
		return controllingPlayerId;
	}

	public static void setControllingPlayerId(int controllingPlayer) {
		Player.controllingPlayerId = controllingPlayer;
	}

	public static Player getControllingPlayer() {
		return controllingPlayer;
	}

	public int getPlayerID() {
		return playerID;
	}
	
	public static Player getPlayer(int id){
		return players.get(id);
	}

	public static PlayField getPlayField() {
		return playField;
	}
	public static boolean getReadyFlag(){
		return readyFlag;
	}
	public static boolean getBusyFlag(){
		return busyFlag;
	}

	public static Duration getAnimationSpeed() {
		return animationSpeed;
	}

	public void startGame() {
		readyFlag = true;
		System.out.println(activePlayer.getPlayerID());
		System.out.println(controllingPlayer.getPlayerID());
//		setActivePlayer(players.get(random.nextInt(players.size())));
		activePlayer.throwDie();
	}

	public double getPositionX() {
		return positionX;
	}

	public double getPositionY() {
		return positionY;
	}

	public double getOrientation() {
		return orientation;
	}

	public static void setActivePlayerId(int nextInt) {
		activePlayerID = nextInt;
	}

	public static void setBusyFlag(boolean busy) {
		busyFlag = busy;
	}
}
