package socialmedia;

/**
 * Represents a comment post on the platform.
 */
public class Comment extends Post{
    /**
     * The post object that the comment is under.
     */
    private Post referencePost;

    /**
     * Creates an instance of a Comment object, by first calling the post
     * superclass constructor and then assigning a value for reference post.
     * @param account the account that is creating the comment.
     * @param message the text contained in the comment object.
     * @param referencePost the post object that the comment refers to.
     */
    public Comment(Account account, String message, Post referencePost) {
        super(account, message);
        this.referencePost = referencePost;
        referencePost.addComment(this);
		account.addPost(this);
    }
    
    /** This returns the post object that the comment refers to.
     * @return Post that the comment refers to.
     */
    public Post getReferencePost(){
        return referencePost;
    }
}
