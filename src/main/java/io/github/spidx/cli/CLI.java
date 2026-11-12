package io.github.spidx.cli;

import io.github.spidx.exception.EntityNotFoundException;
import io.github.spidx.model.Citizen;
import io.github.spidx.model.Request;
import io.github.spidx.model.Status;
import io.github.spidx.service.CitizenService;
import io.github.spidx.service.RequestService;
import io.github.spidx.util.ExcelExporter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/*
 * CLI
 */
public class CLI {
    public static void Run() {
        int op;

        do {
            PrintMenu();
            op = ReadOption("Enter an option: ", 0, 12);

            switch (op) {
                case 1:
                    PrintAllCitizens();
                    break;
                case 2:
                    OpenRequestsMenu();
                    break;
                case 3:
                    OpenFindMenu();
                    break;
                case 4:
                    OpenFilterMenu();
                    break;
                case 5:
                    PrintStatistic();
                case 6:
                    try {
                        ExcelExporter.Export(
                                Path.of("exports", "cadastral_data.xlsx")
                        );

                        IO.println("Data successfully exported!");
                    } catch (IOException e) {
                        IO.println("Failed to export data: " + e.getMessage());
                    }
                    break;
                default:
                    IO.println("");
            }
        } while (op != 0);
    }

    public static void PrintMenu() {
        IO.print(
                """
                        ======================================== Cadastral Service System ========================================
                        
                        1. Citizens
                        2. Requests
                        3. Find
                        4. Filter
                        5. Statistic
                        6. Export Data
                        7. Print Database Tables
                        0. Exit
                        """);
    }

    private static void OpenRequestsMenu() {
        while (true) {
            IO.println("""

                ==================== Requests ====================

                1. List all requests
                2. Create request
                3. Find request by ID
                4. Update request
                5. Delete request
                0. Return to main menu
                """);

            switch (ReadOption("Enter an option: ", 0, 5)) {
                case 0 -> {
                    return;
                }
                case 1 -> PrintAllRequests();
                case 2 -> CreateRequest();
                case 3 -> FindRequestById();
                case 4 -> UpdateRequest();
                case 5 -> DeleteRequest();
            }
        }
    }

    private static void CreateRequest() {
        IO.println("\n=== Create Request ===");

        UUID citizenId = ReadUUID("Enter citizen ID: ");

        // Make sure the citizen exists before creating the request.
        try {
            CitizenService.GetById(citizenId);
        } catch (EntityNotFoundException e) {
            IO.println("Unable to find citizen with this id");
            return;
        }


        Status status = ReadStatus(
                "Enter status (NEW, IN_PROGRESS, DONE, CANCELLED): "
        );

        OffsetDateTime openAt = ReadDateTime("Enter opening date: ");
        OffsetDateTime closedAt = ReadOptionalDateTime(
                "Enter closing date (leave empty if not closed): "
        );

        Request request = new Request(
                UUID.randomUUID(),
                citizenId,
                status,
                openAt,
                closedAt
        );

        RequestService.Add(request);

        IO.println("Request successfully created!");
        Print(request);
    }

    private static void UpdateRequest() {
        IO.println("\n=== Update Request ===");

        UUID id = ReadUUID("Enter request ID: ");

        // Check that the request exists.
        RequestService.GetById(id);

        UUID citizenId = ReadUUID("Enter new citizen ID: ");
        CitizenService.GetById(citizenId);

        Status status = ReadStatus(
                "Enter new status (NEW, IN_PROGRESS, DONE, CANCELLED): "
        );

        OffsetDateTime openAt = ReadDateTime("Enter new opening date: ");
        OffsetDateTime closedAt = ReadOptionalDateTime(
                "Enter new closing date (leave empty if not closed): "
        );

        Request updatedRequest = new Request(
                id,
                citizenId,
                status,
                openAt,
                closedAt
        );

        RequestService.Update(updatedRequest);

        IO.println("Request successfully updated!");
        Print(RequestService.GetById(id));
    }

    private static void DeleteRequest() {
        IO.println("\n=== Delete Request ===");

        UUID id = ReadUUID("Enter request ID: ");

        Request request = RequestService.GetById(id);
        Print(request);

        String confirmation = IO.readln(
                "Are you sure you want to delete this request? (y/N): "
        );

        if (!"y".equalsIgnoreCase(confirmation.trim())) {
            IO.println("Deletion cancelled.");
            return;
        }

        RequestService.Delete(id);

        IO.println("Request successfully deleted!");
    }

