package view;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import controller.MainSearcher;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import result.Page;
import utilities.command.CategoryCommand;
import utilities.command.Command;
import utilities.command.CommandFactory;
import utilities.command.GuiCheckboxCommand;
import view.action.RemoveAction;
import view.button.AddRegExButton;
import view.button.AddSubExButton;
import view.button.PapaButton;
import view.button.PopupButton;
import view.button.RemoveButton;
import view.button.SearchButton;
import view.button.StopButton;
import view.field.BabyField;
import view.field.BasicField;
import view.field.CategoryField;
import view.field.CheckboxField;
import view.field.CookieField;
import view.field.PapaField;
import view.field.ParseImmediateField;
import view.field.RegExField;

public class HabitabberGUI extends Application {

	public static final int REGEX_TYPE = 0;	
	public static final int CATEGORY_TYPE = 1;
	public static final int SUBEX_TYPE = 2;

	public static final String TITLE = "Habitabber";

	public static Image HABITABBER_ICON = new Image(HabitabberGUI.class.getResourceAsStream("/view/habitabber_64.png"));

	private static final ScrollPane scrollPane = new ScrollPane();

	private static Stage stage;

	private static GridPane grid;

	private static int hIndex[] = {2, 0, 0};

	private static int hIndexOffset[] = {hIndex[REGEX_TYPE], 4, 0};

	private Map<String, Boolean> checkBoxMap = new HashMap<String, Boolean>();

	private static Stage progressIndicatorStage = null;

	private static final String INLINE_CSS_STYLE = "-fx-background-color: #1d1d1d; -fx-text-fill: white;";

	private static final Color FILL_COLOR = Color.web("0076a3");

