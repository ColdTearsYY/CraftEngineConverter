package fr.robie.craftengineconverter.common.utils;

import net.momirealms.craftengine.core.util.Key;
import org.jetbrains.annotations.Nullable;

public class CecAttributeModifier {
    private final String type;
    private final net.momirealms.craftengine.core.attribute.AttributeModifier.Slot slot;
    private final Key id;
    private final double amount;
    private final net.momirealms.craftengine.core.attribute.AttributeModifier.Operation operation;
    @Nullable
    private final Display display;

    public CecAttributeModifier(String type, net.momirealms.craftengine.core.attribute.AttributeModifier.Slot slot, Key id, double amount, net.momirealms.craftengine.core.attribute.AttributeModifier.Operation operation, @Nullable Display display) {
        this.amount = amount;
        this.display = display;
        this.id = id;
        this.operation = operation;
        this.slot = slot;
        this.type = type;
    }

    public double amount() {
        return amount;
    }

    public @Nullable Display display() {
        return display;
    }

    public Key id() {
        return id;
    }

    public net.momirealms.craftengine.core.attribute.AttributeModifier.Operation operation() {
        return operation;
    }

    public net.momirealms.craftengine.core.attribute.AttributeModifier.Slot slot() {
        return slot;
    }

    public String type() {
        return type;
    }

    public record Display(net.momirealms.craftengine.core.attribute.AttributeModifier.Display.Type type, String value) {
    }
}
