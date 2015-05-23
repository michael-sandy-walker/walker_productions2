package controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import result.MainPage;
import result.PageFactory;
import result.SubPage;
import utilities.Command;
import utilities.CommandFactory;
import utilities.ConcatenatedTokenCommand;
import utilities.IOSingleton;
import utilities.LogCommand;
import utilities.MaxSearchCommand;
import utilities.PageCommand;
import utilities.ParseImmediateCommand;
import utilities.Token;
import utilities.TokenCommand;
import utilities.UrlToken;
import utilities.VisitedLinkCommand;
import utilities.WebFile;

public class MainSearcher {

	WebFile webFile;

	private List<Token> tokenList = new ArrayList<Token>();

	//	public final static Pattern p = Pattern.compile("/^(?:\\([2-9]\\d{2}\\)\\ ?|[2-9]\\d{2}(?:\\-?|\\ ?))[2-9]\\d{2}[- ]?\\d{4}$/");
	public final static Pattern p1 = Pattern.compile("(09\\/[0-9]{3}\\.[0-9]{2}\\.[0-9]{2}|04[789][0-9]\\/[0-9]{2}\\.[0-9]{2}\\.[0-9]{2})");
	public final static Pattern p2 = Pattern.compile(".*(((0)[1-9]{2}[0-9][-]?(\\s?)[1-9][0-9]{5})|((\\+31|0|0031)[1-9][0-9][-]?[1-9][0-9]{6})).*");
	public final static Pattern p3 = Pattern.compile(".*(((\\+31|0|0031)6){1}[1-9]{1}[0-9]{7}).*", Pattern.CASE_INSENSITIVE);
	public final static Pattern p4 = Pattern.compile(".*(((0)[1-9][-]?\\s?[1-9][0-9]{2}\\s?[0-9]{5})).*");

	public static int MAX_SITES = 1000;
	public static int counter = 0;

	public String testStr = "http://www.marktplaats.nl/a/huizen-en-kamers/huizen-te-huur/m925913694-woning-te-huur-in-ijmuiden.html?c=efb2ef4dc323389c4f92ed10afa33e3a&previousPage=lr";

	//	public static List<String> visitedLinkList = new ArrayList<String>();
	public static Set<String> visitedLinkList = new TreeSet<String>();

	public boolean doImmediateParse = true;

	List<Command> commandList = null;

	Logger logger = Logger.getLogger("MainSearcher");

	IOSingleton io;
	{
		logger.setLevel(Level.INFO);
		//get the top Logger:
		Logger topLogger = java.util.logging.Logger.getLogger("");

		// Handler for console (reuse it if it already exists)
		Handler consoleHandler = null;
		//see if there is already a console handler
		for (Handler handler : topLogger.getHandlers()) {
			if (handler instanceof ConsoleHandler) {
				//found the console handler
				consoleHandler = handler;
				break;
			}
		}


		if (consoleHandler == null) {
			//there was no console handler found, create a new one
			consoleHandler = new ConsoleHandler();
			topLogger.addHandler(consoleHandler);
		}
		//set the console handler to fine:
		consoleHandler.setLevel(java.util.logging.Level.FINEST);
	}

