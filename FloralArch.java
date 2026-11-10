import java.util.Objects;
import java.util.UUID;

// Composition: a FloralArch is created inside a Wedding and does not live without it.
public class FloralArch {
    private static final double COST_PER_FLOWER = 1.5;

    private final String id = UUID.randomUUID().toString();
    private String style;
    private int flowerCount;

    FloralArch(String style, int flowerCount) {
        setStyle(style);
        setFlowerCount(flowerCount);
    }

    public String getStyle() { return style; }
    public int getFlowerCount() { return flowerCount; }

    public void setStyle(String style) {
        if (style == null || style.trim().isEmpty())
            throw new IllegalArgumentException("Arch style cannot be empty.");
        this.style = style;
    }

    public void setFlowerCount(int flowerCount) {
        if (flowerCount <= 0)
            throw new IllegalArgumentException("Flower count must be positive.");
        this.flowerCount = flowerCount;
    }

    public double calculateCost() {
        return flowerCount * COST_PER_FLOWER;
    }

    @Override
    public String toString() {
        return "FloralArch: " + style + " | Flowers: " + flowerCount
                + " | Cost: $" + calculateCost();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        return id.equals(((FloralArch) object).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
