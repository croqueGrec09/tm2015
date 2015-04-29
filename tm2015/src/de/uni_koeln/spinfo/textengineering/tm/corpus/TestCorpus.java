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
package de.uni_koeln.spinfo.textengineering.tm.corpus;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.uni_koeln.spinfo.textengineering.tm.corpus.crawler.Crawler;
import de.uni_koeln.spinfo.textengineering.tm.document.WebDocument;
import de.uni_koeln.spinfo.textengineering.tm.document.Document;

/**
 * Material for the course 'Text-Mining', University of Cologne.
 * (http://www.spinfo.phil-fak.uni-koeln.de/spinfo-textmining.html)
 * 
 * @author Fabian Steeg, Claes Neuefeind
 */
public class TestCorpus {
	private Corpus corpus;

	public static void main(final String[] args) {
		/* Hier erstellen und crawlen (dauert). */
		Corpus c = CorpusDatabase.create("data/corpus-tm-1.db");
		List<String> seed = Arrays.asList("http://www.spiegel.de", "http://www.bild.de");
		// List<String> seed = Arrays.asList("http://www.der-postillon.com/");// ,"http://www.spiegel.de");
		List<WebDocument> list = Crawler.crawl(1, seed);
		System.out.println("# of docs crawled: " + list.size());
		c.addAll(list);
	}

	@Before
	public void before() {
		/* Hier (vor jedem Test) nur öffnen. */
		corpus = CorpusDatabase.open("data/corpus-tm-1.db");
	}

	@Test
	public void testSourceQuery() {
		List<Document> all = corpus.getDocuments();
		List<Document> spiegel = corpus.getDocumentsForSource("spiegel.de");
		List<Document> bild = corpus.getDocumentsForSource("bild.de");
		/* Jetzt können wir erstmal überprüfen ob wir überhaupt was haben: */
		Assert.assertTrue("Found no documents at all", all.size() > 0);
		Assert.assertTrue("No documents source query", spiegel.size() > 0);
		Assert.assertTrue("No documents source query", bild.size() > 0);
		/*
		 * Und unsere Erwartungen an die Ergebnisse festhalten (durch die Strings, die als Fehlermeldung ausgegeben
		 * würden wenn die Überprüfung fehlschlägt) und überprüfen:
		 */
		Assert.assertTrue("Source result has wrong source", spiegel.get(0).getSource().contains("spiegel.de"));
		Assert.assertTrue("Source result has wrong source", bild.get(0).getSource().contains("bild.de"));
		/* Kurzausgaben zur Info: */
		System.out.println("----------------------");
		System.out.println("Results for source 'spiegel':");
		System.out.println("----------------------");
		for (Document document : spiegel) {
			System.out.println(document);
		}
		System.out.println("----------------------");
		System.out.println("Results for source 'bild':");
		System.out.println("----------------------");
		for (Document document : bild) {
			System.out.println(document);
		}
	}

	public void testTopicQuery() {
		
		//TODO
		
	}

	@After
	public void after() {
		corpus.close();
	}
}
