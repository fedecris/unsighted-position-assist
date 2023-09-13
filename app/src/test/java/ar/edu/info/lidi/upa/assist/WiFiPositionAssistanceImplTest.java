package ar.edu.info.lidi.upa.assist;
import org.junit.Test;

import java.util.Random;

import ar.edu.info.lidi.upa.exception.ProcessingException;
import ar.edu.info.lidi.upa.model.Location;
import ar.edu.info.lidi.upa.model.ScanDetail;
import ar.edu.info.lidi.upa.model.TrainingSet;
import ar.edu.info.lidi.upa.utils.Constants;
import ar.edu.info.lidi.upa.utils.SignalUtils;

import static org.junit.Assert.*;

public class WiFiPositionAssistanceImplTest implements ProcessCompletedCallBackInterface {

    WiFiPositionAssistanceImpl wpa = null;
    TrainingSet trainingSet = null;
    final int MAX_LOCATIONS = 10;
    final int MAX_BSSIDS_PER_LOCATION = 10;

    protected TrainingSet loadTrainingSet() {
        Random random = new Random();
        trainingSet = new TrainingSet();

        // Cargar las locations
        for (int loc=1; loc<=MAX_LOCATIONS; loc++) {
            Location aLocation = new Location("LOCATION_" + loc);
            // Cargar las intensidades de cada location
            for (int bssid=1; bssid<=MAX_BSSIDS_PER_LOCATION; bssid++) {
                String name = "BSSID_"+bssid;
                int rssi = random.nextInt(-1* SignalUtils.MAX_RSSI) + SignalUtils.MIN_RSSI;
                int level = SignalUtils.normalize(rssi);
                aLocation.getScanDetails().add(new ScanDetail(name, level, rssi));
            }
            trainingSet.getLocations().add(aLocation);
        }
        System.out.println(trainingSet);
        return trainingSet;
    }


    @Test
    public void exactSignalLevelsShouldMatchAtLeasth80Percent()  {
        wpa = new WiFiPositionAssistanceImpl();
        wpa.setTrainingSet(loadTrainingSet());
        wpa.setIface(this);
        //wpa.setStrategy(new WiFiEuclideanLocationStrategy(wpa));
        wpa.setStrategy(new WiFiNearestLocationStrategy(wpa));
        int matches=0;
        for (int loc=1; loc<=MAX_LOCATIONS; loc++) {
            Location currentLoc = trainingSet.getLocations().get(loc-1);
            Location toEstimate = new Location(currentLoc.getName());
            System.out.println(String.format("\n>> Estimando %s...", toEstimate.getName()));
            currentLoc.getScanDetails().forEach(sd -> toEstimate.getScanDetails().add(new ScanDetail(sd.getBbsid(), sd.getLevel(), sd.getRssi())));
            if (("LOCATION_"+loc).equalsIgnoreCase(wpa.estimateLocation(toEstimate.getScanDetails()))) {
                matches++;
            }
        }
        float rate = 1f * matches / MAX_LOCATIONS * 100;
        System.out.println("[ === EFECTIVIDAD (%): " + rate + " === ]");
        assertTrue(rate >= 80) ;
    }


    @Override
    public void trainingCompleted(String message) {
        System.out.println(message);
    }

    @Override
    public void estimationCompleted(String message) {
        System.out.println("Estimacion: " + message);
    }

    @Override
    public void processingError(ProcessingException ex) {
        System.err.println(ex.getMessage());
    }
}