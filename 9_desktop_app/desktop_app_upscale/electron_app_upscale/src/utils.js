
function compute_n_cols() {
    let w = window.innerWidth;
    let n_col;
    if (w < 576) { n_col = 2 } else if (w < 668) { n_col = 3 } else if (w < 892) { n_col = 4 } else if (w < 1100) { n_col = 5 } else if (w < 1600) { n_col = 6 } else if (w < 1900) { n_col = 7 } else if (w < 2100) { n_col = 8 } else if (w < 2400) { n_col = 9 }
    n_col -= 1;
    return n_col;
}

function compute_time_remaining(time_remaining) {
    if (time_remaining.asSeconds() < 1) return "";
    if (time_remaining.hours() > 0) return `(剩余 ${time_remaining.hours()} 小时 ${time_remaining.minutes()} 分 )`;
    else return `(剩余 ${time_remaining.minutes()} 分 ${time_remaining.seconds()} 秒 )`;
}

function simple_hash( strr ) {
    var hash = 0;
    for (var i = 0; i < strr.length; i++) {
        var char = strr.charCodeAt(i);
        hash = ((hash<<5)-hash)+char;
        hash = hash & hash; // Convert to 32bit integer
    }
    return hash;
}

function resolve_asset_illustration(name) {
    let pre_assets_list_svg = [
      ];
    let pre_assets_list_png = [
        ];
    if (pre_assets_list_svg.includes(name))
        return require("@/assets/" + name + ".svg")
    else  if (pre_assets_list_png.includes(name))
        return require("@/assets/" + name + ".png")
    else if (name.startsWith("https://") || name.startsWith("http://"))
        return name;
    else
        return "file://" + name;
}

const escapeHtml = (unsafe) => {
    return unsafe.replaceAll('&', '&amp;').replaceAll('<', '&lt;').replaceAll('>', '&gt;').replaceAll('"', '&quot;').replaceAll("'", '&#039;');
}

function open_popup( img_url , text ){
    let css = `
        <style>
        img {
                     width: 100%;
                      height:100%;
                      object-fit: contain;
                      user-drag: none;
            }
            @media (prefers-color-scheme: light) {
                body {
                    background-color: #f2f2f2;
                }
            }
            @media (prefers-color-scheme: dark) {
                body {
                    background-color: #303030;
                }
            }
            body{
                padding : 0;
                margin: 0;
                -webkit-user-select: none;
                    -webkit-app-region: drag;
                  
                      user-drag: none;
                        -webkit-user-drag: none;
                        user-select: none;
                        -moz-user-select: none;
                        -webkit-user-select: none;
                        -ms-user-select: none;
            }
            p{
                padding:40px;
            }

       </style>
    `
    let html = '<html><head>'+css+'</head><body>' ;
    if (img_url)
        html += '<img src="'+escapeHtml(img_url)+'"> ';
    if( text )
         html += '<p> '+ escapeHtml(text) +' </p>';
    html += '</body></html>'
    let uri = "data:text/html," + encodeURIComponent(html);
    uri;
    let if_frame= '';
    if(navigator.platform.toUpperCase().indexOf('MAC')>=0 ){
        if_frame = ",frame=false"
    }

    window.open(escapeHtml(uri), '_blank', 'top=100,left=100,nodeIntegration=no'+if_frame); //

}

export { compute_n_cols , compute_time_remaining , resolve_asset_illustration , simple_hash , open_popup }
