/*
 * menuExpandable2.js - implements an expandable menu based on a HTML list
 * Author: Dave Lindquist (dave@gazingus.org)
 */

if (!document.getElementById)
    document.getElementById = function() { return null; }

function isExpanded(menuId) {
    var cookieName = window.location.pathname + "=";
    var returnvalue = "";

    if (document.cookie.length > 0) {
        offset = document.cookie.indexOf(cookieName)
        // if cookie exists
        if (offset != -1) {
            offset += cookieName.length
            // set index of beginning of value
            end = document.cookie.indexOf(";", offset);
            // set index of end of cookie value
            if (end == -1) end = document.cookie.length;
            returnvalue = unescape(document.cookie.substring(offset, end))
        }
    }

    if (returnvalue!="") {
        if (returnvalue.indexOf(menuId) != -1) return true;
    }
    return false;
}

function initializeMenu(menuId, actuatorId) {
    var menu = document.getElementById(menuId);
    var actuator = document.getElementById(actuatorId);

    if (menu == null || actuator == null) return;

    if (window.opera) return; // I'm too tired

    actuator.parentNode.style.backgroundImage = "url(../images/plus.gif)";
    actuator.onclick = function() {
        var display = menu.style.display;

        if (display == "block") {
            this.parentNode.style.backgroundImage = "url(../images/plus.gif)";
            menu.style.display = "none";
        } else {
            this.parentNode.style.backgroundImage = "url(../images/minus.gif)";
            menu.style.display = "block";
        }
        return false;
    }

    // expand if necessary
    if (isExpanded(menuId)) {
        menu.style.display = "block";
        menu.parentNode.style.backgroundImage = "url(../images/minus.gif)";
    }

    // remove if there are no child nodes
    if (actuator.parentNode.children(1).children.length == 0) {
        actuator.parentNode.removeNode(true);
    }
}

function saveCookie() {
    var cookieName = window.location.pathname + "=";
    var saveList = "";

    for (i=0; i<document.getElementsByTagName("UL").length; i++){
        currUL = document.getElementsByTagName("UL")[i];
        if ((currUL.className=="menu") && (currUL.style.display=="block")) {
            saveList += currUL.id;
        }
    }
    document.cookie = cookieName+saveList;
}

window.onunload = saveCookie;





