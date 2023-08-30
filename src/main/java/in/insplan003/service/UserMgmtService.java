package in.insplan003.service;

import java.util.List;

import in.insplan003.binding.ActivateAccount;
import in.insplan003.binding.Login;
import in.insplan003.binding.User;

public interface UserMgmtService {
	
	public boolean saveUser(User user);
	
	public boolean activateUserAcc(ActivateAccount activateAcc);
	
	public List<User> getAllUsers();
	
	public User getUserById(Integer userId);
	
	public boolean deleteUserById(Integer userId);
	
	public boolean changeAccountStatus(Integer userId, String accStatus);
	
	public String login(Login login);
	
	public String forgotPwd(String email);
	
	
}
