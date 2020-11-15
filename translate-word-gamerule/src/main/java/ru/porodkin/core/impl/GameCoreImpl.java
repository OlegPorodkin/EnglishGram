package ru.porodkin.core.impl;

import ru.porodkin.core.GameCore;
import ru.porodkin.core.dao.WordDao;
import ru.porodkin.core.exceptions.CoreException;

public class GameCoreImpl implements GameCore {

    private final WordDao dao;

    public GameCoreImpl(WordDao dao) {
        this.dao = dao;
    }

    @Override
    public String guessWord() {
        return dao.getWord();
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
