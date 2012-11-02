<#assign environments = getDictionaryMap("environments")>
<#assign applications = getDictionaryMap("applications")>
${setIncludeUndefinedEnvironments(true)}
<#escape x as x?html>
<div class="pivot-table-wrap table-wrap">
    <table class="pivot-table confluenceTable">
        <thead>
        <tr>
            <th class="key-names confluenceTh"><#list requiredKeys as key>
                <div class="key-name">${key}</div></#list>
            </th>
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
                    <td class="property-value confluenceTd">
                        <#list requiredKeys as key>
                            <#assign property = getProperty(environment, application, key)>
                            <div class="<#if property.value??><#if property.value?length != 0>defined<#else>blank</#if><#else>undefined</#if>-property-value confluenceTd">${property.value!""}</div>
                        </#list>
                    </td>
                </#list>
            </tr>
            </#list>
        </tbody>
    </table>
</div>
</#escape>
