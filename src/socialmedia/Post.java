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

    //get methods

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
            commentCount = total;
            commentCountUptoDate = true;
        }
        return commentCount;
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
    public boolean isEmptyPost(){
        return isEmptyPost;
    }


    //Setters
    /**
     * Updates the message of the post to provided message
     * @param message New text that the post object shows when printed
     */
    public void setMessage(String message){
        this.message = message;
    }
    /**
     * Sets the post to be an empty post. If a post is deleted it is set to empty. Its message is updated
     * to state that it has been deleted. Comments are still linked to the post 
     */
    public void setPostToEmpty(){
        this.isEmptyPost = true;
        this.account = null;
        this.message = "The original content was removed from the system and is no longer available.";
        //this.postID = -1;
    }
    public static void resetIdCount(){
        nextID = 1;
    }
    //Add to lists
    public void addComment(Comment c){
        comments.add(c);
        commentCountUptoDate = false;
    }
    public void addEndorsementPost(EndorsementPost e){
        endorsements.add(e);
    }
    //other
    public void clearEndorsements(){
        this.endorsements.clear();
    }
    public static void validateMessage(String message) throws InvalidPostException{
        if (message.length()>100 || message.isEmpty()){
            throw new InvalidPostException();
        }
    }
    public static Post findPostByID(int id, ArrayList<Account> accountList) throws PostIDNotRecognisedException{
        for (Account a : accountList){
            for(Post p : a.getPosts()){
                if (p.getID() == id){
                    return p;
                }
            }
            
        }
        throw new PostIDNotRecognisedException();
    }
    public static StringBuilder formatMessage(String message, int indentLevel, boolean isFirstComment){
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
    public void removeEndorsement(EndorsementPost e){
        endorsements.remove(e);
        this.getAccount().setEndorsementCountUpToDateToFalse();
    }
}
