import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JsonParser {
    private final ObjectMapper mapper = new ObjectMapper();

    public List<MetroStation> parse(String filePath) {
        List<MetroStation> stations = new ArrayList<>();

        try {
            JsonNode root = mapper.readTree(new File(filePath));
            JsonNode stationsNode = root.get("stations");

            if (stationsNode != null && stationsNode.isArray()) {
                for (JsonNode stationNode : stationsNode) {
                    try {
                        String name = getTextValue(stationNode, "name");
                        String line = getTextValue(stationNode, "line");

                        if (name.isEmpty() || line.isEmpty()) {
                            continue;
                        }

                        MetroStation station = new MetroStation(name, line);

                        // Добавляем дату если есть
                        String date = getTextValue(stationNode, "date");
                        if (!date.isEmpty()) {
                            station.setDate(date);
                        }

                        // Добавляем глубину если есть
                        JsonNode depthNode = stationNode.get("depth");
                        if (depthNode != null && !depthNode.isNull()) {
                            try {
                                station.setDepth(depthNode.asDouble());
                            } catch (Exception e) {
                                System.out.println("Invalid depth value for station " + name);
                            }
                        }

                        // Добавляем информацию о пересадках если есть
                        JsonNode connectionNode = stationNode.get("hasConnection");
                        if (connectionNode != null && !connectionNode.isNull()) {
                            station.setHasConnection(connectionNode.asBoolean());
                        }

                        stations.add(station);

                    } catch (Exception e) {
                        System.out.println("Error parsing station: " + stationNode);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error parsing JSON file " + filePath + ": " + e.getMessage());
        }

        return stations;
    }

    private String getTextValue(JsonNode node, String fieldName) {
        JsonNode valueNode = node.get(fieldName);
        return valueNode != null && !valueNode.isNull() ? valueNode.asText().trim() : "";
    }
}