package de.uni_koeln.spinfo.textengineering.ir.lucene;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestLucene {

	private static Corpus corpus;
	private static String dataDir;
	private static String luceneDir;
	private static String query;

	@BeforeClass
	public static void setUp() throws Exception {

		corpus = new Corpus("pg100.txt", "1[56][0-9]{2}\n", "\n");
		/*
		 * Verzeichnis mit Beispieltexten:
		 */
		dataDir = "shakespeare/";
		/*
		 * Der CorpusSplitter prüft das betreffende Verzeichnis (dataDir) und
		 * zerlegt das Corpus ggf. in Einzeldateien, benannt nach docId+Titel
		 */
		CorpusSplitter.split(corpus, dataDir);
		/*
		 * Speicherort für den Lucene-Index:
		 */
		luceneDir = "index/";
		query = "title:\"part henry\"~5";
	}

	@Before
	public void printSkip() {
		System.out.println();
	}

	@Test
	public void testCorpusIndexing() throws IOException {
		/*
		 * Erstellt einen Lucene-Index für unser Corpus. Wir prüfen zunächst, ob
		 * mit dem Korpus alles in Ordnung ist ...
		 */
		assertEquals("Corpus sollte genau 38 Dokumente enthalten", 38, corpus
				.getWorks().size());
		/* ... und erstellen uns dann einen Indexer: */
		System.out.print("Indexing corpus ");
		Indexer indexer = new Indexer(luceneDir);
		indexer.index(corpus);
		indexer.close();
		/* Wenn alles OK ist, sollten nun genau die 38 Docs im Index sein: */
		assertEquals("Index sollte der Korpusgröße entsprechen", corpus
				.getWorks().size(), indexer.getNumDocs());
	}

	@Test
	public void testIndexer() throws Exception {
		/*
		 * Ein Lucene-Indexer, der zunächst Dateien aus einem Verzeichnis
		 * ('dataDir') einliest. Um sicherzustellen, dass tatsächlich die
		 * einzelnen Shakespeare-Texte in 'dataDir' liegen, wird in setUp() der
		 * CorpusSplitter ausgeführt.
		 */
		System.out.print("Indexing files ");
		Indexer indexer = new Indexer(luceneDir);
		indexer.index(dataDir);
		indexer.close();
		assertEquals("Index sollte genau 38 Dokumente enthalten", 38,
				indexer.getNumDocs());
	}

	@Test
	public void testSearcher() throws IOException, ParseException {
		/*
		 * Der Searcher ist das Gegenstück zum Indexer. Die Searcher-Klasse
		 * enthält einen IndexSearcher, der auf den oben erstellten (bzw. einen
		 * beliebigen) Index im entsprechenden Verzeichnis angesetzt wird:
		 */
		System.out.println("Search for " + query);
		Searcher searcher = new Searcher(luceneDir);
		searcher.search(query);
		searcher.close();
		assertTrue("Das Suchergebnis sollte nicht leer sein.",
				searcher.totalHits() > 0);
	}
}
