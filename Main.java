import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EventService service = new EventService();

        System.out.println(" Creating events ");
        CorporateEvent teamBuilding = new CorporateEvent("Team Building",
                LocalDate.parse("2026-11-05"), 40, 3000, "Design Studio");
        Wedding wedding = new Wedding("Toi Wedding", LocalDate.parse("2026-06-20"),
                120, 15000, "Aruuke & Tilek", false);
        Conference conference = new Conference("Tech Summit", LocalDate.parse("2026-09-12"),
                300, 45000, "Something technological", 8);
        BirthdayParty birthday = new BirthdayParty("Birthday Bash", LocalDate.parse("2026-12-01"),
                25, 1200, "Aisulu", 21);
        Wedding wedding2 = new Wedding("Kyz-Uzatuu Wedding", LocalDate.parse("2027-02-14"),
                60, 9000, "Nurdzhigit & Ayana", true);

        System.out.println("\n Composition: arches inside a wedding ");
        wedding.addArch("Classic", 200);
        wedding.addArch("Boho", 120);
        wedding.getArches().forEach(System.out::println);

        System.out.println("\n Aggregation: venue assigned to events ");
        Venue hall = new Venue("Grand Hall", 150, 2000);
        wedding.assignVenue(hall);
        wedding2.assignVenue(hall);
        System.out.println(hall);
        try {
            conference.assignVenue(hall); // 300 guests > 150
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }

        System.out.println("\n Adding events (Set + Map + Map) ");
        for (Event event : List.of(teamBuilding, wedding, conference, birthday, wedding2)) {
            service.addEvent(event);
        }

        System.out.println("\n Duplicate ID is rejected ");
        try {
            service.addEvent(new CorporateEvent(teamBuilding.getId(), "Copy",
                    LocalDate.parse("2026-11-06"), 10, 100, "Copy Inc"));
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }

        System.out.println("\n Search by ID (O(1)) ");
        service.findById(wedding.getId()).ifPresent(Event::displayInfo);
        System.out.println("Unknown ID found: " + service.findById("no-such-id").isPresent());

        System.out.println("\n Grouping by type ");
        System.out.println("Weddings: " + service.getEventsByType("Wedding").size());
        System.out.println("Unknown type: " + service.getEventsByType("Concert").size());

        System.out.println("\n Events in Grand Hall ");
        service.getEventsByVenue(hall).forEach(e -> System.out.println(e.getName()));

        System.out.println("\n Natural order (date, then ID) ");
        for (Event event : service.getNaturallySortedEvents())
            System.out.println(event.getDate() + " - " + event.getName());

        System.out.println("\n Custom comparators ");
        System.out.println("By name:");
        for (Event event : service.getSortedEvents(EventService.BY_NAME))
            System.out.println("  " + event.getName());
        System.out.println("By budget:");
        for (Event event : service.getSortedEvents(EventService.BY_BUDGET))
            System.out.println("  " + event.getName() + " - $" + event.getBudget());
        System.out.println("By guest count (highest first):");
        for (Event event : service.getSortedEvents(EventService.BY_GUEST_COUNT))
            System.out.println("  " + event.getName() + " - " + event.getGuestCount());

        System.out.println("\n Filter: budget >= 5000 ");
        for (Event event : service.searchByMinimumBudget(5000))
            System.out.println(event.getName() + " - $" + event.getBudget());

        System.out.println("\n Roles + PriorityQueue ");
        EventOrganizer organizer = new EventOrganizer("Sanira", service);
        EventManager manager = new EventManager(service);

        CorporateEvent charity = new CorporateEvent("Charity Event",
                LocalDate.parse("2027-03-10"), 150, 7000, "Kind Hearts");
        Conference workshop = new Conference("Design Workshop",
                LocalDate.parse("2027-03-15"), 35, 1800, "UI design", 2);
        organizer.submitEvent(workshop, 5);
        organizer.submitEvent(charity, 10);   // higher priority, processed first
        organizer.submitEvent(charity, 1);    // same ID again -> rejected by manager
        manager.processAllRequests();
        manager.processNextRequest();         // empty queue

        System.out.println("\n Update (CRUD) ");
        service.updateEvent(teamBuilding.getId(), event -> {
            event.setBudget(3500);
            event.setGuestCount(50);
        });
        service.findById(teamBuilding.getId()).ifPresent(Event::displayInfo);
        System.out.println("Update unknown ID: " + service.updateEvent("no-such-id", e -> { }));

        System.out.println("\n Remove (CRUD) ");
        System.out.println("Removed: " + service.removeEvent(birthday.getId()));
        System.out.println("Removed again: " + service.removeEvent(birthday.getId()));
        System.out.println("Remaining events: " + service.getAllEvents().size());

        System.out.println("\n Analytics ");
        System.out.print(service.buildReport());

        System.out.println("\n Interfaces (polymorphism) ");
        for (Event event : service.getAllEvents()) {
            if (event instanceof Invitable invitable) invitable.sendInvitations();
            if (event instanceof Decorable decorable)
                System.out.println("  decoration cost: $" + decorable.calculateDecorationCost());
            if (event instanceof Entertainable entertainable)
                System.out.println("  entertainment hours: " + entertainable.getEntertainmentHours());
        }

        System.out.println("\n Subclass methods ");
        wedding.startEntertainment();
        wedding.decorate();
        conference.addSpeaker();
        conference.printSchedule();
        System.out.println("Ticket price: $" + conference.calculateTicketPrice());
        birthday.orderCake();
        System.out.println("Gift bags cost: $" + birthday.calculateGiftBagCost());
    }
}
