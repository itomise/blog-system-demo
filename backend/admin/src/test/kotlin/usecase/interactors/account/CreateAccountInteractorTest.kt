package usecase.interactors.account

import com.auth0.jwk.Jwk
import com.auth0.jwk.JwkProvider
import com.itomise.admin.domain.security.vo.TokenConfig
import com.itomise.admin.infrastructure.DataBaseFactory
import com.itomise.admin.lib.sendgrid.SendGridClient
import com.itomise.admin.module.*
import com.itomise.admin.usecase.interfaces.account.ICreateAccountUseCase
import com.itomise.admin.usecase.interfaces.account.IGetAccountListUseCase
import controller.BaseTestApplication.Companion.cleanup
import controller.BaseTestApplication.Companion.setUpTables
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.koin.core.component.inject
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

internal class CreateAccountInteractorTest : KoinTest {
    private val createUserUseCase by inject<ICreateAccountUseCase>()
    private val getListUserUseCase by inject<IGetAccountListUseCase>()

    @BeforeTest
    fun prepare() {
        envConfig = EnvConfig(
            isTest = true,
            allowHost = "localhost:3000",
            db = EnvConfig.EnvConfigDb(
                url = System.getenv("DB_URL"),
                user = System.getenv("DB_USER"),
                password = System.getenv("DB_PASSWORD"),
                instanceUnixSocket = null,
                instanceConnectionName = null,
            ),
            redis = EnvConfig.EnvConfigRedis(
                endpoint = System.getenv("REDIS_ENDPOINT"),
            ),
            session = EnvConfig.EnvConfigSession(
                signKey = System.getenv("SESSION_SIGN_KEY"),
            ),
            jwt = EnvConfig.EnvConfigJwt(
                privateKey = System.getenv("JWT_PRIVATE_KEY"),
                publicKeyId = "673a5ea3-a4ae-422b-be55-a9c98e674550",
                issuer = "http://0.0.0.0:8080/",
                audience = "http://0.0.0.0:8080/",
                realm = "Access to app",
                encryptionKey = System.getenv("JWT_ENCRYPTION_KEY"),
            ),
            urls = EnvConfig.Urls(
                accountSignUpUrl = "https://localhost:3000/com.itomise.admin/sign-up",
                accountActivateUrl = "http://localhost:3000/com.itomise.admin/sign-up/activate",
                adminRootUrl = "http://localhost:3000/com.itomise.admin/users",
                googleOAuth2CallbackUrl = "http://localhost:8080/api/google_oauth2/callback"
            ),
            sendGrid = EnvConfig.SendGridConfig(
                apiKey = "",
            ),
            google = EnvConfig.GoogleConfig(
                oauth2ClientId = "",
                oauth2ClientSecret = "",
            )
        )
        DataBaseFactory.init()

        setUpTables()

        // メールは全てモックをさす
        mockkObject(SendGridClient)
        every { SendGridClient.send(any()) } answers {}

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

        jwtTokenConfig = TokenConfig(
            issuer = envConfig.jwt.issuer,
            audience = envConfig.jwt.audience,
            expiresIn = 365L * 1000L * 60L * 60L * 24L,
            publicKeyId = envConfig.jwt.publicKeyId
        )

        jwkProvider = mockJwkProvider
    }

    @AfterTest
    fun after() = cleanup()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(useCaseModule + repositoryModule + serviceModule)
    }

    @Test
    fun `ユーザーが作成できること`() = runBlocking {
        val beforeUsers = getListUserUseCase.handle()
        assertEquals(0, beforeUsers.users.size)

        createUserUseCase.handle(
            ICreateAccountUseCase.Command(
                email = "test@test.test"
            )
        )

        val afterUsers = getListUserUseCase.handle()
        assertEquals(1, afterUsers.users.size)
    }
}