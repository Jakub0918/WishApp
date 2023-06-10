package pl.ambsoft.birthdaybot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Administrator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long pracownik_id;
    private boolean add_pracownik = true;
    private boolean edit_pracownik = true;
    private boolean delete_pracownik = true;
    private boolean add_zyczenie = true;
    private boolean edit_zyczenie = true;
    private boolean delete_zyczenie = true;
    private boolean add_przypomnienie = true;
    private boolean edit_przypomnienie = true;
    private boolean delete_przypomnienie = true;
}
