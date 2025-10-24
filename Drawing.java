import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Drawing {
    private List<Integer> winningNumbers;
    private List<Integer> matchedNumbers;
    
    public Drawing() {
        this.winningNumbers = new ArrayList<>();
        this.matchedNumbers = new ArrayList<>();
    }
    
    public List<Integer> getWinningNumbers() { 
    	return new ArrayList<>(winningNumbers); 
    }
    
    public List<Integer> getMatchedNumbers() { 
    	return new ArrayList<>(matchedNumbers); 
    }
    
    public int getMatchCount(List<Integer> playerNumbers) {
        return findMatches(playerNumbers).size();
    }
    
    public void generateNumbers(int count) {
        winningNumbers.clear();
        Random rand = new Random();
        
        while (winningNumbers.size() < count) {
            int num = rand.nextInt(80) + 1;
            if (!winningNumbers.contains(num)) {
                winningNumbers.add(num);
            }
        }
    }
    
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
