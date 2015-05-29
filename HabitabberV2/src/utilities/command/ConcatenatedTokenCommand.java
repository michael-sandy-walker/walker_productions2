package utilities.command;

import java.util.ArrayList;
import java.util.List;

import utilities.Token;

public class ConcatenatedTokenCommand extends Command {
	
	private static List<Token> tokenList = new ArrayList<Token>();
	
	public ConcatenatedTokenCommand(String name, String value) {
		super(name, value);		
		if (value != null && ! value.isEmpty()) {
			tokenList = new ArrayList<Token>();
			for (String v : value.split(TokenCommand.DELIMITER)) {
				tokenList.add(new Token(v));
			}
		}		
	}
	
	public static List<Token> getTokenList() {
		return tokenList;
	}

}
