package socialmedia;

import  java.util.ArrayList;

import java.io.Serializable;

public class Post implements Serializable{
    protected static int nextID = 1;
    protected Account account;
    protected int postID;
    protected String message;
    protected ArrayList<Comment> comments = new ArrayList<Comment>();
    protected ArrayList<EndorsementPost> endorsements = new ArrayList<EndorsementPost>(); //posts endorsing this instance of a post.
    protected boolean isEmptyPost;
    protected int commentCount;
    protected boolean commentCountUptoDate;

    /**
     * Creates an instance of the Post object. Assigns a message, an ID and an account to the post object.
     * @param account Account that is creating the post
     * @param message Text contained in the post
     */
    public Post(Account account, String message){
        this.message=message;
        this.account=account;
        postID=nextID++;
        isEmptyPost = false;
        commentCountUptoDate = false;
    }

    /**
     * Returns the account that created the post
     * @return Account that created the post
     */
    public Account getAccount(){
        return account;
    }
    /**
     * Returns the ID of the post
     * @return Unique ID of post
     */
    public int getID(){
        return postID;
    }
    /**
     * Returns the text message that the post contains
     * @return Text that the post contains
     */
    public String getMessage(){
        return message;
    }
    /**
     * Returns the total number of comments directly about the post
     * @return Number of comments the post has
     */
    public int getNumComments(){
        if (!commentCountUptoDate){
            int total = 0;
            for (Comment c : comments){
                if (!c.isEmptyPost()){
                    total +=1;
                }
            }
            commentCount = total; // Cache the calculated value
            commentCountUptoDate = true;
        }
        return commentCount;
    }
    /**
     * Sets the commentCoutUpToDate varaible to false
     */
    public void setCommentCountUptoDateToFalse(){ 
        commentCountUptoDate = false;
    }
    /**
     * Returns the total number of endorsement about the post
     * @return Number of endorsements about the post 
     */
    public int getNumEndorsements(){
        return endorsements.size();
    }

    /**
     * Returns the List of comments directly under the post
     * @return ArrayList of comment objects about the post
     */
    public ArrayList<Comment> getComments() {
        return comments;
    }

    /**
     * Returns the List of endorsement posts about the post
     * @return ArrayList of endorsementPost objects about the post
     */
    public ArrayList<EndorsementPost> getEndorsements() {
        return endorsements;
    }
    /**
     * Returns the next sequential ID that will be assigned to the next post that is created
     * @return ID to be used by the next post that is created
     */
    public static int getNextId(){
        return nextID;
    }

    /**
     * States whether the post is an empty post. An empty post is a post that
     * has been deleted so no longer has a message
     * @return Boolean value stating whether the post is empty or not
     */
    public boolean isEmptyPost(){ 
        return isEmptyPost;
    }

    /**
     * Sets the next sequential ID, that will be assinged to the next post that is created, to the value
     * passed in
     * @param id The value to assign to nextID
     */
    public static void setNextId(int id){
        nextID=id;
    }
    /**
     * Updates the message of the post to provided message
     * @param message New text that the post object shows when printed
     */
    public void setMessage(String message){
        this.message = message;
    }
    /**
     * Sets the post to be an empty post. If a post is deleted, it is set to empty. Its message is updated
     * to state that it has been deleted. Comments are still linked to the post 
     */
    public void setPostToEmpty(){
        this.isEmptyPost = true;
        this.account = null;
        this.message = "The original content was removed from the system and is no longer available.";
    }
    /**
     * Restets the static ID count of the Post class so that the next post created will have an ID of 1
     */
    public static void resetIdCount(){
        nextID = 1;
    }
    /**
     * Add a comment to the list of comments about the post
     * @param c Comment object about the post
     */
    public void addComment(Comment c){
        comments.add(c);
        commentCountUptoDate = false;
    }
    /**
     * Add an endorsement post to the list of endorsements about the post
     * @param e Endorsement object about the post
     */
    public void addEndorsementPost(EndorsementPost e){
        endorsements.add(e);
    }

    /**
     * Removes a single EndorsementPost object from the list of edorsements stored in a post object
     * @param e The EndorsementPost object that is being removed
     */
    public void removeEndorsement(EndorsementPost e){
        endorsements.remove(e);
    }
    /**
     * Clears the list of endorsements stored in the post Object
     */
    public void clearEndorsements(){
        this.endorsements.clear();
    }

