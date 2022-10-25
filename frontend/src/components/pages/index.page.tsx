import { useCreateUser } from '@/services/user/api/useCreateUser'
import { useUserList } from '@/services/user/api/useUserList'
import React, { useState } from 'react'

export const IndexPage: React.FC = () => {
  const { users } = useUserList()
  const createUserMutation = useCreateUser()
  const [name, setName] = useState('')
  const [email, setEmail] = useState<string>('')

  return (
    <main>
      <ul>
        {users?.map((user) => (
          <li key={user.id}>
            <p>{user.id}</p>
            <p>{user.name}</p>
            <p>{user.email}</p>
          </li>
        ))}
      </ul>
      <form
        onSubmit={(e) => {
          e.preventDefault()
          createUserMutation.mutate({
            name,
            email,
          })
        }}
      >
        <input name="name" onChange={(e) => setName(e.target.value)} />
        <input name="email" onChange={(e) => setEmail(e.target.value)} />
        <button type="submit">送信</button>
      </form>
    </main>
  )
}
