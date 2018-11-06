package model;

import org.springframework.data.repository.CrudRepository;

public interface LightRepository extends CrudRepository<LightDevice, Long> {
	
	public LightDevice findLightByName(String name);
}
