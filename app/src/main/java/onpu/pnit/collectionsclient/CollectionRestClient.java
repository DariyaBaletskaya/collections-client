package onpu.pnit.collectionsclient;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import onpu.pnit.collectionsclient.entities.Collection;

public class CollectionRestClient {

    private String BASE_URL = "https://collections-blue.herokuapp.com/collections/";
    private RestTemplate restTemplate = new RestTemplate();

    public Collection find(long id) {
        try {
            return restTemplate.getForObject(BASE_URL + id, Collection.class);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Collection> findAll() {
        try {
            return restTemplate.exchange(BASE_URL,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Collection>>() {
                    }).getBody();
        } catch (Exception e) {
            return  null;
        }
    }
}