    private static OffsetDateTime ReadOptionalDateTime(String msg) {
        while (true) {
            String line = IO.readln(msg);

            if (line == null || line.isBlank()) {
                return null;
            }

            try {
                return OffsetDateTime.parse(line.trim());
            } catch (DateTimeParseException e) {
                IO.println(
                        "Invalid date format. Expected ISO 8601: " +
                                "2026-11-12T16:30:00+01:00"
                );
            }
        }
    }

    private static void PrintStatistic() {
        List<Citizen> citizens = CitizenService.GetAll();
        List<Request> requests = RequestService.GetAll();

        var newRequests = requests
                .stream()
                .filter(item -> Objects.equals(item.GetStatus(), Status.NEW))
                .count();

        var activeRequests = requests
                .stream()
                .filter(item -> Objects.equals(item.GetStatus(), Status.IN_PROGRESS))
                .count();

        var doneRequests = requests
                .stream()
                .filter(item -> Objects.equals(item.GetStatus(), Status.DONE))
                .count();

        var completedRequests = requests
                .stream()
                .filter(item -> Objects.equals(item.GetStatus(), Status.CANCELLED))
                .count();

        String statistics = String.format("""
                Total Number of Citizens in the System: %d
                Total Number of the Requests:           %d
                Number of New Requests:                 %d
                Number of Active Requests:              %d
                Number of Completed Requests:           %d
                Number of Cancelled Requests:           %d
                """, citizens.size(), requests.size(), newRequests, activeRequests, doneRequests, completedRequests);

        IO.println(statistics);
    }

    static void Print(@NotNull Citizen citizen) {
        IO.println("====");
        IO.println("Id              : " + citizen.GetId());
        IO.println("Fullname        : " + citizen.GetFullName());
        IO.println("Date of birth   : " + citizen.getDateOfBirth());
        IO.println("Passport Series : " + citizen.GetPassportSeries());
        IO.println("Passport Number : " + citizen.GetPassportNumber());
        IO.println("====");
    }

    static void Print(@NotNull Request request) {
        IO.println("====");
        IO.println("Id              : " + request.GetId());
        IO.println("Citizen Id      : " + request.GetCitizenId());
        IO.println("Status          : " + request.GetStatus().name());
        IO.println("Open at         : " + request.GetOpenAt());
        if (request.GetClosedAt() != null) {
            IO.println("Closed at       :" + request.GetClosedAt());
        } else {
            IO.println("Closed at       : still in progress");
        }
        IO.println("====");
    }

    static void PrintAllCitizens() {
        for (var citizen : CitizenService.GetAll())
            Print(citizen);
    }

    static void PrintAllRequests() {
        for (var request : RequestService.GetAll()) {
            Print(request);
        }
    }

    static void OpenFindMenu() {
        IO.println("\n1. Find citizen\n2. Find request\n0. Return to the main menu");
        int choose = ReadOption("Enter an option: ", 0, 2);

        if (choose == 1) {
            IO.println("\n1. Find citizen by ID\n2. Find citizen by  Full Name\n0. Return to the main menu");
            switch (ReadOption("Enter an option:", 0, 2)) {
                case 0 -> Quit();
                case 1 -> FindCitizenById();
                case 2 -> FindCitizenByName();
                default -> throw new IllegalStateException("Unexpected value");
            }
        } else if (choose == 2) {
            IO.println("\n1. Find request by ID\n2. Find request by citizen's by ID\n0. Return to the main menu");
            switch (ReadOption("Enter an option:", 0, 2)) {
                case 0 -> Quit();
                case 1 -> FindRequestById();
                case 2 -> FindRequestsByCitizensId();
                default -> throw new IllegalStateException("Unexpected value");
            }
        }
    }

    static void FindCitizenById() {
        UUID id = ReadUUID("Enter citizen's ID(UUID): ");
        Print(CitizenService.GetById(id));
    }

    static void FindCitizenByName() {
        List<Citizen> citizens = CitizenService.GetAll();

        var line = IO.readln("Enter FullName: ");

        var res = citizens
                .stream()
                .filter(item -> Objects.equals(item.GetFullName(), line))
                .toList();

        for (var citizen : res) {
            Print(citizen);
        }
    }

    static void FindRequestById() {
        UUID id = ReadUUID("Enter request ID (UUID): ");
        Print(RequestService.GetById(id));
    }

    static void FindRequestsByCitizensId() {
        List<Request> requests = RequestService.GetAll();

        UUID id = ReadUUID("Enter citizen's ID(UUID): ");

        var res = requests
                .stream()
                .filter(item -> Objects.equals(item.GetCitizenId(), id))
                .toList();

        for (var request : res) {
            Print(request);
        }
    }

