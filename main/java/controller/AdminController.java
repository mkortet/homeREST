package controller;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import model.Device;
import model.Person;
import model.PersonRepository;
import model.LightDevice;
import model.LightRepository;
import model.StatusMsg;
import util.JWTUtil;


@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	PersonRepository pr;
	
	@Autowired
	LightRepository rgbRep;
	
	@RequestMapping("/user/getUsers")
	public @ResponseBody Iterable<Person> getAll() {
		return pr.findAll();
	}
	
	@RequestMapping("/user/getUser/{id}")
	public @ResponseBody Optional<Person> getPerson(@PathVariable("id") String id) {
		return pr.findById(Integer.valueOf(id));
	}
	
	@RequestMapping(value = "/user/register", method = RequestMethod.POST)
	public @ResponseBody StatusMsg addPerson(@RequestBody Person p) {
		boolean match;
		
		match = p.getUserName() != null && p.getUserName().equals("admin");
		match &= p.getAdmin() != null && p.getAdmin().equals("true");
		
		if (match) {
			pr.save(p);
			return StatusMsg.ok();
		}
		
		return StatusMsg.nok("Bad parameters; username or admin not valid");
	}
	
	@RequestMapping(value = "/user/changePwd", method = RequestMethod.POST)
	public @ResponseBody StatusMsg changePwd(@RequestBody Person p) {
		
		Person person = pr.findByUserName(p.getUserName());
		
		if (person != null && p.getPassWord() != null && !p.getPassWord().isEmpty())  {
			person.setPassWord(p.getPassWord());
			pr.save(person);
			return StatusMsg.ok();
		}
		
		return StatusMsg.nok("username not found");
	}
	
	@RequestMapping(value = "/device/register", method = RequestMethod.POST)
	public @ResponseBody StatusMsg addDevice(@RequestBody Device d) {
		boolean match;
		
		match = d.getName() != null && !d.getName().isEmpty(); 
		match &= d.getIpAddr() != null && !d.getIpAddr().isEmpty();
		match &= d.getType() != null && !d.getType().name().isEmpty(); 
		
		if (!match) 
			return StatusMsg.nok("Bad device parameters");
		
		switch(d.getType()) {
		case RGBLIGHT:
			LightDevice r = new LightDevice(d);
			rgbRep.save(r);
			break;
		default:
			return StatusMsg.nok("Bad type parameter");
		}
		
		return StatusMsg.ok();
	}
	
	@RequestMapping(value = "/device/getDevices", method = RequestMethod.GET)
	public @ResponseBody Iterable<Device> getDevices() {
		ArrayList<Device> allDevices = new ArrayList<>();
		Iterable<LightDevice> rgbLights = rgbRep.findAll();
		
		for (LightDevice r : rgbLights) 
			allDevices.add(r.castToDevice());
	
		return allDevices;
	}
	
	@RequestMapping(value = "/device/removeDevice", method = RequestMethod.DELETE)
	public @ResponseBody StatusMsg removeDevice(@RequestBody Device d) {
		
		LightDevice rgbDevice = rgbRep.findLightByName(d.getName());
		
		if (rgbDevice != null) {
			rgbRep.delete(rgbDevice);
			return StatusMsg.ok();
		}
		
		return StatusMsg.nok("Device not found");
	}	
	
}