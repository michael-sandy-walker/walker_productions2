package view;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
import utilities.command.Command;
import utilities.command.CommandFactory;
import view.action.RemoveAction;
import view.button.AddRegExButton;
import view.button.PapaButton;
import view.button.PopupButton;
import view.button.RemoveButton;
import view.button.SearchButton;
import view.button.StopButton;
import view.field.BabyField;
import view.field.BasicField;
import view.field.CategoryField;
import view.field.PapaField;
import view.field.ParseImmediateField;
import view.field.RegExField;
import controller.MainSearcher;

public class HabitabberGUI extends Application {

	public static final int REGEX_TYPE = 0;	
	public static final int CATEGORY_TYPE = 1;
	
	public static final String TITLE = "Habitabber";
	
	public static Image HABITABBER_ICON = new Image(HabitabberGUI.class.getResourceAsStream("/view/habitabber_64.png"));

	private static final ScrollPane scrollPane = new ScrollPane();

	private static Stage stage;

	private static GridPane grid;

	private static int hIndex[] = {2, 0};

	private static int hIndexOffset[] = {hIndex[REGEX_TYPE], 4};
	
	private Map<String, Boolean> checkBoxMap = new HashMap<String, Boolean>();
	
	private static Stage progressIndicatorStage = null;

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
//		primaryStage.getIcons().add(new Image(HabitabberGUI.class.getResourceAsStream("/view/delete-2.ico")));
		primaryStage.setTitle(TITLE);

		Scene scene = new Scene(new VBox(), 1200, 600);
		scene.setFill(Color.OLDLACE);
		
		
		primaryStage.getIcons().add(HABITABBER_ICON);
//		primaryStage.getIcons().add(new Image("http://goo.gl/kYEQl"));

