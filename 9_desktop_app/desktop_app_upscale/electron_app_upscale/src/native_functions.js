import { ipcMain, dialog } from 'electron'
import { app , screen } from 'electron'
import settings from 'electron-settings';
import fs from "fs";
import path from "path";

var win;

function bind_window_native_functions(w) {
    console.log("browser object binded")
    win = w;
}


let is_windows = process.platform.startsWith('win');



console.log(require('os').freemem()/(1000000000) + " Is the free memory")
console.log(require('os').totalmem()/(1000000000) + " Is the total memory")


ipcMain.on('save_dialog', (event, ...args) => {

    const seed = args[1] ? args[1] : "0"
    const prompt = args[0] ? args[0] : "Untitled"
    let filename = ''
    if (seed === '0') {
        filename = prompt
    } else {
        filename = seed + '-' + prompt
    }
    let trimmedFilename = filename.substring(0, 254) // filename size limit
     let save_path = dialog.showSaveDialogSync({
            title: 'Save Image',
            defaultPath: trimmedFilename,
            filters: [{
              name: 'Image',
              extensions: ['png']
            }]
          })

     event.returnValue = save_path;
} )

console.log(require('os').release() + " ohoho")



ipcMain.on('file_dialog', (event, arg) => {
    console.log("file dialog request recieved" + arg) // prints "ping"
    let properties;
    let options;

    if (arg == "folder") // single folder
    {
        properties = ['openDirectory'];
        options = { properties: properties } ;
    }
    else if(arg == 'img_file') // single image file
    {
        properties = ['openFile' ]
        options = { filters :[ {name: 'Images', extensions: ['jpg', 'jpeg', 'png', 'bmp']}] , properties: properties } ;
    }
    else if(arg == 'ckpt_file') // single image file
    {
        properties = ['openFile' ]
        options = { filters :[ {name: 'Checkpoints', extensions: ['ckpt']}] , properties: properties } ;
    }
    else if(arg == 'img_files') // multi image files
    {
        properties = ['multiSelections' , 'openFile' ]
        options = { filters :[ {name: 'Images', extensions: ['jpg', 'jpeg', 'png', 'bmp']}] , properties: properties } ;
    }
    else if(arg == 'text_files') // multi image files
    {
        properties = ['multiSelections' , 'openFile' ]
        options = { filters :[ {name: 'Images', extensions: ['txt']}] , properties: properties } ;
    }
    else if(arg == 'audio_files') // multi image files
    {
        properties = ['multiSelections' , 'openFile' ]
        options = { filters :[ {name: 'Images', extensions: ['mp3', 'wav']}] , properties: properties } ;
    }
    else if(arg == 'video_files') // multi image files
    {
        properties = ['multiSelections' , 'openFile' ]
        options = { filters :[ {name: 'Images', extensions: ["mp4", "mov", "avi", "flv", "wmv", "mkv"]}] , properties: properties } ;
    }
    else if(arg == 'any_files') // multi image files
    {
        properties = ['multiSelections' , 'openFile' ]
        options = {  properties: properties } ;
    }
    else
    {
        properties = ['openFile'];
        options = { properties: properties } ;
    }

    // //Synchronous
    let filePaths = dialog.showOpenDialogSync(options)

    if (filePaths && filePaths.length > 0)
        event.returnValue = filePaths.join(";;;");
    else
        event.returnValue = "NULL";
})


ipcMain.on('open_url', (event, url) => {
    let website_domain = require('../package.json').website ;
    url = url.replace("__domain__" , website_domain );
    require('electron').shell.openExternal(url);
    event.returnValue = '';
})


ipcMain.on('save_file', (event, arg) => {
    let p1 = arg.split("||")[0];
    let p2 = arg.split("||")[1];
    require('fs').copyFileSync(p1, p2);
    event.returnValue = '';
})


