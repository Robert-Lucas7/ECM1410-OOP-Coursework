package socialmedia;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.*;

/**
 * Implementor of the SocialMediaPlatform interface.
 */
public class SocialMedia implements SocialMediaPlatform {
	/**
	 * The list of accounts on the platform.
	 */
    private ArrayList<Account> accountList = new ArrayList<Account>();
	/**
	 * The list of generic empty posts on the platform.
	 */
	private ArrayList<Post> emptyPostList = new ArrayList<Post>();

    @Override
	public int createAccount(String handle) throws IllegalHandleException, InvalidHandleException {
        Account.validateHandle(handle, accountList); 
        Account newAccount = new Account(handle);
		int numOfAccounts = getNumberOfAccounts();
        accountList.add(newAccount);
		assert (numOfAccounts + 1 == getNumberOfAccounts()) : "Number of accounts has not increased.";
        return newAccount.getID();
	}

	@Override
	public int createAccount(String handle, String description) throws IllegalHandleException, InvalidHandleException {
		Account.validateHandle(handle, accountList); 
		Account newAccount = new Account(handle, description);
		int numOfAccounts = getNumberOfAccounts();
        accountList.add(newAccount);
		assert (numOfAccounts + 1 == getNumberOfAccounts()) : "Number of accounts has not increased.";
        return newAccount.getID();
	}

	@Override
	public void removeAccount(int id) throws AccountIDNotRecognisedException {
		Account accountToDelete = Account.findAccountById(id, accountList);
		int numOfAccounts = getNumberOfAccounts();
		while (accountToDelete.getPosts().size()>0){ // Deletes all posts from the account
			try{
				Post p = accountToDelete.getPosts().get(0);
				deletePost(p.getID());
			} catch(PostIDNotRecognisedException e){
				continue;
			}
		}
		accountList.remove(accountToDelete);
		assert (numOfAccounts - 1 == getNumberOfAccounts()) : "Number of accounts has not decreased.";
	}

	@Override
	public void removeAccount(String handle) throws HandleNotRecognisedException {
		Account accountToDelete = Account.findAccountByHandle(handle, accountList);
		try{
			removeAccount(accountToDelete.getID());
		} catch (AccountIDNotRecognisedException e){

		}
	}

	@Override
	public void changeAccountHandle(String oldHandle, String newHandle)
			throws HandleNotRecognisedException, IllegalHandleException, InvalidHandleException {
        Account.validateHandle(newHandle, accountList);
		Account account = Account.findAccountByHandle(oldHandle, accountList);
		account.setHandle(newHandle);
		assert (account.getHandle() == newHandle) : "Handle has not updated.";

	}

	@Override
	public void updateAccountDescription(String handle, String description) throws HandleNotRecognisedException {
		Account account = Account.findAccountByHandle(handle, accountList);
		account.setDescription(description);
		assert (account.getDescription() == description):"Description has not updated.";
	}

	@Override
	public String showAccount(String handle) throws HandleNotRecognisedException {
		Account account = Account.findAccountByHandle(handle, accountList);
		return account.toString();
	}

	@Override
	public int createPost(String handle, String message) throws HandleNotRecognisedException, InvalidPostException {
		Account postingAccount = Account.findAccountByHandle(handle, accountList); 
		Post.validateMessage(message); 
		Post newPost = new Post(postingAccount, message);
		int numOfAccountPosts = postingAccount.getPosts().size();
		postingAccount.addPost(newPost);
		assert (numOfAccountPosts + 1 == postingAccount.getPosts().size()):"Account post count not updated.";
		
		return newPost.getID();
	}

	@Override
	public int endorsePost(String handle, int id)
			throws HandleNotRecognisedException, PostIDNotRecognisedException, NotActionablePostException {
			Account postingAccount = Account.findAccountByHandle(handle, accountList);
			Post postToEndorse = Post.findPostByID(id, accountList); 
			if (postToEndorse instanceof EndorsementPost){ // Cannot endorse an endorsement post
				throw new NotActionablePostException();
			}

			String message = "EP@" + postToEndorse.getAccount().getHandle() + ": " + postToEndorse.getMessage();
			int numOfEndorsements = getTotalEndorsmentPosts();
			EndorsementPost endorsementPost = new EndorsementPost(postingAccount, message, postToEndorse);
			assert (postToEndorse.getEndorsements().contains(endorsementPost)):"Endorsement post not added to list of endorsements.";
			assert (numOfEndorsements + 1 == getTotalEndorsmentPosts()):"Number of endorsement posts has not increased.";
			return endorsementPost.getID();
	}

