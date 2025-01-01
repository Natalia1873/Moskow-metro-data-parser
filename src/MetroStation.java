import java.util.Objects;

public class MetroStation {
    private String name;
    private String lineNumber;
    private String lineName;
    private String date;
    private Double depth;
    private Boolean hasConnection;

    // Конструктор с номером линии (для парсинга веб-страницы)
    public MetroStation(String name, String lineNumber) {
        this.name = name;
        this.lineNumber = lineNumber;
    }

    // Геттеры и сеттеры
    public String getName() { return name; }
    public String getLineNumber() { return lineNumber; }
    public String getLineName() {
        return this.lineName != null ? this.lineName : this.lineNumber;
    }
    public String getDate() { return date; }
    public Double getDepth() { return depth; }
    public Boolean getHasConnection() { return hasConnection; }

    public void setLineName(String lineName) { this.lineName = lineName; }
    public void setDate(String date) { this.date = date; }
    public void setDepth(Double depth) { this.depth = depth; }
    public void setHasConnection(Boolean hasConnection) { this.hasConnection = hasConnection; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetroStation station = (MetroStation) o;
        return Objects.equals(name, station.name) &&
                Objects.equals(lineNumber, station.lineNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lineNumber);
    }

    @Override
    public String toString() {
        return "Station '" + name + "' (Line " + (lineName != null ? lineName : lineNumber) + ")";
    }
}