		initMenu(scene);
		GridPane grid = initGrid(scene);
		initForm(grid);
		
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
		grid.setStyle("-fx-background-color: #1d1d1d; -fx-text-fill: white;");
		//		root = new StackPane();
		//		root.getChildren().add(grid);
		((VBox) scene.getRoot()).getChildren().add(grid);
		((VBox)scene.getRoot()).setStyle("-fx-background-color: #1d1d1d; -fx-text-fill: white;");
		return grid;
	}

	public void initMenu(Scene scene) {				

		final Menu menu1 = new Menu("File");
		MenuBar menuBar = new MenuBar();

		MenuItem menu11 = new MenuItem("Open");
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
		menuBar.getMenus().add(menu1);
		
		final Menu menu2 = new Menu("View");		

		MenuItem menu21 = new MenuItem("Clear output screen");
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
		
		MenuItem menu22 = new MenuItem("Show progress indicator");
		menu22.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (progressIndicatorStage != null)
					progressIndicatorStage.show();
			}
		});	
		menu2.getItems().add(menu22);
		
		menuBar.getMenus().add(menu2);
		
		final Menu menu3 = new Menu("Search");		

		MenuItem menu31 = new MenuItem("Add category");
		menu31.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				addCategory();				
			}
		});
		menu3.getItems().add(menu31);
		menuBar.getMenus().add(menu3);


		//		grid.add(menuBar, 0, 0);
		((VBox) scene.getRoot()).getChildren().addAll(menuBar);
	}
	
	private void addCategory() {
		final Stage myDialog = new Stage();
		myDialog.getIcons().add(HABITABBER_ICON);
		myDialog.setTitle("Categories");
        myDialog.initModality(Modality.WINDOW_MODAL);
        
//        Scene myDialogScene = new Scene(VBoxBuilder.create()                
//                .alignment(Pos.CENTER)
//                .padding(new Insets(10))
//                .build());
        Scene myDialogScene = new Scene(new VBox(), 400, 200);

        GridPane grid = initGrid(myDialogScene);
        Label categoryNameLabel = new Label("Category name");
        categoryNameLabel.setTextFill(Color.web("0076a3"));
        categoryNameLabel.setPrefSize(100, 20);	
        grid.add(categoryNameLabel,0,0,1,1);
        TextField categoryName = new TextField("Category");        
        grid.add(categoryName,1,0,1,1);
        Label categoryValueLabel = new Label("Category value");
        categoryValueLabel.setTextFill(Color.web("0076a3"));
        categoryValueLabel.setPrefSize(100, 20);	
        grid.add(categoryValueLabel,0,1,1,1);        
        TextField categoryValue = new TextField("Value");        
        grid.add(categoryValue,1,1,1,1);               
		
		 Button saveButton = new Button("Save");
		 saveButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					integrateCategory(categoryName.getText(), categoryValue.getText());
				}
			});
			grid.add(saveButton,1,2,2,1);
			
			 Button button = new Button("Add category");
				button.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						addCategoryField(grid, "Category", "", 4);
					}
				});
				grid.add(button,1,3,2,1);
      
        myDialog.setScene(myDialogScene);        
		
		myDialog.show();	
	}
	
	public void integrateCategory(String name, String value) {
		MainSearcher mainSearcher = MainSearcher.getSingleton(this, null);
		mainSearcher.addCategory(name, value);
	}

	public void initForm(GridPane grid) {
		PapaButton searchButton = new SearchButton(this);
		PapaButton stopButton = new StopButton(this);

		CommandFactory.registerCommands();

		grid.add(new BasicField("-p", "http://www.marktplaats.nl/z.html?attributes=S%2C4548&priceTo=800%2C00&categoryId=2143&postcode=&distance=25000").getTextField(),0,1,4,1);
		searchButton.getButton().setPrefSize(100, 20);		
		grid.add(searchButton.getButton(),4,1);
		stopButton.getButton().setPrefSize(50, 20);
		grid.add(stopButton.getButton(),5,1);

		//		java.awt.Image stopIcon = java.awt.Toolkit.getDefaultToolkit().getImage(getClass().getResource("/view/stop.png"));
		Image stopIcon = new Image(HabitabberGUI.class.getResourceAsStream("/view/stop.png"));
		stopButton.getButton().setText("");
		stopButton.getButton().setGraphic(new ImageView(stopIcon));

		scrollPane.setPrefHeight(stage.getMaxHeight());
		grid.add(scrollPane, 3, 2, 3, 18);

		ColumnConstraints col1Constraints = new ColumnConstraints();
		col1Constraints.setPercentWidth(120);
		ColumnConstraints col2Constraints = new ColumnConstraints();
		col2Constraints.setPercentWidth(250);
		ColumnConstraints col3Constraints = new ColumnConstraints();
		col3Constraints.setPercentWidth(50);
		ColumnConstraints col4Constraints = new ColumnConstraints();
		col4Constraints.setPercentWidth(630);
		ColumnConstraints col5Constraints = new ColumnConstraints();
		col5Constraints.setPercentWidth(100);
		ColumnConstraints col6Constraints = new ColumnConstraints();
		col6Constraints.setPercentWidth(50);

		grid.getColumnConstraints().addAll(col1Constraints, col2Constraints, col3Constraints, col4Constraints, col5Constraints, col6Constraints);

		//Add input fields and fill SearchAction with it
		hIndex[REGEX_TYPE] = 2;
		for (String cmd : Command.getRegisteredCommands()) {
			if (!cmd.equals("PageCommand") && !cmd.equals("RegExCommand")) {
				Label label = new Label(CommandFactory.getCommandAbbreviationByClassName(cmd));
				label.setTextFill(Color.web("0076a3"));
				grid.add(label, 0, hIndex[REGEX_TYPE]);
				if (cmd.equals("ParseImmediateCommand")) {
					ParseImmediateField field = new ParseImmediateField("-" + CommandFactory.getCommandParamByClassName(cmd));
					grid.add(field.getCheckbox(), 1, hIndex[REGEX_TYPE], 2, 1);
				} else if (cmd.equals("TokenCommand")){
					PapaField field = new BasicField("-" + CommandFactory.getCommandParamByClassName(cmd), "huizen-en-kamers");
					grid.add(field.getTextField(), 1, hIndex[REGEX_TYPE], 2, 1);
				} else {
					PapaField field = new BasicField("-" + CommandFactory.getCommandParamByClassName(cmd));
					grid.add(field.getTextField(), 1, hIndex[REGEX_TYPE], 2, 1);
				}
				//				grid.add(new Label(cmd + " ( -" + CommandFactory.getCommandParamByClassName(cmd) + " )"), 0, hIndex);

				hIndex[REGEX_TYPE]++;
			}
		}
		for (String str : new String[]{"description","media"}) {
			CheckBox checkBox = new CheckBox();
			checkBox.setSelected(true);
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
			label.setTextFill(Color.web("0076a3"));
			grid.add(label, 0, hIndex[REGEX_TYPE]);
			grid.add(checkBox, 1, hIndex[REGEX_TYPE], 2, 1);
			hIndex[REGEX_TYPE]++;
		}
		hIndexOffset[REGEX_TYPE] = hIndex[REGEX_TYPE]++;		
		grid.add(new AddRegExButton(this, grid).getButton(), 0, hIndexOffset[REGEX_TYPE]);
		
//		String regExName = "RegEx";
//		addRegExField(grid, regExName, ".*(((0)[1-9]{2}[0-9][-]?(\\s?)[1-9][0-9]{5})|((\\+31|0|0031)[1-9][0-9][-]?[1-9][0-9]{6})).*", null);
//		addRegExField(grid, regExName, ".*(((\\+31|0|0031)6){1}[1-9]{1}[0-9]{7}).*", null);
//		addRegExField(grid, regExName, ".*(((0)[1-9][-]?\\s?[1-9][0-9]{2}\\s?[0-9]{5})).*", null);
	}

	public void appendOutputText(String str) {
		//		System.out.println("append:"+ str + "\n");
		//		HabitabberGUI.outputTextArea.appendText(str);
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
				grid.setStyle("-fx-background-color: #1d1d1d; -fx-text-fill: white;");
				scrollPane.setContent(new GridPane());
			}
			GridPane innerGrid = (GridPane)scrollPane.getContent();
			int rowCount = getRowCount(innerGrid);
			Hyperlink hyperlink =new Hyperlink(page.getUrl());
			hyperlink.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent t) {
					getHostServices().showDocument(hyperlink.getText());
				}
			});
			innerGrid.add(hyperlink, 0, rowCount);
			int index = 1;
			for (String key : page.getContent().keySet()) {
				showContent(key, page, innerGrid, index, rowCount);
				index++;
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

	public boolean getCheckboxConjunctions(Page page) {
		for (String key : checkBoxMap.keySet()) {
			List<Object> content = page.getContent().get(key);
			if (checkBoxMap.get(key) && (content == null || content.isEmpty())) {
				return false;
			}
		}
		return true;
	}



	public void showPopup(GridPane grid, String name) {
		final Popup popup = new Popup(); popup.setX(300); popup.setY(200);
		popup.getContent().addAll(new Circle(25, 25, 50, Color.AQUAMARINE));


		//		Button hide = new Button("Hide");
		//		hide.setOnAction(new EventHandler<ActionEvent>() {
		//			@Override public void handle(ActionEvent event) {
		//				popup.hide();
		//			}
		//		});
		//		VBox layout = (VBox) stage.getScene().getRoot();
		//		layout.getChildren().addAll(show, hide);
		stage.show();
	}
	
	public void addRegExField(GridPane grid, String regExName) {
		addRegExField(grid, regExName, null);
	}
	
	public void addRegExField(GridPane grid, String regExName, Integer hIndex) {
		addRegExField(grid, regExName, "", hIndex);
	}
	
	public void addRegExField(GridPane grid, String regExName, String regExValue, Integer hIndex) {
		int firstFreeHIndex;
		if (hIndex == null) {
			firstFreeHIndex = RegExField.retrieveFirstFreeHIndex();	
		} else {
			firstFreeHIndex = RegExField.retrieveFirstFreeHIndex(hIndex);
		}
		addPapaField(grid, regExName, regExValue, hIndex, firstFreeHIndex, REGEX_TYPE);
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
		addPapaField(grid, regExName, regExValue, hIndex, firstFreeHIndex, CATEGORY_TYPE);
		if (hIndex == null) {
			HabitabberGUI.hIndex[CATEGORY_TYPE]++;
		}
	}
	
	public void addPapaField(GridPane grid, String regExName, String regExValue, Integer hIndex, int firstFreeHIndex, int type) {
		List<Node> nodeList = new ArrayList<Node>();
		String name = "" + firstFreeHIndex;
		
		BabyField babyField = null;
		if (type == CATEGORY_TYPE)
			babyField = new CategoryField(name, regExValue);
		else
			babyField = new RegExField(name, regExValue);
		//		regExFieldList.add(regExField);
		Label label = new Label(regExName + " " + (firstFreeHIndex - getHIndexOffset(type)));		
		label.setTextFill(Color.web("0076a3"));
		grid.add(label, 0, firstFreeHIndex);
		grid.add(babyField.getTextField(), 1, firstFreeHIndex);

		Image removeIcon = new Image(HabitabberGUI.class.getResourceAsStream("/view/delete.png"));
		RemoveButton removeButton = new RemoveButton(name, new RemoveAction(this, babyField.getId()));
		removeButton.getButton().setText("");
		removeButton.getButton().setGraphic(new ImageView(removeIcon));
		grid.add(removeButton.getButton(), 2, firstFreeHIndex);
		nodeList.add(label);
		nodeList.add(babyField.getTextField());
		nodeList.add(removeButton.getButton());
		babyField.setRegExRowNodes(nodeList);
		babyField.setHIndex(firstFreeHIndex);		
	}

	public static int getHIndexOffset(int type) {
		return hIndexOffset[type];
	}

	public Stage getStage() {
		return stage;
	}
	
	
	
	
	
	
	

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

	        return layout;
	    }

	    private ProgressIndicator createProgressIndicator(Task<Void> task) {
	        ProgressIndicator progress = new ProgressIndicator();

	        progress.progressProperty().bind(task.progressProperty());

	        return progress;
	    }

	    private Label createCounter(Task<Void> task) {
	        Label counter = new Label();

	        counter.setMinWidth(20);
	        counter.setAlignment(Pos.CENTER_RIGHT);
	        counter.textProperty().bind(task.messageProperty());
	        counter.setStyle("-fx-border-color: forestgreen;");

	        return counter;
	    }
	    
	    public void startProgressIndicator() {
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
	
}
