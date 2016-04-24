package gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import network.DopeMain;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.tramstop.Boatstop;
import model.tramstop.GamePieceContainer;
import model.Die;
import model.PlayField;
import model.PlayerOrientationConverter;
import model.tramstop.Tramstop;
import model.cards.Card;
import model.cards.Deck;
import model.gamepiece.Boat;
import model.gamepiece.Goal;
import model.gamepiece.HashToken;
import model.gamepiece.GamePiece;
import model.gamepiece.Police;
import model.player.PlayerHuman;
import model.player.Player;
import model.player.PlayerComputer;
public class DopeGUI extends Application implements PlayerOrientationConverter{
	private PlayField playField;
	private Label lb_text;
	private Button btn_click;
//	private Button btn_endTurn;
	private final double preferedRatio = 1.2f;
//	private int clicks = 0;
//	private static Main controller;
	private Pane root = null;
//	private static int numberOfThrowAwayPlayers = 0;
//	private String defaulthost;
//	private int defaultport;
	private static List<String> names;
	private static List<Color> colors;
//	private static int controllingPlayer;
//	private static DopeMain dopeMain;
	
//	private long randomScramble;
	
	public DopeGUI(int playerID, List<Color> playerColors, List<String> names, long randomScramble, DopeMain networkController){
		
	}
	
