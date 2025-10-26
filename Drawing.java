import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Drawing {
    //list to save the numbers generated and the matched numbers
    private List<Integer> winningNumbers;
    private List<Integer> matchedNumbers;
    
    //constructor
    public Drawing() {
        this.winningNumbers = new ArrayList<>();
        this.matchedNumbers = new ArrayList<>();
    }
    
    //returns new array of the winning numbers list
    public List<Integer> getWinningNumbers() { 
    	return new ArrayList<>(winningNumbers); 
    }
    
    //returns new array of the matched numbers list
    public List<Integer> getMatchedNumbers() { 
    	return new ArrayList<>(matchedNumbers); 
    }
    
    //returns the matching numbers as an int (count)
    public int getMatchCount(List<Integer> playerNumbers) {
        return findMatches(playerNumbers).size();
    }
    
    //function generates the numbers that are winners in the round
    public void generateNumbers(int count) {
    	if(count != 20) {
    		throw new IllegalArgumentException("cannot generate anything except 20 winning numbers");
    	}
    	
        winningNumbers.clear();
        Random rand = new Random();
        
        while (winningNumbers.size() < count) {
            int num = rand.nextInt(80) + 1;
            if (!winningNumbers.contains(num)) {
                winningNumbers.add(num);
            }
        }
    }
    
    //function that finds matches from players and winnings numbers
    public List<Integer> findMatches(List<Integer> playerNumbers) {
        matchedNumbers.clear();
        for (int num : playerNumbers) {
            if (winningNumbers.contains(num)) {
                matchedNumbers.add(num);
            }
        }
        return new ArrayList<>(matchedNumbers);
    }
    
   
}
