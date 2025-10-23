/**
 * ok so this is just the UI code for the keno game *
 *
 * - Welcome Scene:
 * - the first screen u see with the "KENO" title
 * - has the "START GAME" button
 * - has the "Menu" dropdown on the top right
 *
 * - Game Scene:
 * - the second screen for the main game
 * - also has the "Menu" dropdown on the top right
 *
 * - Layout:
 * - Left side: all the controls (spots, drawings, buttons)
 * - Middle: the 8x10 grid of 80 buttons
 * - Right side: the results area (drawn numbers, winnings)
 * - Top: the HBox that holds the menu button on the right
 *
 * - Controls (Left Pane):
 * - Radio buttons for "Spots to Play" (1, 4, 8, 10)
 * - Radio buttons for "Number of Drawings" (1-4)
 * - "Submit Choices" button
 * - "Pick Random Numbers" button (disabled)
 * - "Start Drawing" button (disabled)
 * - "Play New Card" button (disabled)
 *
 * - Keno Grid (Center Pane):
 * - the 8x10 grid of 80 buttons
 * - they're all disabled until u hit "submit"
 * - made the buttons wider so the numbers + star show up
 *
 * - Results Display (Right Pane):
 * - the text box for the 20 drawn numbers
 * - Label "1." for This Drawing's Winnings
 * - Label "2." for Total Winnings
 *
 * - Menu Dropdown:
 * - "Rules" (shows a popup)
 * - "Odds of Winning" (shows a popup)
 * - "New Look" (changes the colors to dark mode)
 * - "Exit Game" (closes the app)
 *
 * - Styling:
 * - all the colors and fonts are at the top (no css files)
 * - has the 4 button styles: default, selected (green), drawn (blue), and matched (green star)
 * - has the "New Look" dark mode style
 *
 * - Connections:
 * - all the buttons are wired up with setOnAction
 * - but they just print to the console now, no real logic
 *
 */

// --- NEW IMPORTS FOR IMAGES AND ANIMATION ---
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration; // <-- NEW

import java.util.ArrayList;
import java.util.List;

public class JavaFXTemplate extends Application {

    private Stage primaryStage;
    private Scene welcomeScene, gameScene;

 // Pastel Forest Theme (Default) 
    private String styleDefault = "-fx-background-color: #F0F4F0; " +
                                  "-fx-font-family: 'Arial'; " +
                                  "-fx-text-fill: #3A3A3A; " +
                                  "-fx-base: #D8E0D8; " + // Default button color
                                  "-fx-control-inner-background: #FFFFFF;";
    
    // --- "Neon Purple" Theme (New Look) 
    private String styleNewLook = "-fx-background-color: #1E1E1E; " +
                                  "-fx-font-family: 'Consolas'; " +
                                  "-fx-text-fill: #EE82EE; " +     // Purple writing
                                  "-fx-base: #333333; " +             // Dark buttons
                                  "-fx-control-inner-background: #444444;" +
                                  "-fx-text-base-color: #EE82EE;"; // Purple button text

    // --- Button Styles (to match Pastel Forest) 
    private String buttonStyleDefault = "-fx-background-color: #DDE8D8; " +
                                        "-fx-border-color: #BCC8B8; " +
                                        "-fx-border-width: 1; " +
                                        "-fx-text-fill: #333333;";
    
    private String buttonStyleSelected = "-fx-background-color: #A5D6A7; " + // Brighter pastel green
                                         "-fx-border-color: #79B07B; " +
                                         "-fx-border-width: 2; " +
                                         "-fx-text-fill: #222222;";
    
    private String buttonStyleDrawn = "-fx-background-color: #D6EAF8; " + // Muted pastel blue
                                      "-fx-border-color: #AED6F1; " +
                                      "-fx-border-width: 1; " +
                                      "-fx-text-fill: #333333;";
    
