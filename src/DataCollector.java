import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.util.*;

public class DataCollector {
    private final List<MetroLine> lines = new ArrayList<>();
    private final List<MetroStation> stations = new ArrayList<>();

    public void addLines(List<MetroLine> newLines) {
        lines.addAll(newLines);
    }

    public void addStations(List<MetroStation> newStations) {
        for (MetroStation newStation : newStations) {
            boolean found = false;

            // Ищем существующую станцию
            for (MetroStation existingStation : stations) {
                if (existingStation.getName().equals(newStation.getName())
                        && existingStation.getLineNumber().equals(newStation.getLineNumber())) {
                    // Обновляем существующую станцию
                    if (newStation.getDate() != null) {
                        existingStation.setDate(newStation.getDate());
                    }
                    if (newStation.getDepth() != null) {
                        existingStation.setDepth(newStation.getDepth());
                    }
                    if (newStation.getHasConnection() != null) {
                        existingStation.setHasConnection(newStation.getHasConnection());
                    }
                    found = true;
                    break;
                }
            }

            // Если станция не найдена, добавляем новую
            if (!found) {
                stations.add(newStation);
            }
        }
    }

    public void saveToFiles(String mapPath, String stationsPath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            // Создаем map.json
            Map<String, Object> mapData = new HashMap<>();
            mapData.put("lines", lines);

            // Группируем станции по линиям
            Map<String, List<String>> stationsByLine = new HashMap<>();
            for (MetroStation station : stations) {
                String lineName = station.getLineName();
                if (!stationsByLine.containsKey(lineName)) {
                    stationsByLine.put(lineName, new ArrayList<>());
                }
                stationsByLine.get(lineName).add(station.getName());
            }
            mapData.put("stations", stationsByLine);

            // Создаем stations.json
            List<Map<String, Object>> stationsList = new ArrayList<>();
            for (MetroStation station : stations) {
                Map<String, Object> stationMap = new HashMap<>();
                stationMap.put("name", station.getName());
                stationMap.put("line", station.getLineName());

                if (station.getDate() != null) {
                    stationMap.put("date", station.getDate());
                }
                if (station.getDepth() != null) {
                    stationMap.put("depth", station.getDepth());
                }
                if (station.getHasConnection() != null) {
                    stationMap.put("hasConnection", station.getHasConnection());
                }

                stationsList.add(stationMap);
            }

            Map<String, Object> stationsJson = new HashMap<>();
            stationsJson.put("stations", stationsList);

            // Сохраняем файлы
            mapper.writeValue(new File(mapPath), mapData);
            mapper.writeValue(new File(stationsPath), stationsJson);

            System.out.println("Data successfully saved to " + mapPath + " and " + stationsPath);

        } catch (Exception e) {
            System.out.println("Error saving data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}