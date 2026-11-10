import java.time.LocalDate;

public class Conference extends Event implements Invitable {
    private String topic;
    private int speakerCount;

    public Conference(String name, LocalDate date, int guestCount, double budget,
                      String topic, int speakerCount) {
        super(name, date, guestCount, budget);
        setTopic(topic);
        setSpeakerCount(speakerCount);
    }

    public Conference(String id, String name, LocalDate date, int guestCount, double budget,
                      String topic, int speakerCount) {
        super(id, name, date, guestCount, budget);
        setTopic(topic);
        setSpeakerCount(speakerCount);
    }

    public String getTopic() { return topic; }
    public int getSpeakerCount() { return speakerCount; }

    public void setTopic(String topic) {
        if (topic == null || topic.trim().isEmpty())
            throw new IllegalArgumentException("Topic cannot be empty.");
        this.topic = topic;
    }

    public void setSpeakerCount(int speakerCount) {
        if (speakerCount < 0)
            throw new IllegalArgumentException("Speaker count cannot be negative.");
        this.speakerCount = speakerCount;
    }

    public double calculateTicketPrice() {
        return (getBudget() / getGuestCount()) * 1.2;
    }

    public void addSpeaker() {
        speakerCount++;
        System.out.println("Speaker added. Total speakers: " + speakerCount);
    }

    public void printSchedule() {
        System.out.println(speakerCount + " talks on \"" + topic + "\"");
    }

    @Override
    public void sendInvitations() {
        System.out.println("Sending " + getGuestCount() + " conference passes for " + topic);
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Type: Conference | Topic: " + topic + " | Speakers: " + speakerCount);
    }
}
