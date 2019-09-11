/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.carlayres.hangman;

/**
 *
 * @author tycoo
 */
public class GameState {
    
    private Long id;
    private String guessBoard;
    private String guessedLetters;
    private int numberOfFailedGuesses;
    private boolean gameOver;

    public String getGuessedLetters() {
        return guessedLetters;
    }

    public void setGuessedLetters(String guessedLetters) {
        this.guessedLetters = guessedLetters;
    }
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuessBoard() {
        return guessBoard;
    }

    public void setGuessBoard(String guessBoard) {
        this.guessBoard = guessBoard;
    }

    public int getNumberOfFailedGuesses() {
        return numberOfFailedGuesses;
    }

    public void setNumberOfFailedGuesses(int numberOfFailedGuesses) {
        this.numberOfFailedGuesses = numberOfFailedGuesses;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public GameState(Long id, String guessBoard, String guessedLetters, int numberOfFailedGuesses, boolean gameOver) {
        this.id = id;
        this.guessBoard = guessBoard;
        this.guessedLetters = guessedLetters;
        this.numberOfFailedGuesses = numberOfFailedGuesses;
        this.gameOver = gameOver;
    }
    
    
    
}
