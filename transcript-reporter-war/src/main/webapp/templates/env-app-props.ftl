<#escape x as x?html>
    <#list reportableKeys as key>
        <#assign property = getProperty(environment, application, key)>
    <h1>${key}</h1>
    <pre>${property.value!""}</pre>
    </#list>
</#escape>
