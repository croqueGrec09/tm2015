package de.uni_koeln.spinfo.textengineering.ir.web;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uni_koeln.spinfo.textengineering.ir.boole.Preprocessor;
import de.uni_koeln.spinfo.textengineering.ir.web.crawler.LinkHelper;
import de.uni_koeln.spinfo.textengineering.tm.document.Document;

/*
 * Das Ergebnis der Verarbeitung einer URL: Ein Web-Dokument mit der Herkunfts-URL (z.B. für
 * Relevanz-Gewichtungen), dem eigentlichen Inhalt als String und den ausgehenden Links (für
 * weitergehendes Crawling etc.). Hier haben wir die Links einfach im Dokument anstelle einer
 * eigenen Frontier mit front queues und back queues (siehe IIR-Buch, Kap. 20)
 */
/**
 * Document representation of a website, consisting of the text, the URL and the outgoing links.
 * 
 * @author Fabian Steeg, Claes Neuefeind
 */
public final class WebDocument implements Document {

	private String text;
	private Set<String> links;
	private String url;

	// für die Indexterme:
	private Map<String, Integer> tf;
	private List<String> tokens;
	private static Preprocessor PREPROCESSOR = new Preprocessor();

	/**
	 * @param url
	 *            The URL this document represents
	 * @param text
	 *            The plain text of this document
	 * @param links
	 *            The outgoing links
	 */
	public WebDocument(final String url, final String text, final Set<String> links) {
		if (url == null || text == null || links == null) {
			throw new IllegalArgumentException("Document parameters must not be null");
		}
		this.text = text;
		/* Die Links müssen normalisiert werden: */
		Set<String> cleanLinks = LinkHelper.normalize(links, url);
		/* Und sollten keine verbotenen Ziele haben: */
		cleanLinks = LinkHelper.allowed(cleanLinks, url);
		this.links = cleanLinks;
		this.url = url;
		// die Indexterme:
		this.tokens = PREPROCESSOR.tokenize(text);
		this.tf = computeTf();
	}

	/*
	 * Wir überschreiben equals und hashCode damit wir doppelte Dokumente vermeiden können, indem wir einfach eine
	 * entsprechende Datenstruktur verwenden (hier ein Set).
	 */

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof WebDocument)) {
			return false;
		}
		WebDocument that = (WebDocument) obj;
		return this.url.toString().equals(that.url.toString()) && this.text.equals(that.text)
				&& this.links.equals(that.links);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		final int start = 17;
		int result = start;
		result = prime * result + url.toString().hashCode();
		result = prime * result + text.hashCode();
		result = prime * result + links.hashCode();
		return result;
	}

	/* Und wir überschreiben toString um hilfreiche Ausgaben zu bekommen: */
	@Override
	public String toString() {
		return String.format("WebDocument at %s with %s outgoing links and text size %s", url, links.size(),
				text.length());
	}

	/**
	 * @return The text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @return The outgoing links
	 */
	public Set<String> getLinks() {
		return links;
	}

	/**
	 * @return The URL
	 */
	public URL getLocation() {
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Ab hier Ergänzungen durch das Interface:

	@Override
	public String getSource() {
		return this.url;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uni_koeln.spinfo.textengineering.tm.document.Document#getTerms()
	 * 
	 * Die Indexterme sind hier nochmal innerhalb der Document-Klasse realisiert (übernommen aus
	 * de.uni_koeln.spinfo.textengineering.ir.ranked.Document) - in package tm folgt dann eine spezifischere Lösung
	 */
	@Override
	public Set<String> getTerms() {
		return tf.keySet();
	}

	@Override
	public Integer getTermFrequencyOf(String dictionaryTerm) {
		Integer integer = tf.get(dictionaryTerm);
		return integer == null ? 0 : integer;
	}

	private Map<String, Integer> computeTf() {
		Map<String, Integer> termMap = new HashMap<String, Integer>();
		/* Wir zählen die Häufigkeiten der Tokens: */
		for (String token : tokens) {
			Integer tf = termMap.get(token);
			/*
			 * Wenn der Term noch nicht vorkam, beginnen wir zu zählen (d.h. wir setzen 1)
			 */
			if (tf == null) {
				tf = 1;
			} else {// sonst zählen wir einfach bei jedem Vorkommen hoch
				tf = tf + 1;
			}
			termMap.put(token, tf);
		}
		return termMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uni_koeln.spinfo.textengineering.tm.document.Document#getTopic()
	 * 
	 * Da dieses WebDocument nur im IR-Kontext eingesetzt wird, spielt das topic zunächst keine Rolle.
	 */
	public String getTopic() {
		return "UNKNOWN";
	}

}
