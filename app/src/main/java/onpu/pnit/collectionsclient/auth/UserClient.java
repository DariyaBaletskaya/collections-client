package onpu.pnit.collectionsclient.auth;


import onpu.pnit.collectionsclient.entities.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserClient {


    @POST("/registration")
    Call<User> createUser(@Body User user);

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

}
