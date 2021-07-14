package com.nextsense.nsutils.commons;

import android.os.Build;
import android.util.Base64;

import androidx.annotation.Nullable;

import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

@SuppressWarnings("unused")
public class EncryptionUtil {
    private static SecureRandom secureRandom;

    /**
     * Gets an overly complicated secure random generator
     * @return secure instance of SecureRandom
     */
    public static SecureRandom secureRandomInstance(boolean reinst, boolean prng, boolean weak) {
        if(reinst || secureRandom == null) {
            byte[] secureSeed;
            try {
                if (!prng && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    secureSeed = SecureRandom.getInstanceStrong().generateSeed(128);
                    secureRandom = SecureRandom.getInstanceStrong();
                } else {
                    if(weak) throw new NoSuchAlgorithmException();
                    secureSeed = SecureRandom.getInstance("SHA1PRNG").generateSeed(128);
                    secureRandom = SecureRandom.getInstance("SHA1PRNG");
                }
            } catch (NoSuchAlgorithmException e) {
                secureSeed = new SecureRandom().generateSeed(128);
                secureRandom = new SecureRandom();
            }

            NsLog.l("SEED", Base64.encodeToString(secureSeed, Base64.DEFAULT));

            secureRandom.setSeed(secureSeed);
            byte[] randomByte = new byte[1];
            secureRandom.nextBytes(randomByte);
            byte emptyRun = (byte) Math.abs(secureSeed[Math.abs(randomByte[0])]);
            for (int i = 0; i < emptyRun; i++) {
                secureRandom.nextBytes(new byte[Math.abs(secureSeed[i])]);
            }
        }

        return secureRandom;
    }

    /**
     * Get a secure random array
     * @param byteNum size of the random byte array
     * @return random byte array
     */
    public static byte[] secureRandom(int byteNum) {
        byte[] random = new byte[byteNum];
        secureRandomInstance(true, true, true).nextBytes(random);
        return random;
    }

    /**
     * Sign any type of data
     * @param keyPair public/Private key pair
     * @param algorithm string name of a signing algorithm ex. "SHA256WithRSA"
     * @param data byte array for signing
     * @return signature data in bytes
     * @throws Exception if generating or validating the signature fails
     */
    public static byte[] signData(KeyPair keyPair, String algorithm, byte[] data) throws Exception {
        Signature sig = Signature.getInstance(algorithm);
        sig.initSign(keyPair.getPrivate());
        sig.update(data);
        byte[] signatureBytes = sig.sign();
        sig.initVerify(keyPair.getPublic());
        sig.update(data);
        return signatureBytes;
    }

