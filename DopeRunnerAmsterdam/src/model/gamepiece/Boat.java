package model.gamepiece;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Boat extends GamePiece {
	private static Boat boat;
	public Boat(){
		super(true);
		boat=this;
		Rectangle boat = new Rectangle(36,36);
		boat.setFill(new ImagePattern(new Image("Boat.png")));
		getNode().getChildren().add(boat);
//		size = 36;
	}
	public static Boat getBoat() {
		return boat;
	}	
}
