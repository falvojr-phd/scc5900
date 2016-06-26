package br.usp.falvojr.dp;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang.ArrayUtils;

/**
 * Main class.
 *
 * @author Venilton FalvoJr (falvojr)
 */
public class Main {

	public static void main(String[] args) throws Exception {
		final int dirIndex = ArrayUtils.indexOf(args, "-d");
		final int pathIndex = dirIndex + 1;
		if (dirIndex > -1) {
			try {
				final Path filePath = Paths.get(args[pathIndex]);
				System.out.println(filePath);
			} catch (InvalidPathException | IndexOutOfBoundsException exception) {
				System.err.println("O path especificado para o argumento -d nao e valido");
			}
		} else {
			System.err.println("O argumento -d e obrigatorio, bem como seu respectivo path. Sintaxe: -d [path]");
		}
	}

}
