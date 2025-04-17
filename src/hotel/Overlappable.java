package hotel;

import java.time.LocalDate;

public interface Overlappable {
    boolean isOverlapping(LocalDate checkInDate, LocalDate checkOutDate);
}
