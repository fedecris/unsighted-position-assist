package ar.edu.info.lidi.upa.model;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Random;

import ar.edu.info.lidi.upa.utils.SignalUtils;

public class ScanDetail {

    /** Basic Service Set Identifier */
    protected String bbsid;
    /** Intensidad de la señal (normalizado segun <code>{@link ar.edu.info.lidi.upa.utils.SignalUtils}</code>) */
    protected int level;
    /** Intensidad de la señal (rssi) */
    protected int rssi;

    public ScanDetail(@JsonProperty("bbsid") String bbsid, @JsonProperty("signalStrength") int level) {
        this.bbsid = bbsid;
        this.level = level;
    }

    @JsonCreator
    public ScanDetail(@JsonProperty("bbsid") String bbsid, @JsonProperty("signalStrength") int level, @JsonProperty("rssi") int rssi) {
        this.bbsid = bbsid;
        this.level = level;
        this.rssi = rssi;
    }

    public String getBbsid() {
        return bbsid;
    }

    public void setBbsid(String bbsid) {
        this.bbsid = bbsid;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public ScanDetail clone() {
        return new ScanDetail(bbsid, level, rssi);
    }

    public ScanDetail clone(int maxDeviation) {
        Random random = new Random();
        int deviation = random.nextInt(2 * maxDeviation + 1) - maxDeviation;
        rssi = rssi + deviation;
        level = SignalUtils.normalize(rssi);
        return new ScanDetail(bbsid, level, rssi);
    }

    @NonNull
    @Override
    public String toString() {
        return "ScanDetail{" +
                "bbsid='" + bbsid + '\'' +
                ", level=" + level +
                ", rssi=" + rssi +
                '}';
    }
}
