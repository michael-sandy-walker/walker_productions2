package model.gamepiece;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Ellipse;

public class Goal extends GamePiece {
	private String name;
	private Ellipse ellipse;
	private boolean isWhite;
	public Goal(String name, boolean isWhite) {
		super(true);
		this.isWhite = isWhite;
		double size = 20.0;
		Ellipse backSide = new Ellipse(0,0,size,size);
		if(isWhite){
			backSide.setFill(Color.WHITE);
		} 
		else{
			backSide.setFill(Color.BLACK);
		}
		
		ellipse = new Ellipse(0,0,size*0.95,size*0.95);
		ellipse.setFill(new ImagePattern(new Image("/chits/img/"+name+".png")));
		getNode().getChildren().addAll(backSide,ellipse);
		this.name = name;
	}
	public boolean isWhite(){
		return isWhite;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void flipGoal(){
		ellipse.setVisible(!ellipse.isVisible());
	}
	public boolean isFrontUp(){
		return(ellipse.isVisible());
	}
}
