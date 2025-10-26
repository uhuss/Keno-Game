import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Player {

    private Integer balance = 0; //balance for player
    private Integer currentBet = 0; //wager that player placed
    private List<Integer> selectedNumbers; //numbers that player chose
    private Payout payout; //payout particular to player's matched numbers
    
    //default constructor
    public Player() {
        this.balance = 100;
        this.selectedNumbers = new ArrayList<>();
        this.currentBet = 0;
        this.payout = null;
    }
    
    //parameterized constructor (adds balance for recurring games)
    public Player(int addBalance) {
    	this.balance = this.balance + addBalance;
    	this.selectedNumbers = new ArrayList<>();
    	this.currentBet = 0;
    	this.payout = null;

    }
    
    //initalizes payout instance through passing in the player's choice of spot(s) game
    public void initializePayout(int spots) {
    	this.payout = new Payout(spots);
    }
    
    //getters
    
    public Integer getBalance() {
    	return balance;
    }
    
    public Integer getCurrentBet() { 
    	return currentBet; 
    }
    
    public List<Integer> getSelectedNumbers() {
    	return new ArrayList<>(selectedNumbers);
    }
    
    public int getSpotsChosen() { 
    	return selectedNumbers.size(); 
    }    
    
    public Payout gePayout() {
    	return payout;
    }
    
    //function for intializing selected numbers and payout 
    public boolean selectNumbers(List<Integer> numbers) {
        if (numbers == null || numbers.size() < 1 || numbers.size() > 10) {
            return false;
        }
        this.selectedNumbers = new ArrayList<>(numbers);
        this.payout = new Payout(numbers.size());
        return true;
    }
    
    //function that generates numbers if player chose not to pick themselves
    public void quickPick(int numSpots) {
        Random rand = new Random();
        selectedNumbers.clear();
        
        while (selectedNumbers.size() < numSpots) {
            int num = rand.nextInt(80) + 1;
            if (!selectedNumbers.contains(num)) {
                selectedNumbers.add(num);
            }
        }
        
        this.payout = new Payout(numSpots);
    }
    
    //function that checks the vaildity of the bet and decreases the amount from player's balance
    public boolean placeBet(int amount) {
        if (amount <= balance && amount > 0) {
            this.currentBet = amount;
            this.balance -= amount;
            return true;
        }
        return false;
    }
    
    //function that awards winnings after matched numbers found
    public void awardWinnings(int amount) {
        this.balance += amount;
    }
    
    //function that returns T/F if balance is less than amount wagered
    public boolean canAffordBet(int amount) {
        return amount <= balance;
    }
    
    //function that calculates multiplier 
    public int calculateWinnings(int matches) {
    	if(payout == null) {
    		return 0;
    		}
    	
    	int prizeMultiplier = payout.getPayout(matches);
    	return currentBet * prizeMultiplier;
    }
}
