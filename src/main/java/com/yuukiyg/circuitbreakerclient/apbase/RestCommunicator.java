package com.yuukiyg.circuitbreakerclient.apbase;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

/**
 * [TODO write an overview of the class]<br>
 * <p>
 * [TODO write a specification of the class]
 * </p>
 */
public interface RestCommunicator {


    /**
     * GETリクエストの送信
     *
     * @param uri
     * @param urlParameters
     * @param headers
     * @return
     */
    public ResponseEntity<String> sendGET(URI uri, MultiValueMap<String, String> urlParameters,
            MultiValueMap<String, String> headers);

    /**
     * GETリクエストの送信
     *
     * @param uri
     * @param headers
     * @return
     */
    public ResponseEntity<String> sendGET(URI uri, MultiValueMap<String, String> headers);

    /**
     * POSTリクエストの送信
     *
     * @param uri
     * @param urlParameters
     * @param headers
     * @param bodyJson
     * @return
     */
    public ResponseEntity<String> sendPOST(URI uri, MultiValueMap<String, String> urlParameters,
            MultiValueMap<String, String> headers, String bodyJson);

    /**
     * POSTリクエストの送信
     *
     * @param uri
     * @param headers
     * @param bodyJson
     * @return
     */
    public ResponseEntity<String> sendPOST(URI uri, MultiValueMap<String, String> headers, String bodyJson);


}
