package com.github.nexus.configuration;

import java.util.List;

public interface Configuration {

    String keygenBasePath();

    List<String> publicKeys();

    List<String> privateKeys();

    String url();

    int port();

    List<String> othernodes();

}