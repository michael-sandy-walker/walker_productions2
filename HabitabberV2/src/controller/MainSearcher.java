package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore.Entry.Attribute;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Platform;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import result.Page;
import result.PageFactory;
import result.SubPage;
import utilities.IOSingleton;
import utilities.Token;
import utilities.UrlToken;
import utilities.WebFile;
import utilities.command.Command;
import utilities.command.CommandFactory;
import utilities.command.ConcatenatedTokenCommand;
import utilities.command.LogCommand;
import utilities.command.MaxSearchCommand;
import utilities.command.PageCommand;
import utilities.command.ParseImmediateCommand;
import utilities.command.RegExCommand;
import utilities.command.TokenCommand;
import utilities.command.VisitedLinkCommand;
import view.HabitabberGUI;

public class MainSearcher {

	private HabitabberGUI gui;
	
	WebFile webFile;
	
	private Map<String, String> categoryMap = new HashMap<String, String>();

	private List<Token> tokenList = new ArrayList<Token>();

	public final static List<Pattern> patternList = new ArrayList<Pattern>();
	static {
//		patternList.add(Pattern.compile(".*(((0)[1-9]{2}[0-9][-]?(\\s?)[1-9][0-9]{5})|((\\+31|0|0031)[1-9][0-9][-]?[1-9][0-9]{6})).*"));
//		patternList.add(Pattern.compile(".*(((\\+31|0|0031)6){1}[1-9]{1}[0-9]{7}).*", Pattern.CASE_INSENSITIVE));
//		patternList.add(Pattern.compile(".*(((0)[1-9][-]?\\s?[1-9][0-9]{2}\\s?[0-9]{5})).*"));
	}
	//	public final static Pattern p1 = Pattern.compile(".*(((0)[1-9]{2}[0-9][-]?(\\s?)[1-9][0-9]{5})|((\\+31|0|0031)[1-9][0-9][-]?[1-9][0-9]{6})).*");
	//	public final static Pattern p2 = Pattern.compile(".*(((\\+31|0|0031)6){1}[1-9]{1}[0-9]{7}).*", Pattern.CASE_INSENSITIVE);
	//	public final static Pattern p3 = Pattern.compile(".*(((0)[1-9][-]?\\s?[1-9][0-9]{2}\\s?[0-9]{5})).*");

	public static int MAX_SITES = 1000;
	public static int COUNTER = 0;

	public String testStr = "http://www.marktplaats.nl/a/huizen-en-kamers/huizen-te-huur/m925913694-woning-te-huur-in-ijmuiden.html?c=efb2ef4dc323389c4f92ed10afa33e3a&previousPage=lr";

	//	public static List<String> visitedLinkList = new ArrayList<String>();
	public static Set<String> visitedLinkSet = new TreeSet<String>();

	public boolean doImmediateParse = true;

	private List<Command> commandList = null;

	private static Logger logger = Logger.getLogger("MainSearcher");

	private static MainSearcher singleton;

	private static boolean stop;

	private static String[] pageArr;
	
	private static boolean inMain = false;
	
