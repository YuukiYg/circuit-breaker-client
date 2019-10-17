package com.yuukiyg.circuitbreakerclient.apbase;

import java.net.URI;

import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * [TODO write an overview of the class]<br>
 * <p>
 * [TODO write a specification of the class]
 * </p>
 */
public class SimpleRestCommunicator implements RestCommunicator{
    //TODO: ここはAutowiredではなく、普通にnewするべきかも。
    //@Autowired
    //RestTemplate restTemplate;

    RestTemplate restTemplate = new RestTemplate();


    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<String> sendGET(URI uri,
            MultiValueMap<String, String> urlParameters,
            MultiValueMap<String, String> headers) {

        RequestEntity<String> requestEntity = createGetRequest(uri,
                urlParameters, headers);

        return send(requestEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<String> sendPOST(URI uri,
            MultiValueMap<String, String> urlParameters,
            MultiValueMap<String, String> headers, String bodyJson) {

        RequestEntity<String> requestEntity = createPostRequest(uri,
                urlParameters, headers, bodyJson);

        return send(requestEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<String> sendGET(URI uri, MultiValueMap<String, String> headers) {
        return sendGET(uri, null, headers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<String> sendPOST(URI uri, MultiValueMap<String, String> headers, String bodyJson) {
        return sendPOST(uri, null, headers, bodyJson);
    }

    /**
     * GETリクエストを作成する。<br>
     * ヘッダにリクエストトレース用のIDを差し込む。
     * @param uri
     * @param urlParameters
     * @param headers
     * @return
     */
    protected RequestEntity<String> createGetRequest(URI uri,
            MultiValueMap<String, String> urlParameters,
            MultiValueMap<String, String> headers) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(uri);

        if(urlParameters != null && !urlParameters.isEmpty()) {
            builder.queryParams(urlParameters);
        }

        RequestEntity<String> request = new RequestEntity<String>(headers, HttpMethod.GET, builder
                .build().toUri());

        return request;
    }

    /**
     * POSTリクエストを作成する。<br>
     * ヘッダにリクエストトレース用のIDを差し込む。
     * @param uri
     * @param urlParameters
     * @param headers
     * @param bodyJson
     * @return
     */
    protected RequestEntity<String> createPostRequest(URI uri,
            MultiValueMap<String, String> urlParameters,
            MultiValueMap<String, String> headers, String bodyJson) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(uri);

        if(urlParameters != null && !urlParameters.isEmpty()) {
            builder.queryParams(urlParameters);
        }

        RequestEntity<String> request = new RequestEntity<String>(bodyJson, headers, HttpMethod.POST, builder
                .build().toUri());

        return request;
    }

    /**
     * HTTPリクエストを送信する。
     * @param request
     * @return
     */
    protected ResponseEntity<String> send(RequestEntity<?> request) {
        ResponseEntity<String> response = null;
        if (request != null) {
            try {
                response = restTemplate.exchange(request, String.class);
            } catch (Exception e) {
                throw e;
            }
        }
        return response;
    }

}
