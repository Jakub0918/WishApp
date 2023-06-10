package pl.ambsoft.birthdaybot.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.ambsoft.birthdaybot.entity.Administrator;


@Repository
public interface AdministratorRepository extends CrudRepository<Administrator, Long> {
}