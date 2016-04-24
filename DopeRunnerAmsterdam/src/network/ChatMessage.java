package network;

import java.io.*;
/*
 * This class defines the different type of messages that will be exchanged between the
 * Clients and the Server. 
 * When talking from a Java Client to a Java Server a lot easier to pass Java objects, no 
 * need to count bytes or to wait for a line feed at the end of the frame
 */
public class ChatMessage implements Serializable {

	protected static final long serialVersionUID = 1112122200L;

	// The different types of message sent by the Client
	// WHOISIN to receive the list of the users connected
	// MESSAGE an ordinary message
	// LOGOUT to disconnect from the Server
	public static final int WHOISIN = 0, MESSAGE = 1, LOGOUT = 2, START_GAME = 3, SELECTCARD = 4, DEALREGULARCARD = 5, 
			DEALADVANCEDCARD = 6, DISCARDCARD = 7, MOVECARD = 8, PLACEPOLICE = 9, REMOVEPOLICE = 10, MOVEPAWN = 11, 
			GROWPAWN = 12, MAKEDEAL = 13, DIETHROWN = 14, CARDCHOSEN = 15, FLIPGOAL = 16, THROWAWAYCARDS = 17, ENDTURN = 18, READY =19, SHUFFLE = 20;
	private int type;
	private String message;
	private int cardId;
	private int playerId;
	private int tramstopId;
	private int senderId;
	// constructor
	public ChatMessage(int type, String message) {
		this.type = type;
		this.message = message;
	}
	// constructor for dopeMessage
	public ChatMessage(int type, int cardId, int playerId, int tramstopId){
		this.type = type;
		this.cardId = cardId;
		this.playerId = playerId;
		this.tramstopId = tramstopId;
	}
	// constructor for end turn, die thrown, grow pawn
	public ChatMessage(int type){
		this.type = type;
	}
	
	// getters
	int getType() {
		return type;
	}
	String getMessage() {
		return message;
	}
	public int getCardId() {
		return cardId;
	}
	public int getPlayerId() {
		return playerId;
	}
	public int getTramstopId() {
		return tramstopId;
	}
	public int getSenderId() {
		return senderId;
	}
	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}
	
}


