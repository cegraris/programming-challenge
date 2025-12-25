package de.exxcellent.challenge;

import org.junit.jupiter.api.BeforeAll;

import java.util.Locale;

/**
 * Entry point of unit tests.
 */
class AppTest {

    @BeforeAll
    static void setUp() {
        Locale.setDefault(Locale.ENGLISH);
    }

}
