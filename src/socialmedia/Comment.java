package socialmedia;

public class Comment extends Post{
    private Post referencePost;

    public Comment(Account account, String message, Post referencePost) {
        super(account, message);
        this.referencePost = referencePost;
    }

    public Post getReferencePost(){
        return referencePost;
    }
}
