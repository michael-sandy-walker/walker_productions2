package model.gamepiece;

import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.ArcTo;
public class Police extends GamePiece {
	public Police(Color color) {
		super(false);
		double size = 16;
		
		MoveTo moveTo = new MoveTo(0.0,-size*1.5);

		LineTo lineToDown = new LineTo(- size/2, 0.0);

		ArcTo arcTo = new ArcTo();
		arcTo.setX(size/2);
		arcTo.setY(0.0);
		arcTo.setRadiusX(size/2);
		arcTo.setRadiusY(size/4);
		arcTo.setSweepFlag(false);
		ClosePath closePath = new ClosePath();
		
		Path path = new Path();
		path.getElements().addAll(moveTo, lineToDown, arcTo,closePath);
		
		path.setFill(color);
		path.setStroke(Color.BLACK);
		
		getNode().getChildren().add(path);
	}
}
