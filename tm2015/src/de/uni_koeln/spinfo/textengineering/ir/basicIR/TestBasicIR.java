package de.uni_koeln.spinfo.textengineering.ir.basicIR;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestBasicIR {

	private static Corpus corpus;
	private static String query;

	@BeforeClass
	public static void setUp() throws Exception {
		// Korpus einlesen und in Werke unterteilen:
		String filename = "pg100.txt";
		String delimiter = "1[56][0-9]{2}\n";
		corpus = new Corpus(filename, delimiter);
	}

	@Test
	public void testCorpus() throws Exception {
		// Testen, ob Korpus korrekt angelegt wurde:
		List<String> works = corpus.getWorks();
		System.out.println("Anzahl der Werke: " + works.size());
		assertTrue("Korpus sollte mehr als 1 Werk enthalten", works.size() > 1);
	}

	@Test
	public void testLinearSearch() {
		// Testen, ob lineare Suche ein Ergebnis liefert:

		System.out.println();
		System.out.println("Lineare Suche:");
		System.out.println("-------------------");
		LinearSearch linear = new LinearSearch(corpus);

		query = "Brutus";
		Set<Integer> result = linear.search(query);
		System.out.println("Ergebnis für " + query + ": " + result);
		assertTrue("Mindestens ein Treffer erwartet", result.size() >= 1);

		query = "Brutus Caesar";
		Set<Integer> result2 = linear.search(query);
		System.out.println("Ergebnis für " + query + ": " + result2);
		assertTrue("Ergebnis-Set sollte größer sein als bei einzelnem Term",
				result2.size() >= result.size());
	}

	@Test
	public void testMatrixSearch() {
		// Testen, ob Suche in Term-Dokument-Matrix ein Ergebnis liefert:

		System.out.println();
		System.out.println("Term-Dokument-Matrix:");
		System.out.println("-------------------");
		TermDokumentMatrix matrix = new TermDokumentMatrix(corpus);

		query = "Brutus";
		Set<Integer> result = matrix.search(query);
		System.out.println("Ergebnis für " + query + ": " + result);
		assertTrue("Mindestens ein Treffer erwartet", result.size() >= 1);

		query = "Caesar";
		Set<Integer> result1 = matrix.search(query);
		System.out.println("Ergebnis für " + query + ": " + result1);
		assertTrue("Mindestens ein Treffer erwartet", result1.size() >= 1);

		// hier behandeln wir die Terme als ODER-verknüpft:
		query = "Brutus Caesar";
		Set<Integer> result2 = matrix.search(query);
		System.out.println("OR-Ergebnis für " + query + ": " + result2);
		assertTrue("Ergebnis-Set sollte größer sein als bei einzelnem Term",
				result2.size() >= result.size());

		// das gleiche nochmal mit UND-Verknüpfung:
		query = "Brutus Caesar";
		Set<Integer> result3 = matrix.booleanSearch(query);
		System.out.println("AND-Ergebnis für " + query + ": " + result3);
		assertTrue("Ergebnis-Set sollte kleiner sein als bei einzelnem Term",
				result2.size() >= result.size());
	}

}