	public DopeGUI(){
//		super();
//		if(Player.getRandom() == null)
//			Player.setRandom(new Random(100000));
//		initGUI();
//		DopeGUI.launch();
	}
	@Override
	public void init(){
		
		Card.setAnimationSpeed(Duration.millis(500));
		GamePiece.setAnimationSpeed(Duration.millis(500));
		
		Rectangle gameBoard = new Rectangle();
		ImagePattern imagePattern = new ImagePattern(new Image("/GameBoard685.png"));
		gameBoard.setFill(imagePattern);
		gameBoard.setWidth(625);
		gameBoard.setHeight(625);
		playField = new PlayField();
		
		playField.add(gameBoard);
		playField.setPrefWidth(960);
		playField.setPrefHeight(800);
		
//		btn_endTurn = new Button("End Turn");
//		btn_endTurn.setMouseTransparent(true);
//		btn_endTurn.setTranslateX(520);
//		btn_endTurn.setTranslateY(650);
//		btn_endTurn.setOnAction(new EventHandler<ActionEvent>(){
//			@Override
//			public void handle(ActionEvent event) {
//				endTurn();
//			}
//		});
//		
//		playField.add(btn_endTurn);
		
		Deck deck = new Deck(476, 112, 476, 6, true);
		int[] cardFrequencyRegular = {  5,       5, 	   5,      5,     5,    5,       10,        6,          6,         6};
		String[] cardNamesRegular = {"Purple","Orange","Yellow","Green","Blue","Red","Rainbow","PurpleHaze","OrangeBud","Top44"};
		addCards(cardFrequencyRegular,cardNamesRegular, deck);
		
		deck = new Deck(550, 112, 550, 6, false);
		int[] cardFrequencyAdvanced ={   2,       2,       2,         2,       2,     2,         2,             4          , 4,          4};
		String[] cardNamesAdvanced = {"PurpleRainbow","OrangeRainbow","YellowRainbow","GreenRainbow","BlueRainbow","RedRainbow","RainbowRainbow","Hiya","Afghan","Shiva"};
		addCards(cardFrequencyAdvanced,cardNamesAdvanced, deck);
		
		GamePieceContainer hashTokenBase = new GamePieceContainer(300, 30);
		for(int i=0;i<5;i++){
			HashToken hashToken = new HashToken();
			hashToken.setGamePieceBase(hashTokenBase);
			playField.add(hashToken.getNode());
			Player.setHashBase(hashTokenBase);
		}
				  				//index=    001, 002, 003, 004, 005, 006, 007, 008, 009, 010, 011, 012, 013, 014, 015, 016
		Integer[] goals = new Integer[]  {    1,    5,   13,   20,   33,   42,     7,   43,   16,   17,    18,    10,    52,    49,    53,    21};
		boolean[] isWhite = new boolean[]{false, true, true, true, true, true, false, true, true, true, false, false, false, false, false, false};
		double[] offsetX = 					{20,   20,   20,   20,   20,   20,    20,   20,   20,   20,    20,    20,    20,    20,    20,    20};
		double[] offsetY = 					{20,   20,   20,   20,   20,   20,    20,   20,   20,   20,    20,    20,    20,    20,    20,    20};
		
					//index=    000, 001, 002,   003, 004, 005, 006, 007, 008, 009, 010, 011,   012,   013,   014, 015,   016, 017, 018,   019, 020, 021,   022,   023,   024, 025, 026,   027,   028,   029,   030, 031,   032,   033,   034,   035, 036, 037, 038,   039,   040,   041, 042, 043, 044,   045,   046,   047,   048, 049, 050,   051, 052, 053];
		double[] tramstopX = {450.7, 124, 192, 262.7, 246, 306, 362, 414, 488, 538, 579, 100, 169.2, 225.7, 275.8, 327, 385.5, 443, 491,  72.6, 143,  48,  28.5,  80.7, 128.0, 188, 280, 328.6, 391.3, 435.5, 497.7,  61, 120.8, 181.0, 229.0, 231.0, 266, 286, 335, 128.0, 129.5, 173.5, 240, 343, 183, 264.5, 358.2, 467.7, 533.3, 248, 322, 426.5, 495,  89};
		double[] tramstopY = {236.8,  28,  50,  73.5, 127, 121, 169, 486, 269, 303, 377,  85, 141.5, 181.0, 202.8, 207, 239.0, 299, 345, 155.0, 191, 207, 285.5, 264.7, 245.3, 256, 272, 300.7, 327.5, 382.5, 404.1, 370, 331.5, 331.5, 332.5, 398.5, 345, 402, 378, 419.5, 490.5, 448.2, 447, 429, 543, 500.3, 499.8, 470.0, 453.0, 583, 589, 570.3, 546, 441};
		Integer[] coffeeshops = {00, 02, 03, 04, 11, 12, 14, 15, 19, 22, 23, 24, 25, 27, 28, 29, 32, 34, 35, 36, 37, 38, 39, 40, 41, 44, 45, 46, 48};
		
		List<List<Integer>> tramLines = new ArrayList<List<Integer>>(6);
		Integer[] purpleLine = new Integer[]{ 1,  2,  3,  5,  6,  0,  8, 17, 28, 38, 37, 42, 41, 39, 32, 24, 20, 12, 11,  1};
		Integer[] orangeLine = new Integer[]{26, 27, 28, 17,  8,  9, 10, 48, 47,  7, 46, 45, 41, 39, 53 ,31, 32, 33, 34, 26};
		Integer[] yellowLine = new Integer[]{22, 23, 24, 25, 26, 36, 37, 43, 46, 50, 49, 44, 40, 53, 31, 22, 21, 19, 11,  1};
		Integer[] greenLine = new Integer[]{19, 12, 13, 14,  6, 16, 28, 29, 47, 52, 51, 46, 43, 37, 35, 33, 32, 31, 22, 21, 19};
		Integer[] blueLine = new Integer[]{37, 45, 49, 50, 51, 52, 48, 30, 29, 43, 37, 36, 26, 15,  6, 14, 26, 34, 35, 41, 40, 44, 49};
		Integer[] redLine = new Integer[]{ 2,  3,  4, 13, 14, 15, 16,  0,  8,  9, 18, 29, 38, 37, 35, 33, 25, 24, 20, 12,  2};
		tramLines.add(Arrays.asList(purpleLine));
		tramLines.add(Arrays.asList(orangeLine));
		tramLines.add(Arrays.asList(yellowLine));
		tramLines.add(Arrays.asList(greenLine));
		tramLines.add(Arrays.asList(blueLine));
		tramLines.add(Arrays.asList(redLine));
		
		addTramstops(tramstopX, tramstopY, Arrays.asList(coffeeshops), Arrays.asList(goals), isWhite, offsetX, offsetY, tramLines, cardNamesRegular, cardNamesAdvanced);
		
		initPolice(400.0,55.0,6);

		int[] whiteGoalFrequency ={      3,         3 ,        3,      3};
		String[] whiteGoalNames = {"PurpleHaze","OrangeBud","Top44","PurpleHazeOrangeBudTop44"};
		int[] blackGoalFrequency ={   2,      2,      2,        3};
		String[] blackGoalNames = {"Hiya","Afghan","Shiva", "HiyaAfghanShiva"};
		double[] whiteGoalStopsOutsideX = { 25,  25,  25,  25};
		double[] whiteGoalStopsOutsideY = {430, 480, 530, 580};
		double[] blackGoalStopsOutsideX = { 80,  80,  80, 125};
		double[] blackGoalStopsOutsideY = {500, 550, 600, 580};
		addGoalStopsOutside(whiteGoalStopsOutsideX,whiteGoalStopsOutsideY,whiteGoalNames,whiteGoalFrequency, true);
		addGoalStopsOutside(blackGoalStopsOutsideX,blackGoalStopsOutsideY,blackGoalNames,blackGoalFrequency, false);
		
		if(names.isEmpty()){
			names = new ArrayList<String>();
			names.add("Harmjan");
			names.add("Michael");
			names.add("Roger");
			names.add("Ruben");
			
			colors = new ArrayList<Color>();
			colors.add(Color.YELLOW);
			colors.add(Color.PURPLE);
			colors.add(Color.GREEN);
			colors.add(Color.BLUE);
		}
		initPlayers(names,colors);
		for(Card card : Card.getCards())
			playField.add(card.getCard());
		for(Card card : Card.getCards())
			playField.add(card.getGhostCard());

		
		String[] results = new String[]{"any", "weed", "hash", "fourCards", "gangster", "moreThenSix"}; 
		Die die = new Die(245.0, 23.0, results);
		Player.setDie(die);
		Point2D coord = convertToPlayerOrientation(die.getPositionX(), die.getPositionY(), Player.getActivePlayer().getOrientation());
		die.moveDieTo(Player.getActivePlayer().getPositionX()+coord.getX(), Player.getActivePlayer().getPositionY()+coord.getY(), Player.getActivePlayer().getOrientation());
		

		//TODO give each player cards and open some tramstops
		List<Player> players = Player.getPlayers(); 
		for(Player player : players){
			player.getStartingCards();
			
//			Random random = new Random();
//			
//			List<Boatstop> boatstops = controller.getGoalTramstops();
//			Boatstop boatstop = boatstops.get(random.nextInt(boatstops.size()));
//			while(boatstop.hasGoal()){
//				boatstop = boatstops.get(random.nextInt(boatstops.size()));
//			}
////			System.out.println("PLayers geslaags");
//			Player.placeGoal(boatstop);
//			
//			if(player == Player.getActivePlayer()){
//				player.deactivateCards();
//			}
		}
//		players.get(0).throwDie();
//		controller.getDice().get(0).activateDie();
	}

