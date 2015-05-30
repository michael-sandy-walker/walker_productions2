package view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import result.Page;
import controller.MainSearcher;
import utilities.command.Command;
import utilities.command.CommandFactory;
import view.action.RemoveAction;
import view.button.AddRegExButton;
import view.button.PapaButton;
import view.button.PopupButton;
import view.button.RemoveButton;
import view.button.SearchButton;
import view.button.StopButton;
import view.field.PapaField;
import view.field.ParseImmediateField;
import view.field.RegExField;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class HabitabberGUI extends Application {

	public static final String TITLE = "Habitabber";

	private static final TextArea outputTextArea = new TextArea();
	private static final ScrollPane scrollPane = new ScrollPane();

	private static Stage stage;

	private static GridPane grid;

	private static int hIndex = 2;

	private static int hIndexOffset = hIndex;

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

		initMenu(scene);
		GridPane grid = initGrid(scene);
		initForm(grid);

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
		menu11.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Resource File");
				File file = fileChooser.showOpenDialog(stage);								

				MainSearcher mainSearcher = MainSearcher.getSingleton(null);
				mainSearcher.addVisitedLinksFromFile(file.getAbsolutePath());
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

		//		grid.add(menuBar, 0, 0);
		((VBox) scene.getRoot()).getChildren().addAll(menuBar);
	}

	public void initForm(GridPane grid) {
		PapaButton searchButton = new SearchButton();
		PapaButton stopButton = new StopButton();

		CommandFactory.registerCommands();

		grid.add(new PapaField("-p", "http://www.marktplaats.nl/z.html?attributes=S%2C4548&priceTo=800%2C00&categoryId=2143&postcode=&distance=25000").getTextField(),0,1,4,1);
		searchButton.getButton().setPrefSize(100, 20);		
		grid.add(searchButton.getButton(),4,1);
		stopButton.getButton().setPrefSize(50, 20);
		grid.add(stopButton.getButton(),5,1);

		//		java.awt.Image stopIcon = java.awt.Toolkit.getDefaultToolkit().getImage(getClass().getResource("/view/stop.png"));
		Image stopIcon = new Image(HabitabberGUI.class.getResourceAsStream("/view/stop.png"));
		stopButton.getButton().setText("");
		stopButton.getButton().setGraphic(new ImageView(stopIcon));

		getOutputtextarea().setPrefHeight(600);
		//		grid.add(getOutputtextarea(), 3, 2, 3, 18);

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
		hIndex = 2;
		for (String cmd : Command.getRegisteredCommands()) {
			if (!cmd.equals("PageCommand") && !cmd.equals("RegExCommand")) {
				Label label = new Label(CommandFactory.getCommandAbbreviationByClassName(cmd));
				label.setTextFill(Color.web("0076a3"));
				grid.add(label, 0, hIndex);
				if (cmd.equals("ParseImmediateCommand")) {
					ParseImmediateField field = new ParseImmediateField("-" + CommandFactory.getCommandParamByClassName(cmd));
					grid.add(field.getCheckbox(), 1, hIndex, 2, 1);
				} else if (cmd.equals("TokenCommand")){
					PapaField field = new PapaField("-" + CommandFactory.getCommandParamByClassName(cmd), "huizen-en-kamers");
					grid.add(field.getTextField(), 1, hIndex, 2, 1);
				} else {
					PapaField field = new PapaField("-" + CommandFactory.getCommandParamByClassName(cmd));
					grid.add(field.getTextField(), 1, hIndex, 2, 1);
				}
				//				grid.add(new Label(cmd + " ( -" + CommandFactory.getCommandParamByClassName(cmd) + " )"), 0, hIndex);

				hIndex++;
			}
		}
		hIndexOffset = hIndex++;
		grid.add(new AddRegExButton(this, grid).getButton(), 0, hIndexOffset);
	}

	public static void appendOutputText(String str) {
		//		System.out.println("append:"+ str + "\n");
		//		HabitabberGUI.outputTextArea.appendText(str);
		stage.show();
	}

	/**
	 * @return the outputtextarea
	 */
	public static TextArea getOutputtextarea() {
		return outputTextArea;
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

	public static void setPage(Page page) {		

		//		GridPane grid;
		//		for (Node node : stage.getScene().getRoot().getChildren()) {
		//			if (node instanceof)
		//		}

		//		grid.add(getOutputtextarea(), 3, 2, 3, 18);
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
		innerGrid.add(new Text(page.getUrl()), 0, rowCount);	
		innerGrid.add(new Text(page.getContent().get("tel").toString()), 1, rowCount);				

		if (page.getDescription() != null) {
			innerGrid.add(new PopupButton("Show content", page).getButton(), 2, rowCount);
		}

		stage.show();
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

	public void addRegExField(GridPane grid, String regEx) {
		int firstFreeHIndex = PapaField.retrieveFirstFreeHIndex();
		List<Node> nodeList = new ArrayList<Node>();
		String name = "" + firstFreeHIndex;
		RegExField regExField = new RegExField(name, regEx);
		//		regExFieldList.add(regExField);
		Label label = new Label("Regex " + (firstFreeHIndex - getHIndexOffset()));		
		grid.add(label, 0, firstFreeHIndex);
		grid.add(regExField.getTextField(), 1, firstFreeHIndex);

		Image removeIcon = new Image(HabitabberGUI.class.getResourceAsStream("/view/delete.png"));
		RemoveButton removeButton = new RemoveButton(name, new RemoveAction(name));
		removeButton.getButton().setText("");
		removeButton.getButton().setGraphic(new ImageView(removeIcon));
		grid.add(removeButton.getButton(), 2, firstFreeHIndex);
		nodeList.add(label);
		nodeList.add(regExField.getTextField());
		nodeList.add(removeButton.getButton());
		regExField.setRegExRowNodes(nodeList);
		regExField.setHIndex(firstFreeHIndex);
		hIndex++;
	}

	public static int getHIndexOffset() {
		return hIndexOffset;
	}

	public static Stage getStage() {
		return stage;
	}
}
