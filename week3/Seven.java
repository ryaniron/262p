import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class Seven {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage: java Style7 <text-file>");
            return;
        }

        // Load stop words (comma-separated) and add single letters a–z
        Set<String> stops = new HashSet<>(Arrays.asList(
            Files.readString(Paths.get("stop_words.txt")).split(",")));
        for (char c = 'a'; c <= 'z'; c++) stops.add(String.valueOf(c));

        // Read the input file and split on non-letters
        String text = Files.readString(Paths.get(args[0]));
        String[] words = text.toLowerCase().split("[^a-zA-Z]+");

        // Count word frequencies (excluding stop words)
        Map<String, Integer> freqs = new HashMap<>();
        for (String w : words)
            if (w.length() > 0 && !stops.contains(w))
                freqs.put(w, freqs.getOrDefault(w, 0) + 1);

        // Sort by frequency (descending) and print top 25
        freqs.entrySet().stream()
             .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
             .limit(25)
             .forEach(e -> System.out.println(e.getKey() + " - " + e.getValue()));
    }
}
