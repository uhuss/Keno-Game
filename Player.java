import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Player {
    private Integer balance;
    private Integer currentBet;
    private List<Integer> selectedNumbers;
    private Payout payout;
    
    public Player() {
        this.balance = 100;
        this.selectedNumbers = new ArrayList<>();
        this.currentBet = 0;
        this.payout = null;
    }
    
    public Player(int addBalance) {
    	this.balance = this.balance + addBalance;
    	this.selectedNumbers = new ArrayList<>();
    	this.currentBet = 0;
    	this.payout = null;

    }
    public void initializePayout(int spots) {
    	this.payout = new Payout(spots);
    }
    
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
    
    public boolean selectNumbers(List<Integer> numbers) {
        if (numbers == null || numbers.size() < 1 || numbers.size() > 10) {
            return false;
        }
        this.selectedNumbers = new ArrayList<>(numbers);
        this.payout = new Payout(numbers.size());
        return true;
    }
    
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
    
    public boolean placeBet(int amount) {
        if (amount <= balance && amount > 0) {
            this.currentBet = amount;
            this.balance -= amount;
            return true;
        }
        return false;
    }
    
    public void awardWinnings(int amount) {
        this.balance += amount;
    }
    
    public boolean canAffordBet(int amount) {
        return amount <= balance;
    }
    
    public int calculateWinnings(int matches) {
    	if(payout == null) {
    		return 0;
    		}
    	
    	int prizeMultiplier = payout.getPayout(matches);
    	return currentBet * prizeMultiplier;
    }
}
