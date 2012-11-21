<#escape x as x?html>
<div id="${parameters.get("report.id", "env-app-props")}" class="env-app-props">
    <#list getKeys([parameters.environment], [parameters.application], parameters.keys, false) as key>
        <h1>${key}</h1>
        <pre>${getProperties(parameters.environment, parameters.application, false)[key]}</pre>
    </#list>
</div>
</#escape>
