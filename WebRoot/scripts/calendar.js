<!--// Hide Javascript
//-------------------------------------
// Javascript Date / Date Time picker
// Author: Ng Kim Boon
//-------------------------------------
var defaultFormat = "dd/MM/yyyy";
var dateFormat = defaultFormat;
var days = new Array("Sun","Mon","Tue","Wed","Thu","Fri","Sat");
var months = new Array( "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" );
var today = new Date();
today = new Date( today.getFullYear(), today.getMonth(), today.getDate() );
var currMonth;

//deprecated function. Use showCalendar instead.
function show_calendar(str_target) {
  var targetForm = "document.forms['" + str_target.substring(0, str_target.indexOf(".")) + "']";
  var targetField = "elements['" + str_target.substring(str_target.indexOf(".")+1) + "']";
  showCalendar( eval(targetForm + "." + targetField ) );
}

function showCalendar(field, format, year, month ) {
  dateFormat = format!=null? format : defaultFormat;
  initCurrentMonth(field, year, month);
  render( field );
}

function showDateTime(field, format, year, month, hours, minutes ) {
  dateFormat = format!=null? format : defaultFormat + " hh:mm";
  initCurrentMonth(field, year, month);
  render( field, true );
}

function initCurrentMonth( field, y, m ) {
  var date = new DateFormat( dateFormat ).parseDate( field.value );
  if (date==null) date=today;
  if ( m==null || m<=0) m=date.getMonth() + 1;
  if ( y==null ) y=date.getFullYear();
  currMonth = new Month( y, m );
}

// ----DateFormat object start---
function DateFormat( pattern ) {
  this.pattern = pattern;
  this.format = doFormat;
  this.parseDate = doParseDate;
}

function doFormat( date ) {
    var f = this.pattern;
    var y=date.getFullYear();
    var m=date.getMonth()+1;
    var d=date.getDate();
    var h=date.getHours();
    var mi=date.getMinutes();
    var months = new Array( "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" );
    var formatters = new Array( /yyyy/, y, /yy/, y.toString().substr(2), /y/, y,
                                /MMM/, months[m-1], /MM/, (100+m).toString().substr(1), /M/, m,
                                /dd/, (100+d).toString().substr(1), /d/, d,
                                /hh/, (100+h).toString().substr(1), /h/, h,
                                /mm/, (100+mi).toString().substr(1), /m/, mi );
    for (var i=0; i<formatters.length; i=i+2 ) f = f.replace( formatters[i], formatters[i+1] );
    return f;
}

function doParseDate( dateStr ) {
    if (dateStr==null || dateStr=="") return null;
    var formatters = new Array( new Array("yyyy","yy","y"), new Array("MM","M"), new Array("dd","d"),
                                new Array("hh","h"), new Array("mm","m"), new Array("ss","s") );
    var values = new Array(0,0,0,0,0,0,0);
    for (var i=0; i<formatters.length; i++ ) {
        for (var j=0; j<formatters[i].length; j++ ) {
            var index = this.pattern.indexOf( formatters[i][j] );
            if (index>-1) {
                values[i] = dateStr.substr( index, formatters[i][j].length ) - 0;
                if (isNaN(values[i])) values[i]=0;
            	break;
            }
		}
	}
    if (values[0]==0 || values[1]==0 || values[2]==0) return null;
    return new Date(values[0], values[1]-1, values[2], values[3], values[4], values[5], values[6] );
}
// ----DateFormat object end---

function Month( y, m ) {
	// properties
	this.year = y - 0;	// minus 0 to convert it to number
	this.month = m - 0;
	this.name = months[m-1];
    this.length = new Date( this.year, this.month, 0).getDate();
	this.firstDay = new Date(this.year, this.month-1, 1).getDay();
    this.add = monthAdd;
    this.getDate = monthGetDate;
}

function monthAdd( n ) {
  var d = new Date( this.year, this.month-1 + n, 1);
  return new Month( d.getFullYear(), d.getMonth()+1 );
}

function monthGetDate( day ) {
    return new Date( this.year, this.month-1, day );
}

