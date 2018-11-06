package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import model.Device;
import model.LightDevice;
import model.LightRepository;
import model.StatusMsg;
import util.HttpsJsonPowerOnOff;

@RestController
@RequestMapping("/lights")
public class LightController {
	
	@Autowired
	LightRepository lightRep;
	
	@RequestMapping("/all")
	public @ResponseBody Iterable<LightDevice>getAll() {return lightRep.findAll();}
	
	@RequestMapping(value = "/poweronoff", method = RequestMethod.POST)
	public @ResponseBody StatusMsg powerOnOff(@RequestBody Device d) {
		String ret = "";
		LightDevice r = null;
		
		if (d.getName() != null) 
			r = lightRep.findLightByName(d.getName());
		else 
			return StatusMsg.nok("Bad JSON parameters");
		
		if (r != null) {
		
			try {
				ret = new HttpsJsonPowerOnOff(r.getIpAddr(), d.getPower()).connect();
				
				if (ret.contains("OK"))
					return StatusMsg.ok();
				
			}catch(Exception m) {
				return StatusMsg.nok("Device returned: " + ret + ". Exception: " + m.getMessage());
			}
		}
		else 
			return StatusMsg.nok("Device not registered");
		
		return StatusMsg.nok("Error -> @HttpsJsonPowerOff. Returned: " + ret);
	}
}