	@Override
	public int commentPost(String handle, int id, String message) throws HandleNotRecognisedException,
			PostIDNotRecognisedException, NotActionablePostException, InvalidPostException {

		Account postingAccount = Account.findAccountByHandle(handle, accountList); 
		Post commentedPost = Post.findPostByID(id, accountList); 
		Post.validateMessage(message); 

		if (commentedPost instanceof EndorsementPost){ //Cannot comment on an endorsement post
			throw new NotActionablePostException();
		}

		int numOfComments = getTotalCommentPosts();
		Comment newComment = new Comment(postingAccount, message, commentedPost);
		assert (commentedPost.getComments().contains(newComment)):"Comment post not added to comment list.";
		assert (numOfComments + 1 == getTotalCommentPosts()):"Number of comment posts has not increased.";
		return newComment.getID();
	}

	@Override
	public void deletePost(int id) throws PostIDNotRecognisedException {
		Post postToDelete = Post.findPostByID(id, accountList); 
		if (postToDelete instanceof EndorsementPost){ //Removes the endorsement from the post that is endorsed
			((EndorsementPost)postToDelete).getReferencePost().removeEndorsement((EndorsementPost)postToDelete);
		}
		else{
			postToDelete.clearEndorsements();
			if (!(postToDelete instanceof Comment)){ // If post is an original post, account post count must be recalculated
				postToDelete.getAccount().setPostCountUpToDateToFalse();
			}
		}
		postToDelete.getAccount().setEndorsementCountUpToDateToFalse();
		postToDelete.getAccount().removePost(postToDelete);
		postToDelete.setPostToEmpty();
		emptyPostList.add(postToDelete);
		
	}

	@Override
	public String showIndividualPost(int id) throws PostIDNotRecognisedException {
		Post postToShow = Post.findPostByID(id, accountList, emptyPostList);
		return postToShow.toString();
	}

	
	private StringBuilder DFSChildren(Post originalPost, Post post, int indent, StringBuilder sb, ArrayList<Post> visited) throws PostIDNotRecognisedException{
		visited.add(post); // List of posts that have been visited by search

		if(post instanceof Comment){ 
			boolean isFirstComment;
			if (((Comment) post).getReferencePost().getComments().get(0).equals(post)){ //Checks whether post is the first comment under a post
				isFirstComment = true;
			} else{
				isFirstComment = false;
			}
			sb.append(Post.formatMessage(showIndividualPost(post.getID()), indent, isFirstComment));
		}	
		else{ //Post is an original post
			sb.append(showIndividualPost(post.getID())+"\n");
		}
		indent++;

		for(Comment c:post.getComments()){ //Recursively calls on all comments under the current post
			if(!visited.contains(c)){
				DFSChildren(originalPost, c, indent, sb, visited);
			}
		}
		return sb;
	}


	@Override
	public StringBuilder showPostChildrenDetails(int id)
			throws PostIDNotRecognisedException, NotActionablePostException {
		StringBuilder sb = new StringBuilder();
		Post postToShow = Post.findPostByID(id, accountList, emptyPostList);

		if (postToShow instanceof EndorsementPost){ //Cannot call method on endorsement posts
			throw new NotActionablePostException();
		}
		return DFSChildren(postToShow, postToShow, 0,sb,new ArrayList<Post>()); //recursively visits all comments under original post
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
			total += a.getEndorsementCount();
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
				if(mostEndorsedPost == null || p.getNumEndorsements() > mostEndorsedPost.getNumEndorsements()){
					mostEndorsedPost = p;
				}
			}
		}
		if (mostEndorsedPost == null){ // If there are no posts on the platform
			return 0;
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
		if (mostEndorsedAccount == null){ // If there are no accounts on the platform
			return 0;
		}
		return mostEndorsedAccount.getID();
	}

	@Override
	public void erasePlatform() {
		accountList.clear();
		emptyPostList.clear();
		Post.resetIdCount();
		Account.resetIdCount();
		assert (accountList.size() == 0) : "Account list not empty";
	}
	@Override
	public void savePlatform(String filename) throws IOException {
		try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))){
			Account[] accountArr = accountList.toArray(new Account[accountList.size()]); //Create an array of accountList
			out.writeObject(accountArr);
			Post[] emptyPostArr = emptyPostList.toArray(new Post[emptyPostList.size()]); //Create an array of emptyPostList
			out.writeObject(emptyPostArr);
			
			int nextAccountID = Account.getNextId(); // Saves account ID counters' value
			out.writeObject(nextAccountID);
			int nextPostID = Post.getNextId(); // Saves post ID counters' value
			out.writeObject(nextPostID);
		}

		File f = new File(filename);
		assert (f.isFile()) : "File has not been created";
	}

	@Override
	public void loadPlatform(String filename) throws IOException, ClassNotFoundException {
		try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))){
			Account[] accountArr = (Account[])in.readObject(); // Read as Array
			accountList = new ArrayList<>(Arrays.asList(accountArr)); // Cast to ArrayList

			Post[] emptyPostArr = (Post[])in.readObject();
			emptyPostList = new ArrayList<>(Arrays.asList(emptyPostArr));
			
			int nextAccountID = (int)in.readObject();
			Account.setNextId(nextAccountID);
			
			int nextPostID = (int)in.readObject();
			Post.setNextId(nextPostID);
			

		} 
	}
}