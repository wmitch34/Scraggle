package scraggle;

import dictionary.Dictionary;
import game.Game;
import userInterface.ScraggleUi;

/**
 *
 * @author Will Mitchell UCF COP 3330
 */
public class Scraggle
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        Dictionary dictionary = new Dictionary();
        Game game = new Game(dictionary);
        game.displayGrid();  
        
        ScraggleUi ui = new ScraggleUi(game);
    }   
}