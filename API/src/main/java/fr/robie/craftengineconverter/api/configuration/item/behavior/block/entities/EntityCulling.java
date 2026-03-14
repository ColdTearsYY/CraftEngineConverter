package fr.robie.craftengineconverter.api.configuration.item.behavior.block.entities;

import java.util.LinkedHashMap;
import java.util.Map;

public class EntityCulling {
    public Boolean enabled;
    public String aabb;
    public Integer viewDistance;
    public Boolean rayTracing;

    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        if (enabled != null) map.put("enabled", enabled);
        if (aabb != null) map.put("aabb", aabb);
        if (viewDistance != null) map.put("view-distance", viewDistance);
        if (rayTracing != null) map.put("ray-tracing", rayTracing);
        return map;
    }
}
