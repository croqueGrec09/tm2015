/**
 * Material for the course 'Text-Engineering', University of Cologne.
 * (http://www.spinfo.phil-fak.uni-koeln.de/spinfo-textengineering.html)
 * <p/>
 * Copyright (C) 2008-2009 Fabian Steeg
 * <p/>
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later
 * version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p/>
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses/>.
 */
package de.uni_koeln.spinfo.textengineering.tm.classification;

import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.index.TermContext;

import de.uni_koeln.spinfo.textengineering.tm.document.Document;

/**
 * Naive bayes classifier strategy to use for text classification.
 * 
 * @author Fabian Steeg
 */
public class NaiveBayes implements ClassifierStrategy {

	/** Total number of documents */
	private int docCount = 0;
	/** Number of documents for each class */
	private Map<String, Integer> classFrequencies = new HashMap<String, Integer>();
	/**
	 * For each class, we map a mapping of all the terms of that class to their term frequencies:
	 */
	private Map<String, Map<String, Integer>> termFrequenciesForClasses = new HashMap<String, Map<String, Integer>>();

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.uni_koeln.spinfo.textengineering.tm.classification.ClassifierStrategy#train(de.uni_koeln.spinfo.textengineering.tm.document.Document,
	 *      java.lang.String)
	 */
	@Override
	public ClassifierStrategy train(Document document) {

		/* Als "Klasse" des Dokuments nehmen wir sein topic */
		String c = document.getTopic();
		/*
		 * Wir zählen mit, wie viele Dokumente wir insgesamt haben, für die Berechnung der A-Priori-Wahrscheinlichkeit
		 * ('prior probability')
		 */
		docCount++;
		Integer classFreq = classFrequencies.get(c);
		if (classFreq == null) {
			/* Erstes Vorkommen der Klasse: */
			classFreq = 0;
		}
		classFrequencies.put(c, classFreq + 1);
		/*
		 * Für die Evidenz: Häufigkeit eines Terms in den Dokumenten einer Klasse.
		 */
		Map<String, Integer> termFreqs = termFrequenciesForClasses.get(c);
		if (termFreqs == null) {
			/* Erstes Vorkommen der Klasse: */
			termFreqs = new HashMap<String, Integer>();
		}
		/* Jetzt für jeden Term hochzählen: */
		for (String term : document.getTerms()) {
			Integer integer = termFreqs.get(term);
			if (integer == null) {
				/* Erstes Vorkommen des Terms: */
				integer = 0;
			}
			/*
			 * Wir addieren hier die Häufigkeit des Terms im Dokument, die wir direkt aus dem Dokument bekommen. Die
			 * verschiedenen Classifier-Strategien sind somit zwar austauschbar, sie können jedoch in dieser Umsetzung
			 * nur mit Document-Implementierungen zusammenarbeiten.
			 */
			termFreqs.put(term, integer + document.getTermFrequencyOf(term));
		}
		termFrequenciesForClasses.put(c, termFreqs);
		return this;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.uni_koeln.spinfo.textengineering.tm.classification.ClassifierStrategy#classify(de.uni_koeln.spinfo.textengineering.tm.document.Document)
	 */
	@Override
	public String classify(Document document) {

		/*
		 * TODO hier muss nun die 'beste Klasse' ermittelt, indem der trainierte Classifier angewendet wird ... dafür
		 * brauchen wir noch die 'prior probability' und die Summe der Term-Evidenzen - z.B. jeweils als eigene Methode
		 */

		return null;
	}

}
