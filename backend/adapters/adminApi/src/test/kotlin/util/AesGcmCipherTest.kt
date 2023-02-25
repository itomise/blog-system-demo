package util

import com.itomise.adminApi.util.AesGcmCipher
import kotlin.test.Test

class AesGcmCipherTest {
    @Test
    fun test() {
        val plainText = "This is plain text."
        val key = "ZXp3ifkqaRiGZyvZ6HcK"

        val cipher = AesGcmCipher(key)

        val encryptData = cipher.encrypt(plainText)

        val decryptData = cipher.decrypt(encryptData)

        assert(decryptData == plainText)
    }
}