/** @type {import('next').NextConfig} */
const nextConfig = {
  eslint: {
    ignoreDuringBuilds: true,
  },
  reactStrictMode: true,
  compiler: {
    emotion: true,
    removeConsole: process.env.NODE_ENV === 'production',
  },
  output: 'standalone',
}

module.exports = nextConfig