ipcMain.on('show_dialog_on_quit', (event, msg) => {
    if(win)
    {
        win.show_dialog_on_quit = true;
        win.dialog_on_msg = msg;
    }
    event.returnValue = 'ok';

})


ipcMain.on('dont_show_dialog_on_quit', (event, arg) => {
    if(win)
        win.show_dialog_on_quit = false;
    event.returnValue = 'ok';

})


ipcMain.on('get_instance_id', (event, arg) => {
    if (settings.hasSync('instance_id')){
        event.returnValue =  settings.getSync('instance_id')
        return;
    }
    let instance_id =  (Math.random() + 1).toString(36);
    settings.set('instance_id', instance_id);
    event.returnValue =   instance_id;

})


ipcMain.on('unfreeze_win', (event, arg) => {

    if (win) {
	win.savable=true;
        const primaryDisplay = screen.getPrimaryDisplay()
        const { width, height } = primaryDisplay.workAreaSize

        if (settings.hasSync('windowPosState')) {
            let windowState = settings.getSync('windowPosState');
            console.log("stateeee")
            console.log(windowState)

            if( windowState.x  >  0.8*width ||  windowState.y  >  0.8*height ||  windowState.x  < -0.2*width || windowState.y  < -0.2*height    ){
                win.setSize(850, 650, false);
            } else {
               win.setPosition( windowState.x  ,  windowState.y  , false);
                win.setSize(windowState.width, windowState.height , false);
            }

        }
        else{
            win.setSize(850, 650, false);
        }

        win.setResizable(true);
        win.setMaximizable(true);

    }

    event.returnValue = 'ok';

})



ipcMain.on('freeze_win', (event, arg) => {

    if (win) {
	    win.savable=false;
	    win.restore()
        win.setSize(770, 550, false);
        win.setResizable(false);
        win.setMaximizable(false);

        const primaryDisplay = screen.getPrimaryDisplay()
        const { width, height } = primaryDisplay.workAreaSize;

        console.log( width +" " +  height)

        win.setPosition( parseInt((width-770)/2)  , parseInt((height-550)/2), false);

    }

    event.returnValue = 'ok';

})



ipcMain.on('show_about', (event, arg) => {

    if (win) {

        if(is_windows)
        {
            let about_content = require('../package.json').name + "\n" + "Version " + require('../package.json').version + " (" + require('../package.json').build_number + ")\n" + require('../package.json').description;
            const choice = require('electron').dialog.showMessageBoxSync(this, {
                buttons: ['Okay'],
                title: require('../package.json').name ,
                message: about_content
            });
        }
        else{
            app.showAboutPanel()
        }


    }

    event.returnValue = 'ok';

})




ipcMain.on('native_confirm', (event, arg) => {

    if (win) {

        const choice = require('electron').dialog.showMessageBoxSync(this, {
            type: 'question',
            buttons: ['Yes', 'No'],
            title: require('../package.json').name ,
            message: arg
        });
        if (choice === 1) {
            event.returnValue = false ;
        }
        else{
            event.returnValue = true ;
        }

    }
    else{
        event.returnValue = false ;
    }

})



ipcMain.on('close_window', (event, arg) => {

    if (win) {

        win.close()
        event.returnValue = true ;
    }
    else{
        event.returnValue = false ;
    }

})




ipcMain.on('native_alert', (event, arg) => {

    if (win) {

        const choice = require('electron').dialog.showMessageBoxSync(this, {
            buttons: ['Okay'],
            title: require('../package.json').name ,
            message: arg
        });

    }

    event.returnValue = true ;

})




ipcMain.on('save_b64_image', (event, b64_str, save_to_tmp ) => {

    const path = require('path');
    const fs = require('fs');

    let base64Data = b64_str.replace(/^data:image\/png;base64,/, "");

    const homedir = require('os').homedir();
    let save_dir = path.join(homedir , "aias.top")


    if (!fs.existsSync(save_dir)){
        fs.mkdirSync(save_dir, { recursive: true });
    }

    if(save_to_tmp){
        save_dir = "/tmp/"
    } else {
        save_dir = path.join(save_dir , "inp_images")
    }



    if (!fs.existsSync(save_dir)){
        fs.mkdirSync(save_dir, { recursive: true });
    }

    let p = require('path').join(save_dir,  Math.random().toString()+".png");

    require("fs").writeFileSync(p , base64Data, 'base64');

    event.returnValue = p ;

})


