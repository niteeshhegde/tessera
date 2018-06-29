package com.github.nexus.key;


import com.github.nexus.config.KeyData;
import com.github.nexus.nacl.Key;
import com.github.nexus.nacl.KeyPair;

import java.util.Set;

/**
 * Manages local keys for the running node, include key lookups and key loading/generation
 */
public interface KeyManager {

    /**
     * Fetches the public key that corresponds to the given private key
     * This means that the returned public key is under control of the current node
     * (i.e. it is one of our keys)
     *
     * @param privateKey the private key for which to find the public key
     * @return the corresponding public key
     * @throws RuntimeException if the private key could not be found
     */
    Key getPublicKeyForPrivateKey(Key privateKey);

    /**
     * Fetches the private key that corresponds to the given public key
     * This means that the returned private key is under control of the current node
     * (i.e. it is one of our keys)
     *
     * @param publicKey the public key for which to find the private key
     * @return the corresponding private key
     * @throws RuntimeException if the public key could not be found
     */
    Key getPrivateKeyForPublicKey(Key publicKey);

    /**
     * Loads the specified keys from file
     * The password may be null if not required
     *
     * @param keyData a data object containing a public key, private key json and a password
     * @return the loaded {@link KeyPair}
     */
    KeyPair loadKeypair(KeyData keyData);

    /**
     * Return a list of all recipients public keys of this node
     *
     * @return the set of all public keys
     */
    Set<Key> getPublicKeys();

    /**
     * Returns the default public key of this node if a transaction is
     * sent that doesn't have a sender.
     *
     * Must return the same key each time during a single run,
     * but no guarantee is made about which key is returned if the
     * application is restarted.
     *
     * @return The public key to use as the default sender
     */
    Key defaultPublicKey();

}