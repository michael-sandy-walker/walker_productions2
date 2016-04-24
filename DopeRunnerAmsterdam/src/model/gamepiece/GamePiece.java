package model.gamepiece;


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.util.Duration;
import model.player.Player;
import model.tramstop.GamePieceContainer;

public class GamePiece{
	protected Timeline timeline = new Timeline();
	protected GamePieceContainer gamePieceContainer;	
	protected static int movingGamePieces = 0;
	protected static Duration animationSpeed;
	private GamePieceContainer gamePieceBase;
	private boolean isChit;
	private final Group node = new Group();
	public GamePiece(boolean isChit){
		getNode().setMouseTransparent(true);
		this.isChit = isChit;
	}
	public void moveTo(double x, double y){
		movingGamePieces++;
		System.out.println("movePiece: "+this.getClass());
		timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(animationSpeed, new KeyValue (getNode().translateXProperty(), x)));
		timeline.getKeyFrames().add(new KeyFrame(animationSpeed, new KeyValue (getNode().translateYProperty(), y)));
	    timeline.setOnFinished(event -> {
			movingGamePieces--;
			if(movingGamePieces == 0){
				Player.getActivePlayer().stoppedMoving();
			}
	    });
		timeline.play();
	}
	
	public void moveToContainer(GamePieceContainer gamePieceContainerNew){
		if(gamePieceContainer != gamePieceContainerNew){
			gamePieceContainer.removeGamePiece(this);
			gamePieceContainerNew.addGamePiece(this);
		} else{
//			System.out.println("Hallo");
		}
	}
	
	public void moveToBase(){
		gamePieceContainer.removeGamePiece(this);
		gamePieceBase.addGamePiece(this);
	}
	
	public void addRotation(int rotation){
		timeline.getKeyFrames().add(new KeyFrame(animationSpeed, new KeyValue (getNode().rotateProperty(), rotation)));
	}
	
	public GamePieceContainer getGamePieceContainer() {
		return gamePieceContainer;
	}

	public void setContainer(GamePieceContainer gamePieceContainer) {
		this.gamePieceContainer = gamePieceContainer;
//		
//		gamePieceContainer.addGamePiece(this);
	}
	
	public static int getMovingGamePieces() {
		return movingGamePieces;
	}

	public static Duration getAnimationSpeed() {
		return animationSpeed;
	}

	public static void setAnimationSpeed(Duration animationSpeed) {
		GamePiece.animationSpeed = animationSpeed;
	}
	
	public GamePieceContainer getGamePieceBase() {
		return gamePieceBase;
	}
	
	public void setGamePieceBase(GamePieceContainer gamePieceBase) {
		this.gamePieceBase = gamePieceBase;
		this.gamePieceContainer = gamePieceBase;
		gamePieceContainer.addGamePiece(this);
		getNode().setTranslateX(gamePieceBase.getPlaceOnPlayfieldX());
		getNode().setTranslateY(gamePieceBase.getPlaceOnPlayfieldY());
	}
	
	public boolean isChit() {
		return isChit;
	}
	public Group getNode() {
		return node;
	}
	public double getScale(){
		return node.getScaleX();
	}
//	public void setOffcenterX(double offcenterX) {
//		this.offcenterX = offcenterX;
//	}
//	
//	public void setOffcenterY(double offcenterY) {
//		this.offcenterY = offcenterY;
//	}
}
