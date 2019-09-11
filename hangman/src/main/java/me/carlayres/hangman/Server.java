/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.carlayres.hangman;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author tycoo
 */
@RestController
@RequestMapping("/")
public class Server {
    
    @Autowired
    GameRepository gameRepository;
    
    @GetMapping("/getGames")
    public ArrayList<GameState> getAllGames() {
        
        ArrayList<GameState> gameStateList = new ArrayList<>();
        List<Game> gameList = gameRepository.findAll();
        for (Game game : gameList)
        {
            GameState gs = new GameState(game.getId(), generateGuessboard(game), game.getGuessedLetters(), calculateFailedGuesses(game), game.isGameOver());
            gameStateList.add(gs);
        }
        return gameStateList;
    }
    
    @GetMapping("/getGame/{id}")
    public GameState getGame(@PathVariable(value="id") Long id)
    {
        Game repoGame = gameRepository.findById(id).get();
        String guessboard = generateGuessboard(repoGame);
        int failedGuesses = calculateFailedGuesses(repoGame);
        return new GameState(id, guessboard, repoGame.getGuessedLetters(), failedGuesses,  repoGame.isGameOver());
    }
    
    @PostMapping("/newGame/{wordToGuess}")
    public GameState newGame(@PathVariable(value="wordToGuess") String wordToGuess)
    {
        Game game = new Game(wordToGuess.toLowerCase());
        game = gameRepository.save(game);
        return new GameState(game.getId(), generateGuessboard(game), "", calculateFailedGuesses(game), false);
    }
    
    @PutMapping(path="/guessLetter/{id}/{letter}", produces="application/json")
    public GameState updateGame(@PathVariable(value="id") Long id, @PathVariable(value="letter") String letter)
    {
        //Get game from database
        Game repoGame = gameRepository.findById(id).get();
        
        //Add the letter to the list of letters guessed
        if (letter.length() == 1)
        {
            repoGame.setGuessedLetters(repoGame.getGuessedLetters() + letter.toLowerCase());
            gameRepository.save(repoGame);
        }
        
        String guessboard = generateGuessboard(repoGame);
        int failedGuesses = calculateFailedGuesses(repoGame);
        
        //Check if they win
        
        String guessedWord = repoGame.getWordToGuess();
        guessedWord = guessedWord.replaceAll(" ", "");
        for (char ch : repoGame.getGuessedLetters().toCharArray())
        {
            guessedWord = guessedWord.replaceAll(Character.toString(ch), "");
        }
        if (guessedWord.length() == 0)
        {
            repoGame.setGameOver(true);
            gameRepository.save(repoGame);
            GameState gs = new GameState(id, guessboard, repoGame.getGuessedLetters(), failedGuesses, true);
            
            
            return gs;
        }
        //If we are at 6 failed guesses, the game is over
        else if (calculateFailedGuesses(repoGame) > 5)
        {
            repoGame.setGameOver(true);
            gameRepository.save(repoGame);
            return new GameState(id, guessboard, repoGame.getGuessedLetters(), failedGuesses, true);
        }
        //If we havent reached the number of guesses, then play on!
        else
        {
            return new GameState(id, guessboard, repoGame.getGuessedLetters(), failedGuesses, false);
        }
    }
    
    public static String unique(String s) 
    { 
        String str = new String(); 
        int len = s.length(); 
        for (int i = 0; i < len; i++)  
        { 
            char c = s.charAt(i); 
            if (str.indexOf(c) < 0) 
            { 
                str += c; 
            } 
        } 
        return str; 
    } 

    private String generateGuessboard(Game repoGame) {
        String board = "";
        
        for (int i = 0; i < repoGame.getWordToGuess().length(); i++)
        {
            String stringToAdd = "";
            if (repoGame.getGuessedLetters().length() == 0)
            {
                stringToAdd = repoGame.getWordToGuess().charAt(i) == ' ' ? " " : "_";
            }
            for (int j = 0; j < repoGame.getGuessedLetters().length(); j++)
            {
                if (repoGame.getWordToGuess().charAt(i) == ' ')
                {
                    stringToAdd = " ";
                }
                else if (repoGame.getWordToGuess().charAt(i) == repoGame.getGuessedLetters().charAt(j))
                {
                    stringToAdd = Character.toString(repoGame.getGuessedLetters().charAt(j));
                    break;
                }
                else
                {
                    stringToAdd = "_";
                }
            }
            board = board + stringToAdd;
        }
        return board;
    }

    private int calculateFailedGuesses(Game repoGame) {
        String uniqueChars = unique(repoGame.getWordToGuess());
        int wrongCount = 0;
        for (int i = 0; i < repoGame.getGuessedLetters().length(); i++)
        {
            if (!uniqueChars.contains(Character.toString(repoGame.getGuessedLetters().charAt(i))))
            {
                wrongCount++;
            }
        }
        return wrongCount;
    }
}
