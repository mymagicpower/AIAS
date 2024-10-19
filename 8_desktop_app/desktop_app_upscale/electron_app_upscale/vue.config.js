
module.exports = {

    pluginOptions: {
        electronBuilder: {
            preload: './src/preload.js',

            // Or, for multiple preload files:
            // preload: { preload: 'src/preload.js', otherPreload: 'src/preload2.js' }
            builderOptions: {
                productName:"图像放大",//项目名 这也是生成的exe文件的前缀名
                appId: 'top.aias.upscale',
                copyright:"All rights reserved by aias.top",//版权 信息
                afterSign: "./afterSignHook.js",
                "extraResources": [{
                    "from": "E:\\upscale\\" , // "../backends/stable_diffusion_tf/diffusion_backend.py" process.env.BACKEND_BUILD_PATH
                    "to": "core",
                    "filter": [
                        "**/*"
                    ]
                }],
                "nsis": {
                    "oneClick": false, // 是否一键安装
                    "allowElevation": true, // 允许请求提升。 如果为false，则用户必须使用提升的权限重新启动安装程序。
                    "allowToChangeInstallationDirectory": true, 	// 允许修改安装目录
                    // "installerIcon": "build/Icon-1024.png",		// 安装图标路径
                    // "uninstallerIcon": "build/Icon-1024.png",	//卸载图标
                    // "installerHeaderIcon": "build/Icon-1024.png", // 安装时头部图标
                    "createDesktopShortcut": true, 	// 创建桌面图标
                    "createStartMenuShortcut": true,	// 创建开始菜单图标
                    "shortcutName": "图像放大", 	// 图标名称
                },
                "mac": {
                    "icon" : "build/Icon-1024.png" ,
                    "hardenedRuntime": true,
                    "entitlements": "build/entitlements.mac.plist",
                    "entitlementsInherit": "build/entitlements.mac.plist",
                    "minimumSystemVersion": "12.3.0",
                    "extendInfo": {
                        "LSMinimumSystemVersion": "12.3.0"
                    } ,

                    "target": {
                        "target": "dmg",
                        "arch": [
                            'x64'  //'arm64' , 'x64' process.env.BUILD_ARCH
                        ]
                    }
                },

                "win": {
                    "icon" : "build/Icon-1024.png" ,
                    "target": {
                        "target": "NSIS",
                        "arch": [
                            process.arch   //
                        ]
                    }
                }
            }


        }
    }
}
