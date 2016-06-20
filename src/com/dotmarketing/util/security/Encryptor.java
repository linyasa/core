package com.dotmarketing.util.security;

import com.dotmarketing.cms.factories.PublicCompanyFactory;
import com.dotmarketing.cms.factories.PublicEncryptionFactory;
import com.dotmarketing.exception.DotRuntimeException;
import com.dotmarketing.util.Logger;
import com.liferay.portal.model.Company;
import com.liferay.util.EncryptorException;

import java.io.Serializable;
import java.security.Key;
import java.security.Provider;

/**
 * This class is just a wrapper to encapsulate the {@link com.liferay.util.Encryptor}
 * This approach provides the ability to inject, proxy, mock, use diff implementation based on a contract etc.
 *
 * @author jsanca
 */
public interface Encryptor extends Serializable {

    default Key generateKey() throws EncryptorException {

        return com.liferay.util.Encryptor.generateKey();
    }

    default Key generateKey(final String algorithm) throws EncryptorException {

        return com.liferay.util.Encryptor.generateKey(algorithm);
    }

    default Provider getProvider()
            throws ClassNotFoundException, IllegalAccessException,
            InstantiationException {

        return com.liferay.util.Encryptor.getProvider();
    }

    default String decrypt(final Key key,
                                  final String encryptedString)
            throws EncryptorException {

        return com.liferay.util.Encryptor.decrypt(key, encryptedString);
    }

    default String digest(final String text) {

        return com.liferay.util.Encryptor.digest(text);
    }

    default String digest(final String algorithm,
                                 final String text) {

       return com.liferay.util.Encryptor.digest(algorithm, text);
    }

    default String encrypt(final Key key, final String plainText)
            throws EncryptorException {

        return com.liferay.util.Encryptor.encrypt(key, plainText);
    }

    default String encryptString(final String x) {

        return PublicEncryptionFactory.encryptString(x);
    }

    default String decryptString(final String x) {

        return PublicEncryptionFactory.decryptString(x);
    }
} // E:O:F:Encryptor.
