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
import org.jsoup.select.Elements;

/**
 * Hello world!
 *
 */
public class App {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		String corpusPath = "F:" + File.separator + "WT10G" + File.separator + "corpus" + File.separator;
		String outputHtmls = "F:" + File.separator + "WT10G" + File.separator + "pages" + File.separator;

		File file = new File(corpusPath);

		for (String wtx : file.list()) {
			String wtxPath = corpusPath + wtx;
			File file3 = new File(wtxPath);
			if (file3.isDirectory()) {
				for (String gzFile : file3.list()) {
					GZIPInputStream inputStream = new GZIPInputStream(
							new FileInputStream(wtxPath + File.separator + gzFile));

					Document doc = Jsoup.parse(inputStream, null, gzFile);

					for (Element element : doc.select("doc")) {
						element.tagName("html");
						element.select("doc").remove();
						element.select("docoldno").remove();
						element.select("dochdr").tagName("head");
						// element.appendElement("uri").html(element.select("head").text().split("
						// ")[0]);
						Elements all = element.children().not("head").not("title").not("uri").not("body").not("docno");
						if (element.select("body").size() == 0) {
							element.appendElement("body").html(all.toString());
						} else {
							element.select("body").html(all.toString());
						}
						all.remove();
						String text = element.toString();
						String[] split = element.select("docno").text().split("-");
						File file2 = new File(outputHtmls + File.separator + split[0] + File.separator + split[1]);
						file2.mkdirs();
						FileOutputStream fileOutputStream = new FileOutputStream(
								file2.getPath() + File.separator + split[2] + ".html");
						fileOutputStream.write(text.getBytes());
						fileOutputStream.flush();
						fileOutputStream.close();
					}
				}
			}
		}

	}
}