	IOSingleton io;
	static {
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
	
	private MainSearcher(String[] argv) { 
		this(null, argv);
	}

	private MainSearcher(HabitabberGUI gui, String[] argv) {
		this.gui = gui;
	}

	public static MainSearcher getSingleton(HabitabberGUI gui, String[] argv) {
		if (MainSearcher.singleton == null) {
			MainSearcher.singleton = new MainSearcher(gui, argv);
		}	
		return singleton;
	}

	public void activate(String[] argv) {
		try {
			if (argv != null && argv.length > 0) {
				/**
				 * Begin setup
				 */
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date();				

				assignInputParams(argv);

				logger.info("Maximum number of searchable pages: " + MAX_SITES);
				//				io.write(dateFormat.format(date) + " - HabitabberV2 initialized.\n", true);
				outputText(dateFormat.format(date) + " - HabitabberV2 initialized.\n");

				/**
				 * End setup
				 */

				/**
				 * Begin with the tree generation (and the search) 
				 */				
				Page page = generateTree(pageArr);			
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

	public void assignInputParams(String[] argv) {
		pageArr = null; 
		commandList = parseCommands(argv);
		doImmediateParse = ParseImmediateCommand.isParseImmediate();
		logger.setLevel(LogCommand.getLevel());
		MAX_SITES = MaxSearchCommand.getMaxSearches();

		boolean hasVisitedLinkCommand = VisitedLinkCommand.getVisitedLinkFileName() != null && !VisitedLinkCommand.getVisitedLinkFileName().isEmpty();
		io = IOSingleton.getIOSingleton(false, hasVisitedLinkCommand);				
		if (hasVisitedLinkCommand) {
			logger.fine("Adding visited links");
			addVisitedLinksFromFile(VisitedLinkCommand.getVisitedLinkFileName());
		}														
		if (commandList.isEmpty() || (commandList.size() == 1 && (commandList.get(0) == null || !commandList.get(0).getName().equals("p")))) {				
			// just a page input
			pageArr = argv;				
		} else { // retrieve pages from PageCommand
			for (Command cmd : commandList) {
				if (cmd != null) {
					logger.fine("Command: " + cmd.getName() + " Attribute: " + cmd.getValue());

					if (cmd instanceof PageCommand && cmd.getValue() != null && !cmd.getValue().isEmpty()) {
						pageArr = cmd.getValue().split(Command.DELIMITER);
					} else if (cmd instanceof RegExCommand && cmd.getValue() != null && !cmd.getValue().isEmpty()) {
						for (String cmdSplitlet : cmd.getValue().split(Command.DELIMITER)) {
							patternList.add(Pattern.compile(cmdSplitlet));
						}

					}
				}
			}			
		}	
	}

	public void addVisitedLinksFromFile(String fileName) {
		if (io == null) {
			io = IOSingleton.getIOSingleton();
		}
		io.setReadFileName(fileName);
		String line = null;
		while ((line = io.readLine()) != null) {
			visitedLinkSet.add(line);
		}
	}

	public void terminate() {
		if (io != null) {
			io.reset(true);
			COUNTER = 0;
			setStop(true);
			if (io != null) {
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date();
				outputText(dateFormat.format(date) + " - HabitabberV2 terminated.\n");
				String visitedLinkListName = io.getReadFileName();
				if (visitedLinkListName == null || visitedLinkListName.isEmpty()) {
					visitedLinkListName = "visitedLinkList.txt";
				}
				io.setWriterFileName(visitedLinkListName);
				for (String link : visitedLinkSet) {
					io.write(link + "\n", false, false);
				}
				io.closeIO();	
			}
		}
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

	public Page generateTree(String... argv) {		
		Page result = null;
		for (String str : argv) {			
			COUNTER = 0;
			UrlToken token = new UrlToken(str);
			tokenList.add(token);
			result = performSearch(token);
		}
		return result;
	}

	public Page performSearch(UrlToken token) {
		if (!isStop()) {
			return performSearch(token, false);
		} else {
			return null;
		}
	}

	public Page performSearch(UrlToken token, boolean isSubPage) {		
		try {
			Thread.sleep(1);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		if (logger.getLevel().equals(Level.INFO)) {
			System.out.print(">");		
			if (COUNTER != 0 && COUNTER%60 == 0) {
				System.out.println("");
			}
		}
		if (isSubPage)
			visitedLinkSet.add(token.getUrl());

		logger.fine("token: " + token.getUrl());
		// 1.) retrieve page (first page = root)
		Page page = retrievePage(token.getUrl(), isSubPage);
		// 2.) add links of page to tree (children of current node) incl. content
		if (page != null) {			
			if (doImmediateParse)
				parseContent(page);
			addLinksToPage(page);
			// 3.) iterate through current tree level 
			for (SubPage subPage : page.getSubPageList()) {
				logger.finest("subPage: " + subPage.getUrl());
				// 4.) if link of current node is not within visitedList
				if (/*!visitedLinkList.contains(subPage.getUrl()) &&*/ COUNTER < MAX_SITES && !isStop()) {
					COUNTER++;					
					// 5.) repeat steps
					try {
						performSearch(new UrlToken(subPage.getUrl()), true);
					} catch (Exception e) {
						logger.severe("Exception: " + e.getMessage());
						e.printStackTrace();
					}
				}
			}
		}

		return page;
	}

	public void searchTree(Page page) {
		if (!isStop()) {
			parseContent(page);

			Object tel = page.getContent().get("tel");
			if (tel != null) {
				//			logger.info("Found: " + page.getUrl());
				String str = "Found: " + page.getUrl() + " , Tel: " + tel.toString();
				//				System.out.println(str);
				//				io.write(str + "\n", true);
				//				io.write(page.getUrl(), true);
				outputText(str + "\n");
				//				HabitabberGUI.getOutputtextarea().appendText(str);
			}

			// recursive call
			List<SubPage> subPageList = page.getSubPageList();			
			for (SubPage subPage : subPageList) {		
				searchTree(subPage);
			}
		}
	}

	public Page retrievePage(String url) {
		return retrievePage(url, false);
	}

	public Page retrievePage(String url, boolean subPage) {			
		Page result = new Page();
		try {
			StringBuffer content = new StringBuffer();
			if (url != null && !url.isEmpty()) {
				webFile = new WebFile(url);
				if (webFile.getContent() != null) {
					Object contentObj = webFile.getContent();					
					if (contentObj instanceof byte[]) { // TODO: write better mechanism to retrieve the charset (e.g., Facebook apparently doesn't give information about it) 
						byte[] contentByte = (byte[]) contentObj;
						content.append(new String(contentByte, StandardCharsets.UTF_8)); 
					}
					else
						content.append(webFile.getContent().toString());
				}
			}

			result = PageFactory.getPage(content.toString(), subPage);
			result.setUrl(url);

			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public void addLinksToPage(Page page) {
		// Parse the content and stuff it into Site.
		Document doc = Jsoup.parse(page.getUnparsedContent());		
		Elements links = doc.select("a[href]");				
		for (Element link : links) {						
			String linkStr = link.attr("href");
			if (linkStr.startsWith("/")) { // We have a sub page link here, let's add its parent url
				try {
					URL url = new URL(page.getUrl());
					String host = url.getHost();
					String protocol = url.getProtocol();
					linkStr = protocol + "://" + host + linkStr;
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}			
			if (visitedLinkSet.add(linkStr) && containsString(linkStr)) {		
				SubPage result = new SubPage();	
				result.setUrl(linkStr);
				logger.fine("Adding link to page " + page.getUrl() + ": " + linkStr);
				page.addSubPage(result);
			}
		}
	}
	
	public boolean containsString(String linkStr) {
		boolean containsStr = false;
		if ((TokenCommand.getTokenList() == null || TokenCommand.getTokenList().isEmpty()) 
				&& (ConcatenatedTokenCommand.getTokenList() == null || ConcatenatedTokenCommand.getTokenList().isEmpty())) {
			containsStr = true;
		} else {								
			if (TokenCommand.getTokenList() != null) {
				for (Token token : TokenCommand.getTokenList()) {					
					if (!linkStr.isEmpty() && linkStr.contains(token.getContent())) {
						containsStr = true;
						break;
					}
				}										
			}				
			boolean containsStrTmp = true;
			for (Token token : ConcatenatedTokenCommand.getTokenList()) {
				if (linkStr.isEmpty() || !linkStr.contains(token.getContent())) {						
					containsStrTmp = false;
					break;
				}
			}	
			if (!ConcatenatedTokenCommand.getTokenList().isEmpty()) {
				containsStr = containsStrTmp;
			}
		}
		return containsStr;
	}

	public void parseContent(Page page) {
		// If the main page does not contain the conjunctive / disjunctive set(s)
//		if (page instanceof Page && !containsString(page.getUrl())) {
//			return;
//		}
		// If we don't parse immediately, retrieve the content of the link
		if (!doImmediateParse && (page instanceof SubPage)) {			
			page = retrievePage(page.getUrl(), true);
		}		
		// Parse the content and stuff it into the Page.
		String unparsedContent = page.getUnparsedContent();
		if (unparsedContent != null && !unparsedContent.isEmpty()) {
			Document doc = Jsoup.parse(unparsedContent);	
			Elements media = doc.select("[src]");
			Elements[] descriptions = new Elements[]{doc.select("div.l-body-content"), doc.select("section[id=postingbody]")};
			Elements prices = doc.select("span.price");

			logger.finest(doc.toString());						

			for (Element medium : media) {
				if (medium.tagName().equals("img"))
					page.setContent("media", medium.attr("src"));			
			}
			
			for (Elements description : descriptions) {
				for (Element desc : description) {
					if (description.text() != null && !description.text().isEmpty()) {
						page.setDescription(desc);
						page.setContent("description", desc);
					}
				}
			}
			
			for (Element p : prices) {
				if (p.text() != null && !p.text().isEmpty()) {
					page.setContent("price", p.text());
				}
			}
			
			for (String categoryName : categoryMap.keySet()) {
				String categoryValue = categoryMap.get(categoryName);
				Elements elements = doc.select(categoryValue);
				for (Element element : elements) {
					if (element.text() != null && !element.text().isEmpty()) {
						page.setContent(categoryName, element.text());
					} else if (element.attributes() != null && (element.attributes().size() > 0)) {
						for (org.jsoup.nodes.Attribute attribute : element.attributes().asList()) 
							if (attribute.getKey().equals("src"))
								page.setSource(categoryName, attribute.getValue());
					}
				}
			}
			
			if (page.getDescription() == null || !page.getDescription().hasText()) {
				Elements desc = doc.select("title");
				for (Element element : desc) {
					page.setDescription(element);
					page.setContent("description", element);
				}
			}

			search:
				for (Element elem : doc.getAllElements()) {
					for (Node node : elem.childNodes()) {
						if (node instanceof TextNode) {
							String text = ((TextNode)node).text();

							for (Pattern p : patternList) {
								Matcher m = p.matcher(text);
								if (m.matches()) {
									logger.fine(text);		
										String str = "Found: " + page.getUrl() + " , content: " + text;
										//								System.out.println(str);
										//								io.write(str + "\n", true);
										//								HabitabberGUI.appendOutputText(str + "\n");
										//								io.write(page.getUrl(), true);
										if (!text.trim().isEmpty()) { // We could have a regex like .*
											page.setContent("tel", text);
										}
										outputText(str + "\n");										
										setPage(page);
										break search;									
								}								
							}						
						}
					}
				}
		}
	}

	/**
	 * @return the stop
	 */
	public static boolean isStop() {
		return stop;
	}

	/**
	 * @param stop the stop to set
	 */
	public static void setStop(boolean stop) {
		MainSearcher.stop = stop;
	}

	public void outputText(String str) {	
		io.write(str, true);
		if (!inMain) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					gui.appendOutputText(str);                                
				}
			});		
		}
		System.out.println(str);
	}
	
	public void setPage(Page page) {	
//		io.write(page.getUrl(), true);
		if (!inMain) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					gui.setPage(page);                             
				}
			});		
		}
	}

	public HabitabberGUI getGui() {
		return gui;
	}

	public void setGui(HabitabberGUI gui) {
		this.gui = gui;
	}
	
	public void resetVisitedLinks() {
		visitedLinkSet = new TreeSet<String>();
	}
	
	public void addCategory(String name, String value) {
		categoryMap.put(name, value);		
		System.out.println("Saving category " + name + ", value: " + value);
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
		inMain = true;
		MainSearcher mainSearcher = new MainSearcher(argv);
		mainSearcher.activate(argv);		
	}
	
	//TODO: Add categories to saving (29-AUG-2017)
	public void saveConfigFile(File file, List<String> inputParams) {
		try (
				Writer writer = new FileWriter(file);
				BufferedWriter bw = new BufferedWriter(writer);
				) {
			assignInputParams(inputParams.toArray(new String[inputParams.size()]));
			outputText("Saved.");
			for (Command command : commandList) {
				bw.write(command.getName() + CommandFactory.SEPARATOR + command.getValue() + "\n");
			}
		} catch (IOException e) {
			logger.warning("IOException occured during saving config file.");
		} finally {
		}
	}
	
	public void loadConfigFile(File file) {
		try (
				Reader reader = new FileReader(file);
				BufferedReader bw = new BufferedReader(reader);
				) {
			String line = null;
			if (commandList == null)
				commandList = new ArrayList<>();
			commandList.clear();
			while ((line = bw.readLine()) != null) {
				String[] cmdPair = line.split(CommandFactory.SEPARATOR);
				commandList.add(CommandFactory.getCommand(cmdPair[0], cmdPair[1]));
			}
			if (!inMain) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						// url
						
						// left config:	
						// 	page limit
						//	log level
						//	link file
						//	immediate search
						//	disjunctions
						//	conjunctions
						//	use cookies
						//	descriptions
						//	media
						//	regex
						// categories
						// content
						gui.loadConfig(commandList);                               
					}
				});		
			}
		} catch (IOException e) {
			logger.warning("IOException occured during saving config file.");
		} finally {
		}
	}
	
}
