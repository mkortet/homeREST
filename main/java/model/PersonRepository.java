package model;

import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Integer>{
	
	public Person findByUserName(String userName);
}