    private String buttonStyleMatched = "-fx-background-color: #66BB6A; " + // Nice medium green
                                        "-fx-text-fill: white; " +
                                        "-fx-border-color: #4CAF50; " +
                                        "-fx-border-width: 2; " +
                                        "-fx-font-weight: bold;";
    // --- UI Components ---
    private List<Button> kenoGridButtons = new ArrayList<>();
    private ToggleGroup spotsGroup = new ToggleGroup();
    private ToggleGroup drawingsGroup = new ToggleGroup();
    private Button submitSelectionsButton;
    private Button randomPickButton;
    private Button startDrawingButton;
    private Button playNewCardButton;
    private Label drawingWinningsLabel;
    private Label totalWinningsLabel;
    private TextArea numbersDrawnArea;
    private BorderPane gameRoot; // Main root pane for the game scene

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Keno");

        //Build the Welcome Scene
        welcomeScene = createWelcomeScene();
        
        //Build the Game Scene
        gameScene = createGameScene();

        //Start with the Welcome Scene
        primaryStage.setScene(welcomeScene);
        primaryStage.show();
    }

    //Creates the initial Welcome Scene.
    private Scene createWelcomeScene() {
        BorderPane root = new BorderPane();
        root.setStyle(styleDefault);
        root.setPadding(new Insets(10));

        //Menu Control
        root.setTop(createMenuControls(false));

        //Center Content
        VBox centerBox = new VBox(20); // Adjusted spacing
        centerBox.setAlignment(Pos.CENTER);

        ImageView kenoLogo = new ImageView(new Image("images/Keno-Logo.png")); 
        kenoLogo.setFitWidth(400); // You can change this size
        kenoLogo.setPreserveRatio(true);

        Text developers = new Text("Game Developers:\nUshnah Hussain & Ariana Garcia");
        developers.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        developers.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        
        Button startGameButton = new Button("START GAME");
        startGameButton.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        startGameButton.setPrefSize(200, 60);

        //Event Handler
        // This is the "connection" to the logic.
        startGameButton.setOnAction(e -> {
            // Game logic would go here
            primaryStage.setScene(gameScene);
            System.out.println("Start Game button clicked!");
        });

        centerBox.getChildren().addAll(kenoLogo, developers, startGameButton);
        root.setCenter(centerBox);

        return new Scene(root, 1000, 700);
    }

    // GAME SCENE CREATION
    //creates the main Game Play Scene.
    private Scene createGameScene() {
        gameRoot = new BorderPane();
        gameRoot.setStyle(styleDefault);
        gameRoot.setPadding(new Insets(10));

        //Menu Control
        gameRoot.setTop(createMenuControls(true)); // true = is game scene

        //Left: Controls
        gameRoot.setLeft(createControlsPane());

        //Center: Keno Grid
        gameRoot.setCenter(createKenoGridPane());
        
        //Right: Results
        gameRoot.setRight(createResultsPane());

        return new Scene(gameRoot, 1000, 700);
    }

    //Creates the left-side VBox for game controls (Spots, Drawings, Buttons).
    private VBox createControlsPane() {
        VBox controlsPane = new VBox(15);
        controlsPane.setPadding(new Insets(10));
        controlsPane.setMinWidth(220);

        //Spots Selection
        VBox spotsBox = new VBox(5);
        spotsBox.getChildren().add(new Label("1. Spots to Play:"));
        RadioButton spot1 = new RadioButton("1 Spot");
        RadioButton spot4 = new RadioButton("4 Spots");
        RadioButton spot8 = new RadioButton("8 Spots");
        RadioButton spot10 = new RadioButton("10 Spots");
        spot1.setToggleGroup(spotsGroup);
        spot4.setToggleGroup(spotsGroup);
        spot8.setToggleGroup(spotsGroup);
        spot10.setToggleGroup(spotsGroup);
        spotsBox.getChildren().addAll(spot1, spot4, spot8, spot10);

        //Drawings Selection
        VBox drawingsBox = new VBox(5);
        drawingsBox.getChildren().add(new Label("2. Number of Drawings:"));
        RadioButton draw1 = new RadioButton("1 Drawing");
        RadioButton draw2 = new RadioButton("2 Drawings");
        RadioButton draw3 = new RadioButton("3 Drawings");
        RadioButton draw4 = new RadioButton("4 Drawings");
        draw1.setToggleGroup(drawingsGroup);
        draw2.setToggleGroup(drawingsGroup);
        draw3.setToggleGroup(drawingsGroup);
        draw4.setToggleGroup(drawingsGroup);
        drawingsBox.getChildren().addAll(draw1, draw2, draw3, draw4);

        //Submit Selections Button
        submitSelectionsButton = new Button("3. Submit Choices");
        submitSelectionsButton.setPrefWidth(200);
        submitSelectionsButton.setOnAction(e -> {
            System.out.println("Submit Choices button clicked!");
        });

        //Separator
        Separator sep1 = new Separator();

        //Random Pick Button
        randomPickButton = new Button("Pick Random Numbers");
        randomPickButton.setPrefWidth(200);
        randomPickButton.setOnAction(e -> {
            System.out.println("Pick Random Numbers button clicked!");
        });

        //Start Drawing Button
        startDrawingButton = new Button("Start Drawing");
        startDrawingButton.setPrefWidth(200);
        startDrawingButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        startDrawingButton.setOnAction(e -> {
            System.out.println("Start Drawing button clicked!");
        });

        // Separator
        Separator sep2 = new Separator();

        //Play New Card Button
        playNewCardButton = new Button("Play New Card");
        playNewCardButton.setPrefWidth(200);
        playNewCardButton.setOnAction(e -> {
            // resetGame(); // Logic call removed
            System.out.println("Play New Card button clicked!");
        });

        //Set initial button states
        randomPickButton.setDisable(true);
        startDrawingButton.setDisable(true);
        playNewCardButton.setDisable(true);

        controlsPane.getChildren().addAll(
            spotsBox, drawingsBox, submitSelectionsButton, sep1,
            randomPickButton, startDrawingButton, sep2, playNewCardButton
        );

        return controlsPane;
    }

    //Creates the center 8x10 GridPane 
    private GridPane createKenoGridPane() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(10));

        kenoGridButtons.clear(); 
        int number = 1;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 10; col++) {
                Button button = new Button(String.valueOf(number));
                button.setUserData(number); // Store the number itself
                button.setPrefSize(55, 45); 
                button.setFont(Font.font("Arial", FontWeight.BOLD, 12));
                button.setStyle(buttonStyleDefault);
                button.setDisable(true); // Disabled by default

                // Event handler for clicking a number
                button.setOnAction(e -> {
                    // We can get the number from the button's UserData
                    int num = (int) button.getUserData(); 
                    System.out.println("Button " + num + " clicked!");
                });
                kenoGridButtons.add(button);
                grid.add(button, col, row);
                number++;
            }
        }
        return grid;
    }

    //Creates the right-side VBox for displaying drawing results
    private VBox createResultsPane() {
        VBox resultsPane = new VBox(10);
        resultsPane.setPadding(new Insets(10));
        resultsPane.setMinWidth(220);

        Label title = new Label("Drawing Results");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Label drawnLabel = new Label("Numbers Drawn (20):");
        numbersDrawnArea = new TextArea();
        numbersDrawnArea.setEditable(false);
        numbersDrawnArea.setWrapText(true);
        numbersDrawnArea.setPrefHeight(200);

        // Labels are numbered, per request
        drawingWinningsLabel = new Label("Drawing Winnings $0.00");
        drawingWinningsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        totalWinningsLabel = new Label("Total Winnings $0.00");
        totalWinningsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        resultsPane.getChildren().addAll(
            title, drawnLabel, numbersDrawnArea, 
            drawingWinningsLabel, totalWinningsLabel
        );
        return resultsPane;
    }

    // MENU BAR AND EVENT HANDLER
     //Creates an HBox with a MenuButton aligned to the right.
     //isGameScene Adds "New Look" if true.
     //An HBox node to be placed at the top of the scene.
    private HBox createMenuControls(boolean isGameScene) {
        //Rules
        MenuItem rulesItem = new MenuItem("Rules");
        rulesItem.setOnAction(e -> showRules());

        //Odds
        MenuItem oddsItem = new MenuItem("Odds of Winning");
        oddsItem.setOnAction(e -> showOdds());
        
        //Exit 
        MenuItem exitItem = new MenuItem("Exit Game");
        exitItem.setOnAction(e -> Platform.exit());
        
        // Create the MenuButton (looks like a dropdown)
        MenuButton menuButton = new MenuButton("Menu");
        
        menuButton.getItems().addAll(rulesItem, oddsItem);

        //New Look (Game Scene Only)
        if (isGameScene) {
            MenuItem newLookItem = new MenuItem("New Look");
            newLookItem.setOnAction(e -> toggleNewLook());
            menuButton.getItems().add(2, newLookItem); 
        }

        menuButton.getItems().addAll(new SeparatorMenuItem(), exitItem);

        // Create an HBox to hold the button
        HBox menuContainer = new HBox(menuButton);
        menuContainer.setAlignment(Pos.CENTER_RIGHT); 
        menuContainer.setPadding(new Insets(5, 0, 5, 0)); 

        return menuContainer;
    }
    
    //new look     
    private void toggleNewLook() {
        Region welcomeRoot = (Region) welcomeScene.getRoot();
        Region gameRoot = (Region) gameScene.getRoot();

        if (gameRoot.getStyle().equals(styleDefault)) {
            welcomeRoot.setStyle(styleNewLook);
            gameRoot.setStyle(styleNewLook);
        } else {
            welcomeRoot.setStyle(styleDefault);
            gameRoot.setStyle(styleDefault);
        }
    }

    //Displays an Alert dialog with the game rules.
    private void showRules() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Keno Rules");
        alert.setHeaderText("How to Play Keno");
        alert.getDialogPane().setGraphic(null);
        alert.setContentText(
            "1. Choose how many 'Spots' you want to play (1, 4, 8, 10).\n" +
            "2. Choose how many drawings you want to play (1 to 4).\n" +
            "3. Click 'Submit Choices' to lock in your selections.\n" +
            "4. Pick your numbers on the grid, matching the number of spots you chose. Or, click 'Pick Random Numbers'.\n" +
            "5. Click 'Start Drawing'.\n"+
            "6. The game will draw 20 random numbers.\n" +
            "7. You win based on how many of your numbers match the drawn numbers.\n" +
            "8. If you have more drawings, click 'Continue to Next Drawing'.\n" +
            "9. When all drawings are finished, you can 'Play New Card' or 'Exit Game'."
        );
        alert.showAndWait();
    }

    /**
     * Displays an Alert dialog with the odds
     */
    private void showOdds() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Odds of Winning");
        alert.setHeaderText("Prize Chart & Odds");
        alert.getDialogPane().setGraphic(null); // This keeps your icon removed
        
        alert.setContentText(
            "10 SPOT GAME\n" +
            "Match 10: $100,000*\n" +
            "Match 9:  $4,250\n" +
            "Match 8:  $450\n" +
            "Match 7:  $40\n" +
            "Match 6:  $15\n" +
            "Match 5:  $2\n" +
            "Match 0:  $5\n" +
            "Overall Odds: 1 in 9.05\n\n" +

            "8 SPOT GAME\n" +
            "Match 8:  $10,000*\n" +
            "Match 7:  $750\n" +
            "Match 6:  $50\n" +
            "Match 5:  $12\n" +
            "Match 4:  $2\n" +
            "Overall Odds: 1 in 9.77\n\n" +

            "4 SPOT GAME\n" +
            "Match 4:  $75\n" +
            "Match 3:  $5\n" +
            "Match 2:  $1\n" +
            "Overall Odds: 1 in 3.86\n\n" +

            "1 SPOT GAME\n" +
            "Match 1:  $2\n" +
            "Overall Odds: 1 in 4.00"
        );
        
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}