const Path = require('path');
const vuePlugin = require('@vitejs/plugin-vue')
const { defineConfig } = require('vite');
import vueI18nPlugin from "@intlify/unplugin-vue-i18n/vite";

/**
 * https://vitejs.dev/config
 */
const config = defineConfig({
    root: 'src',
    publicDir: 'public',
    server: {
        port: 8080,
    },
		node: {
			global: false
		},
    open: false,
    build: {
        outDir: 'build',
        emptyOutDir: true,
    },
    plugins: [
			vuePlugin(),
			vueI18nPlugin({
				include: Path.resolve(__dirname, "./src/locales"),
				runtimeOnly: false,
				jitCompilation: true,
			}),
		],
	resolve:{
			define:{
				__INTLIFY_JIT_COMPILATION__: true,
			}
	}
});

module.exports = config;
