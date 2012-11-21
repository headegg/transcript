<#include "macros.ftl">
<#assign environments = getEnvironments(parameters.environments)>
<#assign applications = getApplications(environments, parameters.applications)>
<#if !parameters.get("undefined", false)>
    <#assign environments = getEnvironmentsWithApplications(environments, applications, parameters.keys, parameters.includeVersionControlProperties)>
    <#assign applications = getApplicationsWithEnvironments(environments, applications, parameters.keys, parameters.includeVersionControlProperties)>
</#if>
<#assign keys = getKeys(environments, applications, parameters.keys, parameters.includeVersionControlProperties)>
<#assign namedEnvironments = getAssociations(environments, "environments")>
<#assign namedApplications = getAssociations(applications, "applications")>
<#escape x as x?html>
<div id="${parameters.get("report.id", "pivot-table")}" class="pivot-table table-wrap">
    <table class="confluenceTable">
        <thead>
        <tr>
            <th class="key-names confluenceTh"><#list keys as key>
                <div class="key-name">${key}</div></#list></th>
            <#list namedEnvironments?keys as environmentName>
                <th class="environment-name confluenceTh">${environmentName}</th>
            </#list>
        </tr>
        </thead>
        <tbody>
            <#list namedApplications?keys as applicationName>
            <tr>
                <th class="application-name confluenceTh">${applicationName}</th>
                <#list namedEnvironments?keys as environmentName>
                    <td class="property-value confluenceTd">
                        <#list keys as key>
                            <div><@property class="property-value" environment=namedEnvironments[environmentName] application=namedApplications[applicationName] key=key /></div>
                        </#list>
                    </td>
                </#list>
            </tr>
            </#list>
        </tbody>
    </table>
</div>
</#escape>
