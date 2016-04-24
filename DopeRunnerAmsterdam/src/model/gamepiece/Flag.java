package model.gamepiece;

import javafx.scene.paint.Color;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import model.player.Player;

public class Flag extends GamePiece{
	private Player player;
//	private PathElement curve;
	public Flag(Player player){
		super(true);
		Color color = player.getColor();
		double height = 36.0;
		double width = 32.0;
		
		Line flagPost = new Line(0.0,0.0,0.0,-height);
		flagPost.setStrokeWidth(3);
		
		Path path = new Path();
		path.getElements().add(new MoveTo(0.0,-height));
		
		double[] X = new double[]{width/3, 2*width/3, width};
		double[] Y = new double[]{-height, -1 * height / 2};
		boolean upsideDown = true;
		for(int i =0;i<2;i++){
			double y=Y[i];
			for(double x : X){
				ArcTo arcTo = new ArcTo();
				arcTo.setLargeArcFlag(false);
				arcTo.setRadiusX(width/3);
				arcTo.setRadiusY(width/3);
				arcTo.setX(x);
				arcTo.setY(y);
				arcTo.setSweepFlag(upsideDown);
				path.getElements().add(arcTo);
				upsideDown = !upsideDown;
			}
			if(i==0) path.getElements().add(new LineTo(width,Y[1]));
			X = new double[]{2*width/3, width/3, 0};
		}
		path.getElements().add(new ClosePath());

		path.setFill(color);
		path.setStroke(Color.BLACK);
		getNode().getChildren().addAll(path, flagPost);
	}
	public Player getPlayer() {
		return player;
	}
	
}
