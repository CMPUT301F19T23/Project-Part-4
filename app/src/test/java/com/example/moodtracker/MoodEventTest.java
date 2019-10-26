package com.example.moodtracker;

import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;
import static com.example.moodtracker.EmotionData.ANGRY_DATA;
import static com.example.moodtracker.EmotionData.HAPPY_DATA;
import static com.example.moodtracker.EmotionData.NEUTRAL_DATA;
import static com.example.moodtracker.EmotionData.SAD_DATA;

public class MoodEventTest {

    private MoodEvent mockMoodEvent(){
        return new MoodEvent(mockDate(), "Happy", "I just am.");
    }

    private Calendar mockDate(){
        return new GregorianCalendar(1999, Calendar.DECEMBER, 31, 23, 59);
    }

    private EmotionData mockEmotionData(){
        return new EmotionData("Happy", ":)", 0);
    }

    @Test
    void testSetEmotion(){
        MoodEvent m = mockMoodEvent();
        EmotionData e = mockEmotionData();

        assertTrue(m.getEmotion().equals(e.getEmotion()));
        assertTrue(m.getEmoji().equals(e.getEmoji()));
        assertEquals(m.getColor(), HAPPY_DATA.getColor());

        // should get same result regardless of case, as long as letters match
        // NOTE: change emotion each time, since a failure results in no change

        // uppercase
        m.setEmotion("SAD");
        assertTrue(m.getEmotion().equals(SAD_DATA.getEmotion()));
        assertTrue(m.getEmoji().equals(SAD_DATA.getEmoji()));
        assertEquals(m.getColor(), SAD_DATA.getColor());

        // lowercase
        m.setEmotion("angry");
        assertTrue(m.getEmotion().equals(ANGRY_DATA.getEmotion()));
        assertTrue(m.getEmoji().equals(ANGRY_DATA.getEmoji()));
        assertEquals(m.getColor(), ANGRY_DATA.getColor());

        // mixed case
        m.setEmotion("hAppY");
        assertTrue(m.getEmotion().equals(HAPPY_DATA.getEmotion()));
        assertTrue(m.getEmoji().equals(HAPPY_DATA.getEmoji()));
        assertEquals(m.getColor(), HAPPY_DATA.getColor());

        // invalid emotion
        m.setEmotion("text");
        assertTrue(m.getEmotion().equals(HAPPY_DATA.getEmotion()));
        assertTrue(m.getEmoji().equals(HAPPY_DATA.getEmoji()));
        assertEquals(m.getColor(), HAPPY_DATA.getColor());

        // test cases should be sufficient, from here make sure to test each predefined mood as we add them

        //  neutral
        m.setEmotion("Neutral");
        assertTrue(m.getEmotion().equals(NEUTRAL_DATA.getEmotion()));
        assertTrue(m.getEmoji().equals(NEUTRAL_DATA.getEmoji()));
        assertEquals(m.getColor(), NEUTRAL_DATA.getColor());
    }

    @Test
    void testGetEmotion(){
        MoodEvent m = mockMoodEvent();
        // don't use == for strings
        assertTrue(m.getEmotion().equals("Happy"));
    }

    @Test
    void testGetEmoji(){
        MoodEvent m = mockMoodEvent();
        // don't use == for strings
        assertTrue(m.getEmoji().equals(":)"));
    }

    @Test
    void testGetColor(){
        MoodEvent m = mockMoodEvent();
        assertEquals(m.getColor(), HAPPY_DATA.getColor());
    }

    @Test
    void testGetReasonString(){
        MoodEvent m = mockMoodEvent();
        assertTrue(m.getReasonString().equals(mockMoodEvent().getReasonString()));
    }

    @Test
    void testSetReasonString(){
        MoodEvent m = mockMoodEvent();
        String testStr = "Some Reason";
        m.setReasonString(testStr);
        assertTrue(m.getReasonString().equals(testStr));
    }

    @Test
    void testGetDate(){
        MoodEvent m = mockMoodEvent();
        Calendar mc = m.getDate();
        Calendar c = mockDate();
        assertEquals(mc.get(Calendar.YEAR), c.get(Calendar.YEAR));
        assertEquals(mc.get(Calendar.MONTH), c.get(Calendar.MONTH));
        assertEquals(mc.get(Calendar.DAY_OF_MONTH), c.get(Calendar.DAY_OF_MONTH));
        assertEquals(mc.get(Calendar.HOUR_OF_DAY), c.get(Calendar.HOUR_OF_DAY));
        assertEquals(mc.get(Calendar.MINUTE), c.get(Calendar.MINUTE));
    }

    @Test
    void testSetDate(){
        MoodEvent m = mockMoodEvent();
        m.setDate(2000, Calendar.JANUARY, 1, 0, 0);
        Calendar mc = m.getDate();
        Calendar c = mockDate();
        assertNotEquals(mc.get(Calendar.YEAR), c.get(Calendar.YEAR));
        assertNotEquals(mc.get(Calendar.MONTH), c.get(Calendar.MONTH));
        assertNotEquals(mc.get(Calendar.DAY_OF_MONTH), c.get(Calendar.DAY_OF_MONTH));
        assertNotEquals(mc.get(Calendar.HOUR_OF_DAY), c.get(Calendar.HOUR_OF_DAY));
        assertNotEquals(mc.get(Calendar.MINUTE), c.get(Calendar.MINUTE));
    }

}
