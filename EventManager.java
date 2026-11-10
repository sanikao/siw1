// Role: takes requests from the queue (poll) and registers events.
public class EventManager {
    private final EventService service;

    public EventManager(EventService service) {
        this.service = service;
    }

    public void processNextRequest() {
        EventRequest request = service.getNextRequest();
        if (request == null) {
            System.out.println("There are no pending requests");
            return;
        }

        System.out.println("Manager is processing \"" + request.getEvent().getName()
                + "\" from " + request.getSubmittedBy());
        try {
            service.addEvent(request.getEvent());
        } catch (IllegalArgumentException e) {
            System.out.println("Request rejected: " + e.getMessage());
        }
    }

    public void processAllRequests() {
        while (service.getQueueSize() > 0) {
            processNextRequest();
        }
    }
}
