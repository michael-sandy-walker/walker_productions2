package model.tramstop;

import java.util.ArrayList;
import java.util.List;

import model.gamepiece.GamePiece;
import model.gamepiece.Goal;
import model.gamepiece.HashToken;
import model.gamepiece.Police;
import model.player.Gangster;
import model.player.Player;

public class GamePieceContainer {
	protected static final int[] radius = {0,0,12,12,15,18,21,24,27};
	protected double placeOnPlayfieldX;
	protected double placeOnPlayfieldY;
	protected List<GamePiece> gamePieces = new ArrayList<GamePiece>();
	protected List<GamePiece> chits = new ArrayList<GamePiece>();
	public GamePieceContainer(double placeOnPlayfieldX, double placeOnPlayfieldY) {
		super();
		this.placeOnPlayfieldX = placeOnPlayfieldX;
		this.placeOnPlayfieldY = placeOnPlayfieldY;
	}
	
	public double getPlaceOnPlayfieldX() {
		return placeOnPlayfieldX;
	}
	
	public double getPlaceOnPlayfieldY() {
		return placeOnPlayfieldY;
	}
	
	public void addGamePiece(GamePiece piece){
		piece.setContainer(this);
		if(piece.isChit()){
			chits.add(piece);
			piece.moveTo(placeOnPlayfieldX, placeOnPlayfieldY);
//			if(piece instanceof Gangster){
//				owner = ((Gangster) piece).getPlayer();
//			}
		} else{
			this.gamePieces.add((GamePiece) piece);
			if(piece instanceof Police){
				System.out.println("Politie");
			}
			recalculate();
		}
	}
	
	public void removeGamePiece(GamePiece piece){
		if(piece.isChit()){ 
			chits.remove(piece);
		} else{
			this.gamePieces.remove(piece);
			recalculate();
		}
	}
	
	public GamePiece getPolice(){
		for(GamePiece gamePiece : gamePieces){
			if(gamePiece instanceof Police){
				return gamePiece;
			}
		}
		return null;
	}
	
	public boolean hasPolice(){
		for(GamePiece gamePiece : gamePieces){
			if(gamePiece instanceof Police){
				return true;
			}
		}
		return false;
	}
	public GamePiece getGangster(){
		for(GamePiece gamePiece:chits){
			if(gamePiece instanceof Gangster){
				return gamePiece;
			}
		}
		return null;
	
	}
	public Player getOwner(){
		for(GamePiece gamePiece:chits){
			if(gamePiece instanceof Gangster){
				return ((Gangster) gamePiece).getPlayer();
			}
		}
		return null;
	}
	public boolean hasOwner(){
		for(GamePiece gamePiece:chits){
			if(gamePiece instanceof Gangster){
				return true;
			}
		}
		return false;
	}
	
	public Goal getGoal(){
		for(GamePiece gamePiece:chits){
			if(gamePiece instanceof Goal){
				return ((Goal) gamePiece);
			}
		}
		return null;
	}
	public boolean hasGoal(){
		for(GamePiece gamePiece:chits){
			if(gamePiece instanceof Goal){
				return true;
			}
		}
		return false;
	}
	
	public int getNumberOfHashTokens(){
		int num = 0;
		for (GamePiece gamePiece : gamePieces){
			if(gamePiece instanceof HashToken){
				num++;
			}
		}
		return num;
	}
	public void resetHashTokens(){
		int index =0;
		while(index<gamePieces.size()){
			if(gamePieces.get(index) instanceof HashToken){
				gamePieces.get(index).moveToBase();
			} else index++;
		}
	}
	
	private void recalculate(){
		int total = gamePieces.size();
		int rad;
		if(total<radius.length){
			rad= radius[total];
		}else{
			rad= radius[radius.length-1];
		}
		int i=0;
		for(GamePiece gamePiece : gamePieces){
			if(total == 1){
				System.out.println(gamePiece == null);
				gamePiece.moveTo(placeOnPlayfieldX, placeOnPlayfieldY);
			}
			else{
				gamePiece.moveTo(placeOnPlayfieldX + rad * Math.sin(i * 2 * Math.PI / total), placeOnPlayfieldY +rad * Math.cos(i * 2 * Math.PI / total));
			}
			i++;
		}
	}

	public List<GamePiece> getGamePieces() {
		return gamePieces;
	}
	
	public List<GamePiece> getChits() {
		return chits;
	}
	
	
}
