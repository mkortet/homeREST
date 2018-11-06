package unit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.core.IsAnything;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.hibernate.engine.spi.PersistenceContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import controller.LoginController;
import model.Person;
import model.PersonRepository;
import net.minidev.json.JSONObject;
import util.JWTUtil;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestContext.class, WebApplicationContext.class, PersistenceContext.class})
@AutoConfigureMockMvc
@SpringBootTest(classes = com.example.demo.DemoApplication.class)

public class LoginControllerUnitTests {
	
	@Autowired
	MockMvc mockMvc;
	
	@Mock
	PersonRepository personRepositoryMock;
	
	@InjectMocks
	private LoginController controller;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this); 
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}
	
	@Test
	public void test_Should_Return_200_OK() throws Exception {
		when(personRepositoryMock.count()).thenReturn(0L);
		//personRepositoryMock.save(new Person());
		
		mockMvc.perform(post("/login/init")).andDo(print())
			.andExpect(status().isOk());
		
		verify(personRepositoryMock, times(1)).count();
	}
	
	@Test
	public void test_Should_Return_200_NOK() throws Exception {
		when(personRepositoryMock.count()).thenReturn(1L);
		
		mockMvc.perform(post("/login/init")).andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status", is("NOK")));
			
		
		verify(personRepositoryMock, times(1)).count();
	} 
	
	@Test
	public void test_bad_user_from_db() throws Exception {
		when(personRepositoryMock.findByUserName(any(String.class))).thenReturn(getBadUser());
		
		mockMvc.perform(post("/login/user")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(createUserInJson("admin", "true"))
				).andDo(print())
			  .andExpect(jsonPath("$.status", is("NOK")));
		
		verify(personRepositoryMock, times(1)).findByUserName(any(String.class));
	}
	
	@Test
	public void test_bad_user_json_parameter() throws Exception {
		when(personRepositoryMock.findByUserName(Mockito.anyString())).thenReturn(getValidUser());
		
		/*MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/login/user")
												.contentType(MediaType.APPLICATION_JSON)
												.content(createUserInJson("admin", "true"));
		
		*/
		mockMvc.perform(post("/login/user")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(createBadJson("admin", "true"))
				).andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status", is("NOK")));
		
		verify(personRepositoryMock, times(1)).findByUserName(Mockito.anyString());
	}
	
	@Test
	public void test_valid_user_request() throws Exception {
		when(personRepositoryMock.findByUserName(Mockito.anyString())).thenReturn(getValidUser());
		
		/*MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/login/user")
												.contentType(MediaType.APPLICATION_JSON)
												.content(createUserInJson("admin", "true"));
		
		*/
		mockMvc.perform(post("/login/user")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(createUserInJson("admin", "true"))
				).andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status", is("OK")))
			.andExpect(jsonPath("$.message", is(new JWTUtil(getValidUser()).createToken())));
			
		
		verify(personRepositoryMock, times(1)).findByUserName(Mockito.anyString());
	}

	private Person getBadUser() {
		Person p = new Person();
		p.setAdmin("true");
		p.setUserName("admin");
		p.setPassWord("n");
		
		return p;
	}
	
	private Person getValidUser() {
		Person p = new Person();
		
		p.setAdmin("true");
		p.setUserName("admin");
		p.setPassWord("p");
		
		return p;
	}
	
	private String createUserInJson(String userName, String admin) {
		JSONObject js = new JSONObject();
		js.put("userName", userName);
		js.put("admin", admin);
		js.put("firstName", "null");
		js.put("lastName", "null");
		js.put("age", "null");
		js.put("id", 1);
		js.put("idString", "2");
		js.put("passWord", "p");
		
		// System.out.println("JSON: " + js.toJSONString());
		
		return js.toJSONString();
	}
	
	private String createBadJson(String userName, String admin) {
		JSONObject js = new JSONObject();
		js.put("userName", userName);
		js.put("admin", admin);
		js.put("firstName", "null");
		js.put("lastName", "null");
		js.put("age", "null");
		js.put("id", 1);
		js.put("idString", "2");
		js.put("passWord", "r"); // wrong pass
		
		// System.out.println("JSON: " + js.toJSONString());
		
		return js.toJSONString();
	}
}
