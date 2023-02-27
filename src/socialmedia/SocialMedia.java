package socialmedia;

import java.util.ArrayList;

public class SocialMedia implements SocialMediaPlatform{
    private ArrayList<Account> accountList = new ArrayList<Account>();
    
    @Override
	public int createAccount(String handle) throws IllegalHandleException, InvalidHandleException {
		//Validate handle for both exceptions
        Account.validHandle(handle, accountList);
        Account newAccount = new Account(handle);
        accountList.add(newAccount);
        return newAccount.getID();
	}

	@Override
	public int createAccount(String handle, String description) throws IllegalHandleException, InvalidHandleException {
		int id = createAccount(handle);
		//Should we validate description???????????
        accountList.get(accountList.size() -1).setDescription(description);
		return id;
	}

	@Override
	public void removeAccount(int id) throws AccountIDNotRecognisedException {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeAccount(String handle) throws HandleNotRecognisedException {
		// TODO Auto-generated method stub

	}

	@Override
	public void changeAccountHandle(String oldHandle, String newHandle)
			throws HandleNotRecognisedException, IllegalHandleException, InvalidHandleException {
		
		//Validate handle for both exceptions
        Account.validHandle(newHandle, accountList);
		boolean accountFound = false;
		for(Account a : accountList){
			if(a.getHandle().equals(oldHandle)){
				a.setHandle(newHandle);
				accountFound = true;
				break;
			}
		}
		if(!accountFound){
			throw new HandleNotRecognisedException();
		}

	}

	@Override
	public void updateAccountDescription(String handle, String description) throws HandleNotRecognisedException {
		
		//Validate Description????
		boolean accountFound = false;
		for(Account a : accountList){
			if(a.getHandle().equals(handle)){
				a.setDescription(description);
				accountFound = true;
				break;
			}
		}
		if(!accountFound){
			throw new HandleNotRecognisedException();
		}

	}

	@Override
	public String showAccount(String handle) throws HandleNotRecognisedException {
		boolean accountFound = false;
		String output = "";
		for(Account a : accountList){
			if(a.getHandle().equals(handle)){
				//Create formatted string
				output = "<pre>" +
							"\nID: " + a.getID() +
							"\nHandle: " + a.getHandle() +
							"\nDescription: " + a.getDescription() +
							"\nPost count: " + a.getPostCount() +
							"\nEndorse count: " + a.getEndorsementCount() +
							"\n</pre>";
				
				accountFound = true;
				break;
			}
		}
		if(accountFound){
			return output;
		}
		else{
			throw new HandleNotRecognisedException();
		}
	}

	@Override
	public int createPost(String handle, String message) throws HandleNotRecognisedException, InvalidPostException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int endorsePost(String handle, int id)
			throws HandleNotRecognisedException, PostIDNotRecognisedException, NotActionablePostException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int commentPost(String handle, int id, String message) throws HandleNotRecognisedException,
			PostIDNotRecognisedException, NotActionablePostException, InvalidPostException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deletePost(int id) throws PostIDNotRecognisedException {
		// TODO Auto-generated method stub

	}

	@Override
	public String showIndividualPost(int id) throws PostIDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuilder showPostChildrenDetails(int id)
			throws PostIDNotRecognisedException, NotActionablePostException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumberOfAccounts() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTotalOriginalPosts() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTotalEndorsmentPosts() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTotalCommentPosts() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMostEndorsedPost() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMostEndorsedAccount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void erasePlatform() {
		// TODO Auto-generated method stub

	}

	@Override
	public void savePlatform(String filename) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadPlatform(String filename) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub

	}
}
