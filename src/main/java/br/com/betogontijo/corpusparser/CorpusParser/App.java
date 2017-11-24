package br.com.betogontijo.corpusparser.CorpusParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Hello world!
 *
 */
public class App {
	private final static String DOINIT = "<doc>";
	private final static String DOCEND = "</doc>";
	private final static String DOCHEADINIT = "<dochdr>";
	private final static String DOCHEADEND = "</dochdr>";
	private final static String DOCNAMEINIT = "<docno>";
	private final static String DOCNAMEEND = "</docno>";
	private final static String DOCIDINIT = "<docoldno>";
	private final static String DOCIDEND = "</docoldno>";
	// private final static String DOCURIINIT = "<uri>";
	// private final static String DOCURIEND = "</uri>";

	public static void main(String[] args) throws FileNotFoundException, IOException {
		String corpusPath = "E:/WT10G/corpus/";
		String outputHtmls = "E:/WT10G/pages/";

		File file = new File(corpusPath);

		for (String wtx : file.list()) {
			String wtxPath = corpusPath + wtx;
			for (String gzFile : new File(wtxPath).list()) {
				GZIPInputStream inputStream = new GZIPInputStream(new FileInputStream(wtxPath + "/" + gzFile));

				Document doc = Jsoup.parse(inputStream, null, gzFile);

				for (Element element : doc.select("doc")) {
					String text = element.toString().replace(DOINIT, "<html>").replace(DOCEND, "</html>")
							.replace(DOCHEADINIT, "<head>").replace(DOCHEADEND, "</head>");
					int docNameInit = text.indexOf(DOCNAMEINIT);
					int docIdName = text.indexOf(DOCIDEND);
					String nameAndId = text.substring(docNameInit + DOCNAMEINIT.length() + 2, docIdName)
							.replace(DOCNAMEEND, "").replace(DOCIDINIT, "").replaceAll("\n", "")
							.replaceAll("\\s{2,}", " ").trim();
					// String select = element.select("dochdr").toString();
					// int uriStart = select.indexOf("http");
					// int uriEnd = select.indexOf(" ", uriStart);
					// String uri = select.substring(uriStart, uriEnd);
					// text = text.substring(0, docNameInit) + DOCURIINIT + "\n"
					// + uri + "\n" + DOCURIEND + "\n"
					// + text.substring(docIdName + DOCIDEND.length() + 2,
					// text.length());

					text = text.substring(0, docNameInit)
							+ text.substring(docIdName + DOCIDEND.length() + 2, text.length());
					FileOutputStream fileOutputStream = new FileOutputStream(outputHtmls + nameAndId + ".html");
					fileOutputStream.write(text.getBytes());
					fileOutputStream.flush();
					fileOutputStream.close();
				}
			}

		}

	}
}
