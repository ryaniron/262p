import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Eight {
    // Iterative (tail-recursion-style) parser
    static List<String> parse(Reader reader, Set<String> stopWords) throws IOException {
        List<String> words = new ArrayList<>();
        StringBuilder currentWord = new StringBuilder();

        int ch;
        while ((ch = reader.read()) != -1) {
            char c = (char) ch;

            if (Character.isLetterOrDigit(c)) {
                currentWord.append(Character.toLowerCase(c));
            } else {
                if (currentWord.length() > 0) {
                    String w = currentWord.toString().toLowerCase();
                    if (!stopWords.contains(w)) words.add(w);
                    currentWord.setLength(0); // reset for next word
                }
            }
        }

        // Handle last word at EOF
        if (currentWord.length() > 0) {
            String w = currentWord.toString().toLowerCase();
            if (!stopWords.contains(w)) words.add(w);
        }

        return words;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage: java RecursiveTermFrequencySafe <text-file>");
            return;
        }

        // Load stop words (and single letters)
        Set<String> stopWords = new HashSet<>();
        String[] stops = Files.readString(Paths.get("stop_words.txt")).split(",");
        Collections.addAll(stopWords, stops);
        for (char c = 'a'; c <= 'z'; c++) stopWords.add(String.valueOf(c));

        // Parse words
        List<String> words;
        try (Reader reader = new BufferedReader(new FileReader(args[0]))) {
            words = parse(reader, stopWords);
        }

        // Count frequencies
        Map<String, Integer> freqs = new HashMap<>();
        for (String w : words)
            freqs.put(w, freqs.getOrDefault(w, 0) + 1);

        // Sort and print top 25
        freqs.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(25)
                .forEach(e -> System.out.println(e.getKey() + " - " + e.getValue()));
    }
}
