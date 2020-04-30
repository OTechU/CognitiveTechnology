import java.util.*;
import java.io.*;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.*;

import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

@SuppressWarnings("serial")
public class Gui extends JPanel {

    private ArrayList blockList;
    private ArrayList labelList;

    private JButton switchPlayer;
    private JButton agentStep;
    private JButton agentAdvance;
    private JButton exit;
    private JButton reset;

    private boolean agent;

    private JLabel currentPlayer;
    private JPanel panel2048;

    private Demo game;

    private int xMax = 4;
    private int yMax = 4;

    Color colorBack = new Color(173,157,142);
    Color colorEmpty = new Color(194, 178, 165);
    Color color2 = new Color(226,214,202);
    Color color4 = new Color(232,217,194);
    Color color8 = new Color(227, 163, 109);
    Color color16 = new Color(228,120,65);
    Color color32 = new Color(222,111,86);
    Color color64 = new Color(219,85,60);
    Color color128 = new Color(228,194,162);
    Color color256 = new Color(237,200,59);
    Color color512 = new Color(226,186,74);
    Color color1024 = new Color(216,174,14);
    Color color2048 = new Color(225,179,53);

    private void prepareGame() {
        agent = false;
        game = new Demo(true);

        game.prepareGameBoard();

        //Load the Soar Production file, if possible:

        game.loadSoar("2048.soar");

        /*try {
            Process p = Runtime.getRuntime().exec("/Users/sleary10/Documents/SoarSuite_9.6.0-Multiplatform_64bit/SoarJavaDebugger.sh -remote -width 800 -height 800");
        } catch(Throwable t) {
            
        }*/
        //

        refreshUI(game.getBoardValues());
    }
    private void refreshUI(int[][] board) {
        for(int x = 0; x < xMax; x++) {
            for(int y = 0; y < yMax; y++) {
                int value = board[x][y];
                JLabel text = (JLabel) labelList.get((x * 4) + y);
                JPanel panel = (JPanel) blockList.get((x * 4) + y);
                if(value == 0) {
                    text.setText("");
                    panel.setBackground(colorEmpty);
                } else {
                    switch(value) {
                        case 2:
                            panel.setBackground(color2);
                            break;
                        case 4:
                            panel.setBackground(color4);
                            break;
                        case 8:
                            panel.setBackground(color8);
                            break;
                        case 16:
                            panel.setBackground(color16);
                            break;
                        case 32:
                            panel.setBackground(color32);
                            break;
                        case 64:
                            panel.setBackground(color64);
                            break;
                        case 128:
                            panel.setBackground(color128);
                            break;
                        case 256:
                            panel.setBackground(color256);
                            break;
                        case 512:
                            panel.setBackground(color512);
                            break;
                        case 1024:
                            panel.setBackground(color1024);
                            break;
                        case 2048:
                            panel.setBackground(color2048);
                            break;
                    }
                    //panel.setBackground(color2);
                    text.setText(String.valueOf(value));
                }
            }
        }
    }

