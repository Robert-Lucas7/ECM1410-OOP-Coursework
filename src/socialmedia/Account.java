package socialmedia;

import java.util.ArrayList;

import java.io.Serializable;

/**
 * Represents an account on the platform.
 */
public class Account implements Serializable{
    /**
     * ID assigned to next account to be created
     */
    private static int nextID = 1;
    /**
     * The handle associated with the account
     */
    private String handle;
    /**
     * The description associated with the account 
     */
    private String description;
    /**
     * The unique integer ID associated with the account
     */
    private int accountID;
    /**
     * Number of original posts posted by the account
     */
    private int postCount = 0;
    /**
     * Number of endorsements refering to a post posted by this account
     * */
    private int endorsementCount = 0;
    /**
     * A flag to indicate whether the postCount attribute is up to date or should be recalculated
     */
    private boolean postCountUpToDate = false;
    /**
     * A flag to indicate whether the endorsement attribute is up to date or should be recalculated
     */
    private boolean endorsementCountUpToDate = false;
    /**
     * A constant thats value indicates the maximum number of characters a handle can be
     */
    private final static int MAX_HANDLE_LENGTH = 30; 
    /**
     * All posts posted by the account (original, comments and endorsements).
     */
    private ArrayList<Post> accountPosts = new ArrayList<Post>();
    
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
                if(!(p instanceof Comment || p instanceof EndorsementPost)){
                    total += 1;
                }
            }
            postCount = total; // cache the calculated value
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
            int total = 0;
            for(Post p:accountPosts){
                total += p.getNumEndorsements();
            }
            endorsementCount = total; // Cache the calculated value
            endorsementCountUpToDate = true;
        }
        return endorsementCount;
    }
    /**
     * Returns the number of posts made by the account. This includes original posts, comments and endorsements
     * @return The number of posts made by the account.
     */
    public int getTotalPostCount(){ 
        return accountPosts.size();
    }
    /**
     * Returns the ArrayList accountPosts which contains all of the posts created by the account. This includes
     * original posts, comments and endorsements.
     * @return ArrayList of posts created by the account
     */
    public ArrayList<Post> getPosts(){
        return accountPosts;
    } 
    /**
     * Returns the next sequential ID that will be assigned to the next account to be created
     * @return ID to be used by the next account to be created
     */
    public static int getNextId(){
        return nextID;
    }
    
    //Setters
    /**
     * Sets the next sequential ID, that will be assinged to the next account to be created, to the value
     * passed in
     * @param id The value to assign to nextID
     */
    public static void setNextId(int id){
        nextID=id;
    }
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
    /**
     * Sets the boolean value of postCountUpToDate to false. postCountUpToDate states whether the count of original posts
     * is up to date.
     */
    public void setPostCountUpToDateToFalse(){
        this.postCountUpToDate = false;
    }
    /**
     * Adds a post to the list of posts that the account has made.
     * @param p The post to be added to the list of posts that the account has made.
     */
    public void addPost(Post p){
        this.accountPosts.add(p);
        if (!(p instanceof Comment || p instanceof EndorsementPost)){ //If post is original post
            postCountUpToDate = false;
        }
        
    }
    /**
     * Removes the post (specified by parameter p) from the list of posts that the account has made.
     * @param p The post to be removed from the list of posts that the account has made.
     */
    public void removePost(Post p){
        this.accountPosts.remove(p);
        if (!(p instanceof Comment || p instanceof EndorsementPost)){ // If post is original post
            postCountUpToDate = false;
        }
    }

    /**
     * Returns a string that contains information about an account including its unique account ID, the handle
     * of the account, the description of the account, the number of posts the account has created (original posts, 
     * comments and endorsements) and the number of endorsement posts that refer to a post created by this account
     * 
     * <pre>
     * ID: 1
     * Handle: user1
     * Description: This is my description
     * Post count: 1
     * Endorse count: 1
     * </pre>
     */
    @Override
    public String toString(){
        return	"ID: " + accountID +
                "\nHandle: " + handle +
                "\nDescription: " + description +
                "\nPost count: " + getTotalPostCount() +
                "\nEndorse count: " + getEndorsementCount();
    }
    /**
     * Validates a string to be used as the handle of an account by checking that it is less than 30 characters, not empty, contains 
     * no white space and is not being used by any other account in the system.
     * @param handle The string to be used as the handle of an account.
     * @param accountList An ArrayList of account objects.
     * @throws InvalidHandleException This is thrown when the handle is more than 30 characters, or is an empty string, or contains any whitespace.
     * @throws IllegalHandleException This is thrown when an account already contains the handle defined in the parameter handle.
     */
    public static void validateHandle(String handle, ArrayList<Account> accountList) throws InvalidHandleException, IllegalHandleException{
        if(handle.length() > MAX_HANDLE_LENGTH || handle.isEmpty() || handle.matches("(\\s)+")){ //Checks string is not empty, less than 30 characters and contains no whitespace
            throw new InvalidHandleException();
        }
        for(Account a : accountList){ // Checks if any account already owns this handle
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