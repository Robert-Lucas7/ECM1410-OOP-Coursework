package socialmedia;
/**
 * Represents an endorsement post on the platform.
 */
public class EndorsementPost extends Post{

    /**
     * The post object that the endorsement post is endorsing.
     */
    private Post referencePost;
    /**
     * Creates an instance of a EndorsementPost object, by first calling the post
     * superclass constructor and then assigning a value for reference post.
     * @param account the account that is posting the endorsement.
     * @param message The text shown in the post body. This will contain the original post message 
     * @param referencePost The post that is being endorsed
     */
    public EndorsementPost(Account account, String message, Post referencePost) {
        super(account, message);
        this.referencePost = referencePost;
        referencePost.addEndorsementPost(this);
		referencePost.getAccount().setEndorsementCountUpToDateToFalse();//postingAccount.setEndorsementCountUpToDateToFalse();
		account.addPost(this);
    }
    /**
     * Returns the post object that the endorsement referes to
     * @return Post that endorsement referes to
     */
    public Post getReferencePost(){
        return this.referencePost;
    }
}
