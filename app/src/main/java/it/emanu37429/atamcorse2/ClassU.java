package it.emanu37429.atamcorse2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.emanu37429.atamcorse2.trainsfrag.FragmentInfoTreno;

public class ClassU {

    public static String baseUrl = "https://emanu37429.altervista.org/atamcorserc/dsm/";
    public static String linkElencoVeicoli = baseUrl + "functionDispatcher.php?func=vecinfo";
    public static String linkCercaPercorso = baseUrl + "getPercorsoRestV1.php";
    public static String linkElencoCorse = baseUrl + "getLinea.php?linea=";
    public static String linkCorsePalina = baseUrl + "getPalinaNewSc.php?id=";
    public static String linkRSSRFI = baseUrl + "getRSSRFI.php";
    public static String linkNewsAtam = baseUrl + "functionDispatcher.php?func=atamnews";
    public static String linkCercaIndirizzo = baseUrl + "AddressSearch.php?auth=X&indirizzo=";
    public static String linkNewsAtamcorse = baseUrl + "acnews.php";
    public static String linkInfoCorsa = baseUrl + "infoCorsa.php?id=";
    public static String linkGetCorsa = baseUrl + "getCorsa.php?id=";
    public static String linkViaggiaTreno = "http://www.viaggiatreno.it/viaggiatrenonew/resteasy/viaggiatreno/";
    public static String linkVGTPartenze = linkViaggiaTreno + "partenze/";
    public static String linkVGTArrivi = linkViaggiaTreno + "arrivi/";

    public static String StringFromResource(Context cx, int res) throws Exception {
        InputStream inputStream = cx.getResources().openRawResource(res);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int ctr;
        ctr = inputStream.read();
        while (ctr != -1) {
            byteArrayOutputStream.write(ctr);
            ctr = inputStream.read();
        }
        inputStream.close();
        return byteArrayOutputStream.toString();
    }

    public static class Linea {
        public String id, nome, num;

        public Linea(String id, String nome, String num) {
            this.id = id;
            this.nome = nome;
            this.num = num;
        }
    }

    public static class Fermata {
        public String id, num, nome;

        public Fermata(String id, String nome, String num) {
            this.id = id;
            this.nome = nome;
            this.num = num;
        }
    }

    public static class Corsa {
        public String orapart, oraarr, part, arr, id;
        public AppCompatActivity act;

        public Corsa(String oraarr, String orapart, String part, String arr, String id, AppCompatActivity act) {
            this.arr = arr;
            this.part = part;
            this.oraarr = oraarr;
            this.orapart = orapart;
            this.id = id;
            this.act = act;
        }
    }

    public static class FermataCorsaP {
        public String lincap, rit, vec, ora, idc;
        public AppCompatActivity act;
        public Boolean nav;

        public FermataCorsaP(String idc, String lincap, String ora, String vec, String rit, Boolean nav, AppCompatActivity act) {
            this.lincap = lincap;
            this.vec = vec;
            this.ora = ora;
            this.idc = idc;
            this.nav = nav;
            this.rit = rit;
            this.act = act;
        }
    }

    public static class FermataCorsaBase {
        public Boolean pass, out;
        public String orast, orart, nome, id, scost;

        public FermataCorsaBase(String nome, String orast, String orart, String scost, String id, Boolean pass, Boolean out) {
            this.orart = orart;
            this.orast = orast;
            this.pass = pass;
            this.nome = nome;
            this.out = out;
            this.id = id;
            this.scost = scost;
        }
    }

    public static class Pagamento {
        public String datap, importo, oggetto, fornitore;

        public Pagamento(String datap, String importo, String ogggetto, String fornitore) {
            this.datap = datap;
            this.importo = importo;
            this.oggetto = ogggetto;
            this.fornitore = fornitore;
        }
    }

    public static class LPercorso {
        public String linea, part, arr, id;

        public LPercorso(String linea, String part, String arr, String id) {
            this.linea = linea;
            this.arr = arr;
            this.part = part;
            this.id = id;
        }
    }

    public static class News {
        public String datan, titolo, linkn;

        public News(String datan, String titolo, String linkn) {
            this.datan = datan;
            this.titolo = titolo;
            this.linkn = linkn;
        }
    }

    public static class NewsVT {
        public String datan, testo;
        public Boolean primopiano;

        public NewsVT(String datan, String testo, Boolean primopiano) {
            this.datan = datan;
            this.testo = testo;
            this.primopiano = primopiano;
        }
    }

    public static class Veicolo {
        public String id, mod, stat;

        public Veicolo(String id, String mod, String stato) {
            this.id = id;
            this.mod = mod;
            this.stat = stato;
        }
    }

    public static class Versione {
        public String vers, desc;

        public Versione(String vers, String desc) {
            this.vers = vers;
            this.desc = desc;
        }
    }

