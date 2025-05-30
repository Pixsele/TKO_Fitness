package tko.database.entity.user;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "weight_tracker")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeightTrackerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double weight;

    @Column(name = "time_date")
    private LocalDateTime timeDate;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private UsersEntity user;
}
