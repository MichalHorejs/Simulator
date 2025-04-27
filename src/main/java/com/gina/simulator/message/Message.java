package com.gina.simulator.message;

import com.gina.simulator.enums.Sender;
import com.gina.simulator.incident.Incident;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Message {

    @Id
    private UUID id;

    @ManyToOne
    private Incident incident;

    private String message;

    private Sender sender;

    private LocalDateTime timestamp;

    @PrePersist
    public void ensureId() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}
