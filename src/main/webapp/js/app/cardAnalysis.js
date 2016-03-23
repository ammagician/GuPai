/**
 * Created by pym on 2016/3/23.
 */
define(function(){
    var analysisCardType = function(rcm){
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
            return {
                valid: false,
                size: -1
            };
        }
        var cards = rcm.cards;

        if(rcm.size == 2){
            var a,b;
            for(var c in cards){
                if(!a){
                    a = cards[c];
                }else{
                    b = cards[c];
                }
            }

            if(a.type == "WU" && b.type == "WU" && ( (a.value == 4 && b.value == 6)
                || (a.value == 6 && b.value == 4))){
                return {
                    cardIds: [a.id, b.id],
                    cardType: "WU",
                    comType: type,
                    value: 0,
                    size: 2,
                    valid: true,
                    isZhiZun: true
                };
            }
        }


        var v = 0;
        var ct = {
            wen: 0,
            wu: 0
        };

        var cardIds = [];
        var valid = true;
        for(var a in cards){
            var c = cards[a];
            cardIds.push(c.id);
            if(v == 0){
                v = c.value;
            }

            if(c.value != v){
                valid = false;
            }

            var t = c.type;
            if(t == "WEN"){
                ct.wen +=1;
            }else{
                ct.wu +=1;
            }
        }

        if(!valid){
            return {
                valid: valid,
                size: cardIds.length,
                cardIds: cardIds
            };
        }

        var cardType = ct.wen == ct.wu? "COM" : ct.wen > ct.wu? "WEN" : "WU";

        return {
            cardIds: cardIds,
            cardType: cardType,
            comType: type,
            value: v,
            size: cardIds.length,
            valid: true,
            isZhiZun: false
        };
    };

    return {
        analysisCardType: analysisCardType
    }
});