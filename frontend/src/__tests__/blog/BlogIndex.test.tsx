import IndexPage from '@/pages'
import { render, screen, within } from '@testing-library/react'
import { expect, test } from 'vitest'
import { TestApp } from '../helper/TestApp'

test('home', async () => {
  render(
    <TestApp>
      <IndexPage />
    </TestApp>
  )
  const main = within(screen.getByRole('main'))
  expect(main.getByRole('heading', { level: 1, name: /itomise Blog/i })).toBeDefined()

  expect(await main.findByRole('heading', { level: 3, name: /OK/i })).toBeDefined()
})
