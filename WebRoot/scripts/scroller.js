divtextb ="<div id=d";
divtev1=" onmouseover=\"mdivmo(";
divtev2=")\" onmouseout =\"restime(";
divtev3=")\" onclick=\"butclick(";
divtev4=")\"";
/*	
divtexts = " style=\"position:absolute;visibility:hidden;width:"+textwt+";background:#FFFFFF; COLOR: #333333; left:0; top:0; FONT-FAMILY: Arial, Helvetica, sans-serif; FONT-SIZE: 9pt; FONT-STYLE: normal; FONT-WEIGHT: normal; TEXT-DECORATION: none; margin:0px; overflow:hidden-x; LINE-HEIGHT: 10pt; text-align:left;padding:0px; cursor:'default';\">";
titlefont= " style=\"position:relative;background: #FFFFFF; COLOR: #006666; width:"+textwt+"; FONT-FAMILY: Arial, Helvetica, sans-serif; FONT-SIZE: 9pt; FONT-STYLE: normal; FONT-WEIGHT: bold; TEXT-DECORATION: none; LINE-HEIGHT: 10pt; text-align:left;padding:0px;\"";
*/

var inoout=false;
var dahayok=true;
var spage;
var direction;

var uzunobj=null;
var uzuntop=0;
var toplay=0;

var Id=0;
var Id1=0;

function ManualScroll(arrow)
{
	direction=arrow;
	inoout=true;
	
	window.clearTimeout(Id1);
	Id1=0;
	
	PerformScroll();
}

function PerformScroll()
{
	if(inoout==true)
	{
		if(direction=="down")
		{
			uzuntop++;
        }
		else
		{
			uzuntop--;
		}

		for(i=0;i<total_activities;i++)
		{
			objd=document.getElementById('d'+i);
			objd.style.top=""+(uzuntop+(i*activityht))+"px";
		}
		
		for(j=0;j<display_no;j++)
		{
			if(uzuntop<(-1*(total_activities-(display_no-j))*activityht))
			{
				objd=document.getElementById('d'+j);
				objd.style.top=""+(uzuntop+(total_activities*activityht)+activityht*j)+"px";	
			}
        }

        if(direction=="down")
		{
			if(uzuntop>0)
			{        
            	uzuntop=(-1*total_activities*activityht);               
			}
		}
		else
		{
			if(uzuntop<(-1*total_activities*activityht))
			{        
            	uzuntop=0;               
			}
		}
		Id=window.setTimeout("PerformScroll();",fastscroll);
	}
	else
	{
		window.clearTimeout(Id);
		Id=0;
	}
} 
 
function CeaseScroll(){ 
    inoout=false;
	setTimeout('dotrans()',scrolltime);
} 

function mdivmo(gnum)
{
	inoout=true;

	if((linka[gnum].length)>2)
	{
		objd=document.getElementById('d'+gnum);
		objd2=document.getElementById('hgd'+gnum);

		objd.style.color="#0097BE";
		objd2.style.color="#036F9C";

		objd.style.cursor='pointer';
		objd2.style.cursor='pointer';
		window.status=""+linka[gnum];
	}
}

function restime(gnum2)
{
	inoout=false;
	
	objd=document.getElementById('d'+gnum2);
	objd2=document.getElementById('hgd'+gnum2);

	objd.style.color="#333333";
	objd2.style.color="#006666";

	window.status="";
}

function butclick(gnum3)
{
	if(linka[gnum3].substring(0,11)=="javascript:")
	{
		eval(""+linka[gnum3]);
	}
	else
	{
		if((linka[gnum3].length)>3)
		{
        	if((trgfrma[gnum3].indexOf("_parent")>-1))
			{
				eval("parent.window.location='"+linka[gnum3]+"'");
			}
			else if((trgfrma[gnum3].indexOf("_top")>-1))
			{
				eval("top.window.location='"+linka[gnum3]+"'");
			}
			else
			{
				window.open(''+linka[gnum3],''+trgfrma[gnum3]);
			}
		}
	}
}

function dotrans()
{
	if(inoout==false)
	{
		dahayok=false;
		uzuntop--;

		for(i=0;i<total_activities;i++)
		{
			objd=document.getElementById('d'+i);
			objd.style.top=""+(uzuntop+(i*activityht))+"px";
			if((uzuntop+(i*activityht))==4){dahayok=true;}
		}
		
		for(j=0;j<display_no;j++)
		{
			if(uzuntop<(-1*(total_activities-(display_no-j))*activityht))
			{
				objd=document.getElementById('d'+j);
				objd.style.top=""+(uzuntop+(total_activities*activityht)+activityht*j)+"px";
				if((uzuntop+(i*activityht))==4){dahayok=true;}	
			}
        }

		if(uzuntop<(-1*(total_activities)*activityht))
		{        
            uzuntop=0;               
		}
	}

	if(dahayok==true)
	{
		Id1=window.setTimeout('dotrans()',pausetime);
	}
	else
	{
		Id1=window.setTimeout('dotrans()',scrolltime);
	}
}

function initte2()
{
	toplay=4;
	for(i=0;i<total_activities;i++)
	{
		objd=document.getElementById('d'+i);
		objd.style.visibility="visible";
		objd.style.top=""+(toplay+(activityht*i))+"px";
		objd.style.left=""+cellpad+"px";
    }
    uzuntop=5;
	dotrans();
}

function initte()
{
	i=0;
    innertxt="";
                	
	for(i=0;i<total_activities;i++)
	{  		
		innertxt=innertxt+""+divtextb+i+""+divtev1+i+divtev2+i+divtev3+i+divtev4+divtexts+"<div id=\"hgd"+i+"\""+titlefont+">"+datea[i]+"<br></div>"+titlea[i]+"</div>";
    } 
       
	spage=document.getElementById('spagens');
	spage.innerHTML=""+innertxt;
    setTimeout('initte2()', 0);
}

window.onload=initte;
