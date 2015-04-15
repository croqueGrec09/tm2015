package de.uni_koeln.spinfo.textengineering.ir.lucene;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;

public class CorpusSplitter {

	/*
	 * Teilt das Shakespeare-Korpus in Einzeldateien. Dateinamen setzen sich
	 * zusammen aus docId (Index der Werk-Liste) und Titel (jew. erste Zeile).
	 */
	private static String outDir; // Zielverzeichnis

	public static void main(String[] args) {
		outDir = "shakespeare/";
		split(new Corpus("pg100.txt", "1[56][0-9]{2}\n", "\n"), outDir);
	}

	public static void split(Corpus corpus, String targetDir) {
		outDir = targetDir;
		File dir = new File(outDir);
		if (dir.mkdirs()) {
			System.out.println("CorpusSplitter: Neues Verzeichnis: " + dir.getAbsolutePath());
			split(corpus);
		} else {
			System.out.println("CorpusSplitter: Verzeichnis '" + dir.getAbsolutePath()
					+ "' vorhanden, (" + dir.listFiles().length + " Dateien).");
		}
	}

	private static void split(Corpus corpus) {
		List<Document> worksAsList = corpus.getWorks();
		for (Document work : worksAsList) {
			String title = work.get("title");
			String text = work.get("text");
			int docId = worksAsList.indexOf(work) + 1;// erstes 'Werk' entfernt
			String filename = docId + "-" + title + ".txt";
			System.out.println("Neue Datei: " + filename);
			createFile(text, filename);
		}
	}

	private static void createFile(String work, String filename) {
		try {
			FileWriter fw = new FileWriter(new File(outDir + filename));
			fw.write(work);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
