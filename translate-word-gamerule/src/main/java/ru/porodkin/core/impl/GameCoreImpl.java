package ru.porodkin.core.impl;

import ru.porodkin.core.GameCore;
import ru.porodkin.core.exceptions.CoreException;

public class GameCoreImpl implements GameCore {

    @Override
    public String guessWord() {
        return null;
    }

    @Override
    public boolean checkResult(String answerChoice) throws CoreException {
        return false;
    }

    @Override
    public int countGuessWord() {
        return 0;
    }
}
