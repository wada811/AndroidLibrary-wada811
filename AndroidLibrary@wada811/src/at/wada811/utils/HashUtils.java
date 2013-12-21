/*
 * Copyright 2013 wada811<at.wada811@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.wada811.utils;
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import at.wada811.codec.EncodingUtils;
import at.wada811.codec.Hex;
import at.wada811.codec.MessageDigestAlgorithms;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Operations to simplify common {@link java.security.MessageDigest} tasks.
 * This class is immutable and thread-safe.
 * 
 * @version $Id: DigestUtils.java 1465850 2013-04-09 00:46:31Z sebb $
 */
public class HashUtils {

    /**
     * Returns a <code>MessageDigest</code> for the given <code>algorithm</code>.
     * 
     * @param algorithm
     *        the name of the algorithm requested. See <a
     *        href="http://java.sun.com/j2se/1.3/docs/guide/security/CryptoSpec.html#AppA">Appendix
     *        A in the Java
     *        Cryptography Architecture API Specification & Reference</a> for information about
     *        standard algorithm
     *        names.
     * @return An MD5 digest instance.
     * @see MessageDigest#getInstance(String)
     * @throws IllegalArgumentException
     *         when a {@link NoSuchAlgorithmException} is caught.
     */
    private static MessageDigest getDigest(final String algorithm){
        try{
            return MessageDigest.getInstance(algorithm);
        }catch(final NoSuchAlgorithmException e){
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Returns an MD5 MessageDigest.
     * 
     * @return An MD5 digest instance.
     * @throws IllegalArgumentException
     *         when a {@link NoSuchAlgorithmException} is caught, which should never happen because
     *         MD5 is a
     *         built-in algorithm
     * @see MessageDigestAlgorithms#MD5
     */
    private static MessageDigest getMd5Digest(){
        return getDigest(MessageDigestAlgorithms.MD5);
    }

    /**
     * Returns an SHA-1 digest.
     * 
     * @return An SHA-1 digest instance.
     * @throws IllegalArgumentException
     *         when a {@link NoSuchAlgorithmException} is caught, which should never happen because
     *         SHA-1 is a
     *         built-in algorithm
     * @see MessageDigestAlgorithms#SHA_1
     * @since 1.7
     */
    private static MessageDigest getSha1Digest(){
        return getDigest(MessageDigestAlgorithms.SHA_1);
    }

    /**
     * Returns an SHA-256 digest.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     * 
     * @return An SHA-256 digest instance.
     * @throws IllegalArgumentException
     *         when a {@link NoSuchAlgorithmException} is caught, which should never happen because
     *         SHA-256 is a
     *         built-in algorithm
     * @see MessageDigestAlgorithms#SHA_256
     */
    private static MessageDigest getSha256Digest(){
        return getDigest(MessageDigestAlgorithms.SHA_256);
    }

    /**
     * Returns an SHA-512 digest.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     * 
     * @return An SHA-512 digest instance.
     * @throws IllegalArgumentException
     *         when a {@link NoSuchAlgorithmException} is caught, which should never happen because
     *         SHA-512 is a
     *         built-in algorithm
     * @see MessageDigestAlgorithms#SHA_512
     */
    private static MessageDigest getSha512Digest(){
        return getDigest(MessageDigestAlgorithms.SHA_512);
    }

    /**
     * Calculates the MD5 digest and returns the value as a 16 element <code>byte[]</code>.
     * 
     * @param data
     *        Data to digest
     * @return MD5 digest
     */
    public static byte[] md5(final byte[] data){
        return getMd5Digest().digest(data);
    }

    /**
     * Calculates the MD5 digest and returns the value as a 16 element <code>byte[]</code>.
     * 
     * @param data
     *        Data to digest
     * @return MD5 digest
     */
    public static byte[] md5(final String data){
        return md5(EncodingUtils.getBytesUtf8(data));
    }

    /**
     * Calculates the MD5 digest and returns the value as a 32 character hex string.
     * 
     * @param data
     *        Data to digest
     * @return MD5 digest as a hex string
     */
    public static String toMD5(final byte[] data){
        return Hex.encodeHexString(md5(data));
    }

    /**
     * Calculates the MD5 digest and returns the value as a 32 character hex string.
     * 
     * @param data
     *        Data to digest
     * @return MD5 digest as a hex string
     */
    public static String toMD5(final String data){
        return Hex.encodeHexString(md5(data));
    }

    /**
     * Calculates the SHA-1 digest and returns the value as a <code>byte[]</code>.
     * 
     * @param data
     *        Data to digest
     * @return SHA-1 digest
     * @since 1.7
     */
    public static byte[] sha1(final byte[] data){
        return getSha1Digest().digest(data);
    }

    /**
     * Calculates the SHA-1 digest and returns the value as a <code>byte[]</code>.
     * 
     * @param data
     *        Data to digest
     * @return SHA-1 digest
     */
    public static byte[] sha1(final String data){
        return sha1(EncodingUtils.getBytesUtf8(data));
    }

    /**
     * Calculates the SHA-1 digest and returns the value as a hex string.
     * 
     * @param data
     *        Data to digest
     * @return SHA-1 digest as a hex string
     * @since 1.7
     */
    public static String toSHA1(final byte[] data){
        return Hex.encodeHexString(sha1(data));
    }

    /**
     * Calculates the SHA-1 digest and returns the value as a hex string.
     * 
     * @param data
     *        Data to digest
     * @return SHA-1 digest as a hex string
     * @since 1.7
     */
    public static String toSHA1(final String data){
        return Hex.encodeHexString(sha1(data));
    }

    /**
     * Calculates the SHA-256 digest and returns the value as a <code>byte[]</code>.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     * 
     * @param data
     *        Data to digest
     * @return SHA-256 digest
     * @since 1.4
     */
    public static byte[] sha256(final byte[] data){
        return getSha256Digest().digest(data);
    }

    /**
     * Calculates the SHA-256 digest and returns the value as a <code>byte[]</code>.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     * 
     * @param data
     *        Data to digest
     * @return SHA-256 digest
     * @since 1.4
     */
    public static byte[] sha256(final String data){
        return sha256(EncodingUtils.getBytesUtf8(data));
    }

    /**
     * Calculates the SHA-256 digest and returns the value as a hex string.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     * 
     * @param data
     *        Data to digest
     * @return SHA-256 digest as a hex string
     * @since 1.4
     */
    public static String toSHA256(final byte[] data){
        return Hex.encodeHexString(sha256(data));
    }

    /**
     * Calculates the SHA-256 digest and returns the value as a hex string.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     * 
     * @param data
     *        Data to digest
     * @return SHA-256 digest as a hex string
     * @since 1.4
     */
    public static String toSHA256(final String data){
        return Hex.encodeHexString(sha256(data));
    }

    /**
     * Calculates the SHA-512 digest and returns the value as a <code>byte[]</code>.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     * 
     * @param data
     *        Data to digest
     * @return SHA-512 digest
     * @since 1.4
     */
    public static byte[] sha512(final byte[] data){
        return getSha512Digest().digest(data);
    }

    /**
     * Calculates the SHA-512 digest and returns the value as a <code>byte[]</code>.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     * 
     * @param data
     *        Data to digest
     * @return SHA-512 digest
     * @since 1.4
     */
    public static byte[] sha512(final String data){
        return sha512(EncodingUtils.getBytesUtf8(data));
    }

    /**
     * Calculates the SHA-512 digest and returns the value as a hex string.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     * 
     * @param data
     *        Data to digest
     * @return SHA-512 digest as a hex string
     * @since 1.4
     */
    public static String toSHA512(final byte[] data){
        return Hex.encodeHexString(sha512(data));
    }

    /**
     * Calculates the SHA-512 digest and returns the value as a hex string.
     * <p>
     * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
     * </p>
     * 
     * @param data
     *        Data to digest
     * @return SHA-512 digest as a hex string
     * @since 1.4
     */
    public static String toSHA512(final String data){
        return Hex.encodeHexString(sha512(data));
    }
}
