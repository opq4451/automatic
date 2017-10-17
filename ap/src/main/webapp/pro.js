const rangeBigExist = { '01' : 's' , '02' : 's' , '03' : 's' ,'04' : 's' ,'05' : 's' ,  
						'06' : 'b' , '07' : 'b' , '08' : 'b' ,'09' : 'b' ,'10' : 'b' ,

};

const bsCode = {
		'b' : ['06','07','08','09','10'] , 
		's' : ['01','02','03','04','05'] ,
		
}

const rangeSingExist = {
		'01' : 'sin' , '03' : 'sin' , '05' : 'sin' ,'07' : 'sin' ,'09' : 'sin' ,  
		'02' : 'dou' , '04' : 'dou' , '06' : 'dou' ,'08' : 'dou' ,'10' : 'dou' ,
			 

};

const sdCode = {
		'sin' : ['01','03','05','07','09'] , 
		'dou' : ['02','04','06','08','10'] ,
		
}

//大小
function compareByType2(phase_before,phase_now,phase_before4, gid, bIndex, bArray){
		   
		const code_b = getCodeByPhase(phase_before); 
		const code_n = getCodeByPhase(phase_now);
		const code_before4 = getCodeByPhase(phase_before4);
		
		const array_b = code_b.split(',');
		const array_n = code_n.split(',');
		const array_before4 = code_before4.split(',');
		if(array_b.length==0 || array_n.length==0 || array_before4.length==0  ){
			return;
		}
		for(var i=0;i<10;i++){
			const bet_b = array_b[i];
			const bet_n = array_n[i];
			const bet_before4 = array_before4[i];
 
		 	var d = getYYYYMMDDSS();
			var z = i+1;
		    if(bArray[0]==0 && rangeBigExist[bet_b] == rangeBigExist[bet_n]){ //預測到、關卡歸零
		    	 
		    
		    	var suclog =  d + "第"+z+"名"+ " 此期" + phase_now  + "(開獎:"+code_n+")," +  " 預測期數 "+phase_before + "預測("+rangeBigExist[bet_b]+") ,有預測到，下一關，此注不下注" ;
	 
		    	saveLog(document.getElementById("user").value,encodeURI(suclog));

		    	bIndex[z] = 0;
			}

		    	//未預測到、投注
				//取前四期的號碼來預測投注
			 
		    	var log =  d + "第"+z+"名"+ " 此期" + phase_now  + "(開獎:"+code_n+")," + " 預測期數 "+phase_before + "預測("+rangeBigExist[bet_b]+") ,沒有預測到 ,下注" ;
		    	log+= "， 參考期數"+  phase_before4 + "("+code_before4+"), 下注號碼"+ bsCode[rangeBigExist[bet_before4]];
				
		    	log+= ",下注金額:"+bArray[bIndex[z]%bArray.length];
		    	var c = bArray[bIndex[z]%bArray.length] == 0 ? '0' : computeCase(bArray,bArray[bIndex[z]%bArray.length]);
		    	var suc = bArray[bIndex[z]%bArray.length] > 1 ? "成功" : "失敗" ;
		    	var betlog =  "預測第"+ (phase_now+1) +"期"+ "，第"+z+"名，號碼("+bsCode[rangeBigExist[bet_before4]]+")"+ "，第"+c+"關"+"下注金額("+bArray[bIndex[z]%bArray.length]+")"+"("+suc+")";
		    	
		    	var betList =   bsCode[rangeBigExist[bet_before4]].toString().split(',');
		    	
		    	
		    	var writeFlag = false;
		    	for(var b=0;b<betList.length;b++){
		    		var code = betList[b];
		    		var sn = i==9 ? 0 : (i+1);
		    		if(bArray[bIndex[z]%bArray.length]  > 0 ){
		    			//reallyBet(gid,sn,code,bArray[bIndex[z]%bArray.length]);
		    			writeFlag = true;
		    			//處理過關的log
		    			let overLog = (phase_now+1) + "@" + sn + "@" + code ; 
		    			saveOverLog(document.getElementById("user").value,encodeURI(overLog),c);
		    		}
		    			
		    	}
		    	
		    	
		    	
		    	
		    	if(writeFlag){
		    		let ssn = i==9 ? 0 : (i+1);
		    		if(rangeBigExist[bet_before4] =='b'){ //下大
			    		reallyOtherBet(gid,ssn+'OUo1',bArray[bIndex[z]%bArray.length]);
			    	}
			    	
		    		if(rangeBigExist[bet_before4] =='s'){ //下小
		    			reallyOtherBet(gid,ssn+'OUu1',bArray[bIndex[z]%bArray.length]);
			    	}
		    		saveLog(document.getElementById("user").value+"bet",encodeURI(betlog)); //只寫下注成功的log
		    	}
		    		
		    		
		    		
		    	saveLog(document.getElementById("user").value,encodeURI(log));
		    	bIndex[z]++;
		 
			
			
		}
		
}

