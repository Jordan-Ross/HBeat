package com.example.hbeat.bms;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Contains data for a single .bms
 * Created by Jordan on 4/4/2017.
 * TODO: support wav files and not just assume everything works the way it does with wav files
 */

public class BmsSongData {

    private HashMap<String, String> metadata;
    public ArrayList<BmsNoteData> notes;

    // TODO: allow multiple
    public long bgmStartTime;

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
        boolean setBgm = false;
        switch (channel) {
            case 1:
                setBgm = true;
            // P1 side (iidx)
            case 11: // key 1
            case 12: // key 2
            case 13: // key 3
            case 14: // key 4
            case 15: // key 5
            case 18: // key 6
            case 19: // key 7
            case 16: // scratch
                // continue to getting the notes
                break;
            // P2 side (iidx)
            case 21: // key 1
            case 22: // key 2
            case 23: // key 3
            case 24: // key 4
            case 25: // key 5
            case 28: // key 6
            case 29: // key 7
            case 26: // scratch
                // continue to getting the notes
                break;
            // Reflet muse - specific TODO
            case 51:
            case 52:
            case 53:
            case 55:
            case 56:
            case 58:
            case 59:
            case 61:
            case 63:
            case 65:
            case 69:
                // continue to getting the notes
                break;
            default: // Don't parse any other channels for now
                return;
        }
        int subdivision = measureNoteData.length() / 2;
        for (int beat = 0; beat < subdivision; beat++) {
            String beatStr = measureNoteData.substring(beat*2, beat*2 + 2);
            //int wavIndex = Integer.parseInt(beatStr);
            long wavIndex = Long.parseLong(beatStr, 36);
            // if there's a note here
            if (wavIndex > 0) {
                if (setBgm && wavIndex == 1) {
                    this.setBgmStartTime(measure, beat, subdivision);
                    return;
                } else if (!setBgm) {
                    notes.add(new BmsNoteData(measure, subdivision, channel, beat, wavIndex));
                }
            }
        }
    }

    // TODO Make generic
    private void setBgmStartTime(int measure, int beat, int subdivision) {
        float beatsPerSecond = this.getBpm() / 60f;
        // TODO: don't assume 4/4 time
        float measureLength = 4f / beatsPerSecond;
        float noteHitTime = (measureLength * ((float)measure + ((float)beat / (float)subdivision)));
        this.bgmStartTime = (long)(noteHitTime * 1000);
    }

    public long getBgmStarttime() {
        return this.bgmStartTime;
    }

    public float getBpm() {
        return Float.parseFloat(metadata.get("BPM"));
    }

}
