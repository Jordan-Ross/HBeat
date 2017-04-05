package com.example.hbeat.bms;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Contains data for a single .bms
 * Created by Jordan on 4/4/2017.
 */

public class BmsSongData {

    private HashMap<String, String> metadata;
    public ArrayList<BmsNoteData> notes;
    // TODO: support variable bpm

    public BmsSongData() {
        metadata = new HashMap<String, String>();
        notes = new ArrayList<BmsNoteData>();
    }

    public void addMetadata(String key, String value) {
        metadata.put(key, value);
    }

    /***
     * Add notes to the list. bms format specifies
     * @param command
     * The command to parse, first 3 are measure, last 2 are channel
     * @param measureNoteData
     * The data for the subdivision and notes to play in this measure
     */
    public void addNotesFromLine(String command, String measureNoteData) {
        int measure = Integer.parseInt(command.substring(0, 3));
        int channel = Integer.parseInt(command.substring(3, 5));
        int subdivision = measureNoteData.length() / 2;
        for (int beat = 0; beat < subdivision; beat++) {
            String beatStr = measureNoteData.substring(beat*2, beat*2 + 2);
            //int wavIndex = Integer.parseInt(beatStr);
            long wavIndex = Long.parseLong(beatStr, 36);
            // if there's a note here
            if (wavIndex > 0) {
                notes.add(new BmsNoteData(measure, subdivision, channel, beat, wavIndex));
            }
        }
    }

    public float getBpm() {
        return Float.parseFloat(metadata.get("BPM"));
    }
}