    public static String DownloadString(String url0) {
        try {
            URL url = new URL(url0);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(30000);
            java.util.Scanner s = new java.util.Scanner(connection.getInputStream()).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void OnError(final AppCompatActivity cx, final Exception e) {
        e.printStackTrace();
        cx.runOnUiThread(() -> new AlertDialog.Builder(cx)
                .setMessage("Si è verificato un'errore e l'app è stata arrestata.")
                .setPositiveButton("Invia report", (dialog, which) -> {
                    new Thread(() -> {
                        StringWriter sw = new StringWriter();
                        PrintWriter writer = new PrintWriter(sw);
                        e.printStackTrace(writer);
                        PostString(sw.toString());
                        cx.runOnUiThread(() -> {
                            try {
                                Toast.makeText(cx, "Fatto", Toast.LENGTH_LONG).show();
                                Thread.sleep(1000);
                                cx.finishAffinity();
                            } catch (Exception ignored) {
                            }
                        });
                    }).start();
                }).setNegativeButton("Chiudi", (dialog, which) -> cx.finishAffinity()).show());
    }

    static void PostString(String string) {
        HttpURLConnection connection;
        OutputStreamWriter request = null;
        URL url;
        String response;
        String parameters = string;
        try {
            url = new URL(ClassU.baseUrl + "crashreport/crashreport.php");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("POST");
            request = new OutputStreamWriter(connection.getOutputStream());
            request.write(parameters);
            request.flush();
            request.close();
            String line = "";
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            response = sb.toString();
            isr.close();
            reader.close();
        } catch (IOException e) {
        }
    }

    public static class PreferenzaTreno {
        public String stda, sta, stazioneda, stazionea;

        public PreferenzaTreno(String stazioneda, String stda, String stazionea, String sta) {
            this.sta = sta;
            this.stazionea = stazionea;
            this.stazioneda = stazioneda;
            this.stda = stda;
        }
    }

    public static class Treno {
        String OrarioArr, OrarioPart, Part, Arr, Idsol;
        List<String> Treni, Stazioni, CodTreni, TipoTreno;
        String Durata;
        String NCambi;
        Map Cookies;

        Treno(String OrarioArr, String OrarioPart, String Part, String Arr, List Treni, String Durata, String NCambi, List Stazioni, List CodTreni, String idsol, Map cookies) {
            this.Arr = Arr;
            this.Part = Part;
            this.OrarioArr = OrarioArr;
            this.OrarioPart = OrarioPart;
            this.Durata = Durata;
            this.Treni = Treni;
            this.Stazioni = Stazioni;
            this.NCambi = NCambi;
            this.CodTreni = CodTreni;
            this.Idsol = idsol;
            this.Cookies = cookies;
        }
    }

    public static class TrenoInfo {
        public String Treno;
        public List<String> Imgs;
        public List<TrenoFermata> Fermate;

        public TrenoInfo(String treno, List imgs, List fermate) {
            this.Treno = treno;
            this.Imgs = imgs;
            this.Fermate = fermate;
        }
    }

    public static class TrenoFermata {
        public String Fermata, OraArr, OraPart;
        public int type;

        public TrenoFermata(String nome, String oraArr, String oraPart, int type) {
            this.Fermata = nome;
            this.OraArr = oraArr;
            this.OraPart = oraPart;
            this.type = type;
        }
    }

    public static class RecStazione {
        public String stazione, cod;

        public RecStazione(String stazione, String cod) {
            this.stazione = stazione;
            this.cod = cod;
        }
    }

    public static class Stazione {
        public String stazione, arrivo, arrivoreale, partenza, partenzareale, binario, binarioreale, ritardoarrivo, ritardopartenza, idstazione;
        public Boolean Passato, out;

        public Stazione(String stazione, String arrivo, String arrivoreale, String partenza, String partenzareale, String binario, String binarioreale, String ritardoarrivo, String ritardopartenza, Boolean passato, String idstazione, Boolean out) {
            this.stazione = stazione;
            this.arrivo = arrivo;
            this.arrivoreale = arrivoreale;
            this.partenza = partenza;
            this.partenzareale = partenzareale;
            this.binario = binario;
            this.binarioreale = binarioreale;
            this.ritardoarrivo = ritardoarrivo;
            this.ritardopartenza = ritardopartenza;
            this.Passato = passato;
            this.idstazione = idstazione;
            this.out = out;
        }
    }

    public static class TrenoStzPartN {
        public String Num, Nstazione, NomeStaz, nomeStazArr, orapart, oraarr, durata, nomeTr;
        public FragmentInfoTreno fr;
        public JSONObject objTreno;

        public TrenoStzPartN(String num, String stazionepart, String nomeStaz, String nomeStazArr, String orapart, String oraarr, String durata, String nomeTr, FragmentInfoTreno fr, JSONObject objTreno) {
            this.Nstazione = stazionepart;
            this.Num = num;
            this.NomeStaz = nomeStaz;
            this.nomeStazArr = nomeStazArr;
            this.oraarr = oraarr;
            this.orapart = orapart;
            this.durata = durata;
            this.nomeTr = nomeTr;
            this.fr = fr;
            this.objTreno = objTreno;
        }
    }

    public static class Traghetto {
        String part, arr, portopart, portoarr, durata;

        public Traghetto(String part, String arr, String portopart, String portoarr, String durata) {
            this.arr = arr;
            this.part = part;
            this.portoarr = portoarr;
            this.portopart = portopart;
            this.durata = durata;
        }
    }

    public static class Aliscafo {
        String part, arr, portopart, portoarr, tariffa;

        public Aliscafo(String part, String arr, String portopart, String portoarr, String tariffa) {
            this.arr = arr;
            this.part = part;
            this.portoarr = portoarr;
            this.portopart = portopart;
            this.tariffa = tariffa;
        }
    }

    public static BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static class MarkerInfoMapTrack {
        String nomefermata, id, oraprog, orareale, scostamento;

        public MarkerInfoMapTrack(String nomefermata, String id, String orareale, String oraprog, String scostamento) {
            this.nomefermata = nomefermata;
            this.id = id;
            this.oraprog = oraprog;
            this.orareale = orareale;
            this.scostamento = scostamento;
        }
    }

    public static class BusPercorso {
        public String nomeLinea, idCorsa, nomeFerPart, oraFerPart, nomeFerArr, oraFerArr;

        public BusPercorso(String nomeLinea, String idCorsa, String nomeFerPart, String oraFerPart, String nomeFerArr, String oraFerArr) {
            this.nomeLinea = nomeLinea;
            this.idCorsa = idCorsa;
            this.nomeFerArr = nomeFerArr;
            this.nomeFerPart = nomeFerPart;
            this.oraFerArr = oraFerArr;
            this.oraFerPart = oraFerPart;
        }
    }

    public static class TrenoPercorsoFdC {
        String idTreno, nomeFerPart, oraFerPart, nomeFerArr, oraFerArr;

        public TrenoPercorsoFdC(String idTreno, String nomeFerPart, String oraFerPart, String nomeFerArr, String oraFerArr) {
            this.idTreno = idTreno;
            this.nomeFerArr = nomeFerArr;
            this.nomeFerPart = nomeFerPart;
            this.oraFerArr = oraFerArr;
            this.oraFerPart = oraFerPart;
        }
    }

    public static class TrenoPercorsoFCE {
        String idTreno, nomeFerPart, oraFerPart, nomeChange, oraChange, nomeChangePart, oraChangePart, nomeFerArr, oraFerArr, idTreno2;

        public TrenoPercorsoFCE(String idTreno, String nomeFerPart, String oraFerPart, String nomeChange, String oraChange, String idTreno2, String nomeChangePart, String oraChangePart, String nomeFerArr, String oraFerArr) {
            this.idTreno = idTreno;
            this.idTreno2 = idTreno2;
            this.nomeFerArr = nomeFerArr;
            this.nomeFerPart = nomeFerPart;
            this.oraFerArr = oraFerArr;
            this.oraFerPart = oraFerPart;
            this.nomeChange = nomeChange;
            this.oraChange = oraChange;
            this.nomeChangePart = nomeChangePart;
            this.oraChangePart = oraChangePart;
        }
    }

    public static class TrenoPercorsoF2 {
        public String idTreno, nomeFerPart, nomeFerArr;
        public long oraFerPart, oraFerArr;

        public TrenoPercorsoF2(String idTreno, String nomeFerPart, long oraFerPart, String nomeFerArr, long oraFerArr) {
            this.idTreno = idTreno;
            this.nomeFerArr = nomeFerArr;
            this.nomeFerPart = nomeFerPart;
            this.oraFerArr = oraFerArr;
            this.oraFerPart = oraFerPart;
        }
    }

    public static class TrenoPercorsoTre {
        public String idSol;
        public TrenoPercorsoF2[] soluzione;

        public TrenoPercorsoTre(String idSol, TrenoPercorsoF2[] soluzione) {
            this.idSol = idSol;
            this.soluzione = soluzione;
        }
    }

    public static HashMap<String, String> mapvec = null;

    public static String getVeicoloMod(String id, Context cx) {
        if (mapvec != null && mapvec.size() > 0) {
            if (mapvec.containsKey(id)) return mapvec.get(id);
            else return String.valueOf(Integer.parseInt(id) - 400000);
        } else {
            try {
                mapvec = new HashMap<>();
                JSONArray veicoli = new JSONArray(ClassU.StringFromResource(cx, R.raw.veicoli));
                for (int i = 0; i < veicoli.length(); i++) {
                    JSONObject obj = veicoli.getJSONObject(i);
                    mapvec.put(obj.getString("id"), (obj.getInt("id") - 400000) + " - " + obj.getString("modello"));
                }
                return mapvec.get(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }
    public static Boolean isNetworkAvailable(Activity application) {
        ConnectivityManager connectivityManager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network nw = connectivityManager.getActiveNetwork();
            if (nw == null) return false;
            NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
            return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
        } else {
            NetworkInfo nwInfo = connectivityManager.getActiveNetworkInfo();
            return nwInfo != null && nwInfo.isConnected();
        }
    }
}
