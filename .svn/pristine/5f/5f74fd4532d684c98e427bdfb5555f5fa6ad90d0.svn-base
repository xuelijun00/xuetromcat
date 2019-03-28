function exportDataToCSV(filename,dataArray,title,column){
	var obj = {
		title:title,
		items:dataArray
	};
	var json = JSON.stringify(obj);
	JSONToCSVConvertor(json, 1,filename,column);
}
function JSONToCSVConvertor(JSONData, ShowLabel,filename,column) {     
	//If JSONData is not an object then JSON.parse will parse the JSON string in an Object
	var arrData = typeof JSONData != 'object' ? JSON.parse(JSONData) : JSONData;
	var CSV = '';    
	//This condition will generate the Label/Header
	if (ShowLabel) {
	    var row = "";
	    //This loop will extract the label from 1st index of on array
	    /*for (var index in arrData.items[0]) {
	        //Now convert each value to string and comma-seprated
	        row += index + ',';
	    }*/
	    for(var i=0;i<arrData.title.length;i++){
	    	row += arrData.title[i] + ',';
	    }
	    row = row.slice(0, -1);
	    //append Label row with line break
	    CSV += row + '\r\n';
	}
	//1st loop is to extract each row
	for (var i = 0; i < arrData.items.length; i++) {
	    var row = "";
	    //2nd loop will extract each column and convert it in string comma-seprated
	    if(column && column != null && column.length > 0){
	    	for (var j = 0; j < column.length; j++) {
		    	row += '"' + (arrData.items[i][column[j]]+"").replace(/(<([^>]+)>)/ig, '') + '",';
		    }
	    }else{
	    	for (var index in arrData.items[i]) {
		    	row += '"' + (arrData.items[i][index]+"").replace(/(<([^>]+)>)/ig, '') + '",';
		    }
	    }
	    row.slice(0, row.length - 1);
	    //add a line break after each row
	    CSV += row + '\r\n';
	}
	if (CSV == '') {        
	    alert("Invalid data");
	    return;
	}   
	/*
	 * 
	 * FORCE DOWNLOAD
	 * 
	 */
	//this trick will generate a temp "a" tag
	var link = document.createElement("a");    
	link.id="lnkDwnldLnk";

	//this part will append the anchor tag and remove it after automatic click
	document.body.appendChild(link);

	var csv = CSV;  
	blob = new Blob([csv], { type: 'text/csv' }); 
	
	var myURL = window.URL || window.webkitURL;
	
	var csvUrl = myURL.createObjectURL(blob);
	jQuery("#lnkDwnldLnk").attr({
	    'download': filename,
	    'href': csvUrl
	}); 
	jQuery('#lnkDwnldLnk')[0].click();    
	document.body.removeChild(link);
}
