package ru.porodkin.core;

import org.junit.jupiter.api.Test;
import ru.porodkin.core.exceptions.CoreException;
import ru.porodkin.core.impl.GameCoreImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameCoreTest {

    private GameCore core = new GameCoreImpl();

    @Test
    public void whenCoreGuessWord_thenNotNull() {
        assertThat(core.guessWord()).isNotNull();
    }

    @Test
    public void whenCoreCheckResultCorrect_thenReturnTrue() {
        String userGuess = "привет";
        assertThat(core.checkResult(userGuess)).isEqualTo(true);
    }

    @Test
    public void whenGuessWordIncorrect_thenThrowCoreException() {
        String userGuess = "Как дела";
        assertThrows(CoreException.class, () -> core.checkResult(userGuess));
    }

    @Test
    public void whenCoreCountGuessWord_thenReturnCountOfCorrectGuessWord() {
        assertThat(core.countGuessWord()).isEqualTo(3);
    }
}
