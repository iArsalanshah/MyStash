package com.citemenu.mystash.pojo.redeem_coupon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RedeemCoupon {

    @SerializedName("header")
    @Expose
    private Header header;
    @SerializedName("body")
    @Expose
    private Body body;

    /**
     * @return The header
     */
    public Header getHeader() {
        return header;
    }

    /**
     * @param header The header
     */
    public void setHeader(Header header) {
        this.header = header;
    }

    /**
     * @return The body
     */
    public Body getBody() {
        return body;
    }

    /**
     * @param body The body
     */
    public void setBody(Body body) {
        this.body = body;
    }

}
