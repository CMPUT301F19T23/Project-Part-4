package com.example.moodtracker;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmotionDataTest {
    private EmotionData mockEmotionData(){
        return new EmotionData("Happy", ":)", 0);
    }

    @Test
    void testGetEmotion(){
        EmotionData e = mockEmotionData();
        // don't use == for strings
        assertTrue(e.getEmotion().equals("Happy"));
    }

    @Test
    void testGetEmoji(){
        EmotionData e = mockEmotionData();
        // don't use == for strings
        assertTrue(e.getEmoji().equals(":)"));
    }

    @Test
    void testGetColor(){
        EmotionData e = mockEmotionData();
        assertEquals(e.getColor(), 0);
    }
}