	public MainSearcher(String[] argv) {		
		try {

			if (argv != null && argv.length > 0) {
				/**
				 * Begin setup
				 */
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date();				
				String[] pageArr = null; 
				commandList = parseCommands(argv);
				doImmediateParse = ParseImmediateCommand.isParseImmediate();
				logger.setLevel(LogCommand.getLevel());
				MAX_SITES = MaxSearchCommand.getMaxSearches();

				boolean hasVisitedLinkCommand = VisitedLinkCommand.getVisitedLinkFileName() != null;
				io = IOSingleton.getIOSingleton(false, hasVisitedLinkCommand);				
				if (hasVisitedLinkCommand) {
					io.setReadFileName(VisitedLinkCommand.getVisitedLinkFileName());
					String line = io.readLine();
					while (line != null) {
						visitedLinkList.add(line);
					}
				}														
				if (commandList.isEmpty() || (commandList.size() == 1 && (commandList.get(0) == null || !commandList.get(0).getName().equals("p")))) {				
					// just a page input
					pageArr = argv;				
				} else { // retrieve pages from PageCommand
					for (Command cmd : commandList) {
						if (cmd != null) {
							logger.fine("Command: " + cmd.getName() + " Attribute: " + cmd.getValue());

							if (cmd instanceof PageCommand && cmd.getValue() != null) {
								pageArr = cmd.getValue().split(Command.DELIMITER);
							}
						}
					}			
				}			
				logger.info("Maximum number of searchable pages: " + MAX_SITES);
				io.write(dateFormat.format(date) + " - HabitabberV2 initialized.\n", true);
				/**
				 * End setup
				 */

				/**
				 * Begin with the tree generation (and the search) 
				 */				
				MainPage page = generateTree(pageArr);			
				if (page != null && !doImmediateParse) {
					searchTree(page);
				}			
			} else {
				logger.warning("Please specify one or more search params.");
			}
		} finally {
			terminate();
		}
	}

	public void terminate() {
		System.out.println("");
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		io.write(dateFormat.format(date) + " - HabitabberV2 terminated.\n", true);
		String visitedLinkListName = io.getReadFileName();
		if (visitedLinkListName == null) {
			visitedLinkListName = "visitedLinkList.txt";
		}
		io.setWriterFileName(visitedLinkListName);
		for (String link : visitedLinkList) {
			io.write(link + "\n", false, false);
		}
		io.closeIO();				
	}

	public List<Command>parseCommands(String... argv) {
		List<Command> result = new ArrayList<Command>();

		ListIterator<String> argvIter = (ListIterator<String>) Arrays.asList(argv).listIterator();

		boolean firstParam = true;

		while (argvIter.hasNext()) {			
			String cmd = (String) argvIter.next();			
			logger.finest("cmd: " + cmd);
			String value = null;
			if (cmd.startsWith("-")) {
				cmd = cmd.substring(1);
				value = valueLookAhead(argvIter);
			} else if (firstParam) {
				value = cmd;
				cmd = "p";				
			}
			result.add(CommandFactory.getCommand(cmd, value));
			firstParam = false;
		}

		return result;
	}

	/**
	 * Looks for values corresponding to a command
	 * @param cmdIter The command iterator
	 * @return String (value's corresponding to a command separated by a Command DELIMITER)
	 */
	public String valueLookAhead(ListIterator<String> cmdIter) {
		String result = "";

		while (cmdIter.hasNext() ) {			
			String cmd = (String) cmdIter.next();
			if (!cmd.startsWith("-")) {
				if (!result.isEmpty()) {
					result += Command.DELIMITER;
				}
				result += cmd;
			} else {
				cmdIter.previous();
				break;
			}			
		}		

		return result;
	}



	public MainPage generateTree(String... argv) {
		MainPage result = null;
		for (String str : argv) {
			UrlToken token = new UrlToken(str);
			tokenList.add(token);
			result = performSearch(token);
		}
		return result;
	}

	public void searchTree(MainPage page) {
		parseContent(page);

		Object tel = page.getContent().get("tel");
		if (tel != null) {
			//			logger.info("Found: " + page.getUrl());
			String str = "Found: " + page.getUrl() + " , Tel: " + tel.toString();
			System.out.println(str);
			io.write(str + "\n", true);
		}

		// recursive call
		List<SubPage> subPageList = page.getSubPageList();
		for (SubPage subPage : subPageList) {		
			searchTree(subPage);
		}
	}

	public MainPage performSearch(UrlToken token) {
		return performSearch(token, false);
	}

