package br.usp.falvojr.wordwrap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.ArrayUtils;

import br.usp.falvojr.wordwrap.algorithm.DynamicProgramming;
import br.usp.falvojr.wordwrap.util.WordWrapUtils;

/**
 * Main class.
 *
 * @author Venilton FalvoJr (falvojr)
 */
public class Main {

    private static final String INPUT_EXTENSION = ".TXT";

    public static void main(String[] args) throws Exception {
        final int dirIndex = ArrayUtils.indexOf(args, "-d");
        final int pathIndex = dirIndex + 1;
        if (dirIndex > -1) {
            try {
                final Path filePath = Paths.get(args[pathIndex]);
                Files.walk(filePath).forEach(file -> {
                    final String fileName = file.toString().toUpperCase();
                    if (Files.isRegularFile(file) && fileName.endsWith(INPUT_EXTENSION)) {
                        Main.processInput(file);
                    }
                });
            } catch (InvalidPathException | IndexOutOfBoundsException exception) {
                System.err.println("O path especificado para o argumento -d nao e valido");
            }
        } else {
            System.err.println("O argumento -d e obrigatorio, bem como seu respectivo path. Sintaxe: -d [path]");
        }
    }

    private static void processInput(Path inputPath) {
        try {
            final AtomicInteger l = new AtomicInteger();
            final List<String> words = new LinkedList<>();

            Files.lines(inputPath).forEach(row -> {
                String[] wordsSplit = row.split(WordWrapUtils.INPUT_REGEX);
                if (words.isEmpty()) {
                    l.set(Integer.parseInt(wordsSplit[0]));
                    wordsSplit = (String[]) ArrayUtils.remove(wordsSplit, 0);
                }
                words.addAll(Arrays.asList(wordsSplit));
            });
            DynamicProgramming.getInstance().init();
            DynamicProgramming.getInstance().opt(words, l.get());
            DynamicProgramming.getInstance().printSolution(words);
        } catch (final IOException e) {
            System.err.println(e.getMessage());
        }
    }

}
