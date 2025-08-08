import { defineConfig } from "vite";
import react from "@vitejs/plugin-react-swc";
import tailwindcss from "@tailwindcss/vite";

// https://vite.dev/config/
export default defineConfig({
  plugins: [react(), tailwindcss()],
  define: {
    __APP_TITLE__: JSON.stringify("Inventory Management System"),
  },
  server: {
    proxy: {
      "/api/inventory": {
        target: "http://localhost:8081",
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api\/inventory/, ""),
      },
    },
  },
});
