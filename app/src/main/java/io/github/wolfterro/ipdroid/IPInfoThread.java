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

                textViewHostname.setText(String.format("%s %s",
                        ((Activity) c).getString(R.string.hostname),
                        ii.getHostname()));

                textViewLoc.setText(String.format("%s %s",
                        ((Activity) c).getString(R.string.loc),
                        ii.getLoc()));

                textViewOrg.setText(String.format("%s %s",
                        ((Activity) c).getString(R.string.org),
                        ii.getOrg()));

                textViewCity.setText(String.format("%s %s",
                        ((Activity) c).getString(R.string.city),
                        ii.getCity()));

                textViewRegion.setText(String.format("%s %s",
                        ((Activity) c).getString(R.string.region),
                        ii.getRegion()));

                textViewCountry.setText(String.format("%s %s",
                        ((Activity) c).getString(R.string.country),
                        ii.getCountry()));
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
