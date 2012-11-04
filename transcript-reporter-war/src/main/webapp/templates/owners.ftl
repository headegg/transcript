<#assign environments = getDictionaryMap("environments")>
<#assign applications = getDictionaryMap("applications")>
${setIncludeUndefinedEnvironments(true)}
<#escape x as x?html>
<div <#if reportId??>id="${reportId}" </#if>class="pivot-table-wrap table-wrap">
<table class="pivot-table confluenceTable">
    <thead>
    <tr>
        <th class="owners confluenceTh">Owners:</th>
        <#list reportableEnvironments as environment>
            <th class="environment-name confluenceTh">${environments[environment].name!environment}</th>
        </#list>
    </tr>
    </thead>
<tbody>
    <#list reportableApplications as application>
    <tr>
    <th class="application-name confluenceTh">${applications[application].name!application}</th>
        <#list reportableEnvironments as environment>
            <#assign name = getProperty(environment, application, "application.owner.name")>
            <#if name.value?? && name.value?length != 0>
                <#assign email = getProperty(environment, application, "application.owner.email")>
                <#if email.value?? && email.value?length != 0>
                    <#assign owner = "<a href=\"mailto:${email.value}\">${name.value}</a>">
                <#else>
                    <#assign owner = name.value>
                </#if>
            <td class="defined-property-value confluenceTd"><#noescape>${owner}</#noescape></div>
            <#else>
            <td class="undefined-property-value confluenceTd"/>
            </#if>
        </#list>
    </tr>
    </#list>
</tbody>
</table>
</div>
</#escape>
