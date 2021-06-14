package ca.mcgill.ecse428.application;

import java.util.List;

import ca.mcgill.ecse428.application.representation.AccountRep;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import io.reactivex.Observable;

import ca.mcgill.ecse428.application.dto.*;

public interface InterfaceAPI {
    @GET("/ads")
    Observable<List<AdDto>> getAllAds();

    @GET("/ads/asc")
    Observable<List<AdDto>> getAllAdsByZipcodeAsc();

    @GET("/ads/desc")
    Observable<List<AdDto>> getAllAdsByZipcodeDesc();

    @GET("/ads/account/{username}")
    Observable<List<AdDto>> getAccountAds(@Path("username") String username);

    @GET("/ads/{id}")
    Observable<AdDto> getAd(@Path("id") int id);

    @PUT("/ads/{id}")
    Observable<AdDto> modifyAd(@Path("id") int id, @Body AdDto dto);

    @POST("/ads")
    Observable<AdDto> createAd(@Body AdDto dto, @Header("Authorization") String token);

    @DELETE("/ads/{id}")
    Observable<ResponseBody> deleteAd(@Path("id") int id);

    @GET("/accounts/{id}")
    Observable<AccountDto> getAccount(@Path("id") String username);

    @POST("/accounts/")
    Observable<AccountDto> createAccount(@Body AccountDto dto);

    @PUT("/accounts/{id}")
    Observable<AccountDto> modifyAccount(@Path("id") String username,@Body AccountDto dto);

    @DELETE("/accounts/{username}/{confirm}")
    Observable<ResponseBody> deleteAccount(@Path("username") String username,@Path("confirm") boolean confirm);

    @POST("/login")
    Observable<ResponseBody> login(@Body AccountRep accountRep);

    @GET("/bids/{adId}")
    Observable<List<BidDto>> getBids(@Path("adId") String adId);

    @POST("/bids")
    Observable<BidDto> createBid(@Body BidDto bidDto, @Header("Authorization") String token);

    @PUT("/bids/{adId}/{username}")
    Observable<BidDto> acceptBid(@Path("adId") Integer adId, @Path("username") String username);
}
