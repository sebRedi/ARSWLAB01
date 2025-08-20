/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.BlackListSearcher;
import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class HostBlackListsValidator {

    private static final int BLACK_LIST_ALARM_COUNT=5;
    
    /**
     * Check the given host's IP address in all the available black lists,
     * and report it as NOT Trustworthy when such IP was reported in at least
     * BLACK_LIST_ALARM_COUNT lists, or as Trustworthy in any other case.
     * The search is not exhaustive: When the number of occurrences is equal to
     * BLACK_LIST_ALARM_COUNT, the search is finished, the host reported as
     * NOT Trustworthy, and the list of the five blacklists returned.
     * @param ipaddress suspicious host's IP address.
     * @return  Blacklists numbers where the given host's IP address was found.
     */
    public List<Integer> checkHost(String ipaddress, int threads){

        LinkedList<Integer> blackListOcurrences=new LinkedList<>();

        HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();

        List<int[]> indexes = divideSegments(skds.getRegisteredServersCount(), threads); /*Obtenemos índices*/
        ArrayList<BlackListSearcher> searchers = new ArrayList(); /*Creamos la lista con 0 hilos*/
        for (int[] pair : indexes) {
            BlackListSearcher localThread = new BlackListSearcher(pair[0], pair[1], ipaddress);
            localThread.start();
            searchers.add(localThread); /*Agregamos un hilo por cada segmento solicitado*/
        }
        /*Hasta este punto ya tenemos N hilos trabajando en la búsqueda*/

        for (BlackListSearcher s : searchers) {
            try {
                s.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } /*Esperamos a que todos terminen*/

        int ocurrencesCount = 0;
        int checkedListsCount = 0;

        for (BlackListSearcher s : searchers) {
            blackListOcurrences.addAll(s.getResultsIndexes());
            ocurrencesCount += s.getResultsIndexes().size();
            checkedListsCount += s.getCount();
        } /*Recolectamos los resultados de cada hilo*/

        if (ocurrencesCount >= BLACK_LIST_ALARM_COUNT) {
            skds.reportAsNotTrustworthy(ipaddress);
        } else {
            skds.reportAsTrustworthy(ipaddress);
        } /*Reportamos según la regla*/

        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}",
                new Object[]{checkedListsCount, skds.getRegisteredServersCount()});

        return blackListOcurrences;
    }
    
    
    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());

    private static List<int[]> divideSegments(int L/*Longitud total*/, int N/*Cantidad de hilos*/) {
        List<int[]> indexes = new ArrayList<>();

        int base = L / N;
        int rest = L % N;

        int start = 0;
        for (int i = 0; i < N; i++) {
            int len = base + (i < rest ? 1 : 0);
            int end = start + len;
            indexes.add(new int[]{start, end});
            start = end;
        }
        return indexes;

        /*
        Podemos usar los resultados de la forma:
        for (int[] par : dividesegments(..)) {
            BlackListSearcher(par[0], par[1], ip)
        }
         */
    }
    
}
