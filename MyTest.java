import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.BeforeEach;
import java.util.Arrays;
import java.util.List;
import java.util.HashSet;
import java.util.Set;


class MyTest {
	
	private Player player;
	private Drawing drawing;
	private Keno_Game kenoGame;
	
	@BeforeEach
	public void init() {
		player = new Player(100);
		drawing = new Drawing();
		kenoGame = new Keno_Game(100);
	}
	
	
	@Test //1
	@DisplayName("Player should start with correct Balance of $100")
	public void testInitialBalance() {
		assertEquals(100, player.getBalance());
	}
	
	@Test //2
	@DisplayName("Player can place valid bet with balance")
	public void testWager() {
		boolean wagerPlaced = player.placeBet(80);
		assertTrue(wagerPlaced, "Should have balance to place $80");
		assertEquals(20, player.getBalance(), "Balance after $80 wager");
		
		boolean wagerPlaced2 = player.placeBet(100);
		assertFalse(wagerPlaced2, "No longer has balance to make this wager");
	}
	
	@Test //3
	@DisplayName("Player can select numbers within range")
	public void testSelectNumbers() {
		List<Integer> selectedNumbers = Arrays.asList(5, 10, 15, 20);
		boolean result = player.selectNumbers(selectedNumbers);
		
		assertTrue(result, "Acceptable bounds for player to choose from");	
	}
	
	@Test //4
	@DisplayName("Player cannot select numbers because they're out of range")
	public void testFailedSelectedNumbers() {
		List<Integer> failingNumbers = Arrays.asList(1, 3, 100, 420);
		boolean failedResult = player.selectNumbers(failingNumbers);
		
		assertFalse(failedResult, "Exceeds bounds for amount to choose from");
	}
	
	@Test //8
	@DisplayName("Player cannot select too many numbers")
	public void testSelectedTooMany() {
		List<Integer> failingNumbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
		boolean failedResult = player.selectNumbers(failingNumbers);
		
		assertFalse(failedResult, "Exceeds bounds for amount to choose from");
	}
	
	@Test //5
	@DisplayName("Player can successfully use Quick Pick")
	public void testQuickPick() {
		player.quickPick(4);
		
		List<Integer> selectedNumbers = player.getSelectedNumbers();
		
		assertEquals(4, selectedNumbers.size(), "quick pick should have generated 5 numbers");
		
		for(int n : selectedNumbers) {
			assertTrue(n >= 1 && n <= 80, 
				"quick pick should have generated numbers within keno range");
			}
	}
	
	@Test //11
	@DisplayName("QuickPick generated unique numbers")
	public void testQuickPickUniqueness() {
		player.quickPick(8);
		
		List<Integer> generatedNumbers = player.getSelectedNumbers();

		Set<Integer> uniqueNumbers = new HashSet<>(generatedNumbers);
		assertEquals(8, uniqueNumbers.size(), "Should have generated 20 unique numbers");	
	}
	

	@Test //6
	@DisplayName("Player's winnings are successfully added to balance")
	public void testAddWinnings() {
		kenoGame.startGame(4,  4);
		
		assertTrue(kenoGame.getTotalWinnings() >= 0, "Winnings should have been added");
	}
	
	@Test //7
	@DisplayName("Player's winnings are successfully added to balance")
	public void testAddWinningsFromPayout() {
		player.awardWinnings(50);
		
		assertEquals(150, player.getBalance(), "Balance should have accumulated");
	}

	@Test //9
	@DisplayName("Game successfully draws 20 numbers")
	public void testGenerate20Nums() {
		assertThrows(IllegalArgumentException.class, () ->{
			
		drawing.generateNumbers(100); }, "Should throw because of too many numbers");
		drawing.generateNumbers(20);
		
		List<Integer> generatedNumbers = drawing.getWinningNumbers();
		
		assertEquals(20, generatedNumbers.size(), "Game should have generated 20 numbers");
	}
	
	@Test //10
	@DisplayName("Game successfully draws unique numbers")
	public void testUniqueGenerated() {
		drawing.generateNumbers(20);
		
		List<Integer> generatedNumbers = drawing.getWinningNumbers();
		
		Set<Integer> uniqueNumbers = new HashSet<>(generatedNumbers);
		assertEquals(20, uniqueNumbers.size(), "Should have generated 20 unique numbers");	
	}
	
	@Test //12
	@DisplayName("Game generated numbers between 1-80")
	public void testGenerateInRange() {
		drawing.generateNumbers(20);
		
		List<Integer> generatedNumbers = drawing.getWinningNumbers();
		
		for(int num : generatedNumbers) {
			assertTrue(num >= 1 && num <= 80, "Number should be within range");
			}
	}
	
