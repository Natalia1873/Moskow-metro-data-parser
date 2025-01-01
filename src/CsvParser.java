import com.opencsv.CSVReader;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CsvParser {
    public List<MetroStation> parse(String filePath) {
        List<MetroStation> stations = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            // Читаем заголовки
            String[] headers = reader.readNext();
            if (headers == null) return stations;

            // Создаем карту индексов колонок
            Map<String, Integer> headerMap = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                headerMap.put(headers[i].trim().toLowerCase(), i);
            }

            String[] line;
            while ((line = reader.readNext()) != null) {
                try {
                    // Получаем значения используя индексы из карты
                    String name = getValue(line, headerMap, "name");
                    String lineName = getValue(line, headerMap, "line");

                    if (name.isEmpty() || lineName.isEmpty()) {
                        continue;
                    }

                    MetroStation station = new MetroStation(name, lineName);

                    // Пробуем получить дату
                    String date = getValue(line, headerMap, "date");
                    if (!date.isEmpty()) {
                        station.setDate(formatDate(date));
                    }

                    // Пробуем получить глубину
                    String depth = getValue(line, headerMap, "depth");
                    if (!depth.isEmpty()) {
                        try {
                            station.setDepth(Double.parseDouble(depth));
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid depth value for station " + name + ": " + depth);
                        }
                    }

                    stations.add(station);

                } catch (Exception e) {
                    System.out.println("Error parsing line: " + Arrays.toString(line));
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }

        return stations;
    }

    private String getValue(String[] line, Map<String, Integer> headerMap, String columnName) {
        Integer index = headerMap.get(columnName.toLowerCase());
        if (index != null && index < line.length) {
            return line[index].trim();
        }
        return "";
    }

    private String formatDate(String date) {
        try {
            SimpleDateFormat[] formats = {
                    new SimpleDateFormat("dd.MM.yyyy"),
                    new SimpleDateFormat("yyyy-MM-dd"),
                    new SimpleDateFormat("MM/dd/yyyy")
            };

            Date parsedDate = null;
            for (SimpleDateFormat format : formats) {
                try {
                    parsedDate = format.parse(date);
                    break;
                } catch (ParseException ignored) {}
            }

            if (parsedDate != null) {
                return new SimpleDateFormat("dd.MM.yyyy").format(parsedDate);
            }
        } catch (Exception e) {
            System.out.println("Error parsing date: " + date);
        }
        return date;
    }
}