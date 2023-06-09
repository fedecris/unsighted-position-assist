package ar.edu.info.lidi.upa.assist;

import ar.edu.info.lidi.upa.exception.ProcessingException;

public interface ProcessCompletedCallBackInterface {

    /**
     * Finalizacion de entrenamiento de ubicacion
     * @param message mensaje resultante del procesamiento
     */
    void trainingCompleted(String message);


    /**
     * Finalizacion de estimacion de ubicacion
     * @param message mensaje resultante del procesamiento
     */
    void estimationCompleted(String message);

    /**
     * Se produjo un error en la actividad
     * @param ex mensaje de error obtenido
     */
    void processingError(ProcessingException ex);
}