	@Test //13
	@DisplayName("Game finds matches")
	public void testDrawingFindsMatches() {
		drawing.generateNumbers(20);
		
		List<Integer> winningNumbers = drawing.getWinningNumbers();
		
		List<Integer> selectedNumbers = Arrays.asList(winningNumbers.get(0), 
				winningNumbers.get(1), winningNumbers.get(2), 0, 101, 82);
		
		List<Integer> matches = drawing.findMatches(selectedNumbers);
		
		assertNotNull(matches, "matches list should be populated now");
		assertEquals(3, matches.size(), "should have found three matching numbers");
		
		for(int match : matches) {
			assertTrue(winningNumbers.contains(match), match + "should be in player numbers");
		}
	}
	
	@Test //14
	@DisplayName("Game didn't find matches")
	public void testDrawingFindsNoMatches() {
		drawing.generateNumbers(20);
		
		List<Integer> winningNumbers = drawing.getWinningNumbers();
		
		List<Integer> selectedNumbers = Arrays.asList(81, 82, 100, 204, 0);
		
		List<Integer> matches = drawing.findMatches(selectedNumbers);
		
		assertNotNull(matches, "matches list should be null");
		assertEquals(0, matches.size(), "should have found 0 matching numbers");
		assertTrue(matches.isEmpty(), "Should have found no matches");
	}
	
	@Test //15
	@DisplayName("Game found a perfect match")
	public void testDrawingFindsAllAsMatches() {
		drawing.generateNumbers(20);
		
		List<Integer> winningNumbers = drawing.getWinningNumbers();
		
		List<Integer> selectedNumbers = Arrays.asList(winningNumbers.get(0),
				winningNumbers.get(1), winningNumbers.get(2), winningNumbers.get(3),
				winningNumbers.get(4));
		
		List<Integer> matches = drawing.findMatches(selectedNumbers);
		
		assertNotNull(matches, "matches list should be populated now");
		assertEquals(5, matches.size(), "should have found all five matching numbers");
		
		for(int match : matches) {
			assertTrue(winningNumbers.contains(match), match + "should be in player numbers");
		}
	}
	
	@Test //16
	@DisplayName("1 spot game returns correct payout for 1 match")
	public void test1SpotGame() {
		Payout payout = new Payout(1);
		
		assertEquals(2, payout.getPayout(1), "1 spot game with 1 match should win $2");
		
		assertEquals(0, payout.getPayout(0), "1 spot game with 0 matches should win $0");
	}
	
	@Test //17
	@DisplayName("4 spot game returns correct payout for 2, 3, 4 matches")
	public void test4SpotGame() {
		Payout payout = new Payout(4);
		
		assertEquals(1, payout.getPayout(2), "4 spot game with 2 match should win $1");
		assertEquals(5, payout.getPayout(3), "4 spot game with 3 match should win $5");
		assertEquals(75, payout.getPayout(4), "4 spot game with 4 matches should win $75");
		
		assertEquals(0, payout.getPayout(0), "4 spot game with 0 matches should win $0");
		assertEquals(0, payout.getPayout(1), "4 spot game with 1 match should win $0");

	}
	
	@Test //18
	@DisplayName("8 spot game returns correct payout for 4, 5, 6, 7, 8 matches")
	public void test8SpotGame() {
		Payout payout = new Payout(8);
		
		assertEquals(2, payout.getPayout(4), "8 spot game with 4 matches should win $2");
		assertEquals(12, payout.getPayout(5), "8 spot game with 5 matches should win $12");
		assertEquals(50, payout.getPayout(6), "8 spot game with 6 matches should win $50");
		assertEquals(750, payout.getPayout(7), "8 spot game with 7 matches should win $750");
		assertEquals(10000, payout.getPayout(8), "8 spot game with 8 matches should win $10000");
		
		assertEquals(0, payout.getPayout(0), "8 spot game with 0 matches should win $0");
		assertEquals(0, payout.getPayout(1), "8 spot game with 1 match should win $0");
		assertEquals(0, payout.getPayout(2), "8 spot game with 2 match should win $0");
		assertEquals(0, payout.getPayout(3), "8 spot game with 2 match should win $0");



	}
	
	@Test //19
	@DisplayName("10 spot game returns correct payout for 0, 5, 6, 7, 8, 9, 10 matches")
	public void test10SpotGame() {
		Payout payout = new Payout(10);
		
		assertEquals(5, payout.getPayout(0), "10 spot game with 0 matches should win $0");
		assertEquals(2, payout.getPayout(5), "10 spot game with 1 match should win $2");
		assertEquals(15, payout.getPayout(6), "10 spot game with 0 matches should win $0");
		assertEquals(40, payout.getPayout(7), "10 spot game with 0 matches should win $0");
		assertEquals(450, payout.getPayout(8), "10 spot game with 0 matches should win $0");
		assertEquals(4250, payout.getPayout(9), "10 spot game with 0 matches should win $0");
		assertEquals(100000, payout.getPayout(10), "10 spot game with 0 matches should win $0");
		
		assertEquals(0, payout.getPayout(1), "10 spot game with 1 match should win $0");
		assertEquals(0, payout.getPayout(2), "10 spot game with 2 matches should win $0");
		assertEquals(0, payout.getPayout(4), "10 spot game with 4 matches should win $0");
	}
	
