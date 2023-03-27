package socialmedia;

import java.util.ArrayList;

import java.io.Serializable;

class Account implements Serializable{
    private static int nextID = 1;
    private String handle;
    private String description;
    private int accountID;
    private int postCount = 0;
    private int endorsementCount = 0;
    private boolean postCountUpToDate = false;
    private boolean endorsementCountUpToDate = false;
    private final static int MAX_HANDLE_LENGTH = 30; 

    private ArrayList<Post> accountPosts = new ArrayList<Post>();//All posts - original, comments, endorsements.
    
    /**
     * Creates an instance of an Account object and calls the overloaded constructor with the handle and an empty description.
     * @param handle The handle of the account to be created.
     */
    public Account(String handle){
        this(handle, "");

    }
    /**
     * Creates an instance of an account object by assigning the handle and description to the respective parameters. Also, the accountID is assigned
     * to the next  sequential ID. 
     * @param handle The handle to be associated with the account.
     * @param description The description to be associated with the account.
     */
    public Account(String handle, String description){
        this.handle = handle;
        this.description = description;
        accountID = nextID++;
    }
    /**
     * Returns the handle associated with the account.
     * @return The handle of the account.
     */
    public String getHandle(){
        return handle;
    }
    /**
     * Returns the decscription associated with the account.
     * @return The description of the account.
     */
    public String getDescription(){
        return description;
    }
    /**
     * Returns the ID associated with the account.
     * @return The ID related with the account.
     */
    public int getID(){
        return accountID;
    }
    /**
     * Returns the number of original posts (excluding comments and endorsements) the account has made.
     * @return The number of original posts on the account.
     */
    public int getOriginalPostCount(){
        if(!postCountUpToDate){
            int total=0;
            for(Post p:accountPosts){
                if(!(p instanceof Comment || p instanceof EndorsementPost || p.isEmptyPost())){
                    total += 1;
                }
            }
            postCount = total;
            postCountUpToDate = true;
        }
        return postCount;
    }
    /**
     * Returns the number of endorsements for posts that are made for the account.
     * @return The total number of posts that are endorsing the posts made by the account.
     */
    public int getEndorsementCount(){
        if(!endorsementCountUpToDate){
            //count the number of endorsements
            int total = 0;
            for(Post p:accountPosts){
                total += p.getNumEndorsements();
            }
            endorsementCount = total;
            endorsementCountUpToDate = true;
        }
        return endorsementCount;
    }
    /**
     * Returns the number of posts made by the account.
     * @return The number of posts made by the account.
     */
    public int getTotalPostCount(){ 
        //Includes posts, comments and endorsements
        int total = 0;
        for (Post p : accountPosts){
            if (!p.isEmptyPost()){
                total+=1;
            }
        }
        return total;
    }
    public ArrayList<Post> getPosts(){
        return this.accountPosts;
    } 
    
    //Setters
    /**
     * Sets the handle of the account to a new value.
     * @param newHandle The value to change the handle of the account to.
     */
    public void setHandle(String newHandle){
        handle = newHandle;
    }
    /**
     * Sets the description of the account to a new value.
     * @param newDescription The value to change the description of the account to.
     */
    public void setDescription(String newDescription){
        description = newDescription;
    }
    /**
     * Resets the sequential ID to a value of 1.
     */
    public static void resetIdCount(){
        nextID = 1;
    }
    /**
     * Set the endorsementCountUpToDate attribute to false, so when the getEndorsementCount() method is called the number of endorsements related to
     * the account is recalculated.
     */
    public void setEndorsementCountUpToDateToFalse(){
        this.endorsementCountUpToDate = false;
    }
    public void setPostCountUpToDateToFalse(){
        this.postCountUpToDate = false;
    }
    //other
    /**
     * Adds a post to the list of posts that the account has made.
     * @param p The post to be added to the list of posts that the account has made.
     */
    public void addPost(Post p){
        this.accountPosts.add(p);
        if (!(p instanceof Comment || p instanceof EndorsementPost)){
            postCountUpToDate = false;
        }
        
    }
    //================================================== I DONT THINK THIS METHOD IS NEEDED ANYMORE ================================================================================
    /**
     * Removes the post (specified by parameter p) from the list of posts that the account has made.
     * @param p The post to be removed from the list of posts that the account has made.
     */
    public void removePost(Post p){
        this.accountPosts.remove(p);
        if (!(p instanceof Comment || p instanceof EndorsementPost)){
            postCountUpToDate = false;
        }
    }
    //===================================================================================================================================
    /**
     * Returns the account object represented as a String.
     */
    @Override
    public String toString(){
        return	"ID: " + accountID +
                "\nHandle: " + handle +
                "\nDescription: " + description +
                "\nPost count: " + getTotalPostCount() +
                "\nEndorse count: " + getEndorsementCount();
    }
    //Validation for Handle
    /**
     * Validates a string to be used as the handle of an account.
     * @param handle The string to be used as the handle of an account.
     * @param accountList An ArrayList of account objects.
     * @throws InvalidHandleException This is thrown when the handle is more than 30 characters, or is an empty string, or contains any whitespace.
     * @throws IllegalHandleException This is thrown when an account already contains the handle defined in the parameter handle.
     */
    public static void validateHandle(String handle, ArrayList<Account> accountList) throws InvalidHandleException, IllegalHandleException{
        if(handle.length() > MAX_HANDLE_LENGTH || handle.isEmpty() || handle.matches("(\\s)+")){//Add check for whitespace
            throw new InvalidHandleException();
        }
        for(Account a : accountList){
            if(a.getHandle().equals(handle)){
                throw new IllegalHandleException();
            }
        }
    }
    /**
     * Returns the account with the handle defined by parameter handle if it exists in the ArrayList accounts, otherwise a HandleNotRecognisedException is thrown.
     * @param handle The account with the handle to find.
     * @param accounts An ArrayList of Account objects.
     * @return The account object that has the handle that is equal to the parameter handle.
     * @throws HandleNotRecognisedException This is thrown if no account has a handle defined by the parameter handle in the ArrayList of accounts (parameter accounts).
     */
    public static Account findAccountByHandle(String handle, ArrayList<Account> accounts) throws HandleNotRecognisedException{
        for (Account a : accounts){
            if (a.getHandle().equals(handle)){
                return a;
            }
        }
        throw new HandleNotRecognisedException();
    }
    /**
     * Returns the account with the ID defined by the parameter id if it contained in the ArrayList of accounts, otherwise an AccountIDNotRecognisedException is thrown.
     * @param id The ID of the account to find.
     * @param accounts An ArrayList of Accounts.
     * @return The account with the ID that is equal to the parameter id.
     * @throws AccountIDNotRecognisedException This is thrown if no account in the ArrayList of accounts that has an ID that is equal to the parameter id.
     */
    public static Account findAccountById(int id, ArrayList<Account> accounts) throws AccountIDNotRecognisedException{
        for (Account a : accounts){
            if (a.getID() == id){
                return a;
            }
        }
        throw new AccountIDNotRecognisedException();
    }


}