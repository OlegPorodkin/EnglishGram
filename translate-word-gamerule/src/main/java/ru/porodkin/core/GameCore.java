package ru.porodkin.core;


import ru.porodkin.core.exceptions.CoreException;

/**
 *
 *
 */
public interface GameCore {

    /**
     * @return
     */
    String guessWord();

    /**
     * @param answerChoice
     * @return
     * @throws CoreException
     */
    boolean checkResult(String answerChoice) throws CoreException;

    /**
     * @return
     */
    int countGuessWord();
}
