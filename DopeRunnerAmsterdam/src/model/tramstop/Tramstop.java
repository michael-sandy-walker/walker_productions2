package model.tramstop;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;
import model.player.Player;
import model.player.PlayerHuman;
public class Tramstop extends GamePieceContainer{
	private List<List<Tramstop>> regularConnections = new ArrayList<List<Tramstop>>();
	private List<List<Tramstop>> advancedConnections = new ArrayList<List<Tramstop>>();
	private String type;
	private Timeline timeline = new Timeline();
	private static Tramstop tramstopStart = null;
	private static String clickReason = "";
	private Circle circle;
	private boolean dealMade = false;
	private int tramstopID;
	private static List<Tramstop> tramstops = new ArrayList<Tramstop>();
	public Tramstop(String type, double placeOnPlayfieldX, double placeOnPlayfieldY){
		super(placeOnPlayfieldX,placeOnPlayfieldY);
		this.tramstopID = tramstops.size();
		tramstops.add(this);
		this.type = type;
//		this.placeOnPlayfieldX = placeOnPlayfieldX;
//		this.placeOnPlayfieldY = placeOnPlayfieldY;
		double tramstopSize;
		if(type.equals("coffeeshop")){
			tramstopSize = 16;
		} else if(type.equals("regular")){
			tramstopSize = 12;
		} else if(type.equals("start")){
			setTramstopStart(this);
			tramstopSize = 24;
		} else{ // goalWhite or goalBlack
			tramstopSize = 22;
		}
		circle = new Circle(tramstopSize);
		circle.setFill(Color.TRANSPARENT);
		circle.setOnMouseClicked(new EventHandler<MouseEvent>()
	    {
	        @Override
	        public void handle(MouseEvent t) {
		    	System.out.println("Click");
	        	selectTramstop();
	        }
	    });
		circle.setStroke(Color.BLACK);
		circle.setStrokeType(StrokeType.OUTSIDE);
		circle.setStrokeLineCap(StrokeLineCap.BUTT);
		circle.getStrokeDashArray().addAll(8.0,8.0);
		circle.setStrokeWidth(3.0);
		circle.setVisible(false);
		circle.setTranslateX(placeOnPlayfieldX);
		circle.setTranslateY(placeOnPlayfieldY);
	}
	
	public void selectTramstop(){
//        for (TramstopListener tl : listeners){
            ((PlayerHuman) Player.getActivePlayer()).tramstopSelected(this, clickReason);
//        }
	}
	
	public void activate(){
		circle.setVisible(true);
		circle.setMouseTransparent(false);
		if(hasPolice() || (getOwner()!=null && getOwner() != Player.getActivePlayer())){
			circle.setStroke(Color.RED);
			circle.setStrokeWidth(5);
		}
		timeline = new Timeline();
		circle.setMouseTransparent(false);
		timeline.setCycleCount(-1);
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000), new KeyValue (circle.strokeDashOffsetProperty(), 16)));
		timeline.play();
	}
	public void deactivate(){
		circle.setMouseTransparent(true);
		circle.setVisible(false);
		circle.setStroke(Color.BLACK);
		circle.setStrokeWidth(3);
		if(timeline.getCycleCount() == -1){
			timeline.jumpTo(Duration.millis(0));
			timeline.stop();
		}
	}
	
	public String getType() {
		return type;
	}
	
	public static String getClickReason() {
		return clickReason;
	}
	
	public static void setClickReason(String clickReason) {
		Tramstop.clickReason = clickReason;
	}
	
	public static Tramstop getTramstopStart() {
		return tramstopStart;
	}
	
	public static void setTramstopStart(Tramstop tramstopStart) {
		Tramstop.tramstopStart = tramstopStart;
	}
	
	public List<Tramstop> getRegularConnections(int cardSubtype) {
		return new ArrayList<Tramstop>(regularConnections.get(cardSubtype));
	}
	
	public List<Tramstop> getAdvancedConnections(int cardSubtype) {
		return new ArrayList<Tramstop>(advancedConnections.get(cardSubtype));
	}
	
	public void addRegularConnection(int cardSubtype, Tramstop tramstop) {
		if( !tramstop.equals(this) && !regularConnections.get(cardSubtype).contains(tramstop)){
			regularConnections.get(cardSubtype).add(tramstop);
		}
	}
	
	public List<Tramstop> getOneStepConnections(int cardSubtype) {
		ArrayList<Tramstop> connections = new ArrayList<Tramstop>();
		for(Tramstop tramstop:regularConnections.get(cardSubtype)){
			if(regularConnections.get(6).contains(tramstop)){
				connections.add(tramstop);
			}
		}
		return connections;
	}
	
	public List<Tramstop> getConnections(int cardSubtype, boolean isRegular) {
		if(isRegular){
			return regularConnections.get(cardSubtype);
		} else{
			return new ArrayList<Tramstop>(advancedConnections.get(cardSubtype));
		}
	}
	
	public void addAdvancedConnection(int cardSubtype, Tramstop tramstop) {
		if(!tramstop.equals(this) && !advancedConnections.get(cardSubtype).contains(tramstop)){
			advancedConnections.get(cardSubtype).add(tramstop);
		}
	}
	
	public void initConnections(int numberOfRegularCards, int numberOfAdvancedCards){
		for(int index = 0; index<numberOfRegularCards;index++){
			regularConnections.add(new ArrayList<Tramstop>());
		}
		for(int index = 0; index<numberOfAdvancedCards;index++){
			advancedConnections.add(new ArrayList<Tramstop>());
		}
	}
	
	public Circle getCircle() {
		return circle;
	}

	public boolean isDealMade() {
		return dealMade;
	}

	public void setDealMade(boolean dealMade) {
		this.dealMade = dealMade;
	}

	public int getTramstopID() {
		return this.tramstopID;
	}
	
	public static List<Tramstop> getTramstops(){
		return tramstops;
	}
	
	public static Tramstop getTramstop(int index){
		return tramstops.get(index);
	}
}
