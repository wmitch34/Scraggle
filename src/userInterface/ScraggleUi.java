
package userInterface;

import game.Game;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.*;

public class ScraggleUi 
{
    private int score;
    private int minutes = 3;
    private int seconds = 0;
    
    private Timer timer;
    private final ArrayList<String> foundWords = new ArrayList<>();
    private final ResetGameListener resetGameListener;
        
    //window setup
    private JFrame frame;
    private JMenuBar menuBar;
    private JMenu gameMenu;
    private JMenuItem exit;
    private JMenuItem newGame;
    
    // Scraggle board
    private JPanel scragglePanel;
    private JButton[][] diceButtons;

    // Enter found words
    private JPanel wordsPanel;
    private JScrollPane scrollPane;
    private JTextPane wordsArea;
    
    // time label 
    private JLabel timeLabel;
    private JButton shakeDice;

    // Enter current word
    private JPanel currentPanel;
    private JLabel currentLabel;
    private JButton currentSubmit;
        
    // player's score
    private JLabel scoreLabel;
    
    private final Game game;
 
    private final int GRID = 4;
    
    public ScraggleUi(Game inGame)
    {
        game = inGame;
        resetGameListener = new ResetGameListener();
        initComponents();
    }
    
    private void initComponents()
    {
        // Initialize the JFrame
        frame = new JFrame("Scraggle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(660, 500);
        
        // Initialize the JMenuBar and add to the JFrame
        createMenu();   

        // Initialize the JPane for the current word
        setupCurrentPanel();
        
        // Initialize the JPanel for the word entry
        setupWordPanel();
        
        //Timer setup
        timer = new Timer(1000, new TimerListener());
        timer.start();
        
        // Initialize the JPanel for the Scraggle dice
        setupScragglePanel();      
        
        // Add everything to the JFrame
        frame.setJMenuBar(menuBar);
        frame.add(scragglePanel, BorderLayout.WEST);
        frame.add(wordsPanel, BorderLayout.CENTER);
        frame.add(currentPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
    
    //function sets up current menu, action listener for "New Game"  and "Exit" buttons are added here
    private void createMenu()
    {
        menuBar = new JMenuBar();
        gameMenu = new JMenu("Scraggle");
        gameMenu.setMnemonic('S');
        
        newGame = new JMenuItem("New Game");
        newGame.setMnemonic('N');
        newGame.addActionListener(resetGameListener);    

        exit = new JMenuItem("Exit");
        exit.setMnemonic('E');
        ExitListener exitListener = new ExitListener();
        exit.addActionListener(exitListener);
        
        gameMenu.add(newGame);    
        gameMenu.add(exit);    
        
        menuBar.add(gameMenu);
    }

    //function sets up current word panel, action listener for "submit word" button is added here
    private void setupCurrentPanel()
    {
        currentPanel = new JPanel();
        currentPanel.setBorder(BorderFactory.createTitledBorder("Current Word"));

        currentLabel = new JLabel();
        currentLabel.setBorder(BorderFactory.createTitledBorder("Current Word"));
        currentLabel.setMinimumSize(new Dimension(300, 50));
        currentLabel.setPreferredSize(new Dimension(300,50));
        currentLabel.setHorizontalAlignment(SwingConstants.LEFT);
        
        currentSubmit = new JButton("Submit Word");
        currentSubmit.setMinimumSize(new Dimension(200, 100));
        currentSubmit.setPreferredSize(new Dimension(200, 50));
        SubmitListener submitListener = new SubmitListener();
        currentSubmit.addActionListener(submitListener);
        
        
        scoreLabel = new JLabel();
        scoreLabel.setBorder(BorderFactory.createTitledBorder("Score"));
        scoreLabel.setMinimumSize(new Dimension(100, 50));
        scoreLabel.setPreferredSize(new Dimension(100,50));
        
        
        currentPanel.add(currentLabel);
        currentPanel.add(currentSubmit);
        currentPanel.add(scoreLabel);
    }
    
    //function sets up "enet words found" panel, action listener for "shake dice" button is added here
    private void setupWordPanel()
    {
        wordsPanel = new JPanel();
        wordsPanel.setLayout(new BoxLayout(wordsPanel, BoxLayout.Y_AXIS));
        wordsPanel.setBorder(BorderFactory.createTitledBorder("Enter Words Found"));
        
        wordsArea = new JTextPane();
        scrollPane = new JScrollPane(wordsArea);
        scrollPane.setPreferredSize(new Dimension(180, 330));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        
        timeLabel = new JLabel("3:00");
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timeLabel.setFont(new Font("Serif", Font.PLAIN, 48));
        timeLabel.setPreferredSize(new Dimension(240, 100));
        timeLabel.setMinimumSize(new Dimension(240, 100));
        timeLabel.setMaximumSize(new Dimension(240, 100));
        timeLabel.setBorder(BorderFactory.createTitledBorder("Time Left"));        
        
        shakeDice = new JButton("Shake Dice");
        shakeDice.setPreferredSize(new Dimension(240, 100));
        shakeDice.setMinimumSize(new Dimension(240, 100));
        shakeDice.setMaximumSize(new Dimension(240, 100));
        
        shakeDice.addActionListener(resetGameListener);
        wordsPanel.add(scrollPane);
        wordsPanel.add(timeLabel);
        wordsPanel.add(shakeDice);
    }

    private void setupScragglePanel()
    {   
        //instanciaton/formatting
        scragglePanel = new JPanel();
        scragglePanel.setLayout(new GridLayout(4, 4));
        scragglePanel.setMinimumSize(new Dimension(400, 400));
        scragglePanel.setMaximumSize(new Dimension(400, 400));
        scragglePanel.setPreferredSize(new Dimension(400, 400));
        scragglePanel.setBorder(BorderFactory.createTitledBorder("Scraggle Board"));
        
        diceButtons = new JButton[GRID][GRID];
        
        for(int row = 0; row < GRID; row++){
            for(int col = 0; col < GRID; col++)
            {
                //get image, create image icon, passicon as arg to button constructor
                URL imgUrl = getClass().getResource(game.getGrid()[row][col].getImgPath());
                ImageIcon icon = new ImageIcon(new ImageIcon(imgUrl).getImage().getScaledInstance(85, 85, 0));
                diceButtons[row][col] = new JButton(icon);
                
                //formatting
                diceButtons[row][col].setPreferredSize(new Dimension(95, 95));
                diceButtons[row][col].setMinimumSize(new Dimension(95, 95));
                diceButtons[row][col].setMaximumSize(new Dimension(95, 95));

                //add image to JButton
                diceButtons[row][col] = new JButton(icon);
                
                //putClientProperty to easilt grab curr row/col/letter later
                diceButtons[row][col].putClientProperty("row", row);
                diceButtons[row][col].putClientProperty("col", col);
                diceButtons[row][col].putClientProperty("letter", game.getGrid()[row][col].getLetter());    
                
                //event handling
                TileListener tileListener = new TileListener();
                LetterListener letterListener = new LetterListener();
                
                diceButtons[row][col].addActionListener(tileListener);
                diceButtons[row][col].addActionListener(letterListener);
                scragglePanel.add(diceButtons[row][col]);
            }
        }
    }
    
    //exit button in menu action listener
    private class ExitListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae) {
            int response = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit Scraggle?", "Exit?", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION )
            {
                System.exit(0);
            }
        }
    }
    
    //new game and shake dice both reset the game
    private class ResetGameListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            //reset everything to default, restart clock, repaint revalidate
            timer.stop();
            
            score = 0;
            scoreLabel.setText("");
            seconds = 0;
            minutes = 3;
            currentLabel.setText("");
            
            game.populateGrid();
            frame.remove(scragglePanel);
            scragglePanel.removeAll();
            
            setupScragglePanel();
            
            frame.add(scragglePanel, BorderLayout.WEST);
            wordsArea.setText("");
            foundWords.removeAll(foundWords);
            
            //Timer setup
            timer = new Timer(1000, new TimerListener());
            timer.start();
            
            scragglePanel.revalidate();
            scragglePanel.repaint();   
        }
    
    }
    
    //submit button
    private class SubmitListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            int wordScore = game.getDictionary().search(currentLabel.getText().toLowerCase());
            //cannot submit empty string
            String empty = "";
            if(currentLabel.getText().toLowerCase().equals(empty))
            {
                currentLabel.setText("");
                JOptionPane.showMessageDialog(frame, "Please enter a word");
            }
            //cannot submit 1 or 2 letter words
            else if ((currentLabel.getText().toLowerCase().length() == 1) || (currentLabel.getText().toLowerCase().length() == 2))
            {
                currentLabel.setText("");
                JOptionPane.showMessageDialog(frame, "Please enter a word");
                //reenable all buttons after prompting user
                for(int row = 0; row < GRID; row++){
                    for(int col = 0; col < GRID; col++){
                        diceButtons[row][col].setEnabled(true);
                        
                    }
                }
            }
            //dont allow duplicate words
            else if(foundWords.contains(currentLabel.getText().toLowerCase()))
            {
                JOptionPane.showMessageDialog(frame, "Word already found");
                currentLabel.setText("");
                for(int row = 0; row < GRID; row++){
                        for(int col = 0; col < GRID; col++){
                            diceButtons[row][col].setEnabled(true);
                        
                        }
                    }
            }
            //confirm that word exists in library by checking score
            //if score is 0 word doesnt exist
            else if(wordScore == 0)
            {
                JOptionPane.showMessageDialog(frame, "Not a valid word");
                currentLabel.setText("");
                for(int row = 0; row < GRID; row++){
                        for(int col = 0; col < GRID; col++){
                            diceButtons[row][col].setEnabled(true);
                        
                        }
                    }
            }
            //final case for valid word
            else{
                if(wordScore > 0)
                {
                    //push word to text box, update score, reenable all buttons
                    wordsArea.setText(wordsArea.getText() + "\n" + currentLabel.getText());
                    wordsArea.setCaretPosition(wordsArea.getDocument().getLength());
                    foundWords.add(currentLabel.getText().toLowerCase());
                    score += wordScore;
                    scoreLabel.setText(""+ score);
                    currentLabel.setText("");
                    for(int row = 0; row < GRID; row++){
                        for(int col = 0; col < GRID; col++){
                            diceButtons[row][col].setEnabled(true);
                        
                        }
                    }
                }
            }
        }
    }
    //if letter button is clicked, push the letter to the currentLabel
    // panel to build the word one letter at a time
    private class LetterListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            if(ae.getSource() instanceof JButton)
            {
                JButton button = (JButton)ae.getSource();
                String letter = (String)button.getClientProperty("letter");
                currentLabel.setText(currentLabel.getText() + letter);
            }
                   
        }
    }
    // tile listener responsible for displaying valid and invalid tiles
    private class TileListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            if(ae.getSource() instanceof JButton)
            {
                //use getClientProperty to access row/col of selected button
                JButton anotherButton = (JButton)ae.getSource();
                int currRow = (int) anotherButton.getClientProperty("row");
                int currCol = (int) anotherButton.getClientProperty("col");
                
                //disable all buttons
                for(int row = 0; row < GRID; row++){
                    for(int col = 0; col < GRID; col++){
                        diceButtons[row][col].setEnabled(false);
                        
                    }
                }
                //enable all buttons adjacent to selected button. all these if statements 
                //protect the setEnabled() function from trying to access data that is out of bounds
                //prevents null ptr exception
                if(!(((currRow - 1) < 0) || ((currCol - 1) < 0)))
                {
                   diceButtons[currRow - 1][currCol - 1].setEnabled(true);
                }
                if(!(((currRow - 1) < 0) || ((currCol + 1) > 3)))
                {
                    diceButtons[currRow - 1 ][currCol + 1].setEnabled(true);
                }
                if(!(((currRow + 1) > 3) || ((currCol + 1) > 3)))
                {
                    diceButtons[currRow + 1][ currCol + 1].setEnabled(true);
                }
                if(!(((currRow + 1) > 3) || ((currCol - 1) < 0)))
                {
                    diceButtons[currRow + 1][currCol - 1].setEnabled(true);
                }
                if(!((currRow + 1) > 3))
                {
                    diceButtons[currRow + 1][currCol].setEnabled(true);
                }
                if(!((currRow - 1) < 0))
                {
                    diceButtons[ currRow - 1 ][currCol].setEnabled(true);
                }
                if(!((currCol + 1) > 3))
                {
                    diceButtons[currRow][currCol + 1].setEnabled(true);
                }
                if(!((currCol - 1) < 0))
                {
                    diceButtons[currRow][currCol - 1].setEnabled(true);
                }
            }                    
        }
    }
    
    //timer class is copy pasted from Prof Whiting example code
    private class TimerListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            if(seconds == 0 && minutes == 0)
            {              
                timer.stop();
                JOptionPane.showMessageDialog(frame, "Time is up! Game over!\nScore: " + score);
            }
            else
            {
                if(seconds == 0)
                {
                    seconds = 59;
                    minutes--;
                }
                else
                {
                    seconds--;
                }
            }

            if(seconds < 10)
            {
                String strSeconds = "0" + String.valueOf(seconds);
                timeLabel.setText(String.valueOf(minutes) + ":" + strSeconds);
            }
            else
            {
                timeLabel.setText(String.valueOf(minutes) + ":" + String.valueOf(seconds));
            }
            
        }
    }
}