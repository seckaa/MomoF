package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest(showSql=false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	
	@Autowired
	private UserRepository repo;
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateUser() {	
	}
	
	@Test
	public void testCreateNewUserWith1Role() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User userSeckaa = new User("test@me.net","test1234", "test", "Seck");
		userSeckaa.addRoll(roleAdmin);
		
		User savedUser = repo.save(userSeckaa);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	@Test
	public void testCreateNewUserWith2Roles() {
		User userBadou =new User("badou@me.net", "test", "Badou", "Seck");
		Role roleEditor = new Role(4);
		Role roleAssistant = 	new Role(3);
		userBadou.addRoll(roleAssistant);
		userBadou.addRoll(roleEditor);
		
		User savedUser = repo.save(userBadou);
		assertThat(savedUser.getId()).isGreaterThan(0); 
	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(user->System.out.println(user));
	}
	
	@Test
	public void testFindByid() {
		User userBadou = repo.findById(9).get();
		System.out.println(userBadou);
		assertThat(userBadou).isNotNull();
	}
	@Test
	public void testUpdateUserDetails () {
		User userBadou = repo.findById(9).get();
		userBadou.setEnabled(true);
		userBadou.setEmail("badouseck@me.net");
		
		repo.save(userBadou);
	}
	
	@Test
	public void testUpdateUserRoles () {
		User userBadou = repo.findById(9).get();
		Role roleEditor = new Role(4);
		Role roleSalesperson = new Role(2);
		userBadou.getRoles().remove(roleEditor);
		userBadou.addRoll(roleSalesperson);
		
		 
		repo.save(userBadou);
	}
	
	@Test
	public void testDeleteUser () {
		Integer userId = 15;
		repo.deleteById(userId);
	}
	
	@Test 
	public void testGetUserByEmail() {
		String email="Khodia@me.com";
		User userByEmail = repo.getUserByEmail(email);
		assertThat(userByEmail).isNotNull();
	}
	
	@Test
	public void testCountById() {
		Integer id = 11;
		Long countById = repo.countById(id);
		
		assertThat(countById).isNotNull().isGreaterThan(0);
		
	}
	
	@Test
	public void testDisableUser() {
		Integer id = 11;
		repo.updateEnabledStatus(id, false);
	}
	
	@Test
	public void testEnableUser() {
		Integer id = 12;
		repo.updateEnabledStatus(id, true);
	}
	
	@Test
	public void testListFirstPage() {
		int pageNumber = 0;
		int pageSize = 4;
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(pageable);
		
		List<User> listUsers = page.getContent();
		
		listUsers.forEach(user->System.out.println(user));
		
		assertThat(listUsers.size()).isEqualTo(pageSize);
	}
	
	@Test
	public void testSearchUsers() {
		String keyword = "seck";
		int pageNumber = 0;
		int pageSize = 4;
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(keyword,pageable);
		
		List<User> listUsers = page.getContent();
		
		listUsers.forEach(user->System.out.println(user));
		assertThat(listUsers.size()).isGreaterThan(0);
	}
	
	
	
	
	
	
}
