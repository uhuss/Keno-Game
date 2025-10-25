import java.util.List;

public class Keno_Game {
    private Player player;
    private Drawing drawing;  
    
    private boolean gameActive = false;
    private int totalDrawings = 1;
    private int totalWinnings = 0;
    private int currentDrawingWinnings = 0;
    private int currentDrawingNumber = 1;


    public Keno_Game(int balance) {
        this.player = new Player(balance);
        this.drawing = new Drawing();
    }


    public void startGame(int spots, int drawings) {
        if (spots < 1 || spots > 10) {
            throw new IllegalArgumentException("Spots must be between 1 and 10");
        }
        
        this.gameActive = true;
        this.totalDrawings = drawings;
        player.initializePayout(spots);
    }

    public boolean processDrawing(List<Integer> playerNumbers, int betAmount) {
    	if(!gameActive) {
    		return false;
    	}
    	
    	if(!player.placeBet(betAmount)) {
    		return false;
    	}
    	  	
    	drawing.generateNumbers(20);
    	
    	List<Integer> matches = drawing.findMatches(playerNumbers);
    	int matchCount = matches.size();
    	
    	this.currentDrawingWinnings = player.calculateWinnings(matchCount);
    	this.totalWinnings += currentDrawingWinnings;
    	
    	if(currentDrawingWinnings > 0) {
    		player.awardWinnings(currentDrawingWinnings);
    	}
    	
    	currentDrawingNumber++;
    	
    	if(currentDrawingNumber >= totalDrawings) {
    		gameActive = false;
    	}
    	
    	return true; 
    }
    
    public void resetGame(int initialBalance) {
        this.player = new Player(initialBalance);
        this.drawing = new Drawing();
        this.currentDrawingNumber = 0;
        this.totalDrawings = 1;
        this.totalWinnings = 0;
        this.currentDrawingWinnings = 0;
        this.gameActive = false;
    }
    
    public boolean hasMoreDrawings() {
    	return gameActive && currentDrawingNumber < totalDrawings;
    }
    
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
