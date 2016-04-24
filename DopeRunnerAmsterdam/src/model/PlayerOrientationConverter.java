package model;

import javafx.geometry.Point2D;

public interface PlayerOrientationConverter {
	default Point2D convertToPlayerOrientation(double x, double y, double orientation){
		double angle = Math.toRadians(orientation);
		double sin = Math.sin(angle);
		double cos = Math.cos(angle);
		double X = x * cos - y * sin;
		double Y = x * sin + y * cos;
		return new Point2D(X,Y);
	}
}
