package de.uni_koeln.spinfo.textengineering.ir.booleanIR;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.uni_koeln.spinfo.textengineering.ir.basicIR.Corpus;

public class TestBooleanIR {

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
	public void testIndexSearch() {
		// Testen, ob Suche in invertiertem Index ein Ergebnis liefert:

		System.out.println();
		System.out.println("Invertierter Index:");
		System.out.println("-------------------");
		InvertedIndex index = new InvertedIndex(corpus);

		query = "Brutus";
		Set<Integer> result = index.search(query);
		assertTrue("Mindestens ein Treffer erwartet", result.size() >= 1);
		System.out.println("Ergebnis für " + query + ": " + result);

		query = "Brutus Caesar";
		Set<Integer> result2 = index.search(query);
		assertTrue("Ergebnis-Set sollte kleiner sein als bei einzelnem Term",
				result2.size() <= result.size());
		System.out.println("Ergebnis für " + query + ": " + result2);
	}

	@Test
	public void testPositionalIndex() {

		System.out.println();
		System.out.println("Positional Index:");
		System.out.println("-------------------");
		PositionalIndex posIndex = new PositionalIndex(corpus);

		query = "Brutus";
		Set<Integer> result = posIndex.search(query);
		System.out.println("Ergebnis für " + query + ": " + result);
		assertTrue("ergebnis sollte nicht leer sein!", result.size() > 0);

		query = "Brutus Caesar";
		result = posIndex.search(query);// einfache Suche (wie bisher)
		System.out.println("Ergebnis für " + query + ": " + result);
		assertTrue("ergebnis sollte nicht leer sein!", result.size() > 0);

		SortedMap<Integer, List<Integer>> posResult;
		posResult = posIndex.proximitySearch(query, 2);// nur konsekutive Terme
		assertTrue("ergebnis sollte nicht leer sein!", posResult.size() > 0);
		System.out.println("Ergebnis für " + query + ": " + posResult);
		// alternativ mit Ausgabe der Fundstellen:
		posIndex.printSnippets(query, posResult, 2);

		query = "to be or not to be";
		result = posIndex.search(query);// einfache Suche (wie bisher)
		System.out.println("Ergebnis für " + query + ": " + result);
		assertTrue("ergebnis sollte nicht leer sein!", result.size() > 0);

		posResult = posIndex.proximitySearch(query, 1);// nur konsekutive Terme
		assertTrue("ergebnis sollte nicht leer sein!", posResult.size() > 0);
		System.out.println("Ergebnis für " + query + ": " + posResult);
		// alternativ mit Ausgabe der Fundstellen:
		posIndex.printSnippets(query, posResult, 1);
	}

	private static final TreeSet<Integer> PL2 = new TreeSet<Integer>(
			Arrays.asList(2, 4, 6, 8));
	private static final TreeSet<Integer> PL1 = new TreeSet<Integer>(
			Arrays.asList(1, 2, 3, 4));
	private static final List<Integer> EXPECTED = Arrays.asList(2, 4);

	@Test
	public void intersect() {
		System.out.println("Intersection-Algorithmus nach Manning et al.");
		List<Integer> list = new ArrayList<Integer>(Intersection.of(PL1, PL2));
		Assert.assertEquals(EXPECTED, list);
	}

}