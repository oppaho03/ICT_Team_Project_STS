// 네이버 스마트 에디터 2.0 적용
let oEditors = [];

function smartEditor() {
    console.log("Naver SmartEditor");
    nhn.husky.EZCreator.createInIFrame({
        oAppRef: oEditors,
        elPlaceHolder: "editorTxt",
        sSkinURI: "/smarteditor/SmartEditor2Skin.html",
        fCreator: "createSEditor2"
    });
}

$(document).ready(function() {
    smartEditor();
});