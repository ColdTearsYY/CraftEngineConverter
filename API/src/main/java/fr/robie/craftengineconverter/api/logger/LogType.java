package fr.robie.craftengineconverter.api.logger;

public enum LogType {
    ERROR("§c"),
    INFO("§7"),
    WARNING("§6"),
    SUCCESS("§2");

    private final String color;

    LogType(String color) {
        this.color = color;
    }

    public String getColor() {
        return this.color;
    }
}
