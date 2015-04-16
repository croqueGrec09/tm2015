package de.uni_koeln.spinfo.textengineering.ir.ranked;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import de.uni_koeln.spinfo.textengineering.ir.boole.Intersection;
import de.uni_koeln.spinfo.textengineering.ir.boole.Preprocessor;

public class InvertedIndex implements InformationRetrieval {

	private Map<String, SortedSet<Integer>> index;
	private static final Preprocessor PREPROCESSOR = new Preprocessor();
	// NEU: Korpus für Zugriff auf Werke (vgl. PositionalIndex)
	private Corpus corpus;

	public InvertedIndex(Corpus corpus) {
		long start = System.currentTimeMillis();
		this.corpus = corpus;// NEU: Korpus mit ablegen
		index = index(corpus);
		System.out.println("Index erstellt, Dauer: "
				+ (System.currentTimeMillis() - start) + " ms.");
	}

	private Map<String, SortedSet<Integer>> index(Corpus corpus) {
		HashMap<String, SortedSet<Integer>> index = new HashMap<String, SortedSet<Integer>>();
		// NEU: 'Documents' statt Strings
		List<Document> works = corpus.getWorks();
		for (int i = 0; i < works.size(); i++) {
			// NEU: Preprocessor muss schon im Document eingesetzt werden
			Set<String> terms = works.get(i).getTerms();
			// der Rest bleibt wie bisher ...
			for (String t : terms) {
				SortedSet<Integer> postings = index.get(t);
				if (postings == null) {
					postings = new TreeSet<Integer>();
					index.put(t, postings);
				}
				postings.add(i);
			}
			// printSortedIndexTerms(index);//optionale Ausgabe der Indexterme
		}
		return index;
	}

	/*
	 *  NEU: Rückgabe von Documents anstelle von docIds.
	 */
	@Override
	public Set<Document> search(String query) {
		long start = System.currentTimeMillis();
		List<String> queries = PREPROCESSOR.process(query);
		List<SortedSet<Integer>> allPostings = new ArrayList<SortedSet<Integer>>();
		for (String q : queries) {
			SortedSet<Integer> postings = index.get(q);
			allPostings.add(postings);
		}
		Collections.sort(allPostings, new Comparator<SortedSet<Integer>>() {
			public int compare(SortedSet<Integer> o1, SortedSet<Integer> o2) {
				return Integer.valueOf(o1.size()).compareTo(o2.size());
			}
		});
		SortedSet<Integer> result = allPostings.get(0);
		for (SortedSet<Integer> set : allPostings) {
			result = Intersection.of(result, set);
		}
		System.out.println("Suchdauer: " + (System.currentTimeMillis() - start)
				+ " ms.");
		/*
		 * NEU: Abschließend holen wir zu jeder docId das passende Document,
		 * indem wir sie uns direkt vom Korpus geben lassen ...
		 */
		Set<Document> resultAsDocSet = new HashSet<Document>();
		for (Integer docId : result) {
			Document doc = getWorks().get(docId);
			resultAsDocSet.add(doc);
		}
		return resultAsDocSet;
	}

	/*
	 *  Alle Dokumente.
	 */
	public List<Document> getWorks() {
		return corpus.getWorks();
	}

	/*
	 * Ausgabe der Indexterme.
	 */
	@SuppressWarnings("unused")
	private void printSortedIndexTerms(Map<String, SortedSet<Integer>> index) {
		TreeSet<String> terms = new TreeSet<String>(index.keySet());
		for (String string : terms) {
			System.out.println(string);
		}
		System.out.println("Anzahl Terme: " + terms.size());
	}

	public Set<String> getTerms() {
		return index.keySet();
	}

	/*
	 *  Die Dokumentenfrequenz zu einem Term:
	 */
	public Integer getDocFreq(String t) {
		return index.get(t).size();
	}

}
