package domain.security.services

import com.itomise.admin.domain.security.interfaces.IHashingService
import com.itomise.admin.domain.security.vo.HashAlgorithm
import com.itomise.admin.domain.security.vo.SaltedHash
import com.itomise.admin.module.serviceModule
import com.itomise.admin.util.getKoinInstance
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test


class HashingServiceTest {
    private lateinit var hashingService: IHashingService

    @BeforeTest
    fun prepare() {
        startKoin {
            modules(serviceModule)
        }
        hashingService = getKoinInstance()
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