package model.gamepiece;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class HashToken extends GamePiece{
	public HashToken(){
		super(false);
		Circle circle = new Circle(9);
		circle.setFill(Color.BROWN);
		circle.setStroke(Color.BLACK);
		getNode().getChildren().add(circle);
	}
}
