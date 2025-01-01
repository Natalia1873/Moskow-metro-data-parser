import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebParser {
    private static final String METRO_URL = "https://skillbox-java.github.io/";

    public List<MetroLine> parseLines() throws IOException {
        Document doc = Jsoup.connect(METRO_URL)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/91.0.4472.124")
                .get();
        List<MetroLine> lines = new ArrayList<>();

        System.out.println("Connected to page: " + doc.title());
        Elements lineElements = doc.select("#metrodata .js-metro-line");
        System.out.println("Found " + lineElements.size() + " line elements");

        for (Element lineElement : lineElements) {
            String number = lineElement.attr("data-line");
            String name = lineElement.text();
            System.out.println("Found line: " + name + " (number: " + number + ")");
            lines.add(new MetroLine(number, name));
        }

        return lines;
    }

    public List<MetroStation> parseStations() throws IOException {
        Document doc = Jsoup.connect(METRO_URL)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/91.0.4472.124")
                .get();
        List<MetroStation> stations = new ArrayList<>();

        // Перебираем все линии
        Elements lineElements = doc.select(".js-metro-stations[data-line]");
        System.out.println("Found " + lineElements.size() + " line containers");

        for (Element lineElement : lineElements) {
            String lineNumber = lineElement.attr("data-line");
            Elements stationElements = lineElement.select("span.name");
            System.out.println("Line " + lineNumber + " has " + stationElements.size() + " stations");

            for (Element stationElement : stationElements) {
                String name = stationElement.text();
                if (!name.isEmpty()) {
                    System.out.println("Found station: " + name + " (line: " + lineNumber + ")");
                    stations.add(new MetroStation(name, lineNumber));
                }
            }
        }

        return stations;
    }
}