package in.insplan003.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import in.insplan003.binding.ActivateAccount;
import in.insplan003.binding.Login;
import in.insplan003.binding.User;
import in.insplan003.entity.UserMaster;
import in.insplan003.repository.UserMasterRepo;
import in.insplan003.utils.EmailUtils;

@Service
public class UserMgmtServiceImpl implements UserMgmtService {

	private Logger logger = LoggerFactory.getLogger(UserMgmtServiceImpl.class);
	
	@Autowired
	private UserMasterRepo userMasterRepo;

	@Autowired
	private EmailUtils emailUtils;
	
	private Random random = new Random();

	@Override
	public boolean saveUser(User user) {

		UserMaster entity = new UserMaster();
		BeanUtils.copyProperties(user, entity);
		entity.setPassword(generateRandomPwd());
		entity.setAccStatus("In-Active");
		UserMaster userrecord = userMasterRepo.save(entity);
		String subject = "Your Registration is Success";
		String fileName = "REG-EMAIL-BODY.txt";
		String body = readEmailBody(entity.getFullName(), entity.getPassword(), fileName);
		emailUtils.sendMail(user.getEmail(), subject, body);

		return userrecord.getUserId() != null;
	}

	@Override
	public boolean activateUserAcc(ActivateAccount activateAcc) {
		UserMaster entity = new UserMaster();
		entity.setEmail(activateAcc.getEmail());
		entity.setPassword(activateAcc.getTempPwd());
		Example<UserMaster> example = Example.of(entity);
		List<UserMaster> findAll = userMasterRepo.findAll(example);
		if (findAll.isEmpty()) {
			return false;
		} else {
			UserMaster userMaster = findAll.get(0);
			userMaster.setPassword(activateAcc.getNewPwd());
			userMaster.setAccStatus("Active");
			userMasterRepo.save(userMaster);
			return true;
		}
	}

	@Override
	public List<User> getAllUsers() {
		List<User> users = new ArrayList<>();
		List<UserMaster> findAll = userMasterRepo.findAll();
		for (UserMaster entity : findAll) {
			User user = new User();
			BeanUtils.copyProperties(entity, user);
			users.add(user);
		}
		return users;
	}

	@Override
	public User getUserById(Integer userId) {
		Optional<UserMaster> findById = userMasterRepo.findById(userId);
		if (findById.isPresent()) {
			User user = new User();
			UserMaster userMaster = findById.get();
			BeanUtils.copyProperties(userMaster, user);
			return user;
		}
		return null;
	}

	@Override
	public boolean deleteUserById(Integer userId) {
		try {
			userMasterRepo.deleteById(userId);
		} catch (Exception e) {
			logger.error("Exception occured", e);;
		}
		return false;
	}

	@Override
	public boolean changeAccountStatus(Integer userId, String accStatus) {
		Optional<UserMaster> findById = userMasterRepo.findById(userId);
		if (findById.isPresent()) {
			UserMaster userMaster = findById.get();
			userMaster.setAccStatus(accStatus);
			userMasterRepo.save(userMaster);
			return true;
		}
		return false;
	}

	@Override
	public String login(Login login) {
		UserMaster entity = new UserMaster();
		entity.setEmail(login.getEmail());
		entity.setPassword(login.getPassword());
		Example<UserMaster> example = Example.of(entity);
		List<UserMaster> findAll = userMasterRepo.findAll(example);
		if (findAll.isEmpty()) {
			return "Invalid Credentials";
		} else {
			UserMaster userMaster = findAll.get(0);
			if (userMaster.getAccStatus().equals("Active")) {
				return "Login Success !";
			} else {
				return "Account not activated. Please activate account";
			}
		}
	}

	@Override
	public String forgotPwd(String email) {
		UserMaster entity = userMasterRepo.findByEmail(email);
		if (entity == null) {
			return "Invalid Email ID";
		}
		String subject = "Forgot Password";
		String fileName = "RECOVER-PWD-BODY.txt";
		String body = readEmailBody(email, entity.getPassword(), fileName);
		boolean sendMail = emailUtils.sendMail(entity.getEmail(), subject, body);

		if (sendMail) {
			return "Password sent to your registered mail";
		}
		return null;
	}

	private String generateRandomPwd() {

		String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
		String numbers = "0123456789";
		String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;
		StringBuilder sb = new StringBuilder();
		//Random random = new Random();
		int length = 6;
		for (int i = 0; i < length; i++) {
			int index = this.random.nextInt(alphaNumeric.length());
			char randomChar = alphaNumeric.charAt(index);
			sb.append(randomChar);
		}
		String randomString = sb.toString();
		System.out.println("Random String is: " + randomString);
		return randomString;
	}

	private String readEmailBody(String fullname, String pwd, String filename) {
		String url = "";
		String mailBody = null;

		try (
				FileReader fr = new FileReader(filename);
				BufferedReader br = new BufferedReader(fr);
			){
			StringBuilder builder = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				builder.append(line);
				line = br.readLine();
			}
			br.close();
			mailBody = builder.toString();
			mailBody = mailBody.replace("{FULL-NAME}", fullname);
			mailBody = mailBody.replace("{TEMP-PWD}", pwd);
			mailBody = mailBody.replace("{URL}", url);
			mailBody = mailBody.replace("{PWD}", pwd);
		} catch (Exception e) {
			logger.error("Exception occured", e);;
		}
		return mailBody;
	}

}
