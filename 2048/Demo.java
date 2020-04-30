import java.util.*;

import java.io.IOException;

import sml.Agent;
import sml.Identifier;
import sml.Kernel;
import sml.smlUpdateEventId;
import sml.Kernel.UpdateEventInterface;

public class Demo {
	private int[][] board;
	private int[][][] workingMemory;
	private int xMax = 4;
	private int yMax = 4;
	private Random rand;
	private boolean noChange;
	private boolean win;
	private boolean agentLoaded;
	private boolean soarLoaded;
	private boolean firstRun;
	private int noChangeCount;

	private String agentPath;

	private ArrayList blockList;
	private ArrayList labelList;

	private boolean spawnDebugger;

	private List<Identifier> memory;

	//Soar vars
	private Kernel kernel;
	private Agent agent;

	private boolean failedGame;

	private int highestValue;
	private boolean noChangeLastTime;

	private void coalesce(String dir) {
		int newValue = 0;
		if(dir.equals("u")) {
			for(int y = 0; y < yMax; y++) {
				for(int x = 0; x < xMax - 1; x++) {
					if(!isBlockEmpty(x, y) && !isBlockEmpty(x + 1, y)) {
						if(board[x][y] == board[x + 1][y] && board[x][y] != 0) {
							noChange = false;
							board[x][y] = board[x][y] * 2;
							newValue = board[x][y];
							board[x + 1][y] = 0;
							x = x + 2;
						}
					}
				}
			}
		}
		if(dir.equals("d")) {
			for(int y = yMax - 1; y >= 0; y--) {
				for(int x = xMax - 1; x > 0; x--) {
					if(!isBlockEmpty(x, y) && !isBlockEmpty(x - 1, y) && board[x][y] != 0) {
						if(board[x][y] == board[x - 1][y]) {
							noChange = false;
							board[x][y] = board[x][y] * 2;
							newValue = board[x][y];
							board[x - 1][y] = 0;
							x = x - 2;
						}
					}
				}
			}
		}
		if(dir.equals("l")) {
			for(int x = 0; x < xMax; x++) {
				for(int y = 0; y < yMax - 1; y++) {
					if(!isBlockEmpty(x, y) && !isBlockEmpty(x, y + 1) && board[x][y] != 0) {
						if(board[x][y] == board[x][y + 1]) {
							noChange = false;
							board[x][y] = board[x][y] * 2;
							newValue = board[x][y];
							board[x][y + 1] = 0;
							y = y + 2;
						}
					}
				}
			}
		}
		if(dir.equals("r")) {
			for(int x = 0; x < xMax; x++) {
				for(int y = yMax - 1; y > 0; y--) {
					if(!isBlockEmpty(x, y) && !isBlockEmpty(x, y - 1) && board[x][y] != 0) {
						if(board[x][y] == board[x][y - 1]) {
							noChange = false;
							board[x][y] = board[x][y] * 2;
							newValue = board[x][y];
							board[x][y - 1] = 0;
							y = y - 2;
						}
					}
				}
			}
		}

		if(newValue > highestValue) {
			highestValue = newValue;
		}
	}

	private void slide(String dir) {
		if(dir.equals("u")) {
			for(int y = 0; y < yMax; y++) {
				for(int x = 0; x < xMax - 1; x++) {
					if(isBlockEmpty(x, y) && !isBlockEmpty(x + 1, y)) {
						noChange = false;
						board[x][y] = board[x + 1][y];
						board[x+1][y] = 0;
						x = -1;
					}
				}
			}
		}
		if(dir.equals("d")) {
			for(int y = 0; y < yMax; y++) {
				for(int x = xMax - 1; x > 0; x--) {
					if(isBlockEmpty(x, y) && !isBlockEmpty(x - 1, y)) {
						noChange = false;
						board[x][y] = board[x - 1][y];
						board[x - 1][y] = 0;
						x = xMax - 1;
					}
				}
			}
		}
		if(dir.equals("l")) {
			for(int x = 0; x < xMax; x++) {
				for(int y = 0; y < yMax - 1; y++) {
					if(isBlockEmpty(x, y) && !isBlockEmpty(x, y + 1)) {
						noChange = false;
						board[x][y] = board[x][y + 1];
						board[x][y + 1] = 0;
						y = -1;
					}
				}
			}
		}
		if(dir.equals("r")) {
			for(int x = 0; x < xMax; x++) {
				for(int y = yMax - 1; y > 0; y--) {
					if(isBlockEmpty(x, y) && !isBlockEmpty(x, y - 1)) {
						noChange = false;
						board[x][y] = board[x][y - 1];
						board[x][y - 1] = 0;
						y = yMax - 1;
					}
				}
			}
		}
	}

	private void action(String dir) {
		slide(dir);
		coalesce(dir);
		slide(dir);
	}