ipcMain.on('save_data', (event, arg) => {
    const path = require('path');
    const fs = require('fs');
    const homedir = require('os').homedir();
    let save_dir = path.join(homedir , "aias.top")


    if (!fs.existsSync(save_dir)){
        fs.mkdirSync(save_dir, { recursive: true });
    }

    let data_path = path.join(homedir , "aias.top" , "data.json")
    fs.writeFileSync( data_path, JSON.stringify(arg) );
    event.returnValue = true ;

})


ipcMain.on('load_data', (event, arg) => {
    const path = require('path');
    const fs = require('fs');
    const homedir = require('os').homedir();
    let data_path = path.join(homedir , "aias.top" , "data.json");

    if (fs.existsSync(data_path)){
        let json_str = fs.readFileSync( data_path );
        try {
            event.returnValue = JSON.parse(json_str);
          } catch (error) {
            event.returnValue = {} ;
          }
    }
    else{
        event.returnValue = {} ;
    }

})


ipcMain.on('delete_file', (event, fpath) => {
    const fs = require('fs');
    try{
        fs.unlinkSync(fpath);
        console.log("deleted")
        event.returnValue = true;
    } catch {
        console.log("err in deleting")
        event.returnValue = false;
    }

})

var proc;
function run_realesrgan(input_path , out_path , cb ){
    console.log(` input path: ${input_path}`);
    console.log(` output path: ${out_path}`);

    const path = require('path');
    if(!out_path){
        out_path = "C:\\Windows\\Temp\\"+Math.random()+".png";
    }
    const fs = require('fs');

    let bin_path = "D:\\ai_projects\\AIAS\\9_desktop_app\\desktop_app_upscale\\realesrgan_ncnn\\realsr-ncnn-vulkan-windows\\realsr-ncnn-vulkan.exe"

    if(bin_path && (fs.existsSync(bin_path))){
        proc = require('child_process').spawn( bin_path  , ['-i' , input_path , '-o' , out_path ]);
    }
    else{
        const path = require('path');
        bin_path = path.join(path.dirname(__dirname), 'core' , 'realsr-ncnn-vulkan.exe' );
        proc = require('child_process').spawn( bin_path  , ['-i' , input_path , '-o' , out_path ]);
    }

    console.log([bin_path , '-i' , input_path , '-o' , out_path ])

    proc.stderr.on('data', (data) => {
        win.webContents.send('to_renderer', '' + data.toString('utf8'));
        console.error(`sr stderr: ${data}`);
    });

    proc.stdout.on('data', (data) => {
        win.webContents.send('to_renderer', '' + data.toString('utf8'));
        console.error(`sr stdout: ${data}`);
    });

    proc.on('close', (code) => {
        if (fs.existsSync(out_path)) {
            cb(out_path);
        }
        else
        {
            cb('');
        }
    });
}


ipcMain.handle('run_realesrgan', async (event, input_path, out_path) => {
    const result = await new Promise(resolve => run_realesrgan( input_path , out_path , resolve));
    return result
})


app.on('window-all-closed', () => {
    require('child_process').exec('taskkill /F /IM electron.exe /T')
    require('child_process').exec('taskkill /F /IM 图像放大.exe /T')
    // console.log('window-all-closed')
    // if(java_process){
    //     console.log('kill java process')
    //     is_app_closing = true;
    //     java_process.kill('SIGKILL')
    //     // process.exit(java_process.pid)
    //     app.quit()
    //     // app.exit();
    // }

})

console.log("native functions imported")


export { bind_window_native_functions }
