package model.cards;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import model.player.Player;
import network.ChatMessage;
import model.player.PlayerCards;

public class Card{
	private int subType;
	private String cardName;
	private int isMoving;
	private boolean isRegular;
//	private boolean isActivated = false;
	private boolean frontUp;
	private double offcenterX = 0;
	private double offcenterY = 0;
	private double rotation;
//	double scale = 1.0;
	private Timeline timeline = new Timeline();
	private static int clickReason = 0;
	private static int numberOfCardsMoving = 0;
	private CardContainer cardContainer = null;
	private CardContainer previousCardContainer = null;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	public static final Color DefaultColor;
	public static final Color ChoiceColor;
	public static final Color MouseEnteredColor;
	public static final Color ThrowAwayColor;
	private static Duration animationSpeed;
	private final GhostCard ghostCard;
	private final CardRectangle cardRectangle;
	private final static ImagePattern imageBackRegular;
	private final static ImagePattern imageBackAdvanced;
	private static List<Card> cards = new ArrayList<Card>();
	private final int cardID;
	public static final int SELECT = 0, STEALCARD = 1, AFGHAN = 2, CHOOSECARD = 3, THROWAWAY = 4, SHOWCARDS = 5, THROWDIE = 6, STEALCARDS = 7;
//	private static DopeMain networkController;
	static{
		DefaultColor = Color.BLACK;
		ChoiceColor = Color.GOLD;
		MouseEnteredColor = Color.DARKGRAY;
		ThrowAwayColor = Color.RED;
		imageBackRegular = new ImagePattern(new Image("/cards/img/WeedBack.png"));
		imageBackAdvanced = new ImagePattern(new Image("/cards/img/HashBack.png"));
	}
	public Card(int subType, String cardName, double width, double height, boolean isRegular) {
		cardID = cards.size();
		cards.add(this);
		this.cardName = cardName;
		frontUp = false;
		isMoving = 0;
		this.subType = subType;
		this.isRegular = isRegular;
		Image cardFrontImage;
		ImagePattern imageBack;
		if(isRegular){
			imageBack = imageBackRegular;
		} else{
			imageBack = imageBackAdvanced;
		}
		
		try {
			cardFrontImage = new Image("/cards/img/"+cardName+".png");
		} catch (Exception e) {
			logger.severe("Could not find image for card " + cardName + ". " + e.getMessage());
		}
		cardFrontImage = new Image("/cards/img/"+cardName+".png");
		ImagePattern imageFront = new ImagePattern(cardFrontImage);
		cardRectangle = new CardRectangle(width, height, imageFront, imageBack);
		getCard().setVisible(false);
//		getCard().setMouseTransparent(true);
		ghostCard = new GhostCard(width, height, imageFront, imageBack, cardRectangle);
		getCard().setOnMouseClicked(event -> cardClicked()); 
		getCard().setOnMouseEntered(event -> mouseEntered());
		getCard().setOnMouseExited(event -> mouseExited());
//	    this.setOnMouseDragged(event -> drag(event));
	}
//	private void drag(MouseEvent event) {
//		setTranslateX(getTranslateX()+event.getX());
//		setTranslateY(getTranslateY()+event.getY());
//	}	
    public void cardClicked(){
    	if(!getGhostCard().isVisible() || isMoving!=0)
    		return;
    	Player player = ((PlayerCards) cardContainer).getPlayer();

    	if(player != Player.getControllingPlayer()){
    		if(frontUp){ // only afghan
    			selectCard();
//    			ghostCard.setVisible(false);
    		}
    		else if(timeline.isAutoReverse()){
        		if(clickReason == STEALCARD){
        			player.giveCardToActivePlayer(this);
        	    	getGhostCard().setVisible(false);
        		}
        		else if(clickReason == SHOWCARDS)
        			player.showCards(this);
    		}
    		return;
    	}
		// card must be in controlling player's hand by now
		if(clickReason == THROWAWAY && getCard().getStroke() == ThrowAwayColor){
//	    	ghostCard.setVisible(false);
			selectCard();
			return;
		}
		if(Player.getActivePlayer()!=Player.getControllingPlayer())
			return;
		// card must be in active, controlling player's hand by now
		switch(clickReason){
		case SELECT: case STEALCARD: 
			selectCard();
//	    	ghostCard.setVisible(false);
			break;
		case CHOOSECARD:
			if(getCard().getStroke() == ChoiceColor){
				player.cardChosen(this);
    	    	getGhostCard().setVisible(false);
			}
			break;
		default: 
			break;
		}
    }
	private void mouseEntered() {
		if(!(cardContainer instanceof PlayerCards))
			return;
    	Player player = ((PlayerCards) cardContainer).getPlayer();
    	if(player != Player.getControllingPlayer()){
    		if(frontUp || timeline.isAutoReverse())
    			getGhostCard().setVisible(true);
    	}
    	else{
    		switch(clickReason){
    		case THROWDIE: case AFGHAN: case STEALCARDS:
    			break;
    		case CHOOSECARD:
    			if(getCard().getStroke() == ChoiceColor)
        			getGhostCard().setVisible(true);
    			break;
			default:
    			getGhostCard().setVisible(true);
    			break;
    		}
    	}
	}
	private void mouseExited() {
		getGhostCard().setVisible(false);
	}
    
