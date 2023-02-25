package com.itomise.adminApi.util

import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

// https://4engineer.net/cipher/kotlin-aes-gcm-encryption/
class AesGcmCipher(originKey: String) {
    private val key: SecretKeySpec
    private val GCM_NONCE_LENGTH = 12
    private val random = SecureRandom()
    private val tagBitLen = 128
    private val charset = Charsets.UTF_8

    private val cipher = Cipher.getInstance("AES/GCM/NoPadding")

    init {
        val hashMd5 = MessageDigest.getInstance("MD5")
        key = SecretKeySpec(hashMd5.digest(originKey.toByteArray()), "AES")
    }

    fun encrypt(plainData: String): String {
        val nonceToEncrypt = ByteArray(GCM_NONCE_LENGTH)
        random.nextBytes(nonceToEncrypt)

        val gcmParameterSpec = GCMParameterSpec(tagBitLen, nonceToEncrypt)

        cipher.init(Cipher.ENCRYPT_MODE, key, gcmParameterSpec)

        val encryptData = cipher.doFinal(plainData.toByteArray())

        val byteArray = cipher.iv + encryptData

        return byteArrayToHexString(byteArray)
    }

    fun decrypt(cipherDataString: String): String {
        val cipherData = hexStringToByteArray(cipherDataString)
        val nonce = cipherData.copyOfRange(0, GCM_NONCE_LENGTH)
        val encryptData = cipherData.copyOfRange(GCM_NONCE_LENGTH, cipherData.size)

        val gcmParameterSpec = GCMParameterSpec(tagBitLen, nonce)

        cipher.init(Cipher.DECRYPT_MODE, key, gcmParameterSpec)

        val byteArray = cipher.doFinal(encryptData)

        return String(byteArray, charset)
    }

    private fun byteArrayToHexString(byteArray: ByteArray) = byteArray.joinToString("") { "%02X".format(it) }

    private fun hexStringToByteArray(string: String) = string.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
}