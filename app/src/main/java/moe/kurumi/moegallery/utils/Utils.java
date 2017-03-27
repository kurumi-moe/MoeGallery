package moe.kurumi.moegallery.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import moe.kurumi.moegallery.data.Providers;

/**
 * Created by kurumi on 15-5-29.
 */
public class Utils {

    public static String getFileNameFromUrl(String url) {
        try {
            return URLDecoder.decode(url.substring(url.lastIndexOf('/') + 1, url.length()),
                    "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getMimeType(String url) {
        String type;
        String extension = url.substring(url.lastIndexOf('.') + 1);
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        type = mime.getMimeTypeFromExtension(extension);
        return type;
    }

    public static List<String> tags2List(String tags) {
        return new ArrayList<>(Arrays.asList(tags.trim().split(" ")));
    }

    // read more from here: http://stackoverflow.com/a/3758880/2600042
    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) {
            return bytes + " B";
        }
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static boolean isGif(String fileName) {
        return fileName.toLowerCase().endsWith(".gif");
    }

    public static String getDomainName(String uri) {
        String domainName = "";

        if (uri.startsWith(Providers.SCHEME_HTTP)) {
            domainName = uri.substring(Providers.SCHEME_HTTP.length());
        } else if (uri.startsWith(Providers.SCHEME_HTTPS)) {
            domainName = uri.substring(Providers.SCHEME_HTTPS.length());
        }
        domainName = domainName.substring(0,
                domainName.contains("/") ? domainName.indexOf('/') : domainName.length());

        return domainName;
    }

    public static String getProviderName(String uri) {

        String providerName = getDomainName(uri);

        int count = providerName.length() - providerName.replace(".", "").length();

        if (count == 2) {
            providerName = providerName.substring(providerName.indexOf('.') + 1);
        }

        if (Providers.DANBOORU_URI.contains(providerName)) {
            providerName = getDomainName(Providers.DANBOORU_URI);
        }

        return providerName;
    }

    public static String getHostName(String uri) {
        String hostname = getProviderName(uri);
        hostname = hostname.substring(0, hostname.lastIndexOf('.'));
        return hostname;
    }

    public static String fixURL(String urlStr) {
        return urlStr.replace("?", "%3F");
    }

    public static int String2Color(String string) {

        String s = MD5(string);
        Log.d("", s);
        //int hash = string.hashCode();
        int hash = 0;
        if (s != null) {
            hash = Integer.parseInt(s.substring(0, 6), 16);
        }
        Log.d("hash:", "" + hash);
        int r = (hash & 0xFF0000) >> 16;
        int g = (hash & 0x00FF00) >> 8;
        int b = hash & 0x0000FF;
        return Color.argb(255, r, g, b);
    }

    public static String MD5(String source) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(source.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte anArray : array) {
                sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException ignored) {
        }
        return null;
    }

    public static int getNavigationBarHeight(Context context, int orientation) {
        Resources resources = context.getResources();

        int id = resources.getIdentifier(
                orientation == Configuration.ORIENTATION_PORTRAIT ? "navigation_bar_height"
                        : "navigation_bar_height_landscape",
                "dimen", "android");
        if (id > 0) {
            return resources.getDimensionPixelSize(id);
        }
        return 0;
    }

    public static void copy(File inputFile, File outputPath, String fileName) {

        InputStream in;
        OutputStream out;
        try {

            if (!outputPath.exists()) {
                outputPath.mkdirs();
            }

            File outputFile = new File(outputPath, fileName);

            if (outputFile.exists()) {
                return;
            }

            in = new FileInputStream(inputFile);
            out = new FileOutputStream(outputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
