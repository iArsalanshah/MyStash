package com.citemenu.mystash.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Telephony;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.citemenu.mystash.R;
import com.citemenu.mystash.constant.Constant;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class SelectShareIntent {
    private static String[] items;

    public static void selectIntent(final Activity activity, final String msg) {

        items = new String[]{"Facebook", "Twitter",
                activity.getString(R.string.email),
                activity.getString(R.string.sms),
                activity.getString(R.string.cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.share_title));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Facebook")) {
                    facebookShare(activity, msg);
                } else if (items[item].equals("Twitter")) {
                    twitterShare(activity, msg);
                } else if (items[item].equals(activity.getString(R.string.email))) {
                    emailShare(activity, msg);
                } else if (items[item].equals(activity.getString(R.string.sms))) {
                    messageShare(activity, msg);
                } else if (items[item].equals(activity.getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf("WTF: ", "UTF-8 should always be supported", e);
            throw new RuntimeException("URLEncoder.encode() failed for " + s);
        }
    }

    //Share on Twitter Starts
    private static void twitterShare(Context context, String msg) {

        // Create intent using ACTION_VIEW and a normal Twitter url:
        String tweetUrl = String.format("https://twitter.com/intent/tweet?text=%s&url=%s",
                urlEncode(msg),
                urlEncode(Constant.WEB_URL)
        );
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));

        // Narrow down to official Twitter app, if available:
        List<ResolveInfo> matches = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
                intent.setPackage(info.activityInfo.packageName);
            }
        }
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(context, context.getString(R.string.no_twitter_message), Toast.LENGTH_SHORT).show();
        }
    }

    //Share on Facebook
    private static void facebookShare(Activity activity, String msg) {
        String msgWithLink = msg + Constant.WEB_URL;

        if (!FacebookSdk.isInitialized()) {
            FacebookSdk.sdkInitialize(activity);
        }
        ShareDialog shareDialog = new ShareDialog(activity);

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(activity.getString(R.string.mystash_app))
                    .setContentDescription(msgWithLink)
                    .setContentUrl(Uri.parse(Constant.WEB_URL))
                    .build();
            shareDialog.show(linkContent);
        }
    }

    //Email Share
    private static void emailShare(Context context, String msg) {
        String msgWithLink = msg + Constant.WEB_URL;

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.mystash_app));
        emailIntent.putExtra(Intent.EXTRA_TEXT, msgWithLink);
        context.startActivity(Intent.createChooser(emailIntent, context.getString(R.string.share)));
    }

    //SMS Share
    private static void messageShare(Context context, String msg) {
        String msgWithLink = msg + Constant.WEB_URL;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) //At least KitKat
        {
            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(context); //Need to change the build to API 19

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, msgWithLink);

            if (defaultSmsPackageName != null)//Can be null in case that there is no default, then the user would be able to choose any app that support this intent.
            {
                sendIntent.setPackage(defaultSmsPackageName);
            }
            context.startActivity(sendIntent);

        } else //For early versions, do what worked for you before.
        {
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setData(Uri.parse("sms:"));
            sendIntent.putExtra("sms_body", msgWithLink);
            context.startActivity(sendIntent);
        }
    }
}
