package com.example.mealqr.services;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfanitiesFilter {

    public static String filterBadLanguage(String originalComment) {
        HashSet<String> curseWords = loadCurseWords();
        HashMap<String, String> evasiveSymbols = loadEvasiveSymbols();

        String copyOfOriginalComment = originalComment.toLowerCase(Locale.ROOT);

        // getting rid of sly symbols that hide word meanings
        for (Map.Entry<String, String> entry : evasiveSymbols.entrySet()) {
            copyOfOriginalComment = copyOfOriginalComment.replace(entry.getKey(), entry.getValue());
        }

        String[] copyOfOriginalCommentTokenized = copyOfOriginalComment.trim().split(" ");
        String[] originalCommentTokenized = originalComment.trim().split(" ");

        // check all word permutations of the comment
        for (int k = 0; k < copyOfOriginalCommentTokenized.length; k++) {
            StringBuilder accumulatingSentenceToCheck = new StringBuilder(copyOfOriginalCommentTokenized[k]);

            for (int l = k + 1; l < copyOfOriginalCommentTokenized.length + 1; l++) {

                if (curseWords.contains(accumulatingSentenceToCheck.toString())) {
                    for (int i = k; i < l; i++) {
                        originalCommentTokenized[i] = "#".repeat(originalCommentTokenized[i].length());
                    }
                }

                // avoid going out of index on last array index
                if (l < copyOfOriginalCommentTokenized.length)
                    accumulatingSentenceToCheck.append(" ").append(copyOfOriginalCommentTokenized[l].trim());
            }
        }

        return String.join(" ", originalCommentTokenized);
    }

    private static HashSet<String> loadCurseWords() {
        HashSet<String> curseWords = new HashSet<>();

        try(Scanner scanner = new Scanner(new File("src/main/resources/bad-words.csv"))) {
            String line;

            // read curse words and phrases and put in a map
            while (scanner.hasNext()) {
                line = scanner.nextLine();

                // skip first lines and language indication lines
                if (line.length() < 1 || line.charAt(0) == '-') {
                    continue;
                }

                curseWords.add(line.trim().toLowerCase(Locale.ROOT).replace("[,']", ""));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return curseWords;
    }

    private static HashMap<String, String> loadEvasiveSymbols() {
        HashMap<String, String> evasiveSymbols = new HashMap<>();

        try(Scanner scanner = new Scanner(new File("src/main/resources/leatspeak.csv"))) {
            String[] pairOfSymbols;

            // read curse words and phrases and put in a map
            while (scanner.hasNext()) {
                pairOfSymbols = scanner.nextLine().replace(",", "").split(" ");

                evasiveSymbols.put(pairOfSymbols[1], pairOfSymbols[0]);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return evasiveSymbols;
    }
}
