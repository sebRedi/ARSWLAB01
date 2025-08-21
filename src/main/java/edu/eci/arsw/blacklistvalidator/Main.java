/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import java.util.List;

/**
 *
 * @author hcadavid
 */
public class Main {
    
    public static void main(String a[]){
        HostBlackListsValidator hblv=new HostBlackListsValidator();

        int cores = Runtime.getRuntime().availableProcessors();

        long start = System.currentTimeMillis();
        List<Integer> blackListOcurrences=hblv.checkHost("200.24.34.55", cores);
        long end = System.currentTimeMillis();
        long duration = end - start;
        System.out.println("The host was found in the following blacklists:"+blackListOcurrences);
        System.out.println("Search time: " + duration + " ms");

        /*
        try{
            Thread.sleep(3_000);
        }
        catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
        */

        
    }
    
}
