package br.usp.falvojr.wordwrap;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

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
            final String inputData = new String(Files.readAllBytes(inputPath), Charset.defaultCharset());
            
            String[] words = inputData.split(WordWrapUtils.INPUT_REGEX);
            int l = Integer.parseInt(words[0]);
            words = (String[]) ArrayUtils.remove(words, 0);

            DynamicProgramming.getInstance().init();
            DynamicProgramming.getInstance().opt(words, l);
            DynamicProgramming.getInstance().printSolution(words);
        } catch (final IOException e) {
            System.err.println(e.getMessage());
        }
    }

}
