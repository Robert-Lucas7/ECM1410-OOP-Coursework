package socialmedia;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.*;


public class SocialMedia implements SocialMediaPlatform {
    private ArrayList<Account> accountList = new ArrayList<Account>();

    @Override
	public int createAccount(String handle) throws IllegalHandleException, InvalidHandleException {
		//Validate handle for both exceptions
        Account.validateHandle(handle, accountList); //throws InvalidHandleException and IllegalHandleException
        Account newAccount = new Account(handle);
        accountList.add(newAccount);
        return newAccount.getID();
	}

	@Override
	public int createAccount(String handle, String description) throws IllegalHandleException, InvalidHandleException {
		Account.validateHandle(handle, accountList); //throws InvalidHandleException and IllegalHandleException
		Account newAccount = new Account(handle, description);
        accountList.add(newAccount);
        return newAccount.getID();
	}

	@Override
	public void removeAccount(int id) throws AccountIDNotRecognisedException {
		Account accountToDelete = Account.findAccountById(id, accountList);
		for (Post p: accountToDelete.getPosts()){
			//Remove posts
			try{
				deletePost(p.getID());
			} catch(PostIDNotRecognisedException e){
				continue;
			}
			//have a function to clear the endorsement list.
			p.clearEndorsements();
		}
		for(Account a: accountList){
			for(Post p : a.getPosts()){
				if(p instanceof Comment){
					Comment c = (Comment)p;
					if(accountToDelete.getPosts().contains(c.getReferencePost())){//if a comment is referencing a post that belongs to the accountToBeRemoved, then it should be removed/deleted.
						try {
							deletePost(c.getID());
						} catch (PostIDNotRecognisedException e){
							continue;
						}
					}
				}
			}
		}
		//Remove from account list
		accountList.remove(accountToDelete);
	}

	@Override
	public void removeAccount(String handle) throws HandleNotRecognisedException {
		Account accountToDelete = Account.findAccountByHandle(handle, accountList);//Throws HandleNotRecognised exception
		try{
			removeAccount(accountToDelete.getID());
		} catch (AccountIDNotRecognisedException e){//HOW SHOULD THIS BE HANDLED?
			//This will never execute, as if the account doesn
		}
	}

	@Override
	public void changeAccountHandle(String oldHandle, String newHandle)
			throws HandleNotRecognisedException, IllegalHandleException, InvalidHandleException {
		
		//Validate handle for both exceptions
        Account.validateHandle(newHandle, accountList);
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
							"\nPost count: " + a.getPosts().size() +
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
		postingAccount.addPost(newPost);
		return newPost.getID();
	}

	@Override
	public int endorsePost(String handle, int id)
			throws HandleNotRecognisedException, PostIDNotRecognisedException, NotActionablePostException {
			Account postingAccount = Account.findAccountByHandle(handle, accountList); //throws HandleNotRecognisedException
			Post postToEndorse = Post.findPostByID(id, accountList); //throws PostIDNotRecognizedException
			//========== CAN YOU ENDORSE A COMMENT? ===========
			if (postToEndorse instanceof EndorsementPost){
				throw new NotActionablePostException();
			}
			String message = "EP@" + postToEndorse.getAccount().getHandle() + ": " + postToEndorse.getMessage();
			EndorsementPost endorsementPost = new EndorsementPost(postingAccount, message, postToEndorse);
			postToEndorse.addEndorsementPost(endorsementPost);
			postToEndorse.getAccount().setEndorsementCountUpToDateToFalse();//postingAccount.setEndorsementCountUpToDateToFalse();
			postingAccount.addPost(endorsementPost);
			return endorsementPost.getID();
	}

	@Override
	public int commentPost(String handle, int id, String message) throws HandleNotRecognisedException,
			PostIDNotRecognisedException, NotActionablePostException, InvalidPostException {

		Account postingAccount = Account.findAccountByHandle(handle, accountList); //throws HandleNotRecognisedException
		Post commentedPost = Post.findPostByID(id, accountList); //throws PostIDNotRecognizedException
		Post.validateMessage(message); //throws InvalidPostException

		if (commentedPost instanceof EndorsementPost){
			throw new NotActionablePostException();
		}

		Comment newComment = new Comment(postingAccount, message, commentedPost);
		commentedPost.addComment(newComment);
		postingAccount.addPost(newComment);
		return newComment.getID();
	}