//單雙
function compareByType3(phase_before,phase_now,phase_before4, gid, bIndex, bArray){
	   
	const code_b = getCodeByPhase(phase_before); 
	const code_n = getCodeByPhase(phase_now);
	const code_before4 = getCodeByPhase(phase_before4);
	
	const array_b = code_b.split(',');
	const array_n = code_n.split(',');
	const array_before4 = code_before4.split(',');
	if(array_b.length==0 || array_n.length==0 || array_before4.length==0  ){
		return;
	}
	for(var i=0;i<10;i++){
		const bet_b = array_b[i];
		const bet_n = array_n[i];
		const bet_before4 = array_before4[i];

	 	var d = getYYYYMMDDSS();
		var z = i+1;
	    if(bArray[0]==0 &&  rangeSingExist[bet_b] == rangeSingExist[bet_n]){ //預測到、關卡歸零
	    	 
	    
	    	var suclog =  d + "第"+z+"名"+ " 此期" + phase_now  + "(開獎:"+code_n+")," +  " 預測期數 "+phase_before + "預測("+rangeSingExist[bet_b]+") ,有預測到，下一關，此注不下注" ;
 
	    	saveLog(document.getElementById("user").value,encodeURI(suclog));

	    	bIndex[z] = 0;
		}

	    	//未預測到、投注
			//取前四期的號碼來預測投注
		 
	    	var log =  d + "第"+z+"名"+ " 此期" + phase_now  + "(開獎:"+code_n+")," + " 預測期數 "+phase_before + "預測("+rangeSingExist[bet_b]+") ,沒有預測到 ,下注" ;
	    	log+= "， 參考期數"+  phase_before4 + "("+code_before4+"), 下注號碼"+ sdCode[rangeSingExist[bet_before4]];
			
	    	log+= ",下注金額:"+bArray[bIndex[z]%bArray.length];
	    	var c = bArray[bIndex[z]%bArray.length] == 0 ? '0' : computeCase(bArray,bArray[bIndex[z]%bArray.length]);
	    	var suc = bArray[bIndex[z]%bArray.length] > 1 ? "成功" : "失敗" ;
	    	var betlog =  "預測第"+ (phase_now+1) +"期"+ "，第"+z+"名，號碼("+sdCode[rangeSingExist[bet_before4]]+")"+ "，第"+c+"關"+"下注金額("+bArray[bIndex[z]%bArray.length]+")"+"("+suc+")";
	    	
	    	var betList =   sdCode[rangeSingExist[bet_before4]].toString().split(',');
	    	
	    	
	    	var writeFlag = false;
	    	for(var b=0;b<betList.length;b++){
	    		var code = betList[b];
	    		var sn = i==9 ? 0 : (i+1);
	    		if(bArray[bIndex[z]%bArray.length]  > 0 ){
	    			//reallyBet(gid,sn,code,bArray[bIndex[z]%bArray.length]);
	    			writeFlag = true;
	    			//處理過關的log
	    			let overLog = (phase_now+1) + "@" + sn + "@" + code ; 
	    			saveOverLog(document.getElementById("user").value,encodeURI(overLog),c);
	    		}
	    			
	    	}
	    	if(writeFlag){
	    		
	    		let ssn = i==9 ? 0 : (i+1);
	    		
	    		if(rangeSingExist[bet_before4] =='sin'){ //下單
		    		reallyOtherBet(gid,ssn+'SCs1',bArray[bIndex[z]%bArray.length]);
		    	}
		    	
	    		if(rangeSingExist[bet_before4] =='dou'){ //下雙
	    			reallyOtherBet(gid,ssn+'SCc1',bArray[bIndex[z]%bArray.length]);
		    	}
	    		saveLog(document.getElementById("user").value+"bet",encodeURI(betlog)); //只寫下注成功的log
	    	}
	    		
	    		
	    		
	    	saveLog(document.getElementById("user").value,encodeURI(log));
	    	bIndex[z]++;
	 
		
		
	}
	
}


function reallyOtherBet(gid,betstr,amount){
	$.ajax({ url:  u + "/betBS?user="+document.getElementById("user").value +"&gid="+gid +"&uid="+encodeURI(document.getElementById("uid").value)+"&mid="+ document.getElementById("mid").value+
			"&betStr="+betstr+"&amount="+amount+"&ltype="+ltype,  
     async: false,
     success: function(data) {
    	 
     	 }
    });
	
}

function connectURL(){
	var frameUrl = "http://203.160.143.110/www_new/index_new.php?username="+document.getElementById("user").value+"&usertype=a&langx=zh-cn&mid="+
	document.getElementById("mid").value+"&uid="+document.getElementById("uid").value;
	 
	window.open(
			frameUrl,
			  '_blank' // <- This is what makes it open in a new window.
			);
}