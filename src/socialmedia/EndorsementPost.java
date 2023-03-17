package socialmedia;

public class EndorsementPost extends Post{

    private Post referencePost;
    public EndorsementPost(Account account, String message, Post referencePost) {
        super(account, message);
        this.referencePost = referencePost;
    }
    public Post getReferencePost(){
        return this.referencePost;
    }
}
