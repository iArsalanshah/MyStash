package com.example.mystashapp.mystashappproject.webservicefactory;

import com.example.mystashapp.mystashappproject.pojo.add_rating_pojo.ADDRating;
import com.example.mystashapp.mystashappproject.pojo.add_review_pojo.ADDReview;
import com.example.mystashapp.mystashappproject.pojo.add_stash.AddStash;
import com.example.mystashapp.mystashappproject.pojo.addloyalty_pojo.AddLoyalty;
import com.example.mystashapp.mystashappproject.pojo.all_flyers_by_categ.GetAllFlyersWebService;
import com.example.mystashapp.mystashappproject.pojo.categories_coupons_pojo.CategoriesCoupons;
import com.example.mystashapp.mystashappproject.pojo.customer_check_in.CustomerCheckIn;
import com.example.mystashapp.mystashappproject.pojo.delete_loyalty_card.DeleteLoyaltyCard;
import com.example.mystashapp.mystashappproject.pojo.editloyalty_pojo.EditLoyalty;
import com.example.mystashapp.mystashappproject.pojo.get_all_coupons_pojo.Get_All_Coupons;
import com.example.mystashapp.mystashappproject.pojo.get_my_stash_list.GetMyStash;
import com.example.mystashapp.mystashappproject.pojo.getcardslist_pojo.GetCardsList;
import com.example.mystashapp.mystashappproject.pojo.getmycards_pojo.GetMycards;
import com.example.mystashapp.mystashappproject.pojo.pojo_cite_points.CitePointsTransactions;
import com.example.mystashapp.mystashappproject.pojo.pojo_login.LoginUser;
import com.example.mystashapp.mystashappproject.pojo.pojo_register.RegisterUser;
import com.example.mystashapp.mystashappproject.pojo.pojo_searchbusiness.SearchBusiness;
import com.example.mystashapp.mystashappproject.pojo.program_stamps.ProgramsStamps;
import com.example.mystashapp.mystashappproject.pojo.redeem_coupon.RedeemCoupon;
import com.example.mystashapp.mystashappproject.pojo.remindme_coupon.RemindMe;
import com.example.mystashapp.mystashappproject.pojo.remove_stash.RemoveStash;
import com.example.mystashapp.mystashappproject.pojo.to_save_coupon_pojo.ToSaveCoupon;
import com.example.mystashapp.mystashappproject.pojo.update_registeration.UpdateRegisteration;
import com.example.mystashapp.mystashappproject.pojo.upload_loyaltyimage_pojo.UploadLoyaltyImage;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface WebService {
    @GET("/mystash/mobileservice_new.php")
    Call<LoginUser> getLoginUsers(@Query("action") String action,
                                  @Query("username") String email,
                                  @Query("password") String password);

    @POST("/mystash/mobileservice_new.php")
    Call<RegisterUser> postRegisterUser(@Query("action") String customer_register,
                                        @Query("cfirstname") String name,
                                        @Query("email") String email,
                                        @Query("password") String password,
                                        @Query("number") String number,
                                        @Query("imgurl") String imgURL,
                                        @Query("birthday") String bday,
                                        @Query("sex") String sex,
                                        @Query("categories") String category,
                                        @Query("areaOfInterest") String areaOfInterst);

    @POST("/mystash/mobileservice_new.php")
    Call<UpdateRegisteration> postUpdateRegisterUser(@Query("action") String customer_register_edit,
                                                     @Query("cfirstname") String name,
                                                     @Query("email") String email,
                                                     @Query("password") String password,
                                                     @Query("number") String phone,
                                                     @Query("imgurl") String imgURL,
                                                     @Query("birthday") String bday,
                                                     @Query("sex") String gender,
                                                     @Query("categories") String category,
                                                     @Query("areaOfInterest") String areaOfInterest);

    @GET("/mystash/mobileservice_new.php")
    Call<GetMyStash> getMyStashList(@Query("action") String action,
                                    @Query("userid") String cid);

    @GET("/mystash/mobileservice_new.php")
    Call<SearchBusiness> getSearchBusinessCall(@Query("action") String action,
                                               @Query("userid") String userid,
                                               @Query("lat") String lat,
                                               @Query("long") String lng,
                                               @Query("radius") float radius);

    @POST("/mystash/mobileservice_new.php")
    Call<LoginUser> getFblogin(@Query("action") String action,  //fbid,imgurl
                               @Query("email") String email,
                               @Query("firstname") String name,
                               @Query("fbid") String fbid,
                               @Query("sex") String gender,
                               @Query("imgurl") String imgurl);

    @POST("/mystash/mobileservice_new.php")
    Call<RegisterUser> getForgotPwd(@Query("action") String action,
                                    @Query("email") String email); // Need to check how to send id in forgot pwd

    @POST("/mystash/mobileservice_new.php")
    Call<AddStash> getAddStash(@Query("action") String add_my_stash,
                               @Query("adminid") String uid,
                               @Query("userid") String cid);

    @POST("/mystash/mobileservice_new.php")
    Call<RemoveStash> getRemoveStash(@Query("action") String delete_my_stash,
                                     @Query("adminid") String uid,
                                     @Query("userid") String cid);

    @GET("/mystash/mobileservice_new.php")
    Call<ProgramsStamps> getStamps(@Query("action") String get_customer_stamps,
                                   @Query("cid") String cid,
                                   @Query("uid") String uid);

    @POST("/mystash/mobileservice_new.php")
    Call<ADDReview> getPostReview(@Query("action") String add_reviews,
                                  @Query("cid") String cid,
                                  @Query("uid") String uid,
                                  @Query("review") String review);

    @POST("/mystash/mobileservice_new.php")
    Call<ADDRating> getPostRating(@Query("action") String add_rating,
                                  @Query("uid") String uid,
                                  @Query("rating") float rating);

    @GET("/mystash/mobileservice_new.php")
    Call<GetMycards> getMyCards(@Query("action") String customer_my_loyalty_card,
                                @Query("userid") String cid_userid);


    @GET("/mystash/mobileservice_new.php")
    Call<GetCardsList> getCardsList(@Query("action") String customer_get_loyalty_card_list,
                                    @Query("userid") String cid_userid);

    @GET("/mystash/mobileservice_new.php")
    Call<AddLoyalty> getAddLoyalty(@Query("action") String customer_add_loyalty_card,
                                   @Query("cid") String cid_userid,
                                   @Query("cardname") String cardname_as_ur_name,
                                   @Query("carddetail") String carddetail_as_card_Name,
                                   @Query("cardno") String cardno,
                                   @Query("notes") String notes,
                                   @Query("frontimage") String frontImage,
                                   @Query("backimage") String barcodeImage);

    @GET("/mystash/mobileservice_new.php")
    Call<EditLoyalty> getEditLoyalty(@Query("action") String edit_customer_loyalty_card,
                                     @Query("cid") String cid_userid,
                                     @Query("cardname") String cardname,
                                     @Query("carddetail") String carddetail,
                                     @Query("companyinfo") String companyinfo,
                                     @Query("companylogo") String companylogo,
                                     @Query("cardno") String cardno,
                                     @Query("notes") String notes,
                                     @Query("frontimage") String frontimage,
                                     @Query("backimage") String backimage,
                                     @Query("is_registerd_company") String is_registerd_company,
                                     @Query("id") String id);

    @GET("/mystash/mobileservice_new.php")
    Call<CategoriesCoupons> getcategoriesCoupons(@Query("action") String customer_get_coupon_categories);

    @GET("/mystash/mobileservice_new.php")
    Call<Get_All_Coupons> getAllCoupons(@Query("action") String action,
                                        @Query("categoryid") String cat_id,
                                        @Query("cid") String cid_userid);

    @GET("/mystash/mobileservice_new.php")
    Call<ToSaveCoupon> getToSaveCoupon(@Query("action") String action,
                                       @Query("adminid") String adminid_uid,
                                       @Query("cid") String cid_userid,
                                       @Query("couponid") String couponid);

    @GET("/mystash/mobileservice_new.php")
    Call<Get_All_Coupons> getSavedCoupons(@Query("action") String customer_get_coupon_categories,
                                          @Query("cid") String cid);

    @GET("/mystash/mobileservice_new.php")
    Call<RemindMe> getRemindCoupon(@Query("action") String remindme_coupon,
                                   @Query("clientid") String clientID_cid,
                                   @Query("couponid") String couponid,
                                   @Query("uid") String uid,
                                   @Query("remindme") String remindme,
                                   @Query("remindmessage") String remindmessage);

    @GET("/mystash/mobileservice_new.php")
    Call<RedeemCoupon> getRedeemCoupon(@Query("action") String customer_redeem_coupons,
                                       @Query("adminid") String adminid_uid,
                                       @Query("cid") String cid,
                                       @Query("couponid") String couponid);

    @Multipart
    @POST("/mystash/mobileservice_new.php")
    Call<UploadLoyaltyImage> uploadLoyaltyImage(@Query("action") String upload_loyalty_image,
                                                @Part MultipartBody.Part file);

    @Multipart
    @POST("/mystash/mobileservice_new.php")
    Call<UploadLoyaltyImage> uploadProfileImage(@Query("action") String upload_image,
                                                @Part MultipartBody.Part file);


    @POST("/mystash/mobileservice_new.php")
    Call<CustomerCheckIn> checkInCustomer(@Query("action") String customer_checkin,
                                          @Query("userid") String userid,
                                          @Query("adminid") String adminid);

    @POST("/mystash/mobileservice_new.php")
    Call<DeleteLoyaltyCard> deleteLoyaltyCard(@Query("action") String delete_customer_loyalty_card,
                                              @Query("cardid") String cardid);

    @GET("/mystash/mobileservice_new.php")
    Call<CitePointsTransactions> getCitePoints(@Query("action") String get_customer_transactions,
                                               @Query("cid") String uid_adminID);

    @GET("/mystash/mobileservice_new.php")
    Call<Get_All_Coupons> getCouponsByAdmin(@Query("action") String get_coupons_by_adminid,
                                            @Query("cid") String cid,
                                            @Query("uid") String uid);

    @GET("/mystash/mobileservice_new.php")
    Call<GetAllFlyersWebService> getAllFlyers(@Query("action") String get_all_flyers,
                                              @Query("categoryid") String categoryid);

    // option 2: using a dynamic URL
    @Streaming
    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlAsync(@Url String fileUrl);
}
