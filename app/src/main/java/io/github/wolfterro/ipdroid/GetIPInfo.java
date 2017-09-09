/*
MIT License

Copyright (c) 2017 Wolfgang Almeida

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

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
    private String IPInfo = "https://ipinfo.io/json";
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
        String res = "";
        try {
            URL u = new URL(IPInfo);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.connect();

            InputStream stream = (InputStream) conn.getContent();

            Scanner s = new Scanner(stream);
            s.useDelimiter("\\A");
            res = s.hasNext() ? s.next() : "";
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
            Log.e("GetIPInfo().init()", e.getMessage());
        }
        catch (IOException e) {
            error = "NOT_FOUND_OR_INNACESSIBLE";
            Log.e("GetIPInfo().init()", e.getMessage());
        }
        catch (JSONException e) {
            error = "JSON_EXCEPTION_ERROR";
            Log.e("GetIPInfo().init()", e.getMessage());
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
