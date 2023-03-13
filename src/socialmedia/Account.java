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
    private final static int MAX_HANDLE_LENGTH = 30; 

    public Account(String handle){
        this(handle, "");

    }
    public Account(String handle, String description){
        this.handle = handle;
        this.description = description;
        accountID =nextID++;
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
    public static void resetIdCount(){
        nextID = 1;
    }
    //other
    public void createPost(){
        
    }
    //Validation for Handle
    public static void validHandle(String handle, ArrayList<Account> accountList) throws InvalidHandleException, IllegalHandleException{
        if(handle.length() > MAX_HANDLE_LENGTH || handle.isEmpty() || !handle.matches("(\\S)+")){//Add check for whitespace
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
            if (a.getID()==id){
                return a;
            }
        }
        throw new AccountIDNotRecognisedException();
    }


}