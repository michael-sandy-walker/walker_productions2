package model.player;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import model.Arrow;
import model.PlayerOrientationConverter;
import model.cards.Card;
import model.gamepiece.GamePiece;
import model.gamepiece.Goal;
import model.tramstop.GamePieceContainer;

public class PlayerRectangle extends Group implements PlayerOrientationConverter{
//	private Arrow arrow = new Arrow();
	private Arrow arrow = new Arrow();
	private Text arrowText; 
	private final Player player;
	private GamePieceContainer gangsterBase;
	private Text gangsterText;
	private double[] goalPlacesX = new double[8];
	private double[] goalPlacesY = new double[8];
	private PlayerCards playerCards;
	
//	private double orientation;
	private List<Goal> goals = new ArrayList<Goal>();
	public PlayerRectangle(Player player, Color color, double positionX, double positionY, double width, double height, double orientation){
		super();
		arrow.setListener(player);
		this.player = player;
		boolean isFrontUp = (player.getPlayerID() == Player.getControllingPlayerId());
		playerCards = new PlayerCards(player, positionX,positionY, width, height, orientation, isFrontUp);
		Rectangle rect = new Rectangle(width, height);
		rect.setFill(color);
		rect.setMouseTransparent(true);
		arrow.setTranslateX(width/2);
//		arrowText.setRotate(-orientation);
		
		arrow.setTranslateY(-30);
		if(orientation == 0){
			arrowText = new Text("0");
			arrowText.setFont(new Font("SweetLeaf", 25));
			arrowText.setFill(Color.WHITE);
			arrowText.setTranslateX(width/2 - 5);
			arrowText.setTranslateY(-3);
//			this.getChildren().add(arrowText);
		}
		getChildren().addAll(rect,arrow);
		getTransforms().add(new Rotate(orientation,0,0));
		setTranslateX(positionX);
		setTranslateY(positionY);

		for(int x=0;x<2;x++){
			for( int y = 0; y<4;y++){
				Point2D point = convertToPlayerOrientation(width + 40 * x, 20 + 40 * y, orientation);
				goalPlacesX[4*x+y] = positionX + point.getX();
				goalPlacesY[4*x+y] = positionY + point.getY();
			}
		}
		double gangsterX = -20;
		double gangsterY = 20;
		Point2D point = convertToPlayerOrientation(gangsterX, gangsterY, orientation);
		gangsterBase = new GamePieceContainer(point.getX()+positionX, point.getY()+ positionY);
		for(int i=0;i<12;i++){
			Gangster gangster = new Gangster(player);
			gangster.setGamePieceBase(gangsterBase);
		}
		gangsterText = new Text(""+gangsterBase.getChits().size());
		gangsterText.setFont(new Font("SweetLeaf", 25));
		gangsterText.setFill(Color.BLACK);
		gangsterText.setTextAlignment(TextAlignment.RIGHT);
		gangsterText.setTranslateX(gangsterX-6);
		gangsterText.setTranslateY(gangsterY+40);
		
		getChildren().add(gangsterText);
	}
	
	public void stopAnimatingCards(){
		playerCards.stopAnimatingCards();
	}
	
	public List<Card> getCards() {
		return playerCards.getCards();
	}
	
	public List<Card> getCards(boolean isRegular) {
		return playerCards.getCards(isRegular);
	}
	

	public List<Card> getSelectedCards() {
		return playerCards.getSelectedCards();
	}
	
	protected void resetSelectedCards(){
		playerCards.resetSelectedCards();
	}
	
	public Card stealCard(boolean isRegular){
		return playerCards.stealCard(isRegular);
	}
	
	public void addCard(Card card){
		playerCards.addCard(card);
	}
	
	public void removeCard(Card card){
		playerCards.removeCard(card);
	}
	
	public void flipCards(boolean frontUp){
		playerCards.flipCards(frontUp);
	}
	
	public void resetCardAnimation(){
		playerCards.resetCardAnimation();
	}
	
	public void addGoal(Goal goal){
		goal.moveTo(goalPlacesX[goals.size()], goalPlacesY[goals.size()]);
		goals.add(goal);
	}
	
	
	public void resetGangsterText(){
		gangsterText.setText(""+gangsterBase.getChits().size());
	}
	
	public void activateArrow(String reason){
		arrow.activateArrow(reason);
	}
	
	public void deactivateArrow(){
		arrow.deactivateArrow();
	}
	
	public Arrow getArrow() {
		return arrow;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public GamePiece getGangster(){
		return gangsterBase.getGangster();
	}
	
	public List<GamePiece> getGangsters(){
		return gangsterBase.getChits();
	}
	
	public List<Goal> getGoals() {
		return goals;
	}

	public void selectCard(Card card) {
		playerCards.selectCard(card);
	}
	
	public void deselectCard(Card card) {
		playerCards.deselectCard(card);
	}

}
