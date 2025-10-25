/**
 * ok so this is just the UI code for the keno game *
 *
 * Welcome Scene:
 * - the first screen u see with the "KENO" title
 * - has the "START GAME" button
 * - has the "Menu" dropdown on the top right
 *
 * Game Scene:
 * - the second screen for the main game
 * - also has the "Menu" dropdown on the top right
 *
 * Layout:
 * - Left side: all the controls (spots, drawings, buttons)
 * - Middle: the 8x10 grid of 80 buttons
 * - Right side: the results area (drawn numbers, winnings)
 * - Top: the HBox that holds the menu button on the right
 *
 * Controls (Left Pane):
 * - Radio buttons for "Spots to Play" (1, 4, 8, 10)
 * - Radio buttons for "Number of Drawings" (1-4)
 * - "Submit Choices" button
 * - "Pick Random Numbers" button (disabled)
 * - "Start Drawing" button (disabled)
 * - "Play New Card" button (disabled)
 *
 * Keno Grid (Center Pane):
 * - the 8x10 grid of 80 buttons
 * - they're all disabled until u hit "submit"
 * - made the buttons wider so the numbers + star show up
 *
 * Results Display (Right Pane):
 * - the text box for the 20 drawn numbers
 * - Label "1." for This Drawing's Winnings
 * - Label "2." for Total Winnings
 *
 * Menu Dropdown:
 * - "Rules" (shows a popup)
 * - "Odds of Winning" (shows a popup)
 * - "New Look" (changes the colors to dark mode)
 * - "Exit Game" (closes the app)
 *
 * Styling:
 * - all the colors and fonts are at the top (no css files)
 * - has the 4 button styles: default, selected (green), drawn (blue), and matched (green star)
 * - has the "New Look" dark mode style
 *
 * Connections:
 * - all the buttons are wired up with setOnAction
 * - but they just print to the console now, no real logic
 *
 */

import javafx.animation.KeyFrame; 
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
import javafx.util.Duration; 
import java.util.ArrayList;
import java.util.List;

public class JavaFXTemplate extends Application {

    private Stage primaryStage;
    private Scene welcomeScene, gameScene;

    private String styleDefault = "-fx-background-color: #F0F4F0; " +
                                  "-fx-font-family: 'Tahoma'; " +
                                  "-fx-text-fill: #3A3A3A; " +
                                  "-fx-base: #D8E0D8; " + // Default button color
                                  "-fx-control-inner-background: #FFFFFF;";
    
    private String styleNewLook = "-fx-background-color: #1E1E1E; " +
                                  "-fx-font-family: 'Consolas'; " +
                                  "-fx-text-fill: #EE82EE; " +     // Purple writing
                                  "-fx-base: #333333; " +             // Dark buttons
                                  "-fx-control-inner-background: #444444;" +
                                  "-fx-text-base-color: #EE82EE;"; // Purple button text

    private String noFadeGridButtons = ".keno-grid-button:disabled { -fx-opacity: 1.0; }";

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
    //UI Components
    private List<Button> kenoGridButtons = new ArrayList<>();
    private ToggleGroup spotsGroup = new ToggleGroup();
    private ToggleGroup drawingsGroup = new ToggleGroup();
    private Button submitSelectionsButton;
    private Button randomPickButton;
    private Button startDrawingButton;
    private Button playNewCardButton;
    private Label drawingWinningsLabel;
    private Label totalWinningsLabel;
    private TextArea numbersDrawnArea; // <-- BACK TO TextArea
    private BorderPane gameRoot; // Main root pane for the game scene

    private VBox spotsBox;
    private VBox drawingsBox;
    private int numSpotsToPick = 0; // Number of spots chosen by the player
    private int animationCounter = 0; // For drawing animation
    
    //ARIANA
    private Keno_Game kenoGame;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Keno");
        
        kenoGame = new Keno_Game(100); // Start player with $100
        //Build the Welcome Scene
        welcomeScene = createWelcomeScene();
        
        //Build the Game Scene
        gameScene = createGameScene();

