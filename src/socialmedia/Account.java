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
    
    public Account(String handle){
        this(handle, "");

    }
    public Account(String handle, String description){
        this.handle = handle;
        this.description = description;
        accountID = nextID++;
    }

    public String getHandle(){
        return handle;
    }
    public String getDescription(){
        return description;
    }
    public int getID(){
        return accountID;
    }
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
    public int getEndorsementCount(){//using caching OR Lazy instantiation??
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
    public ArrayList<Post> getPosts(){
        return this.accountPosts;
    } 
    
    //Setters
    public void setHandle(String newHandle){
        handle = newHandle;
    }
    public void setDescription(String newDescription){
        description = newDescription;
    }
    public static void resetIdCount(){
        nextID = 1;
    }
    public void setEndorsementCountUpToDateToFalse(){
        this.endorsementCountUpToDate = false;
    }
    //other
    public void addPost(Post p){
        this.accountPosts.add(p);
        if (!(p instanceof Comment || p instanceof EndorsementPost)){
            postCountUpToDate = false;
        }
        
    }
    public void removePost(Post p){
        this.accountPosts.remove(p);
        if (!(p instanceof Comment || p instanceof EndorsementPost)){
            postCountUpToDate = false;
        }
    }
    @Override
    public String toString(){
        return	"ID: " + accountID +
                "\nHandle: " + handle +
                "\nDescription: " + description +
                "\nPost count: " + accountPosts.size() +
                "\nEndorse count: " + getEndorsementCount();
    }
    //Validation for Handle
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
    public static Account findAccountByHandle(String handle, ArrayList<Account> accounts) throws HandleNotRecognisedException{
        for (Account a : accounts){
            if (a.getHandle().equals(handle)){
                return a;
            }
        }
        throw new HandleNotRecognisedException();
    }

    public static Account findAccountById(int id, ArrayList<Account> accounts) throws AccountIDNotRecognisedException{
        for (Account a : accounts){
            if (a.getID() == id){
                return a;
            }
        }
        throw new AccountIDNotRecognisedException();
    }


}