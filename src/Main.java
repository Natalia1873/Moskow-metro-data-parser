
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            DataCollector collector = new DataCollector();

            // Парсинг веб-страницы
            System.out.println("\nParsing web page...");
            WebParser webParser = new WebParser();
            List<MetroLine> lines = webParser.parseLines();
            List<MetroStation> webStations = webParser.parseStations();
            collector.addLines(lines);
            collector.addStations(webStations);

            // Поиск файлов
            System.out.println("\nSearching for files...");
            FileSearch fileSearch = new FileSearch();
            List<String> jsonFiles = fileSearch.findFiles("src/resources", ".json");
            List<String> csvFiles = fileSearch.findFiles("src/resources", ".csv");

            // Парсинг JSON-файлов с дополнительной информацией
            JsonParser jsonParser = new JsonParser();

            // Обработка файла с глубинами
            String depthsFile = "src/resources/depths.json";
            if (jsonFiles.contains(depthsFile)) {
                System.out.println("\nProcessing depths data...");
                List<MetroStation> stationsWithDepths = jsonParser.parse(depthsFile);
                collector.addStations(stationsWithDepths);
            }

            // Обработка файла с датами
            String datesFile = "src/resources/dates.json";
            if (jsonFiles.contains(datesFile)) {
                System.out.println("\nProcessing dates data...");
                List<MetroStation> stationsWithDates = jsonParser.parse(datesFile);
                collector.addStations(stationsWithDates);
            }

            // Парсинг остальных JSON файлов
            System.out.println("\nProcessing other JSON files...");
            for (String jsonFile : jsonFiles) {
                if (!jsonFile.equals(depthsFile) && !jsonFile.equals(datesFile)) {
                    List<MetroStation> jsonStations = jsonParser.parse(jsonFile);
                    collector.addStations(jsonStations);
                }
            }

            // Парсинг CSV
            System.out.println("\nProcessing CSV files...");
            CsvParser csvParser = new CsvParser();
            for (String csvFile : csvFiles) {
                List<MetroStation> csvStations = csvParser.parse(csvFile);
                collector.addStations(csvStations);
            }

            // Сохранение результатов
            System.out.println("\nSaving results...");
            collector.saveToFiles("src/resources/map.json", "src/resources/stations.json");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}