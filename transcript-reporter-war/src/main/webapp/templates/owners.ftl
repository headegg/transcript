<#include "macros.ftl">
${parameters.setIncludeUndefinedEnvironments(true)}
${parameters.setIncludeVersionControlProperties(false)}
<#escape x as x?html>
<div <#if reportId??>id="${reportId}" </#if>class="owners-table table-wrap">
    <table class="owners-table confluenceTable">
        <thead>
        <tr>
            <th class="confluenceTh"></th>
            <#list reportableEnvironments as environment>
                <th class="environment-name confluenceTh"><@label type="environments" id=environment /></th>
            </#list>
        </tr>
        </thead>
        <tbody>
            <#list reportableApplications as application>
            <tr>
                <th class="application-name confluenceTh"><@label type="applications" id=application /></th>
                <#list reportableEnvironments as environment>
                    <td class="owner-name confluenceTd"><@property class="owner-name" environment=environment application=application key="application.owner.name" /></td>
                </#list>
            </tr>
            </#list>
        </tbody>
    </table>
</div>
</#escape>
