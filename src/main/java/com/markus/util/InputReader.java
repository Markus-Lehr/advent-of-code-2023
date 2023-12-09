package com.markus.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class InputReader {
    public static List<String> getInput(int day) {
        InputStream inputStream = InputReader.class.getResourceAsStream("/inputs/day" + day);
        List<String> result = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        try {
            for (String line; (line = bufferedReader.readLine()) != null; ) {
                result.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
