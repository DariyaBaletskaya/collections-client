package onpu.pnit.collectionsclient.auth;


import onpu.pnit.collectionsclient.entities.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserClient {

    @FormUrlEncoded
    @POST("/registration")
    Call<User> registerUser(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("/login")
    Call<User> loginUser(
            @Field("username") String username,
            @Field("password") String password
    );

    @GET("/registration")
    Call<User> getRegistrationPage();

    @GET("/users/{username}")
    Call<User> getUser(@Path("username") String username);


}