	private void selectCard(){
		System.out.println("selectCard");
		Player player = ((PlayerCards) cardContainer).getPlayer();
		if(player == Player.getControllingPlayer())
			Player.getNetworkController().sendMessage(new ChatMessage(ChatMessage.SELECTCARD,this.getCardID(),0,0));
//		ghostCard.setVisible(false);
		isMoving = 1;
		player.raiseNumberOfMovingCards();
		numberOfCardsMoving++;
		
//		getCard().setMouseTransparent(true);
		
		double rot = getCard().getRotate();
		double difX = 40 * Math.sin(rot*2*Math.PI / 360);
		double difY = 40 * Math.cos(rot*2*Math.PI / 360);
		//TODO ik denk dat timeline.reverse mogelijk is.
		if(player.getSelectedCards().contains(this)){
			difX = - difX;
		}
		else{
			difY = - difY;
		}
		timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(animationSpeed, new KeyValue (getCard().translateXProperty(), getCard().getTranslateX()+difX)));
		timeline.getKeyFrames().add(new KeyFrame(animationSpeed, new KeyValue (getCard().translateYProperty(), getCard().getTranslateY()+difY)));
		timeline.getKeyFrames().add(new KeyFrame(animationSpeed, new KeyValue (getCard().scaleXProperty(), 2.2-getCard().scaleXProperty().doubleValue())));
		timeline.getKeyFrames().add(new KeyFrame(animationSpeed, new KeyValue (getCard().scaleYProperty(), 2.2-getCard().scaleYProperty().doubleValue())));
		timeline.play();
		
	    timeline.setOnFinished(event -> selectionFinished());
	}
	private void selectionFinished() {
		System.out.println("select"+((PlayerCards) cardContainer).getPlayer().getNumberOfMovingCards());
		isMoving = 0;
		numberOfCardsMoving--;
		Player player =((PlayerCards) cardContainer).getPlayer(); 
		player.lowerNumberOfMovingCards();
		if(!player.getSelectedCards().contains(this)){ 
			player.selectCard(this);
		} else{ 
			player.deselectCard(this);
		}

		if(player.getNumberOfMovingCards()==0){
			player.selectionChanged(clickReason);
		}
//		// TODO als spelers kaarten moeten weggooien gaat dit mis... 
		if (numberOfCardsMoving == 0){
			Player.getActivePlayer().stoppedMoving();
		}
	}
	public void animateCard(){
//		getGhostCard().setStroke(MouseEnteredColor);
		timeline = new Timeline();

		timeline.setCycleCount(-1);
		timeline.setAutoReverse(true);
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(200),
				   new KeyValue (getCard().scaleXProperty(), 2.3-getCard().scaleXProperty().doubleValue())));
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(200),
				   new KeyValue (getCard().scaleYProperty(), 2.3-getCard().scaleYProperty().doubleValue())));
		timeline.play();
	}
	public void resetAnimation(){
//		getGhostCard().setStroke(DefaultColor);
//		System.out.println("Card. resetAni");
		if(timeline.isAutoReverse()){
//			getCard().setMouseTransparent(true);
			timeline.jumpTo(Duration.millis(0));
			timeline.stop();
			timeline = new Timeline();
		}
	}
	public void moveCardTo(double x, double y, double rotation){
		numberOfCardsMoving++;
		if(previousCardContainer != cardContainer){
			if(frontUp && !cardContainer.isFrontUp())
				isMoving = 2;
			else if(!frontUp && cardContainer.isFrontUp()){
				getCard().flip(true);
				frontUp = true;
				isMoving = 1;
			}
		}
//		getCard().setMouseTransparent(true);
//		if(visable == false){
//			// makes the back image show as soon as it has finished moving
//			isMoving = 2;
//		} else{
//			if(!frontUp){
//				getCard().flip(true);
//				frontUp = true;
//			}
//			isMoving = 1;
//		}
		timeline = new Timeline();
//		this.setMouseTransparent(true);
		timeline.getKeyFrames().add(new KeyFrame(animationSpeed,
				   new KeyValue (getCard().translateXProperty(), x)));
		timeline.getKeyFrames().add(new KeyFrame(animationSpeed,
				   new KeyValue (getCard().translateYProperty(), y)));
		timeline.getKeyFrames().add(new KeyFrame(animationSpeed,
				   new KeyValue (getCard().rotateProperty(), rotation)));
		timeline.getKeyFrames().add(new KeyFrame(animationSpeed,
				   new KeyValue (getCard().scaleXProperty(), 1)));
		timeline.getKeyFrames().add(new KeyFrame(animationSpeed,
				   new KeyValue (getCard().scaleYProperty(), 1)));
		
	    timeline.setOnFinished(event ->{
	    	Player.getPlayField().moveCardToFront(getCard());
	    	previousCardContainer = cardContainer;
//			Player.getPlayField().remove(getCard());
//			Player.getPlayField().add(getCard());
			if(isMoving == 2){
				getCard().flip(false);
				frontUp = false;
			}
//			if(((PlayerCards)cardContainer).getPlayer() == Player.getControllingPlayer())
//				getCard().setMouseTransparent(false);
			isMoving = 0;
			numberOfCardsMoving--;
			if(numberOfCardsMoving == 0)
				Player.getActivePlayer().stoppedMoving();
			// TODO set invisible if in discard stack 
	    });
		timeline.play();
	}
	public void flipCard(boolean frontUp){
		this.frontUp = frontUp;
		if(frontUp){
			getCard().flip(true);
//			setMouseTransparent(false);
		} else{
			getCard().flip(false);
//			getCard().setMouseTransparent(true);
		}

	}
	public int isMoving() {
		return isMoving;
	}
	public boolean isRegular() {
		return isRegular;
	}
	public boolean frontUp() {
		return frontUp;
	}
	public double getOffcenterX() {
		return offcenterX;
	}
	public void setOffcenterX(double offcenterX) {
		this.offcenterX = offcenterX;
	}
	public double getOffcenterY() {
		return offcenterY;
	}
	public void setOffcenterY(double offcenterY) {
		this.offcenterY = offcenterY;
	}
	public double getRotation() {
		return rotation;
	}
	public void setRotation(double rotation) {
		this.rotation = rotation;
	}
