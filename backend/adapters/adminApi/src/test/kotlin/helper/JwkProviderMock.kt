package helper

import com.auth0.jwk.Jwk
import com.auth0.jwk.JwkProvider
import com.itomise.adminApi.module.jwkProvider
import io.mockk.every
import io.mockk.mockk

object JwkProviderMock {
    fun handle() {
        val mockJwkProvider = mockk<JwkProvider>()
        every { mockJwkProvider.get(any()) } returns Jwk.fromValues(
            mapOf(
                "use" to "sig",
                "kty" to "RSA",
                "n" to "ql5HLRkh27pA0zONngdyk_mRo7TgcR7Et31YbqDsWDcfLYN_P1XrdTetg89HHuSC_P_G5rabx3NIzenK_Ej8lZES0F7lpagIwaMZQjPO0urEp53MuRhoogppBr6uxRP_Mkv-bESK_cTNaTgG5nfkWzjfq6Bx1wjNOhWQDONAd81V9jFt8vm0oDzdErsBKUmuysRo7Seuol2mgD5D8AyBYyv79NRaP1anuje4h0DUo45yevxOjjdyAwCcM6uZPPNtDN7AoM469il--rgV-17DJSz3lKnYUdwepqn0ULeqCPORRjJ0p-B5VDJI0_CuYMiO8XsQh43y-znq8pYiIkISow",
                "e" to "AQAB",
                "kid" to "673a5ea3-a4ae-422b-be55-a9c98e674550",
                "alg" to "RS256"
            )
        )

        jwkProvider = mockJwkProvider
    }
}