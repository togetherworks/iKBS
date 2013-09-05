function get_cookie1(Name1)
{
    //Get cookie routine by Shelley Powers 
    var search1 = Name1 + "="
    var returnvalue1 = "";
    if (document.cookie.length > 0)
    {
        offset1 = document.cookie.indexOf(search1)
        // if cookie exists
        if (offset1 != -1)
        { 
            offset1 += search1.length
            // set index of beginning of value
            end1 = document.cookie.indexOf(";", offset1);
            // set index of end of cookie value
            if (end1 == -1)
                end1 = document.cookie.length;
            returnvalue1=unescape(document.cookie.substring(offset1, end1))
        }
    }
    
    return returnvalue1;
}
    
var foldercontent1array=new Array()
var c1=0

if (ns61)
{
    for (i=0; i<document.getElementsByTagName("UL").length; i++)
    {
        if (document.getElementsByTagName("UL")[i].id=="foldinglist1")
        {
            foldercontent1array[c1]=document.getElementsByTagName("UL")[i]
            c1++
        }
    }
}

if (document.forms["indexform"].apple.value != ' ')
{
    if (get_cookie1(window.location.pathname) != '')
    {
        var openresults1=get_cookie1(window.location.pathname).split(" ")
        for (i=0; i<openresults1.length; i++)
        {
            if (ns61)
            {
                foldercontent1array[openresults1[i]].style.display=''
                foldercontent1array[openresults1[i]].previousSibling.previousSibling.style.listStyleImage="url(/ikbs/image/folder_open.gif)"
            }
            else
            {
                foldinglist1[openresults1[i]].style.display=''
                document.all[foldinglist1[openresults1[i]].sourceIndex -1].style.listStyleImage="url(/ikbs/image/folder_open.gif)"
            }
        }
    }
}

if (ns61||ie41)
{
    if(defined(foldinglist1)) {
      var nodelength1=ns61? c1 : foldinglist1.length
      var nodes1=new Array(nodelength1)
      var openones1=''
    }
}

function checkit1()
{
    for (i=0; i <= nodelength1-1; i++)
    {
        if ((ns61&&foldercontent1array[i].style.display=='')||(ie41&&foldinglist1[i].style.display==''))
            openones1=openones1 + " " + i
    }
    document.cookie=window.location.pathname+"="+openones1
}

if (ns61||ie41)
    window.onunload=checkit1