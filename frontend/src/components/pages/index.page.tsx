import React from 'react'

export const IndexPage: React.FC = () => {
  const onClickGet = async () => {
    try {
      const res = await fetch('http://localhost:8080/user', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include',
      })
      const json = res.json()
      console.log(res.type)
      console.log('success', json)
    } catch (err) {
      console.log(err)
    }
  }

  const onClickPost = async () => {
    try {
      const res = await fetch('http://localhost:8080/user', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify({
          id: 'client_test',
          name: 'クライアントテストマン',
        }),
      })
      console.log(res.type)
      const json = res.json()

      console.log('success', json)
    } catch (err) {
      console.log(err)
    }
  }

  return (
    <main>
      <p>
        <button onClick={onClickGet}>ゲット</button>
        <button onClick={onClickPost}>ポスト</button>
      </p>
    </main>
  )
}
