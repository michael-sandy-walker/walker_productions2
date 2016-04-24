package model;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;
import model.cards.Deck;
import model.player.Player;
import model.player.PlayerRectangle;

public class Arrow extends Polygon implements PlayerOrientationConverter{
	Timeline timeline;
	private Player listener;
	private static String clickReason = "";
	public Arrow(){
		super();
		drawArrow();
	}
	private void drawArrow(){
		getPoints().addAll(new Double[]{0.0,0.0,20.0,15.0,12.5,15.0,12.5,30.0,-12.5,30.0,-12.5,15.0,-20.0,15.0,0.0,0.0});
		setTranslateY(-30);
		setFill(Color.BLACK);
		setMouseTransparent(true);
		setVisible(false);
		timeline = new Timeline();
		timeline.setCycleCount(-1);
		timeline.setAutoReverse(true);
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(200), new KeyValue (translateYProperty(), -35)));
	    setOnMouseClicked(event -> {
	    	System.out.println("ArrowClicked "+ clickReason);
			if(getParent() instanceof PlayerRectangle){
				listener.playerArrowClicked(((PlayerRectangle) (getParent())).getPlayer(), clickReason);
			} else if(getParent() instanceof Deck){
				Player.getActivePlayer().deckArrowClicked((Deck) getParent(), clickReason);
			}
        	deactivateArrow();
        	setClickReason("");
	    });
//		activateArrow();
	}

	public void setListener(Player player) {
    	listener = player;
	}

	public void activateArrow(String reason){
		System.out.println(this.getTranslateX());
		System.out.println(this.getTranslateY());
		setMouseTransparent(false);
		setVisible(true);
		setClickReason(reason);
		System.out.println("ActivateArrow"+clickReason);
		timeline.play();
	}
	public void deactivateArrow(){
		setMouseTransparent(true);
		setVisible(false);
//		setClickReason("");
		timeline.jumpTo(Duration.millis(0));
		timeline.stop();
	}
	public void showArrow(){
		setVisible(true);
	}
	public static String getClickReason() {
		return clickReason;
	}
	public static void setClickReason(String clickReason) {
		Arrow.clickReason = clickReason;
	}
}
