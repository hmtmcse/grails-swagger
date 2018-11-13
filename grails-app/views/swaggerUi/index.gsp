<!-- HTML for static distribution bundle build -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Swagger UI</title>
    <style>
    html
    {
        box-sizing: border-box;
        overflow: -moz-scrollbars-vertical;
        overflow-y: scroll;
    }

    *,
    *:before,
    *:after
    {
        box-sizing: inherit;
    }

    body
    {
        margin:0;
        background: #fafafa;
    }
    </style>
    <script>
        window.GrailsSwaggerDefinitionURL = "${com.hmtmcse.gs.GsConfigHolder.swaggerDefinitionUrl}"
    </script>
    <asset:stylesheet src="swagger/swagger-ui.css"/>
    <asset:javascript src="swagger/swagger-ui-bundle.js"/>
    <asset:javascript src="swagger/swagger-ui-standalone-preset.js"/>
    <asset:javascript src="swagger/swagger-loader.js"/>

</head>

<body>
<div id="swagger-ui"></div>
</body>
</html>