    static void OpenFilterMenu() {
        IO.println("\n1. Filter citizens\n2. Filter requests\n0. Return to the main menu");
        int choose = ReadOption("Enter an option: ", 0, 2);

        if (choose == 1) {
            IO.println("\n1. Filter citizens by ID\n \n0. Return to the main menu");
            switch (ReadOption("Enter an option:", 0, 1)) {
                case 0 -> Quit();
                case 1 -> FilterCitizensByDateOfBirth();
                default -> throw new IllegalStateException("Unexpected value");
            }
        } else if (choose == 2) {
            IO.println("\n1. Filter requests by Status\n2. Filter requests by Date\n0. Return to the main menu");
            switch (ReadOption("Enter an option:", 0, 1)) {
                case 0 -> Quit();
                case 1 -> FilterRequestsByStatus();
                case 2 -> FilterRequestsByOpenedAt();
                case 3 -> FilterRequestsByCitizenId();
                default -> throw new IllegalStateException("Unexpected value");
            }
        }
    }

    private static void FilterCitizensByDateOfBirth() {
        List<Citizen> citizens = CitizenService.GetAll();

        var start = ReadDate("Enter start date of the range: ");
        var end = ReadDate("Enter end date of the range: ");

        var res = citizens.stream()
                .filter(item -> {
                    LocalDate openedAt = item.getDateOfBirth();

                    return !openedAt.isBefore(start)
                            && !openedAt.isAfter(end);
                })
                .toList();

        for (var request : res) {
            Print(request);
        }
    }

    private static void FilterRequestsByCitizenId() {
        List<Request> requests = RequestService.GetAll();

        UUID id = ReadUUID("Enter citizen's ID(UUID): ");

        var res = requests
                .stream()
                .filter(item -> Objects.equals(item.GetCitizenId(), id))
                .toList();

        for (var request : res) {
            Print(request);
        }
    }

    private static void FilterRequestsByOpenedAt() {
        List<Request> requests = RequestService.GetAll();

        var start = ReadDateTime("Enter start date of the range: ");
        var end = ReadDateTime("Enter end date of the range: ");

        var res = requests.stream()
                .filter(request -> {
                    OffsetDateTime openedAt = request.GetOpenAt();

                    return !openedAt.isBefore(start)
                            && !openedAt.isAfter(end);
                })
                .toList();

        for (var request : res) {
            Print(request);
        }
    }

    private static void FilterRequestsByStatus() {
        List<Request> requests = RequestService.GetAll();

        Status status = ReadStatus("Enter status (new, in_progress, done, canceled)");

        var res = requests
                .stream()
                .filter(item -> Objects.equals(item.GetStatus(), status))
                .toList();

        for (var request : res) {
            Print(request);
        }
    }

    static UUID ReadUUID(String msg) {
        while (true) {
            String line = IO.readln(msg);

            if (line == null) {
                throw new IllegalStateException("End of input reached.");
            }

            try {
                return UUID.fromString(line.trim());
            } catch (IllegalArgumentException e) {
                IO.println("Invalid UUID format. Expected format: 123e4567-e89b-12d3-a456-426614174000");
            }
        }
    }

    static LocalDate ReadDate(String msg) {
        while (true) {
            String line = IO.readln(msg);

            if (line == null) {
                throw new IllegalStateException("End of input reached.");
            }

            try {
                return LocalDate.parse(line.trim());
            } catch (DateTimeParseException e) {
                IO.println("Invalid date format. Expected format: yyyy-MM-dd (e.g. 2026-11-12)");
            }
        }
    }

    static OffsetDateTime ReadDateTime(String msg) {
        while (true) {
            String line = IO.readln(msg);

            if (line == null) {
                throw new IllegalStateException("End of input reached.");
            }

            try {
                return OffsetDateTime.parse(line.trim());
            } catch (DateTimeParseException e) {
                IO.println("Invalid date format. Expected ISO 8601: 2026-11-12T16:30:00+01:00");
            }
        }
    }

    static Status ReadStatus(String msg) {
        while (true) {
            String line = IO.readln(msg);

            try {
                return Status.valueOf(line.trim().toUpperCase());
            } catch (IllegalArgumentException | NullPointerException e) {
                IO.println(
                        "Valid statuses: NEW, IN_PROGRESS, DONE, CANCELLED"
                );
            }
        }
    }

    static int ReadOption(String msg, int lower, int higher) {
        boolean isValid = true;
        while (true) {
            try {
                if (!isValid) {
                    IO.println(String.format("A valid option is in range: [%d;%d]", lower, higher));
                }
                var line = IO.readln(msg);
                int res = Integer.parseInt(line);

                if ((res >= lower) && (res <= higher)) {
                    return res;
                } else {
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                isValid = false;
                IO.println("Invalid Input. Please enter a number.");
            }
        }
    }

    // Does absolutely nothing for switch rule to work properly
    static void Quit() {
        return;
    }
}