	private boolean isBoardFull() {
		for(int x = 0; x < xMax; xMax++) {
			for(int y = 0; y < yMax; yMax++) {
				if(isBlockEmpty(x, y)) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean isBlockEmpty(int x, int y){
		if(board[x][y] == 0) {
			return true;
		} else {
			return false;
		}
	}

	private void placeValue() {
		int x;
		int y;

		do{
			x = rand.nextInt(xMax);
			y = rand.nextInt(yMax);
		} while (!isBlockEmpty(x, y));

		board[x][y] = (1 + rand.nextInt(1)) * 2;
	}

	private void clearBoard() {
		board = new int[xMax][yMax];
	}

	private void createGame() {
		clearBoard();
		placeValue();
		placeValue();
	}

	private void createWorkingMemory() {
		for(int x = 0; x < xMax; x++) {
			for(int y = 0; y < yMax; y++) {
				boolean foundValue = false;

				if(!isBlockEmpty(x, y)) {
					for(int rowVal = x - 1; rowVal >= 0; rowVal--) {
						if(!isBlockEmpty(rowVal, y)) {
							workingMemory[x][y][0] = board[rowVal][y];
							foundValue = true;
							break;
						}
					}
				}

				if(!foundValue) {
					workingMemory[x][y][0] = 0;
				}

				//DOWN VALUE
				foundValue = false;
				for(int rowVal = x + 1; rowVal < xMax; rowVal++) {
					if(!isBlockEmpty(rowVal, y)) {
						workingMemory[x][y][2] = board[rowVal][y];
						foundValue = true;
						break;
					}
				}

				if(!foundValue) {
					workingMemory[x][y][2] = 0;
				}

				//LEFT VALUE
				foundValue = false;
				for(int colVal = y - 1; colVal >= 0; colVal--) {
					if(!isBlockEmpty(x, colVal)) {
						workingMemory[x][y][3] = board[x][colVal];
						foundValue = true;
						break;
					}					
				}

				if(!foundValue) {
					workingMemory[x][y][3] = 0;
				}

				//RIGHT VALUE
				foundValue = false;
				for(int colVal = y + 1; colVal < yMax; colVal++) {
					if(!isBlockEmpty(x, colVal)) {
						workingMemory[x][y][1] = board[x][colVal];
						foundValue = true;
						break;
					}					
				}

				if(!foundValue) {
					workingMemory[x][y][1] = 0;
				}
			}
		}
	}

	private void loadWorkingMemory() {
		//For every block, create an identifier
		//Off that identifier, create a value
		//Also create a direction
		Iterator<Identifier> wmeIter = memory.iterator();
		while (wmeIter.hasNext()) {
			Identifier wme = wmeIter.next();
			
			wme.DestroyWME();
			wmeIter.remove();
		}

		Identifier inputLink = agent.GetInputLink();

		if(noChangeLastTime) {
			Identifier temp = agent.CreateIdWME(inputLink, "change");
			agent.CreateIntWME(temp, "count", noChangeCount);
			noChangeLastTime = false;
			memory.add(temp);
		}
		
		int counter = 0;
		for(int x = 0; x < xMax; x++) {
			for(int y = 0; y < yMax; y++) {
				Identifier blockTemp = agent.CreateIdWME(inputLink, "block" + counter);
				memory.add(blockTemp);

				//Name
				agent.CreateStringWME(blockTemp, "name", "block" + x + "-" + y);

				//Value
				agent.CreateIntWME(blockTemp, "value", board[x][y]);

				//surround
				//Identifier surround = agent.CreateIdWME(blockTemp, "surround");

				//Up
				agent.CreateIntWME(blockTemp, "u", workingMemory[x][y][0]);

				//Right
				agent.CreateIntWME(blockTemp, "r", workingMemory[x][y][1]);

				//Down
				agent.CreateIntWME(blockTemp, "d", workingMemory[x][y][2]);

				//Left
				agent.CreateIntWME(blockTemp, "l", workingMemory[x][y][3]);


				//Up
				if(x != 0) {
					agent.CreateStringWME(blockTemp, "uBlock", "block" + (x - 1) + "-" + y);
				}
				

				//Right
				if(y != 3) {
					agent.CreateStringWME(blockTemp, "rBlock", "block" + x + "-" + (y + 1));
				}

				//Down
				if(x != 3) {
					agent.CreateStringWME(blockTemp, "dBlock", "block" + (x + 1) + "-" + y);
				}

				//Left
				if(y != 0) {
					agent.CreateStringWME(blockTemp, "lBlock", "block" + x + "-" + (y - 1));
				}


				if(x == 0) {
					agent.CreateStringWME(blockTemp, "wall", "u");
				} 

				if (x == 3) {
					agent.CreateStringWME(blockTemp, "wall", "d");
				} 

				if (y == 0) {
					agent.CreateStringWME(blockTemp, "wall", "l");
				} 

				if(y == 3) {
					agent.CreateStringWME(blockTemp, "wall", "r");
				}

				counter++;
			}
		}
	}

	public void resetGame() {
		memory =  new LinkedList<>();
		noChangeCount = 0;
		noChange = true;
		win = false;
		createGame();
		killSoar();
		loadSoar("2048.soar");
		noChangeLastTime = false;
	}

	public void killSoar() {
		kernel.DestroyAgent(agent);
        agent = null;
        kernel.Shutdown();
        kernel = null;
	}

	private void prepareAgent() {
        kernel.RegisterForUpdateEvent(
	        smlUpdateEventId.smlEVENT_AFTER_ALL_OUTPUT_PHASES,
	        new UpdateEventInterface() {
	            public void updateEventHandler(int eventID, Object data,
	                    Kernel kernel, int runFlags) {
	                kernel.StopAllAgents();

	                /*if(agent.GetNumberCommands() == 0) {
						System.out.println("Failed to get commands");
	                	failedGame = true;
	                }*/

	                for (int index = 0; index < agent.GetNumberCommands(); ++index)
	                {
	                    Identifier command = agent.GetCommand(index);

	                    String name = command.GetCommandName();
	                    //System.out.println("Received command: " + name);
	                    //System.out.println("Direction " + command.GetParameterValue("direction"));

	                    //System.out.println("Move START");
	                    makeMove(command.GetParameterValue("direction"));
	                    //System.out.println("Move END");

	                    command.AddStatusComplete();

	                 	//System.out.println("LOADING MEMORY START");
						loadWorkingMemory();
						//System.out.println("LOADING MEMORY FINISH");
	                }

	                agent.ClearOutputLinkChanges();

	                //System.out.println("Update event complete.");
	            }
	        }, null);
	}

	public Demo(boolean spawnDebugger) {
		//Variable initalization
		memory =  new LinkedList<>();
		rand = new Random();
		noChange = true;
		win = false;
		agentLoaded = false;
		soarLoaded = false;
		noChangeCount = 0;
		workingMemory = new int[xMax][yMax][4];
		firstRun = true;
		failedGame = false;
		highestValue = 0;
		this.spawnDebugger = spawnDebugger;
	}

	public boolean isSoarLoaded() {
		return soarLoaded;
	}

	public int[][] getBoardValues() {
		return board;
	}

	public boolean loadSoar(String path) {
		agentPath = path;
		System.out.println("Path: " + agentPath);
		kernel = Kernel.CreateKernelInNewThread();
        agent = kernel.CreateAgent("2048Soar");

 		if (!agent.LoadProductions(agentPath)) {
 			soarLoaded = false;
		} else {
			soarLoaded = true;
			prepareAgent();
			//loadWorkingMemory();
		}

		if(spawnDebugger) {
			agent.SpawnDebugger();
		}

		return soarLoaded;
	}

	public void soarStep() {
		//agent.SpawnDebugger();
		//a
		if(firstRun) {
			loadWorkingMemory();

			firstRun = false;
		}

		agent.RunSelf(1);
	}

	public void soarAdvance() {
		agent.RunSelfForever();
	}

	public void prepareGameBoard() {
		/*memory =  new LinkedList<>();
		rand = new Random();
		noChange = true;
		win = false;
		agentLoaded = false;
		soarLoaded = false;
		noChangeCount = 0;
		workingMemory = new int[xMax][yMax][4];
		firstRun = true;
		failedGame = false;

		noChangeLastTime = false;
		highestValue = 0;*/

		//Create the gameboard, place the two first items
		createGame();

		createWorkingMemory();
		//runAgent();
	}

	public boolean failed() {
		return failedGame;
	}

	public void sendCommand(String command) {
		agent.ExecuteCommandLine(command);
	}

	public int score() {
		int score = 0;

		for(int x = 0; x < xMax; x++) {
			for(int y = 0; y < yMax; y++) {
				score += board[x][y];
			}
		}

		return score;
	}

	public int highestValue() {
		return highestValue;
	}

	public void makeMove(String direction) {
		//Make the action and check if anything has changed
		noChange = true;
		action(direction);

		if(highestValue == 2048) {
			System.out.println("YOU WON!");
			resetGame();
		} else if(noChangeCount > 10) {
			System.out.println("YOU FAILED");
			failedGame = true;
		} else if(noChange) {
			//System.out.println("No Change");
			noChangeLastTime = true;
			noChangeCount++;
		} else {
			//System.out.println("Action sucessful");
			noChangeCount = 0;
			placeValue();
			//noChange = true;
			createWorkingMemory();
		}
	}
}