	@Override
	public void start(Stage stage) throws Exception {
		DropShadow ds = new DropShadow();
		ds.setOffsetX(5);
		ds.setOffsetY(5);
		ds.setColor(Color.GREY);
		Reflection ref = new Reflection();
		ref.setFraction(0.8);
		ref.setTopOffset(-5);
		lb_text = new Label("Something");
		lb_text.getStyleClass().add("myCustomLabel");
		lb_text.setEffect(ref);
		
		btn_click = new Button("Click it");
		btn_click.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				// TODO click it
//				Flag flag = new Flag(Player.getActivePlayer());
				Player player = Player.getActivePlayer();
				Tramstop tramstop = Player.getActivePlayer().getTramstop();
				player.moveGangsterTo(tramstop);
//				playField.add(flag);
//				playField.remove(Player.getActivePlayer().getPawn());
//				playField.add(Player.getActivePlayer().getPawn());
				
//				flag.setTranslateX(tramstop.getPlaceOnPlayfieldX());
//				flag.setTranslateY(tramstop.getPlaceOnPlayfieldY());
				
//				Tramstop tramstop = controller.getTramstops().get(clicks);
//				movePawnTo(controller.getPlayers().get(0), tramstop);
//				Boat boat = Boat.getBoat();
//				moveBoatTo(boat,((Boatstop) boat.getTramstop()).getNextBoatStop());
//				dealCardTo(controller.getPlayers().get(0),true);
//				dealCardTo(controller.getPlayers().get(1),true);
//				dealCardTo(controller.getPlayers().get(2),true);
//				dealCardTo(controller.getPlayers().get(3),true);
//				clicks++;
			}
		});
		

		btn_click.setEffect(ds);
		playField.add(btn_click);
		root = new BorderPane();
