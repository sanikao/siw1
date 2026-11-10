import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Wedding extends Event implements Invitable, Decorable, Entertainable {
    private static final double FLORAL_COST_PER_GUEST = 12.5;

    private String coupleNames;
    private boolean liveBand;
    // Composition: arches are created by the wedding itself (1 wedding : N arches).
    private final List<FloralArch> arches = new ArrayList<>();

    public Wedding(String name, LocalDate date, int guestCount, double budget,
                   String coupleNames, boolean liveBand) {
        super(name, date, guestCount, budget);
        setCoupleNames(coupleNames);
        this.liveBand = liveBand;
    }

    public Wedding(String id, String name, LocalDate date, int guestCount, double budget,
                   String coupleNames, boolean liveBand) {
        super(id, name, date, guestCount, budget);
        setCoupleNames(coupleNames);
        this.liveBand = liveBand;
    }

    public String getCoupleNames() { return coupleNames; }
    public boolean hasLiveBand() { return liveBand; }
    public void setLiveBand(boolean liveBand) { this.liveBand = liveBand; }

    public void setCoupleNames(String coupleNames) {
        if (coupleNames == null || coupleNames.trim().isEmpty())
            throw new IllegalArgumentException("Couple names cannot be empty.");
        this.coupleNames = coupleNames;
    }

    public void addArch(String style, int flowerCount) {
        arches.add(new FloralArch(style, flowerCount));
    }

    public List<FloralArch> getArches() {
        return Collections.unmodifiableList(arches);
    }

    @Override
    public double calculateDecorationCost() {
        double archesCost = 0;
        for (FloralArch arch : arches) archesCost += arch.calculateCost();
        return getGuestCount() * FLORAL_COST_PER_GUEST + archesCost;
    }

    @Override
    public void decorate() {
        System.out.println("Decorating " + getName() + " with " + arches.size() + " floral arch(es)");
    }

    @Override
    public void sendInvitations() {
        System.out.println("Sending " + getGuestCount() + " invitations for " + coupleNames);
    }

    @Override
    public void startEntertainment() {
        liveBand = true;
        System.out.println("Live band plays at " + getName());
    }

    @Override
    public int getEntertainmentHours() {
        return liveBand ? 5 : 2;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Type: Wedding | Couple: " + coupleNames
                + " | Live band: " + (liveBand ? "yes" : "no")
                + " | Arches: " + arches.size());
    }
}
