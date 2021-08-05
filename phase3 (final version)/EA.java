public class EA
{
	private PlayGround board;                    // the playing surface
	private final char [] TOKEN= {'X','O'}; // list of tokens
	private ColinOthelloPlayer [] player;        // array of (2) players
	private ColinOthelloPlayer [] population;	// array of population members
	private ColinOthelloPlayer1 [] standard; //Standard to play randomly generated boards against.
	private boolean verbose;                // determines amount of output
	private int start;                      // who starts?
	int maxPopulation = 5000;
	int standardSize = 2;


	public EA(int p1type, int p2type, boolean verbose, int start)
	{
		board= new PlayGround();
		player= new ColinOthelloPlayer[2];
		population = new ColinOthelloPlayer[maxPopulation];
		standard = new ColinOthelloPlayer1[standardSize];
		this.verbose= verbose;
		player[0]= createPlayer(p1type,TOKEN[0]);
		player[1]= createPlayer(p2type,TOKEN[1]);

		//Fill standard up
		standard[0] = new ColinOthelloPlayer1(board, TOKEN[0], 5, 15, 4);
		standard[1] = new ColinOthelloPlayer1(board, TOKEN[0], 9, 4, 12);
		//Starting population of 50
		this.start= start;
	}

	
	
	private ColinOthelloPlayer createPlayer(int type, char token)
	{
		if (type==0)
			return new HumanOthelloPlayer(board,token);
		if(type==1)
			return new ColinOthelloPlayer(board,token);
		if(type==2)
			return new ColinOthelloPlayer(board,token);
		return null;
	}
	
	/**
	 * Method used to sort our population by their wins to determine the strongest SBE.
	 */
	public void sortByWins(){
		ColinOthelloPlayer temp;
		int k;
		int n = population.length;
		for (int m = n; m >= 0; m--) {
            for (int i = 0; i < n - 1; i++) {
                k = i + 1;
                if (population[i].win <population[k].win) {
					temp = population[i];
					population[i] = population[k];
					population[k] = temp;
				}
			}
		}
	}
	
	/**
	 * Method used to display the top 10 members our population and their weights
	 */
	public void displayTopTen(){
		for(int i=0; i<10; i++)
			System.out.println("Wins:" + population[i].win + ". Stability Weight: " + population[i].stabilityWeight
			+ " Mobility Weight: "+ population[i].mobilityWeight+ " Num Pieces Weight: " + population[i].numPiecesWeight);
	}




	public void play(int computer1, int computer2)
	{
		int turn;
		player[0] = population[computer1];
		player[1] = population[computer2];



		if (start==-1) {
			turn= (int) (Math.random()*2.0);
			start= turn;
		}
		else
			turn= start;

		do {
			if (verbose) {
				System.out.println("It is "+TOKEN[turn]+"'s move ... (" + " moves available)");
			}
			player[turn].makeMove();
			turn= (turn+1)%2;
		} while (verbose);


		
		//Increment wins and losses after a finished game.
		if(player[0].whoWon(player[0].token)){
			player[0].win++;;
			player[1].lose++;
		}
		else{
			player[0].lose++;
			player[1].win++;
		}
	}
}