    /**
     * Checks that the message meets the requirements to be posted. The message must be
     * less than 100 characters and must not be empty
     * @param message Text that is being checked 
     * @throws InvalidPostException If the message is empty or if it is longer than 100 characters
     */
    public static void validateMessage(String message) throws InvalidPostException{
        if (message.length()>100 || message.isEmpty()){
            throw new InvalidPostException();
        }
    }
    /**
     * Searches through all of the posts stored in accounts and all of the empty posts stored in an emptyPostList.
     * Returns the Post object that has the given ID
     * @param id ID of the post to be returned 
     * @param accountList List of accounts stored in SocialMedia
     * @param emptyPostList List of empty posts 
     * @return Post that has the ID that is passed in
     * @throws PostIDNotRecognisedException There is no post that has the given ID
     */
    public static Post findPostByID(int id, ArrayList<Account> accountList, ArrayList<Post> emptyPostList) throws PostIDNotRecognisedException{
        for (Account a : accountList){ // Searches through posts stored in accounts
            for(Post p : a.getPosts()){
                if (p.getID() == id){
                    return p;
                }
            }
        }
        for (Post p : emptyPostList){ // Searches through posts stored in emptyPostList
            if (p.getID() == id){
                return p;
            }
        }
        throw new PostIDNotRecognisedException();
    }
    /**
     * Searches through all of the posts stored in accounts and returns the Post object that has the given ID
     * @param id ID of the post to be returned 
     * @param accountList List of accounts stored in SocialMedia
     * @return Post that has the ID that is passed in
     * @throws PostIDNotRecognisedException There is no post that has the given ID
     */
    public static Post findPostByID(int id, ArrayList<Account> accountList) throws PostIDNotRecognisedException{
        for (Account a : accountList){ // Searches through posts stored in accounts
            for(Post p : a.getPosts()){
                if (p.getID() == id){
                    return p;
                }
            }
            
        }
        throw new PostIDNotRecognisedException();
    }
    
    /**
     * Formats the text to be printed by the showPostChildrenDetails method. Adds |> before the first line 
     * and some number of indents before each new line so that the message is indented when printed. If it is 
     * the first comment of a post a blank line containing a single | is added about. So if the text passed
     *  in is the first comment of a post it returns:
     * 
     * <pre>
     *     |
     *     | > ID: 1
     *     Account: user1
     *     No. endorsements: 0 | No. comments: 0
     *     This is the first comment
     * </pre>
     * 
     * @param message The description of the post that is being formatted
     * @param indentLevel The number of times the message is indented 
     * @param isFirstComment Boolean value stating whether the comment is the first comment of a post
     * @return New string which is the indented message with the appropriate symbols | > before the first line
     */
    public static StringBuilder formatMessage(String message, int indentLevel, boolean isFirstComment){
		StringBuilder sb = new StringBuilder();
		boolean isFirstLine = true;
		for(String line : message.split("\n")){ //Splits message into individual lines
			if(isFirstLine){ 
				String firstLine = "";
				if (indentLevel != 0 && isFirstComment){ 
					firstLine += "    ".repeat(indentLevel-1)+ "|\n"; // Add empty line with | 
				}
				sb.append(firstLine+"    ".repeat(indentLevel-1)+"| > "+line+"\n"); //Indent line and add '| >' before first line
				isFirstLine = false;
			}
			else{ // not first line
				sb.append("    ".repeat(indentLevel)+line+"\n"); //Indent line
			}
		}
		return sb;
	}
    /**
     * Returns a string containing the information about a post. This includes the ID of the post, the account
     * that the post belongs to and the number of endorsements and number of comments that the post has. It will 
     * return a string in the form below.
     * <pre>
     * ID: 1
     * Account: user1
     * No. endorsements: 2 | No. comments: 1
     * This is a post 
     * </pre>
     * <p>
     * An empty post, which has been deleted will display a message stating that it has been deleted.
     * <pre>
     * ----------------------------------------------------------------------------
     * The original content was removed from the system and is no longer available.
     * ----------------------------------------------------------------------------
     * </pre>
     * @return String of information about the post
     */
    @Override
    public String toString(){ 
        if (isEmptyPost){
            return "-".repeat(message.length())+"\n"+message+"\n"+"-".repeat(message.length())+"\n";
        }
        return "ID: "+ postID +
		"\nAccount: "+ account.getHandle()+
		"\nNo. endorsements: "+ getNumEndorsements()+" | No. comments: "+ getNumComments()+ "\n" +
		message;
    }
}