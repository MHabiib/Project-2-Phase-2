package com.future.tcfm.service.impl;

import com.future.tcfm.model.User;
import com.future.tcfm.repository.UserRepository;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;

//@RunWith(MockitoJUnitRunner.Silent.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
@ContextConfiguration
public class UserServiceImplTest extends AbstractTestNGSpringContextTests {
    private static final String USER_ID = "userId";


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;


    @InjectMocks
    UserServiceImpl userService;

    private User user;



    @Before
    public void setup() {
        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        this.mockMvc = builder.build();
    }


    @Before
    public void init(){
        user = new User();
        user.setName("Nancy");
        user.setIdUser(USER_ID);
        user.setGroupName("ISH");
        user.setRole("STAFF");
        user.setEmail("nancy@gdn.com");
        user.setPassword("nancy123");
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void loadAll() {
        List<User> users = Arrays.asList(user,user,user);

        List<User> found =userRepository.findAll();

        assertThat(found.get(0).getName()).isEqualTo("Admin");

     /*   Mockito.when(userRepository.findAll()).thenReturn(users);

        // Method call
        List<User> userList= userService.loadAll();

        // Verification
        Assert.assertThat(userList, Matchers.hasSize(3));
        Mockito.verify(userRepository, Mockito.times(1)).findAll();
        Mockito.verifyNoMoreInteractions(userRepository);*/
    }

    @Test
    public void getUserByEmail() {
        User user = new User();
        user.setEmail("nancy@gdn.com");

        doReturn(user).when(userRepository).findByEmail(this.user.getEmail());
        User result = userService.getUser(user.getEmail());

        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(user.getEmail());
        Assert.assertEquals(result, user);
    }

    @Test
    public void createUserV2() {}

    @Test
    public void updateUserV2() throws Exception{

            ResultMatcher ok = MockMvcResultMatchers.status().isOk();

            String fileName = "test.txt";
            File file = new File(UserServiceImpl.UPLOADED_FOLDER + fileName);
            //delete if exits
            file.delete();

            MockMultipartFile mockMultipartFile = new MockMultipartFile("user-file",fileName,
                    "text/plain", "test data".getBytes());

            MockHttpServletRequestBuilder builder =
                    MockMvcRequestBuilders.fileUpload("/upload")
                            .file(mockMultipartFile);
            this.mockMvc.perform(builder).andExpect(ok)
                    .andDo(MockMvcResultHandlers.print());;
            Assert.assertTrue(file.exists());
    }

/*        MockMultipartFile pic = new MockMultipartFile("data", "filename.jpg", "text/plain", "some xml".getBytes());

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                .file(pic)
                .param("some-random", "4"))
                .andExpect(status().is(200))
                .andExpect(content().string("success"));*/


    @Test
    public void getImage() {}

    @Test
    public void saveUploadedFile() {}

    @Test
    public void checkImageFile() {}



}