    private void createGUI() {
        //Create main frame
        JFrame frame = new JFrame("2048 Soar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.setSize(600,600);

        //Create main panel
        JPanel mainPanel = new JPanel();

        //Create top menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu m1 = new JMenu("File");
        JMenu m2 = new JMenu("Help");
        menuBar.add(m1);
        menuBar.add(m2);

        JMenuItem file1 = new JMenuItem("Load Agent");
        m1.add(file1);
        file1.addActionListener(new ActionBindings(""));

        JMenuItem file2 = new JMenuItem("About");
        m2.add(file2);

        //2048 Panel
        panel2048 = new JPanel();
        GridLayout experimentLayout = new GridLayout(0,4);
        panel2048.setLayout(experimentLayout);

        //Create blocks and labels
        blockList = new ArrayList();
        labelList = new ArrayList();
        for(int x = 0; x < xMax * yMax; x++) {
            JPanel temp = new JPanel();
            JLabel temp2 = new JLabel();
            temp.add(temp2);

            blockList.add(temp);
            labelList.add(temp2);
        }

        for(int x = 0; x < xMax * yMax; x++) {
            JPanel temp = (JPanel) blockList.get(x);
            temp.setLayout(new GridBagLayout());
            temp.setBackground(colorBack);

            panel2048.add(temp);
        }

        //Bottom panel
        JPanel bottomPanel = new JPanel();
        JPanel bottomPanel2 = new JPanel();
        JPanel bottom = new JPanel();

        GridLayout experimentLayout2 = new GridLayout(0,1);
        bottom.setLayout(experimentLayout2);

        currentPlayer = new JLabel("Current Player: Human");

        bottomPanel.add(currentPlayer);

        //Set up input mappings
        panel2048.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "u");
        panel2048.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "d");
        panel2048.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "l");
        panel2048.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "r");
        panel2048.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "R");
        panel2048.getActionMap().put("u", new ActionBindings("u"));
        panel2048.getActionMap().put("d", new ActionBindings("d"));
        panel2048.getActionMap().put("l", new ActionBindings("l"));
        panel2048.getActionMap().put("r", new ActionBindings("r"));
        panel2048.getActionMap().put("R", new ActionBindings("R"));


        //frame.getContentPane().add(button); // Adds Button to content pane of frame
        switchPlayer = new JButton("Switch to Agent");
        agentStep = new JButton("Agent Step");
        agentAdvance = new JButton("Run 25");
        exit = new JButton("Exit");
        reset = new JButton("h");

        switchPlayer.addActionListener(new ActionBindings(""));
        agentStep.addActionListener(new ActionBindings(""));
        exit.addActionListener(new ActionBindings(""));
        reset.addActionListener(new ActionBindings(""));
        agentAdvance.addActionListener(new ActionBindings(""));

        agentStep.setEnabled(false);
        agentAdvance.setEnabled(false);

        bottomPanel.add(switchPlayer);
        bottomPanel.add(agentStep);
        bottomPanel.add(agentAdvance);
        bottomPanel2.add(exit);
        bottomPanel2.add(reset);

        bottom.add(bottomPanel);
        bottom.add(bottomPanel2);

        frame.getContentPane().add(BorderLayout.NORTH, menuBar);
        frame.getContentPane().add(panel2048);
        frame.getContentPane().add(BorderLayout.SOUTH, bottom);
        //frame.getContentPane().add(BorderLayout.SOUTH, bottomPanel);
        //frame.getContentPane().add(BorderLayout.SOUTH, bottomPanel2);

        //mainPanel.add(bottomPanel);
        frame.setResizable(false);
        frame.setVisible(true);

        prepareGame();
   }

   public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Gui window = new Gui();
                window.createGUI();
            }
        });

        /*Demo game2;
        game2 = new Demo();
        game2.prepareGameBoard();
        game2.loadSoar("2048.soar");
        game2.soarStep();*/
        //game2.soarStep();

   }

    private class ActionBindings extends AbstractAction {
        public ActionBindings(String text) {
            super(text);
            putValue(ACTION_COMMAND_KEY, text);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String actionCommand = e.getActionCommand();

            if(actionCommand.equals("R") || actionCommand.equals("Switch to Agent") || actionCommand.equals("Switch to Human")) {
                if(agent == false) {
                    if(game.isSoarLoaded()) {
                        System.out.println("Switching to the agent");
                        currentPlayer.setText("Current Player: Agent");
                        switchPlayer.setText("Switch to Human");
                        agentStep.setEnabled(true);
                        agentAdvance.setEnabled(true);
                        agent = true;
                    } else {
                        System.out.println("ERROR: Soar production file not loaded");
                    }
                    panel2048.requestFocus();
                } else {
                    System.out.println("Switching to the human");
                    currentPlayer.setText("Current Player: Human");
                    switchPlayer.setText("Switch to Agent");
                    agentStep.setEnabled(false);
                    agentAdvance.setEnabled(false);
                    agent = false;
                    panel2048.requestFocus();
                }
            } else if(actionCommand.equals("Agent Step")) {
                game.soarStep();
                refreshUI(game.getBoardValues());   
            } else if(actionCommand.equals("h")) {
                game.resetGame();
                refreshUI(game.getBoardValues());
                try {
                    Process p = Runtime.getRuntime().exec("/Users/sleary10/Documents/SoarSuite_9.6.0-Multiplatform_64bit/SoarJavaDebugger.sh -remote");
                } catch(Throwable t) {
            
                }
            } else if(actionCommand.equals("Exit")) {
                game.killSoar();
                System.exit(0);
            } else if(actionCommand.equals("Run 25")) {
                int gameNumber = 0;
                int scoreAverage = 0;
                int valueAverage = 0;

                int scoreMin = Integer.MAX_VALUE;
                int gameNumberScoreMin = 0;
                int valueMin = Integer.MAX_VALUE;
                int gameNumberValueMin = 0;

                int scoreMax = Integer.MIN_VALUE;
                int gameNumberScoreMax = 0;
                int valueMax = Integer.MIN_VALUE;
                int gameNumberValueMax = 0;

                while(gameNumber < 25) {
                    System.out.println("     Game Number: " + gameNumber);
                    //game.sendCommand("soar init");
                    //game.prepareGameBoard();

                    game.killSoar();
                    game = null;
                    game = new Demo(false);
                    game.prepareGameBoard();
                    game.loadSoar("2048.soar");
                    System.gc();

                    int stepNumber = 0;
                    while(!game.failed()) {
                        System.out.println("        Step number: " + stepNumber);
                        stepNumber++;
                        game.soarStep();
                        //refreshUI(game.getBoardValues());
                    }

                    System.out.println("        Game Num " + gameNumber + " Highest Score: " + game.score() + " highest value: " + game.highestValue());
                    scoreAverage += game.score();
                    valueAverage += game.highestValue();
                    if(game.score() < scoreMin) {
                        scoreMin = game.score();
                        gameNumberScoreMin = gameNumber;
                    }
                    if(game.highestValue() < valueMin) {
                        valueMin = game.highestValue();
                        gameNumberValueMin = gameNumber;
                    }
                    if(game.score() > scoreMax) {
                        scoreMax = game.score();
                        gameNumberScoreMax = gameNumber;
                    }
                    if(game.highestValue() > valueMax) {
                        valueMax = game.highestValue();
                        gameNumberValueMax = gameNumber;
                    }

                    gameNumber++;
                }

                System.out.println("");
                System.out.println("------------------------------------------------------------");
                System.out.println("");
                System.out.println("Game Num " + gameNumberScoreMin + " Min Score: " + scoreMin);
                System.out.println("Game Num " + gameNumberValueMin + " Min Value: " + valueMin);
                System.out.println("Game Num " + gameNumberScoreMax + " Max Score: " + scoreMax);
                System.out.println("Game Num " + gameNumberValueMax + " Max Value: " + valueMax);
                System.out.println("");
                System.out.println("Score average: " + scoreAverage / 25);       
                System.out.println("Highest value average: " + valueAverage / 25); 
            } else if(actionCommand.equals("Load Agent")) {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Soar production files", "soar");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(null);
                if(returnVal == chooser.APPROVE_OPTION) {
                    if(game.loadSoar(chooser.getSelectedFile().getName())) {
                        System.out.println("File loaded Sucessfully");
                    } else {
                        System.out.println("ERROR: File failed to load");
                    }
                } else {
                    System.out.println("ERROR: File failed to load");
                }
            } else {
                if(agent == false) {
                    System.out.println("ACTION: " + actionCommand);
                    //Make the move on the board
                    game.makeMove(actionCommand);  

                    //Update the UI
                    refreshUI(game.getBoardValues());
                } else {
                    System.out.println("ERROR: Agent is playing the game");
                }              
            }
        }
    }
}