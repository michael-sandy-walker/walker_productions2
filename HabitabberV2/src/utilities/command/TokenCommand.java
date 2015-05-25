package utilities.command;

import java.util.ArrayList;
import java.util.List;

import utilities.Token;

public class TokenCommand extends Command {
	
	private static List<Token> tokenList = null;

	public TokenCommand(String name, String value) {
		super(name, value);
		tokenList = new ArrayList<Token>();
		for (String v : value.split(TokenCommand.DELIMITER)) {
			tokenList.add(new Token(v));
		}
	}
	
	public static List<Token> getTokenList() {
		return tokenList;
	}

}
