package de.uni_koeln.spinfo.textengineering.ir.lucene;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;


public class Corpus {

	private String text;
	// NEU: Verwendung von Lucene-Documents anstelle der eigenen Document-Klasse
	private List<Document> works;

	public Corpus(String location, String worksDelimiter, String titleDelimiter) {
		StringBuilder sb = new StringBuilder();
		try {
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(new File(location));
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				sb.append(line);
				sb.append("\n");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		text = sb.toString();
		works = new ArrayList<Document>();
		List<String> worksAsList = Arrays.asList(text.split(worksDelimiter));
		for (String work : worksAsList.subList(1, worksAsList.size())) {
			String title = (work.trim().substring(0,
					work.trim().indexOf(titleDelimiter))).trim();
			// NEU: Wir kapseln die Werke direkt als Lucene-Doc:
			Document doc = buildLuceneDocument(work, title,
					worksAsList.indexOf(work));
			works.add(doc);
		}
	}

	/*
	 * Ein Lucene-Document ist ein Container für sog. Fields. Strukturell ähnelt
	 * ein Field einer Map<Key, Value>, d.h. auf einen Key (Id) wird ein Value
	 * (textuelle Daten) abgebildet.
	 */
	private Document buildLuceneDocument(String work, String title, int docId) {

		Document doc = new Document();
		doc.add(new TextField("contents", work, Store.NO));
		doc.add(new TextField("title", title, Store.YES));
		doc.add(new TextField("text", work, Store.YES));
		// StringField wird nicht tokenisiert, gut z.B. für Sortierung
		doc.add(new StringField("title", title, Store.YES));
		doc.add(new IntField("docId", docId, Store.YES));
		return doc;
	}

	// NEU: Korpus als Sammlung von Document-Objekten
	public List<Document> getWorks() {
		return works;
	}

	public String getText() {
		return text;
	}

}
