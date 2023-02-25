package helper.repository

import com.itomise.admin.domain.account.entities.User
import com.itomise.admin.domain.account.vo.Email
import com.itomise.admin.domain.account.vo.UserId

class InMemoryUserRepository : IUserRepository {
    private val store = mutableListOf<User>()

    override suspend fun getList() = store.toList()

    override suspend fun findByUserId(id: UserId) = store.find { it.id == id }

    override suspend fun findByEmail(email: Email) = store.find { it.email == email }

    override suspend fun save(user: User) {
        val isExists = findByUserId(user.id) != null
        if (isExists) {
            store.removeIf { it.id == user.id }
            store.add(user)
        } else {
            store.add(user)
        }
    }

    override suspend fun delete(user: User) {
        store.removeIf { it.id == user.id }
    }
}