//		playField.setTranslateX(170);
		root.getChildren().addAll(playField);
		Scene scene = new Scene(root, 800,500);
		relocateEverything(scene.widthProperty().doubleValue(),scene.heightProperty().doubleValue());
		scene.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				double height = scene.heightProperty().doubleValue();
				double newV = newValue.doubleValue();
				relocateEverything(newV, height);
			}
	    });
		scene.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				double width = scene.widthProperty().doubleValue();
				double newV = newValue.doubleValue();
				relocateEverything(width, newV);
			}
	    });
		stage.setScene(scene);
		scene.getStylesheets().add("myStyle.css");
		
		stage.show();
		
	}
	
	private void relocateEverything(double width, double height){
		if(width >= preferedRatio * height){
			double scale = height/800;
			playField.setTranslateX(scale*170);
//			playField.setTranslateY(0 + (scale-1)*(400));
			playField.setScaleX(scale);
			playField.setScaleY(scale);
		} else{
			double scale = width/960;
			playField.setTranslateX(scale*170);
//			playField.setTranslateY(0 + (scale-1)*(400));
			playField.setScaleX(scale);
			playField.setScaleY(scale);
		}
	}
		
	private void addCards(int[] cardFrequency, String[] cardNames, Deck deck){
		int subType;
		Player.addDeck(deck);
//		Deck deck = Player.getDeck(isRegular);
		for(int i = 0; i < cardFrequency.length; i++){
			if(i < cardFrequency.length-3){
				subType = i;
			}
			else{
				subType = i - cardFrequency.length + 3;
			}
			for(int j = 0; j < cardFrequency[i]; j++){
				Card card = new Card(subType, cardNames[i], 60, 90, deck.isRegular());
				card.setTranslateX(deck.getDrawStackX());
				card.setTranslateY(deck.getDrawStackY());
				deck.addCardToDrawStack(card);
			}
		}
		deck.shuffleDrawStack();
		playField.add(deck);
	}
	
	private void addTramstops(double[] tramstopX, double[] tramstopY, List<Integer> coffeeshops, List<Integer> goals, boolean[] isWhite, double[] offsetX, double[] offsetY, List<List<Integer>> tramlines, String[] cardNamesRegular, String[] cardNamesAdvanced){
		List<Tramstop> tramstops = new ArrayList<Tramstop>(tramstopX.length);
		Tramstop tramstop;
		String type;
		int goalIndex;
		for(int tramstopIndex=0;tramstopIndex<54;tramstopIndex++){
			if(coffeeshops.indexOf(tramstopIndex)!=-1){
				type =  "coffeeshop";
			}
			else if(goals.indexOf(tramstopIndex)!=-1){
				type = "goal";
			}
			else if(tramstopIndex==26){
				type = "start";
			}
			else{
				type = "regular";
			}
			goalIndex = goals.indexOf(tramstopIndex);
			if(goalIndex == -1){
				tramstop = new Tramstop(type, tramstopX[tramstopIndex], tramstopY[tramstopIndex]);
			}
			else{
				tramstop = new Boatstop(type, tramstopX[tramstopIndex], tramstopY[tramstopIndex], offsetX[goalIndex], offsetY[goalIndex], isWhite[goalIndex]);
			}
			tramstops.add(tramstop);
			tramstop.initConnections(cardNamesRegular.length, cardNamesAdvanced.length);
			playField.add(tramstop.getCircle());
		}
		for(int index = 0; index<goals.size();index++){
			Boatstop boatstop = (Boatstop) tramstops.get(goals.get(index));
			boatstop.setNextBoatStop((Boatstop) tramstops.get(goals.get((index + 1) % goals.size())));
		}
		Boat boat = new Boat();
		Tramstop boatStart = tramstops.get(goals.get(0));
		boat.setGamePieceBase(boatStart);
		playField.add(boat.getNode());

		setConnections(tramstops, tramlines, cardNamesRegular, cardNamesAdvanced);
	}
	
	private void setConnections(List<Tramstop> tramstops, List<List<Integer>> tramlines, String[] cardNamesRegular, String[] cardNamesAdvanced){
		for(Tramstop tramstop : tramstops){
			int tramstopId = tramstops.indexOf(tramstop);
			for (int cardSubtype = 0; cardSubtype < 6; cardSubtype++) {
				List<Integer> tramline = new ArrayList<Integer>(tramlines.get(cardSubtype));
				int indexOnTramLine = tramline.indexOf(tramstopId);
				while (indexOnTramLine!=-1){
					if (indexOnTramLine > 0) {
						int previousTramstopId = tramline.get(indexOnTramLine - 1);
						Tramstop previousTramstop = tramstops.get(previousTramstopId);
						// adds connection to color
						tramstop.addRegularConnection(cardSubtype,previousTramstop);
						// adds connection to rainbow
						tramstop.addRegularConnection(6,previousTramstop);
					}
					if (indexOnTramLine != tramline.size()-1){
						int nextTramstopId = tramline.get(indexOnTramLine + 1);
						Tramstop nextTramstop = tramstops.get(nextTramstopId);
						// adds connection for color
						tramstop.addRegularConnection(cardSubtype,nextTramstop);
						// adds connection for rainbow
						tramstop.addRegularConnection(6,nextTramstop);
					}
					tramline = tramline.subList(indexOnTramLine+1, tramline.size());
					indexOnTramLine = tramline.indexOf(tramstopId);
				}
			}
		}
//		// adds the neighbors of each neighbors to each color
		for(Tramstop tramstop : tramstops){
			for (int cardSubtype = 0; cardSubtype < 6; cardSubtype++) {
				List<Tramstop> neighbors = tramstop.getOneStepConnections(cardSubtype);
				for(Tramstop neighbor: neighbors){
					List<Tramstop> nextNeighbors = neighbor.getOneStepConnections(cardSubtype);
					for(Tramstop nextNeighbor: nextNeighbors){
						tramstop.addRegularConnection(cardSubtype, nextNeighbor);
					}
				}
			}
		}
		// doubletramcards: separate the card into two parts
		ArrayList<Integer> firstPart = new ArrayList<Integer>();
		ArrayList<Integer> lastPart = new ArrayList<Integer>();
		// purple == 0... rainbow == 6
		for(String cardName : cardNamesAdvanced){
			for (int index = 0; index<cardNamesRegular.length-3;index++){
				String part = cardNamesRegular[index];
				if(cardName.startsWith(part)){
					firstPart.add(index);
				}
				if(cardName.endsWith(part)){
					lastPart.add(index);
				}
			}
		}
		// adds advanced connections
		for(Tramstop tramstop : tramstops){
			for (int cardSubtype = 0; cardSubtype < cardNamesAdvanced.length-3; cardSubtype++) {
				List<Tramstop> tramstopsFirstPart = tramstop.getRegularConnections(firstPart.get(cardSubtype));
				for(Tramstop tramstopFirstPart : tramstopsFirstPart){
					tramstop.addAdvancedConnection(cardSubtype, tramstopFirstPart);
					List<Tramstop> tramstopsLastPart = tramstopFirstPart.getRegularConnections(lastPart.get(cardSubtype));
					for(Tramstop tramstopLastPart : tramstopsLastPart){
						tramstop.addAdvancedConnection(cardSubtype, tramstopLastPart);
					}
				}
				List<Tramstop> tramstopsLastPart = tramstop.getRegularConnections(lastPart.get(cardSubtype));
				for(Tramstop tramstopLastPart : tramstopsLastPart){
					tramstop.addAdvancedConnection(cardSubtype, tramstopLastPart);
					tramstopsFirstPart = tramstopLastPart.getRegularConnections(firstPart.get(cardSubtype));
					for(Tramstop tramstopFirstPart : tramstopsFirstPart){
						tramstop.addAdvancedConnection(cardSubtype, tramstopFirstPart);
					}
				}
			}
		}
	}
	
	private void initPolice(double placeOnPlayfieldX, double placeOnPlayfieldY, int numberOfPolice){
		GamePieceContainer policeBase = new GamePieceContainer(placeOnPlayfieldX, placeOnPlayfieldY);
		for(int i=0;i<numberOfPolice;i++){
			GamePiece pol = new Police(Color.RED);
			pol.setGamePieceBase(policeBase);
			Player.setPoliceBase(policeBase);
//			pol.moveToBase();
			playField.add(pol.getNode());
		}
	}
	
	private void addGoalStopsOutside(double[] tramstopX, double[]tramstopY, String[] goalNames, int[] goalFrequency, boolean isWhite){
		// TODO less goalBases
		String goalName; 
		for(int goalType=0;goalType<goalNames.length;goalType++){
			goalName = goalNames[goalType];
			for(int counter = 0; counter < goalFrequency[goalType]; counter++){
				GamePieceContainer goalBase = new GamePieceContainer(tramstopX[goalType]+7*counter, tramstopY[goalType]+7*counter);
				Player.addGoalBase(goalBase);
				Goal goal = new Goal(goalName, isWhite);
				goal.setGamePieceBase(goalBase);
//				System.out.println(goalBase.hasGoal());
				playField.add(goal.getNode());
			}
		}
	}

	private void initPlayers(List<String> names, List<Color> colors) {
		int numberOfPlayers = names.size();
		Player.setNumberOfPlayers(numberOfPlayers);
		List<Double> positionsX = new ArrayList<Double>();
		List<Double> positionsY = new ArrayList<Double>();
		List<Integer> orientations = new ArrayList<Integer>();
		double gameBoardSize = 625;
		double width = 3 * gameBoardSize/7;
		double height = 1 * gameBoardSize/4;
		double x = gameBoardSize / 4;
		double y = gameBoardSize;
		positionsX.add(x);
		positionsY.add(y);
		orientations.add(0);
		x = 0;
		if(numberOfPlayers<4){
			y= 4 * gameBoardSize / 14;
			positionsX.add(x);
			positionsY.add(y);
			orientations.add(90);
		}
		else{
			y= 15 * gameBoardSize / 28;   
			positionsX.add(x);
			positionsY.add(y);
			orientations.add(90);
			y= gameBoardSize / 28;
			positionsX.add(x);
			positionsY.add(y);
			orientations.add(90);
		}
		x =  gameBoardSize;
		if(numberOfPlayers == 3 || numberOfPlayers == 4){
			y = 10 * gameBoardSize / 14;
			positionsX.add(x);
			positionsY.add(y);
			orientations.add(270);
		}
		else{
			y= 13 * gameBoardSize / 28;   
			positionsX.add(x);
			positionsY.add(y);
			orientations.add(270);
			y= gameBoardSize / 28;
			positionsX.add(x);
			positionsY.add(y);
			orientations.add(270);
		}
		Player.setPlayField(playField);
		for(int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++){
			
			Color color = colors.get(playerIndex);
			String name = names.get(playerIndex);
			double positionX = positionsX.get((playerIndex +numberOfPlayers - Player.getControllingPlayerId()) % numberOfPlayers);
			double positionY = positionsY.get((playerIndex +numberOfPlayers - Player.getControllingPlayerId()) % numberOfPlayers);
			double orientation = orientations.get((playerIndex + numberOfPlayers - Player.getControllingPlayerId()) % numberOfPlayers);
			Player player;
			if(playerIndex == 5){
				player = new PlayerComputer(color,name,positionX,positionY, width, height, orientation);
			}else{
				player = new PlayerHuman(color,name,positionX,positionY, width, height, orientation);
			}
			playField.add(player.getPlayerRectangle());
			for (GamePiece gangster : player.getGangsters()){
				playField.add(gangster.getNode());
			}
//			playField.add(player.getArrow());
//			Tramstop tramstopStart = Tramstop.getTramstopStart();
//			pawn.setGamePieceBase(tramstopStart);
			
			playField.add(player.getPawn().getNode());
//			tramstopStart.addGamePiece(pawn);
//			movePawnTo(player, tramstopStart);
			
		}
		List<Player> players = Player.getPlayers();
		for(int playerIndex = 0; playerIndex < numberOfPlayers; playerIndex++){
			players.get(playerIndex).setNextPlayer(players.get((playerIndex+1) % numberOfPlayers));
		}
	}
	
	public static void initGUI(int playerId, List<String> names, List<Color> playerColors, long randomScramble, DopeMain networkController) {
		Player.setRandom(new Random(randomScramble));
		Player.setNetworkController(networkController);
		Player.setControllingPlayerId(playerId);
		Player.setAnimationSpeed(Duration.millis(500));
		if(playerColors.isEmpty()){
			playerColors.add(Color.YELLOW);
			playerColors.add(Color.BLUE);
			playerColors.add(Color.GREEN);
			playerColors.add(Color.BLACK);
		}
		Player.setActivePlayerId(Player.getRandom().nextInt(playerColors.size()));
		colors = playerColors;
		DopeGUI.names = names;
//		controllingPlayer = playerId;
//		defaulthost = host;
//		defaultport = port;
		Player.setRandom(new Random(randomScramble));
//		DopeGUI.dopeMain = dopeMain;
		launch();
	}

}
