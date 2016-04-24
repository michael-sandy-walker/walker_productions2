package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import model.player.Player;

public class Die extends Group{
	private String[] results;
	private Rectangle side;
//	private Random random = new Random();
	private boolean isRolling = false;
	private Timer timer;
	private List<ImagePattern> sides = new ArrayList<ImagePattern>();
//	private static List<DieListener> listeners = new ArrayList<DieListener>();
	private final double positionX;
	private final double positionY;
	public Die(double positionX, double positionY, String[] results){
		this.positionX = positionX;
		this.positionY = positionY;
		setTranslateX(positionX);
		setTranslateY(positionY);
		for(String result: results){
			sides.add(new ImagePattern(new Image("/die/img/"+result+".png")));
		}
		this.results = results;
//		this.sides = sides;
		Rectangle background = new Rectangle(-20,-20,40,40);
		background.setArcHeight(5);
		background.setArcWidth(5);
		background.setFill(Color.GREEN);
	    int r = Player.getRandom().nextInt(6);
//	    result = results[r];
		side = new Rectangle(-20,-20,40,40);
		side.setFill(sides.get(r));
		side.setMouseTransparent(true);
	    side.setOnMouseClicked(event ->
	    {
        	if(isRolling()){
//        		String result = getNextResult();
//	        	isRolling = false;
//	        	timer.cancel();
//	        	for(DieListener dl: listeners){
        		side.setMouseTransparent(true);
        		Player.getControllingPlayer().handleDie();
//	        	}
        	}
	    });
	    this.getChildren().addAll(background,side);
	}
	
	public String computerThrow(){
		// TODO make an animation
		return getNextResult();
	}
	
	public String getNextResult(){
		int r = Player.getRandom().nextInt(6);
    	side.setFill(sides.get(r));
    	isRolling = false;
    	System.out.println("Cancel Die");
    	timer.cancel();
    	timer.purge();
		return results[r];
	}
	
//	public static void addListener(DieListener toAdd){
//		listeners.add(toAdd);
//	}
//	public static void setListener(DieListener toAdd) {
//		listeners = new ArrayList<DieListener>();
//    	listeners.add(toAdd);
//    }

	public void activateDie(){
		System.out.println("Dobbel");
		if(Player.getActivePlayer() == Player.getControllingPlayer())
			side.setMouseTransparent(false);
//		else
//			side.setMouseTransparent(true);
		isRolling = true;
		timer = new Timer();
      	int delay=0; // msec
      	int framelength = 150; // msec
      	Random random = new Random();
      	timer.schedule(new TimerTask()
      	{
      		public void run()
      		{
      			int r = random.nextInt(6);
      			side.setFill(sides.get(r));
  			}
  		},delay,framelength);
	}
	
	public void moveDieTo(double x, double y, double rotation){
		isRolling = true;
		System.out.println("Die x: "+x+" y: "+y);
//		Player.getPlayField().remove(this);
//		Player.getPlayField().add(this);
		Duration animationSpeed = Player.getAnimationSpeed(); 
		Timeline timeline = new Timeline();
//		this.setMouseTransparent(true);
		timeline.getKeyFrames().add(new KeyFrame(animationSpeed,
				   new KeyValue (this.translateXProperty(), x)));
		timeline.getKeyFrames().add(new KeyFrame(animationSpeed,
				   new KeyValue (this.translateYProperty(), y)));
		timeline.getKeyFrames().add(new KeyFrame(animationSpeed,
				   new KeyValue (this.rotateProperty(), rotation)));
		
	    timeline.setOnFinished(event ->{
			isRolling = false;
			Player.getActivePlayer().stoppedMoving();
	    });
		timeline.play();
	}
	
	public boolean isRolling() {
		return isRolling;
	}
	
	public void deactivateDie(){
		
	}
	public double getPositionX() {
		return positionX;
	}
	public double getPositionY() {
		return positionY;
	}
}
