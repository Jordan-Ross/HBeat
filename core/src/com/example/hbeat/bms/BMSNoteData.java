package com.example.hbeat.bms;

import java.util.Locale;

/**
 * Created by Jordan on 4/4/2017.
 */

public class BmsNoteData {
    public int measure;
    public int channel;
    public int subdivision;
    public int beat;
    public long wav;

    /**
     * Create a new notes
     * @param measure
     * Measure the beat occurs
     * @param subdivision
     * Note subdivision for this measure
     * @param channel
     * Which channel should play the note
     * @param beat
     * What beat of the subdivision this occurs on
     * @param wav
     * The wav file to play
     */
    BmsNoteData(int measure, int subdivision, int channel, int beat, long wav) {
        this.measure = measure;
        this.channel = channel;
        this.subdivision = subdivision;
        this.beat = beat;
        this.wav = wav;
    }

    @Override
    public String toString() {
        return String.format(Locale.US,
                "measure %d: %d/%d, ch %d, wav %d",
                measure, beat, subdivision, channel, wav);
    }
}
