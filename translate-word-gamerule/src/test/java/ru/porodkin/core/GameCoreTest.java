package ru.porodkin.core;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.porodkin.core.dao.WordDao;
import ru.porodkin.core.impl.GameCoreImpl;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith()
public class GameCoreTest {

    private static final Logger log = LoggerFactory.getLogger(GameCoreTest.class);

    @Mock
    WordDao mockitoWordDao;

    private GameCore core;

//    @Test
//    public void whenCoreGuessWord_thenNotNull() {
//        assertThat(core.guessWord()).isNotNull();
//    }

//    @Test
//    public void whenCoreCheckResultCorrect_thenReturnTrue() {
//        String userGuess = "привет";
//        assertThat(core.checkResult(userGuess)).isEqualTo(true);
//    }

//    @Test
//    public void whenGuessWordIncorrect_thenThrowCoreException() {
//        String userGuess = "Как дела";
//        assertThrows(CoreException.class, () -> core.checkResult(userGuess));
//    }

//    @Test
//    public void whenCoreCountGuessWord_thenReturnCountOfCorrectGuessWord() {
//        assertThat(core.countGuessWord()).isEqualTo(3);
//    }

    @BeforeAll
    static void setup(){
        log.info("@BeforeAll - executes once before all test methods in this class");
    }

    @BeforeEach
    void init() {
        log.info("@BeforeEach - executes before each test method in this class");
        core = new GameCoreImpl(mockitoWordDao);
        when(mockitoWordDao.getWord()).thenReturn();
    }

    @AfterEach
    void tearDown() {
        log.info("@AfterEach - executed after each test method.");
    }

    @AfterAll
    static void done() {
        log.info("@AfterAll - executed after all test methods.");
    }

    @Test
    void test(){
        log.info("Just is a simple test");
    }

    @DisplayName("Single test successful")
    @Test
    void testSingleSuccessTest() {
        log.info("Success");
    }

    @Test
    @Disabled("Not implemented yet")
    void testShowSomething() {
    }

    @Test
    void lambdaExpressions() {
        assertTrue(Stream.of(1, 2, 3)
                .mapToInt(i -> i)
                .sum() > 5, () -> "Sum should be greater than 5");
    }

}