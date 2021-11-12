package aoc

import junit.framework.TestCase.assertNotNull
import org.junit.Test

class AppTest {

    @Test
    fun appHasAGreeting() {
        assertNotNull("app should have a solution", AocApp.main(arrayOf()))
    }
}
