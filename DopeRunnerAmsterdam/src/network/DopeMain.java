package network;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import gui.DopeGUI;
import javafx.scene.paint.Color;
import model.cards.Card;
import model.gamepiece.Goal;
import model.player.Player;
import model.tramstop.Boatstop;
import model.tramstop.Tramstop;
import network.ChatMessage;

public class DopeMain extends Thread{
	private Client client;
//	private boolean hasSendEndTurn = false;
//	private ChatMessage sendMessage;
	private int readyPlayers = 0;
//	private Player player;
//	private int port;
//	private String host;
//	private DopeGUI dopeGUI = new DopeGUI();
	public DopeMain(Client client) {
		this.client = client;
		start();
	}
	public void run(){
		if(client == null)
			DopeGUI.initGUI(0, new ArrayList<String>(), new ArrayList<Color>(), new Random().nextLong(), this);
		else{
			System.out.println("ClientID: "+client.getUniqueId());
			DopeGUI.initGUI(client.getUniqueId(), new ArrayList<String>(), new ArrayList<Color>(), 100000, this);
		}
//		DopeGUI.initGUI(client.getUniqueId(), new ArrayList<String>(), new ArrayList<Color>(), 100000, this);
	}
	public static void main(String[] args){
		new DopeMain(null);
	}
	public DopeMain(Client client, int playerId, List<String> names, List<Color> colors, long randomScramble) {
		this.client = client;
		if(colors == null){
			colors = new ArrayList<Color>();
		}
//		this.port = port;
//		this.host = host;
//		DopeGUI.initGUI(playerId, names, colors, randomScramble, this);
	}
	
//	public static void main(String[] args){
//		new DopeMain();
//	}
	
	public void sendMessage(ChatMessage message){
		if(Player.getBusyFlag()) return;
		if((Player.getActivePlayer() != Player.getControllingPlayer() || !Player.getReadyFlag())
				&& !((message.getType() == ChatMessage.READY) || Card.getClickReason() == Card.THROWAWAY)) 
			return;
		System.out.println("messageSend at Dope: "+message.getMessage() + "of type: "+message.getType());
//		if(message.getType() == ChatMessage.ENDTURN)
//			has
		if(client!=null){
			if(message.getType() == ChatMessage.READY){
				readyPlayers++;
				System.out.println("playerIsReady: "+readyPlayers);
				if (readyPlayers == Player.getNumberOfPlayers()){
					Player.getActivePlayer().startGame();
					readyPlayers = 0;
				}
			}
			message.setSenderId(client.getUniqueId());
			client.sendMessage(message);
		}
	}
	// called by the Client to append text in the TextArea 
	void append(ChatMessage chatMsg) {
		if(chatMsg.getSenderId() == client.getUniqueId())
			return;
		System.out.print("messageReceived at Dope: "+ chatMsg.getMessage() + "of type: ");
		String msg = chatMsg.getMessage();
		switch(chatMsg.getType()){
		case ChatMessage.CARDCHOSEN:
			Player.getActivePlayer().cardChosen(Card.getCard(chatMsg.getCardId()));
			break;
		case ChatMessage.DEALREGULARCARD:
			System.out.println("DEALREGULARCARD");
			Player.getActivePlayer().dealCardTo(Player.getPlayer(chatMsg.getPlayerId()), true);
			break;
		case ChatMessage.DEALADVANCEDCARD:
			System.out.println("DEALADVANCEDCARD");
			Player.getActivePlayer().dealCardTo(Player.getPlayer(chatMsg.getPlayerId()), false);
			break;
		case ChatMessage.DIETHROWN:
			System.out.println("DIETHROWN");
			Player.getActivePlayer().handleDie();
			break;
		case ChatMessage.DISCARDCARD:
			System.out.println("DISCARDCARD");
			Player.getActivePlayer().discardCard(Card.getCard(chatMsg.getCardId()));
			break;
		case ChatMessage.ENDTURN:
			System.out.println("ENDTURN");
			Player.getActivePlayer().endTurn();
			break;
		case ChatMessage.FLIPGOAL:
			((Goal) (((Boatstop) Tramstop.getTramstop(chatMsg.getTramstopId())).getGoal())).flipGoal();

			break;
		case ChatMessage.GROWPAWN:
			System.out.println("GROWPAWN");
			Player.getActivePlayer().growPawn();
			break;
		case ChatMessage.MAKEDEAL:
			System.out.println("MAKEDEAL");
			Player.getActivePlayer().makeDeal(Tramstop.getTramstop(chatMsg.getTramstopId()));
			break;
		case ChatMessage.MOVECARD:
			System.out.println("MOVECARD");
			int cardId = chatMsg.getCardId();
			int playerId = chatMsg.getPlayerId();
			Player.getActivePlayer().moveCardTo(Card.getCard(cardId), Player.getPlayer(playerId));
			break;
		case ChatMessage.MOVEPAWN:
			System.out.println("MOVEPAWN");
			Player.getActivePlayer().movePawnTo(Player.getPlayer(chatMsg.getPlayerId()),Tramstop.getTramstop(chatMsg.getTramstopId()));
			break;
		case ChatMessage.PLACEPOLICE:
			System.out.println("PLACEPOLICE");
			Player.getActivePlayer().placePolice(Tramstop.getTramstop(chatMsg.getTramstopId()));
			break;
		case ChatMessage.READY:
			System.out.println("READY");
			readyPlayers++;
			System.out.println("readyPlayers: "+readyPlayers);
			if (readyPlayers == Player.getNumberOfPlayers()){
				Player.getActivePlayer().startGame();
				readyPlayers = 0;
			}
			break;
		case ChatMessage.REMOVEPOLICE:
			System.out.println("REMOVEPOLICE");
			Player.getActivePlayer().removePolice(Tramstop.getTramstop(chatMsg.getTramstopId()));
			break;
		case ChatMessage.SELECTCARD:
			System.out.println("SELECTCARD");
			Card.selectCard(chatMsg.getCardId());
			break;
		case ChatMessage.THROWAWAYCARDS:
			Player.setBusyFlag(true);
			Player.getActivePlayer().throwAwayCards(Player.getPlayer(chatMsg.getPlayerId()));
			break;
		default: 
			System.out.println("SOMETHING ELSE");
			break;
		}
//		Player.getActivePlayer().dealCardTo(Player.getActivePlayer(), true);
//		System.out.println("messageReceived : "+str);
//		System.out.println("messageReceived : "+str);
//		ta.setCaretPosition(ta.getText().length() - 1);
	}
//	public Player getPlayer() {
//		return player;
//	}
//	public void setPlayer(Player player) {
//		this.player = player;
//	}

}
