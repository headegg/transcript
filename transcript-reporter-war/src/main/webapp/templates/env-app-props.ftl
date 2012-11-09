<#escape x as x?html>
<div <#if reportId??>id="${reportId}" </#if>class="env-app-props">
    <#list reportableKeys as key>
        <#assign value = getProperties(parameters.environment, parameters.application)[key]>
        <h1>${key}</h1>
        <pre>${value}</pre>
    </#list>
</div>
</#escape>
