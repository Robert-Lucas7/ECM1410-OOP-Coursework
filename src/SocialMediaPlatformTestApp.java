import socialmedia.AccountIDNotRecognisedException;
import socialmedia.SocialMedia;
import socialmedia.IllegalHandleException;
import socialmedia.InvalidHandleException;
import socialmedia.SocialMediaPlatform;

/**
 * A short program to illustrate an app testing some minimal functionality of a
 * concrete implementation of the SocialMediaPlatform interface -- note you will
 * want to increase these checks, and run it on your SocialMedia class (not the
 * BadSocialMedia class).
 *
 * 
 * @author Diogo Pacheco
 * @version 1.0
 */
public class SocialMediaPlatformTestApp {

	/**
	 * Test method.
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		System.out.println("The system compiled and started the execution...");

		SocialMediaPlatform platform = new SocialMedia();

		assert (platform.getNumberOfAccounts() == 0) : "Innitial SocialMediaPlatform not empty as required.";
		assert (platform.getTotalOriginalPosts() == 0) : "Innitial SocialMediaPlatform not empty as required.";
		assert (platform.getTotalCommentPosts() == 0) : "Innitial SocialMediaPlatform not empty as required.";
		assert (platform.getTotalEndorsmentPosts() == 0) : "Innitial SocialMediaPlatform not empty as required.";

		
		try {
			int eID = platform.createAccount("elliot");
			assert (platform.getNumberOfAccounts() == 1) : "number of accounts registered in the system does not match";
			int robID = platform.createAccount("rob");

			int id1 = platform.createPost("elliot", "hello");
			//int id6 = platform.createPost("elliot", "hello again");
			int id2 = platform.endorsePost("rob", id1);
			int id3 = platform.commentPost("rob", id1, "good comment");
			int id4 = platform.commentPost("rob", id1, "brilliant comment");
			int id5 = platform.commentPost("rob", id3, "great comment");
			//platform.commentPost("elliot", 2, "bad comment");
			//platform.deletePost(eID);
			System.out.println(platform.showAccount("elliot"));
			
			//platform.deletePost(id1);
			platform.commentPost("rob", id1, "testing this ");
			//int id7 = platform.endorsePost("rob", id1);
			//platform.deletePost(id6);
			System.out.println();
			System.out.println(platform.showAccount("elliot"));
			//System.out.println(platform.showIndividualPost(eID));
			System.out.println();
			//System.out.println(platform.showIndividualPost(cID));
			System.out.println();
			System.out.println();
			//platform.endorsePost("elliot", 5);
			platform.deletePost(id3);
			System.out.println(platform.showPostChildrenDetails(id1));
			

			//platform.updateAccountDescription("elliot", "I'm not smelly");
			//platform.changeAccountHandle("elliot", "smelliot");
			///Saving and loading platform
			/* 
			platform.savePlatform("saved.ser");
			System.out.println("Saved");
			platform.erasePlatform();
			platform.loadPlatform("saved.ser");
			System.out.println("Loaded");
			System.out.println(platform.showAccount("smelliot"));
			*/
			


		} catch (IllegalHandleException e) {
			assert (false) : "IllegalHandleException thrown incorrectly";
		} catch (InvalidHandleException e) {
			assert (false) : "InvalidHandleException thrown incorrectly";
		} /*catch (AccountIDNotRecognisedException e) {
			assert (false) : "AccountIDNotRecognizedException thrown incorrectly";
		}*/
		catch(Exception e){
			e.printStackTrace();
		}
		


	}

}
