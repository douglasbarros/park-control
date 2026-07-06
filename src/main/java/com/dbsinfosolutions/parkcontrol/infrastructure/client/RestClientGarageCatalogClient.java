package com.dbsinfosolutions.parkcontrol.infrastructure.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.dbsinfosolutions.parkcontrol.domain.model.GarageCatalog;
import com.dbsinfosolutions.parkcontrol.domain.model.SpotCatalog;
import com.dbsinfosolutions.parkcontrol.domain.repository.GarageCatalogClient;
import com.dbsinfosolutions.parkcontrol.infrastructure.client.dto.GarageCatalogResponse;

@Component
public class RestClientGarageCatalogClient implements GarageCatalogClient {

    private final RestClient restClient;

    public RestClientGarageCatalogClient(RestClient.Builder builder,
            @Value("${parkcontrol.garage-url}") String garageUrl) {
        this.restClient = builder.baseUrl(garageUrl).build();
    }

    @Override
    public List<GarageCatalog> fetchGarages() {
        GarageCatalogResponse response =
                restClient.get().retrieve().body(GarageCatalogResponse.class);
        if (response == null || response.garage() == null) {
            return List.of();
        }

        return response.garage().stream()
                .map(item -> new GarageCatalog(item.sector(), item.basePrice(), item.maxCapacity(),
                        item.openHour(), item.closeHour(), item.durationLimitMinutes(),
                        response.spots() == null ? List.of()
                                : response.spots().stream()
                                        .filter(spot -> item.sector().equals(spot.sector()))
                                        .map(spot -> new SpotCatalog(spot.id(), spot.sector(),
                                                spot.lat(), spot.lng(), spot.occupied()))
                                        .toList()))
                .toList();
    }
}
