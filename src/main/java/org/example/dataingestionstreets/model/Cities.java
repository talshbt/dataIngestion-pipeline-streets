package org.example.dataingestionstreets.model;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

public class Cities {

    public static final Map<String, String> HEBREW_FORMATTED_NAMES;

    static {
        Map<String, String> hebrewMap = new HashMap<>();
        hebrewMap.put("Jerusalem", "ירושלים             ");
        hebrewMap.put("Tel Aviv Jaffa", "תל אביב - יפו       ");
        hebrewMap.put("Haifa", "חיפה                ");

        HEBREW_FORMATTED_NAMES = Collections.unmodifiableMap(hebrewMap);
    }
}