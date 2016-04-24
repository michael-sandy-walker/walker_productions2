package model.cards;

import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

public class CardRectangle extends Rectangle{
	private ImagePattern imageFront;
	private ImagePattern imageBack;
	protected boolean isActivated = false;
	public CardRectangle(double width, double height, ImagePattern imageFront, ImagePattern imageBack){
		super(width,height, imageBack);
		setArcHeight(10);
		setArcWidth(10);
		setStroke(Color.BLACK);
		setStrokeType(StrokeType.OUTSIDE);
		setStrokeWidth(5.0);
		this.imageBack = imageBack;
		this.imageFront = imageFront;
	}
	public void flip(boolean frontUp){
		if(frontUp)
			setFill(imageFront);
		else
			setFill(imageBack);
	}
}
