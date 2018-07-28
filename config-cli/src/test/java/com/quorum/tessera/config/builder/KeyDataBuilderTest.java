package com.quorum.tessera.config.builder;

import com.quorum.tessera.config.ConfigException;
import com.quorum.tessera.config.KeyData;
import com.quorum.tessera.config.test.FixtureUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

public class KeyDataBuilderTest {

    @Test
    public void buildThreeLocked() throws IOException {

        final Path passwordFile = Files.createTempFile("tessera-passwords", ".txt");

        List<Path> privateKeyPaths = Arrays.asList(
            Files.createTempFile("buildThreeLocked1", ".txt"),
            Files.createTempFile("buildThreeLocked2", ".txt"),
            Files.createTempFile("buildThreeLocked3", ".txt")
        );


        final byte[] privateKeyData = FixtureUtil.createLockedPrivateKey().toString().getBytes();
        for (Path p : privateKeyPaths) {
            Files.write(p, privateKeyData);
        }

        List<String> publicKeys = Arrays.asList("PUB1", "PUB2", "PUB3");
        List<String> privateKeys = privateKeyPaths.stream().map(Path::toString).collect(Collectors.toList());

        Files.write(passwordFile, Arrays.asList("SECRET1", "SECRET2", "SECRET3"));

        List<KeyData> result = KeyDataBuilder.create()
            .withPrivateKeys(privateKeys)
            .withPublicKeys(publicKeys)
            .withPrivateKeyPasswordFile(passwordFile)
            .build()
            .getKeyData();

        assertThat(result).hasSize(3);

    }

    @Test
    public void differentAmountOfKeysThrowsError() {

        final KeyDataBuilder keyDataBuilder = KeyDataBuilder.create()
            .withPrivateKeys(Collections.emptyList())
            .withPublicKeys(Collections.singletonList("keyfile.txt"))
            .withPrivateKeyPasswordFile(Paths.get("pwfile.txt"));

        final Throwable throwable = catchThrowable(keyDataBuilder::build);

        assertThat(throwable)
            .isInstanceOf(ConfigException.class)
            .hasCauseExactlyInstanceOf(RuntimeException.class);

        assertThat(throwable.getCause()).hasMessage("Different amount of public and private keys supplied");

    }

}