package com.yuukiyg.circuitbreakerclient.apbase;

import java.net.URI;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;


/**
 * <p>
 * マイクロサービスとの通信を行う部品クラス。
 * </p>
 */
@Component
public class MicroserviceRestCommunicator extends SimpleRestCommunicator{

    //TODO: ここはAutowiredではなく、普通にnewするべきかも。
    //@Autowired
    //RestTemplate restTemplate;

    private final String requestTrackId = "REQUEST_TRACE_ID";
    private final String sessionTrackId = "SESSION_TRACE_ID";


    /**
     * {@inheritDoc}
     */
    @HystrixCommand(fallbackMethod = "fallback", commandProperties = {
            @HystrixProperty(name = "execution.timeout.enabled", value = "true"),
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000") })
    @Override
    public ResponseEntity<String> sendGET(URI uri,
            MultiValueMap<String, String> urlParameters,
            MultiValueMap<String, String> headers) {

        RequestEntity<String> requestEntity = createGetRequest(uri, urlParameters, headers);
        return send(requestEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<String> sendPOST(URI uri,
            MultiValueMap<String, String> urlParameters,
            MultiValueMap<String, String> headers, String bodyJson) {

        RequestEntity<String> requestEntity = createPostRequest(uri, urlParameters, headers, bodyJson);
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

        setCommonHeaders(headers);
        return super.createGetRequest(uri, urlParameters, headers);
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

        setCommonHeaders(headers);
        return super.createPostRequest(uri, urlParameters, headers, bodyJson);
    }

    /**
     * 共通のHTTPヘッダを設定する。
     * @param headers
     */
    protected void setCommonHeaders(MultiValueMap<String, String> headers) {
        // headerにセッショントレースIDを差し込む
        if (!headers.containsKey(sessionTrackId)) {
            headers.add(sessionTrackId, (String) MDC.get(sessionTrackId));
        }

        // headerにリクエストトレースIDを差し込む
        if (!headers.containsKey(requestTrackId)) {
            headers.add(requestTrackId, (String) MDC.get(requestTrackId));
        }
    }

    /**
     * HTTPリクエストを送信する。
     * @param request
     * @return
     */
    //これだとうまくいかない。。。。
    @HystrixCommand(fallbackMethod = "fallback", commandProperties = {
            @HystrixProperty(name = "execution.timeout.enabled", value = "true"),
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000") })
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

    /**
     * サーキットブレーカ用のフォールバックメソッド.
     *
     * @return
     */
    protected ResponseEntity<String> fallback(RequestEntity<?> request){
        System.out.println("fallback called!!!!!!!!!!!!!!!!!!!!!!!");
        return new ResponseEntity<String>("fallback!!", HttpStatus.ACCEPTED);
    }


    public ResponseEntity<String> fallback(URI uri,
            MultiValueMap<String, String> urlParameters,
            MultiValueMap<String, String> headers) {
        System.out.println("fallback called (GET)!!!!!!!!!!!!!!!!!!!!!!!");
        return new ResponseEntity<String>("fallback (GET)!!", HttpStatus.ACCEPTED);
    }

}
