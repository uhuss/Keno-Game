import java.util.ArrayList;
import java.util.List;

public class Keno_Game {
	///has two instances of classes- Player and Drawing
    private Player player; 
    private Drawing drawing; 
    
    private boolean gameActive = false; //flag for checking the game's status
    private int totalDrawings = 1; //variable to keep track of player's choice of drawings 
    private int totalWinnings = 0; //variable for keeping track of overall balance
    private int currentDrawingWinnings = 0; //variable for keeping track of current game's balance
    private int currentDrawingNumber = 1; //variable for keeping track of current drawing #

    //constructor
    public Keno_Game(int balance) {
        this.player = new Player(balance);
        this.drawing = new Drawing();
    }

    //function will activate game and initialize drawing & payout
    public void startGame(int spots, int drawings) {
        if (spots < 1 || spots > 10) {
            throw new IllegalArgumentException("Spots must be between 1 and 10");
        }
        
        this.gameActive = true;
        this.totalDrawings = drawings;
        player.initializePayout(spots);
    }

    //function handles drawing after numbers chosen
    public boolean processDrawing(List<Integer> playerNumbers, int betAmount) {
    	
    	//ensure game has processed spots and drawing first 
    	if(!gameActive) {
    		return false;
    	}
    	
    	//ensure bet amount is valid
    	if(!player.placeBet(betAmount)) {
    		return false;
    	}
    	  	
    	//generate the random numbers
    	drawing.generateNumbers(20);
    	
    	//find the matches
    	List<Integer> matches = drawing.findMatches(playerNumbers);
    	int matchCount = matches.size();
    	
    	//calculate winnings 
    	this.currentDrawingWinnings = player.calculateWinnings(matchCount);
    	this.totalWinnings += currentDrawingWinnings;
    	
    	if(currentDrawingWinnings > 0) {
    		player.awardWinnings(currentDrawingWinnings);
    	}
    	
    	//process all drawings
    	currentDrawingNumber++;
    	
    	if(currentDrawingNumber >= totalDrawings) {
    		gameActive = false;
    	}
    	
    	return true; 
    }
    
    //function that resets all private variables to their original values
    public void resetGame(int initialBalance) {
        this.player = new Player(initialBalance);
        this.drawing = new Drawing();
        this.currentDrawingNumber = 0;
        this.totalDrawings = 1;
        this.totalWinnings = 0;
        this.currentDrawingWinnings = 0;
        this.gameActive = false;
    }
    
    //function that determines if more drawings need to be made
    public boolean hasMoreDrawings() {
    	return gameActive && currentDrawingNumber < totalDrawings;
    }
    
    //getters
    
    public int getCurrentDrawingWinnings() {
    	return currentDrawingWinnings;
    }
    
    public int getTotalWinnings() {
    	return totalWinnings;
    }
    
    public int getCurrentDrawingNumber() {
    	return currentDrawingNumber;
    }
    
    public int getTotalDrawings() {
    	return totalDrawings;
    }
    
    public boolean isGameActive() {
        return gameActive;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public Drawing getDrawing() {
        return drawing;
    }
}
