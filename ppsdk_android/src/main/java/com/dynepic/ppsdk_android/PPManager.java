package com.dynepic.ppsdk_android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.dynepic.ppsdk_android.fragments.ssoLoginFragment;
import com.dynepic.ppsdk_android.models.User;
import com.dynepic.ppsdk_android.utils._DevPrefs;
import com.dynepic.ppsdk_android.utils._DialogFragments;
import com.dynepic.ppsdk_android.utils._UserPrefs;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import okhttp3.Interceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dynepic.ppsdk_android.utils._WebApi.getApi;


/**
 * This class should handle most of the activity within the app.

 * Configure PPManager
 * Show SSO Login Form
 * Get Buckets
 * Set Buckets
 * Get User
 * Set User

 ===============
 = Basic Usage =
 ===============

 * Initialize your activity's context. Content cannot be referenced from static context.
 * Requires CONTEXT and ACTIVITY_CONTEXT
 *
 * Activity context is normally 'this', or references the controlling activity.
 * The same applies for normal context.
 * IE:
 * 	  Activity ACTIVITY_CONTEXT = this;
 * 	  Context CONTEXT = this;

 PPManager ppManager = new PPManager(CONTEXT, ACTIVITY_CONTEXT);


 * Configure your Client ID, Client Secret, and Redirect URL.
 * This can be checked using isConfigured() - see below.

 ppManager.configure("CLIENT_ID","CLIENT_SECRET","REDIRECT_URL")


 * Changing config settings is done through ppManager.getConfiguration()
 * Make sure that ppManager is instantiated.
 * All config settings should be changed this way.

 ppManager.getConfiguration().setClientRedirect("REDIRECT_STRING")


 * Call the Single-Sign-On Login page
 * The intent indicates which activity you want to go to after passing the SSO Login

 ppManager.showSSOLogin(INTENT)


 * Check to see if the PPManager has been configured at launch
 * This is normally a good check in case the user has cleared application data.
 * Returns TRUE if the id, secret, and redirect-url are all non-null AND non-empty strings.

 ppManager.isConfigured()


 * Getting logged in user data is done through get and set methods
 * Returns a string value of the user data requested
 * User Data should be handled this way:

 ppManager.getUserData().getValue();
 ppManager.getUserData().setValue();



 ===================
 = Friends Request =
 ===================

 * In your controlling activity or class, you need to specify "implements"

 public class YOUR_CLASS_NAME implements PPManager.FriendsService.onFriendsResponse {}

 * Create request, specify activity, delegate, execute request

 PPManager.FriendsService.getFriends friendsRequest = new PPManager.FriendsService.getFriends(ACTIVITY);
 friendsRequest.delegate = this;
 friendsRequest.execute((Void) null);

 * Create @Override method to get results after the Async Call has been completed
 * Returns an ArrayList of Strings of user Handles

 /@Override
 public void onFriendsResponse(ArrayList<String> output) {
    //update your UI based on response
 }

 *
 */


public class PPManager {

	private Context CONTEXT;
	private Activity ACTIVITY;
	private _DevPrefs devPrefs;
	private _UserPrefs userPrefs;

	public PPManager(Context context, Activity activity){
		CONTEXT = context;
		ACTIVITY = activity;
		devPrefs = new _DevPrefs(CONTEXT);
		userPrefs = new _UserPrefs(CONTEXT);
	}

	//region Configuration
	public void configure(String CLIENT_ID, String CLIENT_SECRET, String REDIRECT_URL) {
		Log.d("PPManager.Configure","\nConfiguring PPManager:\nID : "+CLIENT_ID+"\nSEC : "+CLIENT_SECRET+"\nREDIR : "+REDIRECT_URL);
		devPrefs.setClientId(CLIENT_ID);
		devPrefs.setClientSecret(CLIENT_SECRET);
		devPrefs.setClientRedirect(REDIRECT_URL);
    }

    public Boolean isConfigured(){
		return devPrefs.exists();
	}

	public Configuration getConfiguration(){
		return new Configuration();
	}

	public class Configuration {

		public String getClientId() {
			return devPrefs.getClientId();
		}

		public void setClientId(String value) {
			devPrefs.setClientId(value);
		}

		public String getClientSecret() {
			return devPrefs.getClientSecret();
		}

		public void setClientSecret(String value) {
			devPrefs.setClientSecret(value);
		}

		public String getClientRedirect() {
			return devPrefs.getClientRedirect();
		}

		public void setClientRedirect(String value) {
			devPrefs.setClientRedirect(value);
		}

		public String getClientAccessToken() {
			return devPrefs.getClientAccessToken();
		}

		public void setClientAccessToken(String value) {
			devPrefs.setClientAccessToken(value);
		}

		public boolean exists(){
			return devPrefs.exists();
		}

		public void clear(){
			devPrefs.clear();
		}

	}
	//endregion

	//region UserData
	public UserData getUserData(){
		return new UserData();
	}

	public class UserData{

		public Boolean hasUser(){
			return userPrefs.exists();
		}

		public String getAccountType() {
			return userPrefs.getAccountType();
		}

		public void setAccountType(String accountType) {
			userPrefs.setAccountType(accountType);
		}

		public String getCountry() {
			return userPrefs.getCountry();
		}

		public void setCountry(String country) {
			userPrefs.setCountry(country);
		}

		public String getCoverPhoto() {
			return userPrefs.getCoverPhoto();
		}

		public void setCoverPhoto(String coverPhoto) {
			userPrefs.setCoverPhoto(coverPhoto);
		}

