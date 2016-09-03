package com.github.akinaru;

/**
 * Socket listener
 *
 * @author Bertrand Martel
 */
public interface ISocketListener {

    /***
     * called when a socket error occured
     *
     * @param e exception raised for this error
     */
    void onSocketError(Exception e);

}
