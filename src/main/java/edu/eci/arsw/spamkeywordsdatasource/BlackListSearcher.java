package edu.eci.arsw.spamkeywordsdatasource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author sebastianGalvis
 */
public class BlackListSearcher extends Thread /*Usaremos la herencia en lugar de la implementación de la interfaz, pues así lo solicita el ejercicio*/{
    private int from; /*El punto inicial del segmento asignado al hilo*/
    private int to; /*El punto final del semgneto asignado al hilo*/
    private String ip;
    private List<Integer> resultsIndexes; /*Vamos a ir guardando los índices de aquellas blacklist que reportaron la ip*/
    private int count;
    private int limit;
    private AtomicInteger globalOccurrences;/*Referencia compartida*/

    /*Modificamos el hilo para que conozca la cantidad máxima de ocurrencias permitida y para que tenga el contador compartido*/
    public BlackListSearcher(int from, int to, String ip, int limit, AtomicInteger globalOccurrences) {
        this.from = from;
        this.to = to;
        this.ip = ip;
        this.count = 0;
        this.resultsIndexes = new ArrayList<Integer>();
        this.limit = limit;
        this.globalOccurrences = globalOccurrences;
    }

    @Override
    public void run() {
        HostBlacklistsDataSourceFacade skds = HostBlacklistsDataSourceFacade.getInstance(); /* Por la forma en que se usa en Main, podemos interpretar que es un singleton que podemos consultar en cualquier momento */
        for (int i = from; i < to; i++) {
            /*Si el límite ya fue alcanzado entre todos los hilos, se detienen*/
            if (globalOccurrences.get() >= limit) break;

            count++;
            if (skds.isInBlackListServer(i, ip)) {
                resultsIndexes.add(i);
                globalOccurrences.incrementAndGet(); /*Aumenta el contador compartido*/
            }
        }

    }

    public List<Integer> getResultsIndexes() {
        return resultsIndexes; /*Como lo pide el enunciado*/
    }

    public int getCount() {
        return count;
    }

}
