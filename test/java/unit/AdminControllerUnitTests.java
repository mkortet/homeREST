package unit;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hibernate.engine.spi.PersistenceContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import controller.AdminController;
import model.Device;
import model.Device.Type;
import model.Person;
import model.PersonRepository;
import model.LightDevice;
import model.LightRepository;
import net.minidev.json.JSONObject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestContext.class, WebApplicationContext.class, PersistenceContext.class})
@AutoConfigureMockMvc
@SpringBootTest(classes = com.example.demo.DemoApplication.class)
public class AdminControllerUnitTests {
	
	@Autowired
	MockMvc mockMvc;
	
	@Mock
	PersonRepository personRepositoryMock;
	
	@Mock
	LightRepository rgbRepMock;
	
	@InjectMocks
	private AdminController controller;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this); 
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void test_register_valid_person() throws Exception {
		when(personRepositoryMock.save(Mockito.any(Person.class))).thenReturn(null);
		
		mockMvc.perform(post("/admin/user/register")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(createUserInJson("admin", "true", "p"))
				)
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.status", is("OK")));
		
		verify(personRepositoryMock, times(1)).save(Mockito.any(Person.class));
	}
	
	@Test
	public void test_register_non_valid_person() throws Exception {
		
		mockMvc.perform(post("/admin/user/register")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(createUserInJson("", "true", "p"))
				)
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.status", is("NOK")));
		
		verify(personRepositoryMock, times(0)).save(Mockito.any(Person.class));
	}
	
	@Test
	public void test_changepwd_valid() throws Exception {
		when(personRepositoryMock.findByUserName(Mockito.any(String.class))).thenReturn(createPerson("admin", "true"));
		
		mockMvc.perform(post("/admin/user/changePwd")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(createUserInJson("", "true", "p"))
				)
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.status", is("OK")));
		
		verify(personRepositoryMock, times(1)).save(Mockito.any(Person.class));
	}
	
	@Test
	public void test_changepwd_error() throws Exception {
		when(personRepositoryMock.findByUserName(Mockito.any(String.class))).thenReturn(createPerson("admin", "true"));
		
		mockMvc.perform(post("/admin/user/changePwd")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(createUserInJson("", "true", ""))
				)
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.status", is("NOK")));
		
		verify(personRepositoryMock, times(0)).save(Mockito.any(Person.class));
	}
	
	@Test
	public void test_changepwd_error2() throws Exception {
		when(personRepositoryMock.findByUserName(Mockito.any(String.class))).thenReturn(null);
		
		mockMvc.perform(post("/admin/user/changePwd")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(createUserInJson("admin", "true", ""))
				)
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.status", is("NOK")));
		
		verify(personRepositoryMock, times(0)).save(Mockito.any(Person.class));
	}
	
	@Test
	public void test_rgb_device_register_valid() throws Exception {
		
		mockMvc.perform(post("/admin/device/register")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(createDeviceJson("rgbdev", "192.168.1.1", Type.RGBLIGHT))
				)
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.status", is("OK")));
		
		verify(rgbRepMock, times(1)).save(Mockito.any(LightDevice.class));
	}
	
	@Test
	public void test_rgb_device_register_error() throws Exception {
		
		mockMvc.perform(post("/admin/device/register")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(createDeviceJson("", "192.168.1.1", Type.RGBLIGHT))
				)
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.status", is("NOK")));
		
		verify(rgbRepMock, times(0)).save(Mockito.any(LightDevice.class));
	}
	
	@Test
	public void test_rgb_device_register_error2() throws Exception {
		
		mockMvc.perform(post("/admin/device/register")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(createDeviceJson("rgbdev", "", Type.RGBLIGHT))
				)
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.status", is("NOK")));
		
		verify(rgbRepMock, times(0)).save(Mockito.any(LightDevice.class));
	}
	
	@Test
	public void test_rgb_device_register_error3() throws Exception {
		
		mockMvc.perform(post("/admin/device/register")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(createDeviceJson("rgbdev", "", Type.DEVICE))
				)
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.status", is("NOK")));
		
		verify(rgbRepMock, times(0)).save(Mockito.any(LightDevice.class));
	}

	@Test
	public void test_get_devices_valid() throws Exception {
		
		mockMvc.perform(get("/admin/device/getDevices")
				//.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				//.content(createDeviceJson("rgbdev", "192.168.1.1", Type.RGBLIGHT))
				)
		.andDo(print())
		.andExpect(status().isOk());
		
		verify(rgbRepMock, times(1)).findAll();
	}
	
	@Test
	public void test_remove_device_valid() throws Exception {
		when(rgbRepMock.findLightByName(Mockito.any(String.class))).thenReturn(createDevice("device", ""));
		
		mockMvc.perform(delete("/admin/device/removeDevice")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(createDeviceJson("rgbdev", "", Type.DEVICE))
				)
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.status", is("OK")));
		
		verify(rgbRepMock, times(1)).delete(Mockito.any(LightDevice.class));
	}
	
	@Test
	public void test_remove_device_error() throws Exception {
		when(rgbRepMock.findLightByName(Mockito.any(String.class))).thenReturn(null);
		
		mockMvc.perform(delete("/admin/device/removeDevice")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(createDeviceJson("rgbdev", "", Type.DEVICE))
				)
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.status", is("NOK")));
		
		verify(rgbRepMock, times(0)).delete(Mockito.any(LightDevice.class));
	}

	
	
	private String createDeviceJson(String name, String ip, Device.Type type) {
		JSONObject js = new JSONObject();
		
		js.put("name", name);
		js.put("ipAddr", ip);
		js.put("type", type.toString());
		
		return js.toJSONString();
	}
	
	
	private String createUserInJson(String userName, String admin, String pass) {
		JSONObject js = new JSONObject();
		js.put("userName", userName);
		js.put("admin", admin);
		js.put("firstName", "null");
		js.put("lastName", "null");
		js.put("age", "null");
		js.put("id", 1);
		js.put("idString", "2");
		js.put("passWord", pass);
		
		return js.toJSONString();
	}

	
	private Person createPerson(String user, String admin) {
		Person p = new Person();
		
		p.setUserName(user);
		p.setAdmin(admin);
		return p;
	}
	
	private LightDevice createDevice(String name, String admin) {
		LightDevice d = new LightDevice();
		
		d.setName(name);
		return d;
	}

}