    /**
     * Asymmetric encryption of any data
     * @param publicKey public key for encryption
     * @param algorithm string name of the encryption algorithm ex. "RSA/ECB/PKCS1PADDING"
     * @param plainText byte data for asymmetric encryption
     * @return encrypted data
     * @throws Exception if encryption fails
     */
    public static byte[] encrypt(PublicKey publicKey, String algorithm, byte[] plainText) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(plainText);
    }

    /**
     * Asymmetric decryption of any data
     * @param privateKey private key for decryption
     * @param algorithm string name of the encryption algorithm ex. "RSA/ECB/PKCS1PADDING"
     * @param cipherText byte data for asymmetric encryption
     * @return decrypted data
     * @throws Exception if decryption fails
     */
    public static byte[] decrypt(PrivateKey privateKey, String algorithm, byte[] cipherText) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(cipherText);
    }

    /**
     * Generate an Private/Public key pair
     * @param algorithm key pair algorithm ex. "RSA"
     * @param keySize adequate key size
     * @return generated KeyPair
     * @throws NoSuchAlgorithmException if key generation fails
     */
    public static KeyPair generateKeyPair(String algorithm, int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(algorithm);
        kpg.initialize(keySize);
        return kpg.genKeyPair();
    }

    /**
     * Derive secret key from a string password
     * @param algorithm key algorithm key algorithm ex. "AES"
     * @param keyFactoryAlgorithm SecretKeyFactory algorithm name ex. "PBKDF2WithHmacSHA256"
     * @param iterationCount key generation rounds
     * @param keyLength key size
     * @param password string password
     * @return fully generated SecretKey
     * @throws Exception if generation failed
     */
    public static SecretKey deriveSecretKey(String algorithm, String keyFactoryAlgorithm, int iterationCount, int keyLength, String password) throws Exception {
        return deriveSecretKey(algorithm, keyFactoryAlgorithm, iterationCount, keyLength, password, secureRandom(8));
    }

    /**
     * Derive secret key from a string password with salt
     * @param algorithm key algorithm ex. "AES"
     * @param keyFactoryAlgorithm SecretKeyFactory algorithm name ex. "PBKDF2WithHmacSHA256"
     * @param iterationCount key generation rounds
     * @param keyLength key size
     * @param password string password
     * @param salt hash function generation random element
     * @return fully generated SecretKey
     * @throws Exception if generation failed
     */
    public static SecretKey deriveSecretKey(String algorithm, String keyFactoryAlgorithm, int iterationCount, int keyLength, String password, byte[] salt) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(keyFactoryAlgorithm);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterationCount, keyLength);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), algorithm);
    }

    /**
     * Derive secret key from bytes
     * @param keyBytes byte array with correct size
     * @param algorithm key algorithm ex. "AES"
     * @return fully generated SecretKey
     */
    public static SecretKey deriveSecretKey(byte[] keyBytes, String algorithm) {
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, algorithm);
    }

    /**
     * Get a randomized iv parameter spec
     * @param ivSize size of the iv
     * @return randomized iv parameter spec
     */
    public static IvParameterSpec randomIv(int ivSize) {
        final byte[] iv = new byte[ivSize];
        secureRandomInstance(false, false,false).nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    /**
     * Symmetric encryption of a plaintext
     * @param algorithm name of encryption algorithm ex. "AES/CBC/PKCS5Padding"
     * @param secretKey secret key object
     * @param cbcIv iv parameter spec
     * @param plaintext byte data for symmetric encryption
     * @return ciphertext
     * @throws Exception if encryption process fails
     */
    public static byte[] encrypt(String algorithm, SecretKey secretKey, IvParameterSpec cbcIv, byte[] plaintext) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        if(algorithm.contains("CBC")) {
            cbcIv = cbcIv == null ? new IvParameterSpec(new byte[16]) : cbcIv;
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, cbcIv);
        } else {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        }

        return cipher.doFinal(plaintext);
    }

    /**
     * Symmetric decryption of a ciphertext
     * @param algorithm name of encryption algorithm ex. "AES/CBC/PKCS5Padding"
     * @param secretKey secret key object
     * @param cbcIv iv parameter spec
     * @param ciphertext encrypted data
     * @return ciphertext
     * @throws Exception if encryption process fails
     */
    public static byte[] decrypt(String algorithm, SecretKey secretKey, IvParameterSpec cbcIv, byte[] ciphertext) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        if(algorithm.contains("CBC")) {
            cbcIv = cbcIv == null ? new IvParameterSpec(new byte[16]) : cbcIv;
            cipher.init(Cipher.DECRYPT_MODE, secretKey, cbcIv);
        } else {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
        }

        return cipher.doFinal(ciphertext);
    }

    /**
     * TOTP util
     */
    public static class TOTP {
        public static final String HMAC_SHA1 = "HmacSHA1";
        public static final String HMAC_SHA256 = "HmacSHA256";
        public static final String HMAC_SHA512 = "HmacSHA512";

        private static final int[] DIGITS_POWER = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};
        private static final long DEFAULT_VALIDITY = 30000;
        private static final int DEFAULT_TOTP_SIZE = 6;
        private static final String DEFAULT_HASH_ALGORITHM = HMAC_SHA1;

        /**
         * Generate the current TOTP
         * @param secret key of the TOTP
         * @return TOTP number
         */
        public static int generate(byte[] secret) {
            return generate(secret, System.currentTimeMillis(), null, null, null);
        }

        /**
         * Generate a TOTP for a custom time
         * @param secret key of the TOTP
         * @param timeMillis epoch time for generation of the TOTP
         * @return TOTP number
         */
        public static int generate(byte[] secret, long timeMillis) {
            return generate(secret, timeMillis, null, null, null);
        }

        /**
         * Generate a very customized TOTP
         * @param secret key of the TOTP
         * @param timeMillis epoch time for generation of the TOTP
         * @param validityTimeMillis duration of the validity of the TOTP
         * @param codeDigits amount of digits of the TOTP
         * @param hashAlgorithm Hmac Hash algorithm
         * @return TOTP number
         */
        public static int generate(byte[] secret, long timeMillis, @Nullable Long validityTimeMillis, @Nullable Integer codeDigits, @Nullable String hashAlgorithm) {
            validityTimeMillis = validityTimeMillis == null || validityTimeMillis < 1000 ? DEFAULT_VALIDITY : validityTimeMillis;
            codeDigits = codeDigits == null || codeDigits < 1 || codeDigits > 8 ? DEFAULT_TOTP_SIZE : codeDigits;
            hashAlgorithm = hashAlgorithm == null ||
                    !(hashAlgorithm.equals(HMAC_SHA1) || hashAlgorithm.equals(HMAC_SHA256) || hashAlgorithm.equals(HMAC_SHA512)) ?
                    DEFAULT_HASH_ALGORITHM : hashAlgorithm;

            long totpCounter = timeMillis / validityTimeMillis;
            StringBuilder steps = new StringBuilder(Long.toHexString(totpCounter).toUpperCase());
            while (steps.length() < 16) {
                steps.insert(0, "0");
            }

            byte[] msg = hexStr2Bytes(String.valueOf(steps));
            byte[] hash = calculateHmac(hashAlgorithm, secret, msg);
            int offset = hash[hash.length - 1] & 0xf;
            int binary = ((hash[offset] & 0x7f) << 24) | ((hash[offset + 1] & 0xff) << 16) | ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);
            return binary % DIGITS_POWER[codeDigits];
        }

        private static byte[] calculateHmac(String algorithm, byte[] secret, byte[] data) {
            try {
                Mac hmac = Mac.getInstance(algorithm);
                SecretKeySpec macKey = new SecretKeySpec(secret, "RAW");
                hmac.init(macKey);
                return hmac.doFinal(data);
            } catch (GeneralSecurityException gse) {
                throw new UndeclaredThrowableException(gse);
            }
        }

        private static byte[] hexStr2Bytes(String hex) {
            byte[] bArray = new BigInteger("10" + hex, 16).toByteArray();
            byte[] ret = new byte[bArray.length - 1];
            System.arraycopy(bArray, 1, ret, 0, ret.length);
            return ret;
        }
    }
}