	@Test //20
	@DisplayName("A spot game that is not 1, 4, 8, or 10 is invalid")
	public void testInvalidSpotGames() {
		assertThrows(IllegalArgumentException.class, () ->{
			
			new Payout(0); }, "Should throw invalid # spots");
		
		assertThrows(IllegalArgumentException.class, () ->{
			
			new Payout(5); }, "Should throw invalid # spots");
		
		assertThrows(IllegalArgumentException.class, () ->{
			
			new Payout(11); }, "Should throw invalid # spots");
	}
	
	@Test //21
	@DisplayName("Game starts")
	public void testStartingGame() {
		kenoGame.startGame(1, 4 );
		
		assertTrue(kenoGame.isGameActive(), "Game flag Game Active should be set to true");
		assertEquals(4, kenoGame.getTotalDrawings(), "game should have four drawings" );
		assertEquals(1, kenoGame.getCurrentDrawingNumber(), "game should start on first drawing");
		assertEquals(0, kenoGame.getTotalWinnings(), "game should not have winnings from beginning");
	}
	
	@Test //22
	@DisplayName("Game should process a drawing correctly")
	public void testDrawing() {
		kenoGame.startGame(10, 1);
		
		int initialBalance = kenoGame.getPlayer().getBalance();

		List<Integer> selectedNumbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		
		boolean result = kenoGame.processDrawing(selectedNumbers, initialBalance);
		
		assertTrue(result, "drawing should process sucessfully");
		assertTrue(kenoGame.getTotalWinnings() >=0, "Game should have accrued balance");
		assertTrue(kenoGame.getCurrentDrawingWinnings() >= 0, "game should have accrued balance for first particular game");
		
	}
	
	@Test //23
	@DisplayName("Game should process multiple drawings correctly")
	public void testMultipleDrawing() {
		kenoGame.startGame(10, 4);
		
		int initialBalance = kenoGame.getPlayer().getBalance();

		List<Integer> selectedNumbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		
		boolean result = kenoGame.processDrawing(selectedNumbers, initialBalance);
		
		assertTrue(result, "drawing should process sucessfully");
		assertTrue(kenoGame.getTotalWinnings() >=0, "Game should have accrued balance");
		assertTrue(kenoGame.getCurrentDrawingWinnings() >= 0, "game should have accrued balance for first particular game");
		
	}
	
	@Test //24
	@DisplayName("Game should process the betting correctly")
	public void testBet() {
		kenoGame.startGame(10, 1);
	    int initialBalance = kenoGame.getPlayer().getBalance();

	    List<Integer> selectedNumbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
	    
	    boolean result = kenoGame.processDrawing(selectedNumbers, 50); 
	    
	    assertTrue(result, "Drawing should process successfully");

	    int finalBalance = kenoGame.getPlayer().getBalance();
	    int balanceChange = finalBalance - initialBalance;
	    
	    int expectedChange = kenoGame.getCurrentDrawingWinnings() - 50;
	    assertEquals(expectedChange, balanceChange, "Balance change should equal winnings minus bet");
	    
	    assertTrue(kenoGame.getTotalWinnings() >= 0, "Total winnings should not be what we started with");
	    assertTrue(kenoGame.getCurrentDrawingWinnings() >= 0, "Current drawing winnings should have increased");
	}
	
	@Test //25
	@DisplayName("Game should reset to initial state")
	public void testReset() {
		kenoGame.startGame(10, 4);
		
		List<Integer> selectedNumbers = Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 79, 78);
		
		kenoGame.processDrawing(selectedNumbers, 10);
		
	    assertEquals(4, kenoGame.getTotalDrawings(), "Should have four drawings to process");
	    assertTrue(kenoGame.getTotalWinnings() >= 0, "Total winnings should not be what we started with");
	    assertTrue(kenoGame.getCurrentDrawingWinnings() >= 0, "Current drawing winnings should have increased");
	    
	    kenoGame.resetGame(100);
	    
	    assertEquals(1, kenoGame.getTotalDrawings(), "Should have 1"
	    		+ "drawings to process at very beginning before user has chosen (default)");
	    assertEquals(0, kenoGame.getTotalWinnings(), "Should have 0 "
	    		+ "winnings at very beginning before user has chosen (default)");
	    assertEquals(100, kenoGame.getPlayer().getBalance(), "Should have 100"
	    		+ "winnings at very beginning before user has chosen (default)");    
	    
	}
}
	

