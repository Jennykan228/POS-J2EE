package com.cafedemetro.pos;

import javax.ejb.Remote;

//1. Annotation to indicate this is a remote interface.
@Remote
public interface OrderMgtRemote {

    String CreateOrder(String branchCode, int numOfSeat) throws Exception;

    boolean UpdateOrderInDB(String qrCode, String targetStatus) throws Exception ;

    String PrintOrder(String qrCode) throws Exception;

    String CloseOrder(String qrCode) throws Exception;
    
}
