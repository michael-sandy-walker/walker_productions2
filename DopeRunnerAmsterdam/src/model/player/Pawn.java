package model.player;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import model.gamepiece.GamePiece;

public class Pawn extends GamePiece {
	Pawn(Color color) {
		super(false);
		double size = 10.0;
		Ellipse base = new Ellipse(0,0,size,size);
		base.setFill(color);
		base.setStroke(Color.BLACK);
//		Ellipse top = new Ellipse(0,0,0,0);
		Ellipse top = new Ellipse(0,0,(size*2)/3,(size*2)/3);
		top.setFill(color);
		top.setStroke(Color.BLACK);
		top.setTranslateY(-size);
		getNode().getChildren().addAll(base,top);
	}
	public void growPawn(){
		movingGamePieces++;
		timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(animationSpeed, new KeyValue(getNode().scaleXProperty(), 1.5)));
		timeline.getKeyFrames().add(new KeyFrame(animationSpeed, new KeyValue(getNode().scaleYProperty(), 1.5)));
		timeline.setOnFinished(event -> movingGamePieces--);
		timeline.play();
	}
	public void resetPawn(){
		if(getNode().getScaleX() == 1) return;
		movingGamePieces++;
		timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(animationSpeed, new KeyValue(getNode().scaleXProperty(), 1)));
		timeline.getKeyFrames().add(new KeyFrame(animationSpeed, new KeyValue(getNode().scaleYProperty(), 1)));
		timeline.setOnFinished(event -> movingGamePieces--);
		timeline.play();
	}
}
