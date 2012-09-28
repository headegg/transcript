<html>
<head>
    <link type="text/css" href="css/start/jquery-ui-1.8.23.custom.css" rel="Stylesheet"/>
    <link type="text/css" href="css/jquery.dataTables_themeroller.css" rel="Stylesheet"/>
    <script type="text/javascript" src="js/query-params.js"></script>
    <script type="text/javascript" src="js/jquery-1.8.0.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui-1.8.23.custom.min.js"></script>
    <script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
    <script type="text/javascript">
        var path = getQueryParameter("path");
        var environment = getQueryParameter("env");
        var application = getQueryParameter("app");
        var url = path + environment + "/" + application;
        $.getJSON(url, function(data) {
            var tableData = []
            $.each(data, function(key, value) {
                tableData.push([key, value]);
            });
            $(document).ready(function() {
                $("table").dataTable({
                    "aaData": tableData,
                    "aoColumns": [
                        {"sTitle": "Key", "sWidth": "20%"},
                        {"sTitle": "Value", "bSortable": false}
                    ],
                    "bStateSave": true,
                    "iCookieDuration": 60 * 60 * 24 * 365,
                    "sCookiePrefix": "config_datatable_",
                    "bJQueryUI": true
                });
            });
        });
        $(document).ready(function() {
            $("h1").text("Configuration of " + application + " on " + environment);
        });
    </script>
</head>
<body>
<h1></h1>
<table>
    <thead>
    <tr>
        <th>Key</th>
        <th>Value</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td></td>
        <td></td>
    </tr>
    </tbody>
</table>
</body>
</html>
