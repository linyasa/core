package com.dotmarketing.util.jwt;

import com.dotmarketing.util.Config;
import com.liferay.util.Base64;

import java.security.Key;

/**
 * Created by jsanca on 6/23/16.
 */
public class HashSigningKeyFactoryImpl implements SigningKeyFactory {


    @Override
    public Key getKey() {

        final String hashKey = Config.getStringProperty
                ("hash.signing.key",
                        "rO0ABXNyABRqYXZhLnNlY3VyaXR5LktleVJlcL35T7OImqVDAgAETAAJYWxnb3JpdGhtdAASTGphdmEvbGFuZy9TdHJpbmc7WwAHZW5jb2RlZHQAAltCTAAGZm9ybWF0cQB+AAFMAAR0eXBldAAbTGphdmEvc2VjdXJpdHkvS2V5UmVwJFR5cGU7eHB0AANERVN1cgACW0Ks8xf4BghU4AIAAHhwAAAACBksSlj3ReywdAADUkFXfnIAGWphdmEuc2VjdXJpdHkuS2V5UmVwJFR5cGUAAAAAAAAAABIAAHhyAA5qYXZhLmxhbmcuRW51bQAAAAAAAAAAEgAAeHB0AAZTRUNSRVQ=");

        return (Key) Base64.stringToObject(hashKey);
    }
}
