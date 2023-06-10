package pl.ambsoft.birthdaybot.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.ambsoft.birthdaybot.entity.Pracownik;


@Repository
public interface PracownikRepository extends CrudRepository<Pracownik, Long> {
}