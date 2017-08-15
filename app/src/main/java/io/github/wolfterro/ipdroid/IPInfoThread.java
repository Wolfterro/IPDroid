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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Wolfterro on 19/06/2017.
 */

public class IPInfoThread extends Thread {
    // Propriedades protegidas
    // =======================
    protected TextView textViewExternalIP;    // IP Externo
    protected TextView textViewInternalIP;    // IP Interno
    protected TextView textViewHostname;      // Hostname
    protected TextView textViewLoc;           // Localização
    protected TextView textViewOrg;           // Organização
    protected TextView textViewCity;          // Cidade
    protected TextView textViewRegion;        // Região
    protected TextView textViewCountry;       // País

    // Informações resgatadas
    // ----------------------
    protected String internalIP = "";

    // Propriedades privadas
    // =====================
    private Context c = null;
    private ProgressDialog pd = null;
    private GetIPInfo ii = null;
    private String status = "";

    // Construtor da classe
    // ====================
    public IPInfoThread(Context c, ProgressDialog pd) {
        this.c = c;
        this.pd = pd;

        textViewExternalIP = (TextView) ((Activity)c).findViewById(R.id.textView2_ExternalIP);
        textViewInternalIP = (TextView) ((Activity)c).findViewById(R.id.textView6_InternalIP);
        textViewHostname = (TextView) ((Activity)c).findViewById(R.id.textView3_Hostname);
        textViewLoc = (TextView) ((Activity)c).findViewById(R.id.textView4_Loc);
        textViewOrg = (TextView) ((Activity)c).findViewById(R.id.textView5_Org);
        textViewCity = (TextView) ((Activity)c).findViewById(R.id.textView6_City);
        textViewRegion = (TextView) ((Activity)c).findViewById(R.id.textView7_Region);
        textViewCountry = (TextView) ((Activity)c).findViewById(R.id.textView8_Country);
    }

    @Override
    public void run() {
        GetInternalIPAddress();
        GetExternalIPInfo();

        handler.sendEmptyMessage(0);
    }

    // Métodos privados
    // ================

    // Resgatando o Endereço IP externo e suas informações
    // ===================================================
    private void GetExternalIPInfo() {
        ii = new GetIPInfo(((Activity)c).getString(R.string.notAvailable));
        ii.init();
    }

    // Resgatando o Endereço IP interno da rede Wireless
    // =================================================
    private void GetInternalIPAddress() {
        WifiManager wm = (WifiManager)((Activity)c)
                .getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        int ip = wm.getConnectionInfo().getIpAddress();

        internalIP = Formatter.formatIpAddress(ip);
        if(internalIP.equals("0.0.0.0")) {
            internalIP = ((Activity)c).getString(R.string.notAvailable);
        }
    }

    // Atualizando informações na Activity principal
    // =============================================
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message m) {
            status = ii.getStatus();

            if(status.equals("OK")) {
                textViewInternalIP.setText(internalIP);
                textViewExternalIP.setText(ii.getExternalIP());

                textViewHostname.setText(ii.getHostname());
                textViewLoc.setText(ii.getLoc());
                textViewOrg.setText(ii.getOrg());
                textViewCity.setText(ii.getCity());
                textViewRegion.setText(ii.getRegion());
                textViewCountry.setText(ii.getCountry());
            }
            else {
                String message = String.format("%s %s",
                        ((Activity)c).getString(R.string.couldNotGetInfo),
                        status);

                Toast.makeText(c, message, Toast.LENGTH_LONG).show();
            }

            pd.dismiss();
        }
    };
}
