package com.niit.restcontroller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.niit.dao.UserDAO;
import com.niit.model.User;

@RestController
public class UserController {

	@Autowired
	UserDAO userDAO;

	//------------------CheckLogin-----------------
	@PostMapping(value="/login")
	public ResponseEntity<User> checkLogin(@RequestBody User userDetail, HttpSession session)
	{
		System.out.println("Inside user login");
		if(userDAO.checkLogin(userDetail))
		{
			User tempUser=(User)userDAO.getUser(userDetail.getEmail());
			userDAO.updateOnlineStatus("Y", tempUser);
			session.setAttribute("userRecord", tempUser);
			return new ResponseEntity<User>(tempUser,HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<>(userDetail,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// ----------------- Register User ---------------
	@PostMapping(value = "/registerUser")
	public ResponseEntity<String> registerUser(@RequestBody User user) {
		
		System.out.println("Inside user registration");
		user.setIsOnline("Not");
		user.setRole("USER");
		if (userDAO.addUser(user)) {
			return new ResponseEntity<String>("User Registered Successfully", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("User registration failed", HttpStatus.NOT_FOUND);
		}
	}

	// ----------- Update User -----------------------------
	@PostMapping(value = "/update/{email}")
	public ResponseEntity<String> updateUser(@PathVariable("email") String email, @RequestBody User userDetail) {
		System.out.println("In updating user " + email);
		User mUser = userDAO.getUser(email);
		if (mUser == null) {
			System.out.println("No user found with loginname " + email);
			return new ResponseEntity<String>("No user found", HttpStatus.NOT_FOUND);
		}

		mUser.setEmail(userDetail.getEmail());
		mUser.setPhone(userDetail.getPhone());
		mUser.setAddress(userDetail.getAddress());
		mUser.setName(userDetail.getName());
		userDAO.updateUser(mUser);
		return new ResponseEntity<String>("User updated successfully", HttpStatus.OK);
	}

	// --------------------- List Users --------------------------
	@GetMapping(value = "/listUsers")
	public ResponseEntity<List<User>> listUsers() {
		List<User> listUsers = userDAO.listUsers();
		if (listUsers.size() != 0) {
			return new ResponseEntity<List<User>>(listUsers, HttpStatus.OK);
		}
		return new ResponseEntity<List<User>>(listUsers, HttpStatus.NOT_FOUND);
	}

	// --------------------- Get User ----------------------
	@GetMapping(value = "/getUser/{loginname}")
	public ResponseEntity<User> getUser(@PathVariable("loginname") String loginname) {
		User user = userDAO.getUser(loginname);
		if (user == null) {
			System.out.println("No user found");
			return new ResponseEntity<User>(user, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}
	}

	@DeleteMapping(value = "/deleteUser/{loginname}")
	public ResponseEntity<String> deleteUser(@PathVariable("loginname") String loginname) {
		System.out.println("In delete user" + loginname);
		User user = userDAO.getUser(loginname);
		if (user == null) {
			System.out.println("No user with LoginName " + loginname + " found to delete");
			return new ResponseEntity<String>("No user found to delete", HttpStatus.NOT_FOUND);
		} else {
			userDAO.deleteUser(user);
			return new ResponseEntity<String>("User with LoginName " + loginname + " deleted successfully", HttpStatus.OK);
		}
	}
}