//Smart Folding Menu tree- By Dynamic Drive (rewritten 03/03/02)
//For full source code and more DHTML scripts, visit http://www.dynamicdrive.com
//This credit MUST stay intact for use
    
var head1 = "display:''";
img11 = new Image();
img11.src = "/ikbs/image/folder_closed.gif";
img21 = new Image();
img21.src = "/ikbs/image/folder_open.gif";
        
var ns61 = (document.getElementById && !document.all);
var ie41 = (document.all && navigator.userAgent.indexOf("Opera")==-1);

if(ie41||ns61) {
    document.onclick = checkcontained1;
}
        
function checkcontained1(e1)
{
    var iscontained1=0
    cur1=ns61? e1.target : event.srcElement
    i1=0
    if (cur1.id=="foldheader1")
        iscontained1=1
    else
        while (ns61&&cur1.parentNode||(ie41&&cur1.parentElement))
        {
            if (cur1.id=="foldheader1"||cur1.id=="foldinglist1")
            {
                iscontained1=(cur1.id=="foldheader1")? 1 : 0;
                break;
            }
            cur1=ns61? cur1.parentNode : cur1.parentElement;
        }
    
    if (iscontained1)
    {
        var foldercontent1=ns61? cur1.nextSibling.nextSibling : cur1.all.tags("UL")[0]
        if (foldercontent1.style.display=="none")
        {
            foldercontent1.style.display="";
            cur1.style.listStyleImage="url(/ikbs/image/folder_open.gif)";
        }
        else
        {
            foldercontent1.style.display="none";
            cur1.style.listStyleImage="url(/ikbs/image/folder_closed.gif)";
        }
    }
}
        
function popUp(URL) {
    day = new Date();
    id = day.getTime();
    eval("page" + id + " = window.open(URL, '" + id + "', 'toolbar=0,scrollbars=0,location=0,statusbar=0,menubar=0,resizable=1,width=580,height=380,left = 312,top = 184');");
	history.go(-1);
	
}


function wopen(url, name, w, h)
{
	w += 80;
	h += 150;
 	var win = window.open(url,name, 'width=' + w + ', height=' + h + ', ' +' menubar=no, ' + 'status=no, toolbar=no,location=0,scrollbars=1,resizable=1,left = 200,top = 84');
 win.resizeTo(w, h);
 win.focus();
}

function CreateExcelSheet()
{

var x = myTable.rows;
var xls = new ActiveXObject("Excel.Application");
xls.visible= true;
var objWB = xls.Workbooks.Add();
var objWS = objWB.ActiveSheet;
for(i=0;i<x.length;i++) {
var y = x[i].cells;
for(j=0;j<y.length;j++){
xls.Cells(i+1,j+1).Value = y[j].innerText;
xls.Cells(i+1,j+1).WrapText=true;
}
}
//objWS.Range("A1", "Z1").EntireColumn.AutoFit();
xls.Cells.EntireColumn.AutoFit();
xls.Cells.EntireRow.AutoFit();
//objWS.Columns("A"+":"+ "Z").ColumnWidth = 22;

}