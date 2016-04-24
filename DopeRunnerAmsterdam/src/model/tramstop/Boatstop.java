package model.tramstop;

import java.util.ArrayList;
import java.util.List;

public class Boatstop extends Tramstop {
	
	private final double offsetX;
	private final double offsetY;
	
	private Boatstop nextBoatStop = null;
	private final boolean isWhite;
	private static List<Boatstop> boatstops = new ArrayList<Boatstop>();
	public Boatstop(String type, double placeOnPlayfieldX, double placeOnPlayfieldY, double offsetX, double offsetY, boolean isWhite) {
		super(type, placeOnPlayfieldX, placeOnPlayfieldY);
		boatstops.add(this);
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.isWhite = isWhite;
	}

	public double getOffsetX() {
		return offsetX;
	}

	public double getOffsetY() {
		return offsetY;
	}

	public Boatstop getNextBoatStop() {
		return nextBoatStop;
	}

	public void setNextBoatStop(Boatstop nextBoatStop) {
		this.nextBoatStop = nextBoatStop;
	}

	public boolean isWhite() {
		return isWhite;
	}

	public static List<Boatstop> getBoatstops() {
		return boatstops;
	}
}
