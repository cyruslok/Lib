var Src_Config_Url = "config/src-config.json";
var Header_Div_Id = "#header";
var Content_Div_Id = "#content";
var Footer_Div_Id = "#footer";

(function() {
    getSrcConfigData();
})();

function getSrcConfigData(roots="roots", headers="headers",footers="footers",contents="contents", name="index"){
    $.getJSON( Src_Config_Url , function( data ) {
        var header_url = data[roots][headers]+data[headers][name];
        var content_url = data[roots][contents]+data[contents][name];
        var footer_url = data[roots][footers]+data[footers][name];
        initDiv(header_url,footer_url,content_url);
    });
}
function initDiv(header_url,footer_url,content_url){
    $(Header_Div_Id).load(header_url); 
    $(Content_Div_Id).load(content_url); 
    $(Footer_Div_Id).load(footer_url); 
}

function changeDivContent(div_id,content_url){
    $(div_id).load(content_url); 
}