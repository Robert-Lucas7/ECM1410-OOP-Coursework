public class Account{
    private String handle;
    private String description;
    private int accountID;
    private int postCount = 0;
    private int endorsementCount = 0;

    public Account(String handle){
        this(handle, "");
    }
    public Account(String handle, String description){
        this.handle = handle;
        this.description = description;
    }

    //Getters
    public String getHandle(){
        return handle;
    }
    public String getDescription(){
        return description;
    }
    public int getID(){
        return accountID;
    }
    public int getPostCount(){
        return postCount;
    }
    public int getEndorsementCount(){
        return endorsementCount;
    }
    //Setters
    public void setHandle(String newHandle){
        handle = newHandle;
    }
    public void setDescription(String newDescription){
        description = newDescription;
    }
    //other
    public void createPost(){
        
    }


}