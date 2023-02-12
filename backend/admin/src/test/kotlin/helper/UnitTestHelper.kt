package helper

import com.auth0.jwk.Jwk
import com.itomise.admin.domain.security.vo.TokenConfig
import com.itomise.admin.infrastructure.DataBaseFactory
import com.itomise.admin.lib.sendgrid.SendGridClient
import com.itomise.admin.module.*
import io.ktor.server.config.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin

object UnitTestHelper {
    fun prepare(withDatabase: Boolean) {
        val file = ApplicationConfig("application.test.conf")

        initializeEnvConfig(file)

        // メールは全てモックをさす
        mockkObject(SendGridClient)
        every { SendGridClient.send(any()) } answers {}

        jwkProvider = mockk()
        every { jwkProvider.get(any()) } returns Jwk.fromValues(
            mapOf(
                "use" to "sig",
                "kty" to "RSA",
                "n" to "ql5HLRkh27pA0zONngdyk_mRo7TgcR7Et31YbqDsWDcfLYN_P1XrdTetg89HHuSC_P_G5rabx3NIzenK_Ej8lZES0F7lpagIwaMZQjPO0urEp53MuRhoogppBr6uxRP_Mkv-bESK_cTNaTgG5nfkWzjfq6Bx1wjNOhWQDONAd81V9jFt8vm0oDzdErsBKUmuysRo7Seuol2mgD5D8AyBYyv79NRaP1anuje4h0DUo45yevxOjjdyAwCcM6uZPPNtDN7AoM469il--rgV-17DJSz3lKnYUdwepqn0ULeqCPORRjJ0p-B5VDJI0_CuYMiO8XsQh43y-znq8pYiIkISow",
                "e" to "AQAB",
                "kid" to "673a5ea3-a4ae-422b-be55-a9c98e674550",
                "alg" to "RS256"
            )
        )

        jwtTokenConfig = TokenConfig(
            issuer = envConfig.jwt.issuer,
            audience = envConfig.jwt.audience,
            expiresIn = 365L * 1000L * 60L * 60L * 24L,
            publicKeyId = envConfig.jwt.publicKeyId
        )

        if (withDatabase) {
            DataBaseFactory.init()
            DatabaseTestHelper.setUpSchema()
        }

        // 既に koin が立ち上がっていれば立ち上げない
        if (GlobalContext.getOrNull() == null) {
            startKoin {
                modules(useCaseModule + repositoryModule + serviceModule)
            }
        }
    }

    fun cleanup(withDatabase: Boolean) {
        // test が fail すると Koin が残ったままになるため
//        if (GlobalContext.getOrNull() != null) {
//            stopKoin()
//        }

        unmockkAll()

        if (withDatabase) {
            DatabaseTestHelper.cleanupSchema()
        }
    }
}