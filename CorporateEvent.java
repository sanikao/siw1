import java.time.LocalDate;

public class CorporateEvent extends Event {
    private String companyName;

    public CorporateEvent(String name, LocalDate date, int guestCount, double budget,
                          String companyName) {
        super(name, date, guestCount, budget);
        setCompanyName(companyName);
    }

    public CorporateEvent(String id, String name, LocalDate date, int guestCount, double budget,
                          String companyName) {
        super(id, name, date, guestCount, budget);
        setCompanyName(companyName);
    }

    public String getCompanyName() { return companyName; }

    public void setCompanyName(String companyName) {
        if (companyName == null || companyName.trim().isEmpty())
            throw new IllegalArgumentException("Company name cannot be empty.");
        this.companyName = companyName;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Type: Corporate | Company: " + companyName);
    }
}
