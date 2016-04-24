package model.cards;

import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

public class GhostCard extends CardRectangle{
	private CardRectangle bindingCard;
	public GhostCard(double width, double height, ImagePattern imageFront, ImagePattern imageBack, CardRectangle bindingCard) {
		super(width, height, imageFront, imageBack);
		setStroke(Color.DARKGRAY);
		this.bindingCard = bindingCard;
		fillProperty().bind(this.bindingCard.fillProperty());
		translateXProperty().bind(this.bindingCard.translateXProperty());
		translateYProperty().bind(this.bindingCard.translateYProperty());
		rotateProperty().bind(this.bindingCard.rotateProperty());
		scaleXProperty().bind(this.bindingCard.scaleXProperty());
		scaleYProperty().bind(this.bindingCard.scaleYProperty());
		setMouseTransparent(true);
		setVisible(false);
	}
	
}
