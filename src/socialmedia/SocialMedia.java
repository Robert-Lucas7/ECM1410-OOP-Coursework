package socialmedia;

import java.util.ArrayList;
import java.io.*;


public class SocialMedia implements SocialMediaPlatform {
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
		Account accountToDelete = Account.findAccountById(id, accountList);
		for (Post p: postList){
			//Remove posts
			if (p.getAccount().getID()== id){
				try {
					deletePost(p.getPostID());
				} catch (PostIDNotRecognisedException e){
					continue;
				}
			}
			//Remove endorsements
			ArrayList<EndorsementPost> endorsementsToRemove = new ArrayList<>();
			for (EndorsementPost e: p.getEndorsements()){
				if (e.getAccount().getID() == id){
					endorsementsToRemove.add(e);
				}
			}
			p.getEndorsements().removeAll(endorsementsToRemove);
			//Remove comments
			for (Comment c: p.getComments()){ 
				if (c.getAccount().getID()==id){
					try {
						deletePost(c.getPostID());
					} catch (PostIDNotRecognisedException e){
						continue;
					}
				}
			}
		}
		//Remove from account list
		accountList.remove(accountToDelete);
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
		postingAccount.getAccountPosts().add(newPost);
		postingAccount.incrementPostCount();
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
			EndorsementPost endorsementPost = new EndorsementPost(postingAccount, message, postToEndorse);
			postToEndorse.addEndorsementPost(endorsementPost);
			postingAccount.setEndorsementCountUpToDateToFalse();
			postingAccount.getAccountPosts().add(endorsementPost);
			postList.add(endorsementPost);
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

		Comment newComment = new Comment(postingAccount, message, commentedPost);
		commentedPost.addComment(newComment);
		postingAccount.getAccountPosts().add(newComment);
		postList.add(newComment);
		return newComment.getPostID();
	}

	@Override
	public void deletePost(int id) throws PostIDNotRecognisedException {
		Post postToDelete = Post.findPostByID(id, postList); //throws PostIDNotRecognizedException
		
		/*for(EndorsementPost e : postToDelete.getEndorsements()){
			e.setPostToEmpty();
		}
		*/
		postList.remove(postToDelete);
		postToDelete.getAccount().getAccountPosts().remove(postToDelete);
		
		if (!(postToDelete instanceof EndorsementPost)){
			postToDelete.getEndorsements().removeAll(postToDelete.getEndorsements()); //By passing by reference - clears the arraylist.
			if (!(postToDelete instanceof Comment)){ //If it is an original post, then ...
				postToDelete.getAccount().decrementPostCount();
			}
		}
		else{
			postToDelete.getAccount().setEndorsementCountUpToDateToFalse();
			((EndorsementPost)postToDelete).getReferencePost().getEndorsements().remove(postToDelete);
		}
		postToDelete.setPostToEmpty();
		
		
	}

	@Override
	public String showIndividualPost(int id) throws PostIDNotRecognisedException {
		Post postToShow = Post.findPostByID(id, postList);
		return "ID: "+ postToShow.getPostID() +
		"\nAccount: "+postToShow.getAccount().getHandle() +
		"\nNo. endorsements: "+postToShow.getNumEndorsements()+" | No. comments: "+postToShow.getNumComments()+ "\n" +
		postToShow.getMessage();
	}

	private StringBuilder formatMessage(String message, int indentLevel, boolean isFirstComment){
		StringBuilder sb = new StringBuilder();
		boolean isFirstLine = true;
		for(String line : message.split("\n")){
			if(isFirstLine){
				String firstLine = "";
				if (indentLevel != 0 && isFirstComment){
					firstLine += "    ".repeat(indentLevel-1)+ "|\n";
				}
				sb.append(firstLine+"    ".repeat(indentLevel-1)+"| > "+line+"\n");
				
				//remove tab here
				isFirstLine = false;
			}
			else{
				sb.append("    ".repeat(indentLevel)+line+"\n");
			}
		}
		return sb;
	}
	private StringBuilder DFSChildren(Post originalPost, Post post, int indent, StringBuilder sb, ArrayList<Post> visited) throws PostIDNotRecognisedException{
		visited.add(post);

		//Add indentation and pipes here...
		if(post instanceof Comment){
			if (((Comment) post).getReferencePost().getComments().get(0).equals(post)){
				sb.append(formatMessage(showIndividualPost(post.getPostID()), indent, true));
			} else{
				sb.append(formatMessage(showIndividualPost(post.getPostID()), indent, false));
			}
			
		}	
		else{
			sb.append(showIndividualPost(post.getPostID())+"\n");
		}
		System.out.println("Indent: "+indent+", PostID: "+post.getPostID());
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
		Post postToShow = Post.findPostByID(id, postList);

		if (postToShow instanceof EndorsementPost){
			throw new NotActionablePostException();
		}

		/*sb.append(showIndividualPost(id)); 
		for (Comment c: postToShow.getComments()){

		}*/
		//Come back to this
		return DFSChildren(postToShow, postToShow, 0,sb,new ArrayList<Post>());
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
		accountList.clear();
		postList.clear();
		Post.resetIdCount();
		Account.resetIdCount();

	}

	@Override
	public void savePlatform(String filename) throws IOException {
		try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))){
			out.writeObject(accountList);
			out.writeObject(postList);
		}
	}

	@Override

	public void loadPlatform(String filename) throws IOException, ClassNotFoundException {
		try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))){
			ArrayList obj = (ArrayList) in.readObject();
			if ((obj.size()>0) && (obj.get(0) instanceof Account)){
				accountList = (ArrayList<Account>) obj;
			} else {
				throw new ClassNotFoundException();
			}
			
			obj = (ArrayList) in.readObject();
			if ((obj.size()>0) && (obj.get(0) instanceof Post)){
				postList = (ArrayList<Post>) obj;
			} else {
				throw new ClassNotFoundException();
			}
		} 
	}
}
