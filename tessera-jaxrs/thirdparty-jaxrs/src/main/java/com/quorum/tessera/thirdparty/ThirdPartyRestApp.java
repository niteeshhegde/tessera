package com.quorum.tessera.thirdparty;

import com.quorum.tessera.api.filter.IPWhitelistFilter;
import com.quorum.tessera.app.TesseraRestApplication;
import com.quorum.tessera.config.AppType;
import com.quorum.tessera.core.api.ServiceFactory;
import com.quorum.tessera.partyinfo.PartyInfoService;
import io.swagger.annotations.Api;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.ApplicationPath;

/** The third party API */
@Api
@ApplicationPath("/")
public class ThirdPartyRestApp extends TesseraRestApplication {

    private final PartyInfoService partyInfoService;

    public ThirdPartyRestApp() {
        final ServiceFactory serviceFactory = ServiceFactory.create();
        this.partyInfoService = serviceFactory.partyInfoService();
    }

    @Override
    public Set<Object> getSingletons() {

        final IPWhitelistFilter iPWhitelistFilter = new IPWhitelistFilter();
        final RawTransactionResource rawTransactionResource = new RawTransactionResource();
        final PartyInfoResource partyInfoResource = new PartyInfoResource(partyInfoService);

        return Stream.of(iPWhitelistFilter, rawTransactionResource, partyInfoResource).collect(Collectors.toSet());
    }

    @Override
    public AppType getAppType() {
        return AppType.THIRD_PARTY;
    }
}
