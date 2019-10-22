/**
 * @(#)FrontController.java
 */
package com.yuukiyg.circuitbreakerclient.app;

import java.net.URI;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.yuukiyg.circuitbreakerclient.apbase.RestCommunicator;

@Component
public class SendRestServiceImpl implements SendRestService{

    @Autowired
    private RestCommunicator comm;

    @Value("${targetserver.port}")
    private int serverPort;

    @Override
    public void execute() {
        URI uri = URI.create("http://localhost:"+serverPort+"/wait");
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        MultiValueMap<String, String> urlParameters = new LinkedMultiValueMap<String, String>();
        urlParameters.add("time", "10000");
        urlParameters.add("name", "NG");
        System.out.println("[NG] sending at " + LocalDateTime.now());
        ResponseEntity<String> ret = comm.sendGET(uri, urlParameters, headers);
        System.out.println("[NG]" + ret.getStatusCode() + ": "+ ret.getBody() + "at " + LocalDateTime.now());


        urlParameters.clear();
        urlParameters.add("time", "2000");
        urlParameters.add("name", "OK");
        System.out.println("[OK] sending at " + LocalDateTime.now());
        ret = comm.sendGET(uri, urlParameters, headers);
        System.out.println("[OK]" + ret.getStatusCode() + ": "+ ret.getBody() + "at " + LocalDateTime.now());
    }

}
