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
			// Deleting accounts doesn't work properly when trying to print posts
			//
			//

			int eID = platform.createAccount("elliot");
			assert (platform.getNumberOfAccounts() == 1) : "number of accounts registered in the system does not match";
			int robID = platform.createAccount("rob");

			int id1 = platform.createPost("elliot", "hello");
			//platform.createPost("elliot", "second message");
			int id8= platform.createPost("rob", "third message");
			//int id6 = platform.createPost("elliot", "hello again");

			int id3 = platform.commentPost("rob", id1, "good comment");
			int id4 = platform.commentPost("rob", id1, "brilliant comment");
			int id5 = platform.commentPost("rob", id3, "great comment");

			int id2 = platform.endorsePost("rob", id1);
			int id7 = platform.endorsePost("rob", id1);
			platform.endorsePost("elliot", id8);


			System.out.println();
			System.out.println(platform.showAccount("elliot"));
			//System.out.println(platform.showIndividualPost(eID));
			System.out.println();
			System.out.println(platform.showAccount("rob"));
			//System.out.println(platform.showIndividualPost(cID));
			System.out.println();
			System.out.println();
			//platform.deletePost(id2);
			//platform.deletePost(id7);

			//platform.endorsePost("elliot", 5);
			//platform.deletePost(id3);
			platform.removeAccount("elliot");
			System.out.println(platform.showPostChildrenDetails(id1));
			//System.out.println(platform.showIndividualPost(id1));
			System.out.println();
			System.out.println();
			System.out.println("Num comments:"+ platform.getTotalCommentPosts());
			System.out.println("Num of endorsements:"+ platform.getTotalEndorsmentPosts());
			System.out.println("Num of original posts: "+ platform.getTotalOriginalPosts());
			System.out.println("Num Accounts:"+ platform.getNumberOfAccounts());
			System.out.println("Most endorsed account: " + platform.getMostEndorsedAccount());
			System.out.println("Most endorsed Post: " + platform.getMostEndorsedPost());
			System.out.println();
			System.out.println();
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
