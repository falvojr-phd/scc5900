package br.usp.icmc.falvojr;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String TXT_EXTENSION = ".TXT";
    private static final String BIN_FOLDER = "";

    public static void main(String[] args) throws IOException, URISyntaxException {
	
	final List<Path> inputFilePaths = new ArrayList<>();
	
	final URL url = ClassLoader.getSystemResources(BIN_FOLDER).nextElement();
	
	Files.walk(Paths.get(url.toURI())).forEach(filePath -> {
	    final String fileName = filePath.getFileName().toString().toUpperCase();
	    if (Files.isRegularFile(filePath) && fileName.endsWith(TXT_EXTENSION)) {
		inputFilePaths.add(filePath);
	    }
	});

    }
}
