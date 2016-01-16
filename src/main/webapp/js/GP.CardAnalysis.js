/**
 * Created by pym on 2016/1/15.
 */
ns("GP");
GP.CardAnalysis = function(){

};

GP.CardAnalysis.prototype = {
    cardType: function(rcm){
        if(!rcm || rcm.size == 0 || rcm.size > 4){
            return false;
        }

        var m = {
            1: "single",
            2: "pair",
            3: "triple",
            4: "quadruple"
        };

        var type = m[rcm.size];

        if(!type){
            return false;
        }

        var cards = rcm.cards;
        var v = 0;
        for(var a in cards){
            var c = cards[a];
            if(v == 0){
                v = c.value;
            }

            if(c.value != v){
                return false;
            }
        }

        return {
            type: type,
            value: v
        };
    }
};