package view;

import java.io.File;

import controller.MainSearcher;
import utilities.command.Command;
import utilities.command.CommandFactory;
import view.button.PapaButton;
import view.button.SearchButton;
import view.button.StopButton;
import view.field.PapaField;
import view.field.ParseImmediateField;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class HabitabberGUI extends Application {

	public static final String TITLE = "Habitabber";
	
	private static final TextArea outputTextArea = new TextArea();

	private static Stage stage;
	
	/**
	 * Only for test reasons.
	 * @param argv The arguments
	 */
	public static void main(String[] argv) {
		launch(argv);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.stage= primaryStage;
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
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.TOP_LEFT);
		grid.setHgap(5);
		grid.setVgap(5);
		grid.setPadding(new Insets(5, 5, 5, 5));
		grid.setPrefSize(scene.getWidth(), scene.getHeight());
		grid.setStyle("-fx-background-color: #1d1d1d; -fx-text-fill: white;");
		//		root = new StackPane();
		//		root.getChildren().add(grid);
		((VBox) scene.getRoot()).getChildren().add(grid);
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
		
		grid.add(new PapaField("-p").getTextField(),0,1,3,1);
		searchButton.getButton().setPrefSize(100, 20);		
		grid.add(searchButton.getButton(),3,1);
		stopButton.getButton().setPrefSize(50, 20);
		grid.add(stopButton.getButton(),4,1);
		
		Image stopIcon = new Image(HabitabberGUI.class.getResourceAsStream("rsz_stop.png"));
		stopButton.getButton().setText("");
		stopButton.getButton().setGraphic(new ImageView(stopIcon));
		
		getOutputtextarea().setPrefHeight(600);
		grid.add(getOutputtextarea(), 2, 2, 3, 10);
		
		ColumnConstraints col1Constraints = new ColumnConstraints();
		col1Constraints.setPercentWidth(120);
		ColumnConstraints col2Constraints = new ColumnConstraints();
		col2Constraints.setPercentWidth(300);
		ColumnConstraints col3Constraints = new ColumnConstraints();
		col3Constraints.setPercentWidth(630);
		ColumnConstraints col4Constraints = new ColumnConstraints();
		col4Constraints.setPercentWidth(100);
		ColumnConstraints col5Constraints = new ColumnConstraints();
		col5Constraints.setPercentWidth(50);
		
		grid.getColumnConstraints().addAll(col1Constraints, col2Constraints, col3Constraints, col4Constraints, col5Constraints);

		//Add input fields and fill SearchAction with it
		int hIndex = 2;
		for (String cmd : Command.getRegisteredCommands()) {
			if (!cmd.equals("PageCommand")) {
				Label label = new Label(CommandFactory.getCommandAbbreviationByClassName(cmd));
				label.setTextFill(Color.web("0076a3"));
				grid.add(label, 0, hIndex);
				if (cmd.equals("ParseImmediateCommand")) {
					ParseImmediateField field = new ParseImmediateField("-" + CommandFactory.getCommandParamByClassName(cmd));
					grid.add(field.getCheckbox(), 1, hIndex);
				} else {
					PapaField field = new PapaField("-" + CommandFactory.getCommandParamByClassName(cmd));
					grid.add(field.getTextField(), 1, hIndex);
				}
//				grid.add(new Label(cmd + " ( -" + CommandFactory.getCommandParamByClassName(cmd) + " )"), 0, hIndex);
				
				hIndex++;
			}
		}
	}
	
	public static void appendOutputText(String str) {
		System.out.println("append:"+ str);
		outputTextArea.appendText(str);
		stage.show();
	}

	/**
	 * @return the outputtextarea
	 */
	public static TextArea getOutputtextarea() {
		return outputTextArea;
	}
}