	/**
	 * Only for test reasons.
	 * @param argv The arguments
	 */
	public static void main(String[] argv) {
		launch(argv);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		HabitabberGUI.stage= primaryStage;
		primaryStage.setTitle(TITLE);

		Scene scene = new Scene(new VBox(), 1200, 600);
		scene.setFill(Color.OLDLACE);
		
		primaryStage.getIcons().add(HABITABBER_ICON);

		CommandFactory.registerCommands();
		initMenu(scene);
		GridPane grid = initGrid(scene);
		initForm(grid, null);

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				System.exit(0);
			}
		});

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public GridPane initGrid(Scene scene) {
		grid = new GridPane();
		grid.setAlignment(Pos.TOP_LEFT);
		grid.setHgap(5);
		grid.setVgap(5);
		grid.setPadding(new Insets(5, 5, 5, 5));
		grid.setPrefSize(scene.getWidth(), scene.getHeight());
		grid.setStyle(INLINE_CSS_STYLE);
		((VBox)scene.getRoot()).getChildren().add(grid);
		((VBox)scene.getRoot()).setStyle(INLINE_CSS_STYLE);
		return grid;
	}
	
	public GridPane resetGrid(Scene scene) {
		ObservableList<Node> nodes = ((VBox)scene.getRoot()).getChildren();
		
		nodes.removeIf(new Predicate<Node>() {
		    @Override
		    public boolean test(Node input) {
		        if (input.getClass().getName().contains("Menu"))
		            return false;
		        return true;
		    }
		}); 
		return initGrid(scene);
	}
	
	public static void resetHIndex() {
		hIndex = new int[]{2, 0};
		hIndexOffset = new int[]{hIndex[REGEX_TYPE], 4};
	}

	public void initMenu(Scene scene) {				

		final Menu menu1 = new Menu("File");
		MenuBar menuBar = new MenuBar();

		MenuItem menu11 = new MenuItem("Open Resource File");
		HabitabberGUI gui = this;
		menu11.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Resource File");
				File file = fileChooser.showOpenDialog(stage);								

				if (file != null) {
					MainSearcher mainSearcher = MainSearcher.getSingleton(gui, null);				
					mainSearcher.addVisitedLinksFromFile(file.getAbsolutePath());
				}
			}
		});
		menu1.getItems().add(menu11);

		MenuItem menu12 = new MenuItem("Exit");
		menu12.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.exit(0);
			}
		});
		menu1.getItems().add(menu12);
		
		MenuItem menu13 = new MenuItem("Save As...");
		menu13.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Save Config File");
				File file = fileChooser.showSaveDialog(stage);								
				if (file != null) {
					MainSearcher mainSearcher = MainSearcher.getSingleton(gui, null);	
					mainSearcher.saveConfigFile(file, getArgumentList());
				}
			}
		});
		menu1.getItems().add(menu13);
		
		MenuItem menu14 = new MenuItem("Load Config File");
		menu14.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Load Config File");
				File file = fileChooser.showOpenDialog(stage);								
				if (file != null) {
					MainSearcher mainSearcher = MainSearcher.getSingleton(gui, null);	
					mainSearcher.loadConfigFile(file);
				}
			}
		});
		menu1.getItems().add(menu14);
		
		MenuItem menu15 = new MenuItem("Clear");
		menu15.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				resetGrid(HabitabberGUI.stage.getScene());
			}
		});
		menu1.getItems().add(menu15);
		menuBar.getMenus().add(menu1);
		
		final Menu menu2 = new Menu("Edit");

		MenuItem menu21 = new MenuItem("Reset");
		menu21.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				scrollPane.setContent(null);
				stage.show();
				MainSearcher mainSearcher = MainSearcher.getSingleton(gui, null);
				mainSearcher.resetVisitedLinks();
			}
		});
		menu2.getItems().add(menu21);
		menuBar.getMenus().add(menu2);

		final Menu menu3 = new Menu("View");		

		MenuItem menu31 = new MenuItem("Show progress indicator");
		menu31.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (progressIndicatorStage != null)
					progressIndicatorStage.show();
			}
		});	
		menu3.getItems().add(menu31);

		menuBar.getMenus().add(menu3);

		final Menu menu4 = new Menu("Search");		

		MenuItem menu41 = new MenuItem("Add category");
		menu41.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				addCategory();				
			}
		});
		menu4.getItems().add(menu41);
		menuBar.getMenus().add(menu4);


		//		grid.add(menuBar, 0, 0);
		((VBox) scene.getRoot()).getChildren().addAll(menuBar);
	}

	private void addCategory() {
		final Stage myDialog = new Stage();
		myDialog.getIcons().add(HABITABBER_ICON);
		myDialog.setTitle("Categories");
		myDialog.initModality(Modality.WINDOW_MODAL);

		Scene myDialogScene = new Scene(new VBox(), 400, 200);

		GridPane grid = initGrid(myDialogScene);
		Label categoryNameLabel = new Label("Category name");
		categoryNameLabel.setTextFill(FILL_COLOR);
		categoryNameLabel.setPrefSize(100, 20);	
		grid.add(categoryNameLabel,0,0,1,1);
		TextField categoryName = new TextField("Category 1");        
		grid.add(categoryName,1,0,1,1);
		Label categoryValueLabel = new Label("Category value");
		categoryValueLabel.setTextFill(FILL_COLOR);
		categoryValueLabel.setPrefSize(100, 20);	
		grid.add(categoryValueLabel,0,1,1,1);        
		TextField categoryValue = new TextField("img[src$=.jpg]");        
		grid.add(categoryValue,1,1,1,1);               

		Button saveButton = new Button("Save");
		saveButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				integrateCategory(PapaField.getFieldMapByIdType(CategoryField.class));
			}
		});
		grid.add(saveButton,1,2,2,1);

		int indexOffset = 4;

		Button button = new Button("Add category");
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				addCategoryField(grid, "Category", "", indexOffset);
			}
		});
		grid.add(button,1,3,2,1);

		reinitializePapaFields(grid, PapaField.getFieldMapByIdType(CategoryField.class));

		myDialog.setScene(myDialogScene);        

		myDialog.show();	
	}

	public void integrateCategory(String name, String value) {
		MainSearcher mainSearcher = MainSearcher.getSingleton(this, null);
		mainSearcher.addCategory(name, value);
	}

	public void integrateCategory(Map<String, PapaField> fieldMap) {
		MainSearcher mainSearcher = MainSearcher.getSingleton(this, null);
		for (String key : fieldMap.keySet()) 
			mainSearcher.addCategory("Category " + key.substring(key.indexOf(PapaField.SEPARATOR) + PapaField.SEPARATOR.length()), fieldMap.get(key).getTextField().getText());
	}

	//TODO: Add categories to loading (29-AUG-2017)
	public void initForm(GridPane grid, List<Command> commandList) {
		
		PapaButton searchButton = new SearchButton(this);
		PapaButton stopButton = new StopButton(this);

		BasicField pageField = new BasicField("-p", "https://www.marktplaats.nl/z.html?attributes=S%2C4548&priceTo=800%2C00&categoryId=2143&postcode=&distance=25000");
		grid.add(pageField.getTextField(),0,1,5,1);
		searchButton.getButton().setPrefSize(100, 20);		
		grid.add(searchButton.getButton(),5,1);
		stopButton.getButton().setPrefSize(50, 20);
		grid.add(stopButton.getButton(),6,1);

		Image stopIcon = new Image(HabitabberGUI.class.getResourceAsStream("/view/stop.png"));
		stopButton.getButton().setText("");
		stopButton.getButton().setGraphic(new ImageView(stopIcon));

		scrollPane.setPrefHeight(stage.getMaxHeight());
		grid.add(scrollPane, 4, 2, 3, 18);

		ColumnConstraints col1Constraints = new ColumnConstraints();
		col1Constraints.setPercentWidth(120);
		ColumnConstraints col2Constraints = new ColumnConstraints();
		col2Constraints.setPercentWidth(250);
		ColumnConstraints col3Constraints = new ColumnConstraints();
		col3Constraints.setPercentWidth(35);
		ColumnConstraints col4Constraints = new ColumnConstraints();
		col4Constraints.setPercentWidth(35);
		ColumnConstraints col5Constraints = new ColumnConstraints();
		col5Constraints.setPercentWidth(610);
		ColumnConstraints col6Constraints = new ColumnConstraints();
		col6Constraints.setPercentWidth(100);
		ColumnConstraints col7Constraints = new ColumnConstraints();
		col7Constraints.setPercentWidth(50);

		grid.getColumnConstraints().addAll(col1Constraints, col2Constraints, col3Constraints, col4Constraints, col5Constraints, col6Constraints, col7Constraints);

		List<String> existingRegExes = new ArrayList<>();
		Map<String, String> existingCategories = new LinkedHashMap<>();
		
		//Add input fields and fill SearchAction with it
		hIndex[REGEX_TYPE] = 2;
		Map<String, Command> existingCommands = new HashMap<>();
		if (commandList != null)
			for (Command command : commandList)
				existingCommands.put(CommandFactory.getClassNameByCommandParam(command.getName()), command);
		
		for (String cmd : Command.getRegisteredCommands()) {
			Command existingCommand = existingCommands.get(cmd);
			if (cmd.equals("PageCommand")) {
				if (existingCommand != null)
					pageField.getTextField().setText(existingCommand.getValue());
			} else if (cmd.equals("RegExCommand")) {
				if (existingCommand != null)
					for (String value : existingCommand.getValue().split(Command.DELIMITER)) {
						existingRegExes.add(value);
					}
			} else if (cmd.equals("CategoryCommand")) {
				if (existingCommand != null) {
					for (String value : existingCommand.getValue().split(CategoryCommand.VALUE_DELIMITER)) {
						String[] keyValuePair = value.split(CategoryCommand.KEY_DELIMITER);
						existingCategories.put(keyValuePair[0], keyValuePair[1]);
					}
				}
			} else {
				Label label = new Label(CommandFactory.getCommandAbbreviationByClassName(cmd));
				label.setTextFill(FILL_COLOR);
				grid.add(label, 0, hIndex[REGEX_TYPE]);
				if (cmd.equals("ParseImmediateCommand")) {
					ParseImmediateField field = new ParseImmediateField("-" + CommandFactory.getCommandParamByClassName(cmd));
					if (existingCommand != null)
						field.getCheckbox().setSelected(existingCommand.getValue().equals("y") ? true : false);
					grid.add(field.getCheckbox(), 1, hIndex[REGEX_TYPE], 3, 1);
				} else if (cmd.equals("TokenCommand")){
					PapaField field = new BasicField("-" + CommandFactory.getCommandParamByClassName(cmd), !existingCommands.isEmpty() ? "" : "huizen-en-kamers");
					if (existingCommand != null)
						field.getTextField().setText(existingCommand.getValue());
					grid.add(field.getTextField(), 1, hIndex[REGEX_TYPE], 3, 1);
				} else if (cmd.equals("CookieCommand")) {
					CookieField field = new CookieField("-" + CommandFactory.getCommandParamByClassName(cmd));
					if (existingCommand != null)
						field.getCheckbox().setSelected(existingCommand.getValue().equals("y") ? true : false);
					grid.add(field.getCheckbox(), 1, hIndex[REGEX_TYPE], 3, 1);
				} else {
					if (existingCommand != null)
						for (String value : existingCommand.getValue().split(Command.DELIMITER)) {
							PapaField field = new BasicField("-" + CommandFactory.getCommandParamByClassName(cmd));
							field.getTextField().setText(value);
							grid.add(field.getTextField(), 1, hIndex[REGEX_TYPE], 3, 1);
						}
					else {
						PapaField field = new BasicField("-" + CommandFactory.getCommandParamByClassName(cmd));	
						grid.add(field.getTextField(), 1, hIndex[REGEX_TYPE], 3, 1);
					}
				}
				//				grid.add(new Label(cmd + " ( -" + CommandFactory.getCommandParamByClassName(cmd) + " )"), 0, hIndex);
				hIndex[REGEX_TYPE]++;
			}
		}
		
		for (String str : new String[]{"description","media"}) {
			Boolean existingField = null;
			if (GuiCheckboxCommand.CHECKBOX_MAP != null)
				existingField = GuiCheckboxCommand.CHECKBOX_MAP.get(str);
			CheckBox checkBox = new CheckBox();
			if (existingField != null)
				checkBox.setSelected(existingField);
			else {
				checkBox.setSelected(true);
				if (str.equals("media")) {
					checkBox.setSelected(false);
				}
			}
			checkBoxMap.put(str, checkBox.isSelected());

			checkBox.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					CheckBox chk = (CheckBox) event.getSource();
					checkBoxMap.put(str, chk.isSelected());
				}
			});
			String labelName = str;
			if (str.length() > 1) {
				labelName = str.substring(0, 1).toUpperCase() + str.substring(1);
			}
			Label label = new Label(labelName);			
			label.setTextFill(FILL_COLOR);
			grid.add(label, 0, hIndex[REGEX_TYPE]);
			grid.add(checkBox, 1, hIndex[REGEX_TYPE], 2, 1);
			hIndex[REGEX_TYPE]++;
		}
		hIndexOffset[REGEX_TYPE] = hIndex[REGEX_TYPE]++;		
		grid.add(new AddRegExButton(this, grid).getButton(), 0, hIndexOffset[REGEX_TYPE]);
		
		String regExName = "Regex";
		if (existingRegExes.isEmpty()) {
			addRegExField(grid, regExName, ".*(((0)[1-9]{2}[0-9][-]?(\\s?)[1-9][0-9]{5})|((\\+31|0|0031)[1-9][0-9][-]?[1-9][0-9]{6})).*", null, null);
			addRegExField(grid, regExName, ".*(((\\+31|0|0031)6){1}[1-9]{1}[0-9]{7}).*", null, null);
			addRegExField(grid, regExName, ".*(((0)[1-9][-]?\\s?[1-9][0-9]{2}\\s?[0-9]{5})).*", null, null);
		} else {
			for (String existingRegEx : existingRegExes)
				addRegExField(grid, regExName, existingRegEx, null, null);
		}
		
		if (!existingCategories.isEmpty()) {
			Scene myDialogScene = new Scene(new VBox(), 400, 200);
			GridPane categoryGridDummy = initGrid(myDialogScene);
			for (String existingCategoryKey : existingCategories.keySet())
				addCategoryField(categoryGridDummy, "Category", existingCategories.get(existingCategoryKey), null);
		}
		
	}

	public void appendOutputText(String str) {
		stage.show();
	}

	private static int getRowCount(GridPane pane) {
		int numRows = pane.getRowConstraints().size();
		for (int i = 0; i < pane.getChildren().size(); i++) {
			Node child = pane.getChildren().get(i);
			if (child.isManaged()) {
				int rowIndex = GridPane.getRowIndex(child);
				numRows = Math.max(numRows, rowIndex + 1);
			}
		}
		return numRows;
	}

	public void setPage(Page page) {		
		if (getCheckboxConjunctions(page)) {
			if (scrollPane.getContent() == null) {
				GridPane grid = new GridPane();
				grid.setAlignment(Pos.TOP_LEFT);
				grid.setHgap(5);
				grid.setVgap(5);
				grid.setPadding(new Insets(5, 5, 5, 5));
				grid.setPrefHeight(600);
				grid.setStyle(INLINE_CSS_STYLE);
				scrollPane.setContent(new GridPane());
			}
			GridPane innerGrid = (GridPane)scrollPane.getContent();
			int rowCount = getRowCount(innerGrid);
			Hyperlink hyperlink = new Hyperlink(page.getUrl());
			hyperlink.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent t) {
					getHostServices().showDocument(hyperlink.getText());
				}
			});
			innerGrid.add(hyperlink, 0, rowCount);
			int index = 1;
			for (String key : page.getContent().keySet()) {
				if (checkBoxMap.get(key) == null || checkBoxMap.get(key)) {
					showContent(key, page, innerGrid, index, rowCount);
					index++;
				}
			}
			//			showContent("tel", page, innerGrid, 1, rowCount);
			//			showContent("price", page, innerGrid, 2, rowCount);

			if (page.getDescription() != null) {
				innerGrid.add(new PopupButton(this, "Show content", page).getButton(), 3, rowCount);
			}			

		}

		stage.show();
	}

	private void showContent(String contentName, Page page, GridPane innerGrid, int col, int row) {
		List<Object> content = page.getContent().get(contentName);
		if (content != null && !content.isEmpty()) {
			innerGrid.add(new Text(contentName + ": " + content.toString() + " "), col, row);
		}
	}
	
	public void showPopup(GridPane grid, String name) {
		final Popup popup = new Popup(); popup.setX(300); popup.setY(200);
		popup.getContent().addAll(new Circle(25, 25, 50, Color.AQUAMARINE));
		stage.show();
	}

	public boolean getCheckboxConjunctions(Page page) {
		for (String key : checkBoxMap.keySet()) {
			List<Object> content = page.getContent().get(key);
			if (checkBoxMap.get(key) && (content == null || content.isEmpty())) {
				return false;
			}
		}
		return true;
	}
	
	public static int getHIndexOffset(int type) {
		return hIndexOffset[type];
	}

	public Stage getStage() {
		return stage;
	}
	
	/*
	 * Field helper methods
	 */

	// babyField is the parent field of the field to add
	public void addRegExField(GridPane grid, String regExName, BabyField babyField) {
		addRegExField(grid, regExName, null, babyField);
	}

	// babyField is the parent field of the field to add
	public void addRegExField(GridPane grid, String regExName, Integer hIndex, BabyField babyField) {
		addRegExField(grid, regExName, "", hIndex, babyField);
	}

	// babyField is the parent field of the field to add	
	public void addRegExField(GridPane grid, String regExName, String regExValue, Integer hIndex, BabyField babyField) {
		int firstFreeHIndex;
		int indexByAddOrder = PapaField.getValuesOfFieldMap().size();		
		int type = REGEX_TYPE;
		if (babyField != null)					
			firstFreeHIndex = babyField.getHIndex() + 1;
		else if (hIndex == null)
			firstFreeHIndex = RegExField.retrieveFirstFreeHIndex();
		else
			firstFreeHIndex = RegExField.retrieveFirstFreeHIndex(hIndex);
		RegExField regExField = (RegExField) addBabyField(grid, regExName, regExValue, hIndex, firstFreeHIndex, REGEX_TYPE, indexByAddOrder);
		if (babyField instanceof RegExField)
			regExField.setParent((RegExField) babyField);
		if (hIndex == null) {
			HabitabberGUI.hIndex[REGEX_TYPE]++;
		}
	}

	public void addCategoryField(GridPane grid, String regExName, String regExValue, Integer hIndex) {
		int firstFreeHIndex;
		if (hIndex == null) {
			firstFreeHIndex = CategoryField.retrieveFirstFreeHIndex();	
		} else {
			firstFreeHIndex = CategoryField.retrieveFirstFreeHIndex(hIndex);
		}
		addBabyField(grid, regExName, regExValue, hIndex, firstFreeHIndex, CATEGORY_TYPE, firstFreeHIndex);
		if (hIndex == null) {
			HabitabberGUI.hIndex[CATEGORY_TYPE]++;
		}
	}

	public BabyField addBabyField(GridPane grid, String regExName, String regExValue, Integer hIndex, int firstFreeHIndex, int type, Integer indexByAddOrder) {
		List<Node> nodeList = new ArrayList<Node>();
		String name = regExName + indexByAddOrder;
		
		Image addSubExIcon = new Image(HabitabberGUI.class.getResourceAsStream("/view/object.png"));
		AddSubExButton addSubExButton = null;

		BabyField babyField = null;
		Control label = null; 
		if (type == CATEGORY_TYPE) {
			name += firstFreeHIndex;
			babyField = new CategoryField(name, regExValue);
			label = new TextField(regExName + " " + (firstFreeHIndex - getHIndexOffset(type)));			
			grid.add(label, 0, firstFreeHIndex);
			grid.add(babyField.getTextField(), 1, firstFreeHIndex);
		} else {
			babyField = new RegExField(name, regExValue, firstFreeHIndex, grid);
			label = new Label(regExName + " " + (firstFreeHIndex - getHIndexOffset(type)));		
			((Label)label).setTextFill(FILL_COLOR);
			grid.add(label, 0, firstFreeHIndex);
			grid.add(babyField.getTextField(), 1, firstFreeHIndex);
			addSubExButton = new AddSubExButton(this, grid, babyField);
			addSubExButton.getButton().setText("");
			addSubExButton.getButton().setGraphic(new ImageView(addSubExIcon));
		}

		Image removeIcon = new Image(HabitabberGUI.class.getResourceAsStream("/view/delete.png"));
		RemoveButton removeButton = new RemoveButton(name, new RemoveAction(this, babyField.getId()));
		removeButton.getButton().setText("");
		removeButton.getButton().setGraphic(new ImageView(removeIcon));
		grid.add(removeButton.getButton(), 2, firstFreeHIndex);
		nodeList.add(label);
		nodeList.add(babyField.getTextField());
		nodeList.add(removeButton.getButton());
		if (addSubExButton != null) {
			grid.add(addSubExButton.getButton(), 3, firstFreeHIndex);
			nodeList.add(addSubExButton.getButton());
			babyField.setAddSubExButton(addSubExButton);
		}
		babyField.setRegExRowNodes(nodeList);
		babyField.setRemoveButton(removeButton);	
		babyField.setLabel(label);
		
		return babyField;
	}

	public void reinitializePapaFields(GridPane grid, Map<String, PapaField> fieldMap) {
		for (String key : fieldMap.keySet()) {
			PapaField papaField = fieldMap.get(key);
			if (papaField instanceof BabyField) {
				BabyField babyField =  (BabyField)fieldMap.get(key);
				int hIndex = babyField.getHIndex();
				Control label = babyField.getLabel();
				if (label instanceof Label)
					((Label)label).setTextFill(FILL_COLOR);
				grid.add(label, 0, hIndex);
				grid.add(babyField.getTextField(), 1, hIndex);

				Image removeIcon = new Image(HabitabberGUI.class.getResourceAsStream("/view/delete.png"));
				RemoveButton removeButton = babyField.getRemoveButton();
				removeButton.getButton().setText("");
				removeButton.getButton().setGraphic(new ImageView(removeIcon));
				grid.add(removeButton.getButton(), 2, hIndex);
				AddSubExButton addSubExButton = babyField.getAddSubExButton();
				if (addSubExButton != null) {
					Image addSubExIcon = new Image(HabitabberGUI.class.getResourceAsStream("/view/object.png"));
					addSubExButton.getButton().setText("");
					addSubExButton.getButton().setGraphic(new ImageView(addSubExIcon));
					grid.add(addSubExButton.getButton(), 3, hIndex);
				}
			}
		}
	}

	/*
	 * Counter helper methods
	 */
	
	private Task<Void> createTask() {	    		    	

		return new Task<Void>() {
			@Override public Void call() {
				while (MainSearcher.COUNTER < MainSearcher.MAX_SITES) {
					if (isCancelled()) {
						break;
					}
					updateMessage((MainSearcher.MAX_SITES - MainSearcher.COUNTER) + "");
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						return null;
					}
				}

				updateMessage(0 + "");
				updateProgress(MainSearcher.MAX_SITES, MainSearcher.MAX_SITES);

				return null;
			}
		};
	}

	private HBox createLayout(Task<Void> task) {
		HBox layout = new HBox(10);

		layout.getChildren().setAll(
				createProgressIndicator(task),
				createCounter(task)
				);

		layout.setAlignment(Pos.CENTER_RIGHT);
		layout.setPadding(new Insets(10));
		layout.setStyle(INLINE_CSS_STYLE);

		return layout;
	}

	private ProgressIndicator createProgressIndicator(Task<Void> task) {
		ProgressIndicator progress = new ProgressIndicator();

		progress.progressProperty().bind(task.progressProperty());
		progress.setStyle(INLINE_CSS_STYLE);

		return progress;
	}

	private Label createCounter(Task<Void> task) {
		Label counter = new Label();

		counter.setMinWidth(20);
		counter.setAlignment(Pos.CENTER_RIGHT);
		counter.textProperty().bind(task.messageProperty());
		counter.setStyle(INLINE_CSS_STYLE);

		return counter;
	}

	public void startProgressIndicator() {
		if (progressIndicatorStage == null)
			progressIndicatorStage = new Stage();
		progressIndicatorStage.getIcons().add(HABITABBER_ICON);
		progressIndicatorStage.setTitle("Progress indicator");
		Task<Void> task = createTask();

		progressIndicatorStage.setScene(
				new Scene(
						createLayout(
								task
								)
						)
				);
		progressIndicatorStage.show();

		new Thread(task).start();
	}
	
	public void loadConfig(List<Command> commandList) {
		Scene scene = HabitabberGUI.stage.getScene();
		grid = resetGrid(scene);
		PapaField.resetFields();
		initForm(grid, commandList);
	}
	
	public List<String> getArgumentList() {
		Map<PapaField, Integer> fieldIndexMap = new HashMap<>();
		List<String> regExes = new ArrayList<>();
		List<String> categories = new ArrayList<>();
		List<String> argList = new ArrayList<String>();
		for (PapaField field : PapaField.getValuesOfFieldMap()) {
			if (field instanceof ParseImmediateField || field instanceof CookieField){  
				if (((CheckboxField)field).getCheckbox().isSelected()) {
					argList.add(field.getName());
					argList.add("y");
				} else {
					argList.add(field.getName());
					argList.add("n");
				}
			} else if (!(field instanceof RegExField) && !(field instanceof CategoryField)) {
				if (field.getTextField() != null) {				
					String value = field.getTextField().getText();
					if (value != null && !value.isEmpty()) { 
						argList.add(field.getName()); // The command
						for (String v : value.split(" ")) // The value(s)
							argList.add(v);
					}
				} 
			} else if (field instanceof RegExField) {			
				if (field.getTextField() != null) {									
					String value = field.getTextField().getText();
					if (value != null && !value.isEmpty()) {
						RegExField parent = ((RegExField) field).getParent();
						if (parent != null) {
							int parentIndex = fieldIndexMap.get(parent);
							value = regExes.get(parentIndex) + " --nc GREATER " + value; //TODO: JUST FOR TESTING!!! Make dynamical (11-SEP-2017)
							regExes.remove(parentIndex);
							regExes.add(parentIndex, value);
						} else 
							regExes.add(value);
						fieldIndexMap.put(field, regExes.size()-1);
					}
				}
			} else if (field instanceof CategoryField) {
				if (field.getTextField() != null) {
					String value = field.getTextField().getText();
					if (value != null && !value.isEmpty())
						categories.add(((TextField)((CategoryField) field).getLabel()).getText() + CategoryCommand.KEY_DELIMITER + value);
				}
			}
		}
		if (!regExes.isEmpty()) {
			argList.add("-r");
			for (String regEx : regExes)
				argList.add(regEx);
		}
		if (!categories.isEmpty()) {
			argList.add("-k");
			String categoryLine = "";
			for (String category : categories) {
				if (!categoryLine.isEmpty())
					categoryLine += CategoryCommand.VALUE_DELIMITER;
				categoryLine += category;
			}
			argList.add(categoryLine);
		}
		
		if (!checkBoxMap.isEmpty()) {
			argList.add("-gcb");
			for (String key : checkBoxMap.keySet())
				argList.add(key + GuiCheckboxCommand.VALUE_SEPARATOR + (checkBoxMap.get(key) ? "y" : "n"));
		}
		
		return argList;
	}

}
