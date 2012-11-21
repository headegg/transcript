<#include "macros.ftl">
<#assign environments = getEnvironments(parameters.environments)>
<#assign applications = getApplications(environments, parameters.applications)>
<#assign namedEnvironments = getAssociations(environments, "environments")>
<#assign namedApplications = getAssociations(applications, "applications")>
<#escape x as x?html>
<div id="${parameters.get("report.id", "owners-table")}" class="owners-table table-wrap">
    <table class="confluenceTable">
        <thead>
        <tr>
            <th class="confluenceTh"></th>
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
                    <td class="owner confluenceTd"><@nameAndEmail class="owner" type="owners" environment=namedEnvironments[environmentName] application=namedApplications[applicationName] key="application.owner.name" /></td>
                </#list>
            </tr>
            </#list>
        </tbody>
    </table>
</div>
</#escape>
