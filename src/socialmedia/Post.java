package socialmedia;

import  java.util.ArrayList;
public class Post {
    protected static int nextID = 1;
    protected Account account;
    protected int postID;
    protected String message;
    protected ArrayList<Post> comments = new ArrayList<Post>();
    protected ArrayList<Post> endorsements = new ArrayList<Post>();

    public Post(Account account, String message){
        this.message=message;
        this.account=account;
        postID=nextID++;

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
    public ArrayList<Post> getComments() {
        return comments;
    }
    //Add to lists
    public void addComment(Comment c){
        comments.add(c);
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
