<#include "macros.ftl">
${parameters.setIncludeUndefinedEnvironments(true)}
<#escape x as x?html>
<div <#if reportId??>id="${reportId}" </#if>class="pivot-table table-wrap">
    <table class="confluenceTable">
        <thead>
        <tr>
            <th class="key-names confluenceTh"><#list parameters.keys as key>
                <div class="key-name">${key}</div></#list></th>
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
                    <td class="property-value confluenceTd">
                        <#list parameters.keys as key>
                            <div><@property class="property-value" environment=environment application=application key=key /></div>
                        </#list>
                    </td>
                </#list>
            </tr>
            </#list>
        </tbody>
    </table>
</div>
</#escape>
