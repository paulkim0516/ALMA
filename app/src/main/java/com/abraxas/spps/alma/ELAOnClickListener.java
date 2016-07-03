package com.abraxas.spps.alma;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

import java.io.InputStream;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class ELAOnClickListener implements View.OnClickListener{

    private String str;
    private String cookie;
    private DownloadManager.Request request;

    public ELAOnClickListener(String string, String cookie){
        this.str = string;
        this.cookie = cookie;
    }

    @Override
    public void onClick(View view) {
        DownloadManager downloadManager = (DownloadManager) view.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        request = new DownloadManager.Request(Uri.parse("https://spps.getalma.com" + str.split(";")[1]));
        String[][] requestProperties = new String[][]{
                {"Host", "spps.getalma.com"},
                {"Connection", "keep-alive"},
                {"Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"},
                {"Upgrade-Insecure-Requests", "1"},
                {"User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.111 Safari/537.36"},
                {"Accept-Encoding", "gzip, deflate, sdch"},
                {"Accept-Language", "en-US,en;q=0.8,ko;q=0.6"},
                {"Cookie", cookie}
        };
        for (String[] property : requestProperties) {
            request.addRequestHeader(property[0], property[1]);
        }
        request.setTitle(str.split(";")[0]).setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, str.split(";")[0]).setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        downloadManager.enqueue(request);
    }
}
