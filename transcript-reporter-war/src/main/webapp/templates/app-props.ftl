<#include "macros.ftl">
<#assign environments = getEnvironments(parameters.environments)>
<#if !parameters.get("undefined", false)>
    <#assign environments = getEnvironmentsWithApplications(environments, [parameters.application], parameters.keys, parameters.includeVersionControlProperties)>
</#if>
<#assign keys = getKeys(environments, [parameters.application], parameters.keys, parameters.includeVersionControlProperties)>
<#assign namedEnvironments = getAssociations(environments, "environments")>
<#assign applicationName = getPrettyName(parameters.application, "applications")>
<#escape x as x?html>
<div id="${parameters.get("report.id", "properties-table")}" class="properties-table table-wrap">
    <table class="confluenceTable">
        <thead>
        <tr>
            <th class="application-name confluenceTh">${applicationName}</th>
            <#list namedEnvironments?keys as environmentName>
                <th class="environment-name confluenceTh">${environmentName}</th>
            </#list>
        </tr>
        </thead>
        <tbody>
            <#list keys as key>
                <#assign rowState = "consistent">
                <#attempt>
                    <#assign firstValue = getProperties(environments[0], parameters.application, parameters.includeVersionControlProperties)[key]>
                    <#assign firstValueDefined = true>
                    <#recover>
                        <#assign firstValueDefined = false>
                </#attempt>
                <#list environments as environment>
                    <#attempt>
                        <#assign value = getProperties(environment, parameters.application, parameters.includeVersionControlProperties)[key]>
                        <#assign changed = !firstValueDefined || value != firstValue>
                        <#recover>
                            <#assign changed = firstValueDefined>
                    </#attempt>
                    <#if changed>
                        <#assign rowState = "inconsistent">
                        <#break>
                    </#if>
                </#list>
            <tr class="${rowState}-property-values">
                <th class="property-key confluenceTh">${key}</th>
                <#list namedEnvironments?keys as environmentName>
                    <td class="property-value confluenceTd"><@property class="property-value" environment=namedEnvironments[environmentName] application=parameters.application key=key /></td>
                </#list>
            </tr>
            </#list>
        </tbody>
    </table>
</div>
</#escape>
