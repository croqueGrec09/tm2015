/**
 * Material for the course 'Text-Mining', University of Cologne.
 * (http://www.spinfo.phil-fak.uni-koeln.de/spinfo-textmining.html)
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
package de.uni_koeln.spinfo.textengineering.tm.document;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.uni_koeln.spinfo.textengineering.tm.corpus.Corpus;
import de.uni_koeln.spinfo.textengineering.tm.corpus.CorpusDatabase;
import de.uni_koeln.spinfo.textengineering.tm.corpus.crawler.Crawler;

/**
 * Material for the course 'Text-Mining', University of Cologne.
 * (http://www.spinfo.phil-fak.uni-koeln.de/spinfo-textmining.html)
 * 
 * @author Fabian Steeg
 */
public class TestFeatureVector {
	private static final String DATA = "data/corpus-tm-1.db";
	private Corpus corpus;
	private Document document;

	/*
	 * Das Drumherum: Korpus einmal crawlen, öffnen und schliessen vor und nach jedem Test (jeder Test soll atomar sein,
	 * alleine und unabhängig von den anderen ausführbar).
	 */

	public static void main(final String[] args) {
		/* Hier erstellen und crawlen (dauert). */
		Corpus c = CorpusDatabase.create(DATA);
		List<String> seed = Arrays.asList("http://www.spiegel.de", "http://www.bild.de");
		List<WebDocument> documents = Crawler.crawl(1, seed);
		c.addAll(documents);
	}

	@Before
	public void before() {
		/* Hier (vor jedem Test) nur öffnen. */
		corpus = CorpusDatabase.open(DATA);
		document = corpus.getDocumentsForSource("spiegel").get(0);
		System.out.println("-----------------------------------------------------------------------------------");
	}

	@After
	public void after() {
		/* Hier (nach jedem Test) schliessen. */
		corpus.close();
	}

	@Test
	public void vector() {
		/*
		 * Durch den Einbau der IR-Komponenten bekommen wir nun Merkmalsvektoren von den Dokumenten, die in unserer
		 * Korpus-Datenbank sind
		 */
		FeatureVector vector = ((WebDocument) document).getVector(corpus);
		Assert.assertTrue("Vector is null", vector != null);
		Assert.assertTrue("Vector contains no elements", vector.getValues().size() > 0);
		System.out.println("Vector values:\n" + vector.getValues());
	}

}
