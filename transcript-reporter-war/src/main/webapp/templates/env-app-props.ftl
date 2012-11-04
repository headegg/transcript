<#escape x as x?html>
<div <#if reportId??>id="${reportId}" </#if>class="env-app-props">
    <#list reportableKeys as key>
        <#assign property = getProperty(environment, application, key)>
        <h1>${key}</h1>
        <pre>${property.value!""}</pre>
    </#list>
</div>
</#escape>
