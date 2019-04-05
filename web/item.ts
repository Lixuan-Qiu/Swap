
class item_ {
    constructor() {
        
    }
    private updateItemList(data:any) {

        $("#Message").empty();
        for(let i = 0; i<data.item.length;i++){
            $("#itemInfo").append("<p>" +data.item[i].itemDescription+"</p>");
        }
        /*
        for(let i = 0; i<data.length;i++){
            $("#"+i).click(function(){
                window.location.href = "item.html";
            });
        }
        */
        //$("#Message").html(
        //$("#Message").html(Handlebars.templates["itemList.hb"](data));
        /*
        for(let i = 0; i<data.length;i++){
            $("#" + data[i].itemId).click(this.getItemInfo(data[i].itemId))
        }
        */
    }
}
$(function () {

    
});