	public MainPage performSearch(UrlToken token, boolean isSubPage) {

		//		System.out.print(counter + " ");
		if (logger.getLevel().equals(Level.INFO)) {
			System.out.print(">");
			if (counter != 0 && counter%60 == 0) {
				System.out.println("");
			}
		}
		if (isSubPage)
			visitedLinkList.add(token.getUrl());

		logger.fine("token: " + token.getUrl());
		// 1.) retrieve page (first page = root)
		MainPage page = retrievePage(token.getUrl(), isSubPage);
		// 2.) add links of page to tree (children of current node) incl. content
		if (page != null) {			
			if (doImmediateParse)
				parseContent(page);

			addLinksToPage(page);
			// 3.) iterate through current tree level 
			for (SubPage subPage : page.getSubPageList()) {

				// 4.) if link of current node is not within visitedList
				if (!containsString(visitedLinkList, subPage.getUrl()) && counter < MAX_SITES) {
					counter++;					
					// 5.) repeat steps
					try {
						performSearch(new UrlToken(subPage.getUrl()), true);
					} catch (Exception e) {
						logger.severe("Exception: " + e.getMessage());
					}
				}
			}
		}

		return page;
	}

	public MainPage retrievePage(String url) {
		return retrievePage(url, false);
	}

	public MainPage retrievePage(String url, boolean subPage) {	
		MainPage result = new MainPage();
		try {
			String content = "";
			if (url != null && !url.isEmpty()) {
				webFile = new WebFile(url);
				if (webFile.getContent() != null) {
					content = webFile.getContent().toString();
				}
			}

			result = PageFactory.getPage(content, subPage);
			result.setUrl(url);

			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public void addLinksToPage(MainPage page) {
		// Parse the content and stuff it into Site.
		Document doc = Jsoup.parse(page.getUnparsedContent());		
		Elements links = doc.select("a[href]");				
		for (Element link : links) {			
			String linkStr = link.attr("abs:href");			

			boolean containsStr = false;
			if (TokenCommand.getTokenList() == null || TokenCommand.getTokenList().isEmpty() 
					&& (ConcatenatedTokenCommand.getTokenList() == null || ConcatenatedTokenCommand.getTokenList().isEmpty())) {
				if (linkStr.contains("huizen-en-kamers")) {
					containsStr = true;
				}
			} else {				
				for (Token token : TokenCommand.getTokenList()) {
					if (linkStr.contains(token.getContent())) {
						containsStr = true;
						break;
					}
				}
				for (Token token : ConcatenatedTokenCommand.getTokenList()) {
					if (!linkStr.contains(token.getContent())) {						
						containsStr = false;
						break;
					}
				}
			}
			if (containsStr) {				
				SubPage result = new SubPage();	
				result.setUrl(linkStr);
				logger.fine("Adding link to page " + page.getUrl() + ": " + linkStr);
				page.addSubPage(result);
			}
		}
	}

	public void parseContent(MainPage page) {
		// Parse the content and stuff it into Site.
		Document doc = Jsoup.parse(page.getUnparsedContent());	
		Elements media = doc.select("[src]");

		logger.finest(doc.toString());

		for (Element medium : media) {
			if (medium.tagName().equals("img"))
				page.setContent("media", medium.attr("abs:src"));			
		}

		for (Element elem : doc.getAllElements()) {
			for (Node node : elem.childNodes()) {
				if (node instanceof TextNode) {
					String text = ((TextNode)node).text();

					Matcher m1 = p1.matcher(text);	
					Matcher m2 = p2.matcher(text);
					Matcher m3 = p3.matcher(text);
					Matcher m4 = p4.matcher(text);
					if (m1.matches() || m2.matches() || m3.matches() || m4.matches()) {
						logger.fine(text);		
						if (doImmediateParse) {
							String str = "Found: " + page.getUrl() + " , Tel: " + text;
							System.out.println(str);
							io.write(str + "\n", true);
						}
						page.setContent("tel", text);
					}
				}
			}
		}
	}

	public boolean containsString(Set<String> list, String str) {
		for (String s : list) {
			if(s.equals(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Main class.
	 * Commands: -p PAGE 		- The searchable root pages
	 * 			 -l LEVEL 		- The log level
	 * 			 -i 			- Immediate parsing of the content
	 * 			 -m NUMBER  	- The number of the searchable pages 
	 * 			 -v LINKFILE	- The file of the previously visited links
	 * 			 -t	TOKEN(S)	- The disjunctive token(s) links are searched for
	 * 			 -c CONJUNCTION - The conjunctive tokens links are searched for 
	 * @param argv 
	 */
	public static void main(String[] argv) {
		new MainSearcher(argv);
	}
}