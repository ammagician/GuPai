/**
 * Created by pym on 2016/1/18.
 */
ns("GP.UI");

GP.UI.Tip = function(hashData){
    hashData = hashData || {};
    this.hashData = hashData;
    this.width = hashData.width || 250;
    this.height = hashData.height || 100;
    this.msg = hashData.msg || "";
    this.constrain = hashData.constrain || $("body");
    this.duration = hashData.duration || 1000;
    this.init();
};

GP.UI.Tip.prototype = {
    init: function(){
        var p = this.constrain.css("position");
        if(p != "absolute" && p != "relative"){
            this.constrain.addClass(".tip-constrain");
        }

        var w = this.constrain.width(),
            h = this.constrain.height(),
            top = (h - this.height) / 2,
            left = (w - this.width) / 2;


        var str = "<div class='pa tc tip-container none'></div>";
        var el = $(str);
        el.css({top: top + "px", left: left + "px",width: this.width + "px",
            height: this.height + "px", "line-height": this.height + "px"});
        el.html(this.msg);

        this.constrain.append(el);
        this.el = el;
        el.fadeOut();

        var ctr = this;
        setTimeout(function(){
            ctr.dispose();
        }, this.duration);
    },

    dispose: function(){
        if(this.constrain.hasClass(".tip-constrain")){
            this.constrain.removeClass(".tip-constrain");
        }

        this.el.remove();
    }
};