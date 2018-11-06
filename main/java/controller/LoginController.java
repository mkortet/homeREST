package controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import model.Device;
import model.Person;
import model.PersonRepository;
import model.LightRepository;
import model.StatusMsg;
import util.JWTUtil;

@RestController
@RequestMapping("/login")
public class LoginController {
	
	@Autowired
	PersonRepository pr;
	
	@Autowired
	LightRepository rgbRep;
	
	@RequestMapping("/init")
	public @ResponseBody StatusMsg initUser() {
		
		if (pr.count() == 0) {
			Person p = new Person();
			p.setAdmin("true");
			p.setUserName("admin");
			p.setPassWord("admin");
			pr.save(p);
			return StatusMsg.ok();
		}
				
		return StatusMsg.nok("Admin user already created");
	}
	
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public @ResponseBody StatusMsg login(@RequestBody Person p) {
		Person person;
		boolean match;
		String token;
		
		person = pr.findByUserName(p.getUserName());
		
		match = p.getUserName() != null;
		match &= p.getAdmin() != null && person != null && person.getPassWord() != null;
		match &= p.getPassWord().equals(person.getPassWord());
		
		if (match) {
			token = new JWTUtil(person).createToken();
			return StatusMsg.ok(token);
		}
		
		return StatusMsg.nok("Wrong person parameters");
	}
	
	@RequestMapping(value = "/device", method = RequestMethod.POST)
	public @ResponseBody StatusMsg login(@RequestBody Device d) {
		boolean match;
		
		match = d.getIpAddr() != null && d.getName() != null;
		match &= d.getType() != null && isRegistered(d);
		
		if (match) {
			return StatusMsg.ok(new JWTUtil(d).createToken());
		}
		else {
			return StatusMsg.nok("Wrong device parameters or device not registered");
		}		
		
	}
	
	private boolean isRegistered(Device d) {
		
		switch (d.getType()) {
		case RGBLIGHT:
			return rgbRep.findLightByName(d.getName()) != null ? true : false;
		default:
			return false;
		}
	}
	
	public PersonRepository getRepo() {return this.pr;}
}
