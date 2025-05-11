package ru.job4j.devops.models;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entity class representing a calculation event in the system.
 * Stores information about mathematical operations performed by users,
 * including the operands, result, and metadata about the operation.
 *
 * @author Maksim Levin
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "calc_events")
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class CalcEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Double first;

    @Column(nullable = false)
    private Double second;

    @Column(nullable = false)
    private Double result;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @Column(nullable = false)
    private String type;
} 