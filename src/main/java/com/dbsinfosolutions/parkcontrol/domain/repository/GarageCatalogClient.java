package com.dbsinfosolutions.parkcontrol.domain.repository;

import java.util.List;

import com.dbsinfosolutions.parkcontrol.domain.model.GarageCatalog;

public interface GarageCatalogClient {

    List<GarageCatalog> fetchGarages();
}
