package fr.robie.craftengineconverter.api.configuration.events;

import java.util.Map;

public interface EventFunction {
    Map<String, Object> serialize();
}
