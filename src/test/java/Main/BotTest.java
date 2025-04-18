package Main;

import org.junit.Test;
import static org.junit.Assert.*;

public class BotTest {

    @Test
    public void getInstanceReturnsSameInstance() {
        Bot instance1 = Bot.getInstance();
        Bot instance2 = Bot.getInstance();

        assertSame("getInstance should always return the same instance", instance1, instance2);
    }

    @Test
    public void botIsInstanceOfUsuari() {
        Bot bot = Bot.getInstance();

        assertTrue("Bot should be an instance of Usuari", bot instanceof Usuari);
    }

    @Test
    public void botNameIsSetCorrectly() {
        Bot bot = Bot.getInstance();

        assertEquals("Bot should be initialized with name 'bot'", "bot", bot.getNom());
    }

    @Test
    public void millorJugadaAcrossIsInitiallyNull() {
        Bot bot = Bot.getInstance();

        // Using reflection to access private field for testing
        java.lang.reflect.Field field;
        try {
            field = Bot.class.getDeclaredField("millorJugadaAcross");
            field.setAccessible(true);
            assertNull("millorJugadaAcross should be initially null", field.get(bot));
        } catch (Exception e) {
            fail("Could not access millorJugadaAcross field: " + e.getMessage());
        }
    }
}