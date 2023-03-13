package socialmedia;

import  java.util.ArrayList;

import java.io.Serializable;

public class Post implements Serializable{
    protected static int nextID = 1;
    protected Account account;
    protected int postID;
    protected String message;
    protected ArrayList<Comment> comments = new ArrayList<Comment>();
    protected ArrayList<EndorsementPost> endorsements = new ArrayList<EndorsementPost>();
    protected boolean isEmptyPost;

    /*public Post(){
        this.message = "The original content was removed from the system and is no longer available.";
    }*/

    public Post(Account account, String message){
        this.message=message;
        this.account=account;
        postID=nextID++;
        isEmptyPost = false;
    }

    //get methods
    public Account getAccount(){
        return account;
    }
    public int getPostID(){
        return postID;
    }
    public String getMessage(){
        return message;
    }
    public int getNumComments(){
        return comments.size();
    }
    public int getNumEndorsements(){
        return endorsements.size();
    }
    public ArrayList<Comment> getComments() {
        return comments;
    }
    public ArrayList<EndorsementPost> getEndorsements() {
        return endorsements;
    }
    //Setters
    public void setMessage(String message){
        this.message = message;
    }
    public void setPostToEmpty(){
        this.isEmptyPost = true;
        this.account = null;
        this.message = "The original content was removed from the system and is no longer available.";
        this.postID = -1;
    }
    public static void resetIdCount(){
        nextID = 1;
    }
    //Add to lists
    public void addComment(Comment c){
        comments.add(c);
    }
    public void addEndorsementPost(EndorsementPost e){
        endorsements.add(e);
    }
    //other
    public static void validateMessage(String message) throws InvalidPostException{
        if (message.length()>100 || message.isEmpty()){
            throw new InvalidPostException();
        }
    }
    public static Post findPostByID(int id, ArrayList<Post> posts) throws PostIDNotRecognisedException{
        for (Post p : posts){
            if (p.getPostID() == id){
                return p;
            }
        }
        throw new PostIDNotRecognisedException();
    }
}
