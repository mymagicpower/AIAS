import {contextBridge, ipcRenderer} from 'electron';

contextBridge.exposeInMainWorld('ipcRenderer', ipcRenderer)
contextBridge.exposeInMainWorld('ipcRenderer_on', ipcRenderer.on)

contextBridge.exposeInMainWorld('electronAPI', {
  sendMessage: (message: string) => ipcRenderer.send('message', message)
})

var bind_ipc_renderer_on_fn = undefined;


function bind_ipc_renderer_on(fn) {
	bind_ipc_renderer_on_fn = fn;
}

contextBridge.exposeInMainWorld('bind_ipc_renderer_on', bind_ipc_renderer_on)

// the msg channel which is used for electron to send messages to browser / renderer
ipcRenderer.on("to_renderer", (e, data) => {
	if (bind_ipc_renderer_on_fn){
		// @ts-ignore
		bind_ipc_renderer_on_fn(data)
	}
});
