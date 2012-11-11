<#escape x as x?html>
<div <#if reportId??>id="${reportId}" </#if>class="env-app-props">
    <#list reportableKeys as key>
        <h1>${key}</h1>
        <pre>${getProperties(parameters.environment, parameters.application)[key]}</pre>
    </#list>
</div>
</#escape>
