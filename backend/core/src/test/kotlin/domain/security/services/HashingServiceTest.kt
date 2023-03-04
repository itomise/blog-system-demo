package domain.security.services

import com.itomise.core.domain.security.services.HashingService
import com.itomise.core.domain.security.vo.HashAlgorithm
import com.itomise.core.domain.security.vo.SaltedHash
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test


class HashingServiceTest : KoinTest {
    private val hashingService by inject<HashingService>()

    @BeforeTest
    fun prepare() {
        startKoin {
            modules(
                module {
                    single { HashingService() }
                }
            )
        }
    }

    @AfterTest
    fun cleanup() {
        stopKoin()
    }

    @Test
    fun `正しく評価できること`() {
        val password = "_WG_xzVMaERveTcZ7yU!"

        val saltedHash = hashingService.generateSaltedHash(password)

        assert(hashingService.verifySaltedHash(password, saltedHash))
    }

    @Test
    fun `文字列が異なる場合等価にならないこと`() {
        val password = "_WG_xzVMaERveTcZ7yU!"

        val saltedHash = hashingService.generateSaltedHash(password)

        assert(!hashingService.verifySaltedHash("_WG_xzVMaERveTcZ7yU", saltedHash))
    }

    @Test
    fun `アルゴリズムが異なる場合等価にならないこと`() {
        val password = "_WG_xzVMaERveTcZ7yU!"

        val saltedHash = hashingService.generateSaltedHash(password)

        var differentAlgorithm = HashAlgorithm.getRandom()

        while (saltedHash.algorithm == differentAlgorithm) {
            differentAlgorithm = HashAlgorithm.getRandom()
        }

        val differentSaltedHash = SaltedHash(
            hash = saltedHash.hash,
            salt = saltedHash.salt,
            algorithm = differentAlgorithm
        )

        assert(!hashingService.verifySaltedHash(password, differentSaltedHash))
    }
}