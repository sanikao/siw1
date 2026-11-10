import java.time.LocalDate;

public class BirthdayParty extends Event implements Invitable, Decorable, Entertainable {
    private static final double GIFT_BAG_PRICE = 4.0;
    private static final double DECORATION_PER_GUEST = 2.0;

    private String celebrantName;
    private int age;

    public BirthdayParty(String name, LocalDate date, int guestCount, double budget,
                         String celebrantName, int age) {
        super(name, date, guestCount, budget);
        setCelebrantName(celebrantName);
        setAge(age);
    }

    public BirthdayParty(String id, String name, LocalDate date, int guestCount, double budget,
                         String celebrantName, int age) {
        super(id, name, date, guestCount, budget);
        setCelebrantName(celebrantName);
        setAge(age);
    }

    public String getCelebrantName() { return celebrantName; }
    public int getAge() { return age; }

    public void setCelebrantName(String celebrantName) {
        if (celebrantName == null || celebrantName.trim().isEmpty())
            throw new IllegalArgumentException("Celebrant name cannot be empty.");
        this.celebrantName = celebrantName;
    }

    public void setAge(int age) {
        if (age <= 0 || age > 120)
            throw new IllegalArgumentException("Age must be between 1 and 120.");
        this.age = age;
    }

    public void orderCake() {
        System.out.println("Ordering a cake for " + getGuestCount() + " guests and " + celebrantName);
    }

    public double calculateGiftBagCost() {
        return getGuestCount() * GIFT_BAG_PRICE;
    }

    @Override
    public double calculateDecorationCost() {
        return getGuestCount() * DECORATION_PER_GUEST;
    }

    @Override
    public void decorate() {
        System.out.println("Hanging balloons for " + celebrantName + "'s party");
    }

    @Override
    public void sendInvitations() {
        System.out.println("Sending " + getGuestCount() + " invitations to " + celebrantName + "'s party");
    }

    @Override
    public void startEntertainment() {
        System.out.println("Starting party games for " + celebrantName + "'s birthday!");
    }

    @Override
    public int getEntertainmentHours() {
        return 3;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Type: Birthday | Celebrant: " + celebrantName + " | Turning: " + age);
    }
}
