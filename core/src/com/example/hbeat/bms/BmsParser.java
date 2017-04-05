package com.example.hbeat.bms;

/**
 * Created by Jordan on 4/4/2017.
 */

public class BmsParser {

    /***
     * Simple bms parser
     * @param bmsFileLines
     * lines of bms file
     * @return
     * parsed bms file
     */
    public static BmsSongData parseBMS(String[] bmsFileLines) {
        BmsSongData bms = new BmsSongData();
        for (String line : bmsFileLines) {
            // Directive lines
            if (line.length() > 0 && line.charAt(0) == '#') {
                // remove # at beginning
                line = line.substring(1);
                String[] noteData = line.split(":");
                if (noteData.length > 1) {
                    // Is a note (integers split by :)
                    // Note specification:
                    // #xxxyy: zzzz, where xxx is the measure number, yy is the command, and
                    noteData[0] = noteData[0].trim();
                    noteData[1] = noteData[1].trim();

                    if (noteData[0].matches("\\d+")) {
                        bms.addNotesFromLine(noteData[0], noteData[1]);
                    }
                }
                // Is string (metadata key value)
                else {
                    String [] metadata = line.split(" ");
                    if (metadata.length > 1) {
                        bms.addMetadata(metadata[0], metadata[1]);
                    } else {
                        bms.addMetadata(metadata[0], "");
                    }
                }
            }
        }
        return bms;
    }


}