		public String getFirstName() {
			return userPrefs.getFirstName();
		}

		public void setFirstName(String firstName) {
			userPrefs.setFirstName(firstName);
		}

		public String getHandle() {
			return userPrefs.getHandle();
		}

		public void setHandle(String handle) {
			userPrefs.setHandle(handle);
		}

		public String getLastName() {
			return userPrefs.getLastName();
		}

		public void setLastName(String lastName) {
			userPrefs.setLastName(lastName);
		}

		public String getProfilePic() {
			return userPrefs.getProfilePic();
		}

		public void setProfilePic(String profilePic) {
			userPrefs.setProfilePic(profilePic);
		}

		public String getUserId() {
			return userPrefs.getUserId();
		}

		public void setUserId(String userId) {
			userPrefs.setUserId(userId);
		}

		public String getUserType() {
			return userPrefs.getUserType();
		}

		public void setUserType(String userType) {
			userPrefs.setUserType(userType);
		}

		public String getMyDataStorage() {
			return userPrefs.getMyDataStorage();
		}

		public void setMyDataStorage(String myDataStorage) {
			userPrefs.setMyDataStorage(myDataStorage);
		}

		public String getMyGlobalDataStorage() {
			return userPrefs.getMyGlobalDataStorage();
		}

		public void setMyGlobalDataStorage(String myGlobalDataStorage) {
			userPrefs.setMyGlobalDataStorage(myGlobalDataStorage);
		}

		public ArrayList<String> getStoredFriendData(){
			if(userPrefs.getFriendData()!=null){
				return userPrefs.getFriendData();
			}
			else{
				return new ArrayList<>();
			}
		}

	}
	//endregion

	public FriendsService getFriendsManager(){
		return new FriendsService();
	}

	public class FriendsService {

		FriendsService(){

		}

		public void getFriendsData(Interceptor interceptor){
            Log.d(" GET_FRIENDS","========================1");
			Call<ArrayList<User>> friendsCall = getApi(interceptor).getFriends(devPrefs.getClientAccessToken());
            Log.d(" GET_FRIENDS","========================2");
				friendsCall.enqueue(new Callback<ArrayList<User>>() {
					@Override
					public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                        Log.d(" GET_FRIENDS","========================4");
						if (response.code() == 200) {
                            Log.d(" GET_FRIENDS","========================5");
							System.out.println(response.body());
//							friendsList = response.body();
//							for (int i=0;i>=friendsList.size(); i++){
//								allFriendsHandles.add(friendsList.get(i).getHandle());
//							}
						}
						else{
							Log.e(" GET_FRIENDS_ERR","Error getting friends data.");
							Log.e(" GET_FRIENDS_ERR","Response code is : "+response.code());
							Log.e(" GET_FRIENDS_ERR","Response message is : "+response.message());
						}
					}

					@Override
					public void onFailure(Call<ArrayList<User>> call, Throwable t) {
						Log.e("GET_FRIENDS_ERR", "Request failed with throwable: " + t);
					}
				});
		}

//		public interface FriendsResponse {
//			void onFriendsResponse(ArrayList<String> output);
//		}
//
//		public static class getFriends extends AsyncTask<Void, Void, ArrayList<String>> {
//
//			public FriendsResponse delegate;
//			private ArrayList<User> friendsList;
//			private ArrayList<String> allFriendsHandles;
//			private final WeakReference<Activity> weakActivity;
//
//			public getFriends(Activity activity){
//				weakActivity = new WeakReference<>(activity);;
//			}
//
//			@Override
//			protected ArrayList<String> doInBackground(Void... params) {
//				_DevPrefs devPrefs = new _DevPrefs(weakActivity.get());
//				allFriendsHandles = new ArrayList<>();
//				Call<ArrayList<User>> friendsCall = getApi(weakActivity.get()).getFriends(devPrefs.getClientAccessToken());
//				friendsCall.enqueue(new Callback<ArrayList<User>>() {
//					@Override
//					public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
//						if (response.code() == 200) {
//							System.out.println(response.body());
//							friendsList = response.body();
//							for (int i=0;i>=friendsList.size(); i++){
//								allFriendsHandles.add(friendsList.get(i).getHandle());
//							}
//						}
//						else{
//							Log.e(" GET_FRIENDS_ERR","Error getting friends data.");
//							Log.e(" GET_FRIENDS_ERR","Response code is : "+response.code());
//							Log.e(" GET_FRIENDS_ERR","Response message is : "+response.message());
//						}
//					}
//
//					@Override
//					public void onFailure(Call<ArrayList<User>> call, Throwable t) {
//						Log.e("GET_FRIENDS_ERR", "Request failed with throwable: " + t);
//					}
//				});
//				return allFriendsHandles;
//			}
//
//			@Override
//			protected void onPostExecute(final ArrayList<String> result) {
//				delegate.onFriendsResponse(result);
//			}
//
//			@Override
//			protected void onCancelled() {
//			}
//		}

	}

	public void showSSOLogin(Intent intent){
		Log.d("PPManager.showSSOLogin","Launching playPORTAL SSO...");
		ssoLoginFragment ssoLoginFragment = new ssoLoginFragment();
		ssoLoginFragment.setNextActivity(intent);
		_DialogFragments.showDialogFragment(ACTIVITY, ssoLoginFragment, true, "SSO");
	}

	public void getUserBuckets(){

	}

	public void getGlobalBucket(){

	}

	public void setUserBucket(){

	}

	public void setGlobalBucket(){

	}






}