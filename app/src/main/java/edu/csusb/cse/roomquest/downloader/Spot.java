package edu.csusb.cse.roomquest.downloader;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Michael on 3/21/2015.
 */
public class Spot {
    public static final String DEFAULT_URL =
            "https://139.182.134.143:30107/RoomQuest.zip";
    public static final File MAP_FOLDER = new File(Environment.getExternalStorageDirectory(), "RoomQuest");
    private static boolean trustingEveryone = false;

    // Since we're too poor to acquire an official certificate authority...
    public static void trustEveryone() {
        if (trustingEveryone)
            return;
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }

                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        trustingEveryone = true;
    }
    // Since we're too poor to acquire an official certificate authority...
    public static void loadCert(InputStream certInput) {
        // make a certificate
        try {
            Certificate ca = null;
            try {
                ca = CertificateFactory.getInstance("X.509").generateCertificate(certInput);
            } catch (CertificateException e) {
                e.printStackTrace();
                return;
            } finally {
                try {
                    certInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // put it in a keystore
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that uses the certificate
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // Create an SSLContext that uses the trust manager
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

            // set it as the default
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());

            // since ip address don't work with hostname verification for some reason
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void fetch() {
        URL url;
        try {
            url = new URL(DEFAULT_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return;
        }
        fetch(url);
    }

    public static void fetch(URL url) {
        if (url == null)
            return;
        ZipInputStream zi = null;
        try {
            zi = new ZipInputStream(url.openStream());
            extract(zi);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zi != null)
                try {
                    zi.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    private static void extract(ZipInputStream zi) throws IOException {
        if (MAP_FOLDER.exists())
            eraseFolderContents(MAP_FOLDER);
        else
            MAP_FOLDER.mkdirs();
        extract(zi, MAP_FOLDER);
    }

    private static void extract(ZipInputStream zi, File outFolder) throws IOException {
        ZipEntry ze = zi.getNextEntry();
        if (ze == null)
            return;
        byte[] buffer = new byte[1024];
        while(ze != null) {
            File outFile = new File(outFolder,ze.getName());
            Log.i("Spot", "unzipping " + outFile);
            outFile.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(outFile);
            int len;
            while ((len = zi.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            ze = zi.getNextEntry();
        }
    }

    /**
     * Deletes folder contents. Keeps folder structure.
     * @param folder the folder to delete contents of.
     */
    private static void eraseFolderContents(File folder) {
        String[] files = folder.list();
        if (files != null)
            for (String name : files) {
                File file = new File(folder,name);
                if (file.isDirectory()) {
                    eraseFolderContents(file);
                }
                Log.i("Spot", "Deleting " + file);
                file.delete();
        }
    }
}
