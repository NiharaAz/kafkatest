package Database.model;

import jakarta.persistence.*;

@Entity
@Table(name="ITINERARY")

public class itinerary {
    @Id
    String ID;
    String TD_NO;
    String NAT_CD;
    String BIRTH_DATE;
    String TRANSALATED_ID;
    String MESSAGE_DATETIME;
    String AUDIT_DATETIME;
}
