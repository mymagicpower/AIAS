import {app, BrowserWindow, ipcMain, session} from 'electron';
import {join} from 'path';
import installExtension, { VUEJS_DEVTOOLS } from 'electron-devtools-installer'
import { bind_window_native_functions } from "./native_functions.js"
let is_windows = process.platform.startsWith('win');

let win;

function createWindow () {
	// Create the browser window.
	win = new BrowserWindow({
    width: 1024,
    height: 600,
    webPreferences: {
			webSecurity: false,
      preload: join(__dirname, 'preload.js'),
      nodeIntegration: false,
      contextIsolation: true,
    }
  });

  // remove the menu ( works for windows)
	// win.removeMenu();
	// win.setResizable(true);
	// win.setMaximizable(true);

	if (process.env.NODE_ENV === 'development') {
    const rendererPort = process.argv[2];
		console.log(rendererPort)
		win.loadURL(`http://127.0.0.1:${rendererPort}`);
		// developer tool
		// win.webContents.openDevTools()
  }
  else {
		console.log('sss')
		win.loadFile(join(app.getAppPath(), 'index.html'));
  }
}

app.whenReady().then(() => {
  createWindow();
	// Install Vue Devtools
	installExtension(VUEJS_DEVTOOLS);

	// console.log(win);
	bind_window_native_functions(win);

  session.defaultSession.webRequest.onHeadersReceived((details, callback) => {
    callback({
      responseHeaders: {
        ...details.responseHeaders,
        'Content-Security-Policy': ['script-src \'self\'']
      }
    })
  })

  app.on('activate', function () {
    // On macOS it's common to re-create a window in the app when the
    // dock icon is clicked and there are no other windows open.
    if (BrowserWindow.getAllWindows().length === 0) {
      createWindow();
    }
  });
});

app.on('window-all-closed', function () {
  if (process.platform !== 'darwin') app.quit()
});

ipcMain.on('message', (event, message) => {
  console.log(message);
})
