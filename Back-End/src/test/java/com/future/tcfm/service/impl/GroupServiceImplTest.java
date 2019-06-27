package com.future.tcfm.service.impl;

import com.future.tcfm.model.Group;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.client.ExpectedCount.times;

@RunWith(MockitoJUnitRunner.Silent.class)
public class GroupServiceImplTest {
    private static final String GROUP_ID = "groupId";
    private static final String USER_ID = "userId";

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    GroupServiceImpl groupService;

    private Group group;
    private User user;


    @Before
    public void init(){
        group = new Group();
        group.setName("Breakthrough");
        group.setIdGroup(GROUP_ID);
        group.setTotalExpense((double) 1000000);
        group.setClosedDate(null);
        group.setRegularPayment((double) 10000);
        group.setGroupBalance((double) 50000000);
        group.setActive(true);

        user=new User();
        user.setIdUser(USER_ID);
        user.setGroupName("Breakthrough");
    }

    @Test
    public void loadAll() {
        // Data preparation
        List<Group> groups = Arrays.asList(group,group,group);
        Mockito.when(groupRepository.findAll()).thenReturn(groups);

        // Method call
        List<Group> groupList= groupService.loadAll();

        // Verification
        Assert.assertThat(groupList, Matchers.hasSize(3));
        Mockito.verify(groupRepository, Mockito.times(1)).findAll();
        Mockito.verifyNoMoreInteractions(groupRepository);
    }

    @Test
    public void membersGroup() {
        // Data preparation
        List<User> users = Arrays.asList(user,user,user);
        Mockito.when(userRepository.findByGroupNameLike(user.getGroupName())).thenReturn(users);

        // Method call
        List<User> memberList= groupService.membersGroup(user.getGroupName());

        // Verification
        Assert.assertThat(memberList, Matchers.hasSize(3));
        Mockito.verify(userRepository, Mockito.times(1)).findByGroupNameLike(user.getGroupName());
        Mockito.verifyNoMoreInteractions(groupRepository);
    }

    @Test
    public void createGroup() {
        Group group = new Group();
        group.setName("NotSameName");

        doReturn(group).when(groupRepository).findByName(this.group.getName());
        doReturn(group).when(groupRepository).save(group);

        ResponseEntity<?> result = groupService.createGroup(group);

        Mockito.verify(groupRepository,Mockito.times(1)).save(group);
        Assert.assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void createGroupWithSameName() {
        Group group = new Group();
        group.setName("Breakthrough");

        doReturn(group).when(groupRepository).findByName(this.group.getName());
        //doReturn(group).when(groupRepository).save(group);

        ResponseEntity<?> result = groupService.createGroup(group);

//        Assert.assertNull(result);
        Mockito.verify(groupRepository, Mockito.times(1)).findByName(Mockito.anyString());
        Assert.assertEquals(result.getStatusCode(),HttpStatus.BAD_REQUEST);
    }

    @Test
    public void updateGroup() {
        Group group = new Group();
        group.setName("BOOM BOOM");

        // Method call
        groupService.updateGroup(GROUP_ID,group);

        doReturn(group).when(groupRepository).findByIdGroup(this.group.getIdGroup());
        doReturn(group).when(groupRepository).save(group);

        ResponseEntity<?> result = groupService.createGroup(group);

        Mockito.verify(groupRepository,Mockito.times(1)).save(group);
        Assert.assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void updateGroupIdNotFound() {

        Group group = new Group();
        group.setName("BOOM BOOM");
        group.setIdGroup("incorrectId");

        doReturn(group).when(groupRepository).findByIdGroup(this.group.getName());
        //doReturn(group).when(groupRepository).save(group);

        ResponseEntity<?> result = groupService.updateGroup(group.getIdGroup(),group);

//        Assert.assertNull(result);
        Mockito.verify(groupRepository, Mockito.times(1)).findByIdGroup(Mockito.anyString());
        Assert.assertEquals(result.getStatusCode(),HttpStatus.NOT_FOUND);
    }
}
