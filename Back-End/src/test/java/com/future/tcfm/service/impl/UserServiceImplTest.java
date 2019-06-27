package com.future.tcfm.service.impl;

import com.future.tcfm.model.User;
import com.future.tcfm.repository.GroupRepository;
import com.future.tcfm.repository.UserRepository;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UserServiceImplTest {
    private static final String USER_ID = "userId";


    @Mock
    private UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;



    private User user;


    @Before
    public void init(){
        user = new User();
        user.setName("Nancy");
        user.setIdUser(USER_ID);
        user.setGroupName("ISH");
        user.setRole("STAFF");
        user.setEmail("nancy@gdn.com");
        user.setPassword("nancy123");
    }

    @Test
    public void loadAll() {
        // Data preparation
        List<User> users = Arrays.asList(user,user,user);
        Mockito.when(userRepository.findAll()).thenReturn(users);

        // Method call
        List<User> userList= userService.loadAll();

        // Verification
        Assert.assertThat(userList, Matchers.hasSize(3));
        Mockito.verify(userRepository, Mockito.times(1)).findAll();
        Mockito.verifyNoMoreInteractions(userRepository);
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
/*        MockMultipartFile pic = new MockMultipartFile("data", "filename.jpg", "text/plain", "some xml".getBytes());

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                .file(pic)
                .param("some-random", "4"))
                .andExpect(status().is(200))
                .andExpect(content().string("success"));*/
    }

    @Test
    public void getImage() {}

    @Test
    public void saveUploadedFile() {}

    @Test
    public void checkImageFile() {}



}
