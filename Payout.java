public class Payout {
    private int[] matchArray;
    private int[] prizeArray;
  
    
    public Payout(int spotsChosen) {
        switch (spotsChosen) {
        
        //cases represent the #spot game 
        
            case 1: 
            	matchArray = new int[]{1};
            	prizeArray = new int[]{2};
            	break;
            	
            case 4 : 
            	matchArray = new int[]{2, 3, 4};
            	prizeArray = new int[]{1, 5, 75};
            	break;

            case 8 : 
            	matchArray = new int[]{4, 5, 6, 7, 8};
            	prizeArray = new int[]{2, 12, 50, 750, 10000};
            	break;

            case 10 :
            	matchArray = new int[]{0, 5, 6, 7, 8, 9, 10};
            	prizeArray = new int[]{5, 2, 15, 40, 450, 4250, 100000};
            	break;
            
            default :
            	throw new IllegalArgumentException("Invalid number of spots: " + spotsChosen);
    	}
           
    }
    
	public int getPayout(int matches) {
	    for (int i = 0; i < matchArray.length; i++) {
	        if (matchArray[i] == matches) {
	            return prizeArray[i];
	        }
	    }
	    return 0; 
	}
}

