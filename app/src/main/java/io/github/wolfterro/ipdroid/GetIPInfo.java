package io.github.wolfterro.ipdroid;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Wolfterro on 19/06/2017.
 */

public class GetIPInfo {
    // URL da API do IPInfo
    // ====================
    private String IPInfo = "http://ipinfo.io/json";
    private String[] keys = {"ip", "hostname", "city", "region", "country", "loc", "org"};

    // Informações resgatadas
    // ======================
    protected String externalIP = "";
    protected String hostname = "";
    protected String loc = "";
    protected String org = "";
    protected String city = "";
    protected String region = "";
    protected String country = "";

    // Informações de erro
    // ===================
    protected String error = "";
    protected String notAvailable = "";

    // Construtor da classe
    // ====================
    public GetIPInfo(String notAvailable) {
        this.notAvailable = notAvailable;
    }

    // Inicializando resgate de informações sobre o endereço IP
    // ========================================================
    public void init() {
        try {
            URL u = new URL(IPInfo);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.connect();

            InputStream stream = (InputStream) conn.getContent();

            Scanner s = new Scanner(stream);
            s.useDelimiter("\\A");
            String res = s.hasNext() ? s.next() : "";
            s.close();

            JSONObject obj = new JSONObject(res);

            String v = "";
            for(int i = 0; i < keys.length; i++) {
                if(obj.has(keys[i])) {
                    v = obj.getString(keys[i]);
                }
                else {
                    v = notAvailable;
                }

                switch(keys[i]) {
                    case "ip": externalIP = v; break;
                    case "hostname": hostname = v; break;
                    case "loc": loc = v; break;
                    case "org": org = v; break;
                    case "city": city = v; break;
                    case "region": region = v; break;
                    case "country": country = v; break;
                    default: break;
                }
            }
            error = "OK";
        }
        catch (MalformedURLException e) {
            error = "MALFORMED_URL_EXCEPTION";
            Log.e("GetIPInfo().init()", e.toString());
        }
        catch (IOException e) {
            error = "NOT_FOUND_OR_INNACESSIBLE";
            Log.e("GetIPInfo().init()", e.toString());
        }
        catch (JSONException e) {
            error = "JSON_EXCEPTION_ERROR";
            Log.e("GetIPInfo().init()", e.toString());
        }
    }

    // Métodos de resgate
    // ==================
    public String getExternalIP() {
        return externalIP;
    }

    public String getHostname() {
        return hostname;
    }

    public String getLoc() {
        return loc;
    }

    public String getOrg() {
        return org;
    }

    public String getCity() {
        return city;
    }

    public String getRegion() {
        return region;
    }

    public String getCountry() {
        return country;
    }

    public String getStatus() {
        return error;
    }
}