        //Start with the Welcome Scene
        primaryStage.setScene(welcomeScene);
        primaryStage.setOnCloseRequest(e -> Platform.exit()); // Ensure app closes
        primaryStage.show();
    }
    
    
     //This method now handles the logic for *all* drawings.
     //isFirstDrawing True if this is the first draw (locks in numbers), false for subsequent draws.
    
    private void runDrawing(boolean isFirstDrawing) {
        // Disable buttons to prevent clicks during animation/processing
        startDrawingButton.setDisable(true);
        playNewCardButton.setDisable(true);
        randomPickButton.setDisable(true);
        
        List<Integer> playerNumbers = getSelectedNumbers();
        
        if (isFirstDrawing) {
            // This is the first draw, so lock in the player's numbers
            kenoGame.getPlayer().selectNumbers(playerNumbers);
        }
        
        // The '1' is the bet amount ($1)
        boolean success = kenoGame.processDrawing(playerNumbers, 1);
        
        if (success) {
            animateDrawing();
        } else {
            // Show an error to the user (e.g., out of money)
        	Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Drawing Error");
            alert.setHeaderText("Could not process drawing.");
            alert.setContentText("An unexpected error occurred. Are you out of funds?");
            alert.showAndWait();
            
            // Re-enable the "start" button if something failed
            startDrawingButton.setDisable(false);
        }
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

        ImageView kenoLogo = null;
        // lollll it wasnt working but we got ittt
        try {
            kenoLogo = new ImageView(new Image("images/Keno-Logo.png"));
            kenoLogo.setFitWidth(400); // You can change this size
            kenoLogo.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("Error loading logo: images/Keno-Logo.png not found.");
            kenoLogo = new ImageView(); // Create empty view to avoid crash
        }

        Text developers = new Text("Game Developers:\nUshnah Hussain & Ariana Garcia");
        developers.setFont(Font.font("Tahoma", FontWeight.NORMAL, 16));
        developers.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        
        Button startGameButton = new Button("START GAME");
        startGameButton.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        startGameButton.setPrefSize(200, 60);

        //Event Handler
        startGameButton.setOnAction(e -> {
            primaryStage.setScene(gameScene);
            System.out.println("Start Game button clicked! Switching to game scene.");
        });

        centerBox.getChildren().addAll(kenoLogo, developers, startGameButton);
        root.setCenter(centerBox);

        Scene scene = new Scene(root, 1000, 700);
        
        scene.getStylesheets().add("data:text/css," + noFadeGridButtons.replace(" ", "%20"));
        
        return scene;
    }

    // GAME SCENE CREATION
    //creates the main Game Play Scene.
    private Scene createGameScene() {
        gameRoot = new BorderPane();
        gameRoot.setStyle(styleDefault);
        gameRoot.setPadding(new Insets(20, 0, 20, 20));

        //Menu Control
        gameRoot.setTop(createMenuControls(true)); // true = is game scene

        //Left: Controls
        gameRoot.setLeft(createControlsPane());

        //Center: Keno Grid
        gameRoot.setCenter(createKenoGridPane());
        
        //Right: Results
        gameRoot.setRight(createResultsPane());

        Scene scene = new Scene(gameRoot, 1000, 700);
        scene.getStylesheets().add("data:text/css," + noFadeGridButtons.replace(" ", "%20"));
        
        return scene;
    }

    //Creates the left-side VBox for game controls (Spots, Drawings, Buttons).
    private VBox createControlsPane() {
        VBox controlsPane = new VBox(15);
        controlsPane.setPadding(new Insets(10));
        controlsPane.setMinWidth(220); 
        controlsPane.setMaxWidth(220); 
        controlsPane.setAlignment(Pos.TOP_LEFT); 

        //Spots Selection
        spotsBox = new VBox(5);
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
        drawingsBox = new VBox(5);
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
        submitSelectionsButton.setOnAction(e -> handleSubmitChoices());

        //Separator
        Separator sep1 = new Separator();

        //Random Pick Button
        randomPickButton = new Button("Pick Random Numbers");
        randomPickButton.setPrefWidth(200);
        randomPickButton.setOnAction(e -> handleRandomPick());

        //Start Drawing Button
        startDrawingButton = new Button("Start Drawing");
        startDrawingButton.setPrefWidth(200);
        startDrawingButton.setFont(Font.font("Tahoma", FontWeight.BOLD, 14));
        startDrawingButton.setOnAction(e -> {
            System.out.println("Start Drawing button clicked!");
            runDrawing(true); // This is the FIRST drawing
        });

        // Separator
        Separator sep2 = new Separator();

        //Play New Card Button
        playNewCardButton = new Button("Play New Card");
        playNewCardButton.setPrefWidth(200);
        playNewCardButton.setOnAction(e -> {
            System.out.println("Play New Card button clicked!");
            resetGameUI(); 
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
        grid.setHgap(4); 
        grid.setVgap(4); 
        grid.setPadding(new Insets(10)); 

        kenoGridButtons.clear(); 
        int number = 1;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 10; col++) {
                Button button = new Button(String.valueOf(number));
                button.setUserData(number); // Store the number itself
                button.setPrefSize(55, 45); 
                button.setFont(Font.font("Tahoma", FontWeight.BOLD, 12));
                button.setStyle(buttonStyleDefault);
                button.setDisable(true); // Disabled by default
                
                button.getStyleClass().add("keno-grid-button");

                // Event handler for clicking a number
                button.setOnAction(e -> {
                    int num = (int) button.getUserData(); 
                    System.out.println("Button " + num + " clicked!");
                    
                    // Toggle the button's style
                    if (button.getStyle().equals(buttonStyleDefault)) {
                        // Only allow selection if player hasn't picked max spots
                        if (getSelectedNumbers().size() < numSpotsToPick) {
                            button.setStyle(buttonStyleSelected);
                        }
                    } else {
                        button.setStyle(buttonStyleDefault);
                    }
                    // Check if we should enable/disable the Start button
                    updateGridAndButtonStates();
                });
                
                kenoGridButtons.add(button);
                grid.add(button, col, row);
                number++;
            }
        }
        return grid;
    }

    //Creates the right-side VBox for displaying drawing results.
    private VBox createResultsPane() {
        VBox resultsPane = new VBox(10);
        resultsPane.setPadding(new Insets(10, 0, 10, 10)); 
        resultsPane.setMinWidth(300); 
        resultsPane.setMaxWidth(300); 
        resultsPane.setAlignment(Pos.TOP_LEFT); 

        Label title = new Label("Drawing Results");
        title.setFont(Font.font("Tahoma", FontWeight.BOLD, 16));

        Label drawnLabel = new Label("Numbers Drawn (20):");
        
        numbersDrawnArea = new TextArea();
        numbersDrawnArea.setEditable(false);
        numbersDrawnArea.setWrapText(true); 
        numbersDrawnArea.setPrefHeight(200);
        numbersDrawnArea.setMinHeight(200);
        numbersDrawnArea.setMaxHeight(200);
        numbersDrawnArea.setPrefWidth(280); 
        numbersDrawnArea.setMinWidth(280); 
        numbersDrawnArea.setMaxWidth(280); 

        drawingWinningsLabel = new Label("Current Drawing: $0.00");
        totalWinningsLabel = new Label("Total: $0.00");
        applyThemeStyles(styleDefault); 

        resultsPane.getChildren().addAll(
            title, drawnLabel, numbersDrawnArea, 
            drawingWinningsLabel, totalWinningsLabel
        );
        return resultsPane;
    }

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
        menuContainer.setPadding(new Insets(5, 10, 5, 0)); 

        return menuContainer;
    }
    
    //Applies the correct styles to the labels and text flow
    //This is called when the scene is created and when the theme is toggled.     
    private void applyThemeStyles(String style) {
        String textAreaSpecificStyle = "-fx-vbar-policy: never; -fx-hbar-policy: never;"; // <-- HIDE SCROLL BARS
        
        if (style.equals(styleDefault)) {
            //Default Theme Winnings 
            drawingWinningsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 11pt; -fx-text-fill: #006400;"); // Dark Green
            totalWinningsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16pt; -fx-text-fill: #006400;");
            numbersDrawnArea.setStyle("-fx-font-family: 'Tahoma'; -fx-font-size: 18pt; " + textAreaSpecificStyle);
        } else {
            //New Look Winnings
            drawingWinningsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 11pt; -fx-text-fill: #39FF14;"); // Neon Green
            totalWinningsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16pt; -fx-text-fill: #39FF14;");
            numbersDrawnArea.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 18pt; " + textAreaSpecificStyle);
        }
    }
    
    //new look     
    private void toggleNewLook() {
        Region welcomeRoot = (Region) welcomeScene.getRoot();
        Region gameRoot = (Region) gameScene.getRoot();

        if (gameRoot.getStyle().equals(styleDefault)) {
            welcomeRoot.setStyle(styleNewLook);
            gameRoot.setStyle(styleNewLook);
            applyThemeStyles(styleNewLook); //
        } else {
            welcomeRoot.setStyle(styleDefault);
            gameRoot.setStyle(styleDefault);
            applyThemeStyles(styleDefault);
        }
    }

    //Displays the game rules.
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

    //Displays the odds
    private void showOdds() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Odds of Winning");
        alert.setHeaderText("Prize Chart & Odds");
        alert.getDialogPane().setGraphic(null);
        
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
    
    
    
     //This method now handles the logic all drawings.
     //It loops through the grid and finds all buttons styled as 'selected'.
     //return A List of Integers (the numbers the player selected).
     
    private List<Integer> getSelectedNumbers() {
        List<Integer> selectedNumbers = new ArrayList<>();
        for (Button b : kenoGridButtons) {
            if (b.getStyle().equals(buttonStyleSelected)) {
                selectedNumbers.add((Integer) b.getUserData());
            }
        }
        return selectedNumbers;
    }

     //Called when "3. Submit Choices" is clicked.
     //Validates selections and enables the Keno grid.
    private void handleSubmitChoices() {
        int spots = getSelectedSpotsCount();
        int drawings = getSelectedDrawingsCount();

        // If nothing is selected just do nothing.
        if (spots == 0 || drawings == 0) {
            return;
        }
        
        //Tell the backend to start the game 
        try {
            kenoGame.startGame(spots, drawings);
        } catch (IllegalArgumentException ex) {
            // This should not happen if radio buttons are set up correctly
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid spots selection: " + ex.getMessage());
            alert.showAndWait();
            return;
        }

        // Store the number of spots to pick
        numSpotsToPick = spots;
        System.out.println("Player must pick " + numSpotsToPick + " spots.");

        // Disable spots and drawings controls
        setControlsDisabled(spotsBox, true);
        setControlsDisabled(drawingsBox, true);
        
        // Enable grid and random pick
        for (Button b : kenoGridButtons) {
            b.setDisable(false);
        }
        randomPickButton.setDisable(false);
        
        // Disable this button
        submitSelectionsButton.setDisable(true);
    }

     //Called when "Pick Random Numbers" is clicked.
     //Selects random numbers on the grid for the player.
    private void handleRandomPick() {
        kenoGame.getPlayer().quickPick(numSpotsToPick); 
        
        //Get the numbers that were just picked from the player object.
        List<Integer> randomPicks = kenoGame.getPlayer().getSelectedNumbers();

        // Style the buttons on the grid
        for (Button b : kenoGridButtons) {
            if (randomPicks.contains((Integer) b.getUserData())) {
                b.setStyle(buttonStyleSelected);
            } else {
                b.setStyle(buttonStyleDefault);
            }
        }
        
        // Update button states (this will enable "Start Drawing")
        updateGridAndButtonStates();
    }
    
    
    //This method runs the one-by-one drawing animation
    private void animateDrawing() {
        // Get all the data needed for the animation
        List<Integer> drawn = kenoGame.getDrawing().getWinningNumbers();
        List<Integer> matched = kenoGame.getDrawing().getMatchedNumbers();
        List<Integer> selected = getSelectedNumbers();

        animationCounter = 0;
        numbersDrawnArea.clear(); 
        
        // Reset grid styles (except for player's selection)
        for (Button b : kenoGridButtons) {
            b.setDisable(true); // Disable grid during animation
            b.setGraphic(null); // Clear old stars
            if (!b.getStyle().equals(buttonStyleSelected)) {
                b.setStyle(buttonStyleDefault);
            }
        }
        
        Timeline timeline = new Timeline();
        // A KeyFrame runs a piece of code at a specific time.
        KeyFrame keyFrame = new KeyFrame(Duration.millis(200), e -> {
            if (animationCounter < 20) {
                int numberToDisplay = drawn.get(animationCounter);
                
                // 1. Add number to the text area with 10x10 formatting
                if (animationCounter == 10) {
                    numbersDrawnArea.appendText("\n"); 
                }
                numbersDrawnArea.appendText(String.format("%2d  ", numberToDisplay));
                
                // 2. Find the button on the grid
                Button b = findButtonByNumber(numberToDisplay);
                if (b != null) {
                    // 3. Style the button based on if it's a match or just drawn
                    if (matched.contains(numberToDisplay)) {
                        b.setStyle(buttonStyleMatched);
                        // Add the star 
                        Text star = new Text("*");
                        star.setFont(Font.font("Tahoma", FontWeight.BOLD, 16));
                        star.setFill(Color.WHITE); // Style matches button text
                        b.setGraphic(star);
                    } else if (!selected.contains(numberToDisplay)) {
                        // Only style as "drawn" if it wasn't one of the player's
                        // (we want to keep the player's picks green)
                        b.setStyle(buttonStyleDrawn);
                    }
                }
                animationCounter++;
            }
        });
        
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(20); 

        timeline.setOnFinished(e -> {
            //update winnings and button states
            updateUIAfterAnimation();
        });

        timeline.play(); // Start the animation
    }
    
     //This method is called after the animation is complete.
     //It updates winnings and button states
    private void updateUIAfterAnimation() {
        // Get Winnings
        double drawingWinnings = kenoGame.getCurrentDrawingWinnings();
        double totalWinnings = kenoGame.getTotalWinnings();

        // Update winnings labels
        drawingWinningsLabel.setText(String.format("Current Drawing Winnings: $%.2f", drawingWinnings));
        totalWinningsLabel.setText(String.format("Total Winnings: $%.2f", totalWinnings));
                
        // All buttons are already disabled from the animation start.
        // set the control button states.
        
        // MULTI-DRAW LOGIC
        if (kenoGame.hasMoreDrawings()) {
            // There are more drawings left
            startDrawingButton.setText("Continue to Next Drawing");
            startDrawingButton.setDisable(false); // Re-enable the button
            // Set its action to run the next drawing (not the first)
            startDrawingButton.setOnAction(e -> runDrawing(false)); 
            
            playNewCardButton.setDisable(true); // Can't play new card yet
        } else {
            // This was the last drawing
            startDrawingButton.setText("Start Drawing");
            startDrawingButton.setDisable(true);
            // Reset its action for the next game
            startDrawingButton.setOnAction(e -> runDrawing(true)); 
            
            playNewCardButton.setDisable(false); // Now they can play a new card
        }
        
        randomPickButton.setDisable(true); // Always disabled after first pick
    }

    //Resets the entire game UI to its initial state for a new card.
    private void resetGameUI() {
        // This resets the backend
        kenoGame.resetGame(kenoGame.getPlayer().getBalance());
        
        // Reset Keno grid
        for (Button b : kenoGridButtons) {
            b.setStyle(buttonStyleDefault);
            b.setDisable(true);
            b.setGraphic(null); // Remove star
        }

        //Reset results pane
        numbersDrawnArea.clear(); // <-- Use TextArea
        drawingWinningsLabel.setText("Current Drawing Winnings: $0.00");
        totalWinningsLabel.setText("Total Winnings: $0.00");

        //Reset controls
        setControlsDisabled(spotsBox, false);
        setControlsDisabled(drawingsBox, false);
        spotsGroup.selectToggle(null);
        drawingsGroup.selectToggle(null);

        //Reset button states
        submitSelectionsButton.setDisable(false);
        randomPickButton.setDisable(true);
        startDrawingButton.setText("Start Drawing");
        startDrawingButton.setOnAction(e -> runDrawing(true));
        startDrawingButton.setDisable(true);
        playNewCardButton.setDisable(true);
        numSpotsToPick = 0;
    }

    
     //HELPER METHOD
     //Checks how many numbers are selected vs. how many should be.
     //Enables/Disables the "Start Drawing" and "Random Pick" buttons.
    private void updateGridAndButtonStates() {
        int selectedCount = getSelectedNumbers().size();
        
        if (selectedCount == numSpotsToPick) {
            // Player has selected the correct amount
            startDrawingButton.setDisable(false);
            randomPickButton.setDisable(true); // Can't pick random after manual pick
            
            // Disable all other buttons
            for (Button b : kenoGridButtons) {
                if (!b.getStyle().equals(buttonStyleSelected)) {
                    b.setDisable(true);
                }
            }
        } else {
            // Player has not selected enough (or de-selected one)
            startDrawingButton.setDisable(true);
            randomPickButton.setDisable(false);
            
            // Re-enable all buttons
            for (Button b : kenoGridButtons) {
                b.setDisable(false);
            }
        }
    }
    
     //Reads the selected radio button for "Spots to Play".
     //return The number of spots
    private int getSelectedSpotsCount() {
        RadioButton selected = (RadioButton) spotsGroup.getSelectedToggle();
        if (selected == null) return 0;
        
        String text = selected.getText(); 
        return Integer.parseInt(text.split(" ")[0]);
    }

     //Reads the selected radio button for "Number of Drawings".
     //Return The number of drawings 
    private int getSelectedDrawingsCount() {
        RadioButton selected = (RadioButton) drawingsGroup.getSelectedToggle();
        if (selected == null) return 0;
        
        String text = selected.getText(); 
        return Integer.parseInt(text.split(" ")[0]);
    }
    
    //Finds a button on the grid by its number.
    //return The Button object, or null if not found.
    private Button findButtonByNumber(int number) {
        for (Button b : kenoGridButtons) {
            if ((Integer) b.getUserData() == number) {
                return b;
            }
        }
        return null;
    }
    
    //A utility to disable or enable all controls in a VBox.
    private void setControlsDisabled(VBox pane, boolean disabled) {
        // We skip the first child, which is the Label
        for (Node node : pane.getChildren()) {
            if (node instanceof RadioButton) {
                node.setDisable(disabled);
            }
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
