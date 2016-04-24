package model.player;


import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.gamepiece.GamePiece;
import model.player.Player;

public class Gangster extends GamePiece{
	private Player player;
	public Gangster(Player player){
		super(true);
		double size = 18;
		this.player = player;
		Circle circle = new Circle(size);
		circle.setFill(player.getColor());
		circle.setOpacity(0.5);
		circle.setStroke(Color.BLACK);
		getNode().getChildren().add(circle);
	}
	public Player getPlayer() {
		return player;
	}
}
