package in.insplan003.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.insplan003.binding.ActivateAccount;
import in.insplan003.binding.Login;
import in.insplan003.binding.User;
import in.insplan003.service.UserMgmtService;

@RestController
public class UserRestController {
	
	@Autowired
	private UserMgmtService service;
	
	@PostMapping("/user")
	public ResponseEntity<String> userReg(@RequestBody User user){
		boolean saveUser = service.saveUser(user);
		if(saveUser) {
			return new ResponseEntity<String> ("Registration Success", HttpStatus.CREATED);
		}else {
			return new ResponseEntity<String> ("Registration Failed", HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@PostMapping("/activate")
	public ResponseEntity<String> activateAccount(@RequestBody ActivateAccount activateAccount){
		boolean isActivated = service.activateUserAcc(activateAccount);
		if (isActivated) {
			return new ResponseEntity<> ("Account activated",HttpStatus.OK);
		}else {
			return new ResponseEntity<> ("Invalid Temporary Password", HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/users")
	public ResponseEntity<List<User>> getAllUsers(){
		List<User> allUsers = service.getAllUsers();
		return new ResponseEntity<List<User>>(allUsers, HttpStatus.OK);
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<User> getUserById(@PathVariable Integer userId){
		User user = service.getUserById(userId);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@DeleteMapping("/user/{userId}")
	public ResponseEntity<String> deleteUserById(@PathVariable Integer userId){
		boolean isDeleted = service.deleteUserById(userId);
		
		if(isDeleted) {
			return new ResponseEntity<String> ("User Deleted successfully", HttpStatus.OK);
		}else {
			return new ResponseEntity<String> ("User Delete Failed", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
    }
	@GetMapping("/status/{userId}/{status}")
	public ResponseEntity<String> statusChange(@PathVariable Integer userId, @PathVariable String status){
		boolean isChanged = service.changeAccountStatus(userId, status);
		if(isChanged) {
			return new ResponseEntity<> ("Status Changed", HttpStatus.OK);
		}else {
			return new ResponseEntity<> ("Failed to Change", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody Login login){
		String loginStatus = service.login(login);
		return new ResponseEntity<String>(loginStatus, HttpStatus.OK);
	}
	
	@GetMapping("/forgotpwd/{email}")
	public ResponseEntity<String> forgotPwd(@PathVariable String email){
		String status = service.forgotPwd(email);
		return new ResponseEntity<String> (status, HttpStatus.OK);
	}
	
	
	
}