//	public double getScale() {
//		return scale;
//	}
//	public void setScale( double scale) {
////		this.scale=scale;
//		setScaleX(scale);
//		setScaleY(scale);
//	}
	public int getSubType() {
		return subType;
	}
	
	public String getName() {
		return cardName;
	}
	
	public static int getClickReason() {
		return clickReason;
	}
	
	public static void setClickReason(int clickReason) {
		Card.clickReason = clickReason;
	}
	
	public static int getNumberOfCardsMoving() {
		return numberOfCardsMoving;
	}
	
	public CardContainer getCardContainer() {
		return cardContainer;
	}
	
	public void setFrontUp(boolean frontUp) {
		this.frontUp = frontUp;
		if(frontUp){
			getCard().flip(true);
		}
		else{
			getCard().flip(false);
		}
	}
	
	public static Color getChoicecolor() {
		return ChoiceColor;
	}
	
	public static Color getDefaultcolor() {
		return DefaultColor;
	}
	
	public static Duration getAnimationSpeed() {
		return animationSpeed;
	}
	
	public static void setAnimationSpeed(Duration animationSpeed) {
		Card.animationSpeed = animationSpeed;
	}
	
	public void setStrokeColor(Color color){
		getCard().setStroke(color);
	}
	
	public Paint getStrokeColor(){
		return getCard().getStroke();
	}
	
	public static Color getThrowAwayColor() {
		return ThrowAwayColor;
	}
	
//	public void setMouseTransparent(boolean value){
//		getCard().setMouseTransparent(value);
//	}
	
	public void setTranslateX(double x){
		getCard().setTranslateX(x);
	}
	
	public void setTranslateY(double y){
		getCard().setTranslateY(y);
	}
	
	public void setVisible(boolean value){
		getCard().setVisible(value);
	}
	
	public double getWidth(){
		return getCard().getWidth();
	}
	
	public double getHeight(){
		return getCard().getHeight();
	}
	
	public CardRectangle getCard() {
		return cardRectangle;
	}
	
	public static void selectCard(int cardID){
		cards.get(cardID).selectCard();
	}
	
	public int getCardID() {
		return cardID;
	}
	
	public static Card getCard(int index){
		return cards.get(index);
	}
	
	public static List<Card> getCards() {
		return cards;
	}
	
	public static Color getMouseenteredcolor() {
		return MouseEnteredColor;
	}
	
	public void setPreviousCardContainer(CardContainer container){
		previousCardContainer = container;
	}
	public void setCardContainer(CardContainer container){
		cardContainer = container;
	}
	public GhostCard getGhostCard() {
		return ghostCard;
	}
}