function render(dateField, timeRequired) {
var w= window.open( "", "calendar","width=290,height=230,resizable=1,status=0,menubar=0,scrollbars=0,fullscreen=0");
var doc = w.document;

doc.writeln('<html>');
doc.writeln('<head>');
doc.writeln('<title>Calendar</title>');
doc.writeln('<style type="text/css">');
doc.writeln('body {');
doc.writeln('	font-size : 9pt;');
doc.writeln('	font-family : Arial;');
doc.writeln('	font-weight : bold;');
doc.writeln('	font-style : normal;');
doc.writeln('}');
doc.writeln('td {');
doc.writeln('	font-size : 9pt;');
doc.writeln('	font-family : Arial;');
doc.writeln('	font-weight : bold;');
doc.writeln('	font-style : normal;');
doc.writeln('}');
doc.writeln('.TABLEHEAD {');
doc.writeln('	color : white;');
doc.writeln('	background-color : #117ACF;');
doc.writeln('}');

doc.writeln('.DAYS {');
doc.writeln('	color : black;');
doc.writeln('	background-color : #87cefa;');
doc.writeln('}');

doc.writeln('.NONE {');
doc.writeln('	color : lightgrey;');
doc.writeln('	background-color : white;');
doc.writeln('}');

doc.writeln('.BEFORETODAY {');
doc.writeln('	font-weight : normal;');
doc.writeln('	color : #000080;');
doc.writeln('	background-color : aliceblue;');
doc.writeln('	line-height: 20px;');
doc.writeln('}');

doc.writeln('.AFTERTODAY {');
doc.writeln('	font-weight : normal;');
doc.writeln('	color : #000080;');
doc.writeln('	background-color : #DDEEFF;');
doc.writeln('	line-height: 20px;');
doc.writeln('}');

doc.writeln('.TODAY {');
doc.writeln('	color : red;');
doc.writeln('	background-color : pink;');
doc.writeln('	line-height: 20px;');
doc.writeln('}');
doc.writeln('</style>');

doc.writeln('<script language="Javascript">');
doc.writeln('var dateField;');
doc.writeln('var dateFormat;');
doc.writeln('function init( field, format ) {');
doc.writeln('  dateField = field;');
doc.writeln('  dateFormat = format;');
doc.writeln('}');
doc.writeln('function returnDate(y, m, d) {');
doc.writeln('  // Put the date value back in the underlying form in the right format.');
doc.writeln('  if(window.opener){');
doc.writeln('    if (calForm.hour) dateField.value = dateFormat.format( new Date(y, m-1, d, calForm.hour.value-0, calForm.minute.value-0 ) )');
doc.writeln('    else dateField.value = dateFormat.format( new Date(y, m-1, d) ); ');
doc.writeln('  }');
doc.writeln('  self.close();');
doc.writeln('}');
doc.writeln('function gotoMonth(y, m) {');
doc.writeln('  window.opener.' + (timeRequired ? 'showDateTime' : 'showCalendar') + '(dateField, dateFormat.pattern, y, m)' );
doc.writeln('}');
doc.writeln('</script>');
doc.writeln('</head>');

doc.writeln('<body>');
doc.writeln('<form name="calForm" onSubmit="gotoMonth(calForm.y.value, calForm.m.value);return false;">');
doc.writeln('<table cellspacing="0" width="245" align="center">');
doc.writeln('<td bgcolor="#117ACF">');
doc.writeln('<table border="0" cellspacing="1" width="245">');

var prevMonth = currMonth.add( -1 );
var nextMonth = currMonth.add( 1 );

doc.writeln('<tr class="TABLEHEAD" align="center">');
doc.writeln('<td colspan="1"><a class="TABLEHEAD" href="javascript:gotoMonth(' + prevMonth.year + ', ' + prevMonth.month + ')">Prev</a></td>');
doc.writeln('<td colspan="5" class="TABLEHEAD">');

//doc.writeln('<select name="m" onChange="' + showCalendar + '(dateField, null, this.value, calForm.y.value)">');
doc.writeln('<select name="m" onChange="gotoMonth(calForm.y.value, calForm.m.value);return false;">');
for (var i=1; i<13; i++ ) {
	doc.writeln('<option value="' + i + '"' + ((i==currMonth.month)? ' selected="true">' : '>') + months[i-1] + '</option>');
}
doc.writeln('</select>');
doc.writeln('<input type="text" size="4" maxlength="4" name="y" value="' + currMonth.year + '">');
doc.writeln('</td>');

doc.writeln('<td colspan="1"><a class="TABLEHEAD" href="javascript:gotoMonth(' + nextMonth.year + ', ' + nextMonth.month + ')">Next</a></td>');
doc.writeln('</tr>');

// writes the days of the week
doc.writeln('<tr align="center">');
for(var d=0;d<7;d++){
	doc.writeln('<td width="14%" class="DAYS"><b>&nbsp;' + days[d] + '&nbsp;</b></td>');
}
doc.writeln('</tr>');

var daycounter = 1;
var cls = "";
// allows month to possibly span over 6 weeks
for(var i=0; i<6; i++){
    // Do not continue if already passed last day
    if ( daycounter > currMonth.length ) break;

    // if we have not exceeded number of days in month
    doc.writeln('<tr align="center">');

    // display each day of the week
    for(var j=0;j<7;j++){
        // First choose different style for different period.

        // Not yet the first day of the month, or it has exceeded the last day in the month.
        if( (i==0 && j<currMonth.firstDay) || daycounter>currMonth.length ) cls = 'class="NONE"';

        // Past date
        else if(currMonth.getDate(daycounter)<today) cls = 'class="BEFORETODAY"';

        // Future date
        else if(currMonth.getDate(daycounter)>today) cls = 'class="AFTERTODAY"';

        // Current date
        else cls = 'class="TODAY"';

        doc.writeln('<td ' + cls + '>');
        if (cls!='class="NONE"') {
            doc.writeln('<a href="javascript:returnDate(' + currMonth.year + "," + currMonth.month + ',' + daycounter + ')" ' + cls + '>' + daycounter + '</a>');
            daycounter++;
        }
        doc.writeln('</td>');

    }//for
    doc.writeln('</tr>');
}//for

doc.writeln('</table>');
if (timeRequired) {
	doc.writeln('<tr><td align="right">');
	doc.writeln('Time: <select name="hour">');
	for (var i=0; i<24; i++) {
	  doc.writeln( '<option value="' + i + '">' + (100+i).toString().substring(1) + '</option>');
	}
	doc.writeln('</select>:');
	doc.writeln('<select name="minute">');
	for (var i=0; i<60; i++) {
	  doc.writeln( '<option value="' + i + '">' + (100+i).toString().substring(1) + '</option>');
	}
	doc.writeln('</select>');
	doc.writeln('</td></tr>');
}
doc.writeln('</table>');
doc.writeln('</form>');
doc.writeln('</body>');
doc.writeln('</html>');
doc.close();
w.init( dateField, new DateFormat(dateFormat) );
}

//-->