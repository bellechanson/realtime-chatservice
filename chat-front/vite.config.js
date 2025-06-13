import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import { NodeGlobalsPolyfillPlugin } from '@esbuild-plugins/node-globals-polyfill';

// 📦 Vite 설정 파일
export default defineConfig({
  plugins: [react()], // 리액트 플러그인 적용

  optimizeDeps: {
    esbuildOptions: {
      define: {
        // ✅ 브라우저에서 Node.js의 global 객체가 없기 때문에 globalThis로 대체
        global: 'globalThis',
      },
      plugins: [
        // ✅ STOMP.js 등에서 buffer 객체를 참조할 때 오류 방지용 polyfill
        NodeGlobalsPolyfillPlugin({
          buffer: true,
        }),
      ],
    },
  },
});
