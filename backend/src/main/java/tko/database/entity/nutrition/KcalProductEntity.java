package tko.database.entity.nutrition;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tko.utils.MealType;

import java.time.LocalDate;

@Entity
@Table(name = "kcal_product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KcalProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "kcal_tracker_id")
    private KcalTrackerEntity kcalTracker;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    private Integer count;

    @Enumerated(EnumType.STRING)
    private MealType typeMeal;
    private LocalDate date;
}
