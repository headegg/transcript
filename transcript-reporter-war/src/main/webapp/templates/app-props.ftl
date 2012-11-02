<#assign environments = getDictionaryMap("environments")>
<#assign applications = getDictionaryMap("applications")>
<#escape x as x?html>
<div class="properties-table-wrap table-wrap">
    <table class="properties-table confluenceTable">
        <thead>
        <tr>
            <th class="application-name confluenceTh">${applications[application].name!application}</th>
            <#list reportableEnvironments as environment>
                <th class="environment-name confluenceTh">${environments[environment].name!environment}</th>
            </#list>
        </tr>
        </thead>
        <tbody>
            <#list reportableKeys as key>
            <tr>
                <th class="property-key confluenceTh">${key}</th>
                <#list reportableEnvironments as environment>
                    <#assign property = getProperty(environment, application, key)>
                    <td class="<#if property.value??><#if property.value?length != 0>defined<#else>blank</#if><#else>undefined</#if>-property-value confluenceTd">${property.value!""}</td>
                </#list>
            </tr>
            </#list>
        </tbody>
    </table>
</div>
</#escape>