	@Override
	public void deletePost(int id) throws PostIDNotRecognisedException {
		Post postToDelete = Post.findPostByID(id, accountList); //throws PostIDNotRecognizedException
		
		postToDelete.getAccount().removePost(postToDelete);
		
		if (postToDelete instanceof EndorsementPost){
			((EndorsementPost)postToDelete).getReferencePost().removeEndorsement((EndorsementPost)postToDelete);
		}
		else{
			postToDelete.clearEndorsements();
		}
		postToDelete.setPostToEmpty();
		
		
	}

	@Override
	public String showIndividualPost(int id) throws PostIDNotRecognisedException {
		Post postToShow = Post.findPostByID(id, accountList);
		return "ID: "+ postToShow.getID() +
		"\nAccount: "+postToShow.getAccount().getHandle() +
		"\nNo. endorsements: "+postToShow.getNumEndorsements()+" | No. comments: "+postToShow.getNumComments()+ "\n" +
		postToShow.getMessage();
	}

	
	private StringBuilder DFSChildren(Post originalPost, Post post, int indent, StringBuilder sb, ArrayList<Post> visited) throws PostIDNotRecognisedException{
		visited.add(post);

		//Add indentation and pipes here...
		if(post instanceof Comment){
			boolean isFirstComment;
			if (((Comment) post).getReferencePost().getComments().get(0).equals(post)){
				isFirstComment = true;
			} else{
				isFirstComment = false;
			}
			sb.append(Post.formatMessage(showIndividualPost(post.getID()), indent, isFirstComment));
		}	
		else{
			sb.append(showIndividualPost(post.getID())+"\n");
		}
		indent++;

		for(Comment c:post.getComments()){
			if(!visited.contains(c)){//upcasting
				
				DFSChildren(originalPost, c, indent, sb, visited);
			}
		}
		return sb;
	}


	@Override
	public StringBuilder showPostChildrenDetails(int id)
			throws PostIDNotRecognisedException, NotActionablePostException {
		StringBuilder sb = new StringBuilder();
		Post postToShow = Post.findPostByID(id, accountList);

		if (postToShow instanceof EndorsementPost){
			throw new NotActionablePostException();
		}
		return DFSChildren(postToShow, postToShow, 0,sb,new ArrayList<Post>());
	}

	@Override
	public int getNumberOfAccounts() {
		return accountList.size();
	}

	@Override
	public int getTotalOriginalPosts() { 
		int total = 0;
		for(Account a:accountList){
			total += a.getOriginalPostCount();
		}
		return total;
	}

	@Override
	public int getTotalEndorsmentPosts() {
		int total = 0;
		for(Account a:accountList){
			total += a.getEndorsementCount();//p.getEndorsements().size();
		}
		return total;
	}

	@Override
	public int getTotalCommentPosts() {
		int total = 0;
		for(Account a:accountList){
			for(Post p:a.getPosts()){
				total += p.getComments().size();
			}
		}
		return total;
	}

	@Override
	public int getMostEndorsedPost() {
		Post mostEndorsedPost = null;
		for(Account a:accountList){
			for(Post p : a.getPosts()){
				if(mostEndorsedPost == null || p.getNumEndorsements() > mostEndorsedPost.getNumEndorsements()){//short-circuits so no error (Hopefully)
					mostEndorsedPost = p;
				}
			}
		}
		return mostEndorsedPost.getID();
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
		accountList.clear();
		Post.resetIdCount();
		Account.resetIdCount();
	}
	@Override
	public void savePlatform(String filename) throws IOException {
		try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))){
			Account[] accountArr = accountList.toArray(new Account[accountList.size()]);
			out.writeObject(accountArr);
		}
	}

	@Override
	public void loadPlatform(String filename) throws IOException, ClassNotFoundException {
		try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))){
			Account[] accountArr = (Account[])in.readObject();
			accountList = new ArrayList<>(Arrays.asList(accountArr));
		} 
	}
}
