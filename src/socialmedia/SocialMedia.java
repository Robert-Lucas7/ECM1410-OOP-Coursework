package socialmedia;

import java.util.ArrayList;
import java.io.IOException;

public class SocialMedia implements SocialMediaPlatform{
    private ArrayList<Account> accountList = new ArrayList<Account>();
	private ArrayList<Post> postList = new ArrayList<Post>();
	//Add number of accounts
	//Total number of posts

	//Generic empty post to replace deleted posts

    
    @Override
	public int createAccount(String handle) throws IllegalHandleException, InvalidHandleException {
		//Validate handle for both exceptions
        Account.validHandle(handle, accountList); //throws InvalidHandleException and IllegalHandleException
        Account newAccount = new Account(handle);
        accountList.add(newAccount);
        return newAccount.getID();
	}

	@Override
	public int createAccount(String handle, String description) throws IllegalHandleException, InvalidHandleException {
		Account.validHandle(handle, accountList); //throws InvalidHandleException and IllegalHandleException
		Account newAccount = new Account(handle, description);
        accountList.add(newAccount);
        return newAccount.getID();
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
				output = 	"ID: " + a.getID() +
							"\nHandle: " + a.getHandle() +
							"\nDescription: " + a.getDescription() +
							"\nPost count: " + a.getPostCount() +
							"\nEndorse count: " + a.getEndorsementCount();
				
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
		Account postingAccount = Account.findAccountByHandle(handle, accountList); //finds account, throws HandleNotRecognised
		Post.validateMessage(message); //Checks message and throws up InvalidPostException
		Post newPost = new Post(postingAccount, message);
		postList.add(newPost);
		return newPost.getPostID();
	}

	@Override
	public int endorsePost(String handle, int id)
			throws HandleNotRecognisedException, PostIDNotRecognisedException, NotActionablePostException {
			Account postingAccount = Account.findAccountByHandle(handle, accountList); //throws HandleNotRecognisedException
			Post postToEndorse = Post.findPostByID(id, postList); //throws PostIDNotRecognizedException
			//========== CAN YOU ENDORSE A COMMENT? ===========
			if (postToEndorse instanceof EndorsementPost){
				throw new NotActionablePostException();
			}
			String message = "EP@" + postToEndorse.getAccount().getHandle() + ": " + postToEndorse.getMessage();
			EndorsementPost endorsementPost = new EndorsementPost(postingAccount, message);
			postToEndorse.addEndorsementPost(endorsementPost);
			return endorsementPost.getPostID();
	}

	@Override
	public int commentPost(String handle, int id, String message) throws HandleNotRecognisedException,
			PostIDNotRecognisedException, NotActionablePostException, InvalidPostException {

		Account postingAccount = Account.findAccountByHandle(handle, accountList); //throws HandleNotRecognisedException
		Post commentedPost = Post.findPostByID(id, postList); //throws PostIDNotRecognizedException
		Post.validateMessage(message); //throws InvalidPostException

		if (commentedPost instanceof EndorsementPost){
			throw new NotActionablePostException();
		}

		Comment newComment = new Comment(postingAccount, message);
		commentedPost.addComment(newComment);
		return newComment.getPostID();
	}

	@Override
	public void deletePost(int id) throws PostIDNotRecognisedException {
		Post postToDelete = Post.findPostByID(id, postList); //throws PostIDNotRecognizedException
		postList.remove(postToDelete);
		postToDelete.setPostToEmpty();
		/*for(EndorsementPost e : postToDelete.getEndorsements()){
			e.setPostToEmpty();
		}
		*/
		postToDelete.getEndorsements().removeAll(postToDelete.getEndorsements()); //By passing by reference - clears the arraylist.
	}

	@Override
	public String showIndividualPost(int id) throws PostIDNotRecognisedException {
		Post postToShow = Post.findPostByID(id, postList);
		return "ID: "+ postToShow.getPostID() +
		"\nAccount: "+postToShow.getAccount().getHandle() +
		"\nNo. endorsements: "+postToShow.getNumEndorsements()+" | No. comments: "+postToShow.getNumComments()+ "\n" +
		postToShow.getMessage();
	}

	@Override
	public StringBuilder showPostChildrenDetails(int id)
			throws PostIDNotRecognisedException, NotActionablePostException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumberOfAccounts() {
		return accountList.size();
	}

	@Override
	public int getTotalOriginalPosts() {
		return postList.size();
	}

	@Override
	public int getTotalEndorsmentPosts() {
		int total = 0;
		for(Post p : postList){
			total += p.getEndorsements().size();
		}
		return total;
	}

	@Override
	public int getTotalCommentPosts() {
		int total = 0;
		for(Post p : postList){
			total += p.getComments().size();
		}
		return total;
	}

	@Override
	public int getMostEndorsedPost() {
		Post mostEndorsedPost = null;
		for(Post p : postList){
			if(mostEndorsedPost == null || p.getNumEndorsements() > mostEndorsedPost.getNumEndorsements()){//short-circuits so no error (Hopefully)
				mostEndorsedPost = p;
			}
		}
		return mostEndorsedPost.getPostID();
	}

	@Override
	public int getMostEndorsedAccount() {
		Account mostEndorsedAccount = null;
		for(Account a : accountList){
			if(mostEndorsedAccount == null || a.getEndorsementCount() > mostEndorsedAccount.getEndorsementCount()){
				mostEndorsedAccount = a;
			}
		}
		return mostEndorsedAccount.getID();
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
