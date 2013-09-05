<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic-el.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean-el.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/iextend-session.tld" prefix="session" %>

<session:constant name="USI" var="USI"/>
<logic:present name='<%= (String)pageContext.getAttribute("USI") %>'>
<table>
<tr class="USI">
  <td><label><bean:message key="label.currentLogin"/></label></td>
  <td>:</td>
  <td><label><session:info name="LoginId"/></label></td>
</tr>
</table>
</logic:present>

<logic:messagesPresent>
<table width="100%" cellpadding="0" cellspacing="0" border="0">
<tr>
  <td><img src="images/blank.gif" width="5" height="10"></td>
</tr>
<tr>
  <td>
  <fieldset width="60%">
  <legend width="60%" class="bodytxt"><bean:message key="label.messages"/></legend>
  <table width="100%" cellpadding="0" cellspacing="0" border="0">
  <tr class="message_display">
    <td><img src="images/blank.gif" width="5" height="5"></td>
  </tr>
  <tr class="message_display">
    <td>
      <html:messages id="error">
        <c:out value='${error}'/>
      </html:messages>
    </td>
  </tr>
  <tr class="message_display">
    <td><img src="images/blank.gif" width="5" height="5"></td>
  </tr>
  </table>
  </fieldset>
  </td>
</tr>
</table>
</logic:messagesPresent>

