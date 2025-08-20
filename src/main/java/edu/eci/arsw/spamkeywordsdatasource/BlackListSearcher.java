package edu.eci.arsw.spamkeywordsdatasource;

import java.util.ArrayList;
import java.util.List;

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

    public BlackListSearcher(int from, int to, String ip) {
        this.from = from;
        this.to = to;
        this.ip = ip;
        this.count = 0;
        this.resultsIndexes = new ArrayList<Integer>();
    }

    @Override
    public void run() {
        HostBlacklistsDataSourceFacade skds = HostBlacklistsDataSourceFacade.getInstance(); /* Por la forma en que se usa en Main, podemos interpretar que es un singleton que podemos consultar en cualquier momento */
        for(int i = from; i < to; i++) /*Los cortes que se le asignarán a cada hilo*/ {
            if(skds.isInBlackListServer(i, ip)){
                resultsIndexes.add(i);
            }
            count++;
        }

    }

    public List<Integer> getResultsIndexes() {
        return resultsIndexes; /*Como lo pide el enunciado*/
    }

    public int getCount() {
        return